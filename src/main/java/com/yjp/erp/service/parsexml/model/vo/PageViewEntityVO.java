package com.yjp.erp.service.parsexml.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageViewEntityVO {
    private Integer pageSize;
    private Integer total;
    private Integer pageNo;
    private List<ViewEntityListVO> dataList;
}
