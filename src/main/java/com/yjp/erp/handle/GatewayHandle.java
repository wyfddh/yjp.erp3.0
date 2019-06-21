package com.yjp.erp.handle;

import com.alibaba.fastjson.JSON;
import com.yjp.erp.conf.UserInfoManager;
import com.yjp.erp.constants.AuditStatusEnum;
import com.yjp.erp.constants.EntityClassEnum;
import com.yjp.erp.constants.RequestEnum;
import com.yjp.erp.model.domain.FieldRuleDO;
import com.yjp.erp.model.domain.MoquiListRetDO;
import com.yjp.erp.model.dto.activiti.WorkflowInstanceDTO;
import com.yjp.erp.model.dto.bill.BillAttachmentDTO;
import com.yjp.erp.model.dto.gateway.*;
import com.yjp.erp.conf.exception.BusinessException;
import com.yjp.erp.model.po.system.Organization;
import com.yjp.erp.service.activiti.InvokeMoquiService;
import com.yjp.erp.service.gateway.GatewayService;
import com.yjp.erp.service.moqui.attachment.AttachmentService;
import com.yjp.erp.service.system.OrgService;
import com.yjp.erp.util.HttpClientUtils;
import com.yjp.erp.util.ObjectUtils;
import com.yjp.erp.model.vo.RetCode;
import com.yjp.erp.model.vo.activiti.WorkflowInstanceVO;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * description:
 *
 * @author liushui
 * @date 2019/3/29
 */
@Component
@Slf4j
public class GatewayHandle {

    /**
     * 匹配时间的正则
     */
    private static final String PATTERN = "(/date\\()(.*)(\\)/)";

    @Value("${moqui.token.url}")
    private String moquiUrl;

    @Value("${moqui.detail.url}")
    private String commonDetailUrl;

    @Value("${moqui.ref.url}")
    private String refUrl;

    @Value("${moqui.filter.url}")
    private String filterUrl;

    @Value("${moqui.refList.url}")
    private String refListUrl;

    @Value("${moqui.server}")
    private String server;

    @Autowired
    private GatewayService gatewayService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private InvokeMoquiService invokeMoquiServiceImpl;

    @Autowired
    private UserInfoManager userInfoManager;

    @Autowired
    private OrgService orgService;

    /**
     * 转发请求
     */
    public String transportReq(GatewayDTO dto) throws Exception {

        if (log.isDebugEnabled()) {
            log.debug(String.format("transport param are { %s }", dto.toString()));
        }
        String response ;
        Map<String, Object> paramMap = dto.getData();

        switch (dto.getMethod().toUpperCase()) {
            case "GET": {
                response = HttpClientUtils.get(String.format("%s?%s=%s", dto.getUrl(), "key", URLEncoder.encode(JSON.toJSONString(paramMap), "utf-8")));
                break;
            }
            case "POST": {
                Map<String, Object> names = gatewayService.getEntityNames(dto.getClassId(), dto.getTypeId());
                Map<String, String> headers = new HashMap<>(4);
                headers.put("Content-Type", "application/json");
                paramMap.putAll(names);
                response = HttpClientUtils.postParameters(server+dto.getUrl(), JSON.toJSONString(paramMap), headers);
                break;
            }
            case "DELETE": {
                response = HttpClientUtils.delete(String.format("%s?%s=%s", dto.getUrl(), "key", URLEncoder.encode(JSON.toJSONString(paramMap), "utf-8")));
                break;
            }
            case "PUT": {
                Map<String, String> headers = new HashMap<>(4);
                headers.put("Content-Type", "application/json");
                paramMap.put("city", dto.getCity());
                response = HttpClientUtils.putParameters(dto.getUrl(), paramMap, headers);
                break;
            }
            default:
                throw new BusinessException(RetCode.METHOD_NOT_ALLOWED, "");
        }

        return ObjectUtils.isNotEmpty(response) ? response : "";
    }

