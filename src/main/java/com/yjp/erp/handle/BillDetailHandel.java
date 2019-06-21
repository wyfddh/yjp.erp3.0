package com.yjp.erp.handle;

import com.alibaba.fastjson.JSONArray;
import com.yjp.erp.constants.EntityClassEnum;
import com.yjp.erp.constants.EntityPropertyEnum;
import com.yjp.erp.constants.EecaPropertyEnum;
import com.yjp.erp.model.domain.*;
import com.yjp.erp.model.domain.fieldrule.OptionRules;
import com.yjp.erp.model.dto.bill.EntityDetailDTO;
import com.yjp.erp.model.dto.bill.EntityPageDTO;
import com.yjp.erp.conf.exception.BusinessException;
import com.yjp.erp.model.po.bill.*;
import com.yjp.erp.model.po.eeca.BillEeca;
import com.yjp.erp.model.po.service.BillAction;
import com.yjp.erp.model.po.service.Service;
import com.yjp.erp.service.*;
import com.yjp.erp.model.vo.RetCode;
import com.yjp.erp.model.vo.bill.BillVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author liushui
 * @description:
 * @date 2019/3/25
 */
@Component
public class BillDetailHandel extends BaseFieldHandel {

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private BillService billService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private BaseEntityService baseEntityService;

    @Autowired
    private PushService pushService;

    /**
     * 根据 EntityDetailDTO 获取实体的vo对象
     *
     * @param dto 查询实体详情的dto对象
     * @return
     * @throws Exception
     */
    public BillVO getBillDetail(EntityDetailDTO dto) throws Exception {
        if (Objects.isNull(dto.getClassId()) || Objects.isNull(dto.getTypeId())) {
            throw new BusinessException(RetCode.PARAM_EMPTY, "classId或typeId为空");
        }
        BillVO vo = new BillVO();
        generateBillProperties(vo, dto);
        generateServiceData(vo, dto);
        return vo;
    }

    /**
     * 从数据库读取实体的相关配置信息，组装成前端需要的结构
     *
     * @param vo  实体的vo对象
     * @param dto 查询实体的dto对象
     */
    private void generateBillProperties(BillVO vo, EntityDetailDTO dto) {
        Module param = createSimpleModule(dto.getClassId(), dto.getTypeId());
        Module module = moduleService.getModuleByClassIdAndTypeId(param);

        List<BillField> moquiProperties = null;
        List<BillField> webProperties = null;
        List<BillProperty> billProperties = null;
        Bill bill = new Bill();

        //如果为表单
        if (Objects.equals(EntityClassEnum.BILL_CLASS.getValue(), dto.getClassId())) {
            bill = billService.getBillByModuleId(module.getId());
            moquiProperties = billService.getBillMoquiProperties(bill.getId());
            webProperties = billService.getBillWebProperties(bill.getId());
        }
        //如果为实体
        if (Objects.equals(EntityClassEnum.ENTITY_CLASS.getValue(), dto.getClassId())) {
            bill = baseEntityService.getBillByModuleId(module.getId());
            moquiProperties = baseEntityService.getBillMoquiProperties(bill.getId());
            webProperties = baseEntityService.getBillWebProperties(bill.getId());
        }
        //读取子表数据
        handelChildrenData(vo, bill.getId(), dto.getClassId());

        BillFieldsRulesDO billFieldsRulesDO = fieldDbDataToVoData(moquiProperties, webProperties);
        billProperties = getBillProperties(dto, billProperties, bill);
        setEntityAttrToVo(vo, billProperties);

        vo.setTitle(bill.getLabel());
        vo.setRules(billFieldsRulesDO.getRules());
        vo.setStatus(module.getPublishState());
        vo.setClassId(dto.getClassId());
        vo.setFields(billFieldsRulesDO.getFields());
        vo.setTypeId(dto.getTypeId());
    }

    /**
     * 给BilVo
     * @param vo
     * @param billProperties
     */
    private void setEntityAttrToVo(BillVO vo, List<BillProperty> billProperties) {
        if (CollectionUtils.isNotEmpty(billProperties)) {
            billProperties.forEach(property -> {
                if (Objects.equals(property.getName(), EntityPropertyEnum.CACHE.getValue())) {
                    vo.setCache(property.getElementValue());
                }
                if (Objects.equals(property.getName(), EntityPropertyEnum.PACKAGE_NAME.getValue())) {
                    vo.setPackageName(property.getElementValue());
                }
                if (Objects.equals(property.getName(), EntityPropertyEnum.USE.getValue())) {
                    vo.setUse(property.getElementValue());
                }
            });
        }
    }

