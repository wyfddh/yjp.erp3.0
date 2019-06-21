package com.yjp.erp.handle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yijiupi.himalaya.base.utils.AssertUtils;
import com.yjp.erp.conf.UserInfoManager;
import com.yjp.erp.conf.exception.BusinessException;
import com.yjp.erp.constants.AuditStatusEnum;
import com.yjp.erp.constants.ExternalCallEnum;
import com.yjp.erp.constants.JsonTypeEnum;
import com.yjp.erp.constants.RequestEnum;
import com.yjp.erp.model.domain.MoquiListRetDO;
import com.yjp.erp.model.dto.bill.BillAttachmentDTO;
import com.yjp.erp.model.dto.externalapi.ExternalCallApiDTO;
import com.yjp.erp.model.dto.gateway.ListEntityDTO;
import com.yjp.erp.model.dto.gateway.QueryDetailDTO;
import com.yjp.erp.model.externalapi.ExternalApiConfig;
import com.yjp.erp.model.externalapi.ExternalCallApi;
import com.yjp.erp.model.vo.RetCode;
import com.yjp.erp.service.externalapi.ExternalApiConfigService;
import com.yjp.erp.service.gateway.GatewayService;
import com.yjp.erp.service.moqui.attachment.AttachmentService;
import com.yjp.erp.service.system.OrgService;
import com.yjp.erp.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author xialei
 * @date 2019/6/17 10:29
 */

@Component
@Slf4j
public class ExternalCallApiHandle {
    @Resource
    private ExternalApiConfigService externalApiConfigService;

    @Resource
    private GatewayService gatewayService;

    @Resource
    private OrgService orgService;

    @Resource
    private UserInfoManager userInfoManager;

    @Resource
    private AttachmentService attachmentService;

    @Resource
    private RestTemplate restTemplate;

    /**
     * 匹配时间的正则
     */
    private static final String PATTERN = "(/date\\()(.*)(\\)/)";

    private static final String packageName = "yjp";

    @Value("${moqui.server}")
    private String server;
    @Value("${moqui.detail.url}")
    private String commonDetailUrl;

    /**
     * 条件查询表单实体列表
     *
     * @param config
     * @return
     * @throws Exception
     */
    public List<Map> entityList(ExternalCallApi config) throws Exception {
        List<Map> result = new ArrayList<>();
        config.setOperateType(ExternalCallEnum.LIST.getValue());
        List<ExternalApiConfig> list = this.findByParams(config);
        if (list.size() > 0) {
            ExternalApiConfig apiConfig = list.get(0);
            String url = server + apiConfig.getUrl();

            Map<String, Object> names = gatewayService.commonGetEntityNames(config.getClassId(), config.getTypeId(),
                    packageName);
            names.put("pageIndex", config.getData().getPageInfo().getPageIndex());
            names.put("pageSize", config.getData().getPageInfo().getPageSize());

            List<ListEntityDTO.Condition> conditions = config.getData().getConditions();
            if (CollectionUtils.isNotEmpty(conditions)) {
                conditions.forEach(condition -> {
                    condition.setCondition("EntityCondition.IN");
                });
            } else {
                conditions = new ArrayList<>();
            }
            //设置组织隔离筛选项
//            if ("bill".equals(config.getClassId())) {
//                ListEntityDTO.Condition orgCondition = new ListEntityDTO.Condition();
//                orgCondition.setCondition("EntityCondition.IN");
//                orgCondition.setFieldName("orgId");
//                List<Organization> orgList = orgService.getBaseOrgByUserId(Long.parseLong(userInfoManager.getUserInfo
//                        ().getUserId()));
//                List<Long> orgIds = orgList.stream().map(Organization::getId).collect(Collectors.toList());
//                orgCondition.setValue(orgIds);
//                conditions.add(orgCondition);
//                names.put("conditions", conditions);
//            }
            names.put("conditions", conditions);
            String json = toMoqui(new JSONObject(names), url);
            MoquiListRetDO moquiListRetDO = JSON.parseObject(json, MoquiListRetDO.class);
            result = moquiListRetDO.getList();
        }
        return result;
    }

