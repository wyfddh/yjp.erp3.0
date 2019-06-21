package com.yjp.erp.mapper;

import com.yjp.erp.model.mq.MqMessageConsume;
import com.yjp.erp.model.mq.MqMessageProducer;
import com.yjp.erp.model.mq.MqQueueService;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MqMapper {

    List<MqQueueService> listAllMqQueues();

    void saveProduceMessage(MqMessageProducer producer);

    void updateProduceMessage(MqMessageProducer producer);

    void saveConsumeMessage(MqMessageConsume consume);

    void updateConsumeMessage(MqMessageConsume consume);
}