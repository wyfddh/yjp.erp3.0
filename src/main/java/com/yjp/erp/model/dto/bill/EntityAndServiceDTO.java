package com.yjp.erp.model.dto.bill;

import com.yjp.erp.model.domain.BillFieldsRulesDO;
import com.yjp.erp.model.domain.FieldRuleDO;
import com.yjp.erp.model.domain.ActionDO;
import com.yjp.erp.model.domain.EventDO;
import com.yjp.erp.model.domain.ServicePropertyDO;
import com.yjp.erp.model.domain.fieldrule.OptionRules;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author liushui
 * @description: 实体与服务入参
 * @date 2019/3/22
 */
@Data
public class EntityAndServiceDTO {

    @NotBlank(message = "表单或实体中文名称不能为空")
    private String title;

    @NotBlank(message = "classId不能为空")
    private String classId;

    @NotBlank(message = "typeId不能为空")
    private String typeId;

    private String packageName;

    private String cache;

    /**
     * 交易类型
     */
    @NotBlank(message = "use不能为空")
    private String use;

    @NotNull
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

    //1-插入 2-更新
    private Integer type;
}
