package com.yjp.erp.constants;

/**
 * @author xialei
 * @date 2019/6/5 17:48
 */
public enum JsonTypeEnum {

    //对象
    OBJECT("object"),

    //数组
    ARRAY("Array");


    private String value;

    JsonTypeEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
