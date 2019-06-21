package com.yjp.erp.service.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yjp.erp.constants.SystemConstant;
import com.yjp.erp.model.dto.PageDTO;
import com.yjp.erp.model.dto.system.LoginParam;
import com.yjp.erp.model.dto.system.UserDTO;
import com.yjp.erp.model.dto.system.UserRoleOrgDTO;
import com.yjp.erp.conf.exception.BusinessException;
import com.yjp.erp.mapper.system.UserMapper;
import com.yjp.erp.model.po.system.Organization;
import com.yjp.erp.model.po.system.Role;
import com.yjp.erp.model.po.system.User;
import com.yjp.erp.model.po.system.UserOrg;
import com.yjp.erp.model.po.system.UserRole;
import com.yjp.erp.util.HttpClientUtils;
import com.yjp.erp.util.Md5Utils;
import com.yjp.erp.util.ObjectUtils;
import com.yjp.erp.util.SnowflakeIdWorker;
import com.yjp.erp.model.vo.PageListVO;
import com.yjp.erp.model.vo.RetCode;
import com.yjp.erp.model.vo.system.OrgVO;
import com.yjp.erp.model.vo.system.UserVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wyf
 * @date 2019/4/1 下午 8:14
 **/
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class UserService {

    @Autowired
    private RoleService roleService;
    @Autowired
    private OrgService orgService;
    @Autowired
    private UserMapper userMapper;

    @Value("${moqui.token.url}")
    private String moquiUrl;

    @Value("${moqui.rest.user}")
    private String restUser;
    @Value("${moqui.rest.getUser}")
    private String restGetUser;
    @Value("${moqui.rest.updateUser}")
    private String restUpdate;
    @Value("${moqui.rest.getUserByUserName}")
    private String restUserByUserName;
    @Value("${moqui.rest.getUserById}")
    private String restId;
    @Value("${moqui.rest.getUserByIds}")
    private String restIds;
    @Value("${moqui.rest.likeUserByName}")
    private String restLikeUser;

    /**
     * @param userName 登录账号
     * @return com.yjp.erp.model.po.system.User
     * @author wyf
     * @description 根据用户登录账号查找用户
     * @date 2019/4/9 上午 11:56
     */
    private User getUserByUserName(String userName) throws Exception{

        String url = restUserByUserName + "?userName=" + userName;
        String result = HttpClientUtils.get(url);
        JSONObject jsonObject = JSON.parseObject(result);
        jsonObject = ObjectUtils.isNotEmpty(jsonObject) ? jsonObject : new JSONObject();
        int code = jsonObject.getIntValue("code");
        if (!Objects.equals(code,SystemConstant.SUCCESS_CODE)) {
            throw new BusinessException(RetCode.MOQUI_ERROR,"moqui服务异常");
        }
        return jsonObject.getObject("data", User.class);
    }

    /**
     * @param userDTO 用户dto实体
     * @author wyf
     * @description 新增用户
     * @date 2019/4/9 上午 11:58
     */
    public void insertUser(UserDTO userDTO) throws Exception {
        User user = new User();
        BeanUtils.copyProperties(userDTO,user);

        if (ObjectUtils.isNotEmpty(user) && ObjectUtils.isNotEmpty(user.getPassword())) {
            //判断用户名是否重复
            if (!checkUserName(user.getUserName())) {
                throw new BusinessException(RetCode.DUPLICATE_USERNAME, "用户名重复");
            }
            //暂时由后端md5加密
            user.setPassword(Md5Utils.encrytMD5(user.getPassword()));
            user.setPhone(user.getUserName());
            user.setMark(SystemConstant.NORMAL_USER_MARK);
            if (ObjectUtils.isEmpty(user.getStatus())) {
                user.setStatus(SystemConstant.NORMAL_STATUS);
            }
            String url = restUser;
            HttpClientUtils.postSimple(url,user);
        } else {
            throw new BusinessException(RetCode.PARAM_EMPTY, "参数为空");
        }
    }

    /**
     * @param user 用户实体
     * @author wyf
     * @description 修改用户
     * @date 2019/4/9 上午 11:56
     */
    public void updateUser(User user) throws Exception {
        if (ObjectUtils.isNotEmpty(user) && ObjectUtils.isNotEmpty(user.getId())) {
            String url = restUpdate;
            HttpClientUtils.postSimple(url,user);
        } else {
            throw new BusinessException(RetCode.PARAM_EMPTY);
        }
    }

    /**
     * @param user 实体
     * @author wyf
     * @description 修改密码
     * @date 2019/4/8 上午 10:31
     */
    public void updatePassword(UserDTO user) throws Exception {
        if (ObjectUtils.isNotEmpty(user) && ObjectUtils.isNotEmpty(user.getId()) && ObjectUtils
                .isNotEmpty(user.getNewPassword())) {

            User u = getUserById(user.getId());
            //目前后端md5加密
            if (u.getPassword().equals(Md5Utils.encrytMD5(user.getPassword()))) {
                u.setPassword(Md5Utils.encrytMD5(user.getNewPassword()));
                updateUser(u);
            } else {
                throw new BusinessException(RetCode.LOGIN_ERROR);
            }
        } else {
            throw new BusinessException(RetCode.PARAM_EMPTY,"参数为空");
        }
    }

    /**
     * @param userName 用户登录名
     * @return java.lang.Boolean
     * @author wyf
     * @description 检测用户名是否可用。可用返回true
     * @date 2019/4/8 上午 10:35
     */
    public Boolean checkUserName(String userName) throws Exception {

        User user = getUserByUserName(userName);
        return ObjectUtils.isEmpty(user) || ObjectUtils.isEmpty(user.getId());
    }

    /**
     * @param id 用户id
     * @return com.yjp.erp.model.po.system.User
     * @author wyf
     * @description 根据id获取用户
     * @date 2019/4/8 下午 4:53
     */
    User getUserById(Long id) throws Exception {
        String url = restId + "?id=" + id;

        String result = HttpClientUtils.get(url);
        JSONObject jsonObject = JSON.parseObject(result);
        jsonObject = ObjectUtils.isNotEmpty(jsonObject) ? jsonObject : new JSONObject();
        int code = jsonObject.getIntValue("code");
        if (!Objects.equals(code,SystemConstant.SUCCESS_CODE)) {
            throw  new BusinessException(RetCode.MOQUI_ERROR,"moqui服务异常");
        }
        return jsonObject.getObject("data", User.class);
    }

    /**
     * @param pageDTO 分页对象
     * @param type 查询筛选状态，1只查询正常，2查询冻结和正常
     * @return java.util.List<com.yjp.erp.model.po.system.User>
     * @author wyf
     * @description 查询所有未删除的用户
     * @date 2019/4/8 下午 3:30
     */
    public PageListVO<UserVO> listAllUser(PageDTO pageDTO,int type) throws Exception {

        int start = pageDTO.getMoquiStart();
        String url = restGetUser + "?pageNo=" + start + "&pageSize=" + pageDTO.getPageSize();
        String result = HttpClientUtils.get(url);
        JSONObject jsonObject = JSON.parseObject(result);
        jsonObject = ObjectUtils.isNotEmpty(jsonObject) ? jsonObject : new JSONObject();
        int code = jsonObject.getIntValue("code");
        if (!Objects.equals(code,SystemConstant.SUCCESS_CODE)) {
            throw  new BusinessException(RetCode.MOQUI_ERROR,"moqui服务异常");
        }
        JSONObject data = jsonObject.getJSONObject("data");
        List<UserVO> userList = new ArrayList<>();
        Integer total = 0;
        if (ObjectUtils.isNotEmpty(data)) {
            JSONArray list = data.getJSONArray("list");
            userList = list.toJavaList(UserVO.class);
            total = data.getInteger("dataCount");
        }

        //获取该用户列表的角色列表
        List<UserRole> userRoleList;
        //获取该用户列表的组织列表
        List<UserOrg> userOrgList;
        if (ObjectUtils.isNotEmpty(userList)) {
            userRoleList = roleService.listRoleByUserList(userList);
            userOrgList = orgService.listUserOrgByUserList(userList);
        } else {
            userRoleList = new ArrayList<>();
            userOrgList = new ArrayList<>();
        }
        List<UserVO> resultList = new ArrayList<>();

        handleUserVOList(resultList,userOrgList,userRoleList,userList,type);

        PageListVO<UserVO> pageListVO = new PageListVO<>();
        pageListVO.setList(resultList);
        pageDTO.setTotal(total);
        pageListVO.setPageDTO(pageDTO);

        return pageListVO;
    }

    /**
     * @author wyf
     * @description   处理用户，返回前端所需的用户结构
     * @param resultList 返回的vo集合
     * @param userOrgList 用户组织关系集合
     * @param userRoleList 用户角色关系集合
     * @param userList  用户集合
     * @param type  1只查询正常，2查询冻结和正常
     */
    private void handleUserVOList(List<UserVO> resultList,List<UserOrg> userOrgList,
            List<UserRole> userRoleList,List<UserVO> userList,int type) throws Exception{

        for (UserVO user : userList) {
            user.setPassword("");
            if (type == 1) {
                //保留正常的数据
                if (Objects.equals(user.getStatus(),SystemConstant.NORMAL_STATUS)) {
                    resultList.add(user);
                }
            } else if (type == 2) {
                //保留正常和冻结的数据
                if (Objects.equals(user.getStatus(),SystemConstant.NORMAL_STATUS) || Objects.equals(user.getStatus(),SystemConstant.FROZEN_STATUS)) {
                    resultList.add(user);
                }
            }
            List<Role> roles = new ArrayList<>();
            for (UserRole userRole : userRoleList) {
                if (Objects.equals(userRole.getUserId(),user.getId())) {
                    Role role = new Role();
                    role.setId(userRole.getRoleId());
                    role.setRole(userRole.getRoleName());
                    roles.add(role);
                }
            }
            user.setRoleList(roles);
            List<Long> orgIds = new ArrayList<>();
            for (UserOrg userOrg : userOrgList) {
                if (Objects.equals(userOrg.getUserId(),user.getId())) {
                    orgIds.add(userOrg.getOrgId());
                }
            }
            List<OrgVO> orgVOList = new ArrayList<>();
            if (ObjectUtils.isNotEmpty(orgIds)) {
                List<Organization> orgByIds = orgService.getOrgByVIds(orgIds);
                for (Organization orgById : orgByIds) {
                    OrgVO orgVO = new OrgVO();
                    BeanUtils.copyProperties(orgById,orgVO);
                    orgVO.setKey(orgVO.getId());
                    orgVOList.add(orgVO);
                }
            }
            user.setOrgList(orgVOList);
        }
    }


    /**
     * @param name 查询的内容
     * @param type 1代表查询正常数据，2代表查询正常和冻结的数据
     * @param pageNo 当前页
     * @param pageSize  每页显示数
     * @param key  1代表查询phone2代表查询名字
     * @return java.util.List<com.yjp.erp.model.po.system.User>
     * @author wyf
     * @description 模糊查询
     * @date 2019/4/8 下午 7:52
     */
    public PageListVO<UserVO> likeUserByName(String name,String type,String pageNo,String pageSize,String key) throws Exception {
        PageDTO pageDTO = new PageDTO();
        if (ObjectUtils.isNotEmpty(pageNo) && ObjectUtils.isNotEmpty(pageSize)) {
            pageDTO.setPageNo(Integer.parseInt(pageNo));
            pageDTO.setPageSize(Integer.parseInt(pageSize));
        }
        String fieldName = "";
        if (Objects.equals(key,SystemConstant.KEY_PHONE)) {
            fieldName = "phone";
        } else if (Objects.equals(key,SystemConstant.KEY_NAME)) {
            fieldName = "displayName";
        }
        String url = restLikeUser + "?fieldValue=" + name + "&pageNo=" + pageDTO.getMoquiStart()
                + "&pageSize=" + pageDTO.getPageSize() + "&fieldName=" + fieldName;
        String result = HttpClientUtils.get(url);
        JSONObject jsonObject = JSON.parseObject(result);
        jsonObject = ObjectUtils.isNotEmpty(jsonObject) ? jsonObject : new JSONObject();
        int code = jsonObject.getIntValue("code");
        if (!Objects.equals(code,SystemConstant.SUCCESS_CODE)) {
            throw  new BusinessException(RetCode.MOQUI_ERROR,"moqui服务异常");
        }
        JSONObject data = jsonObject.getJSONObject("data");
        List<UserVO> userList = new ArrayList<>();
        Integer total = 0;
        if (ObjectUtils.isNotEmpty(data)) {
            JSONArray list = data.getJSONArray("list");
            userList = list.toJavaList(UserVO.class);
            total = data.getInteger("dataCount");
        }
        pageDTO.setTotal(total);

        List<UserVO> resList;
        //过滤
        if (Objects.equals(type,String.valueOf(SystemConstant.NORMAL_TYPE))) {
            resList = new ArrayList<>();
            for (UserVO user : userList) {
                if (Objects.equals(user.getStatus(),SystemConstant.NORMAL_STATUS)) {
                    resList.add(user);
                }
            }
        } else {
            resList = userList;
        }
        PageListVO<UserVO> pageListVO = new PageListVO<>();
        pageListVO.setList(resList);
        pageListVO.setPageDTO(pageDTO);
        return pageListVO;
    }
    
    /**
     * @description: 根据用户id集合获取用户信息
     * @param ids 用户id集合
     */
	public Map<Long, User> getUserMapByIds(List<Long> ids) throws Exception {

        if (ids != null && !ids.isEmpty()) {
            Map<Long, User> map = new HashMap<>(ids.size());

            StringBuilder idString = new StringBuilder();
            for (Long id : ids) {
                idString.append(",").append(new StringBuilder(String.valueOf(id)));
            }
            idString = new StringBuilder(idString.substring(1, idString.length()));
            String url = restIds + "?ids=" + idString;
            String result = HttpClientUtils.get(url);
            JSONObject jsonObject = JSON.parseObject(result);
            jsonObject = ObjectUtils.isNotEmpty(jsonObject) ? jsonObject : new JSONObject();
            int code = jsonObject.getIntValue("code");
            if (!Objects.equals(code,SystemConstant.SUCCESS_CODE)) {
                throw  new BusinessException(RetCode.MOQUI_ERROR,"moqui服务异常");
            }
            JSONArray data = CollectionUtils.isNotEmpty(jsonObject.getJSONArray("data")) ? jsonObject.getJSONArray("data") : new JSONArray();
            List<User> userList = data.toJavaList(User.class);
            for (User user : userList) {
                map.put(user.getId(),user);
            }
            return map;
		}
		return new HashMap<>(4);
	}

    /**
     * @author wyf
     * @description  登录
     * @date  2019/4/13 下午 12:20
     * @param loginParam 登录参数
     * @return void
     */
    public User login(LoginParam loginParam) throws Exception{
        String userName = loginParam.getUserName();
        String password = loginParam.getPassword();
        //查询账号是否存在
        User user = getUserByUserName(userName);
        return extractCode(user,password);
    }

    /**
     * @author wyf
     * @description      抽出的公用方法
     */
    private User extractCode(User user,String password) throws Exception{
        if (ObjectUtils.isNotEmpty(user)) {
            if (user.getStatus() != SystemConstant.NORMAL_STATUS) {
                throw new BusinessException(RetCode.LOCKED_ACCOUNT,"账号被锁定，请联系管理员！");
            }
            String md5Password = Md5Utils.encrytMD5(password);
            if (Objects.equals(md5Password,user.getPassword())) {
                return user;
            } else {
                throw new BusinessException(RetCode.LOGIN_ERROR,"用户名或密码错误！");
            }
        } else {
            throw new BusinessException(RetCode.LOGIN_ERROR,"用户名或密码错误！");
        }
    }

    /**
     * @author wyf
     * @description  登录
     * @date  2019/4/13 下午 12:20
     * @param loginParam 登录参数
     * @return void
     */
    public User configLogin(LoginParam loginParam) throws Exception{
        String userName = loginParam.getUserName();
        String password = loginParam.getPassword();
        //查询账号是否存在
        User user = userMapper.getUserByUserName(userName);
        return extractCode(user,password);
    }

    /**
     * @author wyf
     * @description     配置平台新增用户
     * @date  2019/5/21 下午 4:47
     * @param userDTO 用户信息
     */
    public void configInsertUser(UserDTO userDTO) throws Exception{
        User user = new User();
        BeanUtils.copyProperties(userDTO,user);
        //校验用户名
        User userByUserName = userMapper.getUserByUserName(userDTO.getUserName());
        if (ObjectUtils.isNotEmpty(userByUserName)) {
            throw new BusinessException(RetCode.DUPLICATE_USERNAME,"用户名重复");
        }
        user.setId(SnowflakeIdWorker.nextId());
        //暂时由后端md5加密
        user.setPassword(Md5Utils.encrytMD5(user.getPassword()));
        user.setPhone(user.getUserName());
        user.setMark(SystemConstant.NORMAL_USER_MARK);
        if (ObjectUtils.isEmpty(user.getStatus())) {
            user.setStatus(SystemConstant.NORMAL_STATUS);
        }
        userMapper.insertUser(user);
    }

    /**
     * @author wyf
     * @description  添加用户组织角色关联信息
     * @date  2019/4/17 下午 8:31
     * @param userRoleOrgDTO 用户组织角色dto
     */
    public void updateUserRelate(UserRoleOrgDTO userRoleOrgDTO) throws Exception{

        if (ObjectUtils.isEmpty(userRoleOrgDTO)) {
            throw new BusinessException(RetCode.PARAM_EMPTY,"参数为空");
        }
        List<UserRole> userRoleList = new ArrayList<>();
        List<UserOrg> userOrgList = new ArrayList<>();
        List<User> userList = userRoleOrgDTO.getUserList();
        //删除用户角色关系
        roleService.deleteRoleByUserList(userList);
        //删除用户组织关系
        orgService.deleteUserOrgByUserId(userList);
        List<Organization> orgList = userRoleOrgDTO.getOrgList();
        List<Role> roleList = userRoleOrgDTO.getRoleList();
        for (User user : userList) {
            //更新用户信息
//            if (ObjectUtils.isNotEmpty(user.getDisplayName())) {
//                updateUser(user);
//            }
            for (Role role : roleList) {
                UserRole uRole = new UserRole();
                uRole.setRoleId(role.getId());
                uRole.setUserId(user.getId());
                long uRoleId = SnowflakeIdWorker.nextId();
                uRole.setId(uRoleId);
                userRoleList.add(uRole);
            }
            for (Organization org : orgList) {
                UserOrg userOrg = new UserOrg();
                userOrg.setId(SnowflakeIdWorker.nextId());
                userOrg.setOrgId(org.getId());
                userOrg.setUserId(user.getId());
                userOrgList.add(userOrg);
            }
        }
        //插入用户与组织的关联
        orgService.batchInsertUserOrg(userOrgList);
        //插入用户与角色的关联
        roleService.batchInsertUserRole(userRoleList);
    }
}