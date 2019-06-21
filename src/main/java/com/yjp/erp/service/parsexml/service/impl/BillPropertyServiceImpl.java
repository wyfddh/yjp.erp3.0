package com.yjp.erp.service.parsexml.service.impl;

import com.yjp.erp.mapper.BillPropertyMapper;
import com.yjp.erp.model.po.bill.BillProperty;
import com.yjp.erp.service.parsexml.service.BillPropertyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/28 20:39
 * @Email: jianghongping@yijiupi.com
 */
@Service
public class BillPropertyServiceImpl implements BillPropertyService {
    @Resource
    BillPropertyMapper billPropertyMapper;
    @Override
    public List<BillProperty> getBillProperiesByBillId(Long billId){
        return billPropertyMapper.getBillPropertyByBillId(billId);
    }
}
