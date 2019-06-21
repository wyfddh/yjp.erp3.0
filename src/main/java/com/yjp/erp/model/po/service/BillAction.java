package com.yjp.erp.model.po.service;

import lombok.Data;

@Data
public class BillAction {

    private Long id;

    private Integer level;

    private String label;

    private String button;

    private String icon;

    private String rpcUri;

    private String rpcMethod;

    private Long moduleId;

    private String beforeService;

    private String afterService;

    private Service refService;

    private Integer disableType;

    private String roleAuthorization;

}