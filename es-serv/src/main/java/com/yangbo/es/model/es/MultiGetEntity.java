package com.yangbo.es.model.es;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MultiGetEntity {

    private List<DocInfo> docs;

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DocInfo {
        private String _index;
        private String _type;
        private String _id;
        private boolean found;
        private String error;
        private String _source;
    }

}
