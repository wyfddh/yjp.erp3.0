package com.yjp.erp.service.mq;

import com.alibaba.fastjson.JSON;
import com.yjp.erp.model.dto.mq.MqMessageDTO;
import com.yjp.erp.mapper.MqMapper;
import com.yjp.erp.model.mq.MqMessageConsume;
import com.yjp.erp.model.mq.MqMessageProducer;
import com.yjp.erp.model.mq.MqQueueService;
import com.yjp.erp.util.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * description:
 * @author liushui
 * @date 2019/4/9
 */
@Service
@Slf4j
public class MqService {

    @Value("${moqui.token.url}")
    private String moquiUrl;

    @Autowired(required = false)
    private MqMapper mqMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 保存发送消息历史，消息一定要保存，故该处不需要事务
     */
    public void sendMessage(MqMessageDTO dto){

        //将生产消息记录，默认状态为未成功
        MqMessageProducer producer = new MqMessageProducer();
        producer.setExchangeName(dto.getExchangeName());
        producer.setMessage(JSON.toJSONString(dto.getParams()));
        producer.setMessageState(0);
        mqMapper.saveProduceMessage(producer);
        //向mq存入消息
        rabbitTemplate.convertAndSend(dto.getExchangeName(),"",dto.getParams());
        //发送成功后将消息状态修改为成功
        producer.setMessageState(1);
        mqMapper.updateProduceMessage(producer);
    }

    /**
     * 消费mq消息,每条消息都要保存
     */
    public void consume(){
        List<MqQueueService> list = mqMapper.listAllMqQueues();
        if(CollectionUtils.isNotEmpty(list)){
            list.forEach(queue ->{
                Object message = null;
                try {
                    message = rabbitTemplate.receiveAndConvert(queue.getQueueName());
                    if(Objects.nonNull(message)){
                        //记录消费消息历史，默认状态为未成功
                        MqMessageConsume consume = new MqMessageConsume();
                        consume.setMessage(JSON.toJSONString(message));
                        consume.setQueueName(queue.getQueueName());
                        consume.setMessageState(0);
                        mqMapper.saveConsumeMessage(consume);

                        //调用moqui
                        Map<String,String> headers = new HashMap<>(4);
                        headers.put("Content-Type","application/json");
                        HttpClientUtils.postParameters(queue.getRestUrl(),JSON.toJSONString(message),headers);

                        //调用成功后将消息状态改为成功
                        consume.setMessageState(1);
                        mqMapper.updateConsumeMessage(consume);
                    }
                } catch (Exception e) {
                    log.error(String.format("mq consume error,current message is {%s}", JSON.toJSONString(message)),e);
                }
            });
        }
    }
}
