/*
 * Copyright © 2016 北京易酒批电子商务有限公司. All rights reserved.
 */

package com.yjp.erp.model.vo;

/**
 * 前端Json返回包装基类
 * Created by 魏百川 on 2019/4/12.
 */
public class BaseResult {

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 异常信息
     */
    private String msg;

    /**
     * 异常栈的显示层级
     */
    private final static int ERROR_STACK_DEPTH = 3;


    public BaseResult() {
    }

    public BaseResult(Integer code) {
        this.code = code;
    }

    public BaseResult(String message, Integer code) {
        this.msg = message;
        this.code = code;
    }

    public BaseResult(Exception exception) {
        this.code = RetCode.INTERNAL_SERVER_ERROR.code;
        this.msg = getFinalMessage(exception);
    }

    public BaseResult(String message, Exception exception) {
        this(exception);
        this.msg = message;
    }

    public static BaseResult getSuccessResult() {
        return new BaseResult(null, RetCode.INTERNAL_SERVER_ERROR.code);
    }

    public static BaseResult getFailResult(String message) {
        return new BaseResult(message, RetCode.INTERNAL_SERVER_ERROR.code);
    }

    /**
     * 获取最末层的异常提示信息
     *
     * @param exception 异常
     * @return 最末层的异常提示信息
     */
    private String getFinalMessage(Exception exception) {
        String message = exception.getMessage();
        Throwable cause = exception.getCause();
        //循环3层获取到异常提示信息
        int i = 0;
        while (null != cause && i < ERROR_STACK_DEPTH) {
            message = cause.getMessage();
            cause = cause.getCause();
            i++;
        }
        return message;
    }

    /**
     * 获取 响应码
     *
     * @return code 响应码
     */
    public Integer getCode() {
        return this.code;
    }

    /**
     * 设置 响应码
     *
     * @param code 响应码
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 获取 异常信息
     *
     * @return message 异常信息
     */
    public String getMessage() {
        return this.msg;
    }

    /**
     * 设置 异常信息
     *
     * @param message 异常信息
     */
    public void setMessage(String message) {
        this.msg = message;
    }
}
