package com.yjp.erp.model.po.view;

import lombok.Data;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/29 20:20
 * @Email: jianghongping@yijiupi.com
 */
@Data
public class ViewAlias {
    private Long id;
    private Long viewEntityMemberId;
    private String name;
    private String entityAlias;
    private String field;
    private String description;
    private String expression;
    private String keyType;
}
