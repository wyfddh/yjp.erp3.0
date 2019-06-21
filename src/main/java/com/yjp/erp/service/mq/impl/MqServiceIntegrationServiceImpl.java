package com.yjp.erp.service.mq.impl;


import com.yjp.erp.conf.mq.RabbitBindingConfig;
import com.yjp.erp.mapper.mq.MqConversionRelationMapper;
import com.yjp.erp.mapper.mq.MqServiceIntegrationMapper;
import com.yjp.erp.model.dto.mq.MqConfigDTO;
import com.yjp.erp.model.dto.mq.MqServiceIntegrationDTO;
import com.yjp.erp.model.mq.MqConversionRelation;
import com.yjp.erp.model.mq.MqServiceIntegration;
import com.yjp.erp.model.vo.mq.MqServiceIntegrationVO;
import com.yjp.erp.service.mq.MqServiceIntegrationService;
import com.yjp.erp.util.ObjectUtils;
import com.yjp.erp.util.SnowflakeIdWorker;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author xialei
 * @date  2019-06-04
 */
@Service
public class MqServiceIntegrationServiceImpl implements MqServiceIntegrationService {
    @Resource
    private MqServiceIntegrationMapper mqServiceIntegrationMapper;

    @Resource
    private MqConversionRelationMapper mqConversionRelationMapper;

    @Resource
    private RabbitBindingConfig rabbitBindingConfig;

    @Override
    public MqServiceIntegrationVO get(String id) {
        return mqServiceIntegrationMapper.get(id);
    }

    @Override
    public List<MqServiceIntegration> findList(MqServiceIntegration mqServiceIntegration) {
        return mqServiceIntegrationMapper.findList(mqServiceIntegration);
    }

    @Override
    public Map<String, Object> findAllList(MqConfigDTO dto) {
        Map<String, Object> param = new HashMap<>(8);
        param.put("name", dto.getName());
        param.put("status", dto.getStatus());
        List<Map<String, Object>> list = new ArrayList<>();
        int count = mqServiceIntegrationMapper.findAllListCount(param);
        if (count > 0) {
            param.put("pageSize", dto.getPageSize());
            param.put("startIndex", (dto.getPageNo() - 1) * dto.getPageSize());
            list = mqServiceIntegrationMapper.findAllList(param);
        }

        Map<String, Object> retMap = new HashMap<>(8);
        retMap.put("pageNo", dto.getPageNo());
        retMap.put("pageSize", dto.getPageSize());
        retMap.put("total", count);
        retMap.put("dataList", list);
        return retMap;
    }

    @Override
    public void insert(MqServiceIntegrationDTO dto) {
        MqServiceIntegration mqServiceIntegration = new MqServiceIntegration();
        BeanUtils.copyProperties(dto, mqServiceIntegration);

        Map<String, String> entityName = getEntityName(dto);
        mqServiceIntegration.setParentEntity(entityName.get("parentEntity"));
        mqServiceIntegration.setChildEntity(entityName.get("childEntity"));
        long billId = SnowflakeIdWorker.nextId();
        mqServiceIntegration.setId(billId);
        mqServiceIntegration.setCreateTime(new Date());
        List<MqConversionRelation> items = mqServiceIntegration.getRelations();
        mqServiceIntegrationMapper.insert(mqServiceIntegration);
        if(ObjectUtils.isEmpty(items)){
            return;
        }

        items.forEach(it -> {
            it.setMqId(billId);
            it.setId(SnowflakeIdWorker.nextId());
        });
        mqConversionRelationMapper.insertBatch(items);
    }

    @Override
    public void insertBatch(List<MqServiceIntegration> mqServiceIntegrations) {
        List<MqConversionRelation> mqItems = new ArrayList<>();
        mqServiceIntegrations.forEach(it -> {
            long id = SnowflakeIdWorker.nextId();
            it.setId(id);
            List<MqConversionRelation> items = it.getRelations();
            items.forEach(item -> {
                item.setMqId(id);
                item.setId(SnowflakeIdWorker.nextId());
            });
            mqItems.addAll(items);
        });
        mqServiceIntegrationMapper.insertBatch(mqServiceIntegrations);
        mqConversionRelationMapper.insertBatch(mqItems);

    }

    @Override
    public void update(MqServiceIntegrationDTO dto) {
        MqServiceIntegration mqServiceIntegration = new MqServiceIntegration();
        BeanUtils.copyProperties(dto, mqServiceIntegration);
        Map<String, String> entityName = getEntityName(dto);
        mqServiceIntegration.setParentEntity(entityName.get("parentEntity"));
        mqServiceIntegration.setChildEntity(entityName.get("childEntity"));
        mqServiceIntegration.setLastUpdatedTime(new Date());
        List<MqConversionRelation> items = mqServiceIntegration.getRelations();

        mqServiceIntegrationMapper.update(mqServiceIntegration);
        mqConversionRelationMapper.delete(mqServiceIntegration.getId() + "");
        if(ObjectUtils.isEmpty(items)){
            return;
        }
        long id = mqServiceIntegration.getId();
        items.forEach(item -> {
            item.setMqId(id);
            item.setId(SnowflakeIdWorker.nextId());
        });
        mqConversionRelationMapper.insertBatch(items);

    }

    @Override
    public void delete(String id) {
        String status = "-1";
        mqServiceIntegrationMapper.updateStatus(id, status);
    }

    @Override
    public void bind(String id) {
        MqServiceIntegrationVO mqServiceIntegration = mqServiceIntegrationMapper.get(id);
        String queueName = mqServiceIntegration.getQueueName();
        String exchangeName = mqServiceIntegration.getExchangeName();
        rabbitBindingConfig.bindingExchangeQueue(queueName, exchangeName);
        String status = "1";
        mqServiceIntegrationMapper.updateStatus(id, status);
    }

    @Override
    public void unbind(String id) {
        MqServiceIntegrationVO mqServiceIntegration = mqServiceIntegrationMapper.get(id);
        String queueName = mqServiceIntegration.getQueueName();
        String exchangeName = mqServiceIntegration.getExchangeName();
        rabbitBindingConfig.removeBinding(BindingBuilder.bind(new Queue(queueName)).to(new FanoutExchange
                (exchangeName)));
        String status = "0";
        mqServiceIntegrationMapper.updateStatus(id, status);
    }

    private Map<String,String> getEntityName(MqServiceIntegrationDTO dto){
        Map<String, Object> param = new HashMap<>(8);
        param.put("classId", dto.getClassId());
        param.put("typeId", dto.getTypeId());
        return mqServiceIntegrationMapper.getEntityNameByTypeIdAndClassId(param);
    }

}
