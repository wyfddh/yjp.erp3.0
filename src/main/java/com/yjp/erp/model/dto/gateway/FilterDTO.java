package com.yjp.erp.model.dto.gateway;

import lombok.Data;

/**
 * description:
 * @author liushui
 * @date 2019/4/12
 */
@Data
public class FilterDTO {

    private String classId;

    private String typeId;

    private Object searchValue;
}