    private List<BillProperty> getBillProperties(EntityDetailDTO dto, List<BillProperty> billProperties, Bill bill) {
        if (Objects.equals(EntityClassEnum.BILL_CLASS.getValue(), dto.getClassId())) {
            billProperties = billService.getBillPropertyByBillId(bill.getId());
        }
        if (Objects.equals(EntityClassEnum.ENTITY_CLASS.getValue(), dto.getClassId())) {
            billProperties = baseEntityService.getBillPropertyByBillId(bill.getId());
        }
        return billProperties;
    }

    /**
     * 根据classId 和typeId 创建一个只有classId和typeId的简单module
     *
     * @param classId
     * @param typeId
     * @return module对象
     */
    private Module createSimpleModule(String classId, String typeId) {
        Module simpleModule = new Module();
        simpleModule.setTypeId(typeId);
        simpleModule.setClassId(classId);
        return simpleModule;
    }

    /**
     * 获取实体服务动作等信息
     *
     * @param vo  实体的vo对象
     * @param dto 实体详情的dto对象
     */
    private void generateServiceData(BillVO vo, EntityDetailDTO dto) {
        Module module = createSimpleModule(dto.getClassId(), dto.getTypeId());

        List<BillAction> actionList = serviceService.getBillActionList(module);
        List<BillEeca> eecaList = serviceService.getBillEecaList(module);
        List<OptionRules> optionRules = pushService.listOptionRules(module);

        List<ServicePropertyDO> services = new ArrayList<>();
        List<ActionDO> actionDOS = new ArrayList<>();
        List<EventDO> eventDOS = new ArrayList<>();
        List<String> serviceList = new ArrayList<>();

        addAction(actionList, services, actionDOS, serviceList);

        addEecas(eecaList, services, eventDOS, serviceList);

        vo.setOptionRules(optionRules);
        vo.setServices(services);
        vo.setActions(actionDOS);
        vo.setEvents(eventDOS);
    }

    /**
     * 添加eecas
     *
     * @param eecaList
     * @param services
     * @param eventDOS
     * @param serviceList
     */
    private void addEecas(List<BillEeca> eecaList, List<ServicePropertyDO> services, List<EventDO> eventDOS, List<String> serviceList) {
        if (CollectionUtils.isNotEmpty(eecaList)) {
            eecaList.forEach(eeca -> {
                EventDO eventDO = new EventDO();
                eventDO.setCondition(eeca.getECondition());
                eventDO.setEventType(eeca.getEventType());
                eventDO.setMethods(JSONArray.parseArray(eeca.getEMethod(), String.class));
                eventDO.setServiceName(eeca.getRefService().getServiceName());
                List<String> triggerList = JSONArray.parseArray(eeca.getETrigger(), String.class);
                if (CollectionUtils.isNotEmpty(triggerList)) {
                    triggerList.forEach(trigger -> {
                        if (Objects.equals(trigger, EecaPropertyEnum.RUN_BEFORE.getValue())) {
                            eventDO.setRunBefore(true);
                        }
                        if (Objects.equals(trigger, EecaPropertyEnum.RUN_ON_ERROR.getValue())) {
                            eventDO.setRunOnError(true);
                        }
                    });
                }
                List<String> otherOptions = JSONArray.parseArray(eeca.getOtherOptions(), String.class);
                if (CollectionUtils.isNotEmpty(otherOptions)) {
                    otherOptions.forEach(option -> {
                        if (Objects.equals(option, EecaPropertyEnum.GET_ORIGINAL_VALUE.getValue())) {
                            eventDO.setGetOriginalValue(true);
                        }
                        if (Objects.equals(option, EecaPropertyEnum.GET_ENTIRE_ENTITY.getValue())) {
                            eventDO.setGetEntireEntity(true);
                        }
                        if (Objects.equals(option, EecaPropertyEnum.SET_RESULTS.getValue())) {
                            eventDO.setSetResults(true);
                        }
                    });
                }
                eventDO.setEventName(eeca.getEecaId());
                eventDOS.add(eventDO);
                if (!serviceList.contains(eeca.getRefService().getServiceName())) {
                    Service service = eeca.getRefService();
                    if (!serviceList.contains(service.getServiceName())) {
                        ServicePropertyDO servicePropertyDO = createServicePropertyDO(service);
                        services.add(servicePropertyDO);
                        serviceList.add(service.getServiceName());
                    }
                }
            });
        }
    }

