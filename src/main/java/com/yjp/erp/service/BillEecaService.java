package com.yjp.erp.service;

import com.yjp.erp.conf.exception.BaseException;
import com.yjp.erp.mapper.*;
import com.yjp.erp.model.po.bill.Bill;
import com.yjp.erp.model.po.bill.BillModule;
import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.po.eeca.BillEeca;
import com.yjp.erp.model.po.eeca.EecaService;
import com.yjp.erp.model.po.eeca.BillEecaRule;
import com.yjp.erp.model.po.eeca.BillEecaRuleField;
import com.yjp.erp.model.dto.script.EntityFieldTransmit;
import com.yjp.erp.util.DomUtils;
import com.yjp.erp.util.TransformEntityUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.dom.DOMText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BillEecaService {
    @Autowired(required = false)
    private BillEecaRuleMapper billEecaRuleMapper;
    @Autowired(required = false)
    private BillEecaRuleFieldMapper billEecaRuleFieldMapper;
    @Autowired(required = false)
    private BillEecaMapper billEecaMapper;
    @Autowired(required = false)
    private EecaServiceMapper eecaServiceMapper;
    @Autowired(required = false)
    private ServiceMapper serviceMapper;
    @Autowired(required = false)
    private BillMapper billMapper;
    @Autowired(required = false)
    private BillModuleMapper billModuleMapper;
    @Autowired(required = false)
    private ModuleMapper moduleMapper;

    public String getEeecaScript(Long billEecaId) {
        EntityFieldTransmit entityFieldTransmit = new EntityFieldTransmit();
        //设置主实体之间下推映射
        BillEecaRule billEecaRule = billEecaRuleMapper.getBillEecaRule(billEecaId);
        entityFieldTransmit.setUpEntity(billEecaRule.getDestEntity());
        entityFieldTransmit.setDownEntity(billEecaRule.getSrcEntity());
        //设置主实体之间字段的下推映射
        Long tt = billEecaRule.getBillEecaId();
        List<BillEecaRuleField> mainBillEecaRuleFields = billEecaRuleFieldMapper.getBillEecaRuleField(billEecaRule.getBillEecaId());
        entityFieldTransmit.setMainEntityRelated(mainBillEecaRuleFields);

        //设置明细实体之间下推映射
        BillEecaRule billEecaRuleDetail = billEecaRuleMapper.getBillEecaRule(billEecaRule.getDetailId());
        entityFieldTransmit.setUpEntityDetail(billEecaRuleDetail.getDestEntity());
        entityFieldTransmit.setDownEntityDetail(billEecaRuleDetail.getSrcEntity());
        //设置明细实体之间下推映射
        List<BillEecaRuleField> detailBillEecaRuleFields = billEecaRuleFieldMapper.getBillEecaRuleField(billEecaRuleDetail.getBillEecaId());
        entityFieldTransmit.setDetailEntityRelated(detailBillEecaRuleFields);
        //设置下端实体主实体与明细实体的主外键关联
        BillEeca billEeca = billEecaMapper.getBillEecaById(billEecaId);
        BillModule billModule = billModuleMapper.selectByPrimaryKey(billEeca.getModuleId());


        Bill bill = billMapper.getBillById(billModule.getId());

        Bill billDeatil = billMapper.getBillByParentId(bill.getId());
        BillEecaRuleField downFkRelated = new BillEecaRuleField();
        downFkRelated.setDestField(bill.getForeignKey());
        downFkRelated.setSrcField(billDeatil.getPrimaryKey());
        entityFieldTransmit.setDownFkRelated(downFkRelated);

        return TransformEntityUtil.execute(entityFieldTransmit);
    }

    public void writeEntityEecaXml(Long billEecaId) throws BaseException {
        BillEeca billEeca = billEecaMapper.getBillEecaById(billEecaId);
        Module module=moduleMapper.getModuleById(billEeca.getModuleId());
        Bill bill = billMapper.getBillByModuleId(billEeca.getModuleId());

        Document eecaDom = DomUtils.getDocument();


        Element eecas = DomUtils.getRootElement("eecas", "../../../../framework/xsd/entity-eca-2.1.xsd");
        Element eeca = DomUtils.getElement("eeca").addAttribute("entity", bill.getName());
        //todo 更具methods 来设置监听动作
        if(null!=billEeca.getEMethod()){
            String[] method = billEeca.getEMethod().split(",");
            for (int i = 0; i < billEeca.getEMethod().split(",").length; i++) {
                //todo 解析字符串为eeca 添加具体的动作
            }
        }


        Element condition = DomUtils.getElement("condition");
        condition.add(new DOMText(billEeca.getECondition()));
        Element actions = addMultService(billEeca.getId(),module);

        eeca.add(actions);
        eecas.add(eeca);
        eecaDom.add(eecas);

      /*  DomUtils.outputDom(eecaDom, bill.getName() + ".eeca.xml");*/
    }

    private Element addMultService(Long eecaId,Module module) {
        Element actions = DomUtils.getElement("actions");
        List<EecaService> eecaServices = eecaServiceMapper.getByEecaId(eecaId);
        eecaServices.forEach(eecaService -> {
            com.yjp.erp.model.po.service.Service service = serviceMapper.getServiceById(eecaService.getServiceId());
            Element serviceCall = DomUtils.getElement("service-call")
                    .addAttribute("name", module.getTypeId()+"."
                            + module.getClassId()+"."+service.getVerb() + "."
                            + service.getVerb() + "Service" + "." + service.getNoun()
                            + "#" + service.getVerb());
            actions.add(serviceCall);
        });
        return actions;
    }
}
