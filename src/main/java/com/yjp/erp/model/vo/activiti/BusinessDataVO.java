package com.yjp.erp.model.vo.activiti;

import java.io.Serializable;


/**
 * @ClassName: BusinessDataVO
 * @Description: 业务数据返回实体
 * @author: jihongjin@yijiupi.com
 * @date: 2019年4月12日 上午11:47:04
 * @Copyright: 2019 www.yijiupi.com Inc. All rights reserved.
 */

public class BusinessDataVO implements Serializable {
    /**
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
     */
    private static final long serialVersionUID = 1L;
    /**
     * 实体类型id(如单据bill、业务实体entity、视图view)
     */
    private String classId;
    /**
     * 单据id
     */
    private String billId;
    /**
     * 实体表单名
     */
    private String billEntityName;
    /**
     * 单据名称id（如出库单、入库单）
     */
    private String typeId;

    private String billSummary;

    private String billDisplayName;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getBillSummary() {
        return billSummary;
    }

    public void setBillSummary(String billSummary) {
        this.billSummary = billSummary;
    }

    public String getBillDisplayName() {
        return billDisplayName;
    }

    public void setBillDisplayName(String billDisplayName) {
        this.billDisplayName = billDisplayName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillEntityName() {
        return billEntityName;
    }

    public void setBillEntityName(String billEntityName) {
        this.billEntityName = billEntityName;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

}
