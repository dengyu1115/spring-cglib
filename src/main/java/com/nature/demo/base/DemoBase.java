package com.nature.demo.base;

import com.nature.proxy.annotation.NatureService;
import com.nature.demo.aop.annotation.DoSomething;
import com.nature.demo.function.DemoFunction;
import com.nature.demo.parent.DemoParent;
import com.nature.demo.component.DemoService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 示例service类，作为抽象类实例化示例
 * @author nature
 * @version 1.0.0
 * @since 2021/7/3 21:20
 */
@NatureService
public abstract class DemoBase extends DemoParent implements DemoFunction {

    @Autowired
    private DemoService demoService;

    @DoSomething
    public String test() {
        return demoService.test();
    }
}
