package com.yjp.erp.mapper.system;

import com.yjp.erp.model.po.system.Role;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 * @author wyf
 * @date 2019/4/2 下午 6:10
 **/
@Repository
public interface RoleMapper {
    /**
     * 根据用户id查询其角色
     * @author wyf
     * @date  2019/4/12 下午 3:10
     * @param userId 用户id
     * @return java.util.List<com.yjp.erp.model.po.system.Role>
     */
    List<Role> listRolesByUserId(Long userId);
    /**
     * 根据角色名准确获取角色
     * @author wyf
     * @date  2019/4/12 下午 3:10
     * @param roleName 角色名
     * @return com.yjp.erp.model.po.system.Role
     */
    Role getRoleByName(String roleName);
    /**
     * 新增角色
     * @author wyf
     * @date  2019/4/12 下午 3:12
     * @param  role 角色
     */
    void insertRole(Role role);
    /**
     * 更新角色
     * @author wyf
     * @date  2019/4/12 下午 3:12
     * @param role 角色
     */
    void updateRole(Role role);
    /**
     * 检测角色，返回除自己外的角色数量
     * @author wyf
     * @date  2019/4/12 下午 3:12
     * @param role 角色
     * @return int
     */
    int checkRole(Role role);
    /**
     * 根据条件查询角色列表
     * @author wyf
     * @date  2019/4/12 下午 3:14
     * @param map 条件
     * @return java.util.List<com.yjp.erp.model.po.system.Role>
     */
    List<Role> listAllRoles(Map<String,Object> map);

    /**
     * 根据条件获取角色总数
     * @author wyf
     * @date  2019/4/12 下午 3:15
     * @param map 条件
     * @return int
     */
    int countRolesByMap(Map<String, Object> map);
    /**
     * 根据菜单获取角色
     * @author wyf
     * @param menuId 菜单id
     * @return java.util.List<com.yjp.erp.model.po.system.Role>
     */
    List<Role> listRoleByMenuId(Long menuId);
}
