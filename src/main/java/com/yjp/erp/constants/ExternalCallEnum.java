package com.yjp.erp.constants;

/**
 * @author xialei
 * @date 2019/6/18 16:25
 */
public enum ExternalCallEnum {

    CREATE("create"),

    LIST("list"),

    QUERY("query"),

    //数组
    UPDATE("update");


    private String value;

    ExternalCallEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
