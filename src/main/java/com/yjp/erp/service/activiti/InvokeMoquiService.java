package com.yjp.erp.service.activiti;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.yjp.erp.model.dto.activiti.DoneAndUpComeDTO;
import com.yjp.erp.model.dto.activiti.WorkflowInstanceDTO;
import com.yjp.erp.model.dto.gateway.BillAuditDTO;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.activiti.ProcessTaskVO;

public interface InvokeMoquiService {
	/**
	 * 
	 * @Title: createWorkflowInstance   
	 * @Description: 创建工作流实例
	 * @param: workflowInstanceDTO 入参
	 * @return: JsonResult<WorkflowInstanceVO>      
	 * @throws
	 */
	JsonResult<JSONObject> createWorkflowInstance(WorkflowInstanceDTO workflowInstanceDTO);
	
	/**
	 * 
	 * @Title: auditSubmit   
	 * @Description: 审核提交
	 * @param: billAuditDTO 入参
	 * @return: JsonResult<String>      
	 * @throws
	 */
	JsonResult<String> auditSubmit(BillAuditDTO billAuditDTO);

	JsonResult<Page<ProcessTaskVO>> getProcessedData(String invokeUrl, DoneAndUpComeDTO doneAndUpComeDTO) throws Exception;
	
}
