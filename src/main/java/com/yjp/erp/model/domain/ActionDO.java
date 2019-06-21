package com.yjp.erp.model.domain;

import lombok.Data;

/**
 * @author liushui
 * @description
 * @date 2019/3/22
 */
@Data
public class ActionDO {

    private Long id;

    private String actionName;

    private String label;

    private String icon;

    private String serviceName;

    private String rpcUrl;

    private String beforeService;

    private String afterService;

    private String rpcMethod;

    private Integer disableType;

    private String roleAuthorization;
}
