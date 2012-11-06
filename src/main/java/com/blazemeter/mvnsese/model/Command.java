package com.blazemeter.mvnsese.model;

import java.beans.Encoder;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import com.blazemeter.mvnsese.exec.CommandExecutor;
import com.blazemeter.mvnsese.exec.CommandResult;
import com.thoughtworks.selenium.Selenium;

public class Command {

    private String name;
    private String target;
    private String value;

    

	public Command(CommandExecutor commandExecutor) {
		// TODO Auto-generated constructor stub
	}



	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

	public void execute(Selenium selenium,String  hostName, String pathName) {
		selenium.open(pathName);
		selenium.waitForPageToLoad("30000");
	}

	public void execute(Selenium selenium,Map<String, Object> testCtx, URL url) {
		selenium.open(url.toString());
		selenium.waitForPageToLoad("30000");
		
	}
}
