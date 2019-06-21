package com.yjp.erp.service.parsexml.model;

import lombok.Data;

import java.util.List;

/**
 * @Description: 实体转xml的所有成员数据
 * @Author: 江洪平
 * @CreateDate: 2019/5/22 19:49
 * @Email: jianghongping@yijiupi.com
 */
@Data
public class EntityParseMember {
    private String entityName;
    private String childEntityName;
    private List<ElementAttribute> entityAttributes;
    private List<ElementAttribute> childEntityAttributes;
    private List<EntityField> entityFields;
    private List<EntityField> childEntityFields;
    private List<EntityService> entityServices;
    private List<EntityAction> entityActions;
    private List<EntityEeca> entityEecas;

    @Data
    public class EntityField {
        private String fieldName;
        private String lable;
        private List<ElementAttribute> elementAttributes;
    }

    @Data
    public class ElementAttribute {
        private String name;
        private String value;
    }

    @Data
    public class EntityService {
        private String verb;
        private String location;
        private String script;
    }

    @Data
    public class EntityAction {
        private String firstResource;
        private String secondResource;
        private String callServiceUrl;
    }

    @Data
    public class EntityEeca {
        private List<String> triggers;
        private String entityName;
        private String callServiceUrl;
    }
}
