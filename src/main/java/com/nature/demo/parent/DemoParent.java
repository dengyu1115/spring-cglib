package com.nature.demo.parent;

import com.nature.demo.aop.annotation.DoSomething;
import com.nature.demo.component.DemoParentService;
import org.springframework.beans.factory.annotation.Autowired;

public class DemoParent {

    @Autowired
    private DemoParentService demoParentService;

    @DoSomething
    public String doSomething() {
        return demoParentService.testParent();
    }
}
