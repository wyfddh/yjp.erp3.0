package com.yjp.erp.handle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yjp.erp.conf.UserInfoManager;
import com.yjp.erp.conf.exception.BusinessException;
import com.yjp.erp.constants.*;
import com.yjp.erp.model.domain.*;
import com.yjp.erp.model.domain.fieldrule.OptionRules;
import com.yjp.erp.model.dto.bill.EntityDetailDTO;
import com.yjp.erp.model.dto.bill.EntityAndServiceDTO;
import com.yjp.erp.model.po.bill.*;
import com.yjp.erp.model.po.eeca.BillEeca;
import com.yjp.erp.model.po.eeca.EecaService;
import com.yjp.erp.model.po.service.ActionService;
import com.yjp.erp.model.po.service.BillAction;
import com.yjp.erp.model.po.service.Service;
import com.yjp.erp.model.po.view.ViewField;
import com.yjp.erp.model.po.view.ViewSon;
import com.yjp.erp.model.vo.RetCode;
import com.yjp.erp.model.vo.bill.ServiceAndActionVO;
import com.yjp.erp.service.*;
import com.yjp.erp.util.EntityValidateUtils;
import com.yjp.erp.util.SnowflakeIdWorker;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author liushui
 * @description: 处理表单增删改查handle
 * @date 2019/3/22
 */
@Component
public class BillHandle {

    @Value("${moqui.base.rest}")
    private String baseRest;

    @Value("${moqui.pre}")
    private String pre;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private BillService billService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ViewService viewService;

    @Autowired
    private BaseEntityService baseEntityService;

    @Autowired
    private BillOptionService billOptionService;

    @Autowired
    private UserInfoManager userInfoManager;

    @Transactional(rollbackFor = Exception.class)
    public void handleBillAndService(EntityAndServiceDTO dto) throws Exception {
        EntityValidateUtils.validate(dto);

        //新增module
        List<Module> insertModules = new ArrayList<>();
        //修改module
        List<Module> updateModules = new ArrayList<>();
        BillDO billDO = new BillDO();
        ServiceDO serviceDO = new ServiceDO();
        List<BillOptionRules> optionRules = new ArrayList<>();

        handleData(dto, billDO, serviceDO, insertModules, updateModules, optionRules);

        //修改和新增仅影响module，其他数据直接覆盖
        saveModule(insertModules);
        updateModules(updateModules);
        saveBill(dto, billDO);
        serviceService.saveService(serviceDO);
        billOptionService.insertBillOptionRules(optionRules);
    }

    /**
     * 保存bill
     *
     * @param dto
     * @param billDO
     * @throws Exception
     */
    private void saveBill(EntityAndServiceDTO dto, BillDO billDO) throws Exception {
        if (ModuleConstant.CLASS_ENTITY.equals(dto.getClassId())) {
            baseEntityService.insertBill(billDO);
        } else {
            billService.insertBill(billDO);
        }
    }

    /**
     * 更新 Module
     *
     * @param updateModules 要更新的module对象
     */
    private void updateModules(List<Module> updateModules) {
        if (CollectionUtils.isNotEmpty(updateModules)) {
            moduleService.bathUpdateModule(updateModules);
        }
    }

    private void saveModule(List<Module> insertModules) {
        if (CollectionUtils.isNotEmpty(insertModules)) {
            moduleService.bathInsertModule(insertModules);
        }
    }

    /**
     * 获取script模板
     *
     * @return 返回服务的script模板
     */
    public String getScriptDemo() {

        return serviceService.getScriptDemo();
    }


