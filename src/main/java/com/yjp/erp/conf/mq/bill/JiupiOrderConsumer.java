package com.yjp.erp.conf.mq.bill;

import com.alibaba.fastjson.JSONObject;
import com.yijiupi.himalaya.base.utils.AssertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * 功能描述:订单完成
 *
 * @author xialei
 * @date 2019/4/30
 */
@Component
public class JiupiOrderConsumer {

    private static final Logger log = LoggerFactory.getLogger(JiupiOrderConsumer.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${moqui.rest.createOrders}")
    private String createOrders;


    @RabbitListener(queues = "mq.erp3.order.JiupiOrderComplete")
    public void addOrder(Message message) {
        log.info("recive OrderMq " + message);
        try {
            Object readObject = new ObjectInputStream(new
                    ByteArrayInputStream(message.getBody())).readObject();
            log.info("parse OrderMq " + readObject);
            updateToMoqui(readObject);
        } catch (Exception e) {
            log.error("更新订单异常：" + e.getMessage());
        }
    }

    private void updateToMoqui(Object object) {
        String parameters = JSONObject.toJSONString(object);
        ResponseEntity<String> resp = restTemplate.postForEntity(createOrders,
                parameters, String.class);
        AssertUtils.isTrue(resp.getStatusCode() == HttpStatus.OK, "更新订单异常");
    }
}
