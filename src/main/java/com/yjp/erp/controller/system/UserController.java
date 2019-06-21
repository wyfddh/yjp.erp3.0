package com.yjp.erp.controller.system;

import com.yjp.erp.model.dto.PageDTO;
import com.yjp.erp.model.dto.system.UserDTO;
import com.yjp.erp.model.dto.system.UserRoleOrgDTO;
import com.yjp.erp.model.po.system.User;
import com.yjp.erp.service.system.UserService;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.PageListVO;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.model.vo.system.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户相关接口
 * @author wyf
 * @date 2019/4/8 上午 10:37
 **/
@RestController
@Slf4j
@RequestMapping(value = "/config/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * @author wyf
     * @description    用户更新
     * @param  userDTO 用户入参
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("updateUser")
    public JsonResult updateUser(@RequestBody UserDTO userDTO) throws Exception {

        User user = new User();
        BeanUtils.copyProperties(userDTO,user);
        userService.updateUser(user);
        return RetResponse.makeOKRsp();
    }

    /**
     * @author wyf
     * @description       删除/停用
     * @param  userDTO 用户入参
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("deleteUser")
    public JsonResult deleteUser(@RequestBody UserDTO userDTO) throws Exception {

        User user = new User();
        BeanUtils.copyProperties(userDTO,user);
        userService.updateUser(user);
        return RetResponse.makeOKRsp();
    }

    /**
     * @author wyf
     * @description       关联用户组织角色
     * @date  2019/5/21 下午 2:06
     * @param userRoleOrgDTO 用户角色组织入参
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("updateUserRelate")
    public JsonResult updateUserRelate(@RequestBody UserRoleOrgDTO userRoleOrgDTO) throws Exception {

        userService.updateUserRelate(userRoleOrgDTO);
        return RetResponse.makeOKRsp();
    }

    /**
     * @author wyf
     * @description       更改密码
     * @param userDTO 用户入参
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("updatePassword")
    public JsonResult updatePassword(@RequestBody UserDTO userDTO) throws Exception{

        userService.updatePassword(userDTO);
        return RetResponse.makeOKRsp();
    }

    /**
     * @author wyf
     * @description     检测用户名是否可用
     * @param userDTO 用户入参
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("checkUserName")
    public JsonResult checkUserName(@RequestBody UserDTO userDTO) throws Exception{

        Boolean bo = userService.checkUserName(userDTO.getUserName());
        if (bo) {
            return RetResponse.makeOKRsp("true");
        }
        return RetResponse.makeErrRsp("false");
    }

    /**
     * @author wyf
     * @description       获取状态正常用户列表
     * @param pageDTO 分页对象
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("listUser")
    public JsonResult listUser(@RequestBody PageDTO pageDTO) throws Exception{

        int type = 1;
        PageListVO<UserVO> pageListVO = userService.listAllUser(pageDTO, type);
        return RetResponse.makeOKRsp(pageListVO);
    }

    /**
     * @author wyf
     * @description      获取未删除用户列表
     * @param pageDTO 分页对象
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("listAllUser")
    public JsonResult listAllUser(@RequestBody PageDTO pageDTO) throws Exception{

        int type = 2;
        PageListVO<UserVO> pageListVO = userService.listAllUser(pageDTO, type);
        return RetResponse.makeOKRsp(pageListVO);
    }

    /**
     * @author wyf
     * @description     模糊查询
     * @param pageNo 当前页
     * @param pageSize 每页显示数
     * @param name 名字
     * @param type 1代表查询正常数据，2代表查询正常和冻结的数据
     * @param key 1代表查询phone2代表查询名字
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @GetMapping("likeUserByName")
    public JsonResult likeUserByName(String pageNo,String pageSize,String name,String type,String key) throws Exception {

        PageListVO<UserVO> vo = userService.likeUserByName(name, type, pageNo, pageSize,key);
        return RetResponse.makeOKRsp(vo);
    }
}