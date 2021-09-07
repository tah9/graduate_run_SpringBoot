package com.tah.graduate_run.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ->  tah9  2021/8/28 14:52
 * 需要token的请求使用此注解
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UseToken {
    boolean required() default false;
}
