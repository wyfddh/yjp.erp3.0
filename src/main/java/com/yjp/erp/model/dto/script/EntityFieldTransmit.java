package com.yjp.erp.model.dto.script;

import com.yjp.erp.model.po.eeca.BillEecaRuleField;
import lombok.Data;

import java.util.List;

/**
* @Description: 上流单据与下端单据的字段关联
* @CreateDate: 2019/3/22 11:09
* @EMAIL: jianghongping@yijiupi.com
*/
@Data
public class EntityFieldTransmit {

    private String upEntity;

    private String upEntityDetail;

    private String downEntity;

    private String downEntityDetail;

    private List<BillEecaRuleField> mainEntityRelated;

    private List<BillEecaRuleField> detailEntityRelated;

    private BillEecaRuleField downFkRelated;
}
