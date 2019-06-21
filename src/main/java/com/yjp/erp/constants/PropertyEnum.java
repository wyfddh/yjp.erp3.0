package com.yjp.erp.constants;

/**
 * @author liushui
 * @date 2019/3/21
 */
public enum PropertyEnum {

    //moqui属性
    MOQUI(1),

    //web前端渲染属性
    WEB(2);

    public int code;

    PropertyEnum(int code){
        this.code = code;
    }
}
