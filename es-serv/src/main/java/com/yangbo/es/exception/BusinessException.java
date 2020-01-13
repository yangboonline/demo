package com.yangbo.es.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 通用业务异常
 *
 * @author Yang Bo(boyang217740@sohu-inc.com)
 * @version 1.0
 * @date 2019/12/17 10:02
 */
@Getter
@Setter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int code = 0;
    private String msg = null;

    public BusinessException(int code) {
        super();
        this.code = code;
    }

    public BusinessException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(int code, Exception e) {
        super(e);
        this.code = code;
    }

}
