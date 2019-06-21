package com.yjp.erp.model.vo;

import com.yjp.erp.model.domain.PageInfoDO;
import lombok.Data;

import java.io.Serializable;

/**
 * description:
 *
 * @author liushui
 * @date 2019/4/18
 */
@Data
public class PageListResult<T> implements Serializable {

    private ListResult<T> list;

    private PageInfoDO pageInfo;
}
