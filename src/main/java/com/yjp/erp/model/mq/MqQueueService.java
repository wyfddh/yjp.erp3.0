package com.yjp.erp.model.mq;

import lombok.Data;

import java.util.Date;

@Data
public class MqQueueService {

    private Integer id;

    private String queueName;

    private String restUrl;

    private Long serviceId;

    private String description;

    private Byte activeState;

    private Date gmtCreate;

    private Date gmtModify;

}