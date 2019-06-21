package com.yjp.erp.service.parsexml.service;

import com.yjp.erp.model.po.service.BillAction;

import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/24 20:00
 * @Email: jianghongping@yijiupi.com
 */
public interface ActionService {
    List<BillAction> listAcitonByMoudleId(Long moduleId);
}
