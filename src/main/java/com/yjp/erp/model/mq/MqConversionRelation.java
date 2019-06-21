package com.yjp.erp.model.mq;

import java.io.Serializable;

/**
 * Author xialei
 * Date  2019-06-04
 */
public class MqConversionRelation implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private long mqId;
    private String sourceField;
    private String targetField;


    public void setId (long id) {this.id = id;} 
    public long getId(){ return id;} 
    public void setMqId (long mqId) {this.mqId = mqId;} 
    public long getMqId(){ return mqId;}

    public String getSourceField() {
        return sourceField;
    }

    public void setSourceField(String sourceField) {
        this.sourceField = sourceField;
    }

    public void setTargetField (String targetField) {this.targetField = targetField;}
    public String getTargetField(){ return targetField;} 

}