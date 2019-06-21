package com.yjp.erp.constants;

public enum EntityPropertyEnum {

    //包名
    PACKAGE_NAME("packageName"),

    //是否缓存
    CACHE("cache"),

    //使用方式
    USE("use");

    private String value;

    EntityPropertyEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
