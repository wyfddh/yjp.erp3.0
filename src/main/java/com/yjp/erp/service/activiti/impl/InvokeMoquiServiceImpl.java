package com.yjp.erp.service.activiti.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yjp.erp.conf.UserInfoManager;
import com.yjp.erp.constants.WorkflowStatusEnum;
import com.yjp.erp.model.dto.activiti.DoneAndUpComeDTO;
import com.yjp.erp.model.dto.activiti.WorkflowInstanceDTO;
import com.yjp.erp.model.dto.gateway.BillAuditDTO;
import com.yjp.erp.model.dto.gateway.QueryDetailDTO;
import com.yjp.erp.handle.GatewayHandle;
import com.yjp.erp.mapper.ModuleMapper;
import com.yjp.erp.model.po.activiti.BillBinding;
import com.yjp.erp.model.po.activiti.ReviewerBinding;
import com.yjp.erp.model.po.activiti.Workflow;
import com.yjp.erp.model.po.bill.Bill;
import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.po.system.User;
import com.yjp.erp.service.BillService;
import com.yjp.erp.service.activiti.IBillBindingService;
import com.yjp.erp.service.activiti.IReviewerBindingService;
import com.yjp.erp.service.activiti.IWorkflowService;
import com.yjp.erp.service.activiti.InvokeMoquiService;
import com.yjp.erp.service.gateway.GatewayService;
import com.yjp.erp.service.system.UserService;
import com.yjp.erp.util.FormatReturnEntity;
import com.yjp.erp.util.InvokeMoquiUtils;
import com.yjp.erp.util.TimeUtils;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetCode;
import com.yjp.erp.model.vo.activiti.BusinessDataVO;
import com.yjp.erp.model.vo.activiti.ProcessTaskVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @ClassName: InvokeMoquiServiceImpl
 * @Description: 调用moqui接口实现类
 * @author: jihongjin@yijiupi.com
 * @date: 2019年4月16日 下午4:31:15
 * @Copyright: 2019 www.yijiupi.com Inc. All rights reserved.
 */
@Service
public class InvokeMoquiServiceImpl implements InvokeMoquiService {

    private static final Logger log = LoggerFactory.getLogger(InvokeMoquiServiceImpl.class);

    @Autowired
    GatewayService gatewayService;

    @Autowired
    IBillBindingService billBindingServiceImpl;

    @Autowired
    IWorkflowService workflowServiceImpl;

    @Autowired
    IReviewerBindingService reviewerBindingServiceImpl;

    @Autowired
    private UserInfoManager userInfoManager;

    @Resource
    private UserService userService;

    @Resource
    private ModuleMapper moduleMapper;

    @Resource
    private BillService billService;

    @Resource
    private GatewayHandle gatewayHandle;

    @Value("${moqui.base.rest}")
    private String moquiBaseRest;

    @Value("${moqui.token.url}")
    private String moquiUrl;

