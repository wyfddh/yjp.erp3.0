package com.yjp.erp.model.vo.activiti;

import java.io.Serializable;


/**
 * 
 * @ClassName: TaskDetailVO
 * @Description: 历史记录返回实体
 * @author: jihongjin@yijiupi.com
 * @date: 2019年4月12日 上午11:48:38
 * 
 * @Copyright: 2019 www.yijiupi.com Inc. All rights reserved.
 */

public class TaskDetailVO implements Serializable {
	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 提交人
	 */
	private String submitter;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 处理时间（yyyy-MM-dd HH:mm:ss）
	 */
	private String processTime;

	public String getSubmitter() {
		return submitter;
	}

	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProcessTime() {
		return processTime;
	}

	public void setProcessTime(String processTime) {
		this.processTime = processTime;
	}

}
