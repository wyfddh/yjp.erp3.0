package com.yjp.erp.conf.mq;

import com.yjp.erp.mapper.mq.MqServiceIntegrationMapper;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListenerAnnotationBeanPostProcessor;
import org.springframework.amqp.rabbit.config.RabbitListenerConfigUtils;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态绑定Exchange和Queue for 系统集成
 * Created by weibaichuan on 2019/4/25
 */
@Configuration
public class RabbitBindingConfig {

    @Value("${mq.queueName.default}")
    private String defaultQueueName;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired(required = false)
    private MqServiceIntegrationMapper mqServiceIntegrationMapper;

    @Bean(name = RabbitListenerConfigUtils.RABBIT_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME)
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public RabbitListenerAnnotationBeanPostProcessor rabbitListenerAnnotationProcessor() {
        return new RabbitListenerAnnotationBeanPostProcessor();
    }

    @Bean(name = RabbitListenerConfigUtils.RABBIT_LISTENER_ENDPOINT_REGISTRY_BEAN_NAME)
    public RabbitListenerEndpointRegistry defaultRabbitListenerEndpointRegistry() {
        return new RabbitListenerEndpointRegistry();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }


    /**
     * 启动时初始化mq连接
     *
     * @return
     * @throws Exception
     */
    @Bean
    public String[] mqMsgQueues() throws Exception {
        List<String> queueNames = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("status", "1");
        int count = mqServiceIntegrationMapper.findAllListCount(params);
        if (count > 0) {
            params.put("startIndex", 0);
            params.put("pageSize", 999);
            List<Map<String, Object>> maps = mqServiceIntegrationMapper.findAllList(params);
            for (Map<String, Object> map : maps) {
                String queueName = (String) map.get("queueName");
                String exchangeName = (String) map.get("exchangeName");
                this.bindingExchangeQueue(queueName, exchangeName);
                queueNames.add(queueName);
            }
        } else {
            this.declareQueue(new Queue(defaultQueueName));
        }
        return queueNames.toArray(new String[0]);
    }

    public void declareQueue(Queue queue) {
        amqpAdmin.declareQueue(queue);
    }

    public void deleteQueue(String queueName) {
        amqpAdmin.deleteQueue(queueName);
    }

    public void removeBinding(Binding binding) {
        amqpAdmin.removeBinding(binding);
    }

    public void declareExchange(FanoutExchange exchange) {
        amqpAdmin.declareExchange(exchange);
    }

    public void bindingExchangeQueue(String queueName, String exchangeName) {
        Connection connection = connectionFactory.createConnection();
        try {
            connection.createChannel(false).queueDeclare(queueName, true, false, false, null);
            connection.createChannel(false).queueBind(queueName, exchangeName, "");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
