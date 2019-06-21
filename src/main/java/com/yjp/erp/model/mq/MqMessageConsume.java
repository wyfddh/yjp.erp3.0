package com.yjp.erp.model.mq;

import lombok.Data;

import java.util.Date;

@Data
public class MqMessageConsume {

    private Integer id;

    private String queueName;

    private Date gmtCreate;

    private Date gmtModify;

    private Integer messageState;

    private String message;

}