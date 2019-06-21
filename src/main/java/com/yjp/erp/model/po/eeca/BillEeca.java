package com.yjp.erp.model.po.eeca;

import com.yjp.erp.model.po.service.Service;
import lombok.Data;

@Data
public class BillEeca{

    private Long id;

    private Long moduleId;

    private String label;

    private String eventType;

    private String eTrigger;

    private String eCondition;

    private String serviceName;

    private String eMethod;

    private Service refService;

    private String eecaId;

    private String otherOptions;
}