    /**
     * 获取详情
     */
    @SuppressWarnings("unchecked")
    public Map queryDetail(QueryDetailDTO dto) throws Exception {

        Map<String, Object> param = gatewayService.getEntityNames(dto.getClassId(), dto.getTypeId());
        param.put("id", dto.getId());
        Map<String, String> headers = new HashMap<>(4);
        headers.put("Content-Type", "application/json");
        String response = HttpClientUtils.postParameters(commonDetailUrl, JSON.toJSONString(param), headers);
        Map detail = JSON.parseObject(response,Map.class);
        if(ObjectUtils.isNotEmpty(detail)){
            detail.put("attachment",attachmentService.findAttachmentService(detail.get("id").toString()));
        }
        return detail;
    }

    /**条件查询表单实体列表*/
    public Map<String,Object> entityList(ListEntityDTO dto)throws Exception{

        Map<String, Object> names = gatewayService.getEntityNames(dto.getClassId(), dto.getTypeId());
        names.put("pageIndex",dto.getData().getPageInfo().getPageIndex());
        names.put("pageSize",dto.getData().getPageInfo().getPageSize());

        //获取实体字段规则
        List<Map<String,Object>> list = gatewayService.listFieldRules(dto.getClassId(), dto.getTypeId());
        final Map<String,Object> rulesMap = new HashMap<>();
        list.forEach(item ->{
            Object ruleObj = item.get("rules");
            if(Objects.nonNull(ruleObj)){
                FieldRuleDO fieldRuleDO = JSON.parseObject(ruleObj.toString(),FieldRuleDO.class);
                rulesMap.put(item.get("fieldName").toString(),fieldRuleDO.getSearchRule().get("conditionRule"));
            }
        });

        List<ListEntityDTO.Condition> conditions = dto.getData().getConditions();
        if(CollectionUtils.isNotEmpty(conditions)){
            conditions.forEach(condition -> {
                condition.setCondition(rulesMap.get(condition.getFieldName()));
            });
        }else {
            conditions = new ArrayList<>();
        }

        //设置组织隔离筛选项
        if("bill".equals(dto.getClassId())){
            ListEntityDTO.Condition orgCondition = new ListEntityDTO.Condition();
            orgCondition.setCondition("EntityCondition.IN");
            orgCondition.setFieldName("orgId");
            List<Organization> orgList = orgService.getBaseOrgByUserId(Long.parseLong(userInfoManager.getUserInfo().getUserId()));
            List<Long> orgIds = orgList.stream().map(Organization::getId).collect(Collectors.toList());
            orgCondition.setValue(orgIds);
            conditions.add(orgCondition);
            names.put("conditions",conditions);
        }

        Map<String, String> headers = new HashMap<>(4);
        headers.put("Content-Type", "application/json");
        MoquiListRetDO result = JSON.parseObject(HttpClientUtils.postParameters(server+dto.getUrl(), JSON.toJSONString(names), headers), MoquiListRetDO.class);
        Map<String,Object> retMap = new HashMap<>();
        if(ObjectUtils.isNotEmpty(result)){
            dto.getData().getPageInfo().setTotal(result.getCount());
            retMap.put("pageInfo",dto.getData().getPageInfo());
            retMap.put("list",result.getList());
        }
        return retMap;
    }

