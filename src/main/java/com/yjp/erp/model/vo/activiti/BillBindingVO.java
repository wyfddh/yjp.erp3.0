package com.yjp.erp.model.vo.activiti;

import java.io.Serializable;
import java.util.Date;

import com.yjp.erp.model.vo.PrimaryKeyVO;

/**
 * <p>
 * 单据绑定表返回实体
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-04
 */

public class BillBindingVO extends PrimaryKeyVO implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 名称显示
	 */
	private String label;
	/**
	 * 实体类型(如单据、业务实体)
	 */
	private String classId;
	/**
	 * 实体名称(如入库申请单、库存)
	 */
	private String typeId;
	/**
	 * 实体名称(如入库申请单、库存)
	 */
	private String typeName;
	/**
	 * 工作流
	 */
	private Long workflowId;
	/**
	 * 工作流名称
	 */
	private String workflowName;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 创建时间（yyyy-MM-dd HH:mm:ss）
	 */
	private String createDateStr;
	/**
	 * 创建人
	 */
	private Long creator;
	/**
	 * 创建者姓名
	 */
	private String creatorName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
