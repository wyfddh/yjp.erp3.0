package com.yjp.erp.model.dto.activiti;

import com.yjp.erp.model.dto.PageDTO;
import com.yjp.erp.model.po.activiti.BillBinding;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * <p>
 * 审核配置入参
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-03
 */

public class BillBindingDTO extends PageDTO implements Serializable {
	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 实体名称id
	 */
	private String typeId;
	/**
	 * 实体名称
	 */
	private String typeName;
	/**
	 * 实体类型(bill、entity、view)
	 */
	private String classId;
	/**
	 * 实体类型名称(单据、业务实体、视图)
	 */
	private String className;
	/**
	 * 模版id或工作流id
	 */
	private Long workflowId;
	/**
	 * 模版名称或工作流名称
	 */
	private String workflowName;

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

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
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

	public BillBinding toPo(){
		BillBinding billBinding = new BillBinding();
		try {
			BeanUtils.copyProperties(this, billBinding);
		} catch (Exception e) {
			throw new RuntimeException("单据绑定入参与实体对象拷贝异常：" + e.getMessage());
		}
		return billBinding;
	}
}
