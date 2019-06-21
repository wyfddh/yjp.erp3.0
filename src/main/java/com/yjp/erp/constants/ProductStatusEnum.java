package com.yjp.erp.constants;

/**
 * 
 * @ClassName: ProductStatusEnum
 * @Description: 商品状态枚举类
 * @author: jihongjin@yijiupi.com
 * @date: 2019年4月4日 下午3:22:32
 * 
 * @Copyright: 2019 www.yijiupi.com Inc. All rights reserved.
 */
public enum ProductStatusEnum {

	/**
	 * 下架
	 */
	DOWN(0, "下架"),
	/**
	 * 上架
	 */
	UP(1, "上架");

	private Integer key;

	private String value;

	private ProductStatusEnum(Integer key, String value) {
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
		for (ProductStatusEnum statusEnum : ProductStatusEnum.values()) {
			if (key.equals(statusEnum.getKey())) {
				return statusEnum.getValue();
			}
		}
		return "";
	}

}
