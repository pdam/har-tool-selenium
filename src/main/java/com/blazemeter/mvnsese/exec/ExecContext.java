package com.blazemeter.mvnsese.exec;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class ExecContext {

    private String baseURL;
    private WebDriver webDriver;
    private String timeout = "30000";
    private String maxTestTime;
    private TraceLevel traceHTML;

    public static enum TraceLevel {

        NONE, ERROR, ALL;

        public static TraceLevel getLevel(String level) {
            if ("ALL".equalsIgnoreCase(level)) {
                return ALL;
            }
            if ("ERROR".equalsIgnoreCase(level)) {
                return ERROR;
            }
            return NONE;
        }
    }

    public ExecContext(WebDriver driver) {
		this.webDriver  =driver ;
	}

	public ExecContext(FirefoxDriver dr) {
	        this.webDriver   =  dr;
	}
 
	public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webdriver) {
        this.webDriver = webdriver;
    }

    public String  getTimeout() {
        return timeout;
    }

    public void setTimeout(String  i) {
        this.timeout = i;
    }

    public String  getMaxTestTime() {
        return maxTestTime;
    }

    public void setMaxTestTime(String maxTestTime) {
        this.maxTestTime = maxTestTime;
    }

    public TraceLevel getTraceHTML() {
        return traceHTML;
    }

    public void setTraceHTML(TraceLevel traceHTML) {
        this.traceHTML = traceHTML;
    }
}
