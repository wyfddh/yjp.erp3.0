package com.yjp.erp.util;

import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.yjp.erp.constants.ProductStatusEnum;
import com.yjp.erp.constants.StatusEnum;
import com.yjp.erp.constants.WhetherEnum;
import com.yjp.erp.model.po.activiti.AuditConfig;
import com.yjp.erp.model.po.activiti.BillBinding;
import com.yjp.erp.model.po.activiti.ReviewerBinding;
import com.yjp.erp.model.po.activiti.Workflow;
import com.yjp.erp.model.po.product.Product;
import com.yjp.erp.model.po.system.Organization;
import com.yjp.erp.model.po.system.User;
import com.yjp.erp.model.vo.activiti.*;
import com.yjp.erp.model.vo.product.ProductVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName: FormatReturnEntity
 * @Description: 格式化返回实体
 * @author: jihongjin@yijiupi.com
 * @date: 2019年4月10日 上午10:59:01
 * 
 * @Copyright: 2019 www.yijiupi.com Inc. All rights reserved.
 */
public class FormatReturnEntity {

	private static final Logger log = LoggerFactory.getLogger(FormatReturnEntity.class);

	/**
	 * 
	 * @Title: formatWorkflow
	 * @Description: 工作流对象格式化
	 * @param: workflow 工作流对象
	 * @param: userMap 用户对象
	 * @return: WorkflowVO 工作流返回实体
	 * @throws
	 */
	public static WorkflowVO formatWorkflow(Workflow workflow, Map<Long, User> userMap) {
		WorkflowVO workflowVO = new WorkflowVO();
		try {
			BeanUtils.copyProperties(workflow, workflowVO);
		} catch (Exception e) {
			log.error("工作流对象拷贝异常：" + e.getMessage());
		}
		// 格式化日期
		String createDateStr = TimeUtils.parseTime(TimeUtils.asLocalDateTime(workflow.getCreateDate()));
		workflowVO.setCreateDateStr(createDateStr);
		String creatorName = "";
		if (userMap != null && !userMap.isEmpty()) {
			User user = userMap.get(workflow.getCreator());
			creatorName = user != null ? user.getDisplayName() : "";
		}
		// 格式化创建人姓名
		workflowVO.setCreatorName(creatorName);
		return workflowVO;
	}

	/**
	 * 
	 * @Title: formatWorkflowList
	 * @Description: 工作流集合格式化
	 * @param: workflow 工作流集合
	 * @param: userMap 用户对象
	 * @return: List<WorkflowVO> 工作流返回集合
	 * @throws
	 */
	public static List<WorkflowVO> formatWorkflowList(List<Workflow> list, Map<Long, User> userMap) {
		List<WorkflowVO> workflowList = Lists.newArrayList();
		if (list != null && !list.isEmpty()) {
			for (Workflow workflow : list) {
				WorkflowVO workflowVO = formatWorkflow(workflow, userMap);
				workflowList.add(workflowVO);
			}
		}
		return workflowList;
	}

	/**
	 * 
	 * @Title: formatWorkflowPageList
	 * @Description: 工作流分页集合格式化
	 * @param: pageList 工作流分页集合
	 * @param: userMap 用户对象
	 * @return: Page<WorkflowVO> 工作流返回分页集合
	 * @throws
	 */
	public static Page<WorkflowVO> formatWorkflowPageList(Page<Workflow> pageList, Map<Long, User> userMap) {
		Page<WorkflowVO> pageVOList = new Page<>(pageList.getCurrent(), pageList.getSize());
		try {
			BeanUtils.copyProperties(pageList, pageVOList);
		} catch (Exception e) {
			log.error("工作流分页拷贝异常：" + e.getMessage());
		}
		List<Workflow> records = pageList.getRecords();
		List<WorkflowVO> formatWorkflowList = formatWorkflowList(records, userMap);
		pageVOList.setTotal(pageList.getTotal());
		pageVOList.setRecords(formatWorkflowList);
		return pageVOList;
	}

