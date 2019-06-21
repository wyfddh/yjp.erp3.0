package com.yjp.erp.mapper.base;

import com.yjp.erp.model.po.bill.BillFieldWebProperty;

import java.util.List;

public interface BaseEntityFieldWebPropertyMapper {

    void bathInsertBillFieldsWebProperties(List<BillFieldWebProperty> billFieldWebProperties);

    List<BillFieldWebProperty> getFieldWebProperties(Long id);
}