//package com.yjp.erp.controller.product;
//
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.baomidou.mybatisplus.plugins.Page;
//import com.yjp.erp.constants.ProductStatusEnum;
//import com.yjp.erp.constants.WhetherEnum;
//import com.yjp.erp.model.dto.product.ProductDTO;
//import com.yjp.erp.service.product.IProductService;
//import com.yjp.erp.model.vo.JsonResult;
//import com.yjp.erp.model.vo.product.ProductVO;
//
///**
// * <p>
// * 产品表 前端控制器
// * </p>
// *
// * @author auto
// * @since 2019-04-17
// */
//@Deprecated
//@RestController
//@RequestMapping("/apps/product")
//public class ProductController {
//
//	@Autowired
//	IProductService productServiceImpl;
//
//	/**
//	 *
//	 * @Title: saveProduct
//	 * @Description: 新增保存产品
//	 * @param: productDTO
//	 * @return: JsonResult<Boolean>
//	 * @throws
//	 */
//	@PostMapping("/save")
//	public JsonResult<Boolean> saveProduct(@RequestBody ProductDTO productDTO) throws Exception {
//		return productServiceImpl.insertProduct(productDTO);
//	}
//
//	/**
//	 *
//	 * @Title: updateProduct
//	 * @Description: 编辑保存产品
//	 * @param: productDTO
//	 * @return: JsonResult<Boolean>
//	 * @throws
//	 */
//	@PostMapping("/update")
//	public JsonResult<Boolean> updateProduct(@RequestBody ProductDTO productDTO) throws Exception {
//		return productServiceImpl.updateProduct(productDTO);
//	}
//
//	/**
//	 *
//	 * @Title: deleteProduct
//	 * @Description: 删除产品
//	 * @param: productDTO
//	 * @return: JsonResult<Boolean>
//	 * @throws
//	 */
//	@PostMapping("/delete")
//	public JsonResult<Boolean> deleteProduct(@RequestBody ProductDTO productDTO) throws Exception {
//		return productServiceImpl.deleteProduct(productDTO.getId());
//	}
//
//	/**
//	 *
//	 * @Title: listProduct
//	 * @Description: 分页查询产品
//	 * @param: productDTO
//	 * @return: JsonResult<Boolean>
//	 * @throws
//	 */
//	@PostMapping("/list")
//	public JsonResult<Page<ProductVO>> listProduct(@RequestBody ProductDTO productDTO) throws Exception {
//		return productServiceImpl.listProductPage(productDTO);
//	}
//
//	/**
//	 *
//	 * @Title: listProduct
//	 * @Description: 分页查询产品
//	 * @param: productDTO
//	 * @return: JsonResult<Boolean>
//	 * @throws
//	 */
//	@PostMapping("/view")
//	public JsonResult<ProductVO> viewProduct(@RequestBody ProductDTO productDTO) throws Exception {
//		return productServiceImpl.viewProduct(productDTO.getId());
//	}
//
//	/**
//	 *
//	 * @Title: doAreaProxy
//	 * @Description: 设置区域代理
//	 * @param: productDTO
//	 * @return: JsonResult<Boolean>
//	 * @throws
//	 */
//	@PostMapping("/doProxy")
//	public JsonResult<Boolean> doAreaProxy(@RequestBody ProductDTO productDTO) throws Exception {
//		return productServiceImpl.doAndCancelAreaProxy(productDTO.getId(), WhetherEnum.YES.getKey());
//	}
//
//	/**
//	 *
//	 * @Title: doCancelProxy
//	 * @Description: 取消区域代理
//	 * @param: productDTO
//	 * @return: JsonResult<Boolean>
//	 * @throws
//	 */
//	@PostMapping("/doCancelProxy")
//	public JsonResult<Boolean> doCancelAreaProxy(@RequestBody ProductDTO productDTO) throws Exception {
//		return productServiceImpl.doAndCancelAreaProxy(productDTO.getId(), WhetherEnum.NO.getKey());
//	}
//
//	/**
//	 *
//	 * @Title: doUpProduct
//	 * @Description: 上架商品
//	 * @param: productDTO
//	 * @return: JsonResult<Boolean>
//	 * @throws
//	 */
//	@PostMapping("/up")
//	public JsonResult<Boolean> doUpProduct(@RequestBody ProductDTO productDTO) throws Exception {
//		return productServiceImpl.doUpAndDownProduct(productDTO.getId(), ProductStatusEnum.UP.getKey());
//	}
//
//	/**
//	 *
//	 * @Title: doUpProduct
//	 * @Description: 下架商品
//	 * @param: productDTO
//	 * @return: JsonResult<Boolean>
//	 * @throws
//	 */
//	@PostMapping("/down")
//	public JsonResult<Boolean> doDownProduct(@RequestBody ProductDTO productDTO) throws Exception {
//		return productServiceImpl.doUpAndDownProduct(productDTO.getId(), ProductStatusEnum.DOWN.getKey());
//	}
//
//}
