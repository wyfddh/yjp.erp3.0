package com.yjp.erp.model.po.system;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wyf
 * @date 2019/4/2 下午 5:45
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleMenu extends BaseSys{
    private Long menuId;

    private Long roleId;
}
