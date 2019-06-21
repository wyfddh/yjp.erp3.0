package com.yjp.erp.mapper.base;

import com.yjp.erp.model.po.bill.BillProperty;

import java.util.List;

public interface BaseEntityPropertyMapper {

    List<BillProperty> getBillPropertyByBillId(Long billId);

    void bathInsertBillProperties(List<BillProperty> billProperties);
}