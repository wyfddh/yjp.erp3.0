package com.yjp.erp.service.activiti;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.yjp.erp.model.dto.activiti.WorkflowDTO;
import com.yjp.erp.model.po.activiti.Workflow;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.activiti.WorkflowVO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工作流引擎表 服务类
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-03
 */
public interface IWorkflowService extends IService<Workflow> {
	
	/**
	 * 
	 * @Title: getWorkflowMapByIds   
	 * @Description: 根据工作流id集合获取工作流信息
	 * @param: ids 工作流id集合
	 * @return: Map<Long,Workflow>   
	 * @throws
	 */
	Map<Long, Workflow> getWorkflowMapByIds(List<Long> ids);
	
	/**
	 * 
	 * @Title: insertWorkflow   
	 * @Description: 新增保存工作流
	 * @param:  workflowDTO 入参
	 * @return: JsonResult<Boolean>  true：成功	false：失败    
	 * @throws
	 */
	Boolean insertWorkflow(WorkflowDTO workflowDTO) throws InvocationTargetException, IllegalAccessException;
	
	/**
	 * 
	 * @Title: insertWorkflow   
	 * @Description: 编辑保存工作流
	 * @param:  workflowDTO 入参
	 * @return: JsonResult<Boolean>  true：成功	false：失败    
	 * @throws
	 */
	Boolean updateWorkflow(WorkflowDTO workflowDTO) throws InvocationTargetException, IllegalAccessException;
	
	/**
	 * 
	 * @Title: deleteWorkflow   
	 * @Description: 根据主键删除工作流
	 * @param:  id 入参
	 * @return: JsonResult<Boolean>  true：成功	false：失败    
	 * @throws
	 */
	Boolean deleteWorkflow(Long id);
	
	/**
	 * 
	 * @Title: listWorkflowPage   
	 * @Description: 分页查询工作流
	 * @param: workflowDTO 入参
	 * @return: JsonResult<Page<WorkflowVO>>      
	 * @throws
	 */
	JsonResult<Page<WorkflowVO>> listWorkflowPage(WorkflowDTO workflowDTO) throws InvocationTargetException, IllegalAccessException;
	
	/**
	 * 
	 * @Title: viewWorkflow   
	 * @Description: 查看工作流
	 * @param:  id 主键
	 * @return: JsonResult<WorkflowVO>      
	 * @throws
	 */
	JsonResult<WorkflowVO> viewWorkflow(Long id);
	
	/**
	 * 
	 * @Title: listAllWorkflow   
	 * @Description: 查询所有工作流
	 * @param: workflowDTO 入参
	 * @return: JsonResult<List<WorkflowVO>>      
	 * @throws
	 */
	JsonResult<List<WorkflowVO>> listAllWorkflow(WorkflowDTO workflowDTO);
	
}
