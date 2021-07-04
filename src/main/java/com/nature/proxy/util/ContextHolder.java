package com.nature.proxy.util;

import org.springframework.context.ApplicationContext;

public class ContextHolder {

    private static ApplicationContext ac;

    public static void init(ApplicationContext context) {
        ContextHolder.ac = context;
    }

    public static Object getBean(Class<?> clz) {
        return ac.getBean(clz);
    }

}
