package com.blazemeter.harutil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.browsermob.proxy.ProxyServer;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class NavTimingTest {
    public static void main(String[] args) throws Exception {
        // start the proxy
        ProxyServer proxy = new ProxyServer(4444);
        proxy.start();

        // configure it as a desired capability
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, proxy.seleniumProxy());

        FirefoxDriver driver = new FirefoxDriver(capabilities);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
         
        // start capture
        proxy.newHar("localhost");
        
        
        // write the har file out
        new PerformanceTiming(driver, proxy.getHar());
        proxy.getHar().writeTo(new File("d:/test.har"));

        driver.close();

    }
}
