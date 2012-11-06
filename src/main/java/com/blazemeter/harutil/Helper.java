package com.blazemeter.harutil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.james.mime4j.field.datetime.DateTime;

import au.com.bytecode.opencsv.CSVReader;

public   class  Helper {

public  Helper(){
 }

private  static String testContentBeg  = 

"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\"><head profile=\"http://selenium-ide.openqa.org/profiles/test-case\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><link rel=\"selenium.base\" href=\"http://%s\" /></head><body><table id=\"%s\" cellpadding=\"1\" cellspacing=\"1\" border=\"1\" class=\"selenium\"><thead><tr><td>%s</td></tr></thead><tbody>";


private   static String  testContentForUrl   =  "<tr>"+
"	<td>open</td>"+
"	<td>%s</td>"+ 
"	<td></td>"+
"   </tr>"+
"   <tr>"+
"	<td>waitForPageToLoad</td>"+
"	<td>30000</td>"+
"	<td></td>"+
"</tr>"   ;


private  static String testContentEnd  =   "</tbody></table>" +
"</body>" +
"</html>";

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



public static void    startHeadless() throws IOException {
        String[] command = { "xvfb-run" ,  "-n"  , "99"  ,  "xterm&" };
        ProcessBuilder probuilder = new ProcessBuilder( command );
        Process process = probuilder.start();
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        System.out.printf("Output of running %s is:\n",
                Arrays.toString(command));
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        
        //Wait to get exit value
        try {
            int exitValue = process.waitFor();
            System.out.println("\n\nExit Value is " + exitValue);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }








// this is a dirty hack - but should be ok for a unittest.
  public  static  void addenv(Map<String, String> newenv) throws ClassNotFoundException, IllegalStateException , Exception 
  {
    Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
    Field theEnvironmentField = processEnvironmentClass.getDeclaredField("DISPLAY");
    theEnvironmentField.setAccessible(true);
    Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
    env.clear();
    env.putAll(newenv);
    Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("DISPLAY");
    theCaseInsensitiveEnvironmentField.setAccessible(true);
    Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
    cienv.clear();
    cienv.putAll(newenv);
  }


public static void env_append(Map<String, String> newenv) throws Exception {
    Class[] classes = Collections.class.getDeclaredClasses();
    Map<String, String> env = System.getenv();
    for(Class<?> cl : classes) {
        if("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
            Field field = cl.getDeclaredField("m");
            field.setAccessible(true);
            Map<String, String> map =  (Map<String, String>) field.get(env);
            map.clear();
            map.putAll(newenv);
        }
    }
}



public static   String      generateHtmlSuite(String  csvFile ) throws IOException{
	
	
	
	CSVReader   reader  = new   CSVReader(new  FileReader(csvFile));
	String [] nextLine = null;
	ArrayList<String> listUrls = null ;
	FileWriter  f  =new  FileWriter(csvFile +".html" , true);
	HashMap<String, ArrayList> domainCollection =  new  HashMap<String  , ArrayList>() ;
	       while ((nextLine = reader.readNext()) != null) {
	    	   URL  u  =  new  URL(nextLine[0]);
	           String  hostName  =  u.getHost();
	           String urlPath = u.getPath();
	    	  
	    	   if (domainCollection.containsKey(hostName)) {
	    		      listUrls.add(urlPath);
	    	   }
	    	   else {
	    		        listUrls  =  new ArrayList<String>();
	    		   		listUrls.add(urlPath);         
	    	   }
	    	   domainCollection.put(hostName, listUrls);
	    		       
	       } 		   
	
	       reader.close();
	       
	       System.out.println(domainCollection);
	       
	       final SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
	       String  aggsuiteContentForTest ="";
	       String  timeRun = format.format(new java.util.Date());
	       String  suiteName  =   String.format("suite%s.html", timeRun) ;
	       FileWriter  fSuite  =new  FileWriter( suiteName , false);

	       String[] domains = (String[])( domainCollection.keySet().toArray( new String[domainCollection.size()] ) );
	       aggsuiteContentForTest  ="";
	       for ( String    domain  :  domains ) {
	    	   FtestContent ="";
	    	   aggsuiteContentForTest += String.format(  suiteContentForTest , String.format("%s.html",domain)  ,  String.format("%s.html",domain) )  ;  
		       FileWriter fTest = new  FileWriter(String.format("%s.html",domain) , false);
		       List  urlList =  domainCollection.get(domain);
		       System.out.println( "Hostname  "+ domain +"    has      URLS " +   urlList);
		       String aggtestContentForTest ="";
				for   ( int  j  = 0 ;  j<urlList.size() -1  ;j++){  
					  String  pathName = (String) urlList.get(j).toString();
					   aggtestContentForTest += String.format( testContentForUrl ,  String.format("http://%s%s", domain , pathName) );
				}
				
				FtestContent  =   String.format(testContentBeg, domain ,domain ,domain) +   aggtestContentForTest +  testContentEnd   ;
				fTest.write(FtestContent);
				fTest.close();
	      }
		
			
			FsuiteContent  = suiteContentBeg +   aggsuiteContentForTest + suiteContentEnd   ;
	      fSuite.write(FsuiteContent);
	      fSuite.close();
	      return  suiteName;
	
}



public  static  void setenv(HashMap<String, String> newenv)
{
  try
    {
        Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
        Field theEnvironmentField = processEnvironmentClass.getDeclaredField("DISPLAY");
        theEnvironmentField.setAccessible(true);
        Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
        env.putAll(newenv);
        Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
        theCaseInsensitiveEnvironmentField.setAccessible(true);
        Map<String, String> cienv = (Map<String, String>)     theCaseInsensitiveEnvironmentField.get(null);
        cienv.putAll(newenv);
    }
    catch (NoSuchFieldException e)
    {
      try {
        Class[] classes = Collections.class.getDeclaredClasses();
        Map<String, String> env = System.getenv();
        for(Class<?> cl : classes) {
            if("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                Field field = cl.getDeclaredField("m");
                field.setAccessible(true);
                Object obj = field.get(env);
                Map<String, String> map = (Map<String, String>) obj;
                map.clear();
                map.putAll(newenv);
            }
        }
      } catch (Exception e2) {
        e2.printStackTrace();
      }
    } catch (Exception e1) {
        e1.printStackTrace();
    } 
}



}

