package com.yjp.erp.model.dto.bill;

import com.yjp.erp.model.dto.PageDTO;
import lombok.Data;

/**
 * @author liushui
 * @description:
 * @date 2019/3/25
 */
@Data
public class EntityPageDTO extends PageDTO {

    private String title;

    private Integer status;

    private String classId;

}
