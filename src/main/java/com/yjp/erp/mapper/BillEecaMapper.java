package com.yjp.erp.mapper;

import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.po.eeca.BillEeca;

import java.util.List;

public interface BillEecaMapper {
    BillEeca getBillEecaById(Long billEecaId);

    /**
     * ����ModuleId��ȡʵ����¼�
     * @param id ModuleId
     * @return ����ʵ����¼�
     */
    List<BillEeca> getBillEecaByModuleId(Long id);

    /**
     * ���������¼�
     * @param billEecas �¼�
     */
    void bathInsertBillEeca(List<BillEeca> billEecas);

    /**
     * ͨ��ʵ����Ϣ��ȡ�¼�����
     * @param module ʵ����Ϣ
     * @return ʵ���¼�����
     */
    List<BillEeca> getBillEecaList(Module module);
}
