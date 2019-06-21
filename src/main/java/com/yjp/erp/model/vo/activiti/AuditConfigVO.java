package com.yjp.erp.model.vo.activiti;

import java.io.Serializable;
import java.util.Date;

import com.yjp.erp.model.vo.PrimaryKeyVO;

/**
 * <p>
 * 审核配置表返回实体
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-03
 */
public class AuditConfigVO extends PrimaryKeyVO implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
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
	private Long orgId;
	/**
	 * 组织或区域名称
	 */
	private String orgName;
	/**
	 * 工作流id
	 */
	private Long workflowId;
	/**
	 * 工作流名称
	 */
	private String workflowName;
	/**
	 * 一级审核组(用户id,多个逗号分隔)
	 */
	private String firstLevelGroup;
	/**
	 * 一级审核组名称(用户名,多个逗号分隔)
	 */
	private String firstLevelGroupName;
	/**
	 * 二级审核组(用户id,多个逗号分隔)
	 */
	private String secondLevelGroup;
	/**
	 * 二级审核组名称(用户名,多个逗号分隔)
	 */
	private String secondLevelGroupName;
	/**
	 * 三级审核组(用户id,多个逗号分隔)
	 */
	private String thirdLevelGroup;
	/**
	 * 三级审核组名称(用户名,多个逗号分隔)
	 */
	private String thirdLevelGroupName;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 创建时间（yyyy-MM-dd HH:mm:ss）
	 */
	private String createDateStr;
	/**
	 * 创建者
	 */
	private Long creator;
	/**
	 * 创建者姓名
	 */
	private String creatorName;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 状态(1:启用 0:禁用)
	 */
	private Integer status;
	/**
	 * 状态名称
	 */
	private String statusName;

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

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Long getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public String getFirstLevelGroup() {
		return firstLevelGroup;
	}

	public void setFirstLevelGroup(String firstLevelGroup) {
		this.firstLevelGroup = firstLevelGroup;
	}

	public String getFirstLevelGroupName() {
		return firstLevelGroupName;
	}

	public void setFirstLevelGroupName(String firstLevelGroupName) {
		this.firstLevelGroupName = firstLevelGroupName;
	}

	public String getSecondLevelGroup() {
		return secondLevelGroup;
	}

	public void setSecondLevelGroup(String secondLevelGroup) {
		this.secondLevelGroup = secondLevelGroup;
	}

	public String getSecondLevelGroupName() {
		return secondLevelGroupName;
	}

	public void setSecondLevelGroupName(String secondLevelGroupName) {
		this.secondLevelGroupName = secondLevelGroupName;
	}

	public String getThirdLevelGroup() {
		return thirdLevelGroup;
	}

	public void setThirdLevelGroup(String thirdLevelGroup) {
		this.thirdLevelGroup = thirdLevelGroup;
	}

	public String getThirdLevelGroupName() {
		return thirdLevelGroupName;
	}

	public void setThirdLevelGroupName(String thirdLevelGroupName) {
		this.thirdLevelGroupName = thirdLevelGroupName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateDateStr() {
		return createDateStr;
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
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

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

}