    /**
     * 保存实体
     */
    @SuppressWarnings("unchecked")
    public Long saveEntity(GatewayDTO dto, RequestEnum requestEnum) throws Exception {

        //将前端时间数据转换成时间戳字符串 /date(***)/ ==> "***"
        Map<String, Object> paramMap = dto.getData();
        paramMap.forEach((key, value) -> {
            //排除子表数据
            if (!Objects.equals("children", key)) {
                Pattern r = Pattern.compile(PATTERN);
                Matcher m = r.matcher(value.toString());
                if (m.matches()) {
                    paramMap.put(key, String.valueOf(m.group(2)));
                }
            }
        });
        Object childrenObj = paramMap.get("children");
        if (Objects.nonNull(childrenObj)) {
            List<Map> children = (List<Map>) childrenObj;
            if (CollectionUtils.isNotEmpty(children)) {
                children.forEach(child ->{
                    child.forEach((key, value) -> {
                        if(Objects.nonNull(value)){
                            Pattern r = Pattern.compile(PATTERN);
                            Matcher m = r.matcher(value.toString());
                            if (m.matches()) {
                                child.put(key, String.valueOf(m.group(2)));
                            }
                        }
                    });
                });

            }
        }
        List<Long> orgIds = userInfoManager.getUserInfo().getOrgList();
        if(CollectionUtils.isEmpty(orgIds)){
            throw new BusinessException(RetCode.INTERNAL_SERVER_ERROR,"该用户无基础组织");
        }
        if(orgIds.size() > 1){
            throw new BusinessException(RetCode.INTERNAL_SERVER_ERROR,"该用户无添加权限，请选择基础组织");
        }
        paramMap.put("orgId",orgIds.get(0));
        //设置表单默认字段值
        if(Objects.equals(EntityClassEnum.BILL_CLASS.getValue(),dto.getClassId())){
            paramMap.put("billid",System.currentTimeMillis());
            paramMap.put("create_user",userInfoManager.getUserInfo().getUserId());
            paramMap.put("create_time",String.valueOf(System.currentTimeMillis()));
            paramMap.put("last_modify_user",userInfoManager.getUserInfo().getUserId());
            paramMap.put("status", AuditStatusEnum.NEWLY_BUILD.getValue());
        }

        //转发保存请求
        String response = transportReq(dto);
        Map retMap = JSON.parseObject(response, Map.class);
        Long id = (Long) retMap.get("id");
        String businessId;
        if(Objects.isNull(id)){
            id = Long.parseLong(dto.getData().get("id").toString());
            businessId = String.valueOf(dto.getData().get("id"));
        }else {
            businessId = String.valueOf(id);
        }
        Object attachment = dto.getData().get("attachment");
        if (Objects.isNull(attachment)) {
            return id;
        }
        List<Map> attachmentDTOS = (List<Map>) attachment;
        if (CollectionUtils.isEmpty(attachmentDTOS)) {
            return id;
        }

        List<BillAttachmentDTO> dtos = new ArrayList<>();
        for (Map map : attachmentDTOS) {
            BillAttachmentDTO billAttachmentDTO = new BillAttachmentDTO();
            BeanUtils.populate(billAttachmentDTO,map);
            billAttachmentDTO.setBusinessId(businessId);
            dtos.add(billAttachmentDTO);
        }

        //绑定文件与表单
        attachmentService.addAttachmentService(dtos.toArray(new BillAttachmentDTO[]{}));
        return id;
    }

    /**
     * 保存实体，并发起一个工作流
     */
    public Map<String,Object> saveAndProcess(GatewayDTO dto) throws Exception {

        Map<String,Object> retMap = new HashMap<>(4);
        Long id = saveEntity(dto,RequestEnum.INSERT_REQUEST);

        if (log.isDebugEnabled()) {
            log.debug(String.format("current user is {%s}", userInfoManager.getUserInfo()));
        }
        String orgId = userInfoManager.getUserInfo().getOrgList().get(0).toString();
        WorkflowInstanceDTO workflowInstanceDTO = new WorkflowInstanceDTO();
        workflowInstanceDTO.setOrgId(orgId);
        workflowInstanceDTO.setBillId(id);
        workflowInstanceDTO.setClassId(dto.getClassId());
        workflowInstanceDTO.setTypeId(dto.getTypeId());

        String result = JSON.toJSONString(invokeMoquiServiceImpl.createWorkflowInstance(workflowInstanceDTO).getData());
        retMap.put("id",id);
        retMap.put("result",result);
        return retMap;

    }

    /**功能描述: 生成工作流，并保存单据,然后传递*/
    public Map<String,Object> saveAndProcessAndPush(GatewayDTO dto)throws Exception{

        Map<String,Object> retMap = saveAndProcess(dto);
        //生成工作流，并保存单据
    	WorkflowInstanceVO result = JSON.parseObject(retMap.get("result").toString(),WorkflowInstanceVO.class);

        //传递
        BillAuditDTO billAuditDTO = new BillAuditDTO();
        billAuditDTO.setUserId(userInfoManager.getUserInfo().getUserId());
        billAuditDTO.setTaskId(result.getTaskId());
        invokeMoquiServiceImpl.auditSubmit(billAuditDTO);

        return retMap;
    }

