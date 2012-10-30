package com.blazemeter.mvnsese.model;

import com.blazemeter.mvnsese.model.SeleneseTest;

import java.util.List;

public class SeleneseSuite extends Selenese{
    
    private List<SeleneseTest> tests;

    public List<SeleneseTest> getTests() {
        return tests;
    }

    public void setTests(List<SeleneseTest> tests) {
        this.tests = tests;
    }

}
