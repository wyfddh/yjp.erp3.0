package com.yjp.erp.service.parsexml.service.impl;

import com.yjp.erp.mapper.parsexml.BillFieldPropertyMapperP;
import com.yjp.erp.model.po.bill.BillFieldProperty;
import com.yjp.erp.service.parsexml.service.BillFieldPropertyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/28 16:07
 * @Email: jianghongping@yijiupi.com
 */
@Service
class BillFieldPropertyServiceImpl implements BillFieldPropertyService {
    @Resource
    private BillFieldPropertyMapperP billFieldPropertyMapperP;

    @Override
    public List<BillFieldProperty> getBillFieldPropertiesByFieldId(Long billId) {
        return billFieldPropertyMapperP.getBillFieldPropertiesByFieldId(billId);
    }
}
