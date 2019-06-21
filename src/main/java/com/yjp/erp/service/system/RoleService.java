package com.yjp.erp.service.system;

import com.yjp.erp.conf.UserInfoManager;
import com.yjp.erp.constants.SystemConstant;
import com.yjp.erp.model.domain.RoleMenuDO;
import com.yjp.erp.model.dto.PageDTO;
import com.yjp.erp.model.dto.system.RoleDTO;
import com.yjp.erp.model.dto.system.RoleMenuDTO;
import com.yjp.erp.model.dto.system.UserRoleDTO;
import com.yjp.erp.conf.exception.BusinessException;
import com.yjp.erp.mapper.system.MenuMapper;
import com.yjp.erp.mapper.system.RoleMapper;
import com.yjp.erp.mapper.system.RoleMenuMapper;
import com.yjp.erp.mapper.system.UserRoleMapper;
import com.yjp.erp.model.po.system.*;
import com.yjp.erp.util.ObjectUtils;
import com.yjp.erp.util.SnowflakeIdWorker;
import com.yjp.erp.model.vo.PageListVO;
import com.yjp.erp.model.vo.RetCode;
import com.yjp.erp.model.vo.system.MenuVO;
import com.yjp.erp.model.vo.system.RoleVO;
import com.yjp.erp.model.vo.system.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

