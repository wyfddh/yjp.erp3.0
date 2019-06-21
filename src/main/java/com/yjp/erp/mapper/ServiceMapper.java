package com.yjp.erp.mapper;

import com.yjp.erp.model.domain.ActionDO;
import com.yjp.erp.model.domain.ServicePropertyDO;
import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.po.service.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/3/22
 */
public interface ServiceMapper {
    Service getServiceById(Long id);

    void bathInsertService(List<Service> services);

    /**
     * 获取script模板
     * @return 返回服务的script模板
     */
    String getScriptDemo();

    /**
     * 获取action默认模板
     * @return 返回action默认模板
     */
    List<ActionDO> listActions();

    /**
     * 获取service默认模板
     * @return 返回service默认模板
     */
    List<ServicePropertyDO> getCommonServices();

    /**
     * 获取实体的服务
     * @param module 实体classId和typeId
     * @return 与行为存在关联的服务
     */
    List<Map<String,Object>> listActionService(Module module);


    /**
     * 获取实体的服务
     * @param module 实体classId和typeId
     * @return 与服务存在关系的ecac
     */
    List<Map<String,Object>> listEecaService(Module module);

}
