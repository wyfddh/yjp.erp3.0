package com.yjp.erp.model.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author liushui
 * @description:
 * @date 2019/3/26
 */
@Data
public class BillFieldsRulesDO {

    private List<Map<String,Object>> fields;

    private List<FieldRuleDO> rules;

    private String primaryKey;

    private String foreignKey;

    private String typeId;

    private String title;
}
