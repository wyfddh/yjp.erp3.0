package com.yjp.erp.model.domain.fieldrule;

/**
* @Description:
* @CreateDate: 2019/4/13 14:31
* @EMAIL: jianghongping@yijiupi.com
*/
public class MappingRules {
    /**
     * unuseful
     */
    private String fieldIndex;

    private String operator;
    private String originField;
    private String targetField;
    public void setOperator(String operator) {
         this.operator = operator;
     }
     public String getOperator() {
         return operator;
     }

    public void setOriginField(String originField) {
         this.originField = originField;
     }
     public String getOriginField() {
         return originField;
     }

    public void setTargetField(String targetField) {
         this.targetField = targetField;
     }
     public String getTargetField() {
         return targetField;
     }

    public String getFieldIndex() {
        return fieldIndex;
    }

    public void setFieldIndex(String fieldIndex) {
        this.fieldIndex = fieldIndex;
    }
}