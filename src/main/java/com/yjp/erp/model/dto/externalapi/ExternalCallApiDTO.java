package com.yjp.erp.model.dto.externalapi;

import lombok.Data;

import java.util.Map;

/**
 * @author xialei
 * @date 2019/6/18 10:53
 */
@Data
public class ExternalCallApiDTO {
    private String id;

    private String classId;

    private String typeId;

    private String systemType;

    private Map<String,Object> data;

}
