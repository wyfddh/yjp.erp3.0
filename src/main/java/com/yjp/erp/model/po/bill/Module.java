package com.yjp.erp.model.po.bill;

import lombok.Data;

@Data
public class Module {

    private Long id;

    private String classId;

    private String typeId;

    private Integer activeState;

    private Integer publishState;
}