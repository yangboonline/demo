package com.yangbo.es.dao;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yangbo.es.constant.Constant;
import com.yangbo.es.constant.enums.IndexRefreshPolicyEnum;
import com.yangbo.es.constant.enums.OperateType;
import com.yangbo.es.constant.enums.SearchType;
import com.yangbo.es.model.es.CUDParam;
import com.yangbo.es.model.es.CommonParam;
import com.yangbo.es.model.es.MultiGetEntity;
import com.yangbo.es.model.es.SimilarSearchParam;
import com.yangbo.es.util.EsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.prefixQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Slf4j
@Repository
public class CommonDao {

    @Resource
    private RestClient restClient;
    @Resource
    private RestHighLevelClient restHighLevelClient;

    /**
     * 相似搜索
     * @param similarSearchParam xiang'si
     * @return
     */
    public List<Map<String, Object>> similarSearch(SimilarSearchParam similarSearchParam) {
        QueryBuilder queryBuilder = null;
        if (SearchType.MATCH.equals(similarSearchParam.getSearchType())) {
            queryBuilder = matchQuery(similarSearchParam.getKey(), similarSearchParam.getValue());
        } else if (SearchType.LIKE.equals(similarSearchParam.getSearchType())) {
            queryBuilder = prefixQuery(similarSearchParam.getKey(), similarSearchParam.getValue());
        }
        if (Objects.isNull(queryBuilder)) {
            return Lists.newArrayList();
        }
        BoolQueryBuilder boolQueryBuilder = boolQuery().must(queryBuilder);

        if (MapUtils.isNotEmpty(similarSearchParam.getFilterParam())) {
            Set<Map.Entry<String, Object>> set = similarSearchParam.getFilterParam().entrySet();
            for (Map.Entry<String, Object> entry : set) {
                boolQueryBuilder.filter(termQuery(entry.getKey(), entry.getValue()));
            }
        }

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(boolQueryBuilder);

        List<String> returnParamList = similarSearchParam.getReturnKeys();

        searchSourceBuilder
                .size(Math.min(similarSearchParam.getSize(), Constant.MAX_SEARCH_SIZE));
        if (returnParamList.size() == 1 && Constant.ALL_FIELDS.equals(returnParamList.get(0))) {
            // 字段不做过滤，全部返回
        } else {
            searchSourceBuilder.fetchSource(returnParamList.toArray(new String[]{}), new String[]{});
        }

        if (log.isDebugEnabled()) {
            log.debug(searchSourceBuilder.toString());
        }
        SearchRequest searchRequest = new SearchRequest(similarSearchParam.getIndex()).source(searchSourceBuilder);
        SearchResponse searchResponse = EsUtil.search(restHighLevelClient, searchRequest);
        SearchHits hits = searchResponse.getHits();

        List<Map<String, Object>> list = newArrayList();
        for (SearchHit hit : hits.getHits()) {
            list.add(hit.getSourceAsMap());
        }
        return list;
    }

    public <T> T get(String indexName, String id, Class<T> clazz) {
        return get(indexName, id, clazz, null, null);
    }

