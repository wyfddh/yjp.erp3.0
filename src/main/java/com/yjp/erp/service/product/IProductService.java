package com.yjp.erp.service.product;

import com.yjp.erp.model.dto.product.ProductDTO;
import com.yjp.erp.model.po.product.Product;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.product.ProductVO;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 产品表 服务类
 * </p>
 *
 * @author auto
 * @since 2019-04-17
 */
public interface IProductService extends IService<Product> {

	/**
	 * 
	 * @Title: insertProduct
	 * @Description: 新增保存产品
	 * @param: productDTO 入参
	 * @return: JsonResult<Boolean> true：成功 false：失败
	 * @throws
	 */
	JsonResult<Boolean> insertProduct(ProductDTO productDTO);

	/**
	 * 
	 * @Title: updateProduct
	 * @Description: 编辑保存产品
	 * @param: productDTO 入参
	 * @return: JsonResult<Boolean> true：成功 false：失败
	 * @throws
	 */
	JsonResult<Boolean> updateProduct(ProductDTO productDTO);

	/**
	 * 
	 * @Title: deleteProduct
	 * @Description: 删除产品
	 * @param: id 主键
	 * @return: JsonResult<Boolean> true：成功 false：失败
	 * @throws
	 */
	JsonResult<Boolean> deleteProduct(Long id);

	/**
	 * 
	 * @Title: listProductPage
	 * @Description: 分页查询产品
	 * @param: productDTO 入参
	 * @return: JsonResult<Page<ProductVO>>
	 * @throws
	 */
	JsonResult<Page<ProductVO>> listProductPage(ProductDTO productDTO);

	/**
	 * 
	 * @Title: viewProduct
	 * @Description: 查看产品
	 * @param: id 主键
	 * @return: JsonResult<ProductVO>
	 * @throws
	 */
	JsonResult<ProductVO> viewProduct(Long id);

	/**
	 * 
	 * @Title: doAreaProxy
	 * @Description: 设置或取消区域代理
	 * @param: id 主键
	 * @param: areaProxy 是否区域代理产品（0：否，1：是）
	 * @return: JsonResult<Boolean> true：成功 false：失败
	 * @throws
	 */
	JsonResult<Boolean> doAndCancelAreaProxy(Long id, Integer areaProxy);

	/**
	 * 
	 * @Title: doUpProduct
	 * @Description: 上架或下架产品
	 * @param: id 主键
	 * @param: status 状态（0：下架，1：上架）
	 * @return: JsonResult<Boolean> true：成功 false：失败
	 * @throws
	 */
	JsonResult<Boolean> doUpAndDownProduct(Long id, Integer status);

}
