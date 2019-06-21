package com.yjp.erp.model.dto.gateway;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * description:单据详情统一查询接口
 * @author liushui
 * @date 2019/4/9
 */
@Data
public class QueryDetailDTO {

    /**
     * 表单类型
     */
    @NotBlank(message = "classId不能为空")
    private String classId;

    /**
     * 表单标识
     */
    @NotBlank(message = "typeId不能为空")
    private String typeId;

    /**
     * 表单id
     */
    @NotNull
    private Long id;

}
