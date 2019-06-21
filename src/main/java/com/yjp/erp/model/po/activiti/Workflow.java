package com.yjp.erp.model.po.activiti;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 工作流引擎表
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-03
 */

public class Workflow implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 工作流id
	 */
	private Long id;
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
	 * 创建者
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

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

}
