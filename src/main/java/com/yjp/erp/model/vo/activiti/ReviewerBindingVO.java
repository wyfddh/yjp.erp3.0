package com.yjp.erp.model.vo.activiti;

import com.yjp.erp.model.vo.PrimaryKeyVO;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 审核人绑定表返回实体
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-04
 */
public class ReviewerBindingVO extends PrimaryKeyVO implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 组织或区域id
	 */
	private Long orgId;
	/**
	 * 组织或区域名称
	 */
	private String orgName;
	/**
	 * 一级审核组(多个人逗号分隔)
	 */
	private String firstLevelGroup;
	/**
	 * 一级审核组名称(用户名,多个逗号分隔)
	 */
	private String firstLevelGroupName;
	/**
	 * 二级审核组(多个人逗号分隔)
	 */
	private String secondLevelGroup;
	/**
	 * 二级审核组名称(用户名,多个逗号分隔)
	 */
	private String secondLevelGroupName;
	/**
	 * 二级审核组(多个人逗号分隔)
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

}
