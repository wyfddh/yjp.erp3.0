package com.yjp.erp.constants;
/**
 * 
 * @ClassName: WorkflowStatusEnum   
 * @Description: 工作流状态枚举
 * @author: jihongjin@yijiupi.com
 * @date: 2019年4月11日 下午7:44:08   
 *  
 * @Copyright: 2019 www.yijiupi.com Inc. All rights reserved.
 */
public enum WorkflowStatusEnum {

	/**
	 * 待领取
	 */
	PENDING("candidate", "待领取"), 
	PENDING_REVIEW("assignee", "待处理"), 
	AUDITED("assigneeProcessed", "已处理"), 
	TRANSFER_REVIEWED("transferAndProcessed", "已转交"), 
	TRANSFER_NOT_REVIEWED("transferAndUnprocessed", "转交未审核"),
	COMPLETED("processed","已完成"),
	UNDONE("unprocessed","未完成");
	private String key;

	private String value;

	private WorkflowStatusEnum(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 
	 * @Title: getValueByKey
	 * @Description: 根据key获取value
	 * @param: key
	 * @return: String
	 * @throws
	 */
	public static String getValueByKey(String key) {
		for (WorkflowStatusEnum workflowStatusEnum : WorkflowStatusEnum
				.values()) {
			if (workflowStatusEnum.getKey().equals(key)) {
				return workflowStatusEnum.getValue();
			}
		}
		return "";
	}
}
