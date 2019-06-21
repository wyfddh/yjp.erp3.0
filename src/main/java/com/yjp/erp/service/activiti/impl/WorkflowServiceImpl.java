package com.yjp.erp.service.activiti.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yjp.erp.conf.UserInfoManager;
import com.yjp.erp.mapper.activiti.WorkflowMapper;
import com.yjp.erp.model.dto.activiti.WorkflowDTO;
import com.yjp.erp.model.po.activiti.Workflow;
import com.yjp.erp.model.po.system.User;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.model.vo.activiti.WorkflowVO;
import com.yjp.erp.service.activiti.IWorkflowService;
import com.yjp.erp.service.system.UserService;
import com.yjp.erp.util.FormatReturnEntity;
import com.yjp.erp.util.SnowflakeIdWorker;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 模版表 服务实现类
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-03
 */
@Service
public class WorkflowServiceImpl extends ServiceImpl<WorkflowMapper, Workflow> implements IWorkflowService {

    private static final Logger log = LoggerFactory.getLogger(WorkflowServiceImpl.class);

    private final WorkflowMapper workflowMapper;

    private final UserService userService;

    private final UserInfoManager userInfoManager;

    @Autowired
    public WorkflowServiceImpl(WorkflowMapper workflowMapper, UserService userService, UserInfoManager userInfoManager) {
        this.workflowMapper = workflowMapper;
        this.userService = userService;
        this.userInfoManager = userInfoManager;
    }

    @Override
    public Map<Long, Workflow> getWorkflowMapByIds(List<Long> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            return workflowMapper.getWorkflowMapByIds(ids);
        }
        return new HashMap<>();
    }

    @Override
    public Boolean insertWorkflow(WorkflowDTO workflowDTO) {
        Workflow workflow = workflowDTO.toPo();
        workflow.setId(SnowflakeIdWorker.nextId());
        workflow.setCreateDate(new Date());
        workflow.setCreator(Long.valueOf(userInfoManager.getUserInfo().getUserId()));
        return this.insert(workflow);
    }

    @Override
    public Boolean updateWorkflow(WorkflowDTO workflowDTO) {
        Workflow workflow = workflowDTO.toPo();
        workflow.setCreateDate(new Date());
        workflow.setCreator(Long.valueOf(userInfoManager.getUserInfo().getUserId()));
        return this.updateById(workflow);
    }

    @Override
    public Boolean deleteWorkflow(Long id) {
        return this.deleteById(id);
    }

    @Override
    public JsonResult<Page<WorkflowVO>> listWorkflowPage(WorkflowDTO workflowDTO) {
        Page<Workflow> page = new Page<>();
        page.setCurrent(workflowDTO.getPageNo());
        page.setSize(workflowDTO.getPageSize());
        EntityWrapper<Workflow> ew = new EntityWrapper<>();
        Workflow workflow = workflowDTO.toPo();
        ew.setEntity(workflow);
        ew.orderDesc(Collections.singletonList("create_date"));
        Page<Workflow> pageList = this.selectPage(page, ew);
        List<Workflow> list = pageList.getRecords();
        List<Long> ids = list.stream().map(Workflow::getCreator).distinct().collect(Collectors.toList());
        Map<Long, User> userMap = getLongUserMap(ids);
        Page<WorkflowVO> formatWorkflowPageList = FormatReturnEntity.formatWorkflowPageList(pageList, userMap);
        return RetResponse.makeOKRsp(formatWorkflowPageList);
    }

    @Override
    public JsonResult<WorkflowVO> viewWorkflow(Long id) {
        Workflow workflow = this.selectById(id);
        List<Long> ids = Collections.singletonList(workflow.getCreator());
        Map<Long, User> userMap = getLongUserMap(ids);
        return RetResponse.makeOKRsp(FormatReturnEntity.formatWorkflow(workflow, userMap));
    }

    @Override
    public JsonResult<List<WorkflowVO>> listAllWorkflow(WorkflowDTO workflowDTO) {
        EntityWrapper<Workflow> ew = new EntityWrapper<>();
        Workflow workflow = workflowDTO.toPo();
        ew.setEntity(workflow);
        ew.orderDesc(Collections.singletonList("create_date"));
        List<Workflow> list = this.selectList(ew);
        list = new ArrayList<>(list.stream().collect(Collectors.toMap(Workflow::getIdentifier, v -> v, (k1, k2) -> k1)).values());
        List<Long> ids = list.stream().map(Workflow::getCreator).distinct().collect(Collectors.toList());
        Map<Long, User> userMap = getLongUserMap(ids);
        List<WorkflowVO> formatWorkflowList = FormatReturnEntity.formatWorkflowList(list, userMap);
        return RetResponse.makeOKRsp(formatWorkflowList);
    }

    private Map<Long, User> getLongUserMap(List<Long> ids) {
        Map<Long, User> userMap;
        try {
            userMap = userService.getUserMapByIds(ids);
        } catch (Exception e) {
            throw new RuntimeException("根据用户id集合获取用户信息异常：" + e.getMessage());
        }
        return userMap;
    }
}
