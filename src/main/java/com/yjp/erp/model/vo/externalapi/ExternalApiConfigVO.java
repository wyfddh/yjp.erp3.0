package com.yjp.erp.model.vo.externalapi;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xialei
 * @date 2019-06-17`
 */
@Data
public class ExternalApiConfigVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String classId;
    private String typeId;
    private String operateType;
    private String systemType;
    private byte status;
    private String description;
    private String method;
    private String url;
}