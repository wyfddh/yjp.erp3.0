package com.yjp.erp.model.domain;

import lombok.Data;

import java.util.List;

/**
 * @author liushui
 * @description:
 * @date 2019/3/22
 */
@Data
public class EventDO {

    private String eventName;

    private String eventType;

    private List<String> methods;

    private Boolean runBefore;

    private Boolean runOnError;

    private String condition;

    private String serviceName;

    private Long serviceId;

    private Boolean getEntireEntity;

    private Boolean getOriginalValue;

    private Boolean setResults;

    private String label;

}
