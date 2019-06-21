package com.yjp.erp.model.po.eeca;
import lombok.Data;

@Data
public class BillEecaRuleField {
    private Long id;

    private Long billEecaRuleId;

    private String srcField;

    private String destField;
}
