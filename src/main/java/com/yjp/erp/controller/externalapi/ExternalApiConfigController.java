package com.yjp.erp.controller.externalapi;


import com.yjp.erp.model.dto.externalapi.ExternalApiConfigDTO;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.model.vo.externalapi.ExternalApiConfigVO;
import com.yjp.erp.service.externalapi.ExternalApiConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author xialei
 * @date 2019-06-17
 */
@RestController
@RequestMapping(value = "/config/externalApiConfig")
public class ExternalApiConfigController {

    @Resource
    private ExternalApiConfigService externalApiConfigService;

    @PostMapping("/list")
    public JsonResult list(@RequestBody ExternalApiConfigDTO dto) {
        Map<String, Object> retMap = externalApiConfigService.findAllList(dto);
        return RetResponse.makeOKRsp(retMap);
    }

    @GetMapping("/detail")
    public JsonResult get(@RequestParam String id) {
        ExternalApiConfigVO vo = externalApiConfigService.get(id);
        return RetResponse.makeOKRsp(vo);
    }

    @PostMapping("/create")
    public JsonResult insert(@RequestBody ExternalApiConfigDTO dto) {
        externalApiConfigService.insert(dto);
        return RetResponse.makeOKRsp();
    }


    @PostMapping("/update")
    public JsonResult update(@RequestBody ExternalApiConfigDTO dto) {
        externalApiConfigService.update(dto);
        return RetResponse.makeOKRsp();
    }

    @PostMapping("/delete")
    public JsonResult delete(@RequestBody ExternalApiConfigDTO dto) {
        externalApiConfigService.delete(dto.getId());
        return RetResponse.makeOKRsp();
    }


    @PostMapping("/enable")
    public JsonResult enable(@RequestBody ExternalApiConfigDTO dto) {
        externalApiConfigService.enable(dto.getId());
        return RetResponse.makeOKRsp();
    }


    @PostMapping("/stop")
    public JsonResult stop(@RequestBody ExternalApiConfigDTO dto) {
        externalApiConfigService.stop(dto.getId());
        return RetResponse.makeOKRsp();
    }

}
