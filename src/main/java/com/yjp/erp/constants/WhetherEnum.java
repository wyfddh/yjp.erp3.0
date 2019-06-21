package com.yjp.erp.constants;

/**
 * 
 * @ClassName: WhetherEnum
 * @Description: 是否枚举类
 * @author: jihongjin@yijiupi.com
 * @date: 2019年4月17日 下午7:25:23
 * 
 * @Copyright: 2019 www.yijiupi.com Inc. All rights reserved.
 */
public enum WhetherEnum {
	/**
	 * 是
	 */
	YES(1, "是"), 
	NO(0, "否");

	private Integer key;

	private String value;

	private WhetherEnum(Integer key, String value) {
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
		for (WhetherEnum whetherEnum : WhetherEnum.values()) {
			if (key.equals(whetherEnum.getKey())) {
				return whetherEnum.getValue();
			}
		}
		return "";
	}
}
