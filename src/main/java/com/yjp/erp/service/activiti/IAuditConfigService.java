package com.yjp.erp.service.activiti;


import com.yjp.erp.model.dto.activiti.AuditConfigDTO;
import com.yjp.erp.model.po.activiti.AuditConfig;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.activiti.AuditConfigVO;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 审核配置表 服务类
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-03
 */
public interface IAuditConfigService extends IService<AuditConfig> {

	/**
	 * 
	 * @Title: insertAuditConfig   
	 * @Description: 新增审核配置
	 * @param: auditConfigDTO 入参
	 * @return: JsonResult<Boolean> true：成功	false：失败     
	 * @throws
	 */
	JsonResult<Boolean> insertAuditConfig(AuditConfigDTO auditConfigDTO);
	
	/**
	 * 
	 * @Title: updateAuditConfig   
	 * @Description: 更新审核配置
	 * @param: auditConfigDTO 入参
	 * @return: JsonResult<Boolean> true：成功	false：失败     
	 * @throws
	 */
	JsonResult<Boolean> updateAuditConfig(AuditConfigDTO auditConfigDTO);
	
	/**
	 * 
	 * @Title: deleteAuditConfig   
	 * @Description: 删除审核配置
	 * @param: id 入参
	 * @return: JsonResult<Boolean> true：成功	false：失败     
	 * @throws
	 */
	JsonResult<Boolean> deleteAuditConfig(Long id);
	
	/**
	 * 
	 * @Title: listAuditConfigPage   
	 * @Description: 分页查询审核配置
	 * @param: auditConfigDTO 入参
	 * @return: JsonResult<Page<AuditConfigVO>>      
	 * @throws
	 */
	JsonResult<Page<AuditConfigVO>> listAuditConfigPage(AuditConfigDTO auditConfigDTO);
	
	/**
	 * 
	 * @Title: viewAuditConfig   
	 * @Description: 查看审核配置
	 * @param: id 主键
	 * @return: JsonResult<AuditConfigVO>      
	 * @throws
	 */
	JsonResult<AuditConfigVO> viewAuditConfig(Long id);
	
	/**
	 * 
	 * @Title: updateStatus
	 * @Description: 启用或禁用审核配置
	 * @param: id 审核配置 id
	 * @param: status 状态(1:启用 0:禁用)
	 * @return: JsonResult<Boolean> true：成功 false：失败
	 * @throws
	 */
	JsonResult<Boolean> updateStatus(Long id, Integer status);
	
}
