package com.yjp.erp.controller.view;

import com.yjp.erp.constants.EntityClassEnum;
import com.yjp.erp.constants.RequestEnum;
import com.yjp.erp.handle.ViewEntityHandle;
import com.yjp.erp.model.dto.bill.EntityAndServiceDTO;
import com.yjp.erp.model.dto.bill.EntityDetailDTO;
import com.yjp.erp.model.dto.bill.EntityPageDTO;
import com.yjp.erp.model.dto.bill.ViewEntityAndServiceDTO;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.model.vo.view.ViewEntityVO;
import com.yjp.erp.service.parsexml.handle.ViewEntityDetailHandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/30 11:59
 * @Email: jianghongping@yijiupi.com
 */
@RestController
@Slf4j
@RequestMapping(value = "/config/viewEntityConfig")
public class ViewEntityController {
    @Resource
    private ViewEntityHandle viewEntityHandle;
    @Resource
    private ViewEntityDetailHandle viewEntityDetailHandle;

    @PostMapping("saveView")
    public JsonResult creatView(@RequestBody ViewEntityAndServiceDTO dto) throws Exception {
        dto.setType(RequestEnum.INSERT_REQUEST.getValue());
        viewEntityHandle.handleViewAndService(dto);
        return RetResponse.makeOKRsp();
    }

    /**
     * 修改实体
     *
     * @param dto 前端统一数据模型
     * @return 默认返回值
     * @throws Exception 异常
     */
    @PostMapping("/updateEntity")
    public JsonResult updateView(@RequestBody ViewEntityAndServiceDTO dto) throws Exception {
        dto.setType(RequestEnum.UPDATE_REQUEST.getValue());
        viewEntityHandle.handleViewAndService(dto);
        return RetResponse.makeOKRsp();
    }

    /**
     * @param dto
     * @return
     */
    @PostMapping("/getAllViews")
    public JsonResult getAllViews(@RequestBody EntityPageDTO dto) {
        log.info("获取所有视图实体的" + dto);
        dto.setClassId("view");
        return RetResponse.makeOKRsp(viewEntityHandle.getAllActiveViews(dto));
    }


    @PostMapping("getViewEntityDetail")
    public JsonResult getViewEntityDetail(@RequestBody EntityDetailDTO dto) {
        ViewEntityVO viewEntityVO = viewEntityDetailHandle.handleViewEntityDetail(dto);
        return RetResponse.makeOKRsp(viewEntityVO);
    }

    /**
     * @param classId
     * @param typeId
     * @return com.yjp.erp.model.vo.JsonResult
     * @description
     */
    @GetMapping("/fields")
    public JsonResult getFieldsByEntityName(String classId, String typeId) throws Exception {
        List<Map<String, Object>> fields = viewEntityDetailHandle.getAllClassIdEntityFields(classId, typeId);
        return RetResponse.makeOKRsp(fields);
    }
}
