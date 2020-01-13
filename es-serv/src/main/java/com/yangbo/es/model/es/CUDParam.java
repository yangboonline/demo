package com.yangbo.es.model.es;

import com.yangbo.es.constant.enums.OperateType;
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
public class CUDParam extends CommonParam {

    private OperateType operateType;

    public CUDParam(String index, String id, OperateType operateType) {
        super(index, id);
        this.operateType = operateType;
    }

    public CUDParam(String index, Object data, OperateType operateType) {
        super(index, data);
        this.operateType = operateType;
    }

    public CUDParam(String index, String id, Object data, OperateType operateType) {
        super(index, id, data);
        this.operateType = operateType;
    }

}
