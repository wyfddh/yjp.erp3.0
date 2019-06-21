package com.yjp.erp.mapper;

import com.yjp.erp.model.po.eeca.BillEecaRuleField;

import java.util.List;

public interface BillEecaRuleFieldMapper {
    List<BillEecaRuleField> getBillEecaRuleField(Long billEecaRuleId);

    void bathInsertBillEecaRuleField(List<BillEecaRuleField> billEecas);
}
