package com.yjp.erp.service.parsexml.service.impl;

import com.yjp.erp.mapper.BillFieldMapper;
import com.yjp.erp.model.po.bill.BillField;
import com.yjp.erp.service.parsexml.service.BillFieldService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/28 15:23
 * @Email: jianghongping@yijiupi.com
 */
@Service
public class BillFieldServiceImpl implements BillFieldService {
    @Resource
    private BillFieldMapper billFieldMapper;

    @Override
    public List<BillField> listBillFieldsByBillId(Long billId) {
       return billFieldMapper.getBillFieldByBillId(billId);
    }
}
