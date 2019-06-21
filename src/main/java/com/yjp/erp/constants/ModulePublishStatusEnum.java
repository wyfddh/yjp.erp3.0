package com.yjp.erp.constants;

public enum ModulePublishStatusEnum {

    //已发布
    published(1),

    //未发布
    unpublished(0);

    private Integer state;

    ModulePublishStatusEnum(Integer state){
        this.state = state;
    }

    public Integer getState() {
        return state;
    }
}
