package com.yjp.erp.service.activiti.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yjp.erp.handle.GatewayHandle;
import com.yjp.erp.model.dto.gateway.ListEntityDTO;
import com.yjp.erp.service.activiti.WfInquiryService;
import com.yjp.erp.util.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/6/5
 */
@Service
public class WfInquiryServiceImpl implements WfInquiryService {

    private final GatewayHandle gatewayHandle;

    @Value("${moqui.activiti.inquiry}")
    private String url;

    @Autowired
    public WfInquiryServiceImpl(GatewayHandle gatewayHandle) {
        this.gatewayHandle = gatewayHandle;
    }

    @Override
    public JSONObject inquiryBacklogByNode(ListEntityDTO listEntityDTO) throws Exception {
        // 添加单据状态condition 分页查询所有的新建和处理中的单据
        ListEntityDTO.Condition statusCondition = new ListEntityDTO.Condition();
        List<Long> status = new ArrayList<>(6);
        status.add(1L);
        status.add(2L);
        statusCondition.setFieldName("status");
        statusCondition.setValue(status);
        statusCondition.setCondition("EntityCondition.IN");
        List<ListEntityDTO.Condition> conditions = new ArrayList<>(16);
        conditions.add(statusCondition);
        listEntityDTO.getData().setConditions(conditions);
        Map<String, Object> entityList = gatewayHandle.entityList(listEntityDTO);

        // 获取单据的billId 调用moqui根据billId获取单据的工作流所处节点
        List<String> billIds = new ArrayList<>();
        Object list = entityList.get("list");
        JSONArray jsonArray = JSON.parseArray(String.valueOf(list));
        for (int i = 0; i < jsonArray.size(); i++) {
            billIds.add(jsonArray.getJSONObject(i).getString("billId"));
        }
        Map<String, String> headers = new HashMap<>(4);
        headers.put("Content-Type", "application/json");
        return JSON.parseObject(HttpClientUtils.postParameters(url, JSON.toJSONString(billIds), headers));
    }
}
