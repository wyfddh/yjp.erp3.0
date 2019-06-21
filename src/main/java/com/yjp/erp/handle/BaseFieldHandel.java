package com.yjp.erp.handle;

import com.alibaba.fastjson.JSON;
import com.yjp.erp.constants.EntityConstant;
import com.yjp.erp.constants.MoquiProperties;
import com.yjp.erp.model.domain.BillFieldsRulesDO;
import com.yjp.erp.model.domain.FieldRuleDO;
import com.yjp.erp.model.domain.ReferenceDO;
import com.yjp.erp.model.po.bill.BillField;
import com.yjp.erp.model.po.bill.BillFieldProperty;
import com.yjp.erp.model.po.bill.BillFieldWebProperty;
import com.yjp.erp.model.po.bill.BillFieldWebPropertyRel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author liushui
 * @description:
 * @date 2019/3/27
 */
public class BaseFieldHandel {
    /**
     * 把实体的moqui属性字段集合和 web渲染字段属性的集合
     *
     * @param moquiProperties moqui字段的属性集合
     * @param webProperties   web渲染字段的属性集合
     * @return 实体的字段规则DO对象
     */
    BillFieldsRulesDO fieldDbDataToVoData(List<BillField> moquiProperties, List<BillField> webProperties) {
        List<Map<String, Object>> fieldList = new ArrayList<>();
        List<FieldRuleDO> rules = new ArrayList<>();
        Map<String, Map<String, Object>> markMap = new HashMap<>();

        addMoquiAttr(moquiProperties, fieldList, markMap);
        addWebAttr(webProperties, fieldList, rules, markMap);

        return addRules(fieldList, rules);
    }

    /**
     * 添加规则
     *
     * @param fieldList
     * @param rules
     * @return
     */
    private BillFieldsRulesDO addRules(List<Map<String, Object>> fieldList, List<FieldRuleDO> rules) {
        BillFieldsRulesDO billFieldsRulesDO = new BillFieldsRulesDO();
        billFieldsRulesDO.setFields(fieldList);
        billFieldsRulesDO.setRules(rules);
        return billFieldsRulesDO;
    }

    /**
     * 添加web渲染属性
     *
     * @param webProperties
     * @param fieldList
     * @param rules
     * @param markMap
     */
    private void addWebAttr(List<BillField> webProperties, List<Map<String, Object>> fieldList, List<FieldRuleDO> rules, Map<String, Map<String, Object>> markMap) {
        if (CollectionUtils.isNotEmpty(webProperties)) {
            webProperties.forEach(field -> {
                if (StringUtils.isNotBlank(field.getRules()) && !Objects.equals(EntityConstant.NUMM, field.getRules())) {
                    rules.add(JSON.parseObject(field.getRules(), FieldRuleDO.class));
                }

                Map<String, Object> map = markMap.get(field.getFieldName());
                if (Objects.isNull(map)) {
                    map = new HashMap<>();
                    fieldList.add(map);
                }
                markMap.put(field.getFieldName(), map);
                addWebAttr(field, map);
            });
        }
    }

    /**
     * 给实体字段的web渲染添加属性
     *
     * @param field
     * @param map
     */
    private void addWebAttr(BillField field, Map<String, Object> map) {
        if (CollectionUtils.isNotEmpty(field.getBillFieldWebProperties())) {
            for (BillFieldWebProperty property : field.getBillFieldWebProperties()) {
                if (ArrayUtils.contains(MoquiProperties.BOOLEAN_ELEMENT, property.getName())) {
                    map.put(property.getName(), Boolean.valueOf(property.getElementValue()));
                } else {
                    map.put(property.getName(), property.getElementValue());
                }
                if (Objects.equals("type", property.getName()) && Objects.equals("refObject", property.getElementValue())) {
                    map.put("options", ArrayUtils.EMPTY_OBJECT_ARRAY);
                    map.put("refTarget", generateReferenceDO(property.getBillFieldWebPropertyRel()));
                }
            }
        }
    }

    /**
     * 添加单据字段的moqui属性
     *
     * @param moquiProperties 单据字段集合
     * @param fieldList
     * @param markMap
     */
    private void addMoquiAttr(List<BillField> moquiProperties, List<Map<String, Object>> fieldList, Map<String, Map<String, Object>> markMap) {
        if (CollectionUtils.isNotEmpty(moquiProperties)) {
            moquiProperties.forEach(moqui -> {
                Map<String, Object> map = new HashMap<>();
                String fieldName = moqui.getFieldName();
                map.put("name", fieldName);
                markMap.put(fieldName, map);
                generateMoquiAttr(moqui.getBillFieldProperties(), map);
                fieldList.add(map);
            });
        }
    }

    /**
     * 把字段的moqui属性放入map中
     *
     * @param billFieldProperties
     * @param map
     */
    private void generateMoquiAttr(List<BillFieldProperty> billFieldProperties, Map<String, Object> map) {
        if (CollectionUtils.isNotEmpty(billFieldProperties)) {
            billFieldProperties.forEach(property -> {
                if (ArrayUtils.contains(MoquiProperties.BOOLEAN_ELEMENT, property.getName())) {
                    map.put(property.getName(), Boolean.valueOf(property.getElementValue()));
                } else {
                    map.put(property.getName(), property.getElementValue());
                }
            });
        }
    }

    private ReferenceDO generateReferenceDO(BillFieldWebPropertyRel rel) {

        ReferenceDO referenceDO = new ReferenceDO();
        referenceDO.setClassId(rel.getClassId());
        referenceDO.setKeyField(rel.getSrcField());
        referenceDO.setValueField(rel.getDestField());
        referenceDO.setTypeId(rel.getTypeId());
        referenceDO.setKeyType(rel.getKeyType());

        return referenceDO;
    }
}
