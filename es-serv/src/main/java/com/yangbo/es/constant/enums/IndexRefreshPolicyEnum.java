package com.yangbo.es.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum IndexRefreshPolicyEnum {
    /**
     * Don't refresh after this request. The default.
     */
    NONE("false"),
    /**
     * Force a refresh as part of this request. This refresh policy does not scale for high indexing or search throughput but is useful
     * to present a consistent view to for indices with very low traffic. And it is wonderful for tests!
     */
    IMMEDIATE("true"),
    /**
     * Leave this request open until a refresh has made the contents of this request visible to search. This refresh policy is
     * compatible with high indexing and search throughput but it causes the request to wait to reply until a refresh occurs.
     */
    WAIT_UNTIL("wait_for");

    private String value;

}
