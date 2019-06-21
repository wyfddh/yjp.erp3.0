package com.yjp.erp.model.vo.bill;

import com.yjp.erp.model.domain.FieldRuleDO;
import com.yjp.erp.model.domain.ServicePropertyDO;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author liushui
 * @description:
 * @date 2019/3/27
 */
@Data
public class ModelVO {

    private List<Map<String,Object>> fields;

    private List<FieldRuleDO> rules;

    private List<ServicePropertyDO> services;
}
