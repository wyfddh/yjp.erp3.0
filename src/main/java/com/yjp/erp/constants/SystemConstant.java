package com.yjp.erp.constants;

import com.yjp.erp.controller.externalapi.ExternalApiConfigController;

/**
 * @author wyf
 * @date 2019/4/2 下午 8:58
 **/
public class SystemConstant {

    //http返回的正常code
    public static final int SUCCESS_CODE = 200;
    //状态1正常0冻结/停用-1删除
    public static final int NORMAL_STATUS = 1;
    public static final int DELETE_STATUS = -1;
    public static final int FROZEN_STATUS = 0;

    //分页不传时默认第一页
    public static final int PAGE_NO = 0;
    //分页不传时默认pagesize为99999
    public static final int PAGE_SIZE = 99999;

    //组织默认根节点的pid
    public static final Long ORG_PARENT_ID = -1L;
    //菜单默认根节点的pid
    public static final Long MENU_PARENT_ID = -1L;
    //菜单父节点
    public static final int MENU_PARENT_NOTE = 1;
    //菜单子节点
    public static final int MENU_SON_NOTE = 2;
    //不再拦截的控制层
    public static final String LOGIN_CONTROLLER = "LoginController";
    public static final String KAPTCHACONTROLLER = "KaptchaController";
    public static final String EXTERNAL_API_CONFIGCONTROLLER =  "ExternalCallApiController";

    //存入cookie的userId的 key
    public static final String COOKIE_USERID = "userId";
    //存入session的用户信息
    public static final String SESSION_USER = "UserInfo";

    //非叶子节点
    public static final int LEAF_NOT_NOTE = 1;
    //叶子节点
    public static final int LEAF_NOTE = 0;

    //基本组织类型
    public static final int ORG_TYPE_BASE = 0;
    //业务组织类型
    public static final int ORG_TYPE_ONE = 1;
    //财务组织类型
    public static final int ORG_TYPE_TWO = 2;

    //超级管理员标识
    public static final int ADMIN_MARK = 1;
    //非超级管理员
    public static final int NORMAL_USER_MARK = 0;

    //查询正常状态
    public static final int NORMAL_TYPE = 1;
    //查询正常和冻结状态
    public static final int ALL_TYPE = 2;

    //模糊查询的字段
    public static final String KEY_PHONE = "1";
    public static final String KEY_NAME = "2";

    //请求方式
    public static final String GET = "GET";
    public static final String POST = "POST";
}
