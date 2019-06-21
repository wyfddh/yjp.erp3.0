package com.yjp.erp.model.po.bill;

import lombok.Data;

import java.io.Serializable;

/**
 * @description 表单
 * @author liushui
 * @date 2019/3/21
 */
@Data
public class Bill implements Serializable {

    private Long id;

    private Long parentId;

    private Long moduleId;

    private String name;

    private String label;

    private String primaryKey;

    private String foreignKey;

    private Integer publishState;

}