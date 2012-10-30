package com.blazemeter.mvnsese.exec;

import com.blazemeter.mvnsese.model.SeleneseTest;

import java.util.List;

public class TestResult {

    private SeleneseTest test;
    private List<CommandResult> results;
    private Result result = Result.PASSED;

    public TestResult(SeleneseTest test, List<CommandResult> results) {
        this.test = test;
        this.results = results;
    }

    public SeleneseTest getTest() {
        return test;
    }

    public List<CommandResult> getResults() {
        return results;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
