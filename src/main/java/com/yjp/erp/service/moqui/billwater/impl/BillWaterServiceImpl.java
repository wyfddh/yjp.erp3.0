package com.yjp.erp.service.moqui.billwater.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yjp.erp.model.dto.bill.BillWaterDTO;
import com.yjp.erp.conf.exception.BaseException;
import com.yjp.erp.service.moqui.billwater.BillWaterService;
import com.yjp.erp.util.HttpClientUtils;
import com.yjp.erp.util.ObjectUtils;
import com.yjp.erp.model.vo.RetCode;
import com.yjp.erp.model.vo.bill.BillWaterVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/4/25
 */
@Service
@Slf4j
public class BillWaterServiceImpl implements BillWaterService {

    @Value("${moqui.rest.findBillInventorySummary}")
    private String findBillInventorySummary;

    @Value("${moqui.rest.findBillInboundWater}")
    private String findBillInboundWater;

    @Value("${moqui.rest.findBillOutboundWater}")
    private String findBillOutboundWater;

    @Value("${moqui.rest.findProfit}")
    private String findProfit;

    @Override
    public BillWaterVO findBillInventorySummary(BillWaterDTO billWaterDTO) throws BaseException {
        BillWaterVO billWaterVO = new BillWaterVO();
        log.info("findBillInventorySummary: 查询库存汇总请求参数:" + JSON.toJSON(billWaterDTO));
        JSONObject jsonObject = postRequest(billWaterDTO, findBillInventorySummary);
        if ("200".equals(jsonObject.getString("code"))) {
            List<BillWaterVO.InventorySummary> list = JSONObject.parseArray(jsonObject.getString("data"), BillWaterVO.InventorySummary.class);
            BillWaterVO.Page page = JSONObject.toJavaObject(jsonObject.getJSONObject("page"), BillWaterVO.Page.class);
            billWaterVO.setInventorySummarys(list);
            billWaterVO.setPage(page);
            return billWaterVO;
        }
        String msg = jsonObject.getString("errorMsg");
        billWaterVO.setMsg(msg);
        if (null == msg) {
            billWaterVO.setMsg(jsonObject.getString("msg"));
        }
        log.error("findBillInventorySummary: 查询库存汇总失败,接口返回: " + JSON.toJSON(jsonObject));
        return billWaterVO;
    }

    @Override
    public BillWaterVO findBillInboundWater(BillWaterDTO billWaterDTO) throws BaseException {
        BillWaterVO billWaterVO = new BillWaterVO();
        log.info("findBillInboundWater: 查询入库流水请求参数:" + JSON.toJSON(billWaterDTO));
        JSONObject jsonObject = postRequest(billWaterDTO, findBillInboundWater);
        if ("200".equals(jsonObject.getString("code"))) {
            List<BillWaterVO.InboundWater> list = JSONObject.parseArray(jsonObject.getString("data"), BillWaterVO.InboundWater.class);
            BillWaterVO.Page page = JSONObject.toJavaObject(jsonObject.getJSONObject("page"), BillWaterVO.Page.class);
            billWaterVO.setInboundWaters(list);
            billWaterVO.setPage(page);
            return billWaterVO;
        }
        String msg = jsonObject.getString("errorMsg");
        billWaterVO.setMsg(msg);
        if (null == msg) {
            billWaterVO.setMsg(jsonObject.getString("msg"));
        }
        log.error("findBillInventorySummary: 查询入库流水失败,接口返回: " + JSON.toJSON(jsonObject));
        return billWaterVO;
    }

    @Override
    public BillWaterVO findBillOutboundWater(BillWaterDTO billWaterDTO) throws BaseException {
        BillWaterVO billWaterVO = new BillWaterVO();
        log.info("findBillOutboundWater: 查询出库流水请求参数:" + JSON.toJSON(billWaterDTO));
        JSONObject jsonObject = postRequest(billWaterDTO, findBillOutboundWater);
        if ("200".equals(jsonObject.getString("code"))) {
            List<BillWaterVO.OutboundWater> list = JSONObject.parseArray(jsonObject.getString("data"), BillWaterVO.OutboundWater.class);
            BillWaterVO.Page page = JSONObject.toJavaObject(jsonObject.getJSONObject("page"), BillWaterVO.Page.class);
            billWaterVO.setOutboundWaters(list);
            billWaterVO.setPage(page);
            return billWaterVO;
        }
        String msg = jsonObject.getString("errorMsg");
        billWaterVO.setMsg(msg);
        if (null == msg) {
            billWaterVO.setMsg(jsonObject.getString("msg"));
        }
        log.error("findBillInventorySummary: 查询出库流水失败,接口返回: " + JSON.toJSON(jsonObject));
        return billWaterVO;
    }

    private JSONObject postRequest(BillWaterDTO billWaterDTO, String url) throws BaseException {
        Map<String, String> headers = new HashMap<>(4);
        headers.put("Content-Type", "application/json");
        try {
            JSONObject object = JSON.parseObject(HttpClientUtils.postParameters(url, JSON.toJSONString(billWaterDTO), headers));
            object = ObjectUtils.isNotEmpty(object) ? object : new JSONObject();
            return object;
        } catch (Exception e) {
            throw new BaseException(RetCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public JSONObject findProfit(JSONObject queryParameter) throws BaseException {
        Map<String, String> headers = new HashMap<>(4);
        headers.put("Content-Type", "application/json");
        try {
            JSONObject object = JSON.parseObject(HttpClientUtils.postParameters(findProfit, queryParameter.toJSONString(), headers));
            object = ObjectUtils.isNotEmpty(object) ? object : new JSONObject();
            return object;
        } catch (Exception e) {
            throw new BaseException(RetCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
