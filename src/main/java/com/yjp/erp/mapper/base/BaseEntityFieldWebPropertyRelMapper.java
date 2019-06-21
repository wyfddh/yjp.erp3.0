package com.yjp.erp.mapper.base;

import com.yjp.erp.model.po.bill.BillFieldWebPropertyRel;

import java.util.List;
import java.util.Map;

public interface BaseEntityFieldWebPropertyRelMapper {

    void bathInsertFieldsRefs(List<BillFieldWebPropertyRel> billFieldWebPropertyRels);

    Map<String,Object> getRefProperties(Long id);
}