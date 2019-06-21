package com.yjp.erp.model.po.activiti;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;

/**
 * <p>
 * 审核配置表
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-03
 */

public class AuditConfig implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 实体名称id（如出库单、入库单）
	 */
	private String typeId;
	/**
	 * 实体类型id(如单据bill、业务实体entity)
	 */
	private String classId;
	/**
	 * 组织或区域id
	 */
	@TableField(value = "area_id")
	private Long orgId;

	/**
	 * 工作流id
	 */
	private Long workflowId;

	/**
	 * 一级审核组(用户id,多个逗号分隔)
	 */
	private String firstLevelGroup;

	/**
	 * 二级审核组(用户id,多个逗号分隔)
	 */
	private String secondLevelGroup;

	/**
	 * 三级审核组(用户id,多个逗号分隔)
	 */
	private String thirdLevelGroup;

	/**
	 * 创建时间
	 */
	private Date createDate;

	/**
	 * 创建者
	 */
	private Long creator;

	/**
	 * 描述
	 */
	private String description;
	/**
	 * 状态(1:启用 0:禁用)
	 */
	private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}

	public String getFirstLevelGroup() {
		return firstLevelGroup;
	}

	public void setFirstLevelGroup(String firstLevelGroup) {
		this.firstLevelGroup = firstLevelGroup;
	}

	public String getSecondLevelGroup() {
		return secondLevelGroup;
	}

	public void setSecondLevelGroup(String secondLevelGroup) {
		this.secondLevelGroup = secondLevelGroup;
	}

	public String getThirdLevelGroup() {
		return thirdLevelGroup;
	}

	public void setThirdLevelGroup(String thirdLevelGroup) {
		this.thirdLevelGroup = thirdLevelGroup;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
