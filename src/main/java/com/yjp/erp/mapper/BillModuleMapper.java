package com.yjp.erp.mapper;

import com.yjp.erp.model.po.bill.BillModule;

public interface BillModuleMapper {
    Long deleteByPrimaryKey(Long id);

    Long insert(BillModule record);

    Long insertSelective(BillModule record);

    BillModule selectByPrimaryKey(Long id);

    Long updateByPrimaryKeySelective(BillModule record);

    Long updateByPrimaryKey(BillModule record);
}