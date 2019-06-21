package com.yjp.erp.model.po.activiti;

import com.baomidou.mybatisplus.annotations.TableField;

import java.io.Serializable;
import java.util.Date;


/**
 * <p>
 * 审核人绑定表
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-04
 */
public class ReviewerBinding implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 审核人绑定id
	 */
	private Long id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 组织或区域id
	 */
	@TableField(value = "area_id")
	private Long orgId;
	/**
	 * 一级审核组(多个人逗号分隔)
	 */
	private String firstLevelGroup;
	/**
	 * 二级审核组(多个人逗号分隔)
	 */
	private String secondLevelGroup;
	/**
	 * 二级审核组(多个人逗号分隔)
	 */
	private String thirdLevelGroup;
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

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
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

}
