package com.yjp.erp.model.po.view;

import lombok.Data;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/29 20:20
 * @Email: jianghongping@yijiupi.com
 */
@Data
public class ViewEntity {
    private Long id;
    private Long moduleId;
    private String label;
    private String entityName;
    private String entityPackage;
    private String cache;
    private String autoClearCache;
    private String authorizeSkip;
}
