package com.yjp.erp.constants;

/**
 * @Description
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/3/22
 */
public class XmlConstant {

    public static final String XML_HEAD_1 = "xmlns:xsi";
    public static final String XML_HEAD_2 = "xsi:noNamespaceSchemaLocation";

    public static final String XML_HEAD_1_VALUE = "http://www.w3.org/2001/XMLSchema-instance";

    public static final String XML_HEAD_SERVICE_VALUE = "../service-definition-2.1.xsd";
    public static final String XML_HEAD_ENTITY_VALUE = "../entity-definition-2.1.xsd";
    public static final String XML_HEAD_RESOURCE_VALUE = "../rest-api-2.1.xsd";
    public static final String XML_HEAD_EECAS_VALUE ="../entity-eca-2.1.xsd";


    public static final String XML_LABEL_ENTITY_ROOT = "entities";
    public static final String XML_LABEL_ENTITY_ENTITY = "entity";
    public static final String XML_LABEL_ENTITY_FIELD = "field";
    public static final String XML_LABEL_ENTITY_RELATIONSHIP = "relationship";

    public static final String XML_LABEL_SERVICE_ROOT = "services";
    public static final String XML_LABEL_SERVICE_SSERVICE = "service";


    public static final String XML_LABEL_RESOURCE_ROOT = "resource";
    public static final String XML_LABEL_RESOURCE_RESOURCE = "resource";

    public static final String XML_LABEL_RESOURCE_NAME_SEPARATOR = "#";

    public static final String XML_LABEL_ENTITY_KEY_MAP = "key-map";
    //视图xml
    public static final String XML_LABEL_VIEW_ENTITY = "view-entity";
    public static final String XML_LABEL_VIEW_MEMBER_ENTITY = "member-entity";
    public static final String XML_LABEL_VIEW_ALIAS_ALL = "alias-all";
    public static final String XML_LABEL_VIEW_ALIAS = "alias";

    public static final String XML_LOCATION = "component://yjp-erp/script/";




}
