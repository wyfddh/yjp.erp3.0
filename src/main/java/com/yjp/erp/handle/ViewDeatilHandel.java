package com.yjp.erp.handle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yjp.erp.model.domain.ActionDO;
import com.yjp.erp.model.domain.BillFieldsRulesDO;
import com.yjp.erp.model.domain.EventDO;
import com.yjp.erp.model.domain.FieldRuleDO;
import com.yjp.erp.model.domain.ServicePropertyDO;
import com.yjp.erp.model.dto.bill.EntityDetailDTO;
import com.yjp.erp.model.dto.bill.EntityPageDTO;
import com.yjp.erp.conf.exception.BusinessException;
import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.po.eeca.BillEeca;
import com.yjp.erp.model.po.service.BillAction;
import com.yjp.erp.model.po.service.Service;
import com.yjp.erp.model.po.view.ViewField;
import com.yjp.erp.model.po.view.ViewParent;
import com.yjp.erp.model.po.view.ViewSon;
import com.yjp.erp.service.ModelService;
import com.yjp.erp.service.ModuleService;
import com.yjp.erp.service.ServiceService;
import com.yjp.erp.service.ViewService;
import com.yjp.erp.model.vo.RetCode;
import com.yjp.erp.model.vo.bill.BillVO;
import com.yjp.erp.model.vo.bill.ModelVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wyf
 * @date 2019/3/28 上午 11:47
 **/
@Component
public class ViewDeatilHandel {
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private ViewService viewService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private ModelService modelService;

    public BillVO getViewDetail(EntityDetailDTO dto) throws Exception{

        if (Objects.isNull(dto.getClassId())||Objects.isNull(dto.getTypeId())) {
            throw new BusinessException(RetCode.PARAM_EMPTY, "classId或typeId为空");
        }
        BillVO vo = new BillVO();
        handelViewProperties(vo,dto);
        generateServiceData(vo,dto);
        return vo;
    }
    /**
     * @author wyf
     * @description  从数据库读取实体的相关配置信息，组装成前端需要的结构
     * @date  2019/4/12 下午 5:06
     * @return void
     */
    private void handelViewProperties (BillVO vo, EntityDetailDTO dto) throws Exception{
        Module param = new Module();
        param.setTypeId(dto.getTypeId());
        param.setClassId(dto.getClassId());
        Module module = moduleService.getModuleByClassIdAndTypeId(param);
        ViewParent viewParent = viewService.getViewByModuleId(module.getId());

        List<ViewSon> viewSonList = viewService.getViewSonByParentId(viewParent.getId());
        List<ViewField> viewFieldList = viewService.getViewFieldByParentId(viewParent.getId());
        //组装fields
        BillFieldsRulesDO billFieldsRulesDO = fieldDbDataToVoData(viewSonList, viewFieldList);

        vo.setPackageName(viewParent.getPackagePath());
        vo.setTitle(viewParent.getTitle());
        vo.setStatus(module.getPublishState());
        vo.setRules(billFieldsRulesDO.getRules());
        vo.setClassId(dto.getClassId());
        vo.setFields(billFieldsRulesDO.getFields());
        vo.setTypeId(dto.getTypeId());
        vo.setCache(viewParent.getCache());
//        vo.getUse(viewParent.getTransaction());

    }

    private BillFieldsRulesDO fieldDbDataToVoData(List<ViewSon> viewSonList,List<ViewField> viewFieldList) throws Exception{

        List<Map<String,Object>> fieldList = new ArrayList<>();
        List<FieldRuleDO> rulesList = new ArrayList<>();
        for (ViewSon viewSon : viewSonList) {
            Map<String,Object> map = new HashMap<>(16);
            map.put("masterId",viewSon.getMasterId());
            map.put("entityName",viewSon.getEntityName());
            map.put("entityAlias",viewSon.getEntityAlias());
            if (StringUtils.isNotBlank(viewSon.getJoinFromAlias())) {
                map.put("joinFromAlias",viewSon.getJoinFromAlias());
            }
            if (StringUtils.isNotBlank(viewSon.getEntityParentValue())) {
                map.put("entityParentValue",viewSon.getEntityParentValue());
            }
            if (StringUtils.isNotBlank(viewSon.getEntityValue())) {
                map.put("entityValue",viewSon.getEntityValue());
            }
            List<Map<String,Object>> fields = new ArrayList<>();
            for (ViewField viewField : viewFieldList) {
                Long viewId = viewField.getViewId();
                if (viewId.equals(viewSon.getId())) {
                    Map<String,Object> mapField = new HashMap<>(8);
                    mapField.put("name",viewField.getName());
                    mapField.put("field",viewField.getField());
                    mapField.put("entityAlias",viewSon.getEntityAlias());
                    fields.add(mapField);
                    String rules = viewField.getRules();
                    if(StringUtils.isNotBlank(rules)){
                        rulesList.add(JSON.parseObject(rules,FieldRuleDO.class));
                    }
                }
            }
            if (fields.size() > 0) {
                map.put("viewField",fields);
            }
            fieldList.add(map);
        }
        BillFieldsRulesDO billFieldsRulesDO = new BillFieldsRulesDO();
        billFieldsRulesDO.setFields(fieldList);
        billFieldsRulesDO.setRules(rulesList);
        return billFieldsRulesDO;
    }