    /**
     * 添加服务
     *
     * @param actionList  实体行为的集合
     * @param services    服务属性DO对象集合
     * @param actionDOS   行为DO对象集合
     * @param serviceList
     */
    private void addAction(List<BillAction> actionList, List<ServicePropertyDO> services, List<ActionDO> actionDOS, List<String> serviceList) {
        if (CollectionUtils.isNotEmpty(actionList)) {
            actionList.forEach(action -> {
                ActionDO actionDO = createActionDO(action);
                actionDOS.add(actionDO);
                Service service = action.getRefService();
                if (!serviceList.contains(service.getServiceName())) {
                    ServicePropertyDO servicePropertyDO = createServicePropertyDO(service);
                    services.add(servicePropertyDO);
                    serviceList.add(service.getServiceName());
                }
            });
        }
    }

    /**
     * 根据BillAction 创建ActionDO对象
     *
     * @param action
     * @return
     */
    private ActionDO createActionDO(BillAction action) {
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
        actionDO.setDisableType(action.getDisableType());
        actionDO.setRoleAuthorization(action.getRoleAuthorization());
        return actionDO;
    }

    /**
     * 根据服务的对象生成ServicePropertyDO（xml元素的属性对象）
     *
     * @param service
     * @return
     */
    private ServicePropertyDO createServicePropertyDO(Service service) {
        ServicePropertyDO servicePropertyDO = new ServicePropertyDO();
        servicePropertyDO.setId(service.getId());
        servicePropertyDO.setLabel(service.getLabel());
        servicePropertyDO.setNoun(service.getNoun());
        servicePropertyDO.setScript(service.getScript());
        servicePropertyDO.setServiceName(service.getServiceName());
        servicePropertyDO.setVerb(service.getVerb());
        servicePropertyDO.setRoleAuthorization(service.getRoleAuthorization());
        servicePropertyDO.setServiceType(service.getServiceType());
        return servicePropertyDO;
    }

    /**
     * 获取子表信息
     */
    private void handelChildrenData(BillVO vo, Long billId, String classId) {
        //如果为表单
        List<Bill> childrenBill = new ArrayList<>();
        if (Objects.equals(EntityClassEnum.BILL_CLASS.getValue(), classId)) {
            childrenBill = billService.getChildrenBillByParentId(billId);
        }
        if (Objects.equals(EntityClassEnum.ENTITY_CLASS.getValue(), classId)) {
            childrenBill = baseEntityService.getChildrenBillByParentId(billId);
        }
        List<BillFieldsRulesDO> children = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(childrenBill)) {
            childrenBill.forEach(child -> {
                List<BillField> childMoqui = new ArrayList<>();
                List<BillField> childWeb = new ArrayList<>();
                if (Objects.equals(EntityClassEnum.BILL_CLASS.getValue(), classId)) {
                    childMoqui = billService.getBillMoquiProperties(child.getId());
                    childWeb = billService.getBillWebProperties(child.getId());
                }
                if (Objects.equals(EntityClassEnum.ENTITY_CLASS.getValue(), classId)) {
                    childMoqui = baseEntityService.getBillMoquiProperties(child.getId());
                    childWeb = baseEntityService.getBillWebProperties(child.getId());
                }
                BillFieldsRulesDO childProperties = fieldDbDataToVoData(childMoqui, childWeb);
                childProperties.setPrimaryKey(child.getPrimaryKey());
                childProperties.setForeignKey(child.getForeignKey());
                childProperties.setTitle(child.getLabel());
                String typeId = billService.getTypeIdByModuleId(child.getModuleId());
                childProperties.setTypeId(typeId);
                children.add(childProperties);
            });
        }
        vo.setChildren(children);
    }

    public Map<String, Object> getAllEntities(EntityPageDTO dto) throws Exception {

        return billService.getAllEntities(dto);
    }
}
