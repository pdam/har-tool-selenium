package com.blazemeter.mvnsese.exec;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.browsermob.proxy.ProxyServer;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.server.handler.Status;
import org.openqa.selenium.server.htmlrunner.HTMLTestResults;

import au.com.bytecode.opencsv.CSVReader;

import com.blazemeter.harutil.PerformanceTiming;
import com.blazemeter.mvnsese.model.CSVSuite;
import com.blazemeter.mvnsese.model.CSVTest;
import com.blazemeter.mvnsese.model.Command;
import com.thoughtworks.selenium.Selenium;

public class CSVRunner implements Callable<SuiteResult> {

    private static Logger clog = Logger.getLogger("com.safeway.maven.selenese.exec.console");
    private static String VERSION = "";
    private static String REVISION = "";
    private static AtomicBoolean consoleInit = new AtomicBoolean(false);

    static {
        try {
            InputStream version = SuiteRunner.class.getResourceAsStream("/VERSION.txt");
            if (version != null) {
                Properties p = new Properties();
                p.load(version);
                VERSION = p.getProperty("selenium.core.version");
                REVISION = p.getProperty("selenium.core.revision");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static final String SUITE_SUMMARY =
            "<table id=\"suiteTable\" class=\"selenium\" border=\"1\" cellpadding=\"1\" cellspacing=\"1\"><tbody>\n"
            + "<tr class=\"title {0}\"><td><b>{1}</b></td></tr>{2}\n"
            + "</tbody></table>";
    static Map<String, CommandExecutor> executorMap = buildExecutorMap();
    static Map<String, WebDriverProfileFactory> profileMap = buildProfileMap();
	private static Status testStatus;
	private static String  cmdHtml;
    ExecContext execCtx;
    String  csvFile;
    File reportFile;
    public RemoteWebDriver wdriver;
    private static CSVTest test;
    CSVReader  reader;
	private File suiteFile;
	private ProxyServer proxy;
    public CSVRunner(String  csv, File reportFile, ExecContext execCtx, WebDriver  driver ) {
        this.execCtx = execCtx;
	try {
		this.reader  =  new  CSVReader(new FileReader(csv));
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
	this.reportFile = reportFile;
	this.csvFile  =  csv;
        this.wdriver = (RemoteWebDriver) driver;
    }




    public SuiteResult call(ProxyServer proxy, WebDriver  driver  ) throws Exception {
        
        WebDriverProfileFactory factory = profileMap.get(driver);
        if (factory == null) {
        	((FirefoxDriver) driver).getCapabilities();
        }
        StringBuilder log = new StringBuilder();
        List<TestResult> testResults = new ArrayList<TestResult>();
        long maxTestTime = 300000 ;
        long startTime = System.currentTimeMillis();
        if (clog.isLoggable(Level.INFO) && consoleInit.compareAndSet(false, true) && System.console() != null) {
            System.console().printf("Executing test %65s", "");
        }
	
        String [] nextLine;
        while ((nextLine =this.reader.readNext()) != null)  {
                int  numLines = nextLine.length ;
                System.out.println("Number of Data Items: " + numLines);
                System.out.println("     nextLine[" + 0 + "]:  " + nextLine[0]);
        		proxy.newHar(nextLine[0]);
            	new PerformanceTiming(driver , proxy.getHar());
            	List<CommandResult> cmdResults = new ArrayList<CommandResult>();
            	TestResult testResult = new TestResult(test, cmdResults);
               	Map<String, Object> testCtx = new HashMap<String, Object>();
           	    testCtx.put(CommandExecutor.TIMEOUT, execCtx.getTimeout()); 
               	for ( int  i  = 0  ;  i < numLines-1  ;i++ ) 
               	{
               		URL  u   =  new  URL(nextLine[i]);
               		String  hostName  =  u.getHost();
               		String  pathName  =  u.getPath();
               		Command executor =    new Command(executorMap.get(u));
                	if (nextLine[i] != null) {
                		Selenium selenium = new WebDriverBackedSelenium(this.wdriver,   hostName  );
                  		log.append(String.format("info: Executing: | open | %s | %s | \n", hostName  , pathName));
                    	executor.execute(selenium, hostName,  pathName );
                    	log.append(String.format("*****************\n%s\n%s\n*****************\n", selenium.getLocation(), selenium.getHtmlSource()));
                    	testResults.add(testResult);
           		 		String harName= hostName+".har";
						proxy.getHar().writeTo(new File((harName)));
           		 		if (System.currentTimeMillis() - startTime > maxTestTime) {
              				  	log.append(String.format("error: max test time %d exceeded\n", execCtx.getMaxTestTime()));
              				  	testResult.setResult(Result.FAILED);
              		  	}
				break;
            		}
       		 }
            
	}

        long duration = System.currentTimeMillis() - startTime;
        return generateReport(SeleneseSuite(this.csvFile), testResults, log.toString(), duration, reportFile);
    }




    private CSVSuite SeleneseSuite(String csvFile2) {
		// TODO Auto-generated method stub
		return null;
	}




	static SuiteResult generateReport(CSVSuite suite, List<TestResult> results, String log, long duration, File reportFile) throws Exception {
        int totalTests = 0;
        int testsPassed = 0;
        int testsFailed = 0;
        int commandsPassed = 0;
        int commandsFailed = 0;
        int commandsError = 0;
        String suiteResult = "status_passed";

        StringBuilder suiteIndex = new StringBuilder();
        List<String> testTables = new ArrayList<String>();
        for (int i = 0; i < results.size(); i++) {
            TestResult testResult = results.get(i);
            testResult.getTest();
            totalTests++;
            new StringBuilder();
            for (CommandResult cmdResult : testResult.getResults()) {
                Command cmd = cmdResult.getCommand();

                if (cmdResult.getResult() == Result.PASSED) {
                    commandsPassed++;
                    if (cmd.getName().startsWith("assert")) {
                    } else {
                    }
                } else if (cmdResult.getResult() == Result.FAILED || cmdResult.getResult() == Result.ASSERT_FAILED) {
                    commandsFailed++;
                } else {
                    commandsError++;
                }
                //cmdHtml.append(MessageFormat.format(COMMAND_RESULT, cmdStatus, cmd.getName(), cmd.getTarget(), cmdResult.getMsg() != null ? cmdResult.getMsg() : cmd.getValue()));
            }
            if (testResult.getResult() == Result.PASSED) {
                testsPassed++;
            } else {
                testsFailed++;
                suiteResult = "status_failed";
            }
              testTables.add(MessageFormat.format(test.getBaseURL(), testStatus, test.getTitle(), cmdHtml));
              suiteIndex.append(MessageFormat.format(SUITE_SUMMARY, testStatus, test.getFileName(), test.getTitle()));
        }

        String suiteHtml = MessageFormat.format(SUITE_SUMMARY, suiteResult, suite.getTitle(), suiteIndex);
        String time = String.format("%.2f sec", duration / 1000.0d);

        HTMLTestResults htmlResult = new HTMLTestResults(VERSION, REVISION, suiteResult.substring(7), time, String.valueOf(totalTests), String.valueOf(testsPassed), String.valueOf(testsFailed), String.valueOf(commandsPassed), String.valueOf(commandsFailed), String.valueOf(commandsError), suiteHtml, testTables, log);
        reportFile.getParentFile().mkdirs();
        PrintWriter writer = new PrintWriter(reportFile);
         htmlResult.write(writer);
        writer.close();

        return new SuiteResult(suite, totalTests, testsFailed, time);


    }

    static Map<String, CommandExecutor> buildExecutorMap() {
        Map<String, CommandExecutor> executors = new HashMap<String, CommandExecutor>();
        DefaultCommandFactory defFactory = new DefaultCommandFactory();
        defFactory.register(executors);
        ServiceLoader<CommandFactory> cmdImpls = ServiceLoader.load(CommandFactory.class);
        for (CommandFactory factory : cmdImpls) {
            factory.register(executors);
        }
        return executors;

    }

    static Map<String, WebDriverProfileFactory> buildProfileMap() {
        Map<String, WebDriverProfileFactory> profiles = new HashMap<String, WebDriverProfileFactory>();
        ServiceLoader<WebDriverProfileFactory> profileImpls = ServiceLoader.load(WebDriverProfileFactory.class);
        for (WebDriverProfileFactory factory : profileImpls) {
            profiles.put(factory.profileName(), factory);
        }
        return profiles;
    }




	@Override
	public SuiteResult call() throws Exception {
		return  new SuiteRunner(suiteFile, reportFile, execCtx, wdriver).call(proxy, wdriver);
	}	
}
