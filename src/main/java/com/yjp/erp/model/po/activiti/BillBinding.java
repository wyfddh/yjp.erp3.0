package com.yjp.erp.model.po.activiti;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 单据绑定表
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-04
 */

public class BillBinding implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 单据绑定id
	 */
	private Long id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 实体类型(如单据、业务实体)
	 */
	private String typeId;
	/**
	 * 单据名称(如入库申请单、库存)
	 */
	private String classId;
	/**
	 * 工作流
	 */
	private Long workflowId;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 创建人
	 */
	private Long creator;

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
		this.name = name;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	
	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public Long getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

}
