package com.blazemeter.mvnsese.exec;

import com.blazemeter.mvnsese.model.Command;
import com.thoughtworks.selenium.Selenium;

import java.net.URL;
import java.util.Map;

public interface CommandExecutor {

    public static final String TIMEOUT = "60000";

    CommandResult execute(Selenium s, Map<String, Object> env, Command c);

	CommandResult execute(Selenium selenium, Map<String, Object> testCtx,
			URL url);
}
