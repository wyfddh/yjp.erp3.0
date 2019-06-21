package com.yjp.erp.model.vo;

import java.io.Serializable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
/**
 * 
 * @ClassName: IdVO   
 * @Description: 主键-返回
 * @author: jihongjin@yijiupi.com
 * @date: 2019年4月19日 下午3:53:34   
 *  
 * @Copyright: 2019 www.yijiupi.com Inc. All rights reserved.
 */
public class PrimaryKeyVO implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	// 将Long类型系列化成String避免精度丢失
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
