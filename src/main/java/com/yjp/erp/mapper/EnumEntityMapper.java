package com.yjp.erp.mapper;

import com.yjp.erp.model.dto.bill.EnumEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface EnumEntityMapper {
    void insertEnumEntity(EnumEntity enumEntity);

    EnumEntity getEnumEntityByClassIdTypeId(EnumEntity enumEntity);
}
