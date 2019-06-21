package com.yjp.erp.model.dto.mq;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * description:
 * @author liushui
 * @date 2019/4/9
 */
@Data
public class MqMessageDTO {

    /**
     *请求发送mq交换器
     */
    @NotBlank(message = "交换器名称不能为空")
    private String exchangeName;

    /**
     * 参数
     */
    private Object params;
}
