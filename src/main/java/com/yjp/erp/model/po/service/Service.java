package com.yjp.erp.model.po.service;

import lombok.Data;

@Data
public class Service {

    private Long id;
    /**
     * 后来添加因应该标记 service 是属于哪个module
     */
    private Long moduleId;

    private String serviceName;

    private String label;

    private String verb;

    private String noun;

    private String location;

    private String script;

    private Integer serviceType;

    private String roleAuthorization;
}