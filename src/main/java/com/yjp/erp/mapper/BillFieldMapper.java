package com.yjp.erp.mapper;

import com.yjp.erp.model.po.bill.BillField;

import java.util.List;

/**
 * @Description
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/3/20
 */
public interface BillFieldMapper {

    List<BillField> getBillFieldByBillId(Long billId);

    void bathInsertBillFields(List<BillField> billFields);

    List<BillField> getBillMoquiProperties(Long billId);

    List<BillField> getBillWebProperties(Long billId);
}