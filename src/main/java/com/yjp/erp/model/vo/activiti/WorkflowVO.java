package com.yjp.erp.model.vo.activiti;

import java.io.Serializable;
import java.util.Date;

import com.yjp.erp.model.vo.PrimaryKeyVO;


/**
 * <p>
 * 工作流引擎表返回实体
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-03
 */
public class WorkflowVO extends PrimaryKeyVO implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 工作流名称
	 */
	private String name;
	/**
	 * 标识符(workflow_1,workflow_2...)
	 */
	private String identifier;
	/**
	 * 描述
	 */
	private String description;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

}
