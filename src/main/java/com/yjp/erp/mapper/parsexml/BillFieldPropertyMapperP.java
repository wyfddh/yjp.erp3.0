package com.yjp.erp.mapper.parsexml;

import com.yjp.erp.model.po.bill.BillFieldProperty;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/29 13:49
 * @Email: jianghongping@yijiupi.com
 */
@Repository
public interface BillFieldPropertyMapperP {
    List<BillFieldProperty> getBillFieldPropertiesByFieldId(Long fieldId);
}