    public <T> T get(String indexName, String id, Class<T> clazz, String[] includes, String[] excludes) {
        if (includes == null) {
            includes = Strings.EMPTY_ARRAY;
        }
        if (excludes == null) {
            excludes = Strings.EMPTY_ARRAY;
        }
        try {
            GetRequest getRequest = new GetRequest(indexName, Constant.TYPE, id)
                    .fetchSourceContext(new FetchSourceContext(true, includes, excludes));
            GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            if (getResponse.isExists()) {
                String sourceAsStr = getResponse.getSourceAsString();
                return JSON.parseObject(sourceAsStr, clazz);
            }
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    public <T> List<T> getByIds(String index, List<String> ids, Class<T> clazz) {
        return getByIds(index, ids, clazz, null, null);
    }

    public <T> List<T> getByIds(String index, List<String> ids, Class<T> clazz, String[] includes, String[] excludes) {
        String url = Joiner.on("/").join(index, Constant.TYPE, Constant.MGET);

        if (includes == null) {
            includes = Strings.EMPTY_ARRAY;
        }
        if (excludes == null) {
            excludes = Strings.EMPTY_ARRAY;
        }
        Map<String, String[]> source = Maps.newHashMap();
        source.put("includes", includes);
        source.put("excludes", excludes);

        List<Map<String, Object>> params = Lists.newArrayListWithCapacity(ids.size());
        for (String id : ids) {
            Map<String, Object> param = Maps.newHashMap();
            param.put("_source", source);
            param.put("_id", id);
            params.add(param);
        }

        HttpEntity entity = new NStringEntity(JSON.toJSONString(Collections.singletonMap("docs", params)),
                ContentType.APPLICATION_JSON);
        List<T> list = newArrayList();
        try {
            Request request = new Request(HttpMethod.GET.toString(), url);
            request.setEntity(entity);
            Response response = restClient.performRequest(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == Constant.HTTP_SUCCESS_CODE) {
                MultiGetEntity multiGetEntity = com.alibaba.fastjson.JSON
                        .parseObject(EntityUtils.toString(response.getEntity()), MultiGetEntity.class);
                for (MultiGetEntity.DocInfo docInfo : multiGetEntity.getDocs()) {
                    if (docInfo.getError() == null && docInfo.isFound()
                            && StringUtils.isNotEmpty(docInfo.get_source())) {
                        T t = JSON.parseObject(docInfo.get_source(), clazz);
                        list.add(t);
                    }
                }
            }
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }

        return list;
    }

    public void save(CommonParam commonParam, boolean async) {

        IndexRequest indexRequest = EsUtil.buildIndexRequest(commonParam.getIndex(), commonParam.getId(),
                commonParam.getData());

        if (async) {
            restHighLevelClient.indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
                @Override
                public void onResponse(IndexResponse indexResponse) {
                    List<String> msgs = EsUtil.getResponseWarnings(indexResponse);
                    if (isNotEmpty(msgs)) {
                        log.warn("索引：{}，id：{}，数据：{}，有分片保存失败：{}", commonParam.getIndex(),
                                commonParam.getId(), JSON.toJSONString(commonParam), msgs);
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    log.error("索引：{}，id：{}，数据：{}，保存失败：{}", commonParam.getIndex(),
                            commonParam.getId(), JSON.toJSONString(commonParam), ExceptionUtils.getStackTrace(e));
                }
            });
        } else {
            try {
                IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
                List<String> msgs = EsUtil.getResponseWarnings(indexResponse);
                if (isNotEmpty(msgs)) {
                    log.warn("索引：{}，id：{}，数据：{}，有分片保存失败：{}", commonParam.getIndex(),
                            commonParam.getId(), JSON.toJSONString(commonParam), msgs);
                }
            } catch (IOException e) {
                log.error(ExceptionUtils.getStackTrace(e));
                throw new RuntimeException(e);
            }
        }

    }

    public void batchSave(List<CommonParam> commonParams, boolean async) {

        BulkRequest request = new BulkRequest();
        for (CommonParam commonParam : commonParams) {
            IndexRequest indexRequest = EsUtil.buildIndexRequest(commonParam.getIndex(), commonParam.getId(),
                    commonParam.getData());
            request.add(indexRequest);
        }
        if (async) {
            EsUtil.bulkAsync(request, restHighLevelClient, commonParams);
        } else {
            EsUtil.bulk(request, restHighLevelClient);
        }
    }

    public void delete(CommonParam commonParam, boolean async) {
        DeleteRequest deleteRequest = new DeleteRequest(commonParam.getIndex(), Constant.TYPE, commonParam.getId());

        if (async) {
            restHighLevelClient.deleteAsync(deleteRequest, RequestOptions.DEFAULT, new ActionListener<DeleteResponse>() {
                @Override
                public void onResponse(DeleteResponse deleteResponse) {
                    List<String> msgs = EsUtil.getResponseWarnings(deleteResponse);
                    if (isNotEmpty(msgs)) {
                        log.warn("索引：{}，id：{}，有分片删除失败：{}",
                                commonParam.getIndex(), commonParam.getId(), msgs);
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    log.error("索引：{}，id：{}，删除失败：{}",
                            commonParam.getIndex(), commonParam.getId(), ExceptionUtils.getStackTrace(e));
                }
            });
        } else {
            try {
                DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
                List<String> msgs = EsUtil.getResponseWarnings(deleteResponse);
                if (isNotEmpty(msgs)) {
                    log.warn("索引：{}，id：{}，有分片删除失败：{}",
                            commonParam.getIndex(), commonParam.getId(), msgs);
                }
            } catch (IOException e) {
                log.error(ExceptionUtils.getStackTrace(e));
                throw new RuntimeException(e);
            }
        }
    }

    public void batchDelete(List<CommonParam> commonParams, boolean async) {
        BulkRequest request = new BulkRequest();
        for (CommonParam commonParam : commonParams) {
            DeleteRequest deleteRequest = new DeleteRequest(commonParam.getIndex(), Constant.TYPE, commonParam.getId());
            request.add(deleteRequest);
        }
        if (async) {
            EsUtil.bulkAsync(request, restHighLevelClient, commonParams);
        } else {
            EsUtil.bulk(request, restHighLevelClient);
        }
    }

    public void update(CommonParam commonParam, boolean async, IndexRefreshPolicyEnum policyEnum, boolean upsert) {
        if (policyEnum == null) {
            policyEnum = IndexRefreshPolicyEnum.NONE;
        }
        UpdateRequest updateRequest = EsUtil.buildUpdateRequest(commonParam, upsert)
                .setRefreshPolicy(WriteRequest.RefreshPolicy.parse(policyEnum.getValue()));

        if (async) {
            restHighLevelClient.updateAsync(updateRequest, RequestOptions.DEFAULT, new ActionListener<UpdateResponse>() {

                @Override
                public void onResponse(UpdateResponse updateResponse) {
                    List<String> msgs = EsUtil.getResponseWarnings(updateResponse);
                    if (isNotEmpty(msgs)) {
                        log.warn("索引：{}，id：{}，数据：{}，有分片更新失败：{}", commonParam.getIndex(),
                                commonParam.getId(), commonParam.getData(), msgs);
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    log.error("索引：{}，id：{}，数据：{}，更新失败：{}", commonParam.getIndex(),
                            commonParam.getId(), commonParam.getData(), ExceptionUtils.getStackTrace(e));
                }
            });
        } else {
            try {
                UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
                List<String> msgs = EsUtil.getResponseWarnings(updateResponse);
                if (isNotEmpty(msgs)) {
                    log.warn("索引：{}，id：{}，数据：{}，有分片更新失败：{}",
                            commonParam.getIndex(), commonParam.getId(), commonParam.getData(), msgs);
                }
            } catch (IOException e) {
                log.error("update失败，参数：{}, exception：{}", JSON.toJSONString(commonParam),
                        ExceptionUtils.getStackTrace(e));
                throw new RuntimeException(e);
            }
        }

    }

    public void batchUpdate(List<CommonParam> commonParams, boolean async, IndexRefreshPolicyEnum policyEnum) {
        if (policyEnum == null) {
            policyEnum = IndexRefreshPolicyEnum.NONE;
        }
        BulkRequest request = new BulkRequest().setRefreshPolicy(WriteRequest.RefreshPolicy.parse(policyEnum.getValue()));

        for (CommonParam commonParam : commonParams) {
            UpdateRequest updateRequest = EsUtil.buildUpdateRequest(commonParam, false);
            request.add(updateRequest);
        }
        if (async) {
            EsUtil.bulkAsync(request, restHighLevelClient, commonParams);
        } else {
            EsUtil.bulk(request, restHighLevelClient);
        }
    }

    public void batchCUD(List<CUDParam> cudParams, boolean async) {
        BulkRequest request = new BulkRequest();
        for (CUDParam cudParam : cudParams) {
            OperateType operateType = cudParam.getOperateType();
            switch (operateType) {
                case CREATE:
                    IndexRequest indexRequest = EsUtil.buildIndexRequest(cudParam.getIndex(), cudParam.getId(),
                            cudParam.getData());
                    request.add(indexRequest);
                    break;
                case UPDATE:
                    UpdateRequest updateRequest = EsUtil.buildUpdateRequest(cudParam, false);
                    request.add(updateRequest);
                    break;
                case DELETE:
                    DeleteRequest deleteRequest = new DeleteRequest(cudParam.getIndex(), Constant.TYPE, cudParam.getId());
                    request.add(deleteRequest);
                    break;
                default:
                    log.error("暂不支持的操作类型！");
                    break;
            }
        }
        if (async) {
            EsUtil.bulkAsync(request, restHighLevelClient, cudParams);
        } else {
            EsUtil.bulk(request, restHighLevelClient);
        }
    }

    public Boolean clearScroll(List<String> scrollIds) {
        ClearScrollResponse response = EsUtil.clearScroll(restHighLevelClient, scrollIds);
        return response.isSucceeded();
    }

}
