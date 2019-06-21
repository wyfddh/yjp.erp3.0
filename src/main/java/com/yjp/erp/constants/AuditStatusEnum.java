package com.yjp.erp.constants;

public enum AuditStatusEnum {

    //新建
    NEWLY_BUILD(2),

    //待审核
    TO_BE_AUDITED(1),

    //已审核
    AUDITED(0),

    //已完成
    COMPLETED(10003);

    private Integer value;

    AuditStatusEnum(Integer value){
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
