package com.yjp.erp.constants;
/**
 * 
 * @ClassName: StatusEnum   
 * @Description: 状态枚举类
 * @author: jihongjin@yijiupi.com
 * @date: 2019年4月4日 下午3:22:32   
 *  
 * @Copyright: 2019 www.yijiupi.com Inc. All rights reserved.
 */
public enum StatusEnum {

	/**
	 * 删除
	 */
	DELETE(-1, "已删除"),
	/**
	 * 禁用/冻结/停用/暂存
	 */
	TEMPORARY_STORAGE(0, "暂存"),
	/**
	 * 正常/启用/发布
	 */
	RELEASE(1, "发布");

	private Integer key;

	private String value;

	private StatusEnum(Integer key, String value) {
		this.key = key;
		this.value = value;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
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
	public static String getValueByKey(Integer key) {
		for (StatusEnum statusEnum : StatusEnum.values()) {
			if (key.equals(statusEnum.getKey())) {
				return statusEnum.getValue();
			}
		}
		return "";
	}

}
