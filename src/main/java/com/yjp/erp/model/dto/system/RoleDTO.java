package com.yjp.erp.model.dto.system;

import com.yjp.erp.model.dto.PageDTO;
import com.yjp.erp.model.po.system.BaseSys;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wyf
 * @date 2019/4/11 下午 8:52
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleDTO extends BaseSys{

    private PageDTO page;

    private String role;

    private String description;

    private Long userId;
}
