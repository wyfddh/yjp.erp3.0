package com.yjp.erp.mapper;

import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.po.service.BillAction;

import java.util.List;

/**
 * 实体行为mapper
 * @author wuyizhe@yijiupi.cn
 * @date 2019/3/23
 */
public interface BillActionMapper {


    /**
     * 通过行为id获取实体的行为
     * @param id actionId
     * @return 返回实体的行为数据
     */
    BillAction getBillActionById(Long id);

    /**
     * 通过ModuleId获取实体的行为
     * @param id ModuleId
     * @return 返回实体的行为数据
     */
    List<BillAction> getBillActionByModuleId(Long id);

    /**
     * 批量插入action数据
     * @param billActions 行为数据
     */
    void bathInsertBillAction(List<BillAction> billActions);

    /**
     * 获取action数据
     * @param module 实体信息
     * @return action数据
     */
    List<BillAction> getBillActionList(Module module);
}
