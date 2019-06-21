package com.yjp.erp.model.dto.bill;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * description:
 * @author liushui
 * @date 2019/4/10
 */
@Data
public class EecaDTO {

    @NotBlank(message = "id不能为空")
    private String eecaId;
}
