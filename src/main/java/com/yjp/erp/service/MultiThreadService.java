package com.yjp.erp.service;

import com.yjp.erp.model.po.system.Organization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yjp.erp.model.po.activiti.Workflow;
import com.yjp.erp.model.po.system.User;
import com.yjp.erp.service.activiti.IWorkflowService;
import com.yjp.erp.service.system.OrgService;
import com.yjp.erp.service.system.UserService;
import com.yjp.erp.model.vo.ReturnDataVO;


/**
 * 
 * @ClassName: MultiThreadService   
 * @Description: 多线程获取接口数据
 * @author: jihongjin@yijiupi.com
 * @date: 2019年4月10日 下午5:20:26   
 *  
 * @Copyright: 2019 www.yijiupi.com Inc. All rights reserved.
 */
@Slf4j
@Service
public class MultiThreadService {

	private ExecutorService executorService = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

	@Autowired
	IWorkflowService workflowServiceImpl;
	
	@Autowired
    OrgService orgService;
	
	@Autowired
	UserService userService;
	
	/**
	 * 
	 * @Title: getBaseDataMap   
	 * @Description: 获取基础数据名称
	 * @param: workflowIds 工作流id集合
	 * @param: userIds 用户id集合
	 * @param: orgIds 组织id集合
	 * @return: ReturnDataVO      
	 * @throws
	 */
	public ReturnDataVO getBaseDataMap(List<Long> workflowIds, List<Long> userIds, List<Long> orgIds) {
		ReturnDataVO returnDataVO = new ReturnDataVO();
		FutureTask<Map<Long, Workflow>> workflowTask = new FutureTask<>(new Callable<Map<Long, Workflow>>() {
			@Override
			public Map<Long, Workflow> call() throws Exception {
				if (CollectionUtils.isNotEmpty(workflowIds)) {
					return workflowServiceImpl.getWorkflowMapByIds(workflowIds);
				}
				return new HashMap<>();
			}
		});
		FutureTask<Map<Long, Organization>> orgTask = new FutureTask<>(new Callable<Map<Long, Organization>>() {
			@Override
			public Map<Long, Organization> call() throws Exception {
				if (CollectionUtils.isNotEmpty(orgIds)) {
					return orgService.getOrgMapByIds(orgIds);
				}
				return new HashMap<>();
			}
		});
		FutureTask<Map<Long, User>> userTask = new FutureTask<>(new Callable<Map<Long, User>>() {
			@Override
			public Map<Long, User> call() throws Exception {
				if (CollectionUtils.isNotEmpty(userIds)) {
					return userService.getUserMapByIds(userIds);
				}
				return new HashMap<>();
			}
		});
		
		executorService.submit(workflowTask);
		executorService.submit(orgTask);
		executorService.submit(userTask);
		
		try {
			Map<Long, Workflow> workflowMap = workflowTask.get();
			returnDataVO.setWorkflowMap(workflowMap);
		} catch (Exception e) {
			log.error("根据工作流id集合获取工作流异常：" + e.getMessage());
		}
		try {
			Map<Long, Organization> areaMap = orgTask.get();
			returnDataVO.setOrgMap(areaMap);
		} catch (Exception e) {
			log.error("根据组织或区域id查询组织或区域异常：" + e.getMessage());
		}
		try {
			Map<Long, User> userMap = userTask.get();
			returnDataVO.setUserMap(userMap);
		} catch (Exception e) {
			log.error("根据用户id集合查询用户信息异常：" + e.getMessage());
		}
		return returnDataVO;
	}
	
	
}
