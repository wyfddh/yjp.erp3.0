package com.yjp.erp.service.activiti.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yjp.erp.conf.UserInfoManager;
import com.yjp.erp.mapper.activiti.AuditConfigMapper;
import com.yjp.erp.model.dto.activiti.AuditConfigDTO;
import com.yjp.erp.model.po.activiti.AuditConfig;
import com.yjp.erp.model.po.activiti.Workflow;
import com.yjp.erp.model.po.system.Organization;
import com.yjp.erp.model.po.system.User;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.model.vo.ReturnDataVO;
import com.yjp.erp.model.vo.activiti.AuditConfigVO;
import com.yjp.erp.service.MultiThreadService;
import com.yjp.erp.service.activiti.IAuditConfigService;
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
 * 审核配置表 服务实现类
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-03
 */
@Service
public class AuditConfigServiceImpl extends ServiceImpl<AuditConfigMapper, AuditConfig> implements IAuditConfigService {

    private static final Logger log = LoggerFactory.getLogger(AuditConfigServiceImpl.class);

    private final MultiThreadService multiThreadService;

    private final AuditConfigMapper auditConfigMapper;

    private final UserInfoManager userInfoManager;

    @Autowired
    public AuditConfigServiceImpl(MultiThreadService multiThreadService, AuditConfigMapper auditConfigMapper, UserInfoManager userInfoManager) {
        this.multiThreadService = multiThreadService;
        this.auditConfigMapper = auditConfigMapper;
        this.userInfoManager = userInfoManager;
    }

    /**
     * @throws
     * @Title: updateStatus
     * @Description: 启用或禁用审核配置
     * @param: id 审核配置 id
     * @param: status 状态(1:启用 0:禁用)
     * @return: JsonResult<Boolean> true：成功 false：失败
     */
    @Override
    public JsonResult<Boolean> updateStatus(Long id, Integer status) {
        if (Objects.isNull(id)) {
            return RetResponse.makeErrRsp("审核配置id不能为空！");
        }
        AuditConfig entity = new AuditConfig();
        entity.setId(id);
        entity.setStatus(status);
        Integer count = auditConfigMapper.updateById(entity);
        return RetResponse.makeOKRsp(count > 0);
    }

    /**
     * @throws
     * @Title: insertAuditConfig
     * @Description: 新增审核配置
     * @param: auditConfigDTO 入参
     * @return: JsonResult<Boolean> true：成功	false：失败
     */
    @Override
    public JsonResult<Boolean> insertAuditConfig(AuditConfigDTO auditConfigDTO) {
        AuditConfig auditConfig = this.initAuditConfig(auditConfigDTO);
        boolean flag = this.insert(auditConfig);
        return RetResponse.makeOKRsp(flag);
    }

    /**
     * @throws
     * @Title: updateAuditConfig
     * @Description: 更新审核配置
     * @param: auditConfigDTO 入参
     * @return: JsonResult<Boolean> true：成功	false：失败
     */
    @Override
    public JsonResult<Boolean> updateAuditConfig(AuditConfigDTO auditConfigDTO) {
        AuditConfig auditConfig = this.initAuditConfig(auditConfigDTO);
        boolean flag = this.updateById(auditConfig);
        return RetResponse.makeOKRsp(flag);
    }

    /**
     * @throws
     * @Title: deleteAuditConfig
     * @Description: 删除审核配置
     * @param: id 入参
     * @return: JsonResult<Boolean> true：成功	false：失败
     */
    @Override
    public JsonResult<Boolean> deleteAuditConfig(Long id) {
        if (Objects.isNull(id)) {
            return RetResponse.makeErrRsp("审核配置id不能为空！");
        }
        boolean flag = this.deleteById(id);
        return RetResponse.makeOKRsp(flag);
    }

    /**
     * @throws
     * @Title: listAuditConfigPage
     * @Description: 分页查询审核配置
     * @param: auditConfigDTO 入参
     * @return: JsonResult<Page   <   AuditConfigVO>>
     */
    @Override
    public JsonResult<Page<AuditConfigVO>> listAuditConfigPage(AuditConfigDTO auditConfigDTO) {
        Page<AuditConfig> page = new Page<>();
        if (!Objects.isNull(auditConfigDTO.getPageNo())) {
            page.setCurrent(auditConfigDTO.getPageNo());
        }
        if (!Objects.isNull(auditConfigDTO.getPageSize())) {
            page.setSize(auditConfigDTO.getPageSize());
        }
        EntityWrapper<AuditConfig> ew = new EntityWrapper<>();
        AuditConfig auditConfig = auditConfigDTO.toPo();

        String userId = userInfoManager.getUserInfo().getUserId();
        userId = StringUtils.isBlank(userId) ? auditConfigDTO.getUserId() : userId;
        if (StringUtils.isNotBlank(userId)) {
            auditConfig.setCreator(Long.valueOf(userId));
        }
        ew.setEntity(auditConfig);
        ew.orderDesc(Collections.singletonList("create_date"));
        Page<AuditConfig> pageList = this.selectPage(page, ew);
        List<AuditConfig> list = pageList.getRecords();
        List<Long> orgIds = list.stream().map(AuditConfig::getOrgId).distinct().collect(Collectors.toList());
        List<Long> workflowIds = list.stream().map(AuditConfig::getWorkflowId).distinct().collect(Collectors.toList());

        //用户集合
        List<Long> userIds = this.initUserIds(list);

        ReturnDataVO returnDataVO = multiThreadService.getBaseDataMap(workflowIds, userIds, orgIds);
        Map<Long, Organization> areaMap = returnDataVO.getOrgMap();
        Map<Long, User> userMap = returnDataVO.getUserMap();
        Map<Long, Workflow> workflowMap = returnDataVO.getWorkflowMap();
        Page<AuditConfigVO> formatAuditConfigPageList = FormatReturnEntity.formatAuditConfigPageList(pageList, areaMap, workflowMap, userMap);
        return RetResponse.makeOKRsp(formatAuditConfigPageList);
    }

