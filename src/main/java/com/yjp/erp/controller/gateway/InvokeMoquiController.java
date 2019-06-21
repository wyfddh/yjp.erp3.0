package com.yjp.erp.controller.gateway;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.yjp.erp.conf.UserInfo;
import com.yjp.erp.conf.UserInfoManager;
import com.yjp.erp.model.dto.activiti.*;
import com.yjp.erp.model.dto.gateway.BillAuditDTO;
import com.yjp.erp.model.po.system.User;
import com.yjp.erp.service.activiti.IBillBindingService;
import com.yjp.erp.service.activiti.IReviewerBindingService;
import com.yjp.erp.service.activiti.IWorkflowService;
import com.yjp.erp.service.activiti.InvokeMoquiService;
import com.yjp.erp.service.gateway.GatewayService;
import com.yjp.erp.service.system.UserService;
import com.yjp.erp.util.*;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetCode;
import com.yjp.erp.model.vo.activiti.ProcessHistoryVO;
import com.yjp.erp.model.vo.activiti.ProcessTaskVO;
import com.yjp.erp.model.vo.activiti.TaskDetailVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: InvokeMoquiController
 * @Description: 网关调用moqui
 * @author: jihongjin@yijiupi.com
 * @date: 2019年4月9日 下午7:55:50
 * @Copyright: 2019 www.yijiupi.com Inc. All rights reserved.
 */
@RestController
@RequestMapping({"/config/gateway", "/apps/gateway"})
public class InvokeMoquiController {

    private static final Logger log = LoggerFactory.getLogger(InvokeMoquiController.class);

    @Resource
    private GatewayService gatewayService;

    @Resource
    private IBillBindingService billBindingServiceImpl;

    @Resource
    private IWorkflowService workflowServiceImpl;

    @Resource
    private IReviewerBindingService reviewerBindingServiceImpl;

    @Resource
    private UserInfoManager userInfoManager;

    @Value("${moqui.base.rest}")
    private String moquiBaseRest;

    @Resource
    private InvokeMoquiService invokeMoquiServiceImpl;

    @Resource
    private UserService userService;

    /**
     * @throws Exception
     * @throws Exception
     * @Title: createWorkflow
     * @Description: 创建工作流实例
     * @param: workflowInstanceDTO 入参
     * @return: JsonResult
     */
    @PostMapping("/createWorkflow")
    public JsonResult<JSONObject> createWorkflowInstance(@RequestBody WorkflowInstanceDTO workflowInstanceDTO, HttpSession session) throws Exception {
        UserInfo userInfo = userInfoManager.getUserInfo();
        /**
         * 从session获取当前登录人所属组织
         */
        String orgId = userInfo.getOrgList().get(0).toString();
        workflowInstanceDTO.setOrgId(orgId);
        return invokeMoquiServiceImpl.createWorkflowInstance(workflowInstanceDTO);
    }

