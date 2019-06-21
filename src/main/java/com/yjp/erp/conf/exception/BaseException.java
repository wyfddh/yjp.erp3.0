package com.yjp.erp.conf.exception;

import com.yjp.erp.model.vo.RetCode;

public class BaseException extends Exception{

    private RetCode retCode;

    private String msg;

    public BaseException(RetCode retCode){
        this.retCode = retCode;
    }
    public BaseException(String msg){
        this.msg = msg;
    }

    public BaseException(RetCode retCode,String msg){
        this.retCode = retCode;
        this.msg = msg;
    }

    public RetCode getRetCode() {
        return retCode;
    }

    public void setRetCode(RetCode retCode) {
        this.retCode = retCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
