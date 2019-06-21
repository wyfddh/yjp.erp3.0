package com.yjp.erp.mapper.parsexml;

import com.yjp.erp.model.po.eeca.BillEeca;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BillEecaMapperP {
    List<BillEeca> listBillEecaByModuleId(Long moduleId);
}
