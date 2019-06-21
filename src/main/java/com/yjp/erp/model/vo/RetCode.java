package com.yjp.erp.model.vo;

public enum RetCode {

    //成功
    SUCCESS(200),

    //失败
    FAIL(400),

    //moqui访问失败后返回的code
    MOQUI_ERROR(600),

    //未认证（签名错误）
    UNAUTHORIZED(401),

    //接口不存在
    NOT_FOUND(404),

    //请求方式不正确
    METHOD_NOT_ALLOWED(405),

    //服务器内部错误
    INTERNAL_SERVER_ERROR(500),
    //获取token失败
    ERROR_TOKEN(501),

    //用户名或密码错误
    LOGIN_ERROR(900),

    //登录超时
    LOGIN_OUT_TIME(901),

    //验证码错误
    VERTFICATION_CODE_ERROR(902),

    //用户名重复
    DUPLICATE_USERNAME(903),

    //角色名重复
    DUPLICATE_ROLENAME(904),

    //组织名重复
    DUPLICATE_ORGNAME(905),

    //菜单名重复
    DUPLICATE_MENUNAME(906),

    //菜单路径重复
    DUPLICATE_MENUPATH(907),

    //账号被锁定
    LOCKED_ACCOUNT(908),

    //组织为基础组织并且被引用
    ORG_DELETE_ERROR(909),

    //该用户没有绑定组织
    USER_ORG_NULL(910),

    //参数为空
    PARAM_EMPTY(6),

    //参数为空
    DUPLICATE_NAME(7),

    //classId为错误类型
    ERROR_CLASSID(601),

    //过滤字段未设置
    EMPTY_FILTER(602),

    //未发现实体或单据（不包括子表）MODULE
    NOT_FIND_MODULE(701),

    //空的实体
    NULL_ENTITY(702);




    public Integer code;

    RetCode(Integer code) {
        this.code = code;
    }
}
