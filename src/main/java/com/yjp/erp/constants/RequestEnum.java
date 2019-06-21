package com.yjp.erp.constants;

public enum RequestEnum {

    //更新请求
    UPDATE_REQUEST(2),
    //插入请求
    INSERT_REQUEST(1);

    private Integer value;

    RequestEnum(Integer value){
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
