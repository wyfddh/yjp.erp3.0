package com.yjp.erp.model.dto.activiti;

import java.io.Serializable;


/**
 * 
 * @ClassName: ProcessHistoryDTO
 * @Description: 历史明细入参
 * @author: jihongjin@yijiupi.com
 * @date: 2019年4月12日 下午1:13:32
 * 
 * @Copyright: 2019 www.yijiupi.com Inc. All rights reserved.
 */

public class ProcessHistoryDTO implements Serializable {
	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 流程id
	 */
	private String processId;
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 任务id
	 */
	private String taskId;

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

}
