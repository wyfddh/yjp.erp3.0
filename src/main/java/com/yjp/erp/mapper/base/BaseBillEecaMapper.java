package com.yjp.erp.mapper.base;

import com.yjp.erp.model.po.eeca.BillEeca;

import java.util.List;

public interface BaseBillEecaMapper {
    BillEeca getBillEecaById(Long billEecaId);

    void bathInsertBillEeca(List<BillEeca> billEecas);
}
