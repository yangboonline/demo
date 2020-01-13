package com.yangbo.es.model.common;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.yangbo.es.constant.enums.IErrorEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * RPC服务层调用DTO
 *
 * @author Yang Bo(boyang217740@sohu-inc.com)
 * @version 1.0
 * @date 2019/12/17 9:38
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class RpcResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int SUCCESS = 0;
    public static final int FAILED = 1;

    private int code = 0;
    private String msg = "";
    private Throwable cause;
    private T data = null;
    private Map<String, Object> attachments = Maps.newHashMap();

    public boolean isSuccess() {
        return this.getCode() == RpcResult.SUCCESS;
    }

    public static <T> RpcResult<T> success(T data) {
        RpcResult<T> result = new RpcResult<>();
        result.setCode(SUCCESS);
        result.setData(data);
        return result;
    }

    public static <T> RpcResult<T> emptySuccess() {
        RpcResult<T> result = new RpcResult<>();
        result.setCode(SUCCESS);
        result.setData(null);
        return result;
    }

    public static <T> RpcResult<T> error(String msg) {
        return RpcResult.error(msg, null);
    }

    public static <T> RpcResult<T> error(int code, String msg) {
        return RpcResult.error(code, msg, null);
    }

    public static <T> RpcResult<T> error(String msg, Throwable cause) {
        return RpcResult.error(FAILED, msg, cause);
    }

    public static <T> RpcResult<T> error(int code, String msg, Throwable cause) {
        RpcResult<T> result = new RpcResult<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setCause(cause);
        return result;
    }

    public static <T> RpcResult<T> error(IErrorEnum errorEnum) {
        return RpcResult.error(errorEnum.getCode(), errorEnum.getMsg());
    }

    public static <T> RpcResult<T> error(IErrorEnum errorEnum, Object attachInfo) {
        RpcResult<T> error = RpcResult.error(errorEnum.getCode(), errorEnum.getMsg());
        error.setMsg(error.getMsg() + "\n" + JSON.toJSONString(attachInfo));
        return error;
    }

}
