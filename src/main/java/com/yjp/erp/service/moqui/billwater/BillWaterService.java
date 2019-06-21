package com.yjp.erp.service.moqui.billwater;

import com.alibaba.fastjson.JSONObject;
import com.yjp.erp.model.dto.bill.BillWaterDTO;
import com.yjp.erp.conf.exception.BaseException;
import com.yjp.erp.model.vo.bill.BillWaterVO;

/**
 * @Description
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/4/25
 */
public interface BillWaterService {

    BillWaterVO findBillInventorySummary(BillWaterDTO billWaterDTO) throws BaseException;

    BillWaterVO findBillInboundWater(BillWaterDTO billWaterDTO) throws BaseException;

    BillWaterVO findBillOutboundWater(BillWaterDTO billWaterDTO) throws BaseException;

    JSONObject findProfit(JSONObject queryParameter) throws BaseException;
}
