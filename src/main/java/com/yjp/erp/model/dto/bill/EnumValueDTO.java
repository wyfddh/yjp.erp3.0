package com.yjp.erp.model.dto.bill;

import lombok.Data;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/4/22 13:31
 * @Email: jianghongping@yijiupi.com
 */
@Data
public class EnumValueDTO {
    private String id;
    private String enumId;
    private String typeId;
    private String value;
    private String description;
}