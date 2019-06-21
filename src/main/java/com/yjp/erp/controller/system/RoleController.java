package com.yjp.erp.controller.system;

import com.yjp.erp.model.dto.PageDTO;
import com.yjp.erp.model.dto.system.RoleDTO;
import com.yjp.erp.model.dto.system.RoleMenuDTO;
import com.yjp.erp.model.dto.system.UserDTO;
import com.yjp.erp.model.dto.system.UserRoleDTO;
import com.yjp.erp.model.po.system.Role;
import com.yjp.erp.service.system.RoleService;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.PageListVO;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.model.vo.system.RoleVO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色相关接口
 * @author wyf
 * @date 2019/4/3 下午 6:20
 **/
@RestController
@Slf4j
@RequestMapping(value = "/config/role")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * @author wyf
     * @description   获取所有正常的角色
     * @param  pageDTO 分页对象
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("listNormalRole")
    public JsonResult listNormalRole(@RequestBody PageDTO pageDTO) throws Exception{

        int type = 1;
        PageListVO<RoleVO> rolePageListVO = roleService.listAllRole(pageDTO,type);
        return RetResponse.makeOKRsp(rolePageListVO);
    }

    /**
     * @author wyf
     * @description       获取所有角色
     * @param pageDTO 分页对象
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("listRole")
    public JsonResult listRole(@RequestBody PageDTO pageDTO) throws Exception{

        int type = 2;
        PageListVO<RoleVO> rolePageListVO = roleService.listAllRole(pageDTO,type);
        return RetResponse.makeOKRsp(rolePageListVO);
    }

    /**
     * @author wyf
     * @description       获取当前用户正常的角色
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @GetMapping("listCurrentRole")
    public JsonResult listCurrentRole() throws Exception{

        List<Role> roleList = roleService.listCurrentRole();
        return RetResponse.makeOKRsp(roleList);
    }

    /**
     * @author wyf
     * @description       模糊查询角色
     * @date  2019/5/21 下午 1:57
     * @param roleDTO 角色入参
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("likeRole")
    public JsonResult likeRole(@RequestBody RoleDTO roleDTO) throws Exception{

        PageListVO<Role> rolePageListVO = roleService.likeRole(roleDTO);
        return RetResponse.makeOKRsp(rolePageListVO);
    }

    /**
     * @author wyf
     * @description       获取指定用户的角色
     * @param user 用户入参
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @GetMapping("listRoleById")
    public JsonResult listRoleById(@RequestBody UserDTO user) throws Exception{

        List<Role> roleList = roleService.listRolesByUserId(user.getId());
        return RetResponse.makeOKRsp(roleList);
    }

    /**
     * @author wyf
     * @description       添加角色
     * @param roleDTO 角色入参
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("addRole")
    public JsonResult addRole(@RequestBody RoleDTO roleDTO) throws Exception{

        roleService.addRole(roleDTO);
        return RetResponse.makeOKRsp();
    }

    /**
     * @author wyf
     * @description       修改角色
     * @param roleDTO 角色入参
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("updateRole")
    public JsonResult updateRole(@RequestBody RoleDTO roleDTO) throws Exception{

        int type = 1;
        roleService.updateRole(roleDTO,type);
        return RetResponse.makeOKRsp();
    }

    /**
     * @author wyf
     * @description       删除角色
     * @param roleDTO 角色入参
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("deleteRole")
    public JsonResult deleteRole(@RequestBody RoleDTO roleDTO) throws Exception{

        int type = -1;
        roleService.updateRole(roleDTO,type);
        return RetResponse.makeOKRsp();
    }

    /**
     * @author wyf
     * @description      校验角色名是否可用
     * @param roleDTO 角色入参
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("checkRole")
    public JsonResult checkRole(@RequestBody RoleDTO roleDTO) throws Exception{

        Boolean result = roleService.checkRole(roleDTO);
        return RetResponse.makeOKRsp(result);
    }

    /**
     * @author wyf
     * @description      角色关联用户
     * @param userRoleDTO 用户角色入参
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("roleRelateUser")
    public JsonResult roleRelateUser(@RequestBody UserRoleDTO userRoleDTO) throws Exception{

        roleService.roleRelateUser(userRoleDTO);
        return RetResponse.makeOKRsp();
    }

    /**
     * @author wyf
     * @description       角色关联菜单
     * @param roleMenuDTO 角色菜单入参
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("roleRelateMenu")
    public JsonResult roleRelateMenu(@RequestBody RoleMenuDTO roleMenuDTO) throws Exception{

        roleService.roleRelateMenu(roleMenuDTO);
        return RetResponse.makeOKRsp();
    }
}