/**
 * @author wyf
 * @date 2019/4/2 下午 6:08
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private UserInfoManager userInfoManager;

    /**
     * @author wyf
     * @description  根据用户id获取该用户正常的角色
     * @date  2019/4/3 下午 7:52
     * @param userId 用户id
     * @return java.util.List<com.yjp.erp.model.po.system.Role>
     */
    public List<Role> listRolesByUserId(Long userId) throws Exception{

        return roleMapper.listRolesByUserId(userId);
    }

    /**
     * @author wyf
     * @description  获取当前用户正常的角色
     * @date  2019/4/12 下午 4:02
     * @return java.util.List<com.yjp.erp.model.po.system.Role>
     */
    public List<Role> listCurrentRole() throws Exception{

        Long userId = Long.valueOf(userInfoManager.getUserInfo().getUserId());
        return listRolesByUserId(userId);
    }

    /**
     * @author wyf
     * @description  分页获取所有角色
     * @date  2019/4/9 上午 9:58
     * @param pageDTO 分页对象
     * @param type 1查询正常状态2查询正常和冻结
     * @return java.util.List<com.yjp.erp.model.po.system.Role>
     */
    public PageListVO<RoleVO> listAllRole(PageDTO pageDTO,int type) throws Exception{

        Map<String,Object> map = new HashMap<>(8);
        List<Integer> list = new ArrayList<>();
        if (type == SystemConstant.NORMAL_TYPE) {
            list.add(SystemConstant.NORMAL_STATUS);
            map.put("status",list);
        } else if (type == SystemConstant.ALL_TYPE) {
            list.add(SystemConstant.NORMAL_STATUS);
            list.add(SystemConstant.FROZEN_STATUS);
            map.put("status",list);
        }
        //查询总条数
        int total = roleMapper.countRolesByMap(map);
        pageDTO.setTotal(total);
        int start = pageDTO.getMoquiStart();
        map.put("start",start);
        map.put("end",start + pageDTO.getPageSize());
        List<Role> roleList = roleMapper.listAllRoles(map);
        List<RoleVO> roleVOList = new ArrayList<>();
        List<RoleMenuDO> roleMenuDOList;

        if (ObjectUtils.isNotEmpty(roleList)) {
            roleMenuDOList = roleMenuMapper.listRoleMenuDOByRole(roleList);
        } else {
            roleMenuDOList = new ArrayList<>();
        }
        handleRoleVOList(roleVOList,roleList,roleMenuDOList);
        pageDTO.setTotal(total);
        PageListVO<RoleVO> rolePageListVO = new PageListVO<>();
        rolePageListVO.setPageDTO(pageDTO);
        rolePageListVO.setList(roleVOList);
        return rolePageListVO;
    }

    /**
     * @author wyf
     * @description     处理角色，返回前端需要的结构
     * @param roleVOList 返回的角色列表结构
     * @param roleList    查询的角色结果集
     * @param roleMenuDOList  角色菜单集合
     */
    private void handleRoleVOList(List<RoleVO> roleVOList,List<Role> roleList,List<RoleMenuDO> roleMenuDOList) {

        for (Role role : roleList) {
            RoleVO roleVO = new RoleVO();
            BeanUtils.copyProperties(role,roleVO);
            List<String> menuNameList = new ArrayList<>();
            for (RoleMenuDO roleMenuDO : roleMenuDOList) {
                //前端需要一级节点显示
                if (Objects.equals(roleMenuDO.getRoleId(),role.getId())
                        && Objects.equals(roleMenuDO.getParentId(),SystemConstant.MENU_PARENT_ID)) {
                    menuNameList.add(roleMenuDO.getMenuName());
                }
            }
            roleVO.setMenuList(menuNameList);
            roleVOList.add(roleVO);
        }
    }

    /**
     * @author wyf
     * @description  模糊查询角色
     * @date  2019/4/11 下午 8:53
     * @param roleDTO 角色对象
     */
    public PageListVO<Role> likeRole(RoleDTO roleDTO) throws Exception{

        if (ObjectUtils.isEmpty(roleDTO)) {
            throw new BusinessException(RetCode.PARAM_EMPTY,"参数为空");
        }
        PageDTO page = roleDTO.getPage();
        String role = roleDTO.getRole();
        Map<String,Object> map = new HashMap<>(8);
        if (ObjectUtils.isNotEmpty(role)) {
            map.put("role",role);
        }
        int total = roleMapper.countRolesByMap(map);
        page.setTotal(total);

        Integer start = page.getMoquiStart();
        map.put("start",start);
        map.put("end",start + page.getPageSize());
        List<Role> roleList = roleMapper.listAllRoles(map);
        PageListVO<Role> objectPageListVO = new PageListVO<>();
        objectPageListVO.setList(roleList);
        objectPageListVO.setPageDTO(page);
        return objectPageListVO;
    }

    /**
     * @author wyf
     * @description  添加角色
     * @date  2019/4/3 下午 7:49
     * @param roleDTO 角色dto
     */
    public void addRole(RoleDTO roleDTO) throws Exception{

        if (ObjectUtils.isNotEmpty(roleDTO) && ObjectUtils.isNotEmpty(roleDTO.getRole())) {
            //检测角色名是否重复
            Role roleByName = getRoleByName(roleDTO.getRole());
            if (ObjectUtils.isEmpty(roleByName)) {
                Role role = new Role();
                BeanUtils.copyProperties(roleDTO,role);

                long id = SnowflakeIdWorker.nextId();
                //当前登录的用户
                Long userId = Long.valueOf(userInfoManager.getUserInfo().getUserId());
                //当前时间
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                role.setId(id);
                //角色状态为前端必传
                if (ObjectUtils.isEmpty(role.getStatus())) {
                    role.setStatus(SystemConstant.NORMAL_STATUS);
                }
                role.setCreateDate(timestamp);
                role.setCreator(userId);
                role.setUpdateDate(timestamp);
                role.setUpdater(userId);
                roleMapper.insertRole(role);
            } else {
                throw new BusinessException(RetCode.DUPLICATE_ROLENAME,"角色名重复");
            }
        } else {
            throw new BusinessException(RetCode.PARAM_EMPTY,"参数为空");
        }
    }

    /**
     * @author wyf
     * @description  根据角色名获取角色
     * @date  2019/4/3 下午 7:51
     * @param roleName 角色名
     * @return com.yjp.erp.model.po.system.Role
     */
    private Role getRoleByName(String roleName) throws Exception{

        return roleMapper.getRoleByName(roleName);
    }

    /**
     * @author wyf
     * @description  更新或者删除角色
     * @date  2019/4/3 下午 8:03
     * @param roleDTO 角色dto
     * @param type type=1更新，type=-1删除
     */
    public void updateRole(RoleDTO roleDTO,int type) throws Exception{

        if (ObjectUtils.isNotEmpty(roleDTO) && ObjectUtils.isNotEmpty(roleDTO.getId())) {
            Role role = new Role();
            BeanUtils.copyProperties(roleDTO,role);
            //删除
            if (Objects.equals(SystemConstant.DELETE_STATUS,type)) {
                role.setStatus(SystemConstant.DELETE_STATUS);
            } else {
                //检测角色名是否可用
                if (!checkRoleUpdate(role)) {
                    throw new BusinessException(RetCode.DUPLICATE_ROLENAME,"角色名重复");
                }
            }
            //当前登录的用户
            Long userId = Long.valueOf(userInfoManager.getUserInfo().getUserId());
            //当前时间
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            role.setUpdateDate(timestamp);
            role.setUpdater(userId);
            roleMapper.updateRole(role);

        } else {
            throw new BusinessException(RetCode.PARAM_EMPTY,"参数为空");
        }
    }

    /**
     * @author wyf
     * @description  更新时检测角色名是否可用
     * @date  2019/4/8 下午 5:17
     * @param  role 角色
     * @return java.lang.Boolean
     */
    private Boolean checkRoleUpdate(Role role) throws Exception{
        int count = roleMapper.checkRole(role);
        return count == 0;
    }

    /**
     * @author wyf
     * @description  校验角色名是否可用
     * @date  2019/4/3 下午 8:08
     * @param   roleDTO 角色dto
     * @return java.lang.Boolean
     */
    public Boolean checkRole(RoleDTO roleDTO) throws Exception{
        if (ObjectUtils.isNotEmpty(roleDTO) && ObjectUtils.isNotEmpty(roleDTO.getRole())) {
            Role roleByName = getRoleByName(roleDTO.getRole());
            if (ObjectUtils.isEmpty(roleByName)) {
                return true;
            }
        } else {
            throw new BusinessException(RetCode.PARAM_EMPTY,"参数为空");
        }
        return false;
    }

    /**
     * @author wyf
     * @description  用户和角色关联
     * @date  2019/4/3 下午 8:40
     * @param userRoleDTO 用户角色dto
     */
    public void roleRelateUser(UserRoleDTO userRoleDTO) throws Exception{

        if (ObjectUtils.isEmpty(userRoleDTO)) {
            throw new BusinessException(RetCode.PARAM_EMPTY,"参数为空");
        }
        List<UserRole> userRoleList = new ArrayList<>();
        List<User> userList = userRoleDTO.getUserList();
        //删除用户所有的角色
        userRoleMapper.deleteRoleByUserList(userList);
        List<Role> roleList = userRoleDTO.getRoleList();
        for (User user : userList) {
            //更新用户信息
            if (ObjectUtils.isNotEmpty(user.getDisplayName())) {
                userService.updateUser(user);
            }
            for (Role role : roleList) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(role.getId());
                userRole.setUserId(user.getId());
                userRole.setId(SnowflakeIdWorker.nextId());
                userRoleList.add(userRole);
            }
        }
        //插入用户角色
        userRoleMapper.insertList(userRoleList);
    }

    /**
     * @author wyf
     * @description   删除用户和角色的关系
     * @date  2019/4/17 下午 8:36
     * @param  userList 用户列表
     */
    void deleteRoleByUserList(List<User> userList) throws Exception{

        userRoleMapper.deleteRoleByUserList(userList);
    }

    /**
     * @author wyf
     * @description  批量插入用户角色关系信息
     * @date  2019/4/17 下午 8:47
     * @param userRoleList 用户角色关联集合
     */
    void batchInsertUserRole(List<UserRole> userRoleList) throws Exception{

        userRoleMapper.insertList(userRoleList);
    }

    /**
     * @author wyf
     * @description  角色和菜单关联
     * @date  2019/4/4 下午 3:35
     * @param roleMenuDTO 角色菜单dto
     */
    public void roleRelateMenu(RoleMenuDTO roleMenuDTO) throws Exception{

        if (ObjectUtils.isEmpty(roleMenuDTO)) {
            throw new BusinessException(RetCode.PARAM_EMPTY,"参数为空");
        }
        List<RoleMenu> roleMenuList = new ArrayList<>();
        Role role = roleMenuDTO.getRole();
        //删除该角色下所有菜单
        roleMenuMapper.deleteMenuByRole(role);
        List<MenuVO> menuList = roleMenuDTO.getMenuList();
        List<MenuVO> allMenuList = menuService.listAllMenu();
        HashSet<Long> menuSet = new HashSet<>();
        for (MenuVO me : menuList) {

            Menu menuById = menuService.getMenuById(me.getId());
            BeanUtils.copyProperties(menuById,me);
            batchListRoleMenu(menuSet,me,allMenuList);

            //如果该菜单是父菜单，则为其下所有的菜单关联角色
            if (Objects.equals(menuById.getNodeType(),SystemConstant.MENU_PARENT_NOTE)) {
                List<Menu> menus = menuService.listMenuById(menuById.getId());
                for (Menu menu : menus) {
                    RoleMenu roleMenu = handleRoleMenu(role.getId(), menu.getId());
                    roleMenuList.add(roleMenu);
                }
                //子菜单则添加自己
            } else {
                RoleMenu roleMenuSon = handleRoleMenu(role.getId(), menuById.getId());
                roleMenuList.add(roleMenuSon);
            }
        }
        for (Long id : menuSet) {
            RoleMenu roleMenu = handleRoleMenu(role.getId(), id);
            roleMenuList.add(roleMenu);
        }
        if (ObjectUtils.isNotEmpty(roleMenuList)) {
            batchInsertRoleMenu(roleMenuList);
        }
    }

    /**
     * @author wyf
     * @description       批量插入角色菜单关联
     * @date  2019/5/21 下午 3:53
     * @param roleMenuList 角色菜单关联集合
     */
    void batchInsertRoleMenu(List<RoleMenu> roleMenuList) throws Exception{

        roleMenuMapper.insertList(roleMenuList);
    }

    /**
     * @author wyf
     * @description   当该菜单父级不是一级菜单时 递归查询其所有父级及以上菜单
     * @date  2019/4/20 下午 2:12
     */
    private void batchListRoleMenu(HashSet<Long> hashSet,MenuVO menu,List<MenuVO> menuList) throws Exception{

        //如果该菜单父级id不是-1则添加该菜单父级
        if (!Objects.equals(menu.getParentId(),SystemConstant.MENU_PARENT_ID)) {
            for (MenuVO me : menuList) {
                if (Objects.equals(me.getId(),menu.getParentId())) {
                    hashSet.add(me.getId());
                    batchListRoleMenu(hashSet,me,menuList);
                    break;
                }
            }
        }
    }

    /**
     * @author wyf
     * @description  新建rolemenu
     * @param roleId 角色id
     * @param menuId 菜单id
     * @return com.yjp.erp.model.po.system.RoleMenu
     */
    private RoleMenu handleRoleMenu(Long roleId,Long menuId) throws Exception{

        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setMenuId(menuId);
        roleMenu.setRoleId(roleId);
        roleMenu.setId(SnowflakeIdWorker.nextId());
        return roleMenu;
    }

    /**
     * @author wyf
     * @description  获取该用户列表的角色列表
     * @date  2019/4/13 下午 2:58
     * @param userList 用户集合
     * @return java.util.List<com.yjp.erp.model.po.system.Role>
     */
    List<UserRole> listRoleByUserList(List<UserVO> userList) throws Exception{

        return userRoleMapper.listRoleByUserList(userList);
    }

    /**
     * @author wyf
     * @description    根据菜单获取角色
     * @date  2019/5/21 下午 3:54
     * @param menuId 菜单id
     * @return java.util.List<com.yjp.erp.model.po.system.Role>
     */
    List<Role> listRoleByMenu(Long menuId) throws Exception{

        return roleMapper.listRoleByMenuId(menuId);
    }
}