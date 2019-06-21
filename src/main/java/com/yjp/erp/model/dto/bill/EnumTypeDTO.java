package com.yjp.erp.model.dto.bill;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/4/22 13:33
 * @Email: jianghongping@yijiupi.com
 */
@Data
public class EnumTypeDTO {

    private String id;
    private String typeId;
    private String typeName;
    private String description;
    private List<EnumValueDTO> enumValue;
}