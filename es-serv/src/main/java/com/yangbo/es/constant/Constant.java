package com.yangbo.es.constant;

public interface Constant {

    String COLON = ":";
    String COMMA = ",";
    String SCHEME = "http";
    String ID_SEPARATOR = "@";
    String MGET = "_mget";

    /**
     * ES官网已经不建议一个index多个type, 所以目前每个index就一个type:_doc
     */
    String TYPE = "_doc";

    float MIN_SCORE = 0.0F;

    int MAX_SEARCH_SIZE = 10;

    String ALL_FIELDS = "_all";

    String PAINLESS = "painless";

    int HTTP_SUCCESS_CODE = 200;

    /**
     * ES分页允许的最大参数
     */
    int MAX_COUNT = 10000;

}
