package com.yjp.erp.constants;

/**
 * @author liushui
 * @description: TODO
 * @date 2019/3/27
 */
public interface MoquiProperties {

    String[] MOQUI_ELEMENTS = {"is-pk","type","default","column-name", "create-only","enable-audit-log","enable-localization","create-only","encrypt","not-null"};

    String[] DATES = {"date","time","date-time"};

    String[] NUMBERS = {"number-integer","number-decimal","number-float","currency-amount","currency-precise"};

    String[] TEXTS = {"text-indicator","text-short","text-medium","text-long","text-very-long"};

    String[] SEQUENCES = {"id","id-long"};

    String[] PROPERTIES = {"cache","packageName","use"};

    String MOQUI_FILE = "binary-very-long";

    String[] BOOLEAN_ELEMENT = {"is-pk","required","filterable"};
}
