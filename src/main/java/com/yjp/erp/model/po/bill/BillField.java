package com.yjp.erp.model.po.bill;

import lombok.Data;

import java.util.List;


@Data
public class BillField {

    private Long id;

    private Long billId;

    private String fieldName;

    private String label;

    private String rules;

    /**
     * 实体字段的moqui 元素属性集合
     */
    private List<BillFieldProperty> billFieldProperties;

    /**
     * 实体web渲染属性的集合
     */
    private List<BillFieldWebProperty> billFieldWebProperties;

}