    /**
     * 获取详情
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Map queryDetail(ExternalCallApiDTO dto) throws Exception {
        ExternalCallApi callConfig = new ExternalCallApi();
        callConfig.setClassId(dto.getClassId());
        callConfig.setTypeId(dto.getTypeId());
        callConfig.setSystemType(dto.getSystemType());
        callConfig.setOperateType(ExternalCallEnum.QUERY.getValue());
        List<ExternalApiConfig> list = this.findByParams(callConfig);
        Map detail=new HashMap();
        if (list.size() > 0) {
            Map<String, Object> param = gatewayService.commonGetEntityNames(dto.getClassId(), dto.getTypeId(), packageName);
            param.put("id", dto.getId());
            String response = toMoqui(new JSONObject(param), commonDetailUrl);
            detail = JSON.parseObject(response, Map.class);
            if (ObjectUtils.isNotEmpty(detail)) {
                detail.put("attachment", attachmentService.findAttachmentService(detail.get("id").toString()));
            }
        }
        return detail;
    }


    /**
     * 保存实体
     */
    @SuppressWarnings("unchecked")
    public Long saveEntity(ExternalCallApiDTO dto, String operateType) throws Exception {
        ExternalCallApi callConfig = new ExternalCallApi();
        callConfig.setClassId(dto.getClassId());
        callConfig.setTypeId(dto.getTypeId());
        callConfig.setSystemType(dto.getSystemType());
        callConfig.setOperateType(operateType);
        List<ExternalApiConfig> list = this.findByParams(callConfig);

        if (list.size() > 0) {
            ExternalApiConfig apiConfig = list.get(0);
            Map<String, Object> paramMap = dto.getData();
//            List<Long> orgIds = userInfoManager.getUserInfo().getOrgList();
//            if (CollectionUtils.isEmpty(orgIds)) {
//                throw new BusinessException(RetCode.INTERNAL_SERVER_ERROR, "该用户无基础组织");
//            }
//            if (orgIds.size() > 1) {
//                throw new BusinessException(RetCode.INTERNAL_SERVER_ERROR, "该用户无添加权限，请选择基础组织");
//            }
//            paramMap.put("orgId", orgIds.get(0));
            //设置表单默认字段值
            if (Objects.equals(dto.getTypeId(), dto.getClassId())) {
                paramMap.put("billid", System.currentTimeMillis());
                paramMap.put("create_user", userInfoManager.getUserInfo().getUserId());
                paramMap.put("create_time", String.valueOf(System.currentTimeMillis()));
                paramMap.put("last_update_user", userInfoManager.getUserInfo().getUserId());
                paramMap.put("status", AuditStatusEnum.NEWLY_BUILD.getValue());
            }
            //转发保存请求
            Map<String, Object> names = gatewayService.commonGetEntityNames(dto.getClassId(), dto.getTypeId(),
                    packageName);
            paramMap.putAll(names);
            String url = server + apiConfig.getUrl();

            String response = toMoqui(new JSONObject(paramMap), url);
            Map retMap = JSON.parseObject(response, Map.class);
            Long id = (Long) retMap.get("id");
            String businessId;
            if (Objects.isNull(id)) {
                id = Long.parseLong(dto.getData().get("id").toString());
                businessId = String.valueOf(dto.getData().get("id"));
            } else {
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
                BeanUtils.populate(billAttachmentDTO, map);
                billAttachmentDTO.setBusinessId(businessId);
                dtos.add(billAttachmentDTO);
            }

            //绑定文件与表单
            attachmentService.addAttachmentService(dtos.toArray(new BillAttachmentDTO[]{}));
            return id;
        }
        return null;
    }


    private List<ExternalApiConfig> findByParams(ExternalCallApi callConfig) {
        ExternalApiConfig config = new ExternalApiConfig();
        config.setClassId(callConfig.getClassId());
        config.setTypeId(callConfig.getTypeId());
        config.setSystemType(callConfig.getSystemType());
        config.setStatus(new Byte("1"));
        config.setOperateType(callConfig.getOperateType());
        List<ExternalApiConfig> list = externalApiConfigService.findByParams(config);
        return list;
    }


    private String toMoqui(JSONObject parameters, String url) throws Exception {
        ResponseEntity<String> resp = restTemplate.postForEntity(url, parameters, String.class);
        AssertUtils.isTrue(resp.getStatusCode() == HttpStatus.OK, "moqui请求失败");
        String body = resp.getBody();
        return body;
    }
}
