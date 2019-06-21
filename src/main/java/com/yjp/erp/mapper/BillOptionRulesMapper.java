package com.yjp.erp.mapper;

import com.yjp.erp.model.po.bill.BillOptionRules;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BillOptionRulesMapper {

    void bathInsertRules(List<BillOptionRules> optionRules);

    List<BillOptionRules> getOptionRulesByModule(Long moduleId);
}