    /**
     * 根据实体label模糊搜索实体
     *
     * @param label 实体中文名称
     * @return 返回实体信息
     */
    public List<Map<String, Object>> searchEntities(String label) {
        List<Map<String, Object>> list = baseEntityService.searchEntities(label);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(map -> map.put("entityId", String.valueOf(map.get("entityId"))));
        }
        return list;
    }


    /**
     * 根据实体id获取字段信息
     *
     * @param id 实体id
     * @return 返回实体字段信息
     */
    public List<Map<String, Object>> getEntityFields(Long id) {

        return baseEntityService.getEntityFields(id);
    }


    /**
     * 获取service和action默认模板
     *
     * @return 返回service和action默认模板
     * @throws Exception http请求异常
     */
    public ServiceAndActionVO getCommonServiceAndAction() {
        return serviceService.getCommonServiceAndAction();
    }

    /**
     * 删除表单实体
     */
    public void deleteEntity(EntityDetailDTO dto) throws Exception {

        Module module = new Module();
        module.setClassId(dto.getClassId());
        module.setTypeId(dto.getTypeId());
        //判断module是否发布，发布则不能删除
        Module existModule = moduleService.getModuleByClassIdAndTypeId(module);
        if (Objects.equals(ModulePublishStatusEnum.published.getState(), existModule.getPublishState())) {
            throw new BusinessException(RetCode.INTERNAL_SERVER_ERROR, "实体已发布，不可删除");
        }
        moduleService.deleteModules(module);
    }

    /**
     * 前端dto数据转换成do模型
     *
     * @param dto           前端统一数据模型
     * @param billDO        实体DO对象
     * @param serviceDO     服务的DO对象
     * @param insertModules 作插入的Module集合
     * @param updateModules 作更新的Module集合
     * @param optionRules   下推或回写的操作规则
     * @throws Exception
     */
    private void handleData(EntityAndServiceDTO dto, BillDO billDO, ServiceDO serviceDO, List<Module> insertModules, List<Module> updateModules, List<BillOptionRules> optionRules) {
        Module module = getModule(dto, insertModules, updateModules, dto.getTypeId());
        long billId = SnowflakeIdWorker.nextId();

        generateBillAndPropertyList(module.getId(), billId, -1L, dto, billDO, null);
        generateFieldsProperties(billId, dto.getFields(), dto.getRules(), billDO);
        generateServiceList(module.getId(), dto, serviceDO);
        generateBillOptionRules(module.getId(), dto.getOptionRules(), optionRules);

        List<BillFieldsRulesDO> childBills = dto.getChildren();

        if (CollectionUtils.isNotEmpty(childBills)) {
            for (BillFieldsRulesDO childrenBillDTO : childBills) {
                //判断是否有子表，没有则添加，有则update
                Module newModule = createModule(dto.getClassId(), childrenBillDTO.getTypeId(), dto.getStatus());
                Module childModule = moduleService.getModuleByClassIdAndTypeId(newModule);
                if (Objects.nonNull(childModule)) {
                    updateModules.add(childModule);
                } else {
                    insertModules.add(newModule);
                }
                Long childBillId = SnowflakeIdWorker.nextId();
                generateBillAndPropertyList(module.getId(), childBillId, billId, dto, billDO, childrenBillDTO);
                generateFieldsProperties(childBillId, childrenBillDTO.getFields(), childrenBillDTO.getRules(), billDO);
            }
        }
    }

    private Module createModule(String classId, String typeId, Integer status) {
        Module newModule = new Module();
        newModule.setId(SnowflakeIdWorker.nextId());
        newModule.setClassId(classId.trim());
        newModule.setTypeId(typeId.trim());
        newModule.setPublishState(status);
        return newModule;
    }

    /**
     * 从 EntityAndServiceDTO dto 中获取module 对象，根据dto中的标记的插入或更新分别放入相应的集合中
     *
     * @param dto           前端统一数据模型
     * @param insertModules 作插入操作的module集合
     * @param updateModules 作更新操作的module集合
     * @param typeId        1:插入、2:更新
     * @return 从dto中组装好了的Model对象
     */
    private Module getModule(EntityAndServiceDTO dto, List<Module> insertModules, List<Module> updateModules, String typeId) {
        Module module = createModule(dto.getClassId(), typeId, dto.getStatus());
        if (Objects.equals(RequestEnum.INSERT_REQUEST.getValue(), dto.getType())) {
            insertModules.add(module);
        } else {
            updateModules.add(module);
        }

        return module;
    }

    /**
     * 生成实体以及添加实体的属性 并设置根据公司名设置包名
     *
     * @param moduleId        module的id
     * @param billId          实体id
     * @param parentId        实体的父实体的id
     * @param dto             前端统一数据模型
     * @param billDO          实体的DO对象
     * @param childrenBillDTO 实体的子实体的DO对象
     */
    private void generateBillAndPropertyList(Long moduleId, Long billId, Long parentId, EntityAndServiceDTO dto, BillDO billDO, BillFieldsRulesDO childrenBillDTO) {

        Bill bill = new Bill();
        bill.setId(billId);
        bill.setParentId(parentId);
        String company = userInfoManager.getUserInfo().getCompanySpace();
        bill.setName(String.format("%s_%s_%s", company, dto.getClassId().trim(), dto.getTypeId().trim()));
        bill.setLabel(dto.getTitle());
        bill.setModuleId(moduleId);
        if (Objects.nonNull(childrenBillDTO)) {
            bill.setForeignKey(childrenBillDTO.getForeignKey());
            bill.setPrimaryKey(childrenBillDTO.getPrimaryKey());
            bill.setName(String.format("%s_%s_%s", company, dto.getClassId(), childrenBillDTO.getTypeId().trim()));
            bill.setLabel(childrenBillDTO.getTitle());
        }
        billDO.getBills().add(bill);

        dto.setPackageName(company);
        for (Map.Entry entry : new BeanMap(dto).entrySet()) {
            if (ArrayUtils.contains(MoquiProperties.PROPERTIES, entry.getKey())) {
                BillProperty billProperty = new BillProperty();
                billProperty.setBillId(billId);
                billProperty.setElementValue(Objects.isNull(entry.getValue()) ? null : entry.getValue().toString());
                billProperty.setName(entry.getKey().toString().trim());
                billDO.getBillProperties().add(billProperty);
            }
        }
    }

    /**
     * 生成字段及其元数据
     *
     * @param id     单据id
     * @param fields 字段集合
     * @param rules  字段的规则集合
     * @param billDO 实体的DO对象
     */
    private void generateFieldsProperties(Long id, List<Map<String, Object>> fields, List<FieldRuleDO> rules, BillDO billDO) {
        Map<String, Object> fieldMap = getFieldRuleMap(rules);

        fields.forEach(map -> {
            Long fieldId = SnowflakeIdWorker.nextId();
            BillField billField = getBillField(id, map, fieldId);
            billField.setRules(JSON.toJSONString(fieldMap.get(billField.getFieldName())));
            billDO.getBillFields().add(billField);

            map.forEach((key, value) -> {
                //moqui属性
                if (ArrayUtils.contains(MoquiProperties.MOQUI_ELEMENTS, key)) {
                    dealMoquiAttr(billDO, map, fieldId, key, value);
                } else if (!Objects.equals("name", key) && !Objects.equals("refTarget", key)) {
                    Long billFieldWebPropertyId = SnowflakeIdWorker.nextId();
                    BillFieldWebProperty billFieldWebProperty = new BillFieldWebProperty();
                    billFieldWebProperty.setName(key);
                    billFieldWebProperty.setElementValue(Objects.nonNull(value) ? value.toString() : null);
                    billFieldWebProperty.setBillFieldId(fieldId);
                    billFieldWebProperty.setId(billFieldWebPropertyId);
                    billDO.getBillFieldWebProperties().add(billFieldWebProperty);
                }
            });
        });
    }

    /**
     * 处理实体的 moqui属性
     *
     * @param billDO  实体的DO对象
     * @param map     字段的属性map
     * @param fieldId 字段的id
     * @param key     要处理的字段属性
     * @param value   字段的属性值
     */
    private void dealMoquiAttr(BillDO billDO, Map<String, Object> map, Long fieldId, String key, Object value) {
        //属性是否为type
        if (Objects.equals("type", key) && Objects.equals("refObject", value)) {
            dealRefField(billDO, map, fieldId, key, value);
        } else {
            BillFieldProperty billFieldProperty = new BillFieldProperty();
            billFieldProperty.setElementValue(Objects.isNull(value) ? null : value.toString());
            billFieldProperty.setName(key);
            billFieldProperty.setFieldId(fieldId);
            billDO.getBillFieldProperties().add(billFieldProperty);
        }
    }

    /**
     * 处理引用字段(type)引用
     *
     * @param billDO  实体的DO对象
     * @param map     属性的map集合
     * @param fieldId 字段id
     * @param key     属性名
     * @param value   属性值
     */
    private void dealRefField(BillDO billDO, Map<String, Object> map, Long fieldId, String key, Object value) {
        BillFieldProperty billFieldProperty = new BillFieldProperty();
        billFieldProperty.setName(key);
        billFieldProperty.setFieldId(fieldId);
        ReferenceDO referenceDTO = JSON.toJavaObject((JSONObject) JSON.toJSON(map.get("refTarget")), ReferenceDO.class);
        billFieldProperty.setElementValue(referenceDTO.getKeyType());
        billDO.getBillFieldProperties().add(billFieldProperty);

        Long billFieldWebPropertyId = SnowflakeIdWorker.nextId();
        BillFieldWebProperty billFieldWebProperty = new BillFieldWebProperty();
        billFieldWebProperty.setName(key);
        billFieldWebProperty.setElementValue(value.toString());
        billFieldWebProperty.setBillFieldId(fieldId);
        billFieldWebProperty.setId(billFieldWebPropertyId);
        billDO.getBillFieldWebProperties().add(billFieldWebProperty);

        BillFieldWebPropertyRel billFieldWebPropertyRel = new BillFieldWebPropertyRel();
        billFieldWebPropertyRel.setFieldWebId(billFieldWebPropertyId);
        billFieldWebPropertyRel.setDestField(referenceDTO.getValueField());
        billFieldWebPropertyRel.setSrcField(referenceDTO.getKeyField());
        billFieldWebPropertyRel.setClassId(referenceDTO.getClassId());
        billFieldWebPropertyRel.setTypeId(referenceDTO.getTypeId());
        billFieldWebPropertyRel.setKeyType(referenceDTO.getKeyType());
        billDO.getBillFieldWebPropertyRels().add(billFieldWebPropertyRel);
    }

    /**
     * @param id      字段的id
     * @param map
     * @param fieldId
     * @return
     */
    private BillField getBillField(Long id, Map<String, Object> map, Long fieldId) {
        BillField billField = new BillField();
        billField.setFieldName(map.get("name").toString());
        billField.setBillId(id);
        billField.setId(fieldId);
        billField.setLabel(map.get("label").toString());
        return billField;
    }

    /**
     * @param rules 字段规则集合List 转map
     * @return
     */
    private Map<String, Object> getFieldRuleMap(List<FieldRuleDO> rules) {
        Map<String, Object> fieldMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(rules)) {
            rules.forEach(rule -> {
                if (Objects.nonNull(rule)) {
                    fieldMap.put(rule.getFieldName(), rule);
                }
            });
        }
        return fieldMap;
    }

    /**
     * 生成服务相关数据
     *
     * @param moduleId  moduleId
     * @param dto       前端统一数据模型
     * @param serviceDO 服务的DO实体
     */
    private void generateServiceList(Long moduleId, EntityAndServiceDTO dto, ServiceDO serviceDO) {
        List<ServicePropertyDO> serviceDTOS = dto.getServices();
        List<ActionDO> actionDTOS = dto.getActions();
        List<EventDO> billEecas = dto.getEvents();

        Map<String, Long> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(serviceDTOS)) {
            dealService(serviceDO, serviceDTOS, map);
        }
        if (CollectionUtils.isNotEmpty(actionDTOS)) {
            dealAction(moduleId, dto, serviceDO, actionDTOS, map);
        }
        if (CollectionUtils.isNotEmpty(billEecas)) {
            dealEecas(moduleId, serviceDO, billEecas, map);
        }
    }

    /**
     * 处理eeca
     *
     * @param moduleId  moduleId
     * @param serviceDO 服务的DO对象
     * @param billEecas 实体的Eecas对象
     * @param map
     */
    private void dealEecas(Long moduleId, ServiceDO serviceDO, List<EventDO> billEecas, Map<String, Long> map) {
        billEecas.forEach(billEecaDTO -> {
            long eecaId = SnowflakeIdWorker.nextId();
            BillEeca billEeca = new BillEeca();
            billEeca.setModuleId(moduleId);
            billEeca.setEventType(billEecaDTO.getEventType());
            //触发时机
            List<String> triggerList = new ArrayList<>();
            if (Objects.nonNull(billEecaDTO.getRunBefore()) && billEecaDTO.getRunBefore()) {
                triggerList.add(EecaPropertyEnum.RUN_BEFORE.getValue());
            }
            if (Objects.nonNull(billEecaDTO.getRunOnError()) && billEecaDTO.getRunOnError()) {
                triggerList.add(EecaPropertyEnum.RUN_ON_ERROR.getValue());
            }
            billEeca.setETrigger(JSON.toJSONString(triggerList));
            //其他选项
            List<String> options = new ArrayList<>();
            if (Objects.nonNull(billEecaDTO.getGetEntireEntity()) && billEecaDTO.getGetEntireEntity()) {
                options.add(EecaPropertyEnum.GET_ENTIRE_ENTITY.getValue());
            }
            if (Objects.nonNull(billEecaDTO.getGetOriginalValue()) && billEecaDTO.getGetOriginalValue()) {
                options.add(EecaPropertyEnum.GET_ORIGINAL_VALUE.getValue());
            }
            if (Objects.nonNull(billEecaDTO.getSetResults()) && billEecaDTO.getSetResults()) {
                options.add(EecaPropertyEnum.SET_RESULTS.getValue());
            }
            billEeca.setOtherOptions(JSON.toJSONString(options));
            billEeca.setId(eecaId);
            billEeca.setEecaId(billEecaDTO.getEventName());
            billEeca.setLabel(billEecaDTO.getLabel());
            billEeca.setECondition(billEecaDTO.getCondition());
            billEeca.setEMethod(JSON.toJSONString(billEecaDTO.getMethods()));
            serviceDO.getBillEecas().add(billEeca);

            //eeca与service关联
            if (Objects.nonNull(billEecaDTO.getServiceId()) || StringUtils.isNotBlank(billEecaDTO.getServiceName())) {
                EecaService eecaService = new EecaService();
                eecaService.setEecaId(eecaId);
                eecaService.setPriority(0);
                Long serviceId = Objects.nonNull(billEecaDTO.getServiceId()) ? billEecaDTO.getServiceId() : map.get(billEecaDTO.getServiceName());
                eecaService.setServiceId(serviceId);
                serviceDO.getEecaServices().add(eecaService);
            }
        });
    }

    /**
     * 处理Action
     *
     * @param moduleId   moduleId
     * @param dto        前端统一数据模型
     * @param serviceDO  服务的DO对象
     * @param actionDTOS 行为的集合
     * @param map        服务名-服务id的map集合
     */
    private void dealAction(Long moduleId, EntityAndServiceDTO dto, ServiceDO serviceDO, List<ActionDO> actionDTOS, Map<String, Long> map) {
        actionDTOS.forEach(actionDTO -> {
            Long actionId = SnowflakeIdWorker.nextId();
            String company = userInfoManager.getUserInfo().getCompanySpace();
            String entityName = String.format("%s_%s_%s", company, dto.getClassId().trim(), dto.getTypeId().trim());
            String rpcUri = String.format("%s/%s/%s", pre, entityName, actionDTO.getServiceName().trim());
            BillAction billAction = new BillAction();
            billAction.setButton(actionDTO.getActionName());
            billAction.setId(actionId);
            billAction.setLabel(actionDTO.getLabel());
            billAction.setRpcMethod(MethodEnum.POST.value.toLowerCase());
            billAction.setRpcUri(rpcUri);
            billAction.setModuleId(moduleId);
            billAction.setAfterService(actionDTO.getAfterService());
            billAction.setBeforeService(actionDTO.getBeforeService());
            billAction.setIcon(actionDTO.getIcon());
            billAction.setDisableType(actionDTO.getDisableType());
            billAction.setRoleAuthorization(actionDTO.getRoleAuthorization());
            serviceDO.getBillActions().add(billAction);

            addActionService(serviceDO, map, actionDTO, actionId);
        });
    }

    /**
     * @param serviceDO 服务的DO对象
     * @param map       服务名-服务id的map集合
     * @param actionDTO 行为的DTO对象
     * @param actionId  行为的id
     */
    private void addActionService(ServiceDO serviceDO, Map<String, Long> map, ActionDO actionDTO, Long actionId) {
        if (StringUtils.isNotBlank(actionDTO.getServiceName())) {
            ActionService actionService = new ActionService();
            actionService.setServiceId(map.get(actionDTO.getServiceName()));
            actionService.setActionId(actionId);
            serviceDO.getActionServices().add(actionService);
        }
    }

    /**
     * 处理服务 并把服务的服务名和服务的id放入map
     *
     * @param serviceDO   服务的DO对象
     * @param serviceDTOS 服务的DTO集合
     * @param map         * @param map       服务名-服务id的map集合 在此为其添加元素
     */
    private void dealService(ServiceDO serviceDO, List<ServicePropertyDO> serviceDTOS, Map<String, Long> map) {
        serviceDTOS.forEach(serviceDTO -> {
            Long serviceId = SnowflakeIdWorker.nextId();
            Service service = getService(serviceDTO, serviceId);
            setVerb(serviceDTO, service);

            serviceDO.getServices().add(service);
            serviceDTO.setId(serviceId);
            map.put(serviceDTO.getServiceName(), serviceDTO.getId());
        });
    }

    /**
     * 如果serviceDTO没有服务的verb就取服务名作为verb
     *
     * @param serviceDTO 服务的dto对象
     * @param service    需要设置verb的服务对象
     */
    private void setVerb(ServicePropertyDO serviceDTO, Service service) {
        if (null != serviceDTO.getVerb() && serviceDTO.getVerb().length() > 0) {
            service.setVerb(serviceDTO.getVerb());
        } else {
            service.setVerb(serviceDTO.getServiceName());
        }
    }

    /**
     * 根据ServicePropertyDO serviceDTO 创建一个Service对象
     *
     * @param serviceDTO 服务DTO对象
     * @param serviceId  服务的id
     * @return 创建好的服务对象
     */
    private Service getService(ServicePropertyDO serviceDTO, Long serviceId) {
        Service service = new Service();
        service.setLabel(serviceDTO.getLabel());
        service.setId(serviceId);
        service.setServiceName(serviceDTO.getServiceName());
        service.setNoun(serviceDTO.getNoun());
        service.setScript(serviceDTO.getScript());
        service.setRoleAuthorization(serviceDTO.getRoleAuthorization());
        service.setServiceType(serviceDTO.getServiceType());
        return service;
    }

    /**
     * 获取单据下推及回写规则列表
     *
     * @param moduleId    moduleId
     * @param optionVO    操作的vo对象集合
     * @param optionRules 实体操作的字段映射规则
     */
    private void generateBillOptionRules(Long moduleId, List<OptionRules> optionVO, List<BillOptionRules> optionRules) {
        if (CollectionUtils.isNotEmpty(optionVO)) {
            optionVO.forEach(ruleCell -> {
                BillOptionRules rule = new BillOptionRules();
                rule.setModuleId(moduleId);
                rule.setOptionRules(JSON.toJSONString(ruleCell));
                optionRules.add(rule);
            });
        }
    }

    //将moqui字段属性转变成前段渲染所需
    private String moquiElementToWeb(String typeName) {

        if (ArrayUtils.contains(MoquiProperties.NUMBERS, typeName)) {
            return WebTypeEnum.WEB_NUMBER.getValue();
        }
        if (ArrayUtils.contains(MoquiProperties.DATES, typeName)) {
            return WebTypeEnum.WEB_DATA.getValue();
        }
        if (ArrayUtils.contains(MoquiProperties.TEXTS, typeName)) {
            return WebTypeEnum.WEB_TEXT.getValue();
        }
        if (ArrayUtils.contains(MoquiProperties.SEQUENCES, typeName)) {
            return WebTypeEnum.WEB_SEQUENCE.getValue();
        }
        if (Objects.equals(MoquiProperties.MOQUI_FILE, typeName)) {
            return WebTypeEnum.WEB_FILE.getValue();
        }
        return typeName;
    }


    @Transactional(rollbackFor = Exception.class)
    public void handleViewAndService(EntityAndServiceDTO dto) throws Exception {
        //校验数据
        EntityValidateUtils.validate(dto);
        //处理数据
        Module module = new Module();
        ViewDO viewDo = new ViewDO();
        ServiceDO serviceDO = new ServiceDO();
        handleViewData(dto, module, viewDo, serviceDO);

        if (Objects.equals(dto.getType(), RequestEnum.INSERT_REQUEST.getValue())) {
            moduleService.insertModule(module);
        } else {
            moduleService.updateModule(module);
        }
        viewService.insertView(viewDo);
        serviceService.saveService(serviceDO);

    }

    private void handleViewData(EntityAndServiceDTO dto, Module module, ViewDO viewDo, ServiceDO serviceDO) throws Exception {

        module.setId(SnowflakeIdWorker.nextId());
        module.setClassId(dto.getClassId());
        module.setTypeId(dto.getTypeId());
        module.setPublishState(dto.getStatus());

        handleView(dto, viewDo, module.getId());
        viewGenerateServiceList(module.getId(), dto, serviceDO);
    }

    //组装数据
    private void handleView(EntityAndServiceDTO dto, ViewDO viewDo, long moduleId) throws Exception {

        List<Map<String, Object>> fields = dto.getFields();
        //entityName命名
        String entityName = dto.getTypeId();
        String packagePath = userInfoManager.getUserInfo().getCompanySpace();
        long viewParentId = SnowflakeIdWorker.nextId();

        //设置视图主表
        viewDo.getViewParent().setId(viewParentId);
        viewDo.getViewParent().setEntityName(entityName);
        viewDo.getViewParent().setPackagePath(packagePath);
        viewDo.getViewParent().setModuleId(moduleId);
        viewDo.getViewParent().setTitle(dto.getTitle());
        viewDo.getViewParent().setCache(dto.getCache());
//        viewDo.getViewParent().setTransaction(dto.getTransaction());//entity 没有 transaction属性暂时注释

        Map<String, Object> fieldMap = new HashMap<>();
        List<FieldRuleDO> rules = dto.getRules();
        if (CollectionUtils.isNotEmpty(rules)) {
            rules.forEach(rule -> {
                fieldMap.put(rule.getFieldName(), rule);
            });
        }


        for (Map<String, Object> map : fields) {
            ViewSon viewSon = new ViewSon();
            long viewSonId = SnowflakeIdWorker.nextId();
            //设置视图子表
            viewSon.setId(viewSonId);
            viewSon.setMasterId(viewParentId);
            viewSon.setEntityName(map.get("entityName").toString());
            viewSon.setEntityAlias(map.get("entityAlias").toString());
            if (map.get("joinFromAlias") != null) {
                viewSon.setJoinFromAlias(map.get("joinFromAlias").toString());
            }
            if (map.get("entityParentValue") != null) {
                viewSon.setEntityParentValue(map.get("entityParentValue").toString());
            }
            if (map.get("entityValue") != null) {
                viewSon.setEntityValue(map.get("entityValue").toString());
            }
            viewDo.getViewSonList().add(viewSon);
            if (map.get("viewField") != null) {
                List<Map<String, String>> viewFields = (List<Map<String, String>>) map.get("viewField");
                for (Map<String, String> mapField : viewFields) {
                    ViewField viewField = new ViewField();
                    long viewFieldId = SnowflakeIdWorker.nextId();
                    //设置视图子表列
                    viewField.setId(viewFieldId);
                    viewField.setViewId(viewSonId);
                    viewField.setName(mapField.get("name"));
                    viewField.setField(mapField.get("field"));
                    viewField.setRules(JSON.toJSONString(fieldMap.get(viewField.getName())));
                    viewField.setAlias(viewSon.getEntityAlias());
                    viewField.setDescription(mapField.get("description"));
                    viewField.setExpresion(mapField.get("expresion"));
                    viewDo.getViewFieldList().add(viewField);
                }
            }
        }
    }

    //生成服务相关数据
    private void viewGenerateServiceList(Long moduleId, EntityAndServiceDTO dto, ServiceDO serviceDO) {

        List<ServicePropertyDO> serviceDTOS = dto.getServices();
        List<ActionDO> actionDTOS = dto.getActions();
        List<EventDO> billEecas = dto.getEvents();

        Map<String, Long> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(serviceDTOS)) {
            serviceDTOS.forEach(serviceDTO -> {
                if (Objects.isNull(serviceDTO.getId())) {
                    Long serviceId = SnowflakeIdWorker.nextId();
                    String location = XmlConstant.XML_LOCATION + dto.getClassId() + "/" + dto.getTypeId() + "/" + "get" + dto.getTypeId() + ".groovy";
                    Service service = new Service();
                    service.setLocation(location);
                    service.setLabel(serviceDTO.getLabel());
                    service.setId(serviceId);
                    service.setServiceName(serviceDTO.getServiceName());
                    //服务名词是实体名
                    service.setNoun(dto.getTypeId());
                    service.setScript(serviceDTO.getScript());
                    service.setVerb(serviceDTO.getVerb());
                    serviceDO.getServices().add(service);
                    serviceDTO.setId(serviceId);
                }
                map.put(serviceDTO.getServiceName(), serviceDTO.getId());
            });
        }

        if (CollectionUtils.isNotEmpty(actionDTOS)) {
            actionDTOS.forEach(actionDTO -> {
                String company = userInfoManager.getUserInfo().getCompanySpace();
                String entityName = String.format("%s_%s_%s", company, dto.getClassId(), dto.getTypeId());
                String rpcUri = String.format("%s/%s", entityName, actionDTO.getServiceName());
                Long actionId = SnowflakeIdWorker.nextId();
                BillAction billAction = new BillAction();
                billAction.setButton(actionDTO.getActionName());
                billAction.setId(actionId);
                billAction.setLabel(actionDTO.getLabel());
                billAction.setRpcMethod(actionDTO.getRpcMethod());
                billAction.setRpcUri(rpcUri);
                billAction.setModuleId(moduleId);
                billAction.setAfterService(actionDTO.getAfterService());
                billAction.setBeforeService(actionDTO.getBeforeService());
                billAction.setIcon(actionDTO.getIcon());
                serviceDO.getBillActions().add(billAction);

                addActionService(serviceDO, map, actionDTO, actionId);
            });
        }

    }
}
