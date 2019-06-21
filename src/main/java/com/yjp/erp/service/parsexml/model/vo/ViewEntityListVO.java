package com.yjp.erp.service.parsexml.model.vo;

import lombok.Data;

@Data
public class ViewEntityListVO {
    private String classId;
    private String typeId;
    private String entityName;
    private String displayName;
    private Long createTime;
    private Long latestUpdateTime;
    private Integer status;

}
