package com.yangbo.es.model.common;

import lombok.Getter;
import lombok.Setter;

/**
 * RPC服务层调用DTO
 *
 * @author Yang Bo(boyang217740@sohu-inc.com)
 * @version 1.0
 * @date 2019/12/17 9:38
 */
@Getter
@Setter
public class ServiceResult<T> extends RpcResult<T> {

    private static final long serialVersionUID = 1L;

    private int ps = 20;
    private int pn = 1;
    private int total = 0;
    private boolean isThisTimeDone = true;

    public void setPagination(Pagination pgn, int total) {
        this.pn = pgn.getPn();
        this.ps = pgn.getPs();
        this.total = total;
    }

    /**
     * 自我复制，并重新设置Data
     */
    public ServiceResult<T> duplicate(T data) {
        ServiceResult<T> serviceResult = new ServiceResult<>();
        serviceResult.setData(data);
        serviceResult.pn = this.getPn();
        serviceResult.ps = this.getPs();
        serviceResult.total = this.total;
        serviceResult.setCode(this.getCode());
        serviceResult.setMsg(this.getMsg());
        serviceResult.setCause(this.getCause());
        serviceResult.setAttachments(this.getAttachments());
        return serviceResult;
    }

}
