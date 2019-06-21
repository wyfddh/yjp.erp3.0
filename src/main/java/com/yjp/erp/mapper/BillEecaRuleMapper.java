package com.yjp.erp.mapper;

import com.yjp.erp.model.po.eeca.BillEecaRule;

import java.util.List;

public interface BillEecaRuleMapper {
    BillEecaRule getBillEecaRule(Long billEecaId);

    void bathInsertBillEecaRule(List<BillEecaRule> billEecaRule);
}
