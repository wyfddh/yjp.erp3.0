package com.yjp.erp.model.po.view;

import lombok.Data;

/**
 * @author wyf
 * @date 2019/3/26 下午 8:50
 **/
@Data
public class ViewField {
    private Long id;
    //视图子表id
    private Long viewId;
    //属性名
    private String name;
    //列名
    private String field;
    //别名
    private String alias;
    //校验规则
    private String expresion;
    //描述
    private String description;
    //前端渲染规则
    private String rules;
}
