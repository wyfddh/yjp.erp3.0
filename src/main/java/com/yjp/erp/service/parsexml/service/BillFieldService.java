package com.yjp.erp.service.parsexml.service;

import com.yjp.erp.model.po.bill.BillField;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/28 15:23
 * @Email: jianghongping@yijiupi.com
 */
@Service
public interface BillFieldService {
    List<BillField> listBillFieldsByBillId(Long billId);
}
