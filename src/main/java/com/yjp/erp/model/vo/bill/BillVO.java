package com.yjp.erp.model.vo.bill;

import com.yjp.erp.model.domain.BillFieldsRulesDO;
import com.yjp.erp.model.domain.FieldRuleDO;
import com.yjp.erp.model.domain.ActionDO;
import com.yjp.erp.model.domain.EventDO;
import com.yjp.erp.model.domain.ServicePropertyDO;
import com.yjp.erp.model.domain.fieldrule.OptionRules;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author liushui
 * @description: 传递给前端表单模型
 * @date 2019/3/22
 */
@Data
public class BillVO {

    private String title;

    private String classId;

    private String typeId;

    private String packageName;

    private String cache;

    private String use;

    private Integer status;

    private List<Map<String,Object>> fields;

    private List<FieldRuleDO> rules;

    private List<ServicePropertyDO> services;

    private List<ActionDO> actions;

    private List<EventDO> events;

    private List<BillFieldsRulesDO> children;

    /**
     * 下推或回写的规则定义
     */
    private List<OptionRules> optionRules;
}
