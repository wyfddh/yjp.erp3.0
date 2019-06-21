package com.yjp.erp.controller;

import com.yjp.erp.handle.BillReleaseHandle;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.service.BaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
/**
* @Description: 实体controller
* @CreateDate: 2019/4/8 19:22
* @EMAIL: jianghongping@yijiupi.com
*/
@RestController
@Slf4j
@RequestMapping(value = "/config")
@Api(tags = "单据配置相关接口")
public class EntityController {

    @Autowired
    private BaseService baseService;
    @Autowired
    BillReleaseHandle billReleaseHandle;

    @PostMapping("/transformationAll")
    @ApiOperation(value = "把所有实体及其相关的配置转成xml", notes = "通过module，把实体及其相关的配置转成xml")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求成功", response = Map.class, responseContainer = "Object")})
    public JsonResult transformationAllToXml() {
        billReleaseHandle.dbXmlAllDataRelease();
        return RetResponse.makeOKRsp(null);
    }

    @PostMapping("/packageData")
    @ApiOperation(value = "把生成的xml打成zip包", notes = "把生成的xml打成zip包")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求成功", response = Map.class, responseContainer = "Object")})
    public JsonResult packageAllXmlData() throws Exception {
        baseService.packageAllXml();
        return RetResponse.makeOKRsp(null);
    }

    @PostMapping("/uploadData")
    @ApiOperation(value = "把生成的xml打成zip包", notes = "把生成的xml打成zip包")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求成功", response = Map.class, responseContainer = "Object")})
    public JsonResult uploadData() throws Exception {
        baseService.uploadData();
        return RetResponse.makeOKRsp(null);
    }

    @PostMapping("/recoverComponent")
    @ApiOperation(value = "解压替换原有的component", notes = "解压替换原有的component")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求成功", response = Map.class, responseContainer = "Object")})
    public JsonResult recoverComponent() throws Exception {
        baseService.recoverComponent();
        return RetResponse.makeOKRsp(null);
    }
}
