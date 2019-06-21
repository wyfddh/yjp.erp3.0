package com.yjp.erp.model.dto.mq;

import com.yjp.erp.model.dto.PageDTO;
import lombok.Data;

/**
 * @author liushui
 * @description:
 * @date 2019/3/25
 */
@Data
public class MqConfigDTO extends PageDTO {

    private String id;

    private String name;

    private String status;

}
