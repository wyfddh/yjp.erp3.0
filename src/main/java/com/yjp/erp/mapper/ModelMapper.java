package com.yjp.erp.mapper;

import com.yjp.erp.model.domain.ServicePropertyDO;
import com.yjp.erp.model.po.bill.BillField;

import java.util.List;

public interface ModelMapper {

    List<BillField> getModelMoquiProperties();

    List<BillField> getModelWebProperties();

    List<ServicePropertyDO> getGlobalServices();
}