    /**
     * 获取引用下拉列表
     */
    public List<Map> getRefSpinner(FilterDTO dto) throws Exception {

        if (Objects.equals(EntityClassEnum.ENUM_CLASS.getValue(), dto.getClassId())) {
            Map<String, Object> names = gatewayService.getEntityNames(dto.getClassId(), dto.getTypeId());
            names.put("pageIndex", 0);
            names.put("pageSize", 20);
            names.put("searchValue", dto.getSearchValue());
            Map<String, String> headers = new HashMap<>(4);
            headers.put("Content-Type", "application/json");
            String response = HttpClientUtils.postParameters(refListUrl, JSON.toJSONString(names), headers);
            response = StringUtils.isNotBlank(response) ? response : "";
            return JSON.parseArray(response, Map.class);
        }
        if (Objects.equals(EntityClassEnum.ENTITY_CLASS.getValue(), dto.getClassId())) {
            return JSON.parseArray(filterEntityRecord(dto), Map.class);
        }

        if (Objects.equals(EntityClassEnum.VIEW_CLASS.getValue(), dto.getClassId())) {
            JSON.parseArray(filterEntityRecord(dto), Map.class);
            return JSON.parseArray(filterEntityRecord(dto), Map.class);
        }

        return null;
    }

    /**
     * 获取单个引用值
     */
    public Map getRefValue(RefDTO dto) throws Exception {
        Set<Long> ids = new HashSet<>();
        ids.add(dto.getId());
        dto.setIds(ids);
        List<RefDTO> param = new ArrayList<>();
        param.add(dto);
        return queryListData(param);
    }

    /**
     * 获取引用值列表
     */
    public Map listRefValue(List<RefDTO> dto) throws Exception {

        return queryListData(dto);
    }

    private Map queryListData(List<RefDTO> refDTOS) throws Exception{
        //将classId与typeId替换成entityName
        List<Map<String,Object>> refList = new ArrayList<>();
        for(RefDTO dto:refDTOS){
            Map<String, Object> param = gatewayService.getEntityNames(dto.getClassId(), dto.getTypeId());
            param.put("fields",dto.getTargetFields());
            param.put("ids",dto.getIds());
            refList.add(param);
        }

        Map<String, String> headers = new HashMap<>(4);
        headers.put("Content-Type", "application/json");
        String response = HttpClientUtils.postParameters(refUrl, JSON.toJSONString(refList), headers);
        response = StringUtils.isNotBlank(response) ? response : "";
        log.info(String.format("ref response is {%s}", response));
        return JSON.parseObject(response,Map.class);
    }

    /**
     * 根据过滤字段获取实体详情列表
     */
    public String filterEntityRecord(FilterDTO dto) throws Exception {

        Map<String, Object> param = gatewayService.getEntityNames(dto.getClassId(), dto.getTypeId());
        //获取元数据配置时定义的过滤字段
        List<Map<String, Object>> filterFields = new ArrayList<>();

        if (!"view".equals(dto.getClassId())) {
            filterFields = gatewayService.getFilterFields(dto.getClassId(), dto.getTypeId());
            if (CollectionUtils.isEmpty(filterFields)) {
                throw new BusinessException(RetCode.EMPTY_FILTER, "过滤字段未设置");
            }
        }

        List<String> fieldNameList = filterFields.stream()
                .filter(a -> Objects.equals(a.get("typeValue"), "true"))
                .map(p -> (String) p.get("fieldName"))
                .collect(Collectors.toList());
        //fields为与moqui脚本约定字段
        param.put("fields", fieldNameList);
        //字段过滤的值
        param.put("value", dto.getSearchValue());
        Map<String, String> headers = new HashMap<>(4);
        headers.put("Content-Type", "application/json");
        String response = HttpClientUtils.postParameters(filterUrl, JSON.toJSONString(param), headers);
        response = StringUtils.isNotBlank(response) ? response : "";
        log.info(String.format("filter response is {%s}", response));
        return response;
    }

}
