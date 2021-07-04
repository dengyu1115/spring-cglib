package com.nature.demo.controller;

import com.nature.demo.base.DemoBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("demo")
@RestController
public class DemoController {

    @Autowired
    private DemoBase demoBase;

    @PostMapping("test")
    public void test() {
        System.out.println("this test start");
        String print = demoBase.print();
        System.out.println(print);
        String test = demoBase.test();
        System.out.println(test);
        String something = demoBase.doSomething();
        System.out.println(something);
        System.out.println("this test end");
    }
}
