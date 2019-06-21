/*
 * Copyright © 2016 北京易酒批电子商务有限公司. All rights reserved.
 */

package com.yjp.erp.model.vo;

/**
 * 前端Json返回对象包装类
 * Created by 魏百川 on 2019/4/12.
 */
public class VOResult<T> extends BaseResult {

    T data;

    public VOResult() {
        super();
    }

    public VOResult(T data) {
        super(RetCode.INTERNAL_SERVER_ERROR.code);
        this.setData(data);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
