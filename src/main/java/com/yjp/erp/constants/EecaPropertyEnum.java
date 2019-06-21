package com.yjp.erp.constants;

/**
 * description: eeca触发时机枚举
 * @author liushui
 * @date 2019/4/9
 */
public enum EecaPropertyEnum {

    //运行出错时执行
    RUN_ON_ERROR("run-on-error"),

    //运行前执行
    RUN_BEFORE("run-before"),

    GET_ENTIRE_ENTITY("get-entire-entity"),

    GET_ORIGINAL_VALUE("get-original-value"),

    SET_RESULTS("set-results");

    private String value;

    EecaPropertyEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
