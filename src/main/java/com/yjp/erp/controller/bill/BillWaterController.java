package com.yjp.erp.controller.bill;

import com.alibaba.fastjson.JSONObject;
import com.yjp.erp.conf.UserInfoManager;
import com.yjp.erp.model.dto.bill.BillWaterDTO;
import com.yjp.erp.conf.exception.BaseException;
import com.yjp.erp.model.po.system.Organization;
import com.yjp.erp.service.moqui.billwater.BillWaterService;
import com.yjp.erp.service.system.OrgService;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetCode;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.model.vo.bill.BillWaterVO;
import com.yjp.erp.model.vo.system.OrgVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * description:单据流水的查询
 * @author liushui
 * @date 2019/4/11
 */
@RestController
@RequestMapping("/apps")
@Slf4j
public class BillWaterController {

    @Resource
    private BillWaterService billWaterService;

    @Resource
    private UserInfoManager userInfoManager;

    @Resource
    private OrgService orgService;

    /**
     * 查询库存汇总数据(通过组织ID orgId)
     *
     * @param billWaterDTO 请求参数
     * @return 返回库存汇总数据
     * @throws BaseException http请求异常
     */
    @PostMapping("/inventorySummary")
    public JsonResult inventorySummary(@RequestBody BillWaterDTO billWaterDTO) throws Exception {
        BillWaterVO billInventorySummary = billWaterService.findBillInventorySummary(billWaterDTO);
        if (null == billWaterDTO.getWarehouse()) {
            List<String> warehouse = new ArrayList<>();
            String orgId = billWaterDTO.getOrgId();
            if (null != orgId) {
                List<Long> orgs = new ArrayList<>(1);
                orgs.add(Long.valueOf(orgId));
                List<Organization> bOrgByVId = orgService.getBOrgByVId(orgs);
                for (Organization o : bOrgByVId) {
                    warehouse.add(o.getName());
                }
            } else {
                List<OrgVO> baseOrgList = userInfoManager.getUserInfo().getBaseOrgList();
                for (OrgVO o : baseOrgList) {
                    warehouse.add(o.getName());
                }
            }
            billWaterDTO.setWarehouse(warehouse);
        }
        if (null == billInventorySummary.getInventorySummarys()) {
            return RetResponse.makeRsp(RetCode.INTERNAL_SERVER_ERROR, billInventorySummary.getMsg());
        }
        return RetResponse.makeOKRsp(billInventorySummary);
    }

    /**
     * 查询入库流水数据 (通过组织ID orgId)
     *
     * @param billWaterDTO 请求参数
     * @return 入库流水数据
     * @throws BaseException http请求异常
     */
    @GetMapping("/inboundWater")
    public JsonResult inboundWater(BillWaterDTO billWaterDTO) throws BaseException {
        BillWaterVO billInboundWater = billWaterService.findBillInboundWater(billWaterDTO);
        if (null == billInboundWater.getInboundWaters()) {
            return RetResponse.makeRsp(RetCode.INTERNAL_SERVER_ERROR, billInboundWater.getMsg());
        }
        return RetResponse.makeOKRsp(billInboundWater);
    }

    /**
     * 查询出库流水数据 (通过组织ID orgId)
     *
     * @param billWaterDTO 请求参数
     * @return 出库流水数据
     * @throws BaseException http请求异常
     */
    @GetMapping("/outboundWater")
    public JsonResult outboundWater(BillWaterDTO billWaterDTO) throws BaseException {
        BillWaterVO billOutboundWater = billWaterService.findBillOutboundWater(billWaterDTO);
        if (null == billOutboundWater.getOutboundWaters()) {
            return RetResponse.makeRsp(RetCode.INTERNAL_SERVER_ERROR, billOutboundWater.getMsg());
        }
        return RetResponse.makeOKRsp(billOutboundWater);
    }

    /**
     * 查询 毛利
     *
     * @param queryParameter 请求参数
     * @return 出库流水数据
     * @throws BaseException http请求异常
     */
    @PostMapping("/queryProfit")
    public JsonResult queryProfit(@RequestBody JSONObject queryParameter) throws BaseException {
        JSONObject profits = billWaterService.findProfit(queryParameter);
        return RetResponse.makeOKRsp(profits);
    }
}
