package com.yjp.erp.model.dto.system;

import com.yjp.erp.model.po.system.Role;
import com.yjp.erp.model.vo.system.MenuVO;
import java.util.List;
import lombok.Data;

/**
 * @author wyf
 * @date 2019/4/4 下午 3:31
 **/
@Data
public class RoleMenuDTO {

    private Role role;

    private List<MenuVO> menuList;
}
