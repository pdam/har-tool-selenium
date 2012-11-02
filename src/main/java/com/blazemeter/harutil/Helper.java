package com.blazemeter.harutil;

import  java.lang.reflect.*;
import  java.util.*;
import  java.util.*;
import  java.io.*;

public   class  Helper {

public  Helper(){
 }




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
    for(Class cl : classes) {
        if("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
            Field field = cl.getDeclaredField("m");
            field.setAccessible(true);
            Object obj = field.get(env);
            Map<String, String> map = (Map<String, String>) obj;
            map.clear();
            map.putAll(newenv);
        }
    }
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
        for(Class cl : classes) {
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

