package com.yjp.erp.service.dbxml;

import com.yjp.erp.constants.ScriptEnum;
import com.yjp.erp.constants.XmlConstant;
import com.yjp.erp.model.dto.dbxml.XmlFileRoules;
import com.yjp.erp.conf.exception.BaseException;
import com.yjp.erp.model.po.service.Service;
import com.yjp.erp.util.DomUtils;
import com.yjp.erp.util.FileUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.Map;

/**
 * @Description
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/3/27
 */
@org.springframework.stereotype.Service(value = "service")
public class BillServiceService implements DbToXml {
    @Override
    public Object getDBData(Long id) {
        return null;
    }

    @Override
    public void dataToXmlFile(Object object, XmlFileRoules xmlFileRoules) throws BaseException {
        Map<Integer, Service> map = (Map<Integer, Service>) object;
        // 生成 service xml文件
        Document serviceDom = DomUtils.getDocument();
        Element services = DomUtils.getRootElement(XmlConstant.XML_LABEL_SERVICE_ROOT, XmlConstant.XML_HEAD_SERVICE_VALUE);

        map.forEach((key, value) -> {
            // serviceType等于1时为通用服务,不产生xml,2为个性化服务
            if (ScriptEnum.CUSTOMIZATION.value == value.getServiceType()) {
                Element service = DomUtils.getElement("service");
                service.addAttribute("verb", value.getVerb());
                service.addAttribute("type", "script");
                //要生成的脚本的路径和文件名
                String locationFile = xmlFileRoules.getScriptFileName();
                String scriptName = value.getVerb() + ".groovy";

                service.addAttribute("location", "component://" + locationFile + scriptName);
                service.addAttribute("allow-remote", "true");
                service.addAttribute("authz-action", "all");
                service.addAttribute("authenticate", "false");

                services.add(service);

                //生成服务脚本文件
                String scriptContext = value.getScript();
                String scriptRelFilePath = xmlFileRoules.getRuntimePath() + locationFile;

                FileUtils.writeToFile(scriptRelFilePath, scriptName, scriptContext);
            }
        });
        serviceDom.add(services);

        Service service = map.get(0);
        String serviceFileName = service.getVerb()+ ".xml";
        String servicePath=xmlFileRoules.getRuntimePath()+"/"+xmlFileRoules.getServiceFilePath();
        DomUtils.outputDom(serviceDom, servicePath, serviceFileName);
    }

    @Override
    public void analysisData(Long id, XmlFileRoules xmlFileRoules) {
    }
}