    /**
     * @author wyf
     * @description  获取实体服务动作等信息
     * @date  2019/4/12 下午 5:07
     * @return void
     */
    private void generateServiceData(BillVO vo, EntityDetailDTO dto){

        Module module = new Module();
        module.setTypeId(dto.getTypeId());
        module.setClassId(dto.getClassId());
        List<BillAction> actionList = serviceService.getBillActionList(module);
        List<BillEeca> eecaList = serviceService.getBillEecaList(module);
        List<ServicePropertyDO> services = new ArrayList<>();

        List<ActionDO> actionDOS = new ArrayList<>();
        List<EventDO> eventDOS = new ArrayList<>();
        List<String> serviceList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(actionList)){
            actionList.forEach(action ->{
                ActionDO actionDO = new ActionDO();
                actionDO.setActionName(action.getButton());
                actionDO.setAfterService(action.getAfterService());
                actionDO.setBeforeService(action.getBeforeService());
                actionDO.setIcon(action.getIcon());
                actionDO.setId(action.getId());
                actionDO.setLabel(action.getLabel());
                actionDO.setServiceName(action.getRefService().getServiceName());
                actionDO.setRpcMethod(action.getRpcMethod());
                actionDO.setRpcUrl(action.getRpcUri());
                actionDOS.add(actionDO);
                Service service = action.getRefService();
                if(!serviceList.contains(service.getServiceName())){
                    ServicePropertyDO servicePropertyDO = new ServicePropertyDO();
                    servicePropertyDO.setId(service.getId());
                    servicePropertyDO.setLabel(service.getLabel());
                    servicePropertyDO.setNoun(service.getNoun());
                    servicePropertyDO.setScript(service.getScript());
                    servicePropertyDO.setServiceName(service.getServiceName());
                    servicePropertyDO.setVerb(service.getVerb());
                    services.add(servicePropertyDO);
                    serviceList.add(service.getServiceName());
                }
            });
        }
        if(CollectionUtils.isNotEmpty(eecaList)){
            eecaList.forEach(eeca ->{
                EventDO eventDO = new EventDO();
                eventDO.setCondition(eeca.getECondition());
                eventDO.setEventType(eeca.getEventType());
                eventDO.setMethods(JSONArray.parseArray(eeca.getEMethod(),String.class));
                eventDO.setServiceName(eeca.getRefService().getServiceName());
                eventDOS.add(eventDO);
                if(!serviceList.contains(eeca.getRefService().getServiceName())){
                    Service service = eeca.getRefService();
                    if(!serviceList.contains(service.getServiceName())){
                        ServicePropertyDO servicePropertyDO = new ServicePropertyDO();
                        servicePropertyDO.setId(service.getId());
                        servicePropertyDO.setLabel(service.getLabel());
                        servicePropertyDO.setNoun(service.getNoun());
                        servicePropertyDO.setScript(service.getScript());
                        servicePropertyDO.setServiceName(service.getServiceName());
                        servicePropertyDO.setVerb(service.getVerb());
                        services.add(servicePropertyDO);
                        serviceList.add(service.getServiceName());
                    }
                }
            });
        }
        vo.setServices(services);
        vo.setActions(actionDOS);
        vo.setEvents(eventDOS);
    }

    public ModelVO getModelFields() throws Exception{

        List<ViewSon> viewSonList = viewService.getAllViewSon();
        List<ViewField> viewFieldList = viewService.getAllViewField();

        BillFieldsRulesDO fieldsRulesDO = fieldDbDataToVoData(viewSonList, viewFieldList);

        List<ServicePropertyDO> servicePropertyDOS = modelService.getGlobalServices();

        ModelVO modelVO = new ModelVO();
        modelVO.setFields(fieldsRulesDO.getFields());
        modelVO.setRules(fieldsRulesDO.getRules());
        modelVO.setServices(servicePropertyDOS);
        return modelVO;
    }

    public Map<String,Object> getAllViews(EntityPageDTO dto) {
        Map<String,Object> map = new HashMap<>(8);
        if (com.yjp.erp.util.ObjectUtils.isNotEmpty(dto.getTitle())) {
            map.put("title",dto.getTitle());
        }
        if (com.yjp.erp.util.ObjectUtils.isNotEmpty(dto.getStatus())) {
            map.put("status",dto.getStatus());
        }
        int count = viewService.countViewByMap(map);
        List<Map<String,Object>> list = new ArrayList<>();
        if(count > 0){
            map.put("pageSize",dto.getPageSize());
            map.put("startIndex",(dto.getPageNo()-1)*dto.getPageSize());
            list = viewService.getViewByMap(map);
        }
        Map<String,Object> retMap = new HashMap<>(4);
        retMap.put("pageNo",dto.getPageNo());
        retMap.put("pageSize",dto.getPageSize());
        retMap.put("total",count);
        retMap.put("dataList",list);

        return retMap;

    }
}
