package com.yjp.erp.model.mq;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author xialei
 * @date 2019-06-04
 */
public class MqServiceIntegration implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private String name;
    private String classId;
    private String typeId;
    private byte status;
    private String exchangeName;
    private String queueName;
    private String parentEntity;
    private String childEntity;
    private String moquiUrl;
    private String description;
    private Date createTime;
    private Date lastUpdatedTime;
    private List<MqConversionRelation> relations;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setStatus(byte status) {
        this.status = status;
    }

    public byte getStatus() {
        return status;
    }

    public String getParentEntity() {
        return parentEntity;
    }

    public void setParentEntity(String parentEntity) {
        this.parentEntity = parentEntity;
    }

    public String getChildEntity() {
        return childEntity;
    }

    public void setChildEntity(String childEntity) {
        this.childEntity = childEntity;
    }

    public String getMoquiUrl() {
        return moquiUrl;
    }

    public void setMoquiUrl(String moquiUrl) {
        this.moquiUrl = moquiUrl;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public List<MqConversionRelation> getRelations() {
        return relations;
    }

    public void setRelations(List<MqConversionRelation> relations) {
        this.relations = relations;
    }
}