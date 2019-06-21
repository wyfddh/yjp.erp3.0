package com.yjp.erp.mapper;

import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.po.service.BillAction;

import java.util.List;

/**
 * ʵ����Ϊmapper
 * @author wuyizhe@yijiupi.cn
 * @date 2019/3/23
 */
public interface BillActionMapper {


    /**
     * ͨ����Ϊid��ȡʵ�����Ϊ
     * @param id actionId
     * @return ����ʵ�����Ϊ����
     */
    BillAction getBillActionById(Long id);

    /**
     * ͨ��ModuleId��ȡʵ�����Ϊ
     * @param id ModuleId
     * @return ����ʵ�����Ϊ����
     */
    List<BillAction> getBillActionByModuleId(Long id);

    /**
     * ��������action����
     * @param billActions ��Ϊ����
     */
    void bathInsertBillAction(List<BillAction> billActions);

    /**
     * ��ȡaction����
     * @param module ʵ����Ϣ
     * @return action����
     */
    List<BillAction> getBillActionList(Module module);
}
