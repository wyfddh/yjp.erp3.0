package com.yjp.erp.controller.view;

import com.yjp.erp.model.dto.bill.EntityDetailDTO;
import com.yjp.erp.model.dto.bill.EntityAndServiceDTO;
import com.yjp.erp.model.dto.bill.EntityPageDTO;
import com.yjp.erp.handle.BillHandle;
import com.yjp.erp.handle.ViewDeatilHandel;
import com.yjp.erp.handle.ViewXmlHandle;
import com.yjp.erp.service.ViewService;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.model.vo.bill.BillVO;
import com.yjp.erp.model.vo.bill.ModelVO;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wyf
 * @description: ERP3.0视图配置接口
 * @date 2019/3/27 上午 10:36
 **/
@RestController
@Slf4j
@RequestMapping(value = "/config/viewConfig")
public class ViewController {

    @Autowired
    private BillHandle billHandle;
    @Autowired
    private ViewDeatilHandel viewDeatilHandel;
    @Autowired
    private ViewXmlHandle viewXmlHandle;
    @Autowired
    private ViewService viewService;


    /**
     * @author wyf
     * @description      将实体元数据解析为xml
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @GetMapping("/entityXml")
    public JsonResult test() throws Exception {

        viewXmlHandle.dbXmlDataRelease("cityView", "view-entity");
        return RetResponse.makeOKRsp();
    }

    /**
     * @author wyf
     * @description       添加新的视图元数据
     * @param dto 视图入参实体
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("createView")
    public JsonResult creatView(@RequestBody EntityAndServiceDTO dto) throws Exception{
        dto.setType(1);
        billHandle.handleViewAndService(dto);
        return RetResponse.makeOKRsp();
    }

    /**
     * @author wyf
     * @description      修改视图元数据
     * @param dto 视图入参实体
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("updateView")
    public JsonResult updateView(@RequestBody EntityAndServiceDTO dto) throws Exception{
        dto.setType(2);
        billHandle.handleViewAndService(dto);
        return RetResponse.makeOKRsp();
    }

    /**
     * @author wyf
     * @description       获取视图实体数据详情
     * @param dto 视图入参实体
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("/detail")
    public JsonResult listBills(@RequestBody EntityDetailDTO dto) throws Exception{
        BillVO billVO = viewDeatilHandel.getViewDetail(dto);
        return RetResponse.makeOKRsp(billVO);
    }

    /**
     * @author wyf
     * @description      获取视图基础字段及服务
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @GetMapping("/model")
    public JsonResult getModelFields() throws Exception{

        ModelVO model = viewDeatilHandel.getModelFields();
        return RetResponse.makeOKRsp(model);
    }

    /**
     * @author wyf
     * @description       通过分页信息，查询实体对象的列表
     * @param dto 视图入参实体
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("/getAllViews")
    public JsonResult getAllViews(@RequestBody EntityPageDTO dto) throws Exception{

        Map<String,Object> retMap = viewDeatilHandel.getAllViews(dto);
        return RetResponse.makeOKRsp(retMap);
    }

    /**
     * @author wyf
     * @description       根据entityName获取其字段
     * @param  entityName 实体名
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @GetMapping("/fields")
    public JsonResult getFieldsByEntityName(String entityName) throws Exception{

        List<Map<String,Object>> list = viewService.getFieldsByEntityName(entityName);
        return RetResponse.makeOKRsp(list);
    }

}