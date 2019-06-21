package com.yjp.erp.service.activiti.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yjp.erp.conf.UserInfo;
import com.yjp.erp.conf.UserInfoManager;
import com.yjp.erp.mapper.activiti.BillBindingMapper;
import com.yjp.erp.model.dto.activiti.BillBindingDTO;
import com.yjp.erp.model.po.activiti.BillBinding;
import com.yjp.erp.model.po.activiti.Workflow;
import com.yjp.erp.model.po.system.User;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetCode;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.model.vo.ReturnDataVO;
import com.yjp.erp.model.vo.activiti.BillBindingVO;
import com.yjp.erp.service.MultiThreadService;
import com.yjp.erp.service.activiti.IBillBindingService;
import com.yjp.erp.service.activiti.IWorkflowService;
import com.yjp.erp.util.FormatReturnEntity;
import com.yjp.erp.util.SnowflakeIdWorker;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 单据绑定表 服务实现类
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-04
 */
@Service
public class BillBindingServiceImpl extends ServiceImpl<BillBindingMapper, BillBinding> implements IBillBindingService {

    private static final Logger log = LoggerFactory.getLogger(BillBindingServiceImpl.class);

    @Autowired
    MultiThreadService multiThreadService;

    @Autowired
    IWorkflowService workflowServiceImpl;

    @Autowired
    private UserInfoManager userInfoManager;

    /**
     * @throws
     * @Title: insertBillBinding
     * @Description: 新增保存单据绑定
     * @param: billBindingDTO 入参
     * @return: JsonResult<Boolean> true：成功 false：失败
     */
    @Override
    public JsonResult<Boolean> insertBillBinding(BillBindingDTO billBindingDTO) {
        JsonResult<Boolean> jsonResult = new JsonResult<>();
        jsonResult.setCode(RetCode.FAIL);
        jsonResult.setData(false);
        Long workflowId = billBindingDTO.getWorkflowId();
        BillBinding billBinding = billBindingDTO.toPo();
        if (StringUtils.isBlank(billBindingDTO.getName())) {
            // 名称如果没有填，自动生成，格式：单据-工作流名称
            // 根据工作流id查询工作流名称
            Workflow workflow = workflowServiceImpl.selectById(workflowId);
            // 根据单据id获取单据名称
            billBinding.setName(billBindingDTO.getTypeName() + "-" + workflow.getName());
        }
        billBinding.setId(SnowflakeIdWorker.nextId());
        billBinding.setCreateDate(new Date());
        // 当前登录人
        UserInfo userInfo = userInfoManager.getUserInfo();
        billBinding.setCreator(Long.valueOf(userInfo.getUserId()));
        boolean flag = this.insert(billBinding);
        if (flag) {
            jsonResult.setCode(RetCode.SUCCESS);
            jsonResult.setMsg("新增保存单据绑定成功！");
        } else {
            jsonResult.setMsg("新增保存单据绑定失败！");
        }
        jsonResult.setData(flag);
        return jsonResult;
    }

    /**
     * @throws
     * @Title: updateBillBinding
     * @Description: 编辑保存单据绑定
     * @param: billBindingDTO 入参
     * @return: JsonResult<Boolean> true：成功 false：失败
     */
    @Override
    public JsonResult<Boolean> updateBillBinding(BillBindingDTO billBindingDTO) {
        JsonResult<Boolean> jsonResult = new JsonResult<>();
        jsonResult.setCode(RetCode.FAIL);
        jsonResult.setData(false);
        BillBinding billBinding = billBindingDTO.toPo();
        billBinding.setCreateDate(new Date());
        // 当前登录人
        String userId = userInfoManager.getUserInfo().getUserId();
        billBinding.setCreator(Long.valueOf(userId));// 当前登录人
        boolean flag = this.updateById(billBinding);
        if (flag) {
            jsonResult.setCode(RetCode.SUCCESS);
            jsonResult.setMsg("编辑保存单据绑定成功！");
        } else {
            jsonResult.setMsg("编辑保存单据绑定失败！");
        }
        jsonResult.setData(flag);
        return jsonResult;
    }

