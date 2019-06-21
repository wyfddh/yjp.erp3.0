package com.yjp.erp.service;

import com.yjp.erp.mapper.BillOptionRulesMapper;
import com.yjp.erp.model.po.bill.BillOptionRules;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description:
 *
 * @author liushui
 * @date 2019/4/10
 */
@Service
public class BillOptionService {

    @Autowired
    private BillOptionRulesMapper billOptionRulesMapper;

    public void insertBillOptionRules(List<BillOptionRules> optionRules) {
        if (CollectionUtils.isNotEmpty(optionRules)) {
            billOptionRulesMapper.bathInsertRules(optionRules);
        }
    }
}
