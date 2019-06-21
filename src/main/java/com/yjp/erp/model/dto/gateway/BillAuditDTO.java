package com.yjp.erp.model.dto.gateway;

import java.io.Serializable;

/**
 * 单据审核处理对象
 * Created by weibaichuan on 2019/4/12
 */
public class BillAuditDTO implements Serializable {

    /**
     * 任务Id
     */
    private String taskId;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 提交不传，审核通过传true，审核拒绝传false
     */
    private Boolean variables;

    /**
     * 获取 任务Id
     *
     * @return taskId 任务Id
     */
    public String getTaskId() {
        return this.taskId;
    }

    /**
     * 设置 任务Id
     *
     * @param taskId 任务Id
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * 获取 用户Id
     *
     * @return userId 用户Id
     */
    public String getUserId() {
        return this.userId;
    }

    /**
     * 设置 用户Id
     *
     * @param userId 用户Id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取 备注
     *
     * @return remarks 备注
     */
    public String getRemarks() {
        return this.remarks;
    }

    /**
     * 设置 备注
     *
     * @param remarks 备注
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * 获取 提交不传，审核通过传true，审核拒绝传false
     *
     * @return variables 提交不传，审核通过传true，审核拒绝传false
     */
    public Boolean getVariables() {
        return this.variables;
    }

    /**
     * 设置 提交不传，审核通过传true，审核拒绝传false
     *
     * @param variables 提交不传，审核通过传true，审核拒绝传false
     */
    public void setVariables(Boolean variables) {
        this.variables = variables;
    }
}
