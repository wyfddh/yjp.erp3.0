package com.yjp.erp.mapper;

import com.yjp.erp.model.po.bill.BillProperty;

import java.util.List;

/**
 * @Description
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/3/20
 */
public interface BillPropertyMapper {

    List<BillProperty> getBillPropertyByBillId(Long billId);

    void bathInsertBillProperties(List<BillProperty> billProperties);
}