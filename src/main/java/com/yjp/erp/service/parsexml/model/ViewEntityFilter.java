package com.yjp.erp.service.parsexml.model;

import lombok.Data;

@Data
public class ViewEntityFilter {
    private String label;
    private String classId;
    private Integer publishStatus;
    private Integer offset;
    private Integer size;

}
