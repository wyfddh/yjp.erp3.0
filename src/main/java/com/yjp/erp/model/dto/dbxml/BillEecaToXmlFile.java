package com.yjp.erp.model.dto.dbxml;

import com.yjp.erp.model.po.bill.Bill;
import com.yjp.erp.model.po.eeca.BillEeca;
import com.yjp.erp.model.po.service.Service;

import java.util.Map;

/**
 * @Description
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/3/27
 */
public class BillEecaToXmlFile {

    private Bill bill;

    private BillEeca eeca;

    private Map<Integer, Service> services;

    public BillEeca getEeca() {
        return eeca;
    }

    public void setEeca(BillEeca eeca) {
        this.eeca = eeca;
    }

    public Map<Integer, Service> getServices() {
        return services;
    }

    public void setServices(Map<Integer, Service> services) {
        this.services = services;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }
}
