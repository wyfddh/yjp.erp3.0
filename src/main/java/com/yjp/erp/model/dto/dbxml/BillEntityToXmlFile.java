package com.yjp.erp.model.dto.dbxml;

import com.yjp.erp.model.po.bill.Bill;
import com.yjp.erp.model.po.bill.BillField;
import com.yjp.erp.model.po.bill.BillFieldProperty;
import com.yjp.erp.model.po.bill.BillProperty;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/3/20
 */
public class BillEntityToXmlFile {

    private List<BillProperty> billPropertity;
    private List<BillField> billField;
    private Map<Long, List<BillFieldProperty>> billFieldProperty;
    private BillEntityToXmlFile billDetail;
    private Bill bill;

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public BillEntityToXmlFile getBillDetail() {
        return billDetail;
    }

    public void setBillDetail(BillEntityToXmlFile billDetail) {
        this.billDetail = billDetail;
    }

    public List<BillProperty> getBillPropertity() {
        return billPropertity;
    }

    public void setBillPropertity(List<BillProperty> billPropertity) {
        this.billPropertity = billPropertity;
    }

    public List<BillField> getBillField() {
        return billField;
    }

    public void setBillField(List<BillField> billField) {
        this.billField = billField;
    }

    public Map<Long, List<BillFieldProperty>> getBillFieldProperty() {
        return billFieldProperty;
    }

    public void setBillFieldProperty(Map<Long, List<BillFieldProperty>> billFieldProperty) {
        this.billFieldProperty = billFieldProperty;
    }
}
