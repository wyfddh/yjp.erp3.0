package com.yjp.erp.constants;

/**
 * 字段前端渲染元属性
 */
public enum FieldWebElementEnum {

    //字段名称
    element_name("name"),

    //字段标签
    ELEMENT_LABEL("label"),

    //字段类型
    ELEMENT_TYPE("type"),

    //字段是否用于过滤
    ELEMENT_FILTER("filterable");

    private String value;

    FieldWebElementEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
