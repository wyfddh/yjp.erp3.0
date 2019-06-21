package com.yjp.erp.model.vo.activiti;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @ClassName: ProcessHistoryVO
 * @Description: 历史记录返回实体
 * @author: jihongjin@yijiupi.com
 * @date: 2019年4月12日 上午10:59:11
 * 
 * @Copyright: 2019 www.yijiupi.com Inc. All rights reserved.
 */

public class ProcessHistoryVO implements Serializable {
	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 业务数据
	 */
	private BusinessDataVO businessData;
	/**
	 * 历史明细
	 */
	private List<TaskDetailVO> taskDetails;

	public List<TaskDetailVO> getTaskDetails() {
		return taskDetails;
	}

	public void setTaskDetails(List<TaskDetailVO> taskDetails) {
		this.taskDetails = taskDetails;
	}

	public BusinessDataVO getBusinessData() {
		return businessData;
	}

	public void setBusinessData(BusinessDataVO businessData) {
		this.businessData = businessData;
	}

}
