package com.yangbo.es.service;

import com.yangbo.es.constant.enums.IndexRefreshPolicyEnum;
import com.yangbo.es.model.common.RpcResult;
import com.yangbo.es.model.es.CUDParam;
import com.yangbo.es.model.es.CommonParam;
import com.yangbo.es.model.es.SimilarSearchParam;

import java.util.List;
import java.util.Map;

public interface ICommonService {

    RpcResult<List<Map<String, Object>>> similarSearch(SimilarSearchParam similarSearchParam);

    /**
     * 根据ID获取指定对象,可传入Map或者对应bean的class对象
     */
    <T> RpcResult<T> get(String index, String id, Class<T> clazz);

    /**
     * @param includes 要返回的字段
     * @param excludes 不需要返回的字段
     */
    <T> RpcResult<T> getData(String index, String id, Class<T> clazz, String[] includes, String[] excludes);

    <T> RpcResult<List<T>> getByIds(String index, List<String> ids, Class<T> clazz);

    /**
     * @param includes 要返回的字段
     * @param excludes 不需要返回的字段
     */
    <T> RpcResult<List<T>> getDataByIds(String index, List<String> ids, Class<T> clazz,
                                        String[] includes, String[] excludes);

    /**
     * 必传字段:index,data如果不传递ID ES内部会自动生成一个唯一ID.如果用此方法做修改操作，不需修改字段也需要带上原值
     */
    RpcResult<Void> save(CommonParam commonParam, boolean async);

    /**
     * 批量保存
     */
    RpcResult<Void> batchSave(List<CommonParam> commonParams, boolean async);

    /**
     * 必传:index,id,删除指定ID数据
     */
    RpcResult<Void> delete(CommonParam commonParam, boolean async);

    /**
     * 批量删除
     */
    RpcResult<Void> batchDelete(List<CommonParam> commonParams, boolean async);

    /**
     * 必传:index,id,data提供局部字段修改接口，请确保转换后json不含不需要修改的属性(如果含，请确保带上原值)!
     */
    RpcResult<Void> update(CommonParam commonParam, boolean async);

    /**
     * 带刷新策略和没有是否做插入操作参数
     */
    RpcResult<Void> upsert(CommonParam commonParam, boolean async, IndexRefreshPolicyEnum policyEnum, boolean upsert);

    /**
     * 批量修改
     */
    RpcResult<Void> batchUpdate(List<CommonParam> commonParams, boolean async);

    /**
     * 含刷新策略批量修改
     */
    RpcResult<Void> batchUpdateWithRefreshPolicy(List<CommonParam> commonParams, boolean async,
                                                 IndexRefreshPolicyEnum policyEnum);

    /**
     * 能同时执行CUD的批量操作
     */
    RpcResult<Void> batchCUD(List<CUDParam> cudParams, boolean async);

    /**
     * 主动清除对应scroll的search context能加快释放文件句柄、内存等
     */
    RpcResult<Void> clearScroll(List<String> scrollIds);
}
