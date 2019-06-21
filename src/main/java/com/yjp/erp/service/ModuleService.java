package com.yjp.erp.service;

import com.yjp.erp.mapper.ModuleMapper;
import com.yjp.erp.model.po.bill.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author liushui
 * @description:
 * @date 2019/3/25
 */
@Service
public class ModuleService {

    private final ModuleMapper moduleMapper;

    @Autowired
    public ModuleService(ModuleMapper moduleMapper) {
        this.moduleMapper = moduleMapper;
    }

    /**
     * 根据classId 和typeId 来获取Module实体
     * @param module classId 和typeId
     * @return 返回实体
     */
    public Module getModuleByClassIdAndTypeId(Module module){
        return moduleMapper.getModuleByClassIdAndTypeId(module);
    }

    public List<Module> getModulesByClassIdAndPublishState(String classId,Integer publishState){
       return moduleMapper.getModulesByClassIdAndPublishState(classId,publishState);
    }

    public void deleteModules(Module module){

        moduleMapper.deleteModules(module);
    }

    public void insertModule(Module module){
        moduleMapper.insertModule(module);
    }

    public void updateModule(Module module){
        moduleMapper.updateModule(module);
    }

    public void updateModuleAndActive(Module record){
        moduleMapper.updateModuleAndActive(record);
    }


    public void bathInsertModule(List<Module> modules){

        moduleMapper.bathInsertModule(modules);
    }

    public void bathUpdateModule(List<Module> modules){
        moduleMapper.bathUpdateModule(modules);
    }

    public void publishEntity(Module module){
        moduleMapper.publishEntity(module);
    }

    public Module getMoudleById(Long moduleId){
        return moduleMapper.getModuleById(moduleId);
    }

    /**
     * 根据实体的classId和typeId判断实体表单typeId是否重复
     * @param module   实体的classId和typeId
     * @return typeId是否重复
     */
    public Boolean typeIdCheck(Module module){
        return moduleMapper.typeIdCheck(module) > 0;
    }
}
