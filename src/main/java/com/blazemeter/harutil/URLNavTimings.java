package com.blazemeter.harutil;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.browsermob.core.har.Har;
import org.browsermob.proxy.ProxyServer;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import au.com.bytecode.opencsv.CSVReader;

public  class  URLNavTimings {
   	static String  suiteContentBeg  = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
   			"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">"  +
   			"<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">"+
   			"<head>"+
   			"  <meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\" />" + 
   			"  <title>Test Suite</title>" + 
   			"</head>" + 
   			"<body>" + 
   			"<table id=\"suiteTable\" cellpadding=\"1\" cellspacing=\"1\" border=\"1\" class=\"selenium\"><thead>"+ 
   			"<tr><td><b>Test Suite</b></td></tr></thead><tbody>";


   			static String  suiteContentEnd = "</tbody></table></body></html>" ;


   			static String  suiteContentForTest  =  "<tr><td><a href=\"%s\">%s</a></td></tr>" ;


   			static String FtestContent ="";


   			static  String FsuiteContent ="";


	public static void main( String[] args)  throws  Exception {
	    
		String  csvFile =  "runList.csv";
		
		CSVReader   reader  = new   CSVReader(new  FileReader(csvFile));
		String [] nextLine = null;
		
    	
		ArrayList<String> listUrls = new ArrayList<String>() ;
		HashMap<String, ArrayList<HashMap<String , String>>> domainCollection =  new  HashMap<String  , ArrayList<HashMap<String ,String>>>() ;
		String fName;
		while ((nextLine = reader.readNext()) != null) {
		    	  fName  =    nextLine[0].replace(File.pathSeparator, "_"); 
		    	  new  URL(nextLine[0]).getHost();
		    	  listUrls.add(fName);     	    	
	          }
		 reader.close();
		 System.out.println(domainCollection);
		 ProxyServer proxy = new ProxyServer(4444);
		 final SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
		 String  aggsuiteContentForTest ="";
		 String  timeRun = format.format(new java.util.Date());
		 String  suiteName  =   String.format("suite%shar.html", timeRun) ;
		 FileWriter  fSuite  =new  FileWriter( suiteName , false);
	     aggsuiteContentForTest  ="";
		 FileWriter  fWriter = null;
		  for   ( int  j  = 0 ;  j < listUrls.size()-1  ;j++){ 
		        proxy.start();
		        URL  u = new  URL( listUrls.get(j));
			   String urlFull   =  listUrls.get(j);
			   String  host =  (new  URL(urlFull)).getHost();
			   fWriter = new FileWriter(String.format("%s_%d_har.html"  , host, j) ,true);	
			    aggsuiteContentForTest += String.format(  aggsuiteContentForTest , String.format("%s_%d_har.html"  , host, j) , String.format("%s_%d_har.html"  , host, j) )  ;  
			    DesiredCapabilities capabilities = new DesiredCapabilities();
		    	capabilities.setCapability(CapabilityType.PROXY, proxy.seleniumProxy());
		    	WebDriver driver = new FirefoxDriver(capabilities);
		    	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				proxy.newHar(u.toString());
				driver.get(u.toString());
				//get the HAR data
				Har har = proxy.getHar();
			    har.writeTo(fWriter);
				 String.format( har.toString() , urlFull );
				 driver.close();	
				fWriter.close();
				 proxy.stop();
		      }
			  FsuiteContent  = suiteContentBeg +   aggsuiteContentForTest + suiteContentEnd   ;
		      fSuite.write(FsuiteContent);
		      fSuite.close();
		     
		}
}

