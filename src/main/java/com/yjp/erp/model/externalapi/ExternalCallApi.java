package com.yjp.erp.model.externalapi;

import com.yjp.erp.model.domain.PageInfoDO;
import com.yjp.erp.model.dto.gateway.ListEntityDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author xialei
 * @date 2019/6/17 16:52
 */

@Data
public class ExternalCallApi implements Serializable {
    @NotNull(message = "classId不能为空")
    private String classId;
    @NotNull(message = "typeId不能为空")
    private String typeId;

    private String operateType;

    private String systemType;

    private ListEntityDTO.SearchParam data;

    @Data
    public static class SearchParam{

        private PageInfoDO pageInfo;

        private List<ListEntityDTO.Condition> conditions;
    }

    @Data
    public static class Condition{

        private String fieldName;

        private Object value;

        private Object condition;
    }



}
