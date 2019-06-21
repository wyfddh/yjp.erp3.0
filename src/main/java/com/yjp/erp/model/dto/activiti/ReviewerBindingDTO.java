package com.yjp.erp.model.dto.activiti;

import com.yjp.erp.model.dto.PageDTO;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;


/**
 * <p>
 * 审核人绑定入参
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-03
 */

public class ReviewerBindingDTO extends PageDTO implements Serializable {
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
	 * 组织或区域id
	 */
	@NotBlank
	private Long orgId;
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

}
