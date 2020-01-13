package com.yangbo.es.constant.enums;

/**
 * 自定义错误枚举接口--统一处理返回VO(策略设计模式)
 *
 * @author Yang Bo(boyang217740@sohu-inc.com)
 * @version 1.0
 * @date 2019/12/17 10:02
 */
public interface IErrorEnum {

    /**
     * 错误Code
     *
     * @return 错误Code
     */
    int getCode();

    /**
     * 错误消息
     *
     * @return 错误消息
     */
    String getMsg();

}