    /**
     * 构建查询用的用户集合
     *
     * @param list
     * @return
     */
    private List<Long> initUserIds(List<AuditConfig> list) {
        //创建人
        List<Long> userIds = list.stream().map(AuditConfig::getCreator).distinct().collect(Collectors.toList());
        List<String> oneLevels = list.stream().map(AuditConfig::getFirstLevelGroup).distinct().collect(Collectors.toList());
        this.addGroupUserIds(userIds, oneLevels);
        List<String> twoLevels = list.stream().map(AuditConfig::getSecondLevelGroup).distinct().collect(Collectors.toList());
        this.addGroupUserIds(userIds, twoLevels);
        List<String> threeLevels = list.stream().map(AuditConfig::getThirdLevelGroup).distinct().collect(Collectors.toList());
        this.addGroupUserIds(userIds, threeLevels);
        return userIds.stream().distinct().collect(Collectors.toList());
    }

    /**
     * @throws
     * @Title: viewAuditConfig
     * @Description: 查看审核配置
     * @param: id 主键
     * @return: JsonResult<AuditConfigVO>
     */
    @Override
    public JsonResult<AuditConfigVO> viewAuditConfig(Long id) {
        if (Objects.isNull(id)) {
            return RetResponse.makeErrRsp("审核配置id不能为空！");
        }
        AuditConfig auditConfig = this.selectById(id);
        //组织id
        Long orgId = auditConfig.getOrgId();
        //工作流id
        Long workflowId = auditConfig.getWorkflowId();
        //用户id
        Long creator = auditConfig.getCreator();
        List<Long> userIds = new ArrayList<Long>();
        userIds.add(creator);
        String first = auditConfig.getFirstLevelGroup();
        this.addGroupUserIds(userIds, Collections.singletonList(first));
        String second = auditConfig.getSecondLevelGroup();
        this.addGroupUserIds(userIds, Collections.singletonList(second));
        String third = auditConfig.getThirdLevelGroup();
        this.addGroupUserIds(userIds, Collections.singletonList(third));
        //用户id集合
        userIds = userIds.stream().distinct().collect(Collectors.toList());
        ReturnDataVO returnDataVO = multiThreadService.getBaseDataMap(Collections.singletonList(workflowId), userIds, Collections.singletonList(orgId));
        Map<Long, User> userMap = returnDataVO.getUserMap();
        Map<Long, Organization> areaMap = returnDataVO.getOrgMap();
        Map<Long, Workflow> workflowMap = returnDataVO.getWorkflowMap();
        AuditConfigVO formatAuditConfig = FormatReturnEntity.formatAuditConfig(auditConfig, areaMap, workflowMap, userMap);
        return RetResponse.makeOKRsp(formatAuditConfig);
    }

    /**
     * 审核组用户添加到UserIds中
     *
     * @param userIds
     * @param groups
     */
    private void addGroupUserIds(List<Long> userIds, List<String> groups) {
        for (String levelThree : groups) {
            if (StringUtils.isNotBlank(levelThree)) {
                String[] threeArr = levelThree.split(",");
                for (String v : threeArr) {
                    if (StringUtils.isNotBlank(v)) {
                        userIds.add(Long.valueOf(v));
                    }
                }
            }
        }
    }

    /**
     * 对象转换成后台操作的审核配置对象
     *
     * @param auditConfigDTO
     * @return
     */
    private AuditConfig initAuditConfig(AuditConfigDTO auditConfigDTO) {
        AuditConfig auditConfig = auditConfigDTO.toPo();
        auditConfig.setId(SnowflakeIdWorker.nextId());
        // 这里取当前登录人id
        String userId = userInfoManager.getUserInfo().getUserId();
        auditConfig.setCreator(Long.valueOf(userId));
        auditConfig.setCreateDate(new Date());
        String[] firstLevelGroupArr = auditConfigDTO.getFirstLevelGroup();
        auditConfig.setFirstLevelGroup(StringUtils.join(firstLevelGroupArr, ","));
        String[] secondLevelGroupArr = auditConfigDTO.getSecondLevelGroup();
        auditConfig.setSecondLevelGroup(StringUtils.join(secondLevelGroupArr, ","));
        String[] thirdLevelGroupArr = auditConfigDTO.getThirdLevelGroup();
        auditConfig.setThirdLevelGroup(StringUtils.join(thirdLevelGroupArr, ","));
        auditConfig.setOrgId(auditConfigDTO.getOrgId());
        return auditConfig;
    }
}
