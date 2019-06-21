package com.yjp.erp.model.po.bill;

import lombok.Data;

@Data
public class BillFieldWebProperty {

    private Long id;

    private Long billFieldId;

    private String name;

    private String elementValue;

    /**
     * 字段引用实体的元数据
     */
    private BillFieldWebPropertyRel billFieldWebPropertyRel;

}