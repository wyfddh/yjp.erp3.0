package com.yjp.erp.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author liushui
 * @description:
 * @date 2019/3/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageDTO extends BaseDTO {

    private Integer pageNo = 1;

    private Integer pageSize = 20;

    private Integer total = 0;

    private Integer orgType;

    public PageDTO() {

    }

    public Integer getMysqlStart(){
        return  this.getPageSize() * (this.getPageNo() - 1);
    }

    public Integer getMoquiStart() {
        return this.getPageNo() - 1;
    }

}
