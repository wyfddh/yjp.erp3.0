package com.yjp.erp.constants;


/**
 * description: 枚举
 *
 * @author liushui
 * @date 2019/4/9
 */
public enum EntityClassEnum {

    //表单
    BILL_CLASS("bill"),

    //实体
    ENTITY_CLASS("entity"),

    //枚举
    ENUM_CLASS("enum"),

    //视图
    VIEW_CLASS("view");

    private String value;

    EntityClassEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
