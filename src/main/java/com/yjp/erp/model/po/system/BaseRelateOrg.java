package com.yjp.erp.model.po.system;

import lombok.Data;

/**
 * @author wyf
 * @date 2019/4/23 上午 10:56
 **/
@Data
public class BaseRelateOrg {
    private Long id;

    private Long baseOrgId;

    private Long virtualOrgId;
}
