package com.yjp.erp.model.vo.activiti;

import java.io.Serializable;

/**
 * 
 * @ClassName: ProcessTaskVO
 * @Description: 待办返回实体
 * @author: jihongjin@yijiupi.com
 * @date: 2019年4月11日 下午4:54:36
 * 
 * @Copyright: 2019 www.yijiupi.com Inc. All rights reserved.
 */

public class ProcessTaskVO implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 任务id
	 */
	private String taskId;
	/**
	 * 流程id
	 */
	private String processId;
	/**
	 * 提交人
	 */
	private String processCreator;
	/**
	 * 提交时间（yyyy-MM-dd HH:mm:ss）
	 */
	private String processCreateTime;
	/**
	 * 类型
	 */
	private String statusName;
	/**
	 * 业务数据
	 */
	private BusinessDataVO businessData;
	/**
	 * 按钮权限组
	 */
	private ButtonPermissionVO buttonPermission;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getProcessCreator() {
		return processCreator;
	}

	public void setProcessCreator(String processCreator) {
		this.processCreator = processCreator;
	}

	public String getProcessCreateTime() {
		return processCreateTime;
	}

	public void setProcessCreateTime(String processCreateTime) {
		this.processCreateTime = processCreateTime;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public ButtonPermissionVO getButtonPermission() {
		return buttonPermission;
	}

	public void setButtonPermission(ButtonPermissionVO buttonPermission) {
		this.buttonPermission = buttonPermission;
	}

	public BusinessDataVO getBusinessData() {
		return businessData;
	}

	public void setBusinessData(BusinessDataVO businessData) {
		this.businessData = businessData;
	}

}
