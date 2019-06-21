package com.yjp.erp.model.vo.activiti;

import java.io.Serializable;

/**
 * 
 * @ClassName: WorkflowInstanceVO
 * @Description: 创建工作流实例返回实体
 * @author: jihongjin@yijiupi.com
 * @date: 2019年4月19日 上午9:44:07
 * 
 * @Copyright: 2019 www.yijiupi.com Inc. All rights reserved.
 */
public class WorkflowInstanceVO implements Serializable {
	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 流程id
	 */
	private String processId;
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

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

}
