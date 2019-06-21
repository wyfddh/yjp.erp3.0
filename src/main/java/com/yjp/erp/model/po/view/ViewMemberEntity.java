package com.yjp.erp.model.po.view;

import lombok.Data;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/30 9:23
 * @Email: jianghongping@yijiupi.com
 */
@Data
public class ViewMemberEntity {
    private Long id;
    private Long viewEntityId;
    private String entityAlias;
    private String entityName;
    private String classId;
    private String typeId;
    private String joinFromAlias;
    private String fieldName;
    private String related;
}
