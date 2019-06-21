package com.yjp.erp.service.dbxml;

import com.alibaba.fastjson.JSON;
import com.yjp.erp.model.dto.dbxml.BillEecaToXmlFile;
import com.yjp.erp.model.dto.dbxml.XmlFileRoules;
import com.yjp.erp.conf.exception.BaseException;
import com.yjp.erp.mapper.*;
import com.yjp.erp.model.po.bill.Bill;
import com.yjp.erp.model.po.eeca.BillEeca;
import com.yjp.erp.model.po.eeca.EecaService;
import com.yjp.erp.model.po.service.Service;
import com.yjp.erp.util.DomUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.dom.DOMText;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/3/27
 */
@org.springframework.stereotype.Service(value = "eeca")
public class BillEecaService implements DbToXml {

    private final BillEecaMapper billEecaMapper;
    private final BillMapper billMapper;
    private final EecaServiceMapper eecaServiceMapper;
    private final ServiceMapper serviceMapper;
    private final ModuleMapper moduleMapper;

    @Autowired(required = false)
    public BillEecaService(BillEecaMapper billEecaMapper, BillMapper billMapper, EecaServiceMapper eecaServiceMapper, ServiceMapper serviceMapper, ModuleMapper moduleMapper) {
        this.billEecaMapper = billEecaMapper;
        this.billMapper = billMapper;
        this.eecaServiceMapper = eecaServiceMapper;
        this.serviceMapper = serviceMapper;
        this.moduleMapper = moduleMapper;
    }

    @Override
    public Object getDBData(Long id) {
        BillEecaToXmlFile billEecaToXmlFile = new BillEecaToXmlFile();
        Map<Integer, Service> services = new HashMap<>();
        BillEeca billEeca = billEecaMapper.getBillEecaById(id);
        Bill bill = billMapper.getBillByModuleId(billEeca.getModuleId());
        List<EecaService> eecaServices = eecaServiceMapper.getByEecaId(id);
        eecaServices.forEach(eecaService -> {
            Service service = serviceMapper.getServiceById(eecaService.getServiceId());
            services.put(eecaService.getPriority(), service);
        });

        billEecaToXmlFile.setServices(services);
        billEecaToXmlFile.setEeca(billEeca);
        billEecaToXmlFile.setBill(bill);

        return billEecaToXmlFile;
    }

    @Override
    public void dataToXmlFile(Object object, XmlFileRoules xmlFileRoules) throws BaseException {
        BillEecaToXmlFile billEecaToXmlFile = (BillEecaToXmlFile) object;

        BillEeca billEeca = billEecaToXmlFile.getEeca();
        Bill bill = billEecaToXmlFile.getBill();

        Document eecaDom = DomUtils.getDocument();

        Element eecas = DomUtils.getRootElement("eecas", "../../../../framework/xsd/entity-eca-2.1.xsd");
        Element eeca = DomUtils.getElement("eeca").addAttribute("entity", "yjp."+bill.getName());

        addEecaAttribute(billEeca, eeca);

        Element expression = DomUtils.getElement("expression");
        expression.add(new DOMText(billEeca.getECondition()));

        Element actions = DomUtils.getElement("actions");
        Map<Integer, Service> services = billEecaToXmlFile.getServices();

        Service service = services.get(0);
        String name = service.getVerb() +xmlFileRoules.getEntityName();

        services.forEach((k, v) -> {
            Element serviceCall = DomUtils.getElement("service-call")
                    .addAttribute("name", xmlFileRoules.getServiceRelPath() + v.getVerb()+ "." + v.getVerb());
            actions.add(serviceCall);
        });
        Element condition = DomUtils.getElement("condition");
        condition.add(expression);
        eeca.add(condition);
        eeca.add(actions);
        eecas.add(eeca);
        eecaDom.add(eecas);

        String eecaFileName = name + ".eecas.xml";
        DomUtils.outputDom(eecaDom, xmlFileRoules.getEecaFileName(), eecaFileName);
    }

    /**
     * 给 eeca Element 添加属性信息
     * @param billEeca
     * @param eeca
     */
    private void addEecaAttribute(BillEeca billEeca, Element eeca) {
        eeca.addAttribute("id", billEeca.getEecaId());

        if (null != billEeca.getEMethod()) {
            List<String> method = JSON.parseArray(billEeca.getEMethod(), String.class);
            method.forEach(str -> {
                if (StringUtils.isNotBlank(str)) {
                    eeca.addAttribute(str, "true");
                }
            });
        }
        if (null != billEeca.getOtherOptions()) {
            List<String> otherOptions = JSON.parseArray(billEeca.getOtherOptions(), String.class);
            otherOptions.forEach(option -> {
                eeca.addAttribute(option, "true");
            });
        }
        if (null != billEeca.getETrigger()) {
            List<String> trigger = JSON.parseArray(billEeca.getETrigger(), String.class);
            trigger.forEach(str -> {
                if (StringUtils.isNotBlank(str)) {
                    eeca.addAttribute(str, "true");
                }
            });
        }
    }

    @Override
    public void analysisData(Long id, XmlFileRoules xmlFileRoules) {
    }
}
