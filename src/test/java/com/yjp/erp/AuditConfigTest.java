package com.yjp.erp;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.yjp.erp.model.po.activiti.AuditConfig;
import com.yjp.erp.model.po.activiti.Workflow;
import com.yjp.erp.service.activiti.IAuditConfigService;
import com.yjp.erp.service.activiti.IWorkflowService;
import com.yjp.erp.util.SnowflakeIdWorker;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ErpConfigPlatformApplication.class)
@Slf4j
public class AuditConfigTest {

	@Autowired
	IWorkflowService workflowServiceImpl;
	
	@Autowired
	IAuditConfigService auditConfigServiceImpl;
	
	
	@Test
	public void saveWorkflow(){
		Workflow entity = new Workflow();
		entity.setCreateDate(new Date());
		entity.setCreator(562998806254190592L);
		entity.setId(SnowflakeIdWorker.nextId());
		entity.setName("无审核工作流");
		entity.setDescription("无需审核，提交后成功");
		entity.setIdentifier("workflow_1");
		workflowServiceImpl.insert(entity);
		
		Workflow entity1 = new Workflow();
		entity1.setCreateDate(new Date());
		entity1.setCreator(562998806254190592L);
		entity1.setId(SnowflakeIdWorker.nextId());
		entity1.setName("一级审核工作流");
		entity1.setDescription("提交后审核一次");
		entity1.setIdentifier("workflow_2");
		workflowServiceImpl.insert(entity1);
		
		Workflow entity3 = new Workflow();
		entity3.setCreateDate(new Date());
		entity3.setCreator(562998806254190592L);
		entity3.setId(SnowflakeIdWorker.nextId());
		entity3.setName("二级审核工作流");
		entity3.setDescription("提交后审核两次");
		entity3.setIdentifier("workflow_3");
		workflowServiceImpl.insert(entity3);
		
		Workflow entity2 = new Workflow();
		entity2.setCreateDate(new Date());
		entity2.setCreator(562998806254190592L);
		entity2.setId(SnowflakeIdWorker.nextId());
		entity2.setName("三级审核工作流");
		entity2.setDescription("提交后审核三次");
		entity2.setIdentifier("workflow_4");
		workflowServiceImpl.insert(entity2);
		
	}
	
	@Test
	public void listWorkflow() {
		Page<Workflow> page = new Page<>();
		page.setCurrent(1);// 当前页
		page.setSize(3);// 每页大小
		Page<Workflow> pageList = workflowServiceImpl.selectPage(page);
		log.info("结果集：" + JSON.toJSONString(pageList));
	}
	
	@Test
	public void saveAuditConfig(){
		AuditConfig entity = new AuditConfig();
		entity.setId(SnowflakeIdWorker.nextId());
		entity.setTypeId("sku");
		entity.setClassId("bill");
		entity.setOrgId(1001L);
		entity.setWorkflowId(563298847497912320L);
		entity.setCreateDate(new Date());
		entity.setCreator(562998806254190592L);
		entity.setFirstLevelGroup("562998806254190593");
		entity.setDescription("测试数据1");
		auditConfigServiceImpl.insert(entity);
		
		AuditConfig entity2 = new AuditConfig();
		entity2.setId(SnowflakeIdWorker.nextId());
		entity2.setTypeId("RequisitionDetail");
		entity2.setClassId("bill");
		entity2.setOrgId(1002L);
		entity2.setWorkflowId(563298847674073088L);
		entity2.setCreateDate(new Date());
		entity2.setCreator(562998806254190592L);
		entity2.setFirstLevelGroup("562998806254190593");
		entity2.setSecondLevelGroup("562998806254190593,562998806254190594");
		entity2.setDescription("测试数据2");
		auditConfigServiceImpl.insert(entity2);
		
		AuditConfig entity3 = new AuditConfig();
		entity3.setId(SnowflakeIdWorker.nextId());
		entity3.setTypeId("Requisition");
		entity3.setClassId("entity");
		entity3.setOrgId(1003L);
		entity3.setWorkflowId(563301039978053632L);
		entity3.setCreateDate(new Date());
		entity3.setCreator(562998806254190592L);
		entity3.setFirstLevelGroup("562998806254190593");
		entity3.setSecondLevelGroup("562998806254190593,562998806254190594");
		entity3.setThirdLevelGroup("562998806254190593,562998806254190594,562998806254190594");
		entity3.setDescription("测试数据3");
		auditConfigServiceImpl.insert(entity3);
		
	}
	
	@Test
	public void listAuditConfig() {
		Page<AuditConfig> page = new Page<>();
		page.setCurrent(1);// 当前页
		page.setSize(2);// 每页大小
		Page<AuditConfig> pageList = auditConfigServiceImpl.selectPage(page);
		log.info("结果集：" + JSON.toJSONString(pageList));
	}
	
	@Test
	public void doAuditConfig() {
		Long id = 563331480290328576L;
		Integer status = 0;// 禁用
		auditConfigServiceImpl.updateStatus(id, status);
		status = 1;// 启用
		auditConfigServiceImpl.updateStatus(id, status);
	}
	
}
