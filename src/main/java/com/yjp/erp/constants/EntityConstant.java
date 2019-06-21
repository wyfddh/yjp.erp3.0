package com.yjp.erp.constants;

public class EntityConstant {
    public final static String ENTITY_RECORD_EECAS_FORMAT="" +
            "<eecas xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"\"\n" +
            "       xsi:noNamespaceSchemaLocation=\"../../../../../framework/xsd/entity-eca-2.1.xsd\">\n" +
            "    <eeca entity=\"%realEntityName%\" on-create=\"true\">\n" +
            "        <actions>\n" +
            "            <service-call name=\"commonBase.baseService.addRecord\"/>\n" +
            "        </actions>\n" +
            "    </eeca>\n" +
            "</eecas>";

    public final static String NUMM="null";
}
