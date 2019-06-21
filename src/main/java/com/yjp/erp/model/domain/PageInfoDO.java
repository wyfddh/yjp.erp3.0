package com.yjp.erp.model.domain;

import lombok.Data;

/**
 * description:
 *
 * @author liushui
 * @date 2019/4/18
 */
@Data
public class PageInfoDO {

    private Integer pageSize;

    private Integer pageIndex;

    /**
     * 总条数
     */
    private Integer total;
}