    /**
     * @throws
     * @Title: deleteBillBinding
     * @Description: 删除单据绑定
     * @param: id 主键
     * @return: JsonResult<Boolean> true：成功 false：失败
     */
    @Override
    public JsonResult<Boolean> deleteBillBinding(Long id) {
        JsonResult<Boolean> jsonResult = new JsonResult<>();
        jsonResult.setCode(RetCode.FAIL);
        jsonResult.setData(false);
        boolean flag = this.deleteById(id);
        if (flag) {
            jsonResult.setCode(RetCode.SUCCESS);
            jsonResult.setMsg("删除单据绑定成功！");
        } else {
            jsonResult.setMsg("删除单据绑定失败！");
        }
        jsonResult.setData(flag);
        return jsonResult;
    }

    /**
     * @throws
     * @Title: listBillBindingPage
     * @Description: 分页查询单据绑定
     * @param: billBindingDTO 入参
     * @return: JsonResult<Page < BillBindingVO>>
     */
    @Override
    public JsonResult<Page<BillBindingVO>> listBillBindingPage(BillBindingDTO billBindingDTO) {
        Page<BillBinding> page = new Page<>();
        page.setCurrent(billBindingDTO.getPageNo());
        page.setSize(billBindingDTO.getPageSize());
        EntityWrapper<BillBinding> ew = new EntityWrapper<>();
        BillBinding billBinding = billBindingDTO.toPo();
        ew.setEntity(billBinding);
        // 按创建时间倒序
        ew.orderDesc(Collections.singletonList("create_date"));
        Page<BillBinding> pageList = this.selectPage(page, ew);
        List<BillBinding> records = pageList.getRecords();
        List<Long> creators = records.stream().map(BillBinding::getCreator).distinct().collect(Collectors.toList());
        List<Long> workflowIds = records.stream().map(BillBinding::getWorkflowId).distinct().collect(Collectors.toList());
        ReturnDataVO returnDataVO = multiThreadService.getBaseDataMap(workflowIds, creators, null);
        Map<Long, User> userMap = returnDataVO.getUserMap();
        Map<Long, Workflow> workflowMap = returnDataVO.getWorkflowMap();
        Page<BillBindingVO> formatBillBindingPageList = FormatReturnEntity.formatBillBindingPageList(pageList, workflowMap, userMap);
        return RetResponse.makeOKRsp(formatBillBindingPageList);
    }

    /**
     * @throws
     * @Title: viewBillBinding
     * @Description: 查看单据绑定
     * @param: id 主键
     * @return: JsonResult<BillBindingVO>
     */
    @Override
    public JsonResult<BillBindingVO> viewBillBinding(Long id) {
        BillBinding billBinding = this.selectById(id);
        Long workflowId = billBinding.getWorkflowId();
        Long creator = billBinding.getCreator();
        ReturnDataVO returnDataVO = multiThreadService.getBaseDataMap(Arrays.asList(workflowId), Arrays.asList(creator), null);
        Map<Long, User> userMap = returnDataVO.getUserMap();
        Map<Long, Workflow> workflowMap = returnDataVO.getWorkflowMap();
        BillBindingVO formatBillBinding = FormatReturnEntity.formatBillBinding(billBinding, workflowMap, userMap);
        return RetResponse.makeOKRsp(formatBillBinding);
    }

    /**
     * @throws
     * @Title: listBillBindings
     * @Description: 查询所有单据绑定
     * @return: JsonResult<List < BillBindingVO>>
     */
    @Override
    public JsonResult<List<BillBindingVO>> listBillBindings() {
        EntityWrapper<BillBinding> ew = new EntityWrapper<>();
        // 按创建时间倒序
        ew.orderDesc(Arrays.asList("create_date"));
        List<BillBinding> list = this.selectList(ew);
        List<Long> creators = list.stream().map(BillBinding::getCreator).distinct().collect(Collectors.toList());
        List<Long> workflowIds = list.stream().map(BillBinding::getWorkflowId).distinct().collect(Collectors.toList());
        ReturnDataVO returnDataVO = multiThreadService.getBaseDataMap(workflowIds, creators, null);
        Map<Long, User> userMap = returnDataVO.getUserMap();
        Map<Long, Workflow> workflowMap = returnDataVO.getWorkflowMap();
        List<BillBindingVO> formatBillBindingList = FormatReturnEntity.formatBillBindingList(list, workflowMap, userMap);
        return RetResponse.makeOKRsp(formatBillBindingList);
    }
}
