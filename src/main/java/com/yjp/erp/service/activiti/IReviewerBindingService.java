package com.yjp.erp.service.activiti;



import com.yjp.erp.model.dto.activiti.ReviewerBindingDTO;
import com.yjp.erp.model.po.activiti.ReviewerBinding;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.activiti.ReviewerBindingVO;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import java.lang.reflect.InvocationTargetException;

/**
 * <p>
 * 审核人绑定表 服务类
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-04
 */
public interface IReviewerBindingService extends IService<ReviewerBinding> {

	/**
	 * 
	 * @Title: insertReviewerBinding   
	 * @Description: 新增保存审核人绑定
	 * @param: reviewerBindingDTO 入参
	 * @return: JsonResult<Boolean>  true：成功	false：失败    
	 * @throws
	 */
	Boolean insertReviewerBinding(ReviewerBindingDTO reviewerBindingDTO) throws InvocationTargetException, IllegalAccessException;
	
	/**
	 * 
	 * @Title: updateReviewerBinding   
	 * @Description: 编辑保存审核人绑定
	 * @param: reviewerBindingDTO 入参
	 * @return: JsonResult<Boolean>  true：成功	false：失败    
	 * @throws
	 */
	Boolean updateReviewerBinding(ReviewerBindingDTO reviewerBindingDTO) throws InvocationTargetException, IllegalAccessException;
	
	/**
	 * 
	 * @Title: deleteReviewerBinding   
	 * @Description: 删除审核人绑定
	 * @param: id 入参
	 * @return: JsonResult<Boolean>  true：成功	false：失败    
	 * @throws
	 */
	Boolean deleteReviewerBinding(Long id);
	
	/**
	 * 
	 * @Title: listReviewerBindingPage   
	 * @Description: 分页查询审核人绑定
	 * @param: reviewerBindingDTO 入参
	 * @return: JsonResult<Page<ReviewerBindingVO>>
	 * @throws
	 */
	JsonResult<Page<ReviewerBindingVO>> listReviewerBindingPage(ReviewerBindingDTO reviewerBindingDTO) throws InvocationTargetException, IllegalAccessException;
	
	/**
	 * 
	 * @Title: viewReviewerBinding   
	 * @Description: 查看审核人绑定
	 * @param: id 入参
	 * @return: JsonResult<ReviewerBindingVO>
	 * @throws
	 */
	JsonResult<ReviewerBindingVO> viewReviewerBinding(Long id);
	
}
