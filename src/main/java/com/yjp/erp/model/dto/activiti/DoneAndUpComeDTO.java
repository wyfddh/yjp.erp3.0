package com.yjp.erp.model.dto.activiti;

import com.yjp.erp.model.dto.PageDTO;

import java.io.Serializable;


/**
 * 
 * @ClassName: DoneAndUpComeDTO
 * @Description: 待办和已办入参
 * @author: jihongjin@yijiupi.com
 * @date: 2019年4月13日 上午11:21:07
 * 
 * @Copyright: 2019 www.yijiupi.com Inc. All rights reserved.
 */

public class DoneAndUpComeDTO extends PageDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 待办和已办类型[candidate-待领取，assignee-待处理，processed-已完成，unprocessed-未完成]
	 */
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
