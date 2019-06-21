package com.yjp.erp.model.dto.bill;

import com.yjp.erp.model.domain.BillDO;
import com.yjp.erp.model.domain.ServiceDO;
import lombok.Data;

@Data
public class EntityDO {
    private BillDO billDO;
    private ServiceDO serviceDO;
}
