package com.yjp.erp.mapper.base;

import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.po.service.BillAction;

import java.util.List;

/**
 * @Description
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/3/23
 */
public interface BaseActionMapper {

    List<BillAction> getBillActionByModuleId(Long id);

    BillAction getBillActionById(Long id);

    void bathInsertBillAction(List<BillAction> billActions);

    List<BillAction> getBillActionList(Module module);
}
