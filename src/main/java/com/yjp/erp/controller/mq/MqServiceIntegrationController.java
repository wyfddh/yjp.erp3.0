package com.yjp.erp.controller.mq;


import com.yjp.erp.model.dto.mq.MqConfigDTO;
import com.yjp.erp.model.dto.mq.MqServiceIntegrationDTO;
import com.yjp.erp.model.mq.MqServiceIntegration;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.model.vo.mq.MqServiceIntegrationVO;
import com.yjp.erp.service.mq.MqServiceIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * mq消息系统集成
 * @author xialei
 * @date  2019-06-04
 */
@RestController
@RequestMapping(value = "/config/mqConfig")
public class MqServiceIntegrationController {
    @Autowired
    private MqServiceIntegrationService mqServiceIntegrationService;

    @PostMapping("/list")
    public JsonResult list(@RequestBody MqConfigDTO dto) {
        Map<String, Object> retMap = mqServiceIntegrationService.findAllList(dto);
        return RetResponse.makeOKRsp(retMap);
    }

    @GetMapping("/detail")
    public JsonResult get(@RequestParam String id) {
        MqServiceIntegrationVO vo = mqServiceIntegrationService.get(id);
        return RetResponse.makeOKRsp(vo);
    }

    @PostMapping("/create")
    public JsonResult insert(@RequestBody MqServiceIntegrationDTO dto) {
        mqServiceIntegrationService.insert(dto);
        return RetResponse.makeOKRsp();
    }


    @PostMapping("/update")
    public JsonResult update(@RequestBody MqServiceIntegrationDTO dto) {
        mqServiceIntegrationService.update(dto);
        return RetResponse.makeOKRsp();
    }

    @PostMapping("/delete")
    public JsonResult delete(@RequestBody MqConfigDTO dto) {
        mqServiceIntegrationService.delete(dto.getId());
        return RetResponse.makeOKRsp();
    }


    @PostMapping("/bind")
    public JsonResult bind(@RequestBody MqConfigDTO dto) {
        mqServiceIntegrationService.bind(dto.getId());
        return RetResponse.makeOKRsp();
    }

    @PostMapping("/unBind")
    public JsonResult unbind(@RequestBody MqConfigDTO dto) {
        mqServiceIntegrationService.unbind(dto.getId());
        return RetResponse.makeOKRsp();
    }

}
