package com.bert.swagger.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yangbo
 * @date 2018/6/12
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {

    /**
     * rest请求失败重试次数,默认3次
     */
    int times() default 3;

    /**
     * 请求url
     */
    String url();

}
