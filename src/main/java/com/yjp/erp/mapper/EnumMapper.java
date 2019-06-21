package com.yjp.erp.mapper;

import com.yjp.erp.model.po.bill.Module;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface EnumMapper {


    /**
     * 根据实体classId查询枚举
     * @param classId 实体classId
     * @return 枚举信息
     */
    List<Map<String,Object>> listEnums(String classId);

    List<Map<String,Object>> listFields(Module module);

    String getEnumNameByClassIdAndTypeId(Module module);
}
