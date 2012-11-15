har-tool-selenium
==
For  the  really  impatient  :
  Download  the    zip  as a  tarball from  https://github.com/pdam/har-tool-selenium/zipball/master . 


To  run   it  
java   -classpath "lib/opencsv-2.0.0.jar;lib/selenium-server-standalone-2.25.0.jar:lib/xalan-2.6.0.jar;lib/servlet-api-2.5.jar:lib/jackson-core-asl-1.9.9.jar:lib/jackson-mapper-asl-1.9.9.jar:lib/jackson-mapper-lgpl-1.9.9.jar:lib/commons-logging-1.0.4.jar:lib/commons-codec-1.2.jar;lib/commons-io-1.3.2.jar:lib/ant-1.8.2.jar;lib/ant-launcher-1.8.2.jar:lib/bcprov-jdk15-135.jar:lib/jetty-server-7.3.0.v20110203.jar:lib/jetty-continuation-7.3.0.v20110203.jar:lib/jetty-http-7.3.0.v20110203.jar:lib/jetty-io-7.3.0.v20110203.jar:lib/jetty-util-7.3.0.v20110203.jar:lib/jetty-servlet-7.3.0.v20110203.jar:lib/jetty-security-7.3.0.v20110203.jar:lib/browsermob-proxy-2.0-beta-6.jar:target/har-selenium-1.0.0.jar"  com.blazemeter.harutil.CSVParserTest   path_to_csv


Platforms  supported 
====
      1. If  you  are  on unix  OR   mac  and   have  a   Xwindows (linux  )  Choose   runit_mac.sh  
      2.   If you  are  on  windows  pick up the  batch  file  runit.bat
      3.  If    you   want  to  run  a   haedless  version    choose    runit.sh


Under  the  Hood
====

The runit script   is  preconfigured   against  a   Selenium   Test  Suite  called   src/main/resources/languageSuite.html 
which  has 4  Test  Scripts in it  . Testpython ,Testperl, Testrubygems  and  TestOcaml  .  Open a    shell or  command  window
and    runit  .e.g  on  Linux 
     source   runit.sh  
It  does   the   following 
   1.  BrowserMob Proxy  instance   at  port 4444 . 
   2. Configures a    Firefox  profile   setup  using  the  same  proxy 
   3. Launches   webdriver   and  parse  the  selenese   script 
   4. Starts  the  har  Collection and  also  the  selenium  script  runner
   5. At  the end  stop   the  wedriver instance  and the  proxy
   You   should   see  the  HAR  files   downloaded   as   <Scriptname>.har   

Headless Note 
=====
Unix   shell   with no  X-windows  the   script   downloads  a  couple  of  packahes
for   you .Xvfb  and   related  libraries.  After   that  it   starts a   XFvb   instance   at   port  99  . You  could  
check  it  by  exporting  the   display   ariable  to 99  and  then   run the script 


