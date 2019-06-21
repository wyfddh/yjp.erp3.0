package com.yjp.erp.mapper;

import com.yjp.erp.model.po.bill.BillFieldProperty;

import java.util.List;

/**
 * @Description
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/3/20
 */
public interface BillFieldPropertyMapper {

    List<BillFieldProperty> getBillFieldPropertyByBillId(Long fieldId);

    void bathInsertBillFieldsProperties(List<BillFieldProperty> billFieldProperties);
}