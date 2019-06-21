package com.yjp.erp.model.vo;

import com.yjp.erp.model.po.system.Organization;
import java.io.Serializable;
import java.util.Map;

import lombok.Data;

import com.yjp.erp.model.po.activiti.Workflow;
import com.yjp.erp.model.po.system.User;

@Data
public class ReturnDataVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 工作流
	 */
	private Map<Long, Workflow> workflowMap;
	/**
	 * 组织或区域
	 */
	private Map<Long, Organization> orgMap;
	/**
	 * 用户
	 */
	private Map<Long, User> userMap;
}
