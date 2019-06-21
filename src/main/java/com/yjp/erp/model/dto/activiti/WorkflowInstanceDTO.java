package com.yjp.erp.model.dto.activiti;

import java.io.Serializable;

/**
 * 
 * @ClassName: WorkflowInstanceDTO
 * @Description: 创建工作流实例--前台入参
 * @author: jihongjin@yijiupi.com
 * @date: 2019年4月9日 下午3:08:08
 * 
 * @Copyright: 2019 www.yijiupi.com Inc. All rights reserved.
 */
public class WorkflowInstanceDTO implements Serializable {
	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 实体类型id（bill、entity、view-必传）
	 */
	private String classId;
	/**
	 * 实体名称（表名-必传）
	 */
	private String typeId;
	/**
	 * 单据id（必传）
	 */
	private Long billId;
	/**
	 * 备注（非必传）
	 */
	private String remarks;
	
	/**
     * 组织Id（必传）
     */
    private String orgId;

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

}
