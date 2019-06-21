package com.yjp.erp.model.po.bill;

import java.util.Date;

public class BillModule {
    private Long id;

    private String name;

    private String description;

    private Date gmtCreate;

    private Date gmtModify;

    private Byte activeState;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    public Byte getActiveState() {
        return activeState;
    }

    public void setActiveState(Byte activeState) {
        this.activeState = activeState;
    }
}