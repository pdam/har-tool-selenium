package com.blazemeter.mvnsese.exec;

import java.net.URL;

import com.blazemeter.mvnsese.model.Command;

public class CommandResult {

    private Result result = Result.PASSED;
    private String msg;
    private Command command;

    CommandResult(Command command) {
        this.command = command;
    }

    public CommandResult(URL url) {
		
	}

	public CommandResult(String captureNetworkTraffic) {
		// TODO Auto-generated constructor stub
	}

	public CommandResult fail(String msg) {
        this.result = Result.FAILED;
        this.msg = msg;
        return this;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Command getCommand() {
        return command;
    }
}
