package com.yjp.erp.model.vo.activiti;

import java.io.Serializable;

/**
 * 
 * @ClassName: ButtonPermissionVO
 * @Description: 按钮权限
 * @author: jihongjin@yijiupi.com
 * @date: 2019年4月18日 上午10:49:27
 * 
 * @Copyright: 2019 www.yijiupi.com Inc. All rights reserved.
 */
public class ButtonPermissionVO implements Serializable {
	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 是否可提交
	 */
	private boolean configSubmit;
	/**
	 * 是否可拒绝
	 */
	private boolean configRefuse;
	/**
	 * 是否可通过
	 */
	private boolean configApply;
	/**
	 * 是否可编辑
	 */
	private boolean configEdit;
	/**
	 * 是否可领取
	 */
	private boolean configReceive;

	public boolean isConfigSubmit() {
		return configSubmit;
	}

	public void setConfigSubmit(boolean configSubmit) {
		this.configSubmit = configSubmit;
	}

	public boolean isConfigRefuse() {
		return configRefuse;
	}

	public void setConfigRefuse(boolean configRefuse) {
		this.configRefuse = configRefuse;
	}

	public boolean isConfigApply() {
		return configApply;
	}

	public void setConfigApply(boolean configApply) {
		this.configApply = configApply;
	}

	public boolean isConfigEdit() {
		return configEdit;
	}

	public void setConfigEdit(boolean configEdit) {
		this.configEdit = configEdit;
	}

	public boolean isConfigReceive() {
		return configReceive;
	}

	public void setConfigReceive(boolean configReceive) {
		this.configReceive = configReceive;
	}

}
