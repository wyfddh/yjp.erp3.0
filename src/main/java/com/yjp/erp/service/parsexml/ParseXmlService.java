package com.yjp.erp.service.parsexml;

import com.yjp.erp.conf.exception.BaseException;
import com.yjp.erp.constants.XmlConstant;
import com.yjp.erp.service.parsexml.model.EntityParseMember;
import com.yjp.erp.service.parsexml.util.PathUtils;
import com.yjp.erp.util.DomUtils;
import com.yjp.erp.util.FileUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.dom.DOMText;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/22 20:06
 * @Email: jianghongping@yijiupi.com
 */
@Service
public class ParseXmlService {
    @Value("${xml.create.path}")
    private String baseXmlPath;

    /**
     * 把实体的所有成员转成xml
     *
     * @param entityParseMember
     * @throws BaseException
     */
    public void parseEntityParseMember(EntityParseMember entityParseMember) throws BaseException {
        parseEntity(entityParseMember);
        parseServices(entityParseMember.getEntityServices(), entityParseMember.getEntityName());
        parseActions(entityParseMember.getEntityActions(), entityParseMember.getEntityName());
        parseEecas(entityParseMember.getEntityEecas(), entityParseMember.getEntityName());
    }

    /**
     * 解析实体
     *
     * @param entityParseMember
     * @throws BaseException
     */
    public void parseEntity(EntityParseMember entityParseMember) throws BaseException {
        String entityName = entityParseMember.getEntityName();
        String childEntityName = entityParseMember.getChildEntityName();
        List<EntityParseMember.ElementAttribute> entityAttribute = entityParseMember.getEntityAttributes();
        List<EntityParseMember.ElementAttribute> childEntityAttribute = entityParseMember.getChildEntityAttributes();
        List<EntityParseMember.EntityField> entityFields = entityParseMember.getEntityFields();
        List<EntityParseMember.EntityField> childEntityFields = entityParseMember.getChildEntityFields();
        Element entities = DomUtils.getRootElement("entities", XmlConstant.XML_HEAD_ENTITY_VALUE);

        addEntity(entityFields, entityAttribute, entityName, entities);
        if(null!=childEntityFields){
            addEntity(childEntityFields, childEntityAttribute, childEntityName, entities);
        }

        Document document = DomUtils.getDocument();
        document.add(entities);
        DomUtils.outputDom(document, PathUtils.getAbsoluteEntityPath(entityName), PathUtils.getEntityFileName(entityName) + ".xml");
    }

    /**
     * 把entity 加入entities
     *
     * @param entityFields
     * @param entities
     */
    private void addEntity(List<EntityParseMember.EntityField> entityFields, List<EntityParseMember.ElementAttribute> elementAttributes, String entityName, Element entities) {
        //todo entity 设置属性
        Element entity = DomUtils.getElement("entity");
        entity.addAttribute("entity-name", entityName);
        elementAttributes.forEach(elementAttribute -> {
            entity.addAttribute(elementAttribute.getName(), elementAttribute.getValue());
        });

        entityFields.forEach(entityField -> {
            Element field = DomUtils.getElement("field");
            field.addAttribute("name", entityField.getFieldName());
            entityField.getElementAttributes().forEach(fieldAttribute -> {
                field.addAttribute(fieldAttribute.getName(), fieldAttribute.getValue());
            });
            Element description = DomUtils.getElement("description");
            description.add(new DOMText(entityField.getLable()));
            field.add(description);
            entity.add(field);
        });
        entities.add(entity);
    }

    /**
     * 解析服务
     *
     * @param entityServices
     */
    public void parseServices(List<EntityParseMember.EntityService> entityServices, String entityName) throws BaseException {
        Element services = generateServicesElement(entityServices);
        Document document = DomUtils.getDocument();
        document.add(services);
        DomUtils.outputDom(document, PathUtils.getAbsoluteServicePath(entityName), PathUtils.getServiceFileName(entityName) + ".xml");
    }

