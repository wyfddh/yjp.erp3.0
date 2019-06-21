package com.yjp.erp.model.dto.activiti;

import java.io.Serializable;

import com.yjp.erp.model.dto.PageDTO;
import com.yjp.erp.model.po.activiti.AuditConfig;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 * 审核配置入参
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-03
 */

public class AuditConfigDTO extends PageDTO implements Serializable {
	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 实体名称(如出库单、入库单)
	 */
	private String typeId;
	/**
	 * 实体类型(如单据bill、业务实体entity、视图view)
	 */
	private String classId;
	/**
	 * 组织id
	 */
	private Long orgId;
	/**
	 * 工作流id
	 */
	private Long workflowId;
	/**
	 * 一级审核组(用户id)
	 */
	private String[] firstLevelGroup;
	/**
	 * 二级审核组(用户id)
	 */
	private String[] secondLevelGroup;
	/**
	 * 三级审核组(用户id)
	 */
	private String[] thirdLevelGroup;
	/**
	 * 描述
	 */
	private String description;

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

	public String[] getFirstLevelGroup() {
		return firstLevelGroup;
	}

	public void setFirstLevelGroup(String[] firstLevelGroup) {
		this.firstLevelGroup = firstLevelGroup;
	}

	public String[] getSecondLevelGroup() {
		return secondLevelGroup;
	}

	public void setSecondLevelGroup(String[] secondLevelGroup) {
		this.secondLevelGroup = secondLevelGroup;
	}

	public String[] getThirdLevelGroup() {
		return thirdLevelGroup;
	}

	public void setThirdLevelGroup(String[] thirdLevelGroup) {
		this.thirdLevelGroup = thirdLevelGroup;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AuditConfig toPo(){
		AuditConfig auditConfig = new AuditConfig();
		try {
			BeanUtils.copyProperties(auditConfig, this);
		} catch (Exception e) {
			throw new RuntimeException("审核配置入参与实体拷贝异常：" + e.getMessage());
		}
		return auditConfig;
	}
}
