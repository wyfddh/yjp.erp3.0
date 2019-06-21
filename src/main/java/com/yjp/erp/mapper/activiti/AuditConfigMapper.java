package com.yjp.erp.mapper.activiti;

import org.apache.ibatis.annotations.Param;

import com.yjp.erp.model.po.activiti.AuditConfig;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 * 审核配置表 Mapper 接口
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-03
 */
public interface AuditConfigMapper extends BaseMapper<AuditConfig> {

	/**
	 * 
	 * @Title: updateStatus   
	 * @Description: 启用或禁用审核配置
	 * @param: id 审核配置id
	 * @param: status 状态(1:启用 0:禁用)
	 * @return: int 影响记录行数
	 * @throws
	 */
	int updateStatus(@Param("id") String id, @Param("status") String status);
	
}
