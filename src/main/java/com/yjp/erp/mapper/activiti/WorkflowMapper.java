package com.yjp.erp.mapper.activiti;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.yjp.erp.model.po.activiti.Workflow;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
 * 模版表 Mapper 接口
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-03
 */
public interface WorkflowMapper extends BaseMapper<Workflow> {

	/**
	 * 
	 * @Title: selectWorkflowPage
	 * @Description: 分页查询工作流
	 * @param: page 分页参数
	 * @param: paramsMap 查询参数
	 * @return: List<Workflow>
	 * @throws
	 */
	List<Workflow> selectWorkflowPage(@Param("page") Pagination page, @Param("params") Map<String, Object> paramsMap);
	/**
	 * 
	 * @Title: getWorkflowMapByIds   
	 * @Description: 根据工作流id集合获取工作流信息
	 * @param: ids 工作流id集合
	 * @return: Map<Long,Workflow>      
	 * @throws
	 */
	@MapKey("id")
	Map<Long, Workflow> getWorkflowMapByIds(List<Long> ids);
	
}
