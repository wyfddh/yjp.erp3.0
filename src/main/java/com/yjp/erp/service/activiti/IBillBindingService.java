package com.yjp.erp.service.activiti;

import java.util.List;

import com.yjp.erp.model.dto.activiti.BillBindingDTO;
import com.yjp.erp.model.po.activiti.BillBinding;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.activiti.BillBindingVO;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 单据绑定表 服务类
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-04
 */
public interface IBillBindingService extends IService<BillBinding> {

	/**
	 * 
	 * @Title: insertBillBinding
	 * @Description: 新增保存单据绑定
	 * @param: billBindingDTO 入参
	 * @return: JsonResult<Boolean> true：成功 false：失败
	 * @throws
	 */
	JsonResult<Boolean> insertBillBinding(BillBindingDTO billBindingDTO);

	/**
	 * 
	 * @Title: updateBillBinding
	 * @Description: 编辑保存单据绑定
	 * @param: billBindingDTO 入参
	 * @return: JsonResult<Boolean> true：成功 false：失败
	 * @throws
	 */
	JsonResult<Boolean> updateBillBinding(BillBindingDTO billBindingDTO);

	/**
	 * 
	 * @Title: deleteBillBinding
	 * @Description: 删除单据绑定
	 * @param: id 主键
	 * @return: JsonResult<Boolean> true：成功 false：失败
	 * @throws
	 */
	JsonResult<Boolean> deleteBillBinding(Long id);

	/**
	 * 
	 * @Title: listBillBindingPage
	 * @Description: 分页查询单据绑定
	 * @param: billBindingDTO 入参
	 * @return: JsonResult<Page<BillBindingVO>>
	 * @throws
	 */
	JsonResult<Page<BillBindingVO>> listBillBindingPage(BillBindingDTO billBindingDTO);

	/**
	 * 
	 * @Title: viewBillBinding
	 * @Description: 查看单据绑定
	 * @param: id 主键
	 * @return: JsonResult<BillBindingVO>
	 * @throws
	 */
	JsonResult<BillBindingVO> viewBillBinding(Long id);

	/**
	 * 
	 * @Title: listBillBindings   
	 * @Description: 查询所有单据绑定 
	 * @return: JsonResult<List<BillBindingVO>>      
	 * @throws
	 */
	JsonResult<List<BillBindingVO>> listBillBindings();

}
