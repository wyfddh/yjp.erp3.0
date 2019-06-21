package com.yjp.erp.constants;
/**
* @Description:
* @CreateDate: 2019/4/4 16:20
* @EMAIL: jianghongping@yijiupi.com
*/
public enum ScriptEnum {
    /**
     * 通用脚本
     */
    COMMON(1),
    /**
     * 定制化脚本
     */
    CUSTOMIZATION(2);
    public int value;

    ScriptEnum(int value) {
        this.value = value;
    }
}
