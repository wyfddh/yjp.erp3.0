package com.yjp.erp.model.dto.bill;

import com.yjp.erp.model.dto.BaseDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liushui
 * @description: 表单详情
 * @date 2019/3/21
 */
@Data
public class EntityDetailDTO extends BaseDTO {

    @NotNull(message = "classId不能为空")
    private String classId;
    @NotNull(message = "typeId不能为空")
    private String typeId;

    private String label;

    private Long id;
}
