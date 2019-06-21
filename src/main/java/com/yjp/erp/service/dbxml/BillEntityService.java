package com.yjp.erp.service.dbxml;

import com.yjp.erp.constants.ModuleConstant;
import com.yjp.erp.constants.XmlConstant;
import com.yjp.erp.model.dto.dbxml.BillEntityToXmlFile;
import com.yjp.erp.model.dto.dbxml.XmlFileRoules;
import com.yjp.erp.conf.exception.BaseException;
import com.yjp.erp.mapper.BillFieldMapper;
import com.yjp.erp.mapper.BillFieldPropertyMapper;
import com.yjp.erp.mapper.BillMapper;
import com.yjp.erp.mapper.BillPropertyMapper;
import com.yjp.erp.mapper.base.BaseEntityFieldMapper;
import com.yjp.erp.mapper.base.BaseEntityFieldPropertyMapper;
import com.yjp.erp.mapper.base.BaseEntityMapper;
import com.yjp.erp.mapper.base.BaseEntityPropertyMapper;
import com.yjp.erp.model.po.bill.*;
import com.yjp.erp.util.DomUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.dom.DOMDocument;
import org.dom4j.dom.DOMElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Description 将DB的实体元数据生成xml文件
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/3/20
 */
@Service(value = "entity")
public class BillEntityService implements DbToXml {

    private final BillFieldMapper billFieldMapper;

    private final BillPropertyMapper billPropertyMapper;

    private final BillFieldPropertyMapper billFieldPropertyMapper;

    private final BillMapper billMapper;

    private final BaseEntityMapper baseEntityMapper;

    private final BaseEntityFieldMapper baseEntityFieldMapper;

    private final BaseEntityFieldPropertyMapper baseEntityFieldPropertyMapper;

    private final BaseEntityPropertyMapper baseEntityPropertyMapper;


    @Autowired(required = false)
    public BillEntityService(BillFieldMapper billFieldMapper, BillPropertyMapper billPropertyMapper, BillFieldPropertyMapper billFieldPropertyMapper, BillMapper billMapper, BaseEntityMapper baseEntityMapper, BaseEntityFieldMapper baseEntityFieldMapper, BaseEntityFieldPropertyMapper baseEntityFieldPropertyMapper, BaseEntityPropertyMapper baseEntityPropertyMapper) {
        this.billFieldMapper = billFieldMapper;
        this.billPropertyMapper = billPropertyMapper;
        this.billFieldPropertyMapper = billFieldPropertyMapper;
        this.billMapper = billMapper;
        this.baseEntityMapper = baseEntityMapper;
        this.baseEntityFieldMapper = baseEntityFieldMapper;
        this.baseEntityFieldPropertyMapper = baseEntityFieldPropertyMapper;
        this.baseEntityPropertyMapper = baseEntityPropertyMapper;
    }

    @Override
    public Object getDBData(Long id) {
        BillEntityToXmlFile entityData = (BillEntityToXmlFile) getXmlData(id);
        Bill bill = billMapper.getBillById(id);
        if (null != bill && -1L == bill.getParentId()) {
            entityData.setBill(bill);
            Bill billDetail = billMapper.getBillByParentId(id);
            if (null != billDetail) {
                BillEntityToXmlFile detail = (BillEntityToXmlFile) getXmlData(billDetail.getId());
                detail.setBill(billDetail);
                entityData.setBillDetail(detail);
            }
        }
        return entityData;
    }

    public Object getDBData(Long id, Module module) {
        BillEntityToXmlFile entityData = (BillEntityToXmlFile) getXmlData(id, module);
        Bill bill;
        Bill billDetail;
        if (ModuleConstant.CLASS_ENTITY.equals(module.getClassId())) {
            billDetail = baseEntityMapper.getBillByParentId(id);
            bill = baseEntityMapper.getBillById(id);
        } else {
            billDetail = billMapper.getBillByParentId(id);
            bill = billMapper.getBillById(id);
        }
        if (null != bill && -1L == bill.getParentId()) {
            entityData.setBill(bill);
            if (null != billDetail) {
                BillEntityToXmlFile detail = (BillEntityToXmlFile) getXmlData(billDetail.getId(), module);
                detail.setBill(billDetail);
                entityData.setBillDetail(detail);
            }
        }
        return entityData;
    }

    @Override
    public void analysisData(Long id, XmlFileRoules xmlFileRoules) throws BaseException {
        dataToXmlFile(getDBData(id, xmlFileRoules.getModule()), xmlFileRoules);
    }

    private Object getXmlData(Long id) {
        Map<Long, List<BillFieldProperty>> map = new HashMap<>();
        BillEntityToXmlFile billEntityToXmlFile = new BillEntityToXmlFile();

        List<BillField> billFields = billFieldMapper.getBillFieldByBillId(id);
        billFields.forEach(billField -> {
            Long fieldId = billField.getId();
            List<BillFieldProperty> billFieldProperty = billFieldPropertyMapper.getBillFieldPropertyByBillId(fieldId);
            map.put(fieldId, billFieldProperty);
        });
        List<BillProperty> billPropertys = billPropertyMapper.getBillPropertyByBillId(id);

        billEntityToXmlFile.setBillField(billFields);
        billEntityToXmlFile.setBillFieldProperty(map);
        billEntityToXmlFile.setBillPropertity(billPropertys);
        return billEntityToXmlFile;
    }

