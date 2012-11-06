package com.blazemeter.mvnsese.exec;

import com.blazemeter.mvnsese.model.Command;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.Wait.WaitTimedOutException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;

public class AndWaitCommandExecutor extends ReflectiveCommandExecutor {

    public AndWaitCommandExecutor(Method m) {
        super(m);
    }

    public CommandResult execute(Selenium s, Map<String, Object> env, Command c) {
        CommandResult res = new CommandResult(c);
        try {
            evaluate(s, env, c);
            s.waitForPageToLoad((String) env.get(TIMEOUT));
        } catch (SeleniumException se) {
            res.fail(se.getMessage());
        } catch (WaitTimedOutException we) {
            res.fail(we.getMessage());
        }
        return res;
    }

	@Override
	public CommandResult execute(Selenium selenium,
			Map<String, Object> testCtx, URL url) {
		// TODO Auto-generated method stub
		return null;
	}
}