	/**
	 * 
	 * @Title: formatReviewerBindingPageList
	 * @Description: 格式化审核人绑定分页集合
	 * @param: list 审核人绑定分页集合
	 * @param: orgMap 组织或区域map
	 * @param: userMap 用户map
	 * @throws
	 */
	public static Page<ReviewerBindingVO> formatReviewerBindingPageList(Page<ReviewerBinding> pageList, Map<Long, Organization> orgMap, Map<Long, User> userMap) {
		Page<ReviewerBindingVO> page = new Page<>(pageList.getCurrent(), pageList.getSize());
		try {
			BeanUtils.copyProperties(pageList, page);
		} catch (Exception e) {
			log.error("审核人绑定分页拷贝异常：" + e.getMessage());
		}
		List<ReviewerBinding> records = pageList.getRecords();
		List<ReviewerBindingVO> formatReviewerBindingList = formatReviewerBindingList(records, orgMap, userMap);
		page.setRecords(formatReviewerBindingList);
		page.setTotal(pageList.getTotal());
		return page;
	}

	/**
	 * 
	 * @Title: formatReviewerBindingList
	 * @Description: 格式化审核人绑定集合
	 * @param: list 审核人绑定集合
	 * @param: orgMap 组织或区域map
	 * @param: userMap 用户map
	 * @throws
	 */
	public static List<ReviewerBindingVO> formatReviewerBindingList(List<ReviewerBinding> list, Map<Long, Organization> orgMap, Map<Long, User> userMap) {
		List<ReviewerBindingVO> reviewerBindingVOs = Lists.newArrayList();
		if (list != null && !list.isEmpty()) {
			for (ReviewerBinding reviewerBinding : list) {
				ReviewerBindingVO formatReviewerBinding = formatReviewerBinding(reviewerBinding, orgMap, userMap);
				reviewerBindingVOs.add(formatReviewerBinding);
			}
		}
		return reviewerBindingVOs;
	}

	/**
	 * 
	 * @Title: formatReviewerBinding
	 * @Description: 格式化审核人绑定对象
	 * @param: reviewerBinding 审核人绑定对象
	 * @param: orgMap 组织或区域map
	 * @param: userMap 用户map
	 * @throws
	 */
	public static ReviewerBindingVO formatReviewerBinding(ReviewerBinding reviewerBinding, Map<Long, Organization> orgMap, Map<Long, User> userMap) {
		ReviewerBindingVO reviewerBindingVO = new ReviewerBindingVO();
		try {
			BeanUtils.copyProperties(reviewerBinding, reviewerBindingVO);
		} catch (Exception e) {
			log.error("审核人绑定拷贝异常：" + e.getMessage());
		}
		// 格式化日期
		String createDateStr = TimeUtils.parseTime(TimeUtils.asLocalDateTime(reviewerBinding.getCreateDate()));
		reviewerBindingVO.setCreateDateStr(createDateStr);

		String creatorName = "";
		StringBuffer firstLevelGroupName = new StringBuffer();
		StringBuffer secondLevelGroupName = new StringBuffer();
		StringBuffer thirdLevelGroupName = new StringBuffer();

		if (userMap != null && !userMap.isEmpty()) {
			User user = userMap.get(reviewerBinding.getCreator());
			creatorName = user != null ? user.getDisplayName() : "";

			String firstLevelGroup = reviewerBinding.getFirstLevelGroup();
			if (StringUtils.isNotBlank(firstLevelGroup)) {
				String[] firstArr = firstLevelGroup.split(",");
				for (String v : firstArr) {
					if(StringUtils.isNotBlank(v)){
						User userFirst = userMap.get(Long.valueOf(v));
						String firstName = userFirst != null ? userFirst.getDisplayName() : "";
						if (StringUtils.isNotBlank(firstName)) {
							firstLevelGroupName.append(firstName).append(",");
						}
					}
				}
			}

			String secondLevelGroup = reviewerBinding.getSecondLevelGroup();
			if (StringUtils.isNotBlank(secondLevelGroup)) {
				String[] secondArr = secondLevelGroup.split(",");
				for (String v : secondArr) {
					if(StringUtils.isNotBlank(v)){
						User userSecond = userMap.get(Long.valueOf(v));
						String secondName = userSecond != null ? userSecond.getDisplayName() : "";
						if (StringUtils.isNotBlank(secondName)) {
							secondLevelGroupName.append(secondName).append(",");
						}
					}
				}
			}

			String thirdLevelGroup = reviewerBinding.getThirdLevelGroup();
			if (StringUtils.isNotBlank(thirdLevelGroup)) {
				String[] thirdArr = thirdLevelGroup.split(",");
				for (String v : thirdArr) {
					if(StringUtils.isNotBlank(v)){
						User userThird = userMap.get(Long.valueOf(v));
						String thirdName = userThird != null ? userThird.getDisplayName() : "";
						if (StringUtils.isNotBlank(thirdName)) {
							thirdLevelGroupName.append(thirdName).append(",");
						}
					}
				}
			}

		}
		// 格式化创建人姓名
		reviewerBindingVO.setCreatorName(creatorName);
		// 格式化组织或区域名称
		String orgName = "";
		if (orgMap != null && !orgMap.isEmpty()) {
			Organization org = orgMap.get(reviewerBinding.getOrgId());
			orgName = org != null ? org.getName() : "";
		}
		reviewerBindingVO.setOrgName(orgName);
		// 格式化一级审核组
		if (StringUtils.isNotBlank(firstLevelGroupName)) {
			reviewerBindingVO.setFirstLevelGroupName(firstLevelGroupName.substring(0, firstLevelGroupName.length() - 1));
		}
		// 格式化二级审核组
		if (StringUtils.isNotBlank(secondLevelGroupName)) {
			reviewerBindingVO.setSecondLevelGroupName(secondLevelGroupName.substring(0, secondLevelGroupName.length() - 1));
		}
		// 格式化三级审核组
		if (StringUtils.isNotBlank(thirdLevelGroupName)) {
			reviewerBindingVO.setThirdLevelGroupName(thirdLevelGroupName.substring(0, thirdLevelGroupName.length() - 1));
		}
		return reviewerBindingVO;
	}

