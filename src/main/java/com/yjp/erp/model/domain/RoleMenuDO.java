package com.yjp.erp.model.domain;

import lombok.Data;

/**
 * @author wyf
 * @date 2019/4/20 下午 2:30
 **/
@Data
public class RoleMenuDO {

    private Long roleId;

    private Long menuId;

    private Integer menuNoteType;

    private Long parentId;

    private String menuName;
}
