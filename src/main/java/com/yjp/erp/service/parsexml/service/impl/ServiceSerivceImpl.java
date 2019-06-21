package com.yjp.erp.service.parsexml.service.impl;

import com.yjp.erp.mapper.parsexml.ServiceMapperP;
import com.yjp.erp.model.po.service.Service;
import com.yjp.erp.service.parsexml.service.ServiceServiceP;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/23 17:57
 * @Email: jianghongping@yijiupi.com
 */
@org.springframework.stereotype.Service
public class ServiceSerivceImpl implements ServiceServiceP {

    @Resource
    ServiceMapperP serviceMapperP;

    @Override
    public List<Service> listServiceByModuleId(Long moduleId) {
        return serviceMapperP.listServiceByModuleId(moduleId);
    }
}