	/**
	 * 
	 * @Title: formatBillBindingPageList
	 * @Description: 格式化单据绑定分页集合
	 * @param: pageList 单据绑定分页集合
	 * @param: workflowMap 工作流map
	 * @param: userMap 用户map
	 * @return: Page<BillBindingVO>
	 * @throws
	 */
	public static Page<BillBindingVO> formatBillBindingPageList(Page<BillBinding> pageList, Map<Long, Workflow> workflowMap, Map<Long, User> userMap) {
		Page<BillBindingVO> page = new Page<>(pageList.getCurrent(), pageList.getSize());
		List<BillBinding> records = pageList.getRecords();
		List<BillBindingVO> formatBillBindingList = formatBillBindingList(records, workflowMap, userMap);
		try {
			BeanUtils.copyProperties(page, pageList);
		} catch (Exception e) {
			log.error("单据绑定分页拷贝异常：" + e.getMessage());
		}
		page.setTotal(pageList.getTotal());
		page.setRecords(formatBillBindingList);
		return page;
	}

	/**
	 * 
	 * @Title: formatBillBindingList
	 * @Description: 格式化单据绑定集合
	 * @param: billBinding 单据绑定集合
	 * @param: workflowMap 工作流map
	 * @param: userMap 用户map
	 * @return: List<BillBindingVO>
	 * @throws
	 */
	public static List<BillBindingVO> formatBillBindingList(List<BillBinding> list, Map<Long, Workflow> workflowMap, Map<Long, User> userMap) {
		List<BillBindingVO> billBindingVOs = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (BillBinding billBinding : list) {
				BillBindingVO billBindingVO = formatBillBinding(billBinding, workflowMap, userMap);
				billBindingVOs.add(billBindingVO);
			}
		}
		return billBindingVOs;
	}

	/**
	 * 
	 * @Title: formatBillBinding
	 * @Description: 格式化单据绑定对象
	 * @param: billBinding 单据绑定对象
	 * @param: workflowMap 工作流map
	 * @param: userMap 用户map
	 * @return: BillBindingVO
	 * @throws
	 */
	public static BillBindingVO formatBillBinding(BillBinding billBinding, Map<Long, Workflow> workflowMap, Map<Long, User> userMap) {
		BillBindingVO billBindingVO = new BillBindingVO();
		try {
			BeanUtils.copyProperties(billBinding, billBindingVO);
		} catch (Exception e) {
			log.error("单据绑定对象拷贝异常：" + e.getMessage());
		}
		// 格式化日期
		String createDateStr = TimeUtils.parseTime(TimeUtils.asLocalDateTime(billBinding.getCreateDate()));
		billBindingVO.setCreateDateStr(createDateStr);
		String creatorName = "";
		if (userMap != null && !userMap.isEmpty()) {
			User user = userMap.get(billBinding.getCreator());
			creatorName = user != null ? user.getDisplayName() : "";
		}
		// 格式化创建人姓名
		billBindingVO.setCreatorName(creatorName);

		String workflowName = "";
		if (workflowMap != null && !workflowMap.isEmpty()) {
			Workflow workflow = workflowMap.get(billBinding.getWorkflowId());
			workflowName = workflow != null ? workflow.getName() : "";
		}
		// 格式化工作流名称
		billBindingVO.setWorkflowName(workflowName);
		//前台显示
		billBindingVO.setLabel(billBindingVO.getName());
		return billBindingVO;
	}

	/**
	 * 
	 * @Title: formatAuditConfigPageList
	 * @Description: 格式化审核配置分页对象
	 * @param: pageList 审核配置分页对象
	 * @param: orgMap 组织或区域map
	 * @param: userMap 用户map
	 * @throws
	 */
	public static Page<AuditConfigVO> formatAuditConfigPageList(Page<AuditConfig> pageList, Map<Long, Organization> orgMap, Map<Long, Workflow> workflowMap, Map<Long, User> userMap) {
		Page<AuditConfigVO> page = new Page<>(pageList.getCurrent(), pageList.getSize());
		try {
			BeanUtils.copyProperties(page, pageList);
		} catch (Exception e) {
			log.error("审核配置分页拷贝异常：" + e.getMessage());
		}
		List<AuditConfig> records = pageList.getRecords();
		List<AuditConfigVO> formatAuditConfigList = formatAuditConfigList(records, orgMap, workflowMap, userMap);
		page.setTotal(pageList.getTotal());
		page.setRecords(formatAuditConfigList);
		return page;
	}

	/**
	 * 
	 * @Title: formatAuditConfigList
	 * @Description: 格式化审核配置集合对象
	 * @param: pageList 审核配置集合对象
	 * @param: orgMap 组织或区域map
	 * @param: userMap 用户map
	 * @throws
	 */
	public static List<AuditConfigVO> formatAuditConfigList(List<AuditConfig> list, Map<Long, Organization> orgMap, Map<Long, Workflow> workflowMap, Map<Long, User> userMap) {
		List<AuditConfigVO> auditConfigVOs = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(list)) {
			for (AuditConfig auditConfig : list) {
				AuditConfigVO formatAuditConfig = formatAuditConfig(auditConfig, orgMap, workflowMap, userMap);
				auditConfigVOs.add(formatAuditConfig);
			}
		}
		return auditConfigVOs;
	}

	/**
	 * 
	 * @Title: formatAuditConfig
	 * @Description: 格式化审核配置对象
	 * @param: pageList 审核配置对象
	 * @param: orgMap 组织或区域map
	 * @param: userMap 用户map
	 * @throws
	 */
	public static AuditConfigVO formatAuditConfig(AuditConfig auditConfig, Map<Long, Organization> orgMap, Map<Long, Workflow> workflowMap, Map<Long, User> userMap) {
		AuditConfigVO auditConfigVO = new AuditConfigVO();
		try {
			BeanUtils.copyProperties(auditConfigVO, auditConfig);
		} catch (Exception e) {
			log.error("审核配置格式化异常：" + e.getMessage());
		}
		// 格式化日期
		String createDateStr = TimeUtils.parseTime(TimeUtils.asLocalDateTime(auditConfig.getCreateDate()));
		auditConfigVO.setCreateDateStr(createDateStr);

		String creatorName = "";
		StringBuffer firstLevelGroupName = new StringBuffer();
		StringBuffer secondLevelGroupName = new StringBuffer();
		StringBuffer thirdLevelGroupName = new StringBuffer();

		if (userMap != null && !userMap.isEmpty()) {
			User user = userMap.get(auditConfig.getCreator());
			creatorName = user != null ? user.getDisplayName() : "";

			String firstLevelGroup = auditConfig.getFirstLevelGroup();
			if (StringUtils.isNotBlank(firstLevelGroup)) {
				String[] firstArr = firstLevelGroup.split(",");
				for (String v : firstArr) {
					if(StringUtils.isNotBlank(v)){
						User userFirst = userMap.get(Long.valueOf(v));
						String firstName = userFirst != null ? userFirst.getDisplayName() : "";
						if (StringUtils.isNotBlank(firstName)) {
							firstLevelGroupName.append(firstName).append(",");
						}
					}
				}
			}

			String secondLevelGroup = auditConfig.getSecondLevelGroup();
			if (StringUtils.isNotBlank(secondLevelGroup)) {
				String[] secondArr = secondLevelGroup.split(",");
				for (String v : secondArr) {
					if(StringUtils.isNotBlank(v)){
						User userSecond = userMap.get(Long.valueOf(v));
						String secondName = userSecond != null ? userSecond.getDisplayName() : "";
						if (StringUtils.isNotBlank(secondName)) {
							secondLevelGroupName.append(secondName).append(",");
						}
					}
				}
			}

			String thirdLevelGroup = auditConfig.getThirdLevelGroup();
			if (StringUtils.isNotBlank(thirdLevelGroup)) {
				String[] thirdArr = thirdLevelGroup.split(",");
				for (String v : thirdArr) {
					if(StringUtils.isNotBlank(v)){
						User userThird = userMap.get(Long.valueOf(v));
						String thirdName = userThird != null ? userThird.getDisplayName() : "";
						if (StringUtils.isNotBlank(thirdName)) {
							thirdLevelGroupName.append(thirdName).append(",");
						}
					}
				}
			}

		}
		// 格式化创建人姓名
		auditConfigVO.setCreatorName(creatorName);
		// 格式化组织或区域名称
		String orgName = "";
		if (orgMap != null && !orgMap.isEmpty()) {
			Organization org = orgMap.get(auditConfig.getOrgId());
			orgName = org != null ? org.getName() : "";
		}
		auditConfigVO.setOrgName(orgName);
		String workflowName = "";
		if (workflowMap != null && !workflowMap.isEmpty()) {
			Workflow workflow = workflowMap.get(auditConfig.getWorkflowId());
			workflowName = workflow != null ? workflow.getName() : "";
		}
		// 格式化工作流名称
		auditConfigVO.setWorkflowName(workflowName);
		// 格式化一级审核组
		if (StringUtils.isNotBlank(firstLevelGroupName)) {
			auditConfigVO.setFirstLevelGroupName(firstLevelGroupName.substring(0, firstLevelGroupName.length() - 1));
		}
		// 格式化二级审核组
		if (StringUtils.isNotBlank(secondLevelGroupName)) {
			auditConfigVO.setSecondLevelGroupName(secondLevelGroupName.substring(0, secondLevelGroupName.length() - 1));
		}
		// 格式化三级审核组
		if (StringUtils.isNotBlank(thirdLevelGroupName)) {
			auditConfigVO.setThirdLevelGroupName(thirdLevelGroupName.substring(0, thirdLevelGroupName.length() - 1));
		}
		String statusName = StatusEnum.getValueByKey(auditConfig.getStatus());
		// 格式化状态
		auditConfigVO.setStatusName(statusName);
		return auditConfigVO;
	}

	/**
	 * 
	 * @Title: formatTaskDetailList   
	 * @Description: 格式化历史明细
	 * @param: taskDetails 历史明细
	 * @param: userMap 用户map  
	 * @throws
	 */
	public static void formatTaskDetailList(List<TaskDetailVO> taskDetails, Map<Long, User> userMap) {
		if (CollectionUtils.isNotEmpty(taskDetails)) {
			for (TaskDetailVO detail : taskDetails) {
				String creator = detail.getSubmitter();
				if (userMap != null && !userMap.isEmpty()) {
					User user = userMap.get(Long.valueOf(creator));
					String displayName = user != null ? user.getDisplayName() : "";
					detail.setSubmitter(displayName);
				}
			}
		}
	}

	/**
	 * 
	 * @Title: formatProcessTaskList   
	 * @Description: 格式化待办和已办集合
	 * @param: taskVOList 待办和已办集合
	 * @param: userMap 用户map     
	 * @return: void      
	 * @throws
	 */
	public static void formatProcessTaskList(List<ProcessTaskVO> taskVOList, Map<Long, User> userMap) {
		if (CollectionUtils.isNotEmpty(taskVOList)) {
			for (ProcessTaskVO task : taskVOList) {
				String processCreator = task.getProcessCreator();
				if (userMap != null && !userMap.isEmpty()) {
					User user = userMap.get(Long.valueOf(processCreator));
					String displayName = user != null ? user.getDisplayName() : "";
					task.setProcessCreator(displayName);
				}
			}
		}
	}

	/**
	 * 
	 * @Title: formatProductPageList   
	 * @Description: 格式化产品分页列表
	 * @param: pageList 产品分页列表
	 * @param: userMap 用户map
	 * @return: Page<ProductVO>      
	 * @throws
	 */
	public static Page<ProductVO> formatProductPageList(Page<Product> pageList, Map<Long, User> userMap) {
		Page<ProductVO> pageVO = new Page<>(pageList.getCurrent(), pageList.getSize());
		List<Product> records = pageList.getRecords();
		List<ProductVO> formatProductList = formatProductList(records, userMap);
		pageVO.setRecords(formatProductList);
		pageVO.setTotal(pageList.getTotal());
		return pageVO;
	}

	/**
	 * 
	 * @Title: formatProductList   
	 * @Description: 格式化产品列表
	 * @param: records 产品列表
	 * @param: userMap 用户map
	 * @return: List<ProductVO>      
	 * @throws
	 */
	public static List<ProductVO> formatProductList(List<Product> records, Map<Long, User> userMap) {
		List<ProductVO> productVOs = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(records)) {
			for (Product product : records) {
				ProductVO formatProduct = formatProduct(product, userMap);
				productVOs.add(formatProduct);
			}
		}
		return productVOs;
	}

	/**
	 * 
	 * @Title: formatProduct   
	 * @Description: 格式化产品对象
	 * @param: product 产品对象
	 * @param: userMap 用户map
	 * @return: ProductVO      
	 * @throws
	 */
	public static ProductVO formatProduct(Product product, Map<Long, User> userMap) {
		ProductVO productVO = new ProductVO();
		try {
			BeanUtils.copyProperties(productVO, product);
		} catch (Exception e) {
			log.error("产品格式化异常：" + e.getMessage());
		}
		// 格式化日期
		String createDateStr = TimeUtils.parseTime(TimeUtils.asLocalDateTime(product.getCreateDate()));
		productVO.setCreateDateStr(createDateStr);
		Date updateDate = product.getUpdateDate();
		if(updateDate != null){
			String updateDateStr = TimeUtils.parseTime(TimeUtils.asLocalDateTime(updateDate));
			productVO.setUpdateDateStr(updateDateStr);
		}
		if (userMap != null && !userMap.isEmpty()) {
			User creator = userMap.get(productVO.getCreator());
			String creatorName = creator != null ? creator.getDisplayName() : "";
			productVO.setCreatorName(creatorName);

			User updator = userMap.get(productVO.getUpdator());
			String updatorName = updator != null ? updator.getDisplayName() : "";
			productVO.setUpdatorName(updatorName);
		}
		productVO.setStatusName(ProductStatusEnum.getValueByKey(Integer.parseInt(product.getStatus())));
		productVO.setAreaProxyName(WhetherEnum.getValueByKey(Integer.parseInt(product.getAreaProxy())));
		return productVO;
	}

}
