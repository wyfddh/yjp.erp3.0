package com.yjp.erp.mapper.base;

import com.yjp.erp.model.po.bill.Module;

public interface BaseModuleMapper {

    Module getModuleByClassIdAndTypeId(Module record);

    Module getModuleById(Long id);

    int deleteModules(Module record);

    int insertModule(Module record);

    int updateModule(Module record);
}