package com.yjp.erp.model.po.bill;

import lombok.Data;

@Data
public class BillFieldWebPropertyRel {

    private Integer id;

    private Long fieldWebId;

    private String tableName;

    private String srcField;

    private String destField;

    private String classId;

    private String typeId;

    private String keyType;

}