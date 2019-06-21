package com.yjp.erp.model.dto.resource;

import com.yjp.erp.model.dto.BaseDTO;
import lombok.Data;

/**
 * @author liushui
 * description:
 * @date 2019/3/28
 */
@Data
public class ResourceDTO extends BaseDTO {

    private Long parentId;

    private String resourceName;

    private String resourceIcon;

    private String resourceOrder;

    private String typeId;

    private String classId;

    private String routePath;

    private Integer nodeType;

    private Integer activeState;
}
