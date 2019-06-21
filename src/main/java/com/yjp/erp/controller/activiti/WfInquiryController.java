package com.yjp.erp.controller.activiti;

import com.alibaba.fastjson.JSONObject;
import com.yjp.erp.model.dto.gateway.ListEntityDTO;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.service.activiti.WfInquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @Description
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/6/4
 */
@RestController
@RequestMapping("/apps/wfInquiry")
public class WfInquiryController {
    /**
     * 调用moqui请求返回结果状态值
     */
    private final static String REQUEST_SUCCESS_CODE = "200";
    private final static String REQUEST_STATUS_MSG = "code";

    private final WfInquiryService wfInquiryService;

    @Autowired
    public WfInquiryController(WfInquiryService wfInquiryService) {
        this.wfInquiryService = wfInquiryService;
    }

    @PostMapping("taskByBillId")
    public JsonResult taskByBillId(@RequestBody @Validated ListEntityDTO listEntityDTO) throws Exception {
        JSONObject result = wfInquiryService.inquiryBacklogByNode(listEntityDTO);
        if (Objects.equals(REQUEST_SUCCESS_CODE, result.getString(REQUEST_STATUS_MSG))) {
            return RetResponse.makeOKRsp(result);
        }
        return RetResponse.makeErrRsp("moqui request error: " + result.getString("msg"));
    }

}
