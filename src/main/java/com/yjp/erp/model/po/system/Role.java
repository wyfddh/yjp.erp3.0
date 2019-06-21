package com.yjp.erp.model.po.system;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wyf
 * @date 2019/4/2 下午 5:44
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class Role extends BaseSys{

    private String role;

    private String description;

    private Long userId;

}
