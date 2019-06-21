package com.yjp.erp.model.dto.externalapi;

import com.yjp.erp.model.dto.PageDTO;
import lombok.Data;

/**
 * @author xialei
 * @date 2019-06-17
 */
@Data
public class ExternalApiConfigDTO extends PageDTO {
    private String id;
    private String name;
    private String status;
    private String classId;
    private String typeId;
    private String operateType;
    private String systemType;
    private String description;
    private String method;
    private String url;
}