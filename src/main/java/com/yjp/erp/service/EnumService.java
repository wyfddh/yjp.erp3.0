package com.yjp.erp.service;

import com.yjp.erp.mapper.EnumMapper;
import com.yjp.erp.model.po.bill.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * description:
 *
 * @author liushui
 * @date 2019/4/17
 */
@Service
public class EnumService {

    @Autowired
    private EnumMapper enumMapper;

    /**
     * 根据实体classId查询枚举
     * @param classId 实体classId
     * @return 枚举信息
     */
    public List<Map<String,Object>> listEnums(String classId){

        return enumMapper.listEnums(classId);
    }

    public List<Map<String,Object>> listFields(Module module){

        return enumMapper.listFields(module);
    }
}