    /**
     * 解析行为
     *
     * @param entityActions
     */
    public void parseActions(List<EntityParseMember.EntityAction> entityActions, String entityName) throws BaseException {
        Element actions = generateActionsElement(entityActions, entityName);
        Document document = DomUtils.getDocument();
        document.add(actions);
        DomUtils.outputDom(document, PathUtils.getServceFileAbsolutePath(), PathUtils.getServiceFileName(entityName) + ".xml");
    }

    /**
     * 解析Eeca
     *
     * @param entityEecas
     */
    public void parseEecas(List<EntityParseMember.EntityEeca> entityEecas, String entityName) throws BaseException {
        Element eecas = generateEecasElement(entityEecas, entityName);
        Document document = DomUtils.getDocument();
        document.add(eecas);
        DomUtils.outputDom(document, PathUtils.getEntityFileAbsolutePath(), PathUtils.getSeecaFileName(entityName) + ".xml");
    }

    /**
     * 生成服务的elements
     *
     * @param entityServices
     * @return
     */
    private Element generateServicesElement(List<EntityParseMember.EntityService> entityServices) {
        Element services = DomUtils.getRootElement(XmlConstant.XML_LABEL_SERVICE_ROOT, XmlConstant.XML_HEAD_SERVICE_VALUE);
        entityServices.forEach(entityService -> {
            Element service = DomUtils.getElement("service");
            Element richService = service.addAttribute("verb", entityService.getVerb())
                    .addAttribute("type", "script")
                    .addAttribute("location", entityService.getLocation());
            services.add(richService);
            writeServiceScript(entityService);
        });
        return services;
    }

    /**
     * 把脚本写入文件中
     *
     * @param entityService
     */
    private void writeServiceScript(EntityParseMember.EntityService entityService) {
        String scriptLocation = entityService.getLocation();
        int indexScriptName = scriptLocation.lastIndexOf(File.separator);
        String fileName = scriptLocation.substring(indexScriptName + 1, scriptLocation.length());
        String fileAbsolutePath = PathUtils.scriptRelatedLocationChangeAbsoluteLocation(scriptLocation).replace(fileName,"");
        FileUtils.writeToFile(fileAbsolutePath, fileName, entityService.getScript());
    }

    /**
     * 生成行为的elements
     *
     * @param entityActions
     * @return
     */
    private Element generateActionsElement(List<EntityParseMember.EntityAction> entityActions, String entityName) {
        Element resourceRoot = DomUtils.getRootElement("resource", XmlConstant.XML_HEAD_1_VALUE);
        resourceRoot.addAttribute("name", entityName)
                .addAttribute("displayName", "Example REST API")
                .addAttribute("version", "2.0.0");

        entityActions.forEach(entityAction -> {
            Element service = DomUtils.getElement("service").addAttribute("name", entityAction.getCallServiceUrl());
            Element method = DomUtils.getElement("method")
                    .addAttribute("type", "post");
            Element resource = DomUtils.getElement("resource")
                    .addAttribute("require-authentication", "false")
                    .addAttribute("name", entityAction.getSecondResource());

            method.add(service);
            resource.add(method);
            resourceRoot.add(resource);
        });
        return resourceRoot;
    }

    /**
     * 生成eeca的element
     *
     * @param entityEecas
     * @param entityName
     * @return
     */
    private Element generateEecasElement(List<EntityParseMember.EntityEeca> entityEecas, String entityName) {
        Element eecas = DomUtils.getRootElement("eecas", XmlConstant.XML_HEAD_EECAS_VALUE);
        entityEecas.forEach(entityEeca -> {
            Element eeca = DomUtils.getElement("eeca").addAttribute("entity", entityName);
            entityEeca.getTriggers().forEach(trigger -> {
                eeca.addAttribute(trigger, "true");
            });

            Element actions = DomUtils.getElement("actions");
            Element serviceCall = DomUtils.getElement("service-call").addAttribute("name", entityEeca.getCallServiceUrl());

            actions.add(serviceCall);
            eeca.add(actions);
            eecas.add(eeca);
        });
        return eecas;
    }
}
