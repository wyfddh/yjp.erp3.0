package com.yjp.erp.service.parsexml.handle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yjp.erp.conf.UserInfoManager;
import com.yjp.erp.conf.exception.BaseException;
import com.yjp.erp.conf.exception.BusinessException;
import com.yjp.erp.constants.ModuleConstant;
import com.yjp.erp.constants.XmlConstant;
import com.yjp.erp.model.domain.ModelServiceScript;
import com.yjp.erp.model.po.bill.Bill;
import com.yjp.erp.model.po.bill.BillField;
import com.yjp.erp.model.po.bill.BillFieldProperty;
import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.po.eeca.BillEeca;
import com.yjp.erp.model.po.service.BillAction;
import com.yjp.erp.model.po.service.Service;
import com.yjp.erp.model.po.view.ViewAlias;
import com.yjp.erp.model.po.view.ViewEntity;
import com.yjp.erp.model.po.view.ViewMemberEntity;
import com.yjp.erp.service.BillService;
import com.yjp.erp.service.ModuleService;
import com.yjp.erp.service.parsexml.ParseXmlService;
import com.yjp.erp.service.parsexml.constant.ParseXmlConstant;
import com.yjp.erp.service.parsexml.model.EntityParseMember;
import com.yjp.erp.service.parsexml.model.ViewEntityParseMember;
import com.yjp.erp.service.parsexml.service.*;
import com.yjp.erp.service.parsexml.util.PathUtils;
import com.yjp.erp.service.view.ViewAliasesService;
import com.yjp.erp.service.view.ViewEntityService;
import com.yjp.erp.service.view.ViewMemberEntityService;
import com.yjp.erp.util.DomUtils;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.dom.DOMDocument;
import org.dom4j.dom.DOMText;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/23 0:41
 * @Email: jianghongping@yijiupi.com
 */
@Component
@Slf4j
public class ParseXmlServiceHandle {
    @Resource
    private ModuleService moduleService;
    @Resource
    private BillService billService;
    @Resource
    ServiceServiceP serviceServiceP;
    @Resource
    private ActionService actionService;
    @Resource
    private EecaService eecaService;
    @Resource
    private ParseXmlService parseXmlService;
    @Resource
    private BillFieldService billFieldService;
    @Resource
    private BillFieldPropertyService billFieldPropertyService;
    @Resource
    private BillPropertyService billPropertyService;
    @Resource
    private ModelServiceScriptService modelServiceScriptService;
    @Resource
    private ViewEntityService viewEntityService;
    @Resource
    private ViewMemberEntityService viewMemberEntityService;
    @Resource
    private ViewAliasesService viewAliasesService;
    @Resource
    private UserInfoManager userInfoManager;


    public void exeParse(Module simpleModule) throws BaseException {
        if (ModuleConstant.CLASS_VIEW.equals(simpleModule.getClassId())) {
            parseViewEntity(simpleModule);
            return;
        }
        parseEntity(simpleModule);
    }

    /**
     * 解析视图实体
     *
     * @param simpleModule
     * @throws BaseException
     */
    private void parseViewEntity(Module simpleModule) throws BaseException {
        ViewEntityParseMember viewEntityParseMember = getViewEntityParseMember(simpleModule);
        parseViewEntityParseMember(viewEntityParseMember);
    }

    /**
     * 把ViewEntityParseMember 解析成xml
     *
     * @param entityParseMember
     */
    private void parseViewEntityParseMember(ViewEntityParseMember entityParseMember) throws BaseException {
        Element entities = DomUtils.getRootElement("entities", XmlConstant.XML_HEAD_ENTITY_VALUE);
        String company = userInfoManager.getUserInfo().getCompanySpace();
        Element viewEntity = DomUtils.getElement("view-entity")
                .addAttribute("package", company)
                .addAttribute("entity-name", entityParseMember.getEntityName())
                .addAttribute("authorize-skip", "true")
                .addAttribute("auto-clear-cache", entityParseMember.getAutoClearCache())
                .addAttribute("cache", entityParseMember.getCache());

        addMemberToViewEntityElement(entityParseMember, viewEntity);
        addAliasToViewEntityElement(entityParseMember, viewEntity);

        entities.add(viewEntity);
        Document dom = new DOMDocument();
        dom.add(entities);

        String entityFilePath = PathUtils.getEntityFileAbsolutePath();
        String viewEntityFileName = PathUtils.getViewEntityXmlFileName(entityParseMember.getEntityName());
        DomUtils.outputDom(dom, entityFilePath, viewEntityFileName);
    }

