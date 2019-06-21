package com.yjp.erp.mapper;

import com.yjp.erp.model.po.bill.BillFieldWebProperty;

import java.util.List;

public interface BillFieldWebPropertyMapper {

    void bathInsertBillFieldsWebProperties(List<BillFieldWebProperty> billFieldWebProperties);

    List<BillFieldWebProperty> getFieldWebProperties(Long id);
}