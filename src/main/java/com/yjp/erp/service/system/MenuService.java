package com.yjp.erp.service.system;

import com.yjp.erp.conf.UserInfoManager;
import com.yjp.erp.constants.SystemConstant;
import com.yjp.erp.model.dto.system.MenuDTO;
import com.yjp.erp.model.dto.system.RoleDTO;
import com.yjp.erp.conf.exception.BusinessException;
import com.yjp.erp.mapper.system.MenuMapper;
import com.yjp.erp.model.po.system.Menu;
import com.yjp.erp.model.po.system.Role;
import com.yjp.erp.model.po.system.RoleMenu;
import com.yjp.erp.model.po.system.User;
import com.yjp.erp.util.ObjectUtils;
import com.yjp.erp.util.SnowflakeIdWorker;
import com.yjp.erp.model.vo.RetCode;
import com.yjp.erp.model.vo.system.MenuVO;
import java.sql.Timestamp;
import java.util.*;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wyf
 * @date 2019/4/3 下午 2:17
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class MenuService {

    @Autowired
    private UserInfoManager userInfoManager;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    /**
     * @param menuDTO 新增的菜单实体
     * @author wyf
     * @description 新增单个菜单
     * @date 2019/4/3 下午 4:12
     */
    public void insertMenu(MenuDTO menuDTO) throws Exception {
        if (ObjectUtils.isNotEmpty(menuDTO) && ObjectUtils.isNotEmpty(menuDTO.getTitle()) && ObjectUtils.isNotEmpty(menuDTO.getParentId())) {
            //校验菜单
            checkMenu(menuDTO);
            Menu menu = new Menu();
            BeanUtils.copyProperties(menuDTO,menu);
            //当前登录的用户
            String userId = userInfoManager.getUserInfo().getUserId();
            //当前时间
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            long id = SnowflakeIdWorker.nextId();
            menu.setId(id);
            menu.setCreateDate(timestamp);
            menu.setUpdateDate(timestamp);
            menu.setCreator(Long.valueOf(userId));
            menu.setUpdater(Long.valueOf(userId));
            //新增时一定是子节点
            menu.setNodeType(SystemConstant.MENU_SON_NOTE);
            //前端传递状态
            if (ObjectUtils.isEmpty(menu.getStatus())) {
                menu.setStatus(SystemConstant.NORMAL_STATUS);
            }
            menuMapper.insertMenu(menu);
            if (!Objects.equals(menu.getParentId(),SystemConstant.MENU_PARENT_ID)) {
                handleMenuRole(menu);
            }
            batchMenuNode();
        } else {
            throw new BusinessException(RetCode.PARAM_EMPTY,"参数为空");
        }
    }

    /**
     * @author wyf
     * @description 校验菜单
     * @param menuDTO 菜单入参
     */
    private void checkMenu(MenuDTO menuDTO) throws BusinessException{
        if (ObjectUtils.isNotEmpty(menuDTO.getTitle())) {
            //校验菜单名是否可用
            if (!checkMenuName(menuDTO.getTitle(),menuDTO.getId())) {
                throw new BusinessException(RetCode.DUPLICATE_MENUNAME,"菜单名重复");
            }
        }
        if (ObjectUtils.isNotEmpty(menuDTO.getRoutePath())) {
            //校验菜单路径是否可用
            if (!checkMenuPath(menuDTO.getRoutePath(),menuDTO.getId())) {
                throw new BusinessException(RetCode.DUPLICATE_MENUPATH,"菜单路径重复");
            }
        }
    }

    /**
     * @author wyf
     * @description   检验菜单名是否可用
     * @date  2019/4/19 下午 3:11
     * @param name 菜单名
     * @return java.lang.Boolean
     */
    private Boolean checkMenuName(String name, Long id) throws BusinessException{
        Map<String,Object> map = new HashMap<>(2);
        map.put("name",name);
        if (ObjectUtils.isNotEmpty(id)) {
            map.put("id",id);
        }
        int count = menuMapper.checkMenuName(map);
        return count == 0;
    }

    /**
     * @author wyf
     * @description  检测菜单路径是否可用
     * @date  2019/4/24 下午 3:17
     * @param path 菜单路径
     * @param id  菜单id
     * @return java.lang.Boolean
     */
    private Boolean checkMenuPath(String path,Long id) throws BusinessException{
        if (ObjectUtils.isEmpty(path)) {
            return true;
        }
        Map<String,Object> map = new HashMap<>(2);
        map.put("path",path);
        if (ObjectUtils.isNotEmpty(id)) {
            map.put("id",id);
        }
        int count = menuMapper.checkMenuPath(map);
        return count == 0;
    }

    /**
     * @description 修改单个菜单
     * @author wyf
     * @date 2019/4/3 下午 4:01
     * @param menuDTO 菜单实体
     * @param type  修改类型 1修改-1删除
     */
    public void updateMenu(MenuDTO menuDTO,int type) throws Exception {
        if (ObjectUtils.isNotEmpty(menuDTO) && ObjectUtils.isNotEmpty(menuDTO.getId())) {
            Menu menu = new Menu();
            BeanUtils.copyProperties(menuDTO,menu);
            //当前登录的用户
            Long userId = Long.valueOf(userInfoManager.getUserInfo().getUserId());
            //当前时间
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            menu.setUpdateDate(timestamp);
            menu.setUpdater(userId);
            if (SystemConstant.DELETE_STATUS == type) {
                //获取该菜单和其子节点的所有菜单
                List<Menu> menuList = listMenuById(menu.getId());
                List<Long> idList = new ArrayList<>();
                for (Menu me : menuList) {
                    idList.add(me.getId());
                }
                menuMapper.deleteMenu(idList);
            } else {
                //校验菜单
                checkMenu(menuDTO);
                menuMapper.updateMenuSelective(menu);
            }
            batchMenuNode();
        } else {
            throw new BusinessException(RetCode.PARAM_EMPTY,"参数为空");
        }
    }

    /**
     * @author wyf
     * @description  新增时，如果该菜单父级已经绑定角色，那么将该菜单也一并绑定
     * @date  2019/4/18 上午 10:25
     * @param menu 菜单
     */
    private void handleMenuRole(Menu menu) throws Exception{

        Long parentId = menu.getParentId();
        Menu pMenu = getMenuById(parentId);
        List<Role> roleList = roleService.listRoleByMenu(pMenu.getId());
        if (ObjectUtils.isNotEmpty(roleList)) {
            List<RoleMenu> roleMenuList = new ArrayList<>();
            for (Role role : roleList) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(role.getId());
                roleMenu.setMenuId(menu.getId());
                roleMenu.setId(SnowflakeIdWorker.nextId());
                roleMenuList.add(roleMenu);
            }
            roleService.batchInsertRoleMenu(roleMenuList);
        }
    }

    /**
     * @author wyf
     * @description  批量整体修改菜单的node状态
     * @date  2019/4/10 上午 11:07
     */
    private void batchMenuNode() throws Exception{
        List<MenuVO> menuVOList = listAllMenu();
        List<Menu> menuList = new ArrayList<>();
        for (MenuVO item : menuVOList) {
            Menu menu = new Menu();
            BeanUtils.copyProperties(item,menu);

            for (int i = 0;i < menuVOList.size();i++) {
                if (Objects.equals(menu.getId(),menuVOList.get(i).getParentId())) {
                    menu.setNodeType(SystemConstant.MENU_PARENT_NOTE);
                    menuList.add(menu);
                    break;
                }
                if (i == menuVOList.size() - 1 && !Objects.equals(menu.getId(),menuVOList.get(i).getParentId())) {
                    menu.setNodeType(SystemConstant.MENU_SON_NOTE);
                    menuList.add(menu);
                }
            }
        }
        menuMapper.batchUpdate(menuList);
    }

    /**
     * @author wyf
     * @description  查询所有菜单
     * @date  2019/4/3 下午 6:16
     * @return java.util.List<com.yjp.erp.model.po.system.Menu>
     */
    List<MenuVO> listAllMenu() throws Exception{
        List<Menu> menuList = menuMapper.listAllMenu();
        List<MenuVO> menuVOList = new ArrayList<>();
        copyMenu(menuList,menuVOList);
        return menuVOList;
    }

    /**
     * @author wyf
     * @description     处理菜单，返回vo
     * @date  2019/5/21 下午 3:20
     * @param menuList   需处理的菜单集合
     * @param menuVOList 需返回的vo集合
     */
    private void copyMenu(List<Menu> menuList,List<MenuVO> menuVOList) {

        for (Menu menu : menuList) {
            MenuVO menuVO = new MenuVO();
            BeanUtils.copyProperties(menu,menuVO);
            menuVO.setKey(menu.getId());
            menuVO.setValue(menu.getId());
            //如果该菜单是可展开的，那么屏蔽其路径使其不可跳转
            if (Objects.equals(SystemConstant.MENU_PARENT_NOTE,menuVO.getNodeType())) {
                menuVO.setRoutePath("");
                menuVO.setTypeId("");
                menuVO.setClassId("");
                menuVO.setTemplate("");
            }
            menuVOList.add(menuVO);
        }
    }

    /**
     * @author wyf
     * @description  获取所有菜单，返回菜单树
     * @date  2019/4/12 上午 9:41
     * @return com.yjp.erp.model.po.system.Menu
     */
    public MenuVO getAllMenu() throws Exception{

        List<MenuVO> menuVOList = listAllMenu();
        return handleMenu(menuVOList);
    }

    /**
     * @author wyf
     * @description  根据角色列表获取其菜单
     * @date  2019/4/4 上午 10:00
     * @param  roleList 角色列表
     * @return java.util.List<com.yjp.erp.model.po.system.Menu>
     */
    private MenuVO listMenuByRole(List<Role> roleList) throws Exception{

        List<Menu> menuList = menuMapper.listMenuByRole(roleList);
        List<MenuVO> menuVOList = new ArrayList<>();
        copyMenu(menuList,menuVOList);
        return handleMenu(menuVOList);
    }

    /**
     * @author wyf
     * @description  根据菜单id获取菜单
     * @date  2019/4/16 下午 8:08
     * @param id 菜单id
     * @return com.yjp.erp.model.po.system.Menu
     */
    Menu getMenuById(Long id) throws Exception{
        return menuMapper.getMenuById(id);
    }

    /**
     * @author wyf
     * @description  分配权限时查询该角色分配的菜单，已选菜单设置checked=true
     * @date  2019/4/10 下午 7:22
     * @param  roleDTO 传入角色
     * @return com.yjp.erp.model.po.system.Menu
     */
    public Map<String,Object> listMenuByRole(RoleDTO roleDTO) throws Exception{

        Role role = new Role();
        BeanUtils.copyProperties(roleDTO,role);
        List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        //总菜单
        List<MenuVO> menuVOList = listAllMenu();
        //查出该角色下的菜单
        List<Menu> menuList = menuMapper.listMenuByRole(roleList);
        List<String> idList = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(menuList)) {
            for (Menu menu : menuList) {
                //前端只需要传递子菜单，如果传递父菜单会渲染选中所有子的
                if (Objects.equals(menu.getNodeType(),SystemConstant.MENU_SON_NOTE)) {
                    idList.add(menu.getId().toString());
                }
            }
        }
        //构建menu
        MenuVO menu = handleMenu(menuVOList);
        Map<String,Object> map = new HashMap<>(4);
        map.put("menu",menu);
        map.put("checkedMenuList",idList);
        return map;
    }

    /**
     * @author wyf
     * @description  组装menulist，返回一个json格式的menuVO
     * @date  2019/4/4 上午 10:09
     * @param menuList 需要组装的数据集合
     * @return com.yjp.erp.model.po.system.Menu
     */
    private MenuVO handleMenu(List<MenuVO> menuList) throws Exception{
        //设置根节点
        MenuVO menu = new MenuVO();
        menu.setId(SystemConstant.MENU_PARENT_ID);
        menu.setTitle("ERP业务应用");
        menu.setKey(menu.getId());
        menu.setValue(menu.getId());
        menu.setStatus(SystemConstant.NORMAL_STATUS);
        //找到所有一级菜单
        List<MenuVO> pMenus = new ArrayList<>();
        for (MenuVO pMenu : menuList) {
            if (Objects.equals(pMenu.getParentId(),SystemConstant.MENU_PARENT_ID)) {
                pMenus.add(pMenu);
            }
        }
        //为一级菜单设置子菜单准备递归
        for (MenuVO me :pMenus) {
            //获取父菜单下所有子菜单调用getChildList
            List<MenuVO> childList = getChildList(me.getId(),menuList);
            me.setChildren(childList);
        }
        menu.setChildren(pMenus);
        return menu;
    }

    private List<MenuVO> getChildList(Long id, List<MenuVO> menuList) throws Exception{
        //构建子菜单
        List<MenuVO> childList = new ArrayList<>();
        //遍历传入的list
        for (MenuVO menu:menuList) {
            //所有菜单的父id与传入的根节点id比较，若相等则为该级菜单的子菜单
            if (Objects.equals(menu.getParentId(),id)){
                childList.add(menu);
            }
        }
        //递归
        for (MenuVO m:childList) {
            m.setChildren(getChildList(m.getId(),menuList));
        }
        if (childList.size() == 0){
            return null;
        }
        return childList;
    }

    /**
     * @author wyf
     * @description  获取给定菜单id的子节点所有菜单，包含自己
     * @date  2019/4/3 下午 5:15
     * @param id 菜单id
     * @return java.util.List<com.yjp.erp.model.po.system.Menu>
     */
    List<Menu> listMenuById(Long id) throws Exception {
        //查询所有菜单
        List<MenuVO> menuVOList = listAllMenu();
        List<Menu> menuList = new ArrayList<>();
        for (MenuVO menuVO : menuVOList) {
            Menu menu = new Menu();
            BeanUtils.copyProperties(menuVO,menu);
            menuList.add(menu);
        }
        List<Menu> sonMenuList = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        Menu menu = null ;
        Iterator<Menu> iter = menuList.iterator();
        while(iter.hasNext()){
            Menu newMenu = iter.next();
            if (Objects.equals(newMenu.getId(),id)) {
                menu = newMenu;
            }
            if(ObjectUtils.isNotEmpty(newMenu.getParentId()) && Objects.equals(newMenu.getParentId(),id)){
                sonMenuList.add(newMenu);
                ids.add(newMenu.getId());
                iter.remove();
            }

        }
        List<Menu> recursion = recursion(menuList,ids,sonMenuList);
        recursion.add(menu);
        return recursion;
    }

    /**
     * @author wyf
     * @description  递归方法查询该层菜单及其存在的下级所有菜单
     * @date  2019/4/12 下午 4:11
     * @param menuList 全部菜单集合
     * @param ids  上层id集合
     * @param sonMenuList 子菜单集合
     * @return java.util.List<com.yjp.erp.model.po.system.Menu>
     */
    private  List<Menu> recursion(List<Menu> menuList,List<Long> ids,List<Menu> sonMenuList) throws Exception{
        if (ids.size() > 0) {
            List<Long> newIds = new ArrayList<>();
            Iterator<Menu> iter = menuList.iterator();
            while(iter.hasNext()){
                Menu menu = iter.next();
                for (Long id : ids) {
                    if(menu.getParentId()!=null && menu.getParentId().equals(id)){
                        sonMenuList.add(menu);
                        newIds.add(id);
                        iter.remove();
                    }
                }
            }
            return recursion(menuList,newIds,sonMenuList);
        } else {
            return sonMenuList;
        }
    }

    /**
     * @author wyf
     * @description  根据用户id获取其菜单
     * @date  2019/4/4 下午 5:26
     * @param userId 用户id
     * @return com.yjp.erp.model.po.system.Menu
     */
    public MenuVO getMenuByUserId(Long userId) throws Exception{
        List<Role> roleList;
        MenuVO menuVO;
        User user = userService.getUserById(userId);
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException(RetCode.INTERNAL_SERVER_ERROR);
        }
        if (Objects.equals(user.getMark(),SystemConstant.ADMIN_MARK)) {
            List<MenuVO> menuVOList = listAllMenu();
            menuVO = handleMenu(menuVOList);
            menuVO.setKey(menuVO.getId());
            menuVO.setValue(menuVO.getId());
            return menuVO;
        } else {
            roleList = roleService.listRolesByUserId(userId);
        }
        if (ObjectUtils.isNotEmpty(roleList)) {
            menuVO = listMenuByRole(roleList);
        } else {
            menuVO = new MenuVO();
        }
        menuVO.setKey(menuVO.getId());
        menuVO.setValue(menuVO.getId());
        return menuVO;
    }
    /**
     * @author wyf
     * @description  模糊搜索定位菜单
     * @date  2019/4/16 下午 9:20
     * @param name 搜索的值
     * @return com.yjp.erp.model.vo.system.MenuVO
     */
    public List<String> likeMenuTree(String name) throws Exception{

        List<MenuVO> menuVOList = listAllMenu();
        List<String> idlist = new ArrayList<>();
        for (MenuVO menuVO : menuVOList) {
            if (menuVO.getTitle().contains(name)) {
                if (Objects.equals(menuVO.getParentId(),SystemConstant.MENU_PARENT_ID)) {
                    continue;
                }
                idlist.add(menuVO.getParentId().toString());
                recurMenuTree(menuVOList,idlist,menuVO.getParentId());
            }
        }
        //去重
        LinkedHashSet<String> set = new LinkedHashSet<>(idlist.size());
        set.addAll(idlist);
        idlist.clear();
        idlist.addAll(set);
        return idlist;
    }

    private void recurMenuTree (List<MenuVO> menuVOList,List<String> idlist,Long id) throws Exception{

        for (MenuVO menuVO : menuVOList) {
            if (Objects.equals(id,menuVO.getId())) {
                if (Objects.equals(menuVO.getParentId(),SystemConstant.MENU_PARENT_ID)) {
                    continue;
                }
                idlist.add(menuVO.getParentId().toString());
                recurMenuTree(menuVOList,idlist,menuVO.getParentId());
            }
        }
    }
}