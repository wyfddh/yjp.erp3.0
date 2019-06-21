/*
 * Copyright © 2016 北京易酒批电子商务有限公司. All rights reserved.
 */

package com.yjp.erp.model.vo;

import java.util.List;

/**
 * 前端Json返回集合包装类
 * Created by 魏百川 on 2019/4/12.
 */
public class ListResult<T> extends BaseResult {

    private List<T> list;

    public ListResult() {
        super();
    }

    public ListResult(Integer code, String result) {
        super(result, code);
    }

    public ListResult(List<T> list) {
        super(RetCode.INTERNAL_SERVER_ERROR.code);
        this.setList(list);
    }

    public List<T> getList() {
        return list;
    }

    private void setList(List<T> list) {
        this.list = list;
    }
}
