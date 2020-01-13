package com.yangbo.es.model.es;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommonParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 索引名称
     */
    private String index;

    /**
     * 文档id
     */
    private String id;

    private Type type = Type.DOC;

    /**
     * 要保存或修改的数据，可以是对应实体bean、map或者转换后json字符串
     */
    private Object data;

    public enum Type {
        DOC, SCRIPT;
    }

    public CommonParam(String index, String id) {
        this.index = index;
        this.id = id;
    }

    public CommonParam(String index, Object data) {
        this.index = index;
        this.data = data;
    }

    public CommonParam(String index, String id, Object data) {
        this.index = index;
        this.id = id;
        this.data = data;
    }

}
