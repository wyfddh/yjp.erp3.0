package com.yjp.erp.model.vo.gateway;

import java.io.Serializable;

/**
 * 应用平台待处理单据详情的按钮访问控制
 * Created by weibaichuan on 2019/4/12
 */
public class BillButtonPermissionVO implements Serializable {

    /**
     * 是否可提交
     */
    private Boolean canSubmit;
    /**
     * 是否可审核通过
     */
    private Boolean canRefuse;

    /**
     * 是否可审核拒绝
     */
    private Boolean canApply;

    /**
     * 获取 是否可提交
     *
     * @return canSubmit 是否可提交
     */
    public Boolean getCanSubmit() {
        return this.canSubmit;
    }

    /**
     * 设置 是否可提交
     *
     * @param canSubmit 是否可提交
     */
    public void setCanSubmit(Boolean canSubmit) {
        this.canSubmit = canSubmit;
    }

    /**
     * 获取 是否可审核通过
     *
     * @return canRefuse 是否可审核通过
     */
    public Boolean getCanRefuse() {
        return this.canRefuse;
    }

    /**
     * 设置 是否可审核通过
     *
     * @param canRefuse 是否可审核通过
     */
    public void setCanRefuse(Boolean canRefuse) {
        this.canRefuse = canRefuse;
    }

    /**
     * 获取 是否可审核拒绝
     *
     * @return canApply 是否可审核拒绝
     */
    public Boolean getCanApply() {
        return this.canApply;
    }

    /**
     * 设置 是否可审核拒绝
     *
     * @param canApply 是否可审核拒绝
     */
    public void setCanApply(Boolean canApply) {
        this.canApply = canApply;
    }
}
