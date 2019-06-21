package com.yjp.erp.mapper.parsexml;

import com.yjp.erp.model.po.service.BillAction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillActionMapperP {
    List<BillAction> listBillActionByModuleId(Long moduleId);
}
