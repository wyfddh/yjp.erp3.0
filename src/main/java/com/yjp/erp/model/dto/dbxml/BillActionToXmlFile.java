package com.yjp.erp.model.dto.dbxml;

import com.yjp.erp.model.po.service.BillAction;
import com.yjp.erp.model.po.service.Service;

import java.util.Map;

/**
 * @Description
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/3/23
 */
public class BillActionToXmlFile {

    private BillAction action;

    private Map<Integer, Service> services;

    public BillAction getAction() {
        return action;
    }

    public void setAction(BillAction action) {
        this.action = action;
    }

    public Map<Integer, Service> getServices() {
        return services;
    }

    public void setServices(Map<Integer, Service> services) {
        this.services = services;
    }
}
