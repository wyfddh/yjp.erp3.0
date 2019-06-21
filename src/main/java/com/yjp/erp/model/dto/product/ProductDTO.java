package com.yjp.erp.model.dto.product;

import java.io.Serializable;

import com.yjp.erp.model.dto.PageDTO;
/**
 * 
 * @ClassName: ProductDTO   
 * @Description: 产品实体-入参
 * @author: jihongjin@yijiupi.com
 * @date: 2019年4月17日 下午3:19:46   
 *  
 * @Copyright: 2019 www.yijiupi.com Inc. All rights reserved.
 */
public class ProductDTO extends PageDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 产品编码
	 */
	private Long id;
	/**
	 * 产品名称
	 */
	private String name;
	/**
	 * 类目id
	 */
	private String categoryId;
	/**
	 * 产品类型id
	 */
	private String productTypeId;
	/**
	 * 品牌
	 */
	private String brandName;
	/**
	 * 销售区域
	 */
	private String areaId;
	/**
	 * 产品状态（0：下架，1：上架）
	 */
	private String status;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(String productTypeId) {
		this.productTypeId = productTypeId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
