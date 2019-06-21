package com.yjp.erp.constants;

/**
 * @author wyf
 * @description   方法枚举
 */
public enum MethodEnum {

    /**
     * rest方法
     */
    GET("GET"),
    POST("POST"),
    DELETE("DELETE"),
    PUT("PUT");

    public String value;

    MethodEnum(String value){
        this.value = value;
    }
}
