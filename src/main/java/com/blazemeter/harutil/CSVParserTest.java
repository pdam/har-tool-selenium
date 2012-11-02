package com.blazemeter.harutil;
import com.blazemeter.mvnsese.exec.ExecContext;
import com.blazemeter.mvnsese.exec.ExecContext.TraceLevel;
import java.util.*;
import com.blazemeter.mvnsese.exec.SuiteResult;
import com.blazemeter.mvnsese.exec.SuiteRunner;
import au.com.bytecode.opencsv.CSVReader;
import com.blazemeter.mvnsese.model.SeleneseSuite;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;
import org.browsermob.proxy.ProxyServer;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.CapabilityType;
import com.blazemeter.harutil.Helper;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.net.UnknownHostException;
import java.net.URL;
import java.io.*;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;


public  class  CSVParserTest {
   	public static void main( String[] args)  throws  Exception {
	        Helper.startHeadless();	
		CSVReader reader = new CSVReader(new FileReader(args[0]));
        	ProxyServer proxy = new ProxyServer(4444);
        	proxy.start();
        	DesiredCapabilities capabilities = new DesiredCapabilities();
        	capabilities.setCapability(CapabilityType.PROXY, proxy.seleniumProxy());
        	WebDriver driver = new FirefoxDriver(capabilities);
        	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		String [] nextLine;
		while ((nextLine = reader.readNext()) != null) {
	   		int numLines = nextLine.length;
	   		System.out.println("Number of Data Items: " + numLines);
	   		for (int i = 0; i < numLines; i++)
				{
				      System.out.println("     nextLine[" + i + "]:  " + nextLine[i]);
				      URL  url  = new  URL(nextLine[i]);
				      String  harName =   url.getHost() +  ".har" ;
        	      		      proxy.newHar(harName);
				      new PerformanceTiming(driver, proxy.getHar());
        	      		      proxy.getHar().writeTo(new File(harName));
			          }	
			  }
		
        driver.close();	
        proxy.stop();
		}
}

