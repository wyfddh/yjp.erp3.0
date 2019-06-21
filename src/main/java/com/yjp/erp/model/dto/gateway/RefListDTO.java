package com.yjp.erp.model.dto.gateway;

import lombok.Data;

/**
 * description:引用列表参数对象
 *
 * @author liushui
 * @date 2019/4/17
 */
@Data
public class RefListDTO {

    private String classId;

    private String typeId;

    /**
     * 引用字段
     */
    private String refField;

    /**
     * 显示字段
     */
    private String targetField;

    private Integer pageIndex;

    private Integer pageSize;

    /**
     * 搜索值,搜索目标字段
     */
    private String searchValue;
}
