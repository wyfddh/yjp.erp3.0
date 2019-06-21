package com.yjp.erp.model.dto.resource;

import lombok.Data;

import java.util.List;

/**
 * @author liushui
 * description:
 * @date 2019/3/28
 */
@Data
public class MenuRelatedInfoDTO {

    private Long nodeId;

    private String classId;

    private String typeId;

    private String routePath;

    private String icon;

    private Integer activeState;

    private List<TableField> searchFields;

    private List<TableField> displayFields;

    @Data
    public static class TableField{

        private String filedName;

        private String order;
    }
}
