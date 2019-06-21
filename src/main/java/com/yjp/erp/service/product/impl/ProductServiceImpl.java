package com.yjp.erp.service.product.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.yjp.erp.conf.UserInfoManager;
import com.yjp.erp.constants.ProductStatusEnum;
import com.yjp.erp.constants.WhetherEnum;
import com.yjp.erp.model.dto.product.ProductDTO;
import com.yjp.erp.model.po.product.Product;
import com.yjp.erp.model.po.system.User;
import com.yjp.erp.mapper.product.ProductMapper;
import com.yjp.erp.service.product.IProductService;
import com.yjp.erp.service.system.UserService;
import com.yjp.erp.util.FormatReturnEntity;
import com.yjp.erp.util.SnowflakeIdWorker;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetCode;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.model.vo.product.ProductVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Maps;

import org.springframework.beans.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品表 服务实现类
 * </p>
 *
 * @author auto
 * @since 2019-04-17
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {
	
	private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Autowired
	UserService userService;

	@Autowired
	private UserInfoManager userInfoManager;

	@Override
	public JsonResult<Boolean> insertProduct(ProductDTO productDTO) {
		JsonResult<Boolean> jsonResult = new JsonResult<>();
		jsonResult.setCode(RetCode.FAIL);
		jsonResult.setData(false);
		if (StringUtils.isBlank(productDTO.getName())) {
			jsonResult.setMsg("产品名称不能为空！");
			return jsonResult;
		}
		if (StringUtils.isBlank(productDTO.getCategoryId())) {
			jsonResult.setMsg("类目不能为空！");
			return jsonResult;
		}
		if (StringUtils.isBlank(productDTO.getProductTypeId())) {
			jsonResult.setMsg("产品类型不能为空！");
			return jsonResult;
		}
		if (StringUtils.isBlank(productDTO.getBrandName())) {
			jsonResult.setMsg("品牌不能为空！");
			return jsonResult;
		}
		if (StringUtils.isBlank(productDTO.getAreaId())) {
			jsonResult.setMsg("销售区域不能为空！");
			return jsonResult;
		}
		Product product = new Product();
		try {
			BeanUtils.copyProperties(product, productDTO);
		} catch (Exception e) {
			log.error("产品对象入参与实体拷贝异常：" + e.getMessage());
		}
		product.setCreator(Long.valueOf(userInfoManager.getUserInfo().getUserId()));
		product.setCreateDate(new Date());
		product.setId(SnowflakeIdWorker.nextId());
		boolean flag = this.insert(product);
		if (flag) {
			jsonResult.setCode(RetCode.SUCCESS);
			jsonResult.setMsg("新增保存产品成功！");
		} else {
			jsonResult.setMsg("新增保存产品失败！");
		}
		return jsonResult.setData(flag);
	}

	@Override
	public JsonResult<Boolean> updateProduct(ProductDTO productDTO) {
		JsonResult<Boolean> jsonResult = new JsonResult<>();
		jsonResult.setCode(RetCode.FAIL);
		jsonResult.setData(false);
		if (StringUtils.isBlank(productDTO.getProductTypeId())) {
			jsonResult.setMsg("产品id不能为空！");
			return jsonResult;
		}
		if (StringUtils.isBlank(productDTO.getName())) {
			jsonResult.setMsg("产品名称不能为空！");
			return jsonResult;
		}
		if (StringUtils.isBlank(productDTO.getCategoryId())) {
			jsonResult.setMsg("类目不能为空！");
			return jsonResult;
		}
		if (StringUtils.isBlank(productDTO.getProductTypeId())) {
			jsonResult.setMsg("产品类型不能为空！");
			return jsonResult;
		}
		if (StringUtils.isBlank(productDTO.getBrandName())) {
			jsonResult.setMsg("品牌不能为空！");
			return jsonResult;
		}
		if (StringUtils.isBlank(productDTO.getAreaId())) {
			jsonResult.setMsg("销售区域不能为空！");
			return jsonResult;
		}
		Product product = new Product();
		try {
			BeanUtils.copyProperties(product, productDTO);
		} catch (Exception e) {
			log.error("产品对象入参与实体拷贝异常：" + e.getMessage());
		}
		product.setUpdator(Long.valueOf(userInfoManager.getUserInfo().getUserId()));
		product.setUpdateDate(new Date());
		boolean flag = this.updateById(product);
		if (flag) {
			jsonResult.setCode(RetCode.SUCCESS);
			jsonResult.setMsg("编辑保存产品成功！");
		} else {
			jsonResult.setMsg("编辑保存产品失败！");
		}
		return jsonResult.setData(flag);
	}

	@Override
	public JsonResult<Boolean> deleteProduct(Long id) {
		JsonResult<Boolean> jsonResult = new JsonResult<>();
		jsonResult.setCode(RetCode.FAIL);
		jsonResult.setData(false);
		if (Objects.isNull(id)) {
			jsonResult.setMsg("产品id不能为空！");
			return jsonResult;
		}
		boolean flag = this.deleteById(id);
		if (flag) {
			jsonResult.setCode(RetCode.SUCCESS);
			jsonResult.setMsg("删除产品成功！");
		} else {
			jsonResult.setMsg("删除产品失败！");
		}
		return jsonResult.setData(flag);
	}

	@Override
	public JsonResult<Page<ProductVO>> listProductPage(ProductDTO productDTO) {
		Page<Product> page = new Page<>();
		page.setCurrent(productDTO.getPageNo());
		page.setSize(productDTO.getPageSize());
		EntityWrapper<Product> ew = new EntityWrapper<>();
		String productName = productDTO.getName();
		String areaId = productDTO.getAreaId();
		String productTypeId = productDTO.getProductTypeId();
		String status = productDTO.getStatus();
		if(StringUtils.isNotBlank(productName)){
			ew.like("name", productName);
		}
		if(StringUtils.isNotBlank(areaId)){
			ew.eq("area_id", areaId);
		}
		if(StringUtils.isNotBlank(status)){
			ew.eq("status", status);
		}
		if(StringUtils.isNotBlank(productTypeId)){
			ew.eq("product_type_id", productTypeId);
		}
		// 按时间倒序
		ew.orderDesc(Arrays.asList("update_date", "create_date"));
		Page<Product> pageList = this.selectPage(page, ew);
		List<Product> records = pageList.getRecords();
		List<Long> userIds = records.stream().map(Product::getCreator).collect(Collectors.toList());
		List<Long> updators = records.stream().map(Product::getUpdator).collect(Collectors.toList());
		userIds.addAll(updators);
		userIds = userIds.stream().distinct().collect(Collectors.toList());
		Map<Long, User> userMap = Maps.newConcurrentMap();
		try {
			userMap = userService.getUserMapByIds(userIds);
		} catch (Exception e) {
			log.error("根据用户id集合批量获取用户信息异常：" + e.getMessage());
		}
		Page<ProductVO> formatProductPageList = FormatReturnEntity.formatProductPageList(pageList, userMap);
		return RetResponse.makeOKRsp(formatProductPageList);
	}

	@Override
	public JsonResult<ProductVO> viewProduct(Long id) {
		JsonResult<ProductVO> jsonResult = new JsonResult<>();
		jsonResult.setCode(RetCode.FAIL);
		jsonResult.setData(null);
		if (Objects.isNull(id)) {
			return jsonResult.setMsg("产品id不能为空！");
		}
		Product product = this.selectById(id);
		Long creator = product.getCreator();
		Long updator = product.getUpdator();
		Map<Long, User> userMap = Maps.newConcurrentMap();
		try {
			userMap = userService.getUserMapByIds(Arrays.asList(creator, updator));
		} catch (Exception e) {
			log.error("根据用户id集合批量获取用户信息异常：" + e.getMessage());
		}
		ProductVO formatProduct = FormatReturnEntity.formatProduct(product, userMap);
		return RetResponse.makeOKRsp(formatProduct);
	}

	@Override
	public JsonResult<Boolean> doAndCancelAreaProxy(Long id, Integer areaProxy) {
		JsonResult<Boolean> jsonResult = new JsonResult<>();
		jsonResult.setCode(RetCode.FAIL);
		jsonResult.setData(false);
		if (Objects.isNull(id)) {
			jsonResult.setMsg("产品id不能为空！");
			return jsonResult;
		}
		Product product = new Product();
		product.setId(id);
		product.setAreaProxy(String.valueOf(areaProxy));
		boolean flag = this.updateById(product);
		if (flag) {
			jsonResult.setCode(RetCode.SUCCESS);
			jsonResult.setMsg(WhetherEnum.NO.getKey().equals(areaProxy)?"取消区域代理成功！":"设置区域代理成功！");
		} else {
			jsonResult.setMsg(WhetherEnum.NO.getKey().equals(areaProxy)?"取消区域代理失败！":"设置区域代理失败！");
		}
		return jsonResult.setData(flag);
	}

	@Override
	public JsonResult<Boolean> doUpAndDownProduct(Long id, Integer status) {
		JsonResult<Boolean> jsonResult = new JsonResult<>();
		jsonResult.setCode(RetCode.FAIL);
		jsonResult.setData(false);
		if (Objects.isNull(id)) {
			jsonResult.setMsg("产品id不能为空！");
			return jsonResult;
		}
		Product product = new Product();
		product.setId(id);
		product.setStatus(String.valueOf(status));
		boolean flag = this.updateById(product);
		if (flag) {
			jsonResult.setCode(RetCode.SUCCESS);
			jsonResult.setMsg(ProductStatusEnum.DOWN.getKey().equals(status)?"产品下架成功！":"产品上架成功！");
		} else {
			jsonResult.setMsg(ProductStatusEnum.DOWN.getKey().equals(status)?"产品下架失败！":"产品上架失败！");
		}
		return jsonResult.setData(flag);
	}
	

}
