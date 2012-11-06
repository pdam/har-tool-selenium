package com.blazemeter.mvnsese.exec;

import com.blazemeter.mvnsese.model.Command;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;

public class WaitForCommandExecutor extends ReflectiveCommandExecutor {

    public WaitForCommandExecutor(Method m) {
        super(m);
    }

    public CommandResult execute(Selenium s, Map<String, Object> env, Command c) {
        CommandResult res = new CommandResult(c);
        if (c.getValue() == null) {
            return res.fail("no value specified");
        }
        long start = System.currentTimeMillis();
        long timeout = Long.parseLong((String)env.get(TIMEOUT));
        while (System.currentTimeMillis() - start <= timeout) {
            try {
                if (c.getValue().equals(evaluate(s, env, c))) {
                    return res;
                }
            } catch (SeleniumException se) {
                //if (!(se.getCause() instanceof ScriptException)) {
                return res.fail(se.getMessage());
                //}
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                return res.fail(e.getMessage());
            }
        }
        return res.fail("timed out");
    }

	@Override
	public CommandResult execute(Selenium selenium,
			Map<String, Object> testCtx, URL url) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
