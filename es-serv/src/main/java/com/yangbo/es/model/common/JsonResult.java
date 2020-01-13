package com.yangbo.es.model.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import com.yangbo.es.constant.enums.IErrorEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 返回前端VO
 *
 * @author Yang Bo(boyang217740@sohu-inc.com)
 * @version 1.0
 * @date 2019/12/17 9:38
 */
@Getter
@Setter
@NoArgsConstructor
public class JsonResult implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int SUCCESS = 0;
    public static final int FAILED = 1;

    /**
     * 状态码
     */
    private int code = 0;
    /**
     * 返回消息
     */
    private String msg = StringUtils.EMPTY;
    /**
     * 返回数据
     */
    private Object data = null;
    /**
     * 返回总条数
     */
    private int total = 0;
    /**
     * 服务器时间
     */
    private Date serverTime = Date.from(Instant.now());
    /**
     * 附件
     */
    private Map<String, Object> attachments = Maps.newHashMap();

    public JsonResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public <T> T getData(Class<T> clazz) {
        if (this.getData() == null) {
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(this.getData()), clazz);
    }

    public <T> T getData(TypeReference<T> typeReference) {
        if (this.getData() == null) {
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(this.getData()), typeReference);
    }

    public boolean isSuccess() {
        return this.getCode() == SUCCESS;
    }

    @SuppressWarnings("rawtypes")
    public static JsonResult success(Object data) {
        JsonResult ret = new JsonResult(SUCCESS, "success");
        ret.setData(data);
        if (Objects.nonNull(data) && data instanceof List) {
            ret.setTotal(((List) data).size());
        }
        return ret;
    }

    public static JsonResult success(RpcResult<?> rpcResult) {
        JsonResult ret = new JsonResult(SUCCESS, "success");
        ret.setData(rpcResult.getData());
        ret.setAttachments(rpcResult.getAttachments());
        if (rpcResult instanceof ServiceResult) {
            ret.setTotal(((ServiceResult<?>) rpcResult).getTotal());
        }
        return ret;
    }

    public static JsonResult transmit(RpcResult<?> rpcResult) {
        if (rpcResult.isSuccess()) {
            // 成功
            return JsonResult.success(rpcResult);
        }
        JsonResult ret;
        if (rpcResult.getCause() == null) {
            ret = JsonResult.error(rpcResult.getCode(), rpcResult.getMsg());
        } else {
            ret = JsonResult.error("unknowns error!");
        }
        ret.setAttachments(rpcResult.getAttachments());
        return ret;
    }

    public static JsonResult success() {
        return new JsonResult(SUCCESS, "success");
    }

    public static JsonResult error(String msg) {
        return new JsonResult(FAILED, msg);
    }

    public static JsonResult error(int code, String msg) {
        return new JsonResult(code, msg);
    }

    public static JsonResult fail(IErrorEnum errorEnum) {
        return new JsonResult(errorEnum.getCode(), errorEnum.getMsg());
    }

    public static JsonResult fail(IErrorEnum errorEnum, Object attachInfo) {
        JsonResult fail = JsonResult.fail(errorEnum);
        fail.setMsg(fail.getMsg() + "\n" + JSON.toJSONString(attachInfo));
        return fail;
    }

}
