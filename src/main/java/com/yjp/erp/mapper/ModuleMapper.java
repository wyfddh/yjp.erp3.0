package com.yjp.erp.mapper;

import com.yjp.erp.model.po.bill.Module;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ModuleMapper {

    /**
     * 根据实体的classId和typeId获取实体
     * @param record 实体信息
     * @return 返回实体
     */
    Module getModuleByClassIdAndTypeId(Module record);

    Module getModuleById(Long id);

    List<Module> listAllAvailableModules();

    int deleteModules(Module record);

    int insertModule(Module record);

    int updateModule(Module record);

    int updateModuleAndActive(Module record);

    int bathInsertModule(List<Module> modules);

    int bathUpdateModule(List<Module> modules);

    /**
     * 发布实体
     * @param module 实体信息
     * @return 更改数据量
     */
    int publishEntity(Module module);

    /**
     * 根据实体的classId和typeId判断实体表单typeId是否重复
     * @param module   实体的classId和typeId
     * @return typeId是否存在
     */
    int typeIdCheck(Module module);

    List<Module> getModulesByClassIdAndPublishState(String classId,Integer publishState);
}