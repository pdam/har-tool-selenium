package com.blazemeter.harutil;

import   com.blazemeter.mvnsese.exec.ExecContext;
import com.blazemeter.mvnsese.exec.ExecContext.TraceLevel;
import com.blazemeter.mvnsese.exec.SuiteResult;
import com.blazemeter.mvnsese.exec.SuiteRunner;
import com.blazemeter.mvnsese.model.SeleneseSuite;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;
import org.browsermob.proxy.ProxyServer;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.concurrent.TimeUnit;


public class ProxyTest {
    

	private static String suiteName;
	private static File timeout;
	private static WebClient client;

	public static void main(String[] args) throws Exception {
		
		 File suite = new File("src/main/resources/languageSuite.html");
	     File report = new File("src/main/resources/report-languageSuite.html");
	     File har =  new  File("src/main/resources/languageSuite-har.json");
			
        // start the proxy
        ProxyServer proxy = new ProxyServer(4444);
        proxy.start();

        // configure it as a desired capability
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, proxy.seleniumProxy());

        WebDriver driver = new FirefoxDriver(capabilities);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        ExecContext ctx = new ExecContext(driver);
        ctx.setWebDriver(driver);
        SuiteRunner runner = new SuiteRunner(suite, report, ctx , driver);
        SuiteResult result = runner.call(proxy,driver);
        proxy.getHar().writeTo(har);
        driver.close();
        proxy.stop();
        

        

		

	}

	public static WebClient getClient() {
		return client;
	}

	public static void setClient(WebClient client) {
		ProxyTest.client = client;
	}

	public static File getTimeout() {
		return timeout;
	}

	public static void setTimeout(File timeout) {
		ProxyTest.timeout = timeout;
	} 


}