    /**
     * @throws Exception
     * @Title: receiveTask
     * @Description: 认领当前待审核Task
     * @param: receiveTaskDTO 入参
     * @return: JsonResult
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/receiveTask")
    public JsonResult<String> receiveTask(@RequestBody ReceiveTaskDTO receiveTaskDTO) throws Exception {
        receiveTaskDTO.setUserId(userInfoManager.getUserInfo().getUserId());
        /**
         * 将封装对象转为json字符串
         */
        String paramStr = JSON.toJSONString(receiveTaskDTO);
        /**
         * 远程调用moqui
         */
        return remoteCallMoqui("/act/receiveTask", paramStr);
    }

    /**
     * 获取工作台单据详情中的处理按钮的权限
     *
     * @param taskId 任务Id
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    @GetMapping("/getBillOperationalPermission/{taskId}")
    public JsonResult getProcessCanAccessButton(@PathVariable("taskId") String taskId) throws Exception {
        String res = HttpClientUtils.get(moquiBaseRest + "/act/getOperationalPermission?userId=" + userInfoManager.getUserInfo().getUserId() + "&taskId=" + taskId);
        return ObjectUtils.isNotEmpty(JSON.parseObject(res, JsonResult.class)) ? JSON.parseObject(res, JsonResult.class) : new JsonResult();
    }

    /**
     * 提交单据
     *
     * @param billAuditDTO 入参
     * @throws Exception
     */
    @PostMapping("/auditSubmit")
    public JsonResult<String> auditSubmit(@RequestBody BillAuditDTO billAuditDTO) throws Exception {
        return invokeMoquiServiceImpl.auditSubmit(billAuditDTO);
    }

    /**
     * 单据审核通过
     *
     * @param billAuditDTO 入参
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/auditPass")
    public JsonResult<String> auditPass(@RequestBody BillAuditDTO billAuditDTO) throws Exception {
        billAuditDTO.setUserId(userInfoManager.getUserInfo().getUserId());
        billAuditDTO.setVariables(true);
        String params = JSON.toJSONString(billAuditDTO);
        return remoteCallMoqui("/act/handleTask", params);
    }

    /**
     * 单据审核拒绝
     *
     * @param billAuditDTO 入参
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/auditNotPass")
    public JsonResult<String> auditNotPass(@RequestBody BillAuditDTO billAuditDTO) throws Exception {
        billAuditDTO.setUserId(userInfoManager.getUserInfo().getUserId());
        billAuditDTO.setVariables(false);
        String params = JSON.toJSONString(billAuditDTO);
        return remoteCallMoqui("/act/handleTask", params);
    }

    /**
     * @throws Exception
     * @Title: getProcessHistory
     * @Description: 查看工作流实例的审核明细
     * @param: processHistoryDTO 入参
     * @return: JsonResult
     */
    @PostMapping("/getProcessHistory")
    public JsonResult<ProcessHistoryVO> getProcessHistory(@RequestBody ProcessHistoryDTO processHistoryDTO) throws Exception {
        processHistoryDTO.setUserId(userInfoManager.getUserInfo().getUserId());
        /**
         * 将封装对象转为json字符串
         */
        String paramStr = JSON.toJSONString(processHistoryDTO);
        /**
         * 远程调用moqui
         */
        JsonResult<?> result = remoteCallMoqui("/act/getBillProcessHistory", paramStr);
        Object data = result.getData();
        ProcessHistoryVO history = JSONObject.parseObject(data.toString(), ProcessHistoryVO.class);
        JSONObject jsonData = JSONObject.parseObject(data.toString());

        JSONArray taskDetailArray = jsonData.getJSONArray("taskDetail");
        List<TaskDetailVO> taskDetails = Lists.newArrayList();
        Set<Long> set = new HashSet<>();
        for (int k = 0, len = taskDetailArray.size(); k < len; k++) {
            JSONObject jsonObject = (JSONObject) taskDetailArray.get(k);
            String submitter = jsonObject.getString("reviewer");
            set.add(Long.valueOf(submitter));
            String remark = jsonObject.getString("reviewRemark");
            Long processTimeStr = jsonObject.getLong("endDate");
            String processTime = "";
            if (processTimeStr != null) {
                processTime = TimeUtils.long2DateStr(processTimeStr);
            }
            TaskDetailVO detail = new TaskDetailVO();
            detail.setProcessTime(StringUtils.isBlank(processTime) ? "" : processTime);
            detail.setRemark(StringUtils.isBlank(remark) ? "" : remark);
            detail.setSubmitter(StringUtils.isBlank(submitter) ? "" : submitter);
            taskDetails.add(detail);
        }

        List<Long> userIds = set.stream().collect(Collectors.toList());
        Map<Long, User> userMap = userService.getUserMapByIds(userIds);
        FormatReturnEntity.formatTaskDetailList(taskDetails, userMap);

        //按时间倒序
        Comparator<TaskDetailVO> comparator = (TaskDetailVO detail1, TaskDetailVO detail2) -> detail1.getProcessTime().compareTo(detail2.getProcessTime());
        taskDetails.sort(comparator.reversed());

        history.setTaskDetails(taskDetails);

        JsonResult<ProcessHistoryVO> jsonResult = new JsonResult<>();
        jsonResult.setCode(RetCode.SUCCESS);
        jsonResult.setMsg("成功");
        jsonResult.setData(history);
        log.debug("网关请求返回:" + JSON.toJSONString(jsonResult));
        return jsonResult;
    }

    /**
     * @throws Exception
     * @Title: transferTask
     * @Description: 转交审核(无查看明细权)
     * @param: param 入参
     * @return: JsonResult
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/transferTask")
    public JsonResult transferTask(@RequestBody TransferTaskDTO transferTaskDTO) throws Exception {
        /**
         * 将封装对象转为json字符串
         */
        String paramStr = JSON.toJSONString(transferTaskDTO);
        /**
         * 远程调用moqui
         */
        return remoteCallMoqui("/act/transferTask", paramStr);
    }

    /**
     * @throws Exception
     * @Title: transferTaskAndPermission
     * @Description: 转交审核(有查看明细权)
     * @param: param 入参
     * @return: JsonResult
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/transferTaskAndPermission")
    public JsonResult<String> transferTaskAndPermission(@RequestBody TransferTaskDTO transferTaskDTO) throws Exception {
        /**
         * 将封装对象转为json字符串
         */
        String paramStr = JSON.toJSONString(transferTaskDTO);
        /**
         * 远程调用moqui
         */
        return remoteCallMoqui("/act/transferTaskAndPermission", paramStr);
    }

    /**
     * @throws Exception
     * @throws
     * @Title: Pending
     * @Description: 待办--待领取
     * @param: doneAndUpComeDTO 入参
     * @return: JsonResult<?>
     */
    @PostMapping("/pending")
    public JsonResult<Page<ProcessTaskVO>> pending(@RequestBody DoneAndUpComeDTO doneAndUpComeDTO) throws Exception {
        doneAndUpComeDTO.setType("candidate");
        return invokeMoquiServiceImpl.getProcessedData("/act/getWaitProcessTask", doneAndUpComeDTO);
    }

    /**
     * @throws Exception
     * @throws
     * @Title: Pending
     * @Description: 待办--待处理
     * @param: doneAndUpComeDTO 入参
     * @return: JsonResult<?>
     */
    @PostMapping("/processed")
    public JsonResult<Page<ProcessTaskVO>> processed(@RequestBody DoneAndUpComeDTO doneAndUpComeDTO) throws Exception {
        doneAndUpComeDTO.setType("assignee");
        return invokeMoquiServiceImpl.getProcessedData("/act/getWaitProcessTask", doneAndUpComeDTO);
    }

    /**
     * @throws Exception
     * @throws
     * @Title: Pending
     * @Description: 已办--已完成
     * @param: doneAndUpComeDTO 入参
     * @return: JsonResult<?>
     */
    @PostMapping("/completed")
    public JsonResult<Page<ProcessTaskVO>> completed(@RequestBody DoneAndUpComeDTO doneAndUpComeDTO) throws Exception {
        doneAndUpComeDTO.setType("processed");
        return invokeMoquiServiceImpl.getProcessedData("/act/getProcessedTask", doneAndUpComeDTO);
    }

    /**
     * @throws Exception
     * @throws
     * @Title: Pending
     * @Description: 已办--未完成
     * @param: doneAndUpComeDTO 入参
     * @return: JsonResult<?>
     */
    @PostMapping("/undone")
    public JsonResult<Page<ProcessTaskVO>> undone(@RequestBody DoneAndUpComeDTO doneAndUpComeDTO) throws Exception {
        doneAndUpComeDTO.setType("unprocessed");
        return invokeMoquiServiceImpl.getProcessedData("/act/getProcessedTask", doneAndUpComeDTO);
    }

    /**
     * @throws
     * @Title: getProcessedData
     * @Description: 获取数据（type:[candidate-待领取，assignee-待处理，processed-已完成，unprocessed-未完成]）
     * @param: doneAndUpComeDTO 入参
     * @param: @throws Exception
     * @return: JsonResult<Page                                                               <                                                               WaitProcessTaskVO>>
     */
   /* private JsonResult<Page<ProcessTaskVO>> getProcessedData(String invokeUrl, DoneAndUpComeDTO doneAndUpComeDTO) throws Exception {

        doneAndUpComeDTO.setUserId(userInfoManager.getUserInfo().getUserId());

        String paramStr = JSON.toJSONString(doneAndUpComeDTO);

        JsonResult<?> result = remoteCallMoqui(invokeUrl, paramStr);
        System.err.println("=================" + JSON.toJSONString(result));
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
            taskVOList.add(processTaskVO);
        }

        *//*List<Long> userIds = set.stream().collect(Collectors.toList());*//*
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
    }*/

    /**
     * @throws
     * @Title: remoteCallMoqui
     * @Description: 远程调用moqui
     * @param: reqPath 请求路径
     * @param: paramStr 入参（json字符串）
     * @param: @throws Exception
     * @return: JsonResult
     */
    @SuppressWarnings("rawtypes")
    private JsonResult remoteCallMoqui(String reqPath, String paramStr) throws Exception {
        return InvokeMoquiUtils.remoteCallMoqui(moquiBaseRest, reqPath, paramStr);
    }

}