    /**
     * 视图实体Element 中加入视图字段的Element
     *
     * @param entityParseMember
     * @param viewEntity
     */
    private void addAliasToViewEntityElement(ViewEntityParseMember entityParseMember, Element viewEntity) {
        entityParseMember.getAliases().forEach(alia -> {
            Element alias = DomUtils.getElement("alias")
                    .addAttribute("name", alia.getName())
                    .addAttribute("entity-alias", alia.getEntityAlias())
                    .addAttribute("field", alia.getField())
                    .addAttribute("type", alia.getType());

            Element description = DomUtils.getElement("description");
            description.add(new DOMText(alia.getDescription()));
            alias.add(description);

            String expression = alia.getExpression();
            if (null != expression && expression.length() > 0) {
                Element complexAlias = DomUtils.getElement("complex-alias").addAttribute("expression", alia.getExpression());
                alias.add(complexAlias);
            }
            viewEntity.add(alias);
        });
    }

    /**
     * 视图实体Element 中加入视图成员实体的Element
     *
     * @param entityParseMember
     * @param viewEntity
     */
    private void addMemberToViewEntityElement(ViewEntityParseMember entityParseMember, Element viewEntity) {
        entityParseMember.getMemberEntities().forEach(memberEntity -> {
            Element viewMemberEntity = DomUtils.getElement("member-entity")
                    .addAttribute("entity-alias", memberEntity.getEntityAlias())
                    .addAttribute("entity-name", memberEntity.getEntityName());
            String joinFromAlias = memberEntity.getJoinFromAlias();
            if (null != joinFromAlias && joinFromAlias.length() > 0) {
                viewMemberEntity.addAttribute("join-from-alias", memberEntity.getJoinFromAlias());
            }

            String relatedField = memberEntity.getFieldName();
            if (null != relatedField && relatedField.length() > 0) {
                Element keyMap = DomUtils.getElement("key-map")
                        .addAttribute("field-name", memberEntity.getFieldName())
                        .addAttribute("related", memberEntity.getRelated());

                viewMemberEntity.add(keyMap);
            }

            viewEntity.add(viewMemberEntity);
        });
    }

    /**
     * 获取 视图实体成员
     *
     * @param simpleModule
     * @return
     */
    private ViewEntityParseMember getViewEntityParseMember(Module simpleModule) {
        Module module = moduleService.getModuleByClassIdAndTypeId(simpleModule);

        ViewEntityParseMember viewEntityParseMember = new ViewEntityParseMember();
        ViewEntity viewEntity = viewEntityService.getViewEntityByModule(module.getId());

        addViewEntity(viewEntity, viewEntityParseMember);

        addViewMemberAndAlias(viewEntity, viewEntityParseMember);

        return viewEntityParseMember;
    }

    /**
     * 给 ViewEntityParseMember 加入视图成员和视图字段
     *
     * @param viewEntity
     * @param viewEntityParseMember
     */
    private void addViewMemberAndAlias(ViewEntity viewEntity, ViewEntityParseMember viewEntityParseMember) {
        List<ViewEntityParseMember.MemberEntity> memberEntities = new ArrayList<>();
        List<ViewEntityParseMember.Alias> alias = new ArrayList<>();

        List<ViewMemberEntity> viewMemberEntities = viewMemberEntityService.getViewMemberEntitiesByViewEntityId(viewEntity.getId());
        viewMemberEntities.forEach(viewMemberEntity -> {
            ViewEntityParseMember.MemberEntity memberEntity = viewEntityParseMember.new MemberEntity();
            memberEntity.setEntityAlias(viewMemberEntity.getEntityAlias());
            memberEntity.setEntityName(viewMemberEntity.getEntityName());
            memberEntity.setJoinFromAlias(viewMemberEntity.getJoinFromAlias());
            memberEntity.setFieldName(viewMemberEntity.getFieldName());
            memberEntity.setRelated(viewMemberEntity.getRelated());
            memberEntities.add(memberEntity);

            List<ViewAlias> viewAlias = viewAliasesService.getViewAliasByViewMemberId(viewMemberEntity.getId());
            viewAlias.forEach(viewAlia -> {
                ViewEntityParseMember.Alias alia = viewEntityParseMember.new Alias();
                alia.setName(viewAlia.getName());
                alia.setEntityAlias(viewAlia.getEntityAlias());
                alia.setType(viewAlia.getKeyType());
                alia.setField(viewAlia.getField());
                alia.setExpression(viewAlia.getExpression());
                alia.setDescription(viewAlia.getDescription());
                alias.add(alia);
            });
        });
        viewEntityParseMember.setMemberEntities(memberEntities);
        viewEntityParseMember.setAliases(alias);
    }

