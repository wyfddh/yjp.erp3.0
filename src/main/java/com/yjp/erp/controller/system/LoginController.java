package com.yjp.erp.controller.system;

import com.yjp.erp.conf.UserInfo;
import com.yjp.erp.conf.UserInfoManager;
import com.yjp.erp.constants.SystemConstant;
import com.yjp.erp.model.dto.system.LoginParam;
import com.yjp.erp.model.dto.system.UserDTO;
import com.yjp.erp.conf.exception.BusinessException;
import com.yjp.erp.model.po.system.Organization;
import com.yjp.erp.model.po.system.Role;
import com.yjp.erp.model.po.system.User;
import com.yjp.erp.service.system.OrgService;
import com.yjp.erp.service.system.RoleService;
import com.yjp.erp.service.system.UserService;
import com.yjp.erp.util.CookieUtils;
import com.yjp.erp.util.ObjectUtils;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetCode;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.model.vo.system.OrgVO;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 登录相关接口
 * @author wyf
 * @date 2019/4/1 下午 8:10
 **/
@RestController
@Slf4j
public class LoginController {

    @Autowired
    private UserInfoManager userInfoManager;

    @Autowired
    private UserService userService;

    @Autowired
    private OrgService orgService;

    @Autowired
    private RoleService roleService;

    /**
     * @author wyf
     * @description  业务平台用户登录
     * @param loginParam 登录的一些参数
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("/apps/login/login")
    public JsonResult login(@RequestBody LoginParam loginParam, HttpServletResponse response) throws Exception {
        //验证用户名和密码
        User user = userService.login(loginParam);
        Long id = user.getId();
        //获取当前用户的基础组织
        List<Organization> defaultOrg = orgService.getBaseOrg(id);
        if(ObjectUtils.isEmpty(defaultOrg)){
            throw new BusinessException(RetCode.USER_ORG_NULL,"该用户没有绑定组织");
        }
        List<Long> orgList = defaultOrg.stream().map(Organization::getId).collect(Collectors.toList());
        List<OrgVO> baseOrgList = new ArrayList<>();
        BeanUtils.copyProperties(defaultOrg,baseOrgList);
        //获取当前用户的业务组织
        List<OrgVO> businessOrgList = orgService.getBusinessOrg(id);
        //获取当前用户的财务组织
        List<OrgVO> financeOrg = orgService.getFinanceOrg(id);
        //获取当前用户的角色
        List<Role> roleList = roleService.listRolesByUserId(id);
        UserInfo info = new UserInfo(id.toString(), "yjp", orgList,businessOrgList,financeOrg,baseOrgList,roleList);
        String userId = info.getUserId();
        String sessionId = UUID.randomUUID().toString().replaceAll("-", "") + userId;
        CookieUtils.addCookie(SystemConstant.COOKIE_USERID, sessionId, response);
        log.info("用户信息---" + info.toString());
        userInfoManager.setUserInfo(sessionId, info);
        return RetResponse.makeOKRsp();
    }


    /**
     * @author wyf
     * @description  配置平台用户登录
     * @param loginParam 登录的一些参数
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("/config/login/login")
    public JsonResult configLogin(@RequestBody LoginParam loginParam, HttpServletResponse response) throws Exception {
        //验证用户名和密码
        User user = userService.configLogin(loginParam);

        UserInfo info = new UserInfo(user.getId().toString(), "yjp", null,null,null,null,null);
        String userId = info.getUserId();
        String sessionId = UUID.randomUUID().toString().replaceAll("-", "") + userId;
        CookieUtils.addCookie(SystemConstant.COOKIE_USERID, sessionId, response);
        log.info("用户信息---" + info.toString());
        userInfoManager.setUserInfo(sessionId, info);
        return RetResponse.makeOKRsp();
    }

    /**
     * @author wyf
     * @description     配置平台退出登录
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @RequestMapping("/config/login/loginOut")
    public JsonResult configLoginOut(HttpServletResponse response) throws Exception{
        userInfoManager.removeUserInfo();
        CookieUtils.addCookie(SystemConstant.COOKIE_USERID, null, response);
        return RetResponse.makeOKRsp();
    }

    /**
     * @author wyf
     * @description     应用平台退出登录
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @RequestMapping("/apps/login/loginOut")
    public JsonResult loginOut(HttpServletResponse response) throws Exception{
        userInfoManager.removeUserInfo();
        CookieUtils.addCookie(SystemConstant.COOKIE_USERID, null, response);
        return RetResponse.makeOKRsp();
    }

    /**
     * @author wyf
     * @description       配置平台用户注册
     * @param user 用户参数
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("/config/registerUser")
    public JsonResult configRegisterUser(@RequestBody UserDTO user) throws Exception {

        userService.configInsertUser(user);
        return RetResponse.makeOKRsp();
    }

    /**
     * @author wyf
     * @description       应用平台用户注册
     * @param user 用户参数
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("/apps/registerUser")
    public JsonResult registerUser(@RequestBody UserDTO user) throws Exception {

        userService.insertUser(user);
        return RetResponse.makeOKRsp();
    }
}