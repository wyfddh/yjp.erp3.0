package com.yjp.erp.conf.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yijiupi.himalaya.base.utils.AssertUtils;
import com.yjp.erp.constants.JsonTypeEnum;
import com.yjp.erp.mapper.mq.MqServiceIntegrationMapper;
import com.yjp.erp.model.mq.MqConversionRelation;
import com.yjp.erp.model.mq.MqServiceIntegration;
import com.yjp.erp.util.ObjectUtils;
import com.yjp.erp.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author xialeid
 * @date 2019/6/4 9:02
 */
@Component
@Slf4j
public class MqConsumer {


    @Resource
    private RestTemplate restTemplate;

    @Resource
    private MqServiceIntegrationMapper mqServiceIntegrationMapper;

    @RabbitListener(queues = "mq.erp3.generate.queue")
    public void messageListener(Message message) {
        try {
            String data = new String(message.getBody());
            MqServiceIntegration mqService = mqServiceIntegrationMapper.getByExchangeName(message
                    .getMessageProperties().getReceivedExchange());
            if (mqService == null) {
                return;
            }

            String url = mqService.getMoquiUrl();
            if (ObjectUtils.isEmpty(url)) {
                return;
            }
            Map<String, String> fields = getAllFields(mqService);
            List<Map<String, Object>> list = dataToJson(data, fields, mqService);
            for (Map<String, Object> map : list) {
                updateToMoqui(new JSONObject(map), url);
            }

            log.info("mq同步成功");
        } catch (Exception e) {
            log.error("mq同步异常：" + e.getMessage());
        }
    }


    /**
     * 获取字段对应关系
     *
     * @param mqService 字段信息
     * @return 字段对应关系
     */
    private Map<String, String> getAllFields(MqServiceIntegration mqService) {
        Map<String, String> map = new HashMap<>(16);
        List<MqConversionRelation> relations = mqService.getRelations();
        relations.forEach(it -> map.put(it.getSourceField(), it.getTargetField()));
        return map;
    }


    /**
     * 根据字段关系，获取值并转为json格式
     *
     * @param data mq消息
     * @return 返回json格式字段
     */
    private List<Map<String, Object>> dataToJson(String data, Map<String, String> fields, MqServiceIntegration mqService) {
        String type = isJsonArrayOrObject(data);
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>(16);
        //区分是否一条还是多条数据
        if (Objects.equals(JsonTypeEnum.OBJECT.getValue(), type)) {
            JSONObject jsonObject = JSON.parseObject(data);
            map = objectToMap(jsonObject, fields);
            map.put("parentEntity", mqService.getParentEntity());
            map.put("childEntity", mqService.getChildEntity());
            list.add(map);
        } else if (Objects.equals(JsonTypeEnum.ARRAY.getValue(), type)) {
            JSONArray jsonArray = JSONArray.parseArray(data);
            for (Object object : jsonArray) {
                JSONObject obj = (JSONObject) object;
                map = objectToMap(obj, fields);
                map.put("parentEntity", mqService.getParentEntity());
                map.put("childEntity", mqService.getChildEntity());
                list.add(map);
            }
        }

        return list;
//        return new JSONObject(map);
//        return JSONArray.parseArray(JSON.toJSONString(list));
    }


    /**
     * 根据字段对应关系从json获取值
     *
     * @param jsonObject 数据
     * @param fields     字段对应关系
     * @return 返回值
     */
    private Map<String, Object> objectToMap(JSONObject jsonObject, Map<String, String> fields) {
        Map<String, Object> map = new HashMap<>(16);
        Map<String, String> subFields = new HashMap<>(16);
        String subFieldName = "";

        for (String sourceField : fields.keySet()) {
            //如果是子对象字段，如果是子list，如果是字段
            String[] sourceSplit = sourceField.split("\\.");
            String targetField = fields.get(sourceField);
            //对应字段为空则不处理
            if(ObjectUtils.isEmpty(targetField)){
                map.put(sourceField, targetField);
                continue;
            }
            //自动生成id
            if(Objects.equals("pk", targetField)){
                map.put(sourceField, SnowflakeIdWorker.nextId());
                continue;
            }

            //主表字段
            if (sourceSplit.length == 1) {
                fromObjectGetField(sourceField, targetField, jsonObject, map);
            } else {
                String[] targetSplit = targetField.split("\\.");
                if (targetSplit.length > 1) {
                    subFieldName = targetSplit[0];
                }
                subFields.put(sourceSplit[1], targetField.substring(subFieldName.length() + 1));
            }
        }
        if (ObjectUtils.isNotEmpty(subFieldName)) {
            JSONArray jsonArray = subArrayToJson(jsonObject, subFields, subFieldName);
            map.put("children", jsonArray);
        }
        return map;
    }

    /**
     * 抽取子表字段转为json
     *
     * @param jsonObject   json数据
     * @param subFields    子表字段
     * @param subFieldName 子表名
     * @return 返回字段字段数据
     */
    private JSONArray subArrayToJson(JSONObject jsonObject, Map<String, String> subFields, String subFieldName) {
        List<Map<String, Object>> itemList = new ArrayList<>();
        Object targetObject = jsonObject.get(subFieldName);
        JSONArray jsonArray = JSONArray.parseArray(targetObject.toString());
        for (Object object : jsonArray) {
            JSONObject obj = (JSONObject) object;
            Map<String, Object> map = new HashMap<>(16);
            for (String sourceField : subFields.keySet()) {
                String targetField = subFields.get(sourceField);
                fromObjectGetField(sourceField, targetField, obj, map);
            }
            itemList.add(map);
        }
        return JSONArray.parseArray(JSON.toJSONString(itemList));

    }

    /**
     * 根据目标字段在json中取值
     *
     * @param sourceField 源字段
     * @param targetField 目标字段
     * @param jsonObject  数据
     * @param map         保存值
     */
    private void fromObjectGetField(String sourceField, String targetField, JSONObject jsonObject, Map<String,
            Object> map) {
        String[] targetSplit = targetField.split("\\.");
        Object targetObject;
        //目标字段
        if (targetSplit.length == 1) {
            targetObject = jsonObject.get(targetField);
        } else {
            targetObject = jsonObject.get(targetSplit[0]);
            JSONObject obj = (JSONObject) targetObject;
            targetObject = obj.get(targetSplit[1]);
        }
        map.put(sourceField, targetObject);
    }


    /**
     * 判断是对象还是数组
     *
     * @param jsonStr 字符串
     * @return 返回类型
     */
    private String isJsonArrayOrObject(String jsonStr) {
        Object object = JSON.parse(jsonStr);
        if (object instanceof JSONObject) {
            return "object";
        } else if (object instanceof JSONArray) {
            return "array";
        }
        return null;
    }


    /**
     * 把mq消息发送到moqui
     *
     * @param parameters 订单数据
     */
    private void updateToMoqui(JSONObject parameters, String url) {
        ResponseEntity<String> resp = restTemplate.postForEntity(url, parameters, String.class);
        AssertUtils.isTrue(resp.getStatusCode() == HttpStatus.OK, "更新订单异常");
    }
}
