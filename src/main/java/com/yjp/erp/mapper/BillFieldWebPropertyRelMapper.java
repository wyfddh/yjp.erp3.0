package com.yjp.erp.mapper;

import com.yjp.erp.model.po.bill.BillFieldWebPropertyRel;

import java.util.List;
import java.util.Map;

public interface BillFieldWebPropertyRelMapper {

    void bathInsertFieldsRefs(List<BillFieldWebPropertyRel> billFieldWebPropertyRels);

    Map<String,Object> getRefProperties(Long id);
}