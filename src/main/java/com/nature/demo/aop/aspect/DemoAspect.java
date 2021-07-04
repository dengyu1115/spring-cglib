package com.nature.demo.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class DemoAspect {

    @Pointcut("@annotation(com.nature.demo.aop.annotation.DoSomething)")
    public void mark() {
        // ignore
    }

    @Around("mark()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        System.out.println("this is the aspect logic method = " + point.getSignature().toString());
        return point.proceed();
    }

}
