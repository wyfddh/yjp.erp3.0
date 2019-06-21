package com.yjp.erp.model.dto.system;

import com.yjp.erp.model.po.system.BaseSys;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wyf
 * @date 2019/4/16 上午 11:13
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuDTO extends BaseSys{

    private String title;

    private String icon;

    private String parentTitle;

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
