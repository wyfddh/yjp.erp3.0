package com.yjp.erp.model.dto.bill;

import com.yjp.erp.model.domain.FieldRuleDO;
import com.yjp.erp.model.domain.ActionDO;
import com.yjp.erp.model.domain.EventDO;
import com.yjp.erp.model.domain.ServicePropertyDO;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author liushui
 * @description:
 * @date 2019/3/22
 */
@Data
public class ElementDTO {

    private String entityName;

    private BillPropertyDTO property;

    private List<Map<String,Object>> fields;

    private List<FieldRuleDO> fieldRules;

    private List<ServicePropertyDO> services;

    private List<ActionDO> actions;

    private List<EventDO> events;
}
