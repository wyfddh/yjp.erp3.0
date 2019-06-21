package com.yjp.erp.constants;

public enum ModuleEnum {
    /**
     * 可用
     */
    USEFUL(1),
    /**
     * 不可用
     */
    UN_USEFUL(0);

    public int value;
    ModuleEnum(int value ) {
        this.value = value;
    }
}
