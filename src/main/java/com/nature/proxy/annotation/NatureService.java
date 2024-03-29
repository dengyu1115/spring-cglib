package com.nature.proxy.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用以标记自定义spring组件类
 * @author nature
 * @version 1.0.0
 * @since 2021/7/3 21:19
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NatureService {

    String value() default "";

}
