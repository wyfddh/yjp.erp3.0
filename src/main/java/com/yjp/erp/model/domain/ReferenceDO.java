package com.yjp.erp.model.domain;

import lombok.Data;

/**
 * @author liushui
 * @description:
 * @date 2019/3/25
 */
@Data
public class ReferenceDO {

    private String classId;

    private String typeId;

    private String keyField;

    private String valueField;

    private String keyType;
}
