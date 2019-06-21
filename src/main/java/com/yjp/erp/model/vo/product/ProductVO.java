package com.yjp.erp.model.vo.product;

import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 产品表-返回实体
 * </p>
 *
 * @author auto
 * @since 2019-04-17
 */
public class ProductVO implements Serializable {

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
	 * 类目名称
	 */
	private String categoryName;
	/**
	 * 产品类型id
	 */
	private String productTypeId;
	/**
	 * 产品类型名称
	 */
	private String productTypeName;
	/**
	 * 品牌
	 */
	private String brandName;
	/**
	 * 销售区域
	 */
	private String areaId;
	/**
	 * 销售区域名称
	 */
	private String areaName;
	/**
	 * 状态（0：下架，1：上架）
	 */
	private String status;
	/**
	 * 状态名称
	 */
	private String statusName;
	/**
	 * 是否区域代理产品（0：否，1：是）
	 */
	private String areaProxy;
	/**
	 * 是否区域代理产品（0：否，1：是）
	 */
	private String areaProxyName;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 创建时间（yyyy-MM-dd HH:mm:ss）
	 */
	private String createDateStr;
	/**
	 * 创建人
	 */
	private Long creator;
	/**
	 * 创建人姓名
	 */
	private String creatorName;
	/**
	 * 更新时间
	 */
	private Date updateDate;
	/**
	 * 更新时间（yyyy-MM-dd HH:mm:ss）
	 */
	private String updateDateStr;
	/**
	 * 更新人
	 */
	private Long updator;
	/**
	 * 更新人姓名
	 */
	private String updatorName;

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

	public String getAreaProxy() {
		return areaProxy;
	}

	public void setAreaProxy(String areaProxy) {
		this.areaProxy = areaProxy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Long getUpdator() {
		return updator;
	}

	public void setUpdator(Long updator) {
		this.updator = updator;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getProductTypeName() {
		return productTypeName;
	}

	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getAreaProxyName() {
		return areaProxyName;
	}

	public void setAreaProxyName(String areaProxyName) {
		this.areaProxyName = areaProxyName;
	}

	public String getCreateDateStr() {
		return createDateStr;
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getUpdateDateStr() {
		return updateDateStr;
	}

	public void setUpdateDateStr(String updateDateStr) {
		this.updateDateStr = updateDateStr;
	}

	public String getUpdatorName() {
		return updatorName;
	}

	public void setUpdatorName(String updatorName) {
		this.updatorName = updatorName;
	}

}
