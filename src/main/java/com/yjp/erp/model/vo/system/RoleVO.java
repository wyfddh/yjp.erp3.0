package com.yjp.erp.model.vo.system;

import com.yjp.erp.model.po.system.BaseSys;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wyf
 * @date 2019/4/20 下午 2:17
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleVO extends BaseSys{

    private String role;

    private String description;

    private List<String> menuList;
}
