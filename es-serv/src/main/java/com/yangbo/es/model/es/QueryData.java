package com.yangbo.es.model.es;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QueryData<T> {

    private Long totalCount;
    private T data;
    private String scrollId;

}
