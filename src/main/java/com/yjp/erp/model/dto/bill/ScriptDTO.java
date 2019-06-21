package com.yjp.erp.model.dto.bill;

import lombok.Data;

/**
 * description:
 *
 * @author liushui
 * @date 2019/4/3
 */
@Data
public class ScriptDTO {

    // 1-新增模板 2-修改模板 3-分页查询模板 4-查询详情模板
    private Integer serviceType;
}
