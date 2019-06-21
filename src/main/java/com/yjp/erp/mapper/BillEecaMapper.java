package com.yjp.erp.mapper;

import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.po.eeca.BillEeca;

import java.util.List;

public interface BillEecaMapper {
    BillEeca getBillEecaById(Long billEecaId);

    /**
     * 根据ModuleId获取实体的事件
     * @param id ModuleId
     * @return 返回实体的事件
     */
    List<BillEeca> getBillEecaByModuleId(Long id);

    /**
     * 批量插入事件
     * @param billEecas 事件
     */
    void bathInsertBillEeca(List<BillEeca> billEecas);

    /**
     * 通过实体信息获取事件数据
     * @param module 实体信息
     * @return 实体事件数据
     */
    List<BillEeca> getBillEecaList(Module module);
}
