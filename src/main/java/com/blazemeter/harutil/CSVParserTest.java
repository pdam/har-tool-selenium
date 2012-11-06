package com.blazemeter.harutil;
import au.com.bytecode.opencsv.CSVReader;

import org.browsermob.core.har.Har;
import org.browsermob.core.har.HarPage;
import org.browsermob.proxy.ProxyServer;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.io.FileReader;
import java.util.List;
import java.util.concurrent.TimeUnit;


public  class  CSVParserTest {
   	private static CSVReader reader;

	public static void main( String[] args)  throws  Exception {
	    
		    reader = new CSVReader(new FileReader(args[0]));
		    Helper.generateHtmlSuite(args[0]);
        	ProxyServer proxy = new ProxyServer(4444);
        	proxy.start();
        	DesiredCapabilities capabilities = new DesiredCapabilities();
        	capabilities.setCapability(CapabilityType.PROXY, proxy.seleniumProxy());
        	WebDriver driver = new FirefoxDriver(capabilities);
        	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		    String[] lines ;
			while ((lines = reader.readNext()) != null) {
			    System.out.println(lines[0].toString());
		         URL  url  = new  URL(lines[0]);
			      String  harName =   url.getHost() +  ".har" ;
	        	  Har  har=  proxy.newHar(harName);
	        	  FileWriter fw  =  new FileWriter(harName, true) ;
	        	  new PerformanceTiming(driver, har);
	        	  har.writeTo(fw);
	        	  fw.close();
	        	  
					      			      
				  }
			
		
        driver.close();	
        proxy.stop();
		}
}