    @SuppressWarnings("unchecked")
    @Override
    public JsonResult<JSONObject> createWorkflowInstance(WorkflowInstanceDTO workflowInstanceDTO) {
        JsonResult<JSONObject> jsonResult = new JsonResult<>();
        /**
         * 获取前台入参
         */
        String classId = workflowInstanceDTO.getClassId();
        String typeId = workflowInstanceDTO.getTypeId();
        String remarks = workflowInstanceDTO.getRemarks();
        String orgId = workflowInstanceDTO.getOrgId();
        Long billId = workflowInstanceDTO.getBillId();
        /**
         * 根据classId和typeId查询实体名称
         */
        Map<String, Object> entityMap = Maps.newConcurrentMap();
        try {
            entityMap = gatewayService.getEntityNames(classId, typeId);
        } catch (Exception e) {
            log.error("根据classId和typeId查询实体名称异常：" + e.getMessage());
        }
        /**
         * 单据实体名
         */
        String billEntityName = (String) entityMap.get("parentEntity");
        /**
         * 先根据classId和typeId查询工作流
         */
        Map<String, Object> columnMap = Maps.newConcurrentMap();
        columnMap.put("class_id", classId);
        columnMap.put("type_id", typeId);
        List<BillBinding> billBindings = billBindingServiceImpl.selectByMap(columnMap);
        if (billBindings == null || billBindings.size() == 0) {
            jsonResult.setCode(RetCode.FAIL);
            jsonResult.setMsg("根据classId和typeId查询不到工作流!");
            return jsonResult;
        }
        BillBinding billBinding = billBindings.get(0);
        if (Objects.isNull(billId)) {
            billId = billBinding.getId();
        }
        Long workflowId = billBinding.getWorkflowId();
        /**
         * 根据工作流id查询工作流标识符
         */
        Workflow workflow = workflowServiceImpl.selectById(workflowId);
        String processKey = workflow.getIdentifier();
        /**
         * 申请人（当前登录人id）
         */
        String applicant = userInfoManager.getUserInfo().getUserId();
        /**
         * 根据当前登录人所属组织查询审核组
         */
        EntityWrapper<ReviewerBinding> ew = new EntityWrapper<>();
        ReviewerBinding rb = new ReviewerBinding();
        rb.setOrgId(Long.valueOf(orgId));
        ew.orderDesc(Collections.singletonList("create_date"));
        List<ReviewerBinding> reviewerBindings = reviewerBindingServiceImpl.selectList(ew);
        if (reviewerBindings == null || reviewerBindings.size() == 0) {
            jsonResult.setCode(RetCode.FAIL);
            jsonResult.setMsg("根据组织id查询不到审核组!");
            return jsonResult;
        }
        ReviewerBinding reviewerBinding = reviewerBindings.get(0);
        String firstLevelReviewPersons = reviewerBinding.getFirstLevelGroup();
        String secondLevelReviewRersons = reviewerBinding.getSecondLevelGroup();
        String thirdLevelReviewRersons = reviewerBinding.getThirdLevelGroup();
        /**
         * 组装工作流实例
         */
        JSONObject instance = new JSONObject();
        // 工作流标识符
        instance.put("processKey", processKey);

        JSONObject reviewPersons = new JSONObject();
        reviewPersons.put("firstLevelReviewPersons", firstLevelReviewPersons);
        reviewPersons.put("secondLevelReviewPersons", secondLevelReviewRersons);
        reviewPersons.put("thirdLevelReviewPersons", thirdLevelReviewRersons);
        reviewPersons.put("applicant", applicant);
        reviewPersons.put("updateBillStatus", true);
        JSONObject jsonUpdateBillField = new JSONObject();
        //审核中
        Map<String, Object> underReviewMap = Maps.newConcurrentMap();
        underReviewMap.put("status", "1");
        jsonUpdateBillField.put("apply", underReviewMap);
        //完成
        Map<String, Object> completeMap = Maps.newConcurrentMap();
        completeMap.put("status", "0");
        jsonUpdateBillField.put("complete", completeMap);
        reviewPersons.put("updateBillFields", jsonUpdateBillField);
        // 审核人员
        instance.put("reviewPersons", reviewPersons);

        // 单据唯一标识
        instance.put("billId", String.valueOf(billId));

        JSONObject businessData = new JSONObject();
        businessData.put("classId", classId);
        businessData.put("typeId", typeId);
        businessData.put("billId", String.valueOf(billId));
        businessData.put("billEntityName", billEntityName);
        // 业务数据
        instance.put("businessData", businessData);
        instance.put("remarks", remarks);
        // 将封装JSONObject对象转为json字符串
        String paramStr = instance.toJSONString();
        try {
            // 远程调用moqui
            jsonResult = InvokeMoquiUtils.remoteCallMoqui(moquiBaseRest, "/act/createProcessByKey", paramStr);
        } catch (Exception e) {
            jsonResult.setMsg("远程调用moqui异常：" + e.getMessage());
            log.error("远程调用moqui异常：" + e.getMessage());
        }
        return jsonResult;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JsonResult<String> auditSubmit(BillAuditDTO billAuditDTO) {
        JsonResult<String> jsonResult = new JsonResult<>();
        billAuditDTO.setUserId(userInfoManager.getUserInfo().getUserId());
        billAuditDTO.setVariables(null);
        String params = JSON.toJSONString(billAuditDTO, SerializerFeature.WriteMapNullValue);
        try {
            jsonResult = InvokeMoquiUtils.remoteCallMoqui(moquiBaseRest, "/act/handleTask", params);
        } catch (Exception e) {
            jsonResult.setMsg("远程调用moqui审核提交异常：" + e.getMessage());
            log.error("远程调用moqui审核提交异常：" + e.getMessage());
        }
        return jsonResult;
    }

    @Override
    public JsonResult<Page<ProcessTaskVO>> getProcessedData(String invokeUrl, DoneAndUpComeDTO doneAndUpComeDTO) throws Exception {

        doneAndUpComeDTO.setUserId(userInfoManager.getUserInfo().getUserId());

        String paramStr = JSON.toJSONString(doneAndUpComeDTO);

        JsonResult<?> result = remoteCallMoqui(invokeUrl, paramStr);
        Object data = result.getData();
        JSONObject jsonData = JSONObject.parseObject(data.toString());
        String type = doneAndUpComeDTO.getType();
        JSONArray jsonCandidateArr = jsonData.getJSONArray(type);
        List<ProcessTaskVO> taskVOList = Lists.newArrayList();
        Set<Long> set = new HashSet<>();
        for (Object o : jsonCandidateArr) {
            JSONObject object = (JSONObject) o;
            ProcessTaskVO processTaskVO = JSON.parseObject(object.toString(), ProcessTaskVO.class);
            String processCreateTime = processTaskVO.getProcessCreateTime();
            String processCreator = processTaskVO.getProcessCreator();
            set.add(Long.valueOf(processCreator));
            processTaskVO.setProcessCreateTime(TimeUtils.long2DateStr(Long.valueOf(processCreateTime)));
            processTaskVO.setStatusName(WorkflowStatusEnum.getValueByKey(type));

            Module module = new Module();
            BusinessDataVO businessData = processTaskVO.getBusinessData();
            module.setTypeId(businessData.getTypeId());
            module.setClassId(businessData.getClassId());
            Module moduleData = moduleMapper.getModuleByClassIdAndTypeId(module);
            Bill billData = billService.getBillByModuleId(moduleData.getId());
            QueryDetailDTO queryDetailDTO = new QueryDetailDTO();
            queryDetailDTO.setClassId(businessData.getClassId());
            queryDetailDTO.setTypeId(businessData.getTypeId());
            queryDetailDTO.setId(Long.valueOf(businessData.getBillId()));
            Map detail = gatewayHandle.queryDetail(queryDetailDTO);
            businessData.setBillSummary(JSON.parseObject(JSON.toJSONString(detail)).getString("summary"));
            businessData.setBillDisplayName(billData.getLabel());
            taskVOList.add(processTaskVO);
        }

        List<Long> userIds = new ArrayList<>(set);
        Map<Long, User> userMap = userService.getUserMapByIds(userIds);
        FormatReturnEntity.formatProcessTaskList(taskVOList, userMap);

        // 按时间倒序
        Comparator<ProcessTaskVO> comparator = (ProcessTaskVO taskVO1, ProcessTaskVO taskVO2) -> taskVO1.getProcessCreateTime().compareTo(taskVO2.getProcessCreateTime());
        taskVOList.sort(comparator.reversed());

        // 排序后去重
        taskVOList = new ArrayList<>(taskVOList.stream().collect(Collectors.toMap(ProcessTaskVO::getProcessId, v -> v, (k1, k2) -> k2)).values());
        taskVOList.sort(comparator.reversed());

        int total = taskVOList.size();
        int pageNo = doneAndUpComeDTO.getPageNo();
        int pageSize = doneAndUpComeDTO.getPageSize();
        taskVOList = taskVOList.stream().skip((pageNo - 1) * pageSize).limit(pageSize).collect(Collectors.toList());

        Page<ProcessTaskVO> page = new Page<>(pageNo, pageSize);
        page.setRecords(taskVOList);
        page.setTotal(total);
        JsonResult<Page<ProcessTaskVO>> jsonResult = new JsonResult<>();
        jsonResult.setCode(RetCode.SUCCESS);
        jsonResult.setMsg(total == 0 ? "暂无数据" : "成功");
        jsonResult.setData(page);
        log.debug("网关请求返回:" + JSON.toJSONString(jsonResult));
        return jsonResult;
    }

    private JsonResult remoteCallMoqui(String reqPath, String paramStr) throws Exception {
        return InvokeMoquiUtils.remoteCallMoqui(moquiBaseRest, reqPath, paramStr);
    }
}
