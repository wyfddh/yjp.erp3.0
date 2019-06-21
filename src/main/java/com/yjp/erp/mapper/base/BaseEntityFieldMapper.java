package com.yjp.erp.mapper.base;

import com.yjp.erp.model.po.bill.BillField;

import java.util.List;

public interface BaseEntityFieldMapper {

    List<BillField> getBillFieldByBillId(Long billId);

    void bathInsertBillFields(List<BillField> billFields);

    List<BillField> getBillMoquiProperties(Long billId);

    List<BillField> getBillWebProperties(Long billId);
}