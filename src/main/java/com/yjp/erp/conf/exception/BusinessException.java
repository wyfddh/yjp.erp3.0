package com.yjp.erp.conf.exception;

import com.yjp.erp.model.vo.RetCode;

public class BusinessException extends BaseException {

    public BusinessException(RetCode retCode) {
        super(retCode);
    }

    public BusinessException(RetCode retCode, String msg) {
        super(retCode, msg);
    }

    public BusinessException(String msg) {
        super(msg);
    }
}
