package com.yjp.erp.mapper.system;

import com.yjp.erp.model.po.system.Menu;
import com.yjp.erp.model.po.system.Role;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 * @author wyf
 * @date 2019/4/3 下午 2:18
 **/
@Repository
public interface MenuMapper {

    /**
     * 插入单个菜单
     * @author wyf
     * @date  2019/4/12 下午 3:05
     * @param menu 菜单
     */
    void insertMenu(Menu menu);
    /**
     * 有选择的更新菜单
     * @author wyf
     * @date  2019/4/12 下午 3:05
     * @param menu 菜单
     */
    void updateMenuSelective(Menu menu);
    /**
     * 删除给定的菜单集合
     * @author wyf
     * @date  2019/4/12 下午 3:05
     * @param list 菜单id集合
     */
    void deleteMenu(List<Long> list);
    /**
     * 获取所有菜单列表
     * @author wyf
     * @date  2019/4/12 下午 3:06
     * @return java.util.List<com.yjp.erp.model.po.system.Menu>
     */
    List<Menu> listAllMenu();
    /**
     * 根据角色列表获取菜单
     * @author wyf
     * @date  2019/4/12 下午 3:06
     * @param roleList 角色列表
     * @return java.util.List<com.yjp.erp.model.po.system.Menu>
     */
    List<Menu> listMenuByRole(List<Role> roleList);
    /**
     * 根据id拿到菜单
     * @author wyf
     * @date  2019/4/12 下午 3:09
     * @param id 菜单id
     * @return com.yjp.erp.model.po.system.Menu
     */
    Menu getMenuById(Long id);
    /**
     * 批量更新菜单
     * @author wyf
     * @date  2019/4/12 下午 3:09
     * @param menuList 菜单列表
     */
    void batchUpdate(List<Menu> menuList);
    /**
     * 根据map条件查询菜单名
     * @author wyf
     * @param map 查询条件
     * @return int
     */
    int checkMenuName(Map<String,Object> map);
    /**
     * 根据map条件查询菜单路径
     * @author wyf
     * @param map 查询条件
     * @return int
     */
    int checkMenuPath(Map<String, Object> map);
}
