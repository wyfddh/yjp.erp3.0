package com.yjp.erp.model.po.system;

import lombok.Data;

/**
 * @author wyf
 * @date 2019/6/4 上午 11:27
 **/
@Data
public class VorgRelateVorg {
    private Long id;

    private Long virtualOrgId;

    private Long relateVirtualOrgId;
}
