package com.yangbo.es.model.es;

import com.yangbo.es.constant.enums.SearchType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SimilarSearchParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 索引
     */
    private String index;

    /**
     * 要搜索key
     */
    private String key;

    /**
     * 搜索值
     */
    private String value;

    /**
     * 搜索类型
     */
    private SearchType searchType;

    /**
     * 返回条数
     */
    private int size;

    /**
     * 需要返回字段，如果list中只有一个字段并且名称为_all,则返回全字段
     */
    private List<String> returnKeys;

    /**
     * 暂时默认只支持and连接，全term查询
     */
    private Map<String, Object> filterParam;

}
