package com.yjp.erp.model.po.system;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wyf
 * @date 2019/4/2 下午 5:42
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class UserRole extends BaseSys {
    private Long roleId;

    private String roleName;

    private Long userId;
}
