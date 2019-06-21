package com.yjp.erp.constants;

public enum WebTypeEnum {

    //web数值类型
    WEB_NUMBER("number"),

    //web时间类型
    WEB_DATA("data"),

    //web文本类型
    WEB_TEXT("string"),

    //web序列类型
    WEB_SEQUENCE("sequence"),

    //web文件类型
    WEB_FILE("file");

    private String value;

    WebTypeEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
