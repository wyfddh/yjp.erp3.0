package com.yjp.erp.model.domain;

import lombok.Data;

/**
 * @author liushui
 * @description: 服务dto
 * @date 2019/3/22
 */
@Data
public class ServicePropertyDO {

    private Long id;

    private String label;

    private String serviceName;

    private String verb;

    private String noun;

    private String script;

    private String rpcUrl;

    private String roleAuthorization;
    /**
     * 1：定制化
     * 2：通用
     */
    private Integer serviceType;

}
