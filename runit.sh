#!/bin/sh
debInst()
{
    dpkg-query -Wf'${db:Status-abbrev}' "${1}" 2>/dev/null | grep -q '^i'
}

if debInst "${1}"; then
    printf 'Why yes, the package %s _is_ installed!\n' "${1}"
else
    printf 'I regret to inform you that the package %s is not currently installed.\n' "${1}"
fi

debInst  "xvfb  xorg-common  firefox"
Xvfb :99 -ac -screen 0 1024x768x8 &

export  DISPLAY=:99 && java   -classpath "lib/selenium-server-standalone-2.25.0.jar:lib/xalan-2.6.0.jar;lib/servlet-api-2.5.jar:lib/jackson-core-asl-1.9.9.jar:lib/jackson-mapper-asl-1.9.9.jar:lib/jackson-mapper-lgpl-1.9.9.jar:lib/commons-logging-1.0.4.jar:lib/commons-codec-1.2.jar;lib/commons-io-1.3.2.jar:lib/ant-1.8.2.jar;lib/ant-launcher-1.8.2.jar:lib/bcprov-jdk15-135.jar:lib/jetty-server-7.3.0.v20110203.jar:lib/jetty-continuation-7.3.0.v20110203.jar:lib/jetty-http-7.3.0.v20110203.jar:lib/jetty-io-7.3.0.v20110203.jar:lib/jetty-util-7.3.0.v20110203.jar:lib/jetty-servlet-7.3.0.v20110203.jar:lib/jetty-security-7.3.0.v20110203.jar:lib/browsermob-proxy-2.0-beta-6.jar:target/har-selenium-1.0.0.jar"  com.blazemeter.harutil.ProxyTest   "src/main/resources/languageSuite.html"
