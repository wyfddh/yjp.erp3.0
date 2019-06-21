package com.yjp.erp.model.vo.system;

import com.yjp.erp.model.po.system.BaseSys;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wyf
 * @date 2019/4/16 上午 11:21
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuVO extends BaseSys{

    private String title;

    private Long key;

    private Long value;

    private String parentTitle;

    private String icon;

    private Long parentId;

    private Integer resourceOrder;

    private String typeId;

    private String classId;

    private String template;

    private String routePath;

    /**
     *节点类型 1-父结点 2-子结点
     */
    private Integer nodeType;

    private List<MenuVO> children;

}
