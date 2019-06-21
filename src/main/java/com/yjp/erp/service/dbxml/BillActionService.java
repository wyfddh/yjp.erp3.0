package com.yjp.erp.service.dbxml;

import com.yjp.erp.constants.XmlConstant;
import com.yjp.erp.model.dto.dbxml.BillActionToXmlFile;
import com.yjp.erp.model.dto.dbxml.XmlFileRoules;
import com.yjp.erp.conf.exception.BaseException;
import com.yjp.erp.mapper.ActionServiceMapper;
import com.yjp.erp.mapper.BillActionMapper;
import com.yjp.erp.mapper.ServiceMapper;
import com.yjp.erp.model.po.service.ActionService;
import com.yjp.erp.model.po.service.BillAction;
import com.yjp.erp.model.po.service.Service;
import com.yjp.erp.util.DomUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 将DB的服务和动作元数据生成xml文件
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/3/22
 */
@org.springframework.stereotype.Service(value = "action")
@SuppressWarnings("unchecked")
public class BillActionService implements DbToXml {

    private final ServiceMapper serviceMapper;

    private final BillActionMapper billActionMapper;

    private final ActionServiceMapper actionServiceMapper;

    @Autowired(required = false)
    public BillActionService(BillActionMapper billActionMapper, ServiceMapper serviceMapper, ActionServiceMapper actionServiceMapper) {
        this.billActionMapper = billActionMapper;
        this.serviceMapper = serviceMapper;
        this.actionServiceMapper = actionServiceMapper;
    }

    @Override
    public Object getDBData(Long id) {
        BillActionToXmlFile billActionToXmlFile = new BillActionToXmlFile();
        billActionToXmlFile.setAction(billActionMapper.getBillActionById(id));
        Map<Integer, Service> services = new HashMap<>();
        List<ActionService> actionServices = actionServiceMapper.getServiceIdByActionId(id);
        actionServices.forEach(actionService -> {
            Service service = serviceMapper.getServiceById(actionService.getServiceId());
            services.put(actionService.getPriority(), service);
        });
        billActionToXmlFile.setServices(services);
        return billActionToXmlFile;
    }

    @Override
    public void analysisData(Long id, XmlFileRoules xmlFileRoules) throws BaseException {
        dataToXmlFile(getDBData(id), xmlFileRoules);
    }

    @Override
    public void dataToXmlFile(Object listActionToXmlFile, XmlFileRoules xmlFileRoules) throws BaseException {
        List<BillActionToXmlFile> billActionToXmlFiles = (List<BillActionToXmlFile>) listActionToXmlFile;
        // 生成 resource(action) xml文件
        Element root = DomUtils.getRootElement(XmlConstant.XML_LABEL_RESOURCE_ROOT, XmlConstant.XML_HEAD_RESOURCE_VALUE);
        Document resourceDom = DomUtils.getDocument();
        //第一级resource
        resourceHead(xmlFileRoules, root);

        packageActions(xmlFileRoules, billActionToXmlFiles, root);

        resourceDom.add(root);

        //生成xml文件
        String xmlFileName = xmlFileRoules.getEntityName();
        String actionFileName = xmlFileName + ".rest.xml";
        DomUtils.outputDom(resourceDom, xmlFileRoules.getRuntimePath()+"/"+xmlFileRoules.getActionFileName(), actionFileName);
    }

    /**
     * 多个Action append 到root下面
     *
     * @param xmlFileRoules
     * @param billActionToXmlFiles
     * @param root
     */
    private void packageActions(XmlFileRoules xmlFileRoules, List<BillActionToXmlFile> billActionToXmlFiles, Element root) {
        billActionToXmlFiles.forEach(billActionToXmlFile -> {
            Map<Integer, Service> map = billActionToXmlFile.getServices();
            BillAction action = billActionToXmlFile.getAction();
            Element resource = DomUtils.getElement(XmlConstant.XML_LABEL_RESOURCE_RESOURCE);
            resource.addAttribute("require-authentication", "false");
            Element method = DomUtils.getElement("method");
            method.addAttribute("type", action.getRpcMethod());

            map.forEach((key, value) -> {
                resource.addAttribute("name", value.getServiceName());
                Element actionService = DomUtils.getElement(XmlConstant.XML_LABEL_SERVICE_SSERVICE);
                if (1 == value.getServiceType()) {
                    //todo 导出打印等通用脚本的处理
                    actionService.addAttribute("name", "" + value.getVerb() + value.getNoun() + "." + value.getVerb() + "#" + value.getNoun());
                } else {
                    actionService.addAttribute("name", getServiceName(xmlFileRoules, value));
                }
                method.add(actionService);
            });
            resource.add(method);
            root.add(resource);
        });
    }

    /**
     * 根据服务 给action拼一个 服务名
     *
     * @param xmlFileRoules
     * @param value
     * @return
     */
    private String getServiceName(XmlFileRoules xmlFileRoules, Service value) {
        return xmlFileRoules.getServiceRelPath() + value.getVerb() + "." + value.getVerb();
    }

    /***
     * action的第一级 resource生成
     * @param xmlFileRoules
     * @param root action的root Element
     */
    private void resourceHead(XmlFileRoules xmlFileRoules, Element root) {
        root.addAttribute("name", xmlFileRoules.getEntityName());
        root.addAttribute("displayName", "Example REST API");
        root.addAttribute("version", "2.0.0");
    }
}
