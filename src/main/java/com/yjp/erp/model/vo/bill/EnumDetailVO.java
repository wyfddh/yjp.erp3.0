package com.yjp.erp.model.vo.bill;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/4/22 13:33
 * @Email: jianghongping@yijiupi.com
 */
@Data
public class EnumDetailVO {
    private String id;
    private String typeId;
    private String typeName;
    private String description;
    private List<EnumValueVO> enumValue;
}