    /**
     * 给 ViewEntityParseMember 中加入ViewEntity
     *
     * @param viewEntity
     * @param viewEntityParseMember
     */
    private void addViewEntity(ViewEntity viewEntity, ViewEntityParseMember viewEntityParseMember) {
        viewEntityParseMember.setEntityName(viewEntity.getEntityName());
        viewEntityParseMember.setPackageName(viewEntity.getEntityPackage());
        viewEntityParseMember.setAuthorizeSkip(viewEntity.getAuthorizeSkip());
        viewEntityParseMember.setAutoClearCache(viewEntity.getAutoClearCache());
    }

    /**
     * 解析普通实体
     *
     * @param simpleModule
     * @throws BaseException
     */
    private void parseEntity(Module simpleModule) throws BaseException {
        EntityParseMember entityParseMember = getEntityParseMember(simpleModule);
        parseXmlService.parseEntityParseMember(entityParseMember);
    }

    public EntityParseMember getEntityParseMember(Module simpleModule) throws BusinessException {
        EntityParseMember entityParseMember = new EntityParseMember();
        Module module = moduleService.getModuleByClassIdAndTypeId(simpleModule);
        if (null == module) {
            throw new BusinessException("此simpleModule无对应的Module");
        }
        Bill bill = getBillByModule(module);
        Bill childBill = getBillChildByModule(bill.getId());

        //todo 默认的实体名都是加了包名的
        entityParseMember.setEntityName(bill.getName());
        if(null!=childBill){
            entityParseMember.setChildEntityName(childBill.getName());
        }
        List<EntityParseMember.ElementAttribute> entityAttributes = getEntityAttributes(entityParseMember, bill);

        if(null!=childBill){
            List<EntityParseMember.ElementAttribute> childEntityAttributes = getEntityAttributes(entityParseMember, childBill);
            List<EntityParseMember.EntityField> childEntityFields = generateEntityFields(entityParseMember, childBill);
            entityParseMember.setChildEntityAttributes(childEntityAttributes);
            entityParseMember.setChildEntityFields(childEntityFields);
        }

        List<EntityParseMember.EntityField> entityFields = generateEntityFields(entityParseMember, bill);
        List<EntityParseMember.EntityService> entityServices = generateEntityServices(entityParseMember, module, bill);
        List<EntityParseMember.EntityAction> entityActions = generateActions(entityParseMember, module, bill);
        List<EntityParseMember.EntityEeca> entityEecas = generateEecas(entityParseMember, module, bill);

        entityParseMember.setEntityAttributes(entityAttributes);
        entityParseMember.setEntityFields(entityFields);
        entityParseMember.setEntityServices(entityServices);
        entityParseMember.setEntityActions(entityActions);
        entityParseMember.setEntityEecas(entityEecas);

        return entityParseMember;
    }

    private List<EntityParseMember.ElementAttribute> getEntityAttributes(EntityParseMember entityParseMember, Bill bill) {
        List<EntityParseMember.ElementAttribute> attributes = new ArrayList<>();
        billPropertyService.getBillProperiesByBillId(bill.getId()).forEach(billProperty -> {
            EntityParseMember.ElementAttribute elementAttribute = entityParseMember.new ElementAttribute();
            elementAttribute.setName(billProperty.getName());
            elementAttribute.setValue(billProperty.getElementValue());
            attributes.add(elementAttribute);
        });
        return attributes;
    }

    /**
     * 生成字段
     *
     * @param entityParseMember
     * @param bill
     * @return
     */
    private List<EntityParseMember.EntityField> generateEntityFields(EntityParseMember entityParseMember, Bill bill) {
        List<EntityParseMember.EntityField> entityFields = new ArrayList<>();
        List<BillField> billFields = billFieldService.listBillFieldsByBillId(bill.getId());
        billFields.forEach(billField -> {
            EntityParseMember.EntityField entityField = entityParseMember.new EntityField();
            List<EntityParseMember.ElementAttribute> elementAttributes = getElementAttributes(billField.getId(), entityParseMember);

            entityField.setFieldName(billField.getFieldName());
            entityField.setLable(billField.getLabel());
            entityField.setElementAttributes(elementAttributes);
            entityFields.add(entityField);
        });
        return entityFields;
    }

    /**
     * 获取字段的属性值
     *
     * @param fieldId
     * @param entityParseMember
     * @return
     */
    private List<EntityParseMember.ElementAttribute> getElementAttributes(Long fieldId, EntityParseMember entityParseMember) {
        List<EntityParseMember.ElementAttribute> fieldAttributes = new ArrayList<>();
        List<BillFieldProperty> billFieldProperties = billFieldPropertyService.getBillFieldPropertiesByFieldId(fieldId);
        billFieldProperties.forEach(billFieldProperty -> {
            EntityParseMember.ElementAttribute elementAttribute = entityParseMember.new ElementAttribute();
            elementAttribute.setName(billFieldProperty.getName());
            elementAttribute.setValue(billFieldProperty.getElementValue());
            fieldAttributes.add(elementAttribute);
        });
        return fieldAttributes;
    }

