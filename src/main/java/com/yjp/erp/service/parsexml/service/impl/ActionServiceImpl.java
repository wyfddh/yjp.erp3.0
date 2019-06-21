package com.yjp.erp.service.parsexml.service.impl;

import com.yjp.erp.mapper.parsexml.BillActionMapperP;
import com.yjp.erp.model.po.service.BillAction;
import com.yjp.erp.service.parsexml.service.ActionService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/23 17:57
 * @Email: jianghongping@yijiupi.com
 */
@org.springframework.stereotype.Service
public class ActionServiceImpl implements ActionService {

    @Resource
    private BillActionMapperP billActionMapperP;

    @Override
    public List<BillAction> listAcitonByMoudleId(Long moduleId) {
        return billActionMapperP.listBillActionByModuleId(moduleId);
    }
}
