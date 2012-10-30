package com.blazemeter.mvnsese.exec;

import com.blazemeter.mvnsese.model.Command;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;
import java.lang.reflect.Method;
import java.util.Map;

public class VerifyCommandExecutor extends ReflectiveCommandExecutor {

    public VerifyCommandExecutor(Method m) {
        super(m);
    }

    public CommandResult execute(Selenium s, Map<String, Object> env, Command c) {
        CommandResult res = new CommandResult(c);
        try {
            Object r = evaluate(s, env, c);
            if (r == null) {
                res.fail(String.format("Element %s not found", c.getTarget()));
            } else if (r instanceof Boolean) {
                Boolean b = (Boolean) r;
                if (!b) {
                    res.setResult(Result.FAILED);
                }
            } else if (r instanceof String) {
                String st = (String) r;
                String regex = c.getValue();
                if (regex.length() == 0) {
                    regex = c.getTarget();
                }
                regex = regex.replace("*", ".*").replace("?", ".?").replace("+", ".+");
                if (!st.matches(regex)) {
                    res.fail(String.format("Actual value '%s' did not match '%s'", st, c.getValue()));
                }
            } else {
                res.fail("Unknown return type");
            }

        } catch (SeleniumException se) {
            res.fail(se.getMessage());
        }
        return res;
    }
}
