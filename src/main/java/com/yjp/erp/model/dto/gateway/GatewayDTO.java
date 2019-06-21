package com.yjp.erp.model.dto.gateway;

import com.yjp.erp.model.dto.BaseDTO;
import com.yjp.erp.model.dto.bill.BillAttachmentDTO;
import lombok.Data;

import java.util.Map;

/**
 * description:
 *
 * @author liushui
 * @date 2019/3/29
 */
@Data
public class GatewayDTO extends BaseDTO {

    private String method;

    private String url;

    private String classId;

    private String typeId;

    private Map<String,Object> data;
}
