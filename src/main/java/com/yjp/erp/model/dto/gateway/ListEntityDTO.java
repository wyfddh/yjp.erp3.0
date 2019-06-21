package com.yjp.erp.model.dto.gateway;

import com.yjp.erp.model.domain.PageInfoDO;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * description:
 *
 * @author liushui
 * @date 2019/4/18
 */
@Data
public class ListEntityDTO {

    @NotNull(message = "classId不能为空")
    private String classId;
    @NotNull(message = "typeId不能为空")
    private String typeId;
    @NotNull(message = "method不能为空")
    private String method;
    @NotNull(message = "url不能为空")
    private String url;

    private SearchParam data;

    @Data
    public static class SearchParam{

        private PageInfoDO pageInfo;

        private List<Condition> conditions;
    }

    @Data
    public static class Condition{

        private String fieldName;

        private Object value;

        private Object condition;
    }
}
