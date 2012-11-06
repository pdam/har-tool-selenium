package com.blazemeter.harutil;
import  com.blazemeter.mvnsese.exec.ExecContext;
import com.blazemeter.mvnsese.exec.SuiteResult;
import com.blazemeter.mvnsese.exec.SuiteRunner;
import com.gargoylesoftware.htmlunit.WebClient;
import org.browsermob.proxy.ProxyServer;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.util.concurrent.TimeUnit;


public class   CSVTest {
    

	private static File timeout;
	private static WebClient client;

	public static void main(String[] args) throws Exception {
	     String  csv  =  args[0];	
	     String  sName  = Helper.generateHtmlSuite(csv);
	     Thread.sleep(1000L);
	     File report = new File("report-csvSuite.html");
	     File har =  new  File("listCSVRun.json");
             ProxyServer proxy = new ProxyServer(4444);
             proxy.start();

            // configure it as a desired capability
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(CapabilityType.PROXY, proxy.seleniumProxy());
            WebDriver driver = new FirefoxDriver(capabilities);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            ExecContext ctx = new ExecContext(driver);
            ctx.setWebDriver(driver);
            SuiteRunner runner = new SuiteRunner(new File(sName), report, ctx , driver);
            SuiteResult result = runner.call(proxy,driver);
            proxy.getHar().writeTo(har);
            driver.close();
            proxy.stop();
        

        

		

	}

	public static WebClient getClient() {
		return client;
	}

	public static void setClient(WebClient client) {
		CSVTest.client = client;
	}

	public static File getTimeout() {
		return timeout;
	}

	public static void setTimeout(File timeout) {
		CSVTest.timeout = timeout;
	} 


}
