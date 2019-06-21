package com.yjp.erp.model.mq;

import lombok.Data;

import java.util.Date;

@Data
public class MqMessageProducer {

    private Integer id;

    private String exchangeName;

    private Date gmtCreate;

    private Date gmtModify;

    private Integer messageState;

    private String message;
}