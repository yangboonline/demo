package com.yangbo.es.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 错误参数枚举
 *
 * @author Yang Bo(boyang217740@sohu-inc.com)
 * @version 1.0
 * @date 2019/12/17 10:02
 */
@Getter
@ToString
@AllArgsConstructor
public enum ErrorCodeEnum implements IErrorEnum {

    /**
     * 错误参数枚举
     */
    BAD_REQUEST(400001, "参数错误"),

    /**
     * Http请求出错
     */
    HTTP_REQUEST_ERROR(400004, "Http请求出错"),

    /**
     * 系统异常
     */
    FAILED_ERROR(500001, "系统异常"),
    ;

    private int code;
    private String msg;

}
