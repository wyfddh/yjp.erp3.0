package com.yjp.erp.model.vo;

public class JsonResult<T> {

    private int code;

    private String msg;

    private T data;

    public int getCode() {
        return code;
    }

    public JsonResult<T> setCode(RetCode retCode) {
        this.code = retCode.code;
        return this;
    }

    public JsonResult<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public JsonResult<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public JsonResult<T> setData(T data) {
        this.data = data;
        return this;
    }
}
