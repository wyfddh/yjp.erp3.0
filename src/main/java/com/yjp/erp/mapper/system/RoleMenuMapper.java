package com.yjp.erp.mapper.system;

import com.yjp.erp.model.domain.RoleMenuDO;
import com.yjp.erp.model.po.system.Role;
import com.yjp.erp.model.po.system.RoleMenu;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author wyf
 * @date 2019/4/4 下午 3:43
 **/
@Repository
public interface RoleMenuMapper {
    /**
     * 根据角色删除菜单角色关联
     * @author wyf
     * @date  2019/4/12 下午 3:15
     * @param role 角色
     */
    void deleteMenuByRole(Role role);
    /**
     * 批量插入菜单角色关联
     * @author wyf
     * @date  2019/4/12 下午 3:15
     * @param roleMenuList 角色菜单列表
     */
    void insertList(List<RoleMenu> roleMenuList);
    /**
     * 根据角色列表查询角色菜单关系
     * @author wyf
     * @param roleList 角色列表
     * @return java.util.List<com.yjp.erp.model.domain.RoleMenuDO>
     */
    List<RoleMenuDO> listRoleMenuDOByRole(List<Role> roleList);
}
