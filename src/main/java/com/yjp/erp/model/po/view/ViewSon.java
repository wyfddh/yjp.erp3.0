package com.yjp.erp.model.po.view;

import lombok.Data;

/**
 * @author wyf
 * @date 2019/3/26 下午 8:46
 **/
@Data
public class ViewSon {
    private Long id;
    //主表id
    private Long masterId;
    //实体名
    private String entityName;
    //自己的别名
    private String entityAlias;
    //关联父级的别名
    private String joinFromAlias;
    //关联父级的属性
    private String entityParentValue;
    //自己的属性
    private String entityValue;
}
