package com.yangbo.es.service.impl;

import com.yangbo.es.constant.enums.IndexRefreshPolicyEnum;
import com.yangbo.es.constant.enums.OperateType;
import com.yangbo.es.dao.CommonDao;
import com.yangbo.es.model.common.RpcResult;
import com.yangbo.es.model.es.CUDParam;
import com.yangbo.es.model.es.CommonParam;
import com.yangbo.es.model.es.SimilarSearchParam;
import com.yangbo.es.service.ICommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

@Slf4j
@Service
public class CommonService implements ICommonService {

    @Resource
    private CommonDao commonDao;

    @Override
    public RpcResult<List<Map<String, Object>>> similarSearch(SimilarSearchParam similarSearchParam) {
        List<Map<String, Object>> list = commonDao.similarSearch(similarSearchParam);
        return RpcResult.success(list);
    }

    @Override
    public <T> RpcResult<T> get(String index, String id, Class<T> clazz) {
        return getData(index, id, clazz, null, null);
    }

    @Override
    public <T> RpcResult<T> getData(String index, String id, Class<T> clazz, String[] includes, String[] excludes) {
        T t = commonDao.get(index, id, clazz, includes, excludes);
        return RpcResult.success(t);
    }

    @Override
    public <T> RpcResult<List<T>> getByIds(String index, List<String> ids, Class<T> clazz) {
        return getDataByIds(index, ids, clazz, null, null);
    }

    @Override
    public <T> RpcResult<List<T>> getDataByIds(String index, List<String> ids, Class<T> clazz, String[] includes, String[] excludes) {
        List<T> list = null;
        if (isEmpty(ids)) {
            return RpcResult.success(list);
        }

        if (ids.size() == 1) {
            list = newArrayList();
            T t = commonDao.get(index, ids.get(0), clazz, includes, excludes);
            if (t != null) {
                list.add(t);
            }
            return RpcResult.success(list);
        }
        list = commonDao.getByIds(index, ids, clazz, includes, excludes);
        return RpcResult.success(list);
    }

    @Override
    public RpcResult<Void> save(CommonParam commonParam, boolean async) {
        commonDao.save(commonParam, async);
        return RpcResult.success(null);
    }

    @Override
    public RpcResult<Void> batchSave(List<CommonParam> commonParams, boolean async) {
        if (isNotEmpty(commonParams)) {
            if (commonParams.size() == 1) {
                commonDao.save(commonParams.get(0), async);
            } else {
                commonDao.batchSave(commonParams, async);
            }
        }
        return RpcResult.success(null);
    }

    @Override
    public RpcResult<Void> delete(CommonParam commonParam, boolean async) {
        commonDao.delete(commonParam, async);
        return RpcResult.success(null);
    }

    @Override
    public RpcResult<Void> batchDelete(List<CommonParam> commonParams, boolean async) {
        if (isNotEmpty(commonParams)) {
            if (commonParams.size() == 1) {
                commonDao.delete(commonParams.get(0), async);
            } else {
                commonDao.batchDelete(commonParams, async);
            }
        }
        return RpcResult.success(null);
    }

    @Override
    public RpcResult<Void> update(CommonParam commonParam, boolean async) {
        commonDao.update(commonParam, async, null, true);
        return RpcResult.success(null);
    }

    @Override
    public RpcResult<Void> upsert(CommonParam commonParam, boolean async,
                                  IndexRefreshPolicyEnum policyEnum, boolean upsert) {
        commonDao.update(commonParam, async, policyEnum, upsert);
        return RpcResult.success(null);
    }

    @Override
    public RpcResult<Void> batchUpdate(List<CommonParam> commonParams, boolean async) {
        return batchUpdateWithRefreshPolicy(commonParams, async, null);
    }

    @Override
    public RpcResult<Void> batchUpdateWithRefreshPolicy(List<CommonParam> commonParams, boolean async,
                                                        IndexRefreshPolicyEnum policyEnum) {
        if (isNotEmpty(commonParams)) {
            if (commonParams.size() == 1) {
                commonDao.update(commonParams.get(0), async, policyEnum, false);
            } else {
                commonDao.batchUpdate(commonParams, async, policyEnum);
            }
        }
        return RpcResult.success(null);
    }

    @Override
    public RpcResult<Void> batchCUD(List<CUDParam> cudParams, boolean async) {
        if (isNotEmpty(cudParams)) {
            if (cudParams.size() == 1) {
                CUDParam cudParam = cudParams.get(0);
                OperateType operateType = cudParam.getOperateType();
                switch (operateType) {
                    case CREATE:
                        commonDao.save(cudParam, async);
                        break;
                    case UPDATE:
                        commonDao.update(cudParam, async, null, false);
                        break;
                    case DELETE:
                        commonDao.delete(cudParam, async);
                        break;
                    default:
                        log.error("暂不支持的操作类型！");
                        break;
                }
            } else {
                commonDao.batchCUD(cudParams, async);
            }
        }
        return RpcResult.success(null);
    }

    @Override
    public RpcResult<Void> clearScroll(List<String> scrollIds) {
        boolean success = commonDao.clearScroll(scrollIds);
        if (success) {
            return RpcResult.success(null);
        }
        return RpcResult.error("error");
    }

}
