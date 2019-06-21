package com.yjp.erp.model.po.view;

import lombok.Data;

/**
 * @author wyf
 * @date 2019/3/26 下午 8:39
 **/
@Data
public class ViewParent {
    private Long id;

    private String entityName;

    private String packagePath;

    private String rules;

    private Long moduleId;

    private String title;

    private String cache;

    private String transaction;
}
