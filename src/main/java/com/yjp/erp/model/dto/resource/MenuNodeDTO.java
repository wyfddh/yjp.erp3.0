package com.yjp.erp.model.dto.resource;

import com.yjp.erp.model.dto.BaseDTO;
import lombok.Data;

/**
 * @author liushui
 * @description:
 * @date 2019/3/28
 */
@Data
public class MenuNodeDTO extends BaseDTO {

    private Long parentId;

    private Long nodeId;

}
