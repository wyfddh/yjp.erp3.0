package com.yjp.erp.model.po.system;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wyf
 * @date 2019/4/2 下午 5:46
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class Menu extends BaseSys{

    private String title;

    private String parentTitle;

    private String icon;

    private Long parentId;

    private Integer resourceOrder;

    private String typeId;

    private String classId;

    private String template;

    private String routePath;

    /**
     * @description  //节点类型 1-父结点 2-子结点
     */
    private Integer nodeType;

}
