package com.yjp.erp.controller.gateway;

import com.alibaba.fastjson.JSON;
import com.yjp.erp.constants.RequestEnum;
import com.yjp.erp.model.dto.gateway.*;
import com.yjp.erp.handle.GatewayHandle;
import com.yjp.erp.service.activiti.InvokeMoquiService;
import com.yjp.erp.service.moqui.attachment.AttachmentService;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;


/**
 * description:
 * @author liushui
 * @date 2019/3/29
 */
@RestController
@RequestMapping("/apps/gateway")
@Slf4j
public class GatewayController {

    @Autowired
    private GatewayHandle gatewayHandle;

    @Autowired
    InvokeMoquiService invokeMoquiServiceImpl;

    @Autowired
    private AttachmentService attachmentService;


    /**
     * 功能描述: 查询实体等详情通用接口
     * @author liushui
     * @date 2019/4/9
     */
    @PostMapping("/detail")
    public JsonResult queryDetail(@Valid @RequestBody QueryDetailDTO dto)throws Exception{

        return RetResponse.makeOKRsp(gatewayHandle.queryDetail(dto));
    }

    /**
     * 功能描述: 查询实体列表页
     * @author liushui
     * @date 2019/4/9
     */
    @PostMapping("/list")
    public JsonResult entityList(@RequestBody ListEntityDTO dto)throws Exception{

        return RetResponse.makeOKRsp(gatewayHandle.entityList(dto));
    }

    /**
     * 功能描述: 保存实体
     * @author liushui
     * @date 2019/4/9
     */
    @PostMapping("/save")
    public JsonResult saveEntity(@RequestBody GatewayDTO dto)throws Exception{

        return RetResponse.makeOKRsp(gatewayHandle.saveEntity(dto, RequestEnum.INSERT_REQUEST));
    }

    /**
     * 功能描述: 修改实体
     * @author liushui
     * @date 2019/4/9
     */
    @PostMapping("/update")
    public JsonResult updateEntity(@RequestBody GatewayDTO dto)throws Exception{

        return RetResponse.makeOKRsp(gatewayHandle.saveEntity(dto,RequestEnum.UPDATE_REQUEST));
    }

    /**
     * 功能描述: 删除实体
     * @author liushui
     * @date 2019/4/9
     */
    @PostMapping("/delete")
    public JsonResult deleteEntity(@RequestBody GatewayDTO dto)throws Exception{

        String responds = gatewayHandle.transportReq(dto);
        return RetResponse.makeOKRsp(JSON.parseObject(responds));
    }

    /**
     * 功能描述: 获取引用所有的下拉列表
     * @author liushui
     * @date 2019/4/17
     */
    @PostMapping("/refSpinner")
    public JsonResult refSpinner(@RequestBody FilterDTO dto)throws Exception{

        List<Map> response = gatewayHandle.getRefSpinner(dto);
        return RetResponse.makeOKRsp(response);
    }

    /**
     * 功能描述: 前端查询字段替换统一接口(单个id),
     * @author liushui
     * @date 2019/4/9
     */
    @PostMapping("/getRefValue")
    @Deprecated
    public JsonResult getRefValue(@RequestBody RefDTO dto)throws Exception{

        Object object = gatewayHandle.getRefValue(dto);
        return RetResponse.makeOKRsp(object);
    }

    /**
     * 功能描述: 前端查询字段替换统一接口(多个id)
     * @author liushui
     * @date 2019/4/9
     */
    @PostMapping("/listRefValue")
    public JsonResult listRefValue(@RequestBody List<RefDTO> dto)throws Exception{

        Map retMap = gatewayHandle.listRefValue(dto);
        return RetResponse.makeOKRsp(retMap);
    }

    /**
     * 功能描述: 根据配置字段过滤获取表单实体详情列表
     * @author liushui
     * @date 2019/4/9
     */
    @PostMapping("/filter")
    @Deprecated
    public JsonResult filterEntityRecord(@RequestBody FilterDTO dto)throws Exception{

        String response = gatewayHandle.filterEntityRecord(dto);
        return RetResponse.makeOKRsp(JSON.parseObject(response));
    }

    /**
     * 功能描述: 生成工作流，并保存单据
     * @author liushui
     * @date 2019/4/16
     */
    @PostMapping("/saveAndProcess")
    public JsonResult saveAndProcess(@RequestBody GatewayDTO dto)throws Exception{

        Map<String,Object> retMap = gatewayHandle.saveAndProcess(dto);
        return RetResponse.makeOKRsp(retMap.get("id"));
    }

    /**
     * 功能描述: 生成工作流，并保存单据,然后传递
     * @author liushui
     * @date 2019/4/16
     */
    @PostMapping("/saveAndProcessAndPush")
    public JsonResult saveAndProcessAndPush(@RequestBody GatewayDTO dto)throws Exception{

        Map<String,Object> retMap = gatewayHandle.saveAndProcessAndPush(dto);
        return RetResponse.makeOKRsp(retMap.get("id"));
    }
}
