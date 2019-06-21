package com.yjp.erp.model.domain.fieldrule;

import java.util.List;

/**
 * @Description:
 * @CreateDate: 2019/4/13 14:31
 * @EMAIL: jianghongping@yijiupi.com
 */
public class OptionRules {

    private List<MappingRules> mappingRules;
    private String mappingType;
    private String type;
    private String classId;
    private String typeId;

    public void setMappingRules(List<MappingRules> mappingRules) {
        this.mappingRules = mappingRules;
    }

    public List<MappingRules> getMappingRules() {
        return mappingRules;
    }

    public String getMappingType() {
        return mappingType;
    }

    public void setMappingType(String mappingType) {
        this.mappingType = mappingType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassId() {
        return classId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeId() {
        return typeId;
    }

}