    private Object getXmlData(Long id, Module module) {
        List<BillProperty> billPropertys;
        Map<Long, List<BillFieldProperty>> map = new HashMap<>();
        BillEntityToXmlFile billEntityToXmlFile = new BillEntityToXmlFile();
        List<BillField> billFields;
        if (ModuleConstant.CLASS_ENTITY.equals(module.getClassId())) {
            billPropertys = baseEntityPropertyMapper.getBillPropertyByBillId(id);
            billFields = baseEntityFieldMapper.getBillFieldByBillId(id);
            billFields.forEach(billField -> {
                Long fieldId = billField.getId();
                List<BillFieldProperty> billFieldProperty = baseEntityFieldPropertyMapper.getBillFieldPropertyByBillId(fieldId);
                map.put(fieldId, billFieldProperty);
            });
            billEntityToXmlFile.setBillField(billFields);

        } else {
            billPropertys = billPropertyMapper.getBillPropertyByBillId(id);
            billFields = billFieldMapper.getBillFieldByBillId(id);
            billFields.forEach(billField -> {
                Long fieldId = billField.getId();
                List<BillFieldProperty> billFieldProperty = billFieldPropertyMapper.getBillFieldPropertyByBillId(fieldId);
                map.put(fieldId, billFieldProperty);
            });
            billEntityToXmlFile.setBillField(billFields);
        }


        billEntityToXmlFile.setBillFieldProperty(map);
        billEntityToXmlFile.setBillPropertity(billPropertys);
        return billEntityToXmlFile;
    }


    @Override
    public void dataToXmlFile(Object data, XmlFileRoules xmlFileRoules) throws BaseException {
        BillEntityToXmlFile entityData = (BillEntityToXmlFile) data;
        BillEntityToXmlFile billDetail = entityData.getBillDetail();

        Document dom = new DOMDocument();
        Element entities = DomUtils.getRootElement(XmlConstant.XML_LABEL_ENTITY_ROOT, XmlConstant.XML_HEAD_ENTITY_VALUE);
        dom.add(entities);

        addEntity(entityData, entities);
        if (null != billDetail) {
            addEntity(billDetail, entities);
        }
        String entityFileName = entityData.getBill().getName() + ".xml";

        DomUtils.outputDomEntity(dom, xmlFileRoules.getEntityFileName(), entityFileName);
    }

    private void addEntity(BillEntityToXmlFile entityData, Element entities) {
        Element entity = new DOMElement(XmlConstant.XML_LABEL_ENTITY_ENTITY);
        Bill bill = entityData.getBill();

        List<BillProperty> billPropertitys = entityData.getBillPropertity();
        if (null != billPropertitys && billPropertitys.size() > 0) {
            entity.addAttribute("entity-name", bill.getName());
            entity.addAttribute("authorize-skip", "true");
            billPropertitys.forEach(billProperty -> {
                if ("packageName".equals(billProperty.getName())) {
                    entity.addAttribute("package", billProperty.getElementValue());
                } else {
                    entity.addAttribute(billProperty.getName(), billProperty.getElementValue());
                }
            });
            entities.add(entity);
        }

        List<BillField> billField = entityData.getBillField();
        Map<Long, List<BillFieldProperty>> billFieldProperty = entityData.getBillFieldProperty();
        if (null != billField && billField.size() > 0) {
            billField.forEach(billField1 -> {
                Long id = billField1.getId();
                List<BillFieldProperty> billFieldProperties = billFieldProperty.get(id);
                Element field = new DOMElement(XmlConstant.XML_LABEL_ENTITY_FIELD);
                field.addAttribute("name", billField1.getFieldName());
                billFieldProperties.forEach(billFieldProperty1 -> {
                    field.addAttribute(billFieldProperty1.getName(), billFieldProperty1.getElementValue());
                });
                entity.add(field);
            });
        }

        // 添加relationship
        out:
        if (null != bill && -1L == bill.getParentId()) {
            BillEntityToXmlFile billDetail = entityData.getBillDetail();
            if (null == billDetail) {
                break out;
            }
            Bill detailBill = billDetail.getBill();
            String name = billDetail.getBill().getName();
            Element relationship = new DOMElement(XmlConstant.XML_LABEL_ENTITY_RELATIONSHIP);

            AtomicReference<String> packageName = new AtomicReference<>("yjp");
            List<BillProperty> billPropertity = billDetail.getBillPropertity();
            billPropertity.forEach(property -> {
                String propertyName = property.getName();
                if ("package".equals(propertyName)) {
                    packageName.set(property.getElementValue());
                }
            });
            String entityName = packageName + "." + name;

            relationship.addAttribute("type", "many");
            relationship.addAttribute("related", entityName);
            relationship.addAttribute("short-alias", name.toUpperCase());

            Element keyMap = new DOMElement("key-map");
            keyMap.addAttribute("field-name", detailBill.getPrimaryKey());
            keyMap.addAttribute("related", detailBill.getForeignKey());

            Element master = new DOMElement("master");
            Element detail = new DOMElement("detail");
            detail.addAttribute("relationship", name.toUpperCase());

            master.add(detail);
            relationship.add(keyMap);
            entity.add(relationship);
            entity.add(master);
        }
    }
}
