package com.yjp.erp.controller.externalapi;

import com.yjp.erp.constants.ExternalCallEnum;
import com.yjp.erp.handle.ExternalCallApiHandle;
import com.yjp.erp.model.dto.externalapi.ExternalCallApiDTO;
import com.yjp.erp.model.externalapi.ExternalCallApi;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author xialei
 * @date 2019/6/17 10:29
 */

@RestController
@RequestMapping(value = "/config/externalCallApi")
public class ExternalCallApiController {
    @Resource
    private ExternalCallApiHandle apiHandle;

    @PostMapping("/list")
    public JsonResult list(@RequestBody ExternalCallApi config) throws Exception {
        List<Map> list = apiHandle.entityList(config);
        return RetResponse.makeOKRsp(list);
    }

    @PostMapping("/query")
    public JsonResult get(@RequestBody ExternalCallApiDTO dto) throws Exception {
        return RetResponse.makeOKRsp(apiHandle.queryDetail(dto));
    }

    @PostMapping("/create")
    public JsonResult insert(@RequestBody ExternalCallApiDTO dto) throws Exception {
        return RetResponse.makeOKRsp(apiHandle.saveEntity(dto, ExternalCallEnum.CREATE.getValue()));
    }

    @PostMapping("/update")
    public JsonResult update(@RequestBody ExternalCallApiDTO dto) throws Exception {
        return RetResponse.makeOKRsp(apiHandle.saveEntity(dto, ExternalCallEnum.UPDATE.getValue()));
    }

}
