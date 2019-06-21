package com.yjp.erp.model.dto.activiti;

import java.io.Serializable;


/**
 * 
 * @ClassName: TransferTaskDTO
 * @Description: 转交审核--前台入参
 * @author: jihongjin@yijiupi.com
 * @date: 2019年4月11日 下午3:32:32
 * 
 * @Copyright: 2019 www.yijiupi.com Inc. All rights reserved.
 */

public class TransferTaskDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 任务id
	 */
	private String taskId;
	/**
	 * 接收人
	 */
	private String transferUserId;
	/**
	 * 转交备注
	 */
	private String remarks;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTransferUserId() {
		return transferUserId;
	}

	public void setTransferUserId(String transferUserId) {
		this.transferUserId = transferUserId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
