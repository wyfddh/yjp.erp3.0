package com.yjp.erp.service.parsexml.service;

import com.yjp.erp.model.po.bill.BillFieldProperty;

import java.util.List;

public interface BillFieldPropertyService {
    List<BillFieldProperty> getBillFieldPropertiesByFieldId(Long fieldId);
}
