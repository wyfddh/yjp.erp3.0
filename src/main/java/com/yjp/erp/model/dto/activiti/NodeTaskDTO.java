package com.yjp.erp.model.dto.activiti;

import java.io.Serializable;


/**
 * 
 * @ClassName: NodeTaskDTO   
 * @Description: 审核节点任务入参
 * @author: jihongjin@yijiupi.com
 * @date: 2019年4月9日 下午8:13:32   
 *  
 * @Copyright: 2019 www.yijiupi.com Inc. All rights reserved.
 */

public class NodeTaskDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 节点任务标识
	 */
	private String taskId;
	/**
	 * 审核决定：通过(true)或不通过(false)
	 */
	private boolean pass;
	/**
	 * 审核备注
	 */
	private String remarks;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public boolean isPass() {
		return pass;
	}
	public void setPass(boolean pass) {
		this.pass = pass;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