    private List<EntityParseMember.EntityEeca> generateEecas(EntityParseMember entityParseMember, Module module, Bill bill) {
        List<BillEeca> billEecas = eecaService.listBillEecaByModuleId(module.getId());
        List<EntityParseMember.EntityEeca> entityEecas = new ArrayList<>();
        billEecas.forEach(billEeca -> {
            EntityParseMember.EntityEeca entityEeca = entityParseMember.new EntityEeca();
            billEeca.getServiceName();
            //todo 元数据中的eecaId 中存的是eeca调用的服务名
            entityEeca.setCallServiceUrl(PathUtils.getServiceCallUrl(bill.getName(), billEeca.getEecaId()));
            entityEeca.setEntityName(bill.getName());
            List<String> triggers = getTriggers(billEeca);
            entityEeca.setTriggers(triggers);
        });
        return entityEecas;
    }

    /**
     * 从billEeca中获取trigger的集合
     *
     * @param billEeca
     * @return
     */
    private List<String> getTriggers(BillEeca billEeca) {
        //todo 把元数据表中的trigger与method对调
        JSONArray methodArray = JSON.parseArray(billEeca.getEMethod());
        List<String> triggers = new ArrayList<>();
        methodArray.forEach(method -> {
            triggers.add((String) method);
        });
        return triggers;
    }

    /**
     * 生成Action解析前的数据
     *
     * @param entityParseMember
     * @param module
     * @param bill
     * @return
     */
    private List<EntityParseMember.EntityAction> generateActions(EntityParseMember entityParseMember, Module module, Bill bill) {
        List<BillAction> billActions = actionService.listAcitonByMoudleId(module.getId());
        List<EntityParseMember.EntityAction> entityActions = new ArrayList<>();
        billActions.forEach(billAction -> {
            EntityParseMember.EntityAction entityAction = entityParseMember.new EntityAction();
            entityAction.setFirstResource(bill.getName());
            //todo 表中服务名怎么叫button
            String serviceName = billAction.getButton();
            String callServiceUrl = PathUtils.getServiceCallUrl(bill.getName(), serviceName);
            entityAction.setCallServiceUrl(callServiceUrl);
            entityAction.setSecondResource(serviceName);
            entityActions.add(entityAction);
        });
        return entityActions;
    }

    /**
     * 生成 List<EntityParseMember.EntityService>
     *
     * @param entityParseMember
     * @param module
     * @param bill
     * @return
     */
    private List<EntityParseMember.EntityService> generateEntityServices(EntityParseMember entityParseMember, Module module, Bill bill) {
        List<EntityParseMember.EntityService> entityServices = new ArrayList<>();
        List<Service> services = serviceServiceP.listServiceByModuleId(module.getId());
        services.forEach(service -> {
            String location = getScriptLocation(bill, service);
            EntityParseMember.EntityService entityService = entityParseMember.new EntityService();

            entityService.setLocation(location);
            entityService.setVerb(service.getVerb());
            entityService.setScript(service.getScript());
            entityServices.add(entityService);
        });
        return entityServices;
    }

    /**
     * 如果是是通过脚本是就从通过脚本
     *
     * @param bill
     * @param service
     * @return
     */
    private String getScriptLocation(Bill bill, Service service) {
        AtomicReference<String> location = new AtomicReference<>();
        if (ParseXmlConstant.SERVICE_TYPE_COMMON.equals(service.getServiceType())) {
            List<ModelServiceScript> allModelServiceScript = modelServiceScriptService.getAllModelServiceScript();
            allModelServiceScript.forEach(modelServiceScript -> {
                if (modelServiceScript.getServiceName().equals(service.getServiceName())) {
                    location.set(modelServiceScript.getScriptLocation());
                }
            });
            if (null == location.get()) {
                new BaseException("通过脚本表中未发现此通用服务脚本:" + service.getServiceName());
            }
        } else {
            location.set(PathUtils.getRelatedScriptLocation(bill.getName(), service.getServiceName()));
        }
        return location.get();
    }

    private Bill getBillByModule(Module module) throws BusinessException {
        Bill bill = billService.getBillByModuleId(module.getId());
        if (null == bill) {
            throw new BusinessException("未查询到此module的实体module的值:" + module);
        }
        return bill;
    }

    private Bill getBillChildByModule(Long parentBillId) throws BusinessException {
        Bill bill = billService.getChildBillByMainId(parentBillId);
        if (null == bill) {
            log.info("未查询到单据此bill的子表" + parentBillId);
        }
        return bill;
    }
}
