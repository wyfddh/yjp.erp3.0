package com.yjp.erp.model.po.system;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wyf
 * @date 2019/4/8 上午 11:29
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class UserOrg extends BaseSys{
    private Long userId;

    private Long orgId;
}
