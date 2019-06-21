package com.yjp.erp.conf.scheduler;

import com.yjp.erp.service.mq.MqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * description:MQ消费定时调度任务
 * @author liushui
 * @date 2019/4/9
 */
@Component
@Slf4j
public class MqConsumeScheduler {

    @Autowired
    private MqService mqService;

//    @Scheduled(fixedRate=1000*10)
    public void consume() {
        mqService.consume();
    }
}
