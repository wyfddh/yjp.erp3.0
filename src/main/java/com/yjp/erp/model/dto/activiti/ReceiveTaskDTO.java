package com.yjp.erp.model.dto.activiti;

import java.io.Serializable;


/**
 * 
 * @ClassName: ReceiveTaskDTO
 * @Description: 认领当前待审核Task--入参实体
 * @author: jihongjin@yijiupi.com
 * @date: 2019年4月11日 下午3:04:27
 * 
 * @Copyright: 2019 www.yijiupi.com Inc. All rights reserved.
 */

public class ReceiveTaskDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 任务id
	 */
	private String taskId;
	/**
	 * 用户id
	 */
	private String userId;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
