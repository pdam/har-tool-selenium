package com.blazemeter.mvnsese.exec;

import com.blazemeter.mvnsese.model.Command;
import com.thoughtworks.selenium.Selenium;
import java.util.Map;

public class PauseCommandExecutor implements CommandExecutor {

    public CommandResult execute(Selenium s, Map<String, Object> env, Command c) {
        CommandResult res = new CommandResult(c);
        try {
            long mili = Long.parseLong(c.getTarget());
            Thread.sleep(mili);
        } catch (Exception e) {
            res.fail(e.getMessage());
        }
        return res;
    }
}
