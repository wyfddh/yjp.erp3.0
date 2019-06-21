package com.yjp.erp.model.vo.bill;

import com.yjp.erp.model.domain.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * description:
 *
 * @author liushui
 * @date 2019/4/11
 */
@Data
public class BillElementsVO implements Serializable {

    private String title;

    private String classId;

    private String typeId;

    private List<Map<String,Object>> fields;

    private List<FieldRuleDO> rules;

    private List<ActionDO> actions;

    private List<BillFieldsRulesDO> children;
}
