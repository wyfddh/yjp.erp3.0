package com.yjp.erp.service.parsexml.service;

import com.yjp.erp.model.po.bill.BillProperty;

import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/28 20:39
 * @Email: jianghongping@yijiupi.com
 */
public interface BillPropertyService {
    List<BillProperty> getBillProperiesByBillId(Long billId);
}
