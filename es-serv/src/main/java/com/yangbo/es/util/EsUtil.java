package com.yangbo.es.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.yangbo.es.constant.Constant;
import com.yangbo.es.constant.enums.ErrorCodeEnum;
import com.yangbo.es.exception.BusinessException;
import com.yangbo.es.model.common.Pagination;
import com.yangbo.es.model.common.ServiceResult;
import com.yangbo.es.model.es.CommonParam;
import com.yangbo.es.model.es.QueryData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public class EsUtil {

    public static List<String> getResponseWarnings(DocWriteResponse docWriteResponse) {
        Assert.notNull(docWriteResponse, "docWriteResponse对象不能为null.");
        List<String> list = Lists.newArrayList();
        ReplicationResponse.ShardInfo shardInfo = docWriteResponse.getShardInfo();
        if (shardInfo.getFailed() > 0) {
            for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                list.add(failure.reason());
            }
        }
        return list;
    }


    public static SearchResponse search(RestHighLevelClient restHighLevelClient, SearchRequest searchRequest) {
        Assert.notNull(restHighLevelClient, "RestHighLevelClient对象不能为null.");
        Assert.notNull(searchRequest, "SearchRequest对象不能为null.");
        SearchResponse searchResponse;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new BusinessException(ErrorCodeEnum.FAILED_ERROR.getCode(), e.getMessage());
        }
        return searchResponse;
    }

    public static SearchResponse search(RestHighLevelClient restHighLevelClient, SearchScrollRequest scrollRequest) {
        Assert.notNull(restHighLevelClient, "RestHighLevelClient对象不能为null.");
        Assert.notNull(scrollRequest, "SearchScrollRequest对象不能为null.");
        SearchResponse searchResponse;
        try {
            searchResponse = restHighLevelClient.searchScroll(scrollRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new BusinessException(ErrorCodeEnum.FAILED_ERROR.getCode(), e.getMessage());
        }
        return searchResponse;
    }

    public static ClearScrollResponse clearScroll(RestHighLevelClient restHighLevelClient, List<String> scrollIds) {
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.setScrollIds(scrollIds);
        ClearScrollResponse clearScrollResponse;
        try {
            clearScrollResponse = restHighLevelClient.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new BusinessException(ErrorCodeEnum.FAILED_ERROR.getCode(), e.getMessage());
        }
        return clearScrollResponse;
    }

    public static XContentBuilder buildKeywordType(XContentBuilder builder, String name) throws IOException {
        return builder.startObject(name).field("type", "keyword").endObject();
    }

    public static XContentBuilder buildByteType(XContentBuilder builder, String name) throws IOException {
        return builder.startObject(name).field("type", "byte").endObject();
    }

    public static XContentBuilder buildIntegerType(XContentBuilder builder, String name) throws IOException {
        return builder.startObject(name).field("type", "integer").endObject();
    }

    public static XContentBuilder buildLongType(XContentBuilder builder, String name) throws IOException {
        return builder.startObject(name).field("type", "long").endObject();
    }

    public static XContentBuilder buildDoubleType(XContentBuilder builder, String name) throws IOException {
        return builder.startObject(name).field("type", "double").endObject();
    }

    public static XContentBuilder buildDateType(XContentBuilder builder, String name) throws IOException {
        return builder.startObject(name).field("type", "date").endObject();
    }

    public static XContentBuilder buildDefaultTextType(XContentBuilder builder, String name) throws IOException {
        return builder.startObject(name).field("type", "text").startObject("fields").startObject("keyword")
                .field("type", "keyword").field("ignore_above", 256).endObject().endObject().endObject();
    }

    public static XContentBuilder buildSmartCNType(XContentBuilder builder, String name) throws IOException {
        return builder.startObject(name).field("type", "text").field("analyzer", "smartcn").startObject("fields")
                .startObject("keyword").field("type", "keyword").field("ignore_above", 256).endObject().endObject()
                .endObject();
    }

    public static XContentBuilder buildNgramType(XContentBuilder builder, String name) throws IOException {
        return builder.startObject(name).field("type", "text").field("analyzer", "ngram_analyzer").startObject("fields")
                .startObject("keyword").field("type", "keyword").field("ignore_above", 256).endObject().endObject()
                .endObject();
    }

    /**
     * 判断索引是否存在
     */
    public static boolean indexNotExists(RestClient restClient, String index) throws IOException {
        Response response = restClient.performRequest(new Request(HttpMethod.HEAD.toString(), index));
        return response.getStatusLine().getStatusCode() != 200;
    }

    public static long getTime(Date date) {
        if (date == null) {
            return System.currentTimeMillis();
        }
        return date.getTime();
    }

    public static String object2jsonStr(Object data) {
        String jsonData = null;
        if (data instanceof String) {
            jsonData = (String) data;
        } else {
            jsonData = JSON.toJSONString(data);
        }
        return jsonData;
    }

    public static IndexRequest buildIndexRequest(String index, String id, Object data) {
        IndexRequest indexRequest = null;
        if (StringUtils.isEmpty(id)) {
            indexRequest = new IndexRequest(index, Constant.TYPE);
        } else {
            indexRequest = new IndexRequest(index, Constant.TYPE, id);
        }
        String jsonData = object2jsonStr(data);
        indexRequest.source(jsonData, XContentType.JSON);
        return indexRequest;
    }

    /**
     * 通用批量写入操作
     *
     * @param data 批量保存数据，失败时记录日志使用,请覆盖对象toString方法
     */
    public static void bulkAsync(BulkRequest request, RestHighLevelClient restHighLevelClient, Object data) {
        restHighLevelClient.bulkAsync(request, RequestOptions.DEFAULT, new ActionListener<BulkResponse>() {
            @Override
            public void onResponse(BulkResponse bulkResponse) {
                bulkResponseProcess(bulkResponse);
            }

            @Override
            public void onFailure(Exception e) {
                log.error("批量操作对象：{}失败，错误信息：{}", data, ExceptionUtils.getStackTrace(e));
            }
        });
    }

    /**
     * 通用批量写入操作(同步)
     */
    public static void bulk(BulkRequest request, RestHighLevelClient restHighLevelClient) {
        try {
            BulkResponse bulkResponse = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
            bulkResponseProcess(bulkResponse);
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }

    public static void bulkResponseProcess(BulkResponse bulkResponse) {
        if (bulkResponse.hasFailures()) {
            for (BulkItemResponse bulkItemResponse : bulkResponse) {
                if (bulkItemResponse.isFailed()) {
                    BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                    log.warn(failure.toString());
                }
            }
        }
    }

    public static UpdateRequest buildUpdateRequest(CommonParam param, boolean upsert) {
        UpdateRequest updateRequest = new UpdateRequest(param.getIndex(), Constant.TYPE, param.getId());
        if (param.getType() == CommonParam.Type.DOC) {
            updateRequest.doc(object2jsonStr(param.getData()), XContentType.JSON);
        } else if (param.getType() == CommonParam.Type.SCRIPT) {
            Script inline = new Script(param.getData().toString());
            updateRequest.script(inline);
        }

        if (upsert) {
            IndexRequest indexRequest = buildIndexRequest(param.getIndex(), param.getId(), object2jsonStr(param.getData()));
            updateRequest.upsert(indexRequest);
        }

        return updateRequest;
    }

    public static UpdateRequest buildUpdateRequest(String index, String id, Object data) {
        String jsonData = object2jsonStr(data);
        return new UpdateRequest(index, Constant.TYPE, id).doc(jsonData, XContentType.JSON);
    }

    public static <T> ServiceResult<T> queryData2RpcResult(QueryData<T> queryData, Pagination pgn) {
        ServiceResult<T> result = new ServiceResult<>();
        if (pgn != null) {
            result.setPagination(pgn, queryData.getTotalCount().intValue());
        }
        result.setData(queryData.getData());
        return result;
    }

    public static String getHighlightValue(Map<String, HighlightField> highlightFields, String key) {
        HighlightField highlight = highlightFields.get(key);
        if (highlight != null) {
            return highlight.getFragments()[0].toString();
        }
        return null;
    }

}
