package com.yjp.erp.controller.bill;

import com.yjp.erp.constants.EntityConstant;
import com.yjp.erp.constants.ModuleConstant;
import com.yjp.erp.constants.RequestEnum;
import com.yjp.erp.handle.*;
import com.yjp.erp.model.dto.bill.*;
import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.model.vo.bill.BillVO;
import com.yjp.erp.model.vo.bill.ModelVO;
import com.yjp.erp.model.vo.bill.ServiceAndActionVO;
import com.yjp.erp.service.parsexml.handle.ParseXmlServiceHandle;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author liushui
 * @description: ERP3.0单据配置接口
 * @date 2019/3/20
 */
@RestController
@Slf4j
@RequestMapping(value = "/config")
@Api(tags = "单据配置相关接口")
public class BillController {

    @Resource
    private BillHandle billHandle;

    @Resource
    private BillDetailHandel billDetailHandel;

    @Resource
    private ModelHandle modelHandle;

    @Resource
    private BillReleaseHandle billReleaseHandle;

    @Resource
    private CustomEntityHandel customEntityHandel;
    @Resource
    private ParseXmlServiceHandle parseXmlServiceHandle;

    /**
     * 创建实体
     *
     * @param dto 前端统一数据模型
     * @return 默认返回值
     * @throws Exception 异常
     */
    @PostMapping("/createEntity")
    @Transactional
    public JsonResult addBill(@Valid @RequestBody EntityAndServiceDTO dto) throws Exception {
        dto.setType(RequestEnum.INSERT_REQUEST.getValue());
        billHandle.handleBillAndService(dto);
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
    @Transactional
    public JsonResult updateBill(@Valid @RequestBody EntityAndServiceDTO dto) throws Exception {
        dto.setType(RequestEnum.UPDATE_REQUEST.getValue());
        billHandle.handleBillAndService(dto);
        return RetResponse.makeOKRsp();
    }

    /**
     * 功能描述: 删除表单实体
     *
     * @author liushui
     * @date 2019/4/12
     */
    @PostMapping("/deleteEntity")
    public JsonResult deleteEntity(@RequestBody EntityDetailDTO dto) throws Exception {
        billHandle.deleteEntity(dto);
        return RetResponse.makeOKRsp();
    }

    /**
     * 获取实体数据详情
     *
     * @param dto 单据查询入参模型
     * @return 返回实体vo对象
     * @throws Exception 异常
     */
    @GetMapping("/getEntityDetail")
    public JsonResult listBills(EntityDetailDTO dto) throws Exception {
        BillVO billVO = billDetailHandel.getBillDetail(dto);

        return RetResponse.makeOKRsp(billVO);
    }

    /**
     * 获取表单基础字段及服务
     *
     * @return 返回module vo对象
     */
    @GetMapping("/model")
    public JsonResult getModelFields() {
        ModelVO model = modelHandle.getModelFields();
        return RetResponse.makeOKRsp(model);
    }

    /**
     * 通过分页信息，查询实体对象的列表
     *
     * @param dto 分页查询入参
     * @return 实体列表
     * @throws Exception 异常
     */
    @GetMapping("/getAllEntitys")
    public JsonResult getAllEntities(EntityPageDTO dto) throws Exception {
        Map<String, Object> retMap = billDetailHandel.getAllEntities(dto);
        return RetResponse.makeOKRsp(retMap);
    }


    /**
     * 获取表单或实体字段
     *
     * @param dto 查询入参
     * @return 字段集合
     * @throws Exception 异常
     */
    @GetMapping("/billFields")
    public JsonResult getBillFields(EntityDetailDTO dto) throws Exception {

        List<Map<String, Object>> retList = customEntityHandel.listFields(dto);
        return RetResponse.makeOKRsp(retList);
    }

    /**
     * 获取表单及其子表实体的字段
     *
     * @param dto 获取实体及其子实体的入参
     * @return 返回表单及其子表实体的字段
     * @throws Exception 异常
     */
    @GetMapping("/billAndChildFields")
    public JsonResult getBillAndChildFields(EntityDetailDTO dto) throws Exception {
        Map<String, List<Map<String, Object>>> retList = customEntityHandel.listBillAndChildFields(dto);
        return RetResponse.makeOKRsp(retList);
    }

    /**
     * 根据classId获取所有表单或实体等的下拉列表
     *
     * @param dto 查询实体和表单的入参条件
     * @return 表单或实体的列表
     */
    @GetMapping("/billList")
    public JsonResult listEntities(EntityDetailDTO dto) {

        List<Map<String, Object>> retList = customEntityHandel.listEntities(dto);
        return RetResponse.makeOKRsp(retList);
    }

    /**
     * 通过module，把实体及其相关的配置转成xml,并发布
     *
     * @param module 实体信息
     * @return 返回默认格式
     * @throws Exception 异常
     */
    @PostMapping("/transformation")
    public JsonResult transformationToXml(@RequestBody Module module) throws Exception {
//            parseXmlServiceHandle.exeParse(module);

        if (ModuleConstant.CLASS_VIEW.equals(module.getClassId())) {
            parseXmlServiceHandle.exeParse(module);
            return RetResponse.makeOKRsp();
        }

        billReleaseHandle.dbXmlDataRelease(module);
        customEntityHandel.publishEntity(module);
        return RetResponse.makeOKRsp();
    }

    /**
     * 新增service时获取script模板
     *
     * @return 返回服务的script模板
     */
    @GetMapping("/service/scriptDemo")
    public JsonResult getScriptDemo() {
        String script = billHandle.getScriptDemo();
        return RetResponse.makeOKRsp(script);
    }

    /**
     * 根据实体名模糊搜索实体
     *
     * @param dto 实体参数
     * @return 返回实体详情
     */
    @GetMapping("/searchEntities")
    public JsonResult searchEntities(EntityDetailDTO dto) {

        List<Map<String, Object>> entityFields = billHandle.searchEntities(dto.getLabel());
        return RetResponse.makeOKRsp(entityFields);
    }

    /**
     * 根据实体id获取实体字段信息
     *
     * @param dto 实体参数
     * @return 返回实体字段信息
     */
    @GetMapping("/getEntityFields")
    public JsonResult getEntityFields(EntityDetailDTO dto) {

        List<Map<String, Object>> entityFields = billHandle.getEntityFields(dto.getId());
        return RetResponse.makeOKRsp(entityFields);
    }

    /**
     * 功能描述: 获取通用行为及服务
     *
     * @return 返回通用行为及服务
     */
    @GetMapping("/commonServiceAndAction")
    public JsonResult commonServiceAndAction() {
        ServiceAndActionVO serviceAndActionVO = billHandle.getCommonServiceAndAction();
        return RetResponse.makeOKRsp(serviceAndActionVO);
    }


    /**
     * 功能描述: 判断eeca id是否重复
     *
     * @param dto eeca id
     * @return 返回eeca id是否重复
     */
    @GetMapping("/repetitionCheck")
    public JsonResult repetitionCheck(@Valid EecaDTO dto) {

        Boolean isRepeated = customEntityHandel.repetitionCheck(dto);
        return RetResponse.makeOKRsp(isRepeated);
    }

    /**
     * 功能描述: 获取所有实体（包括表单及视图）
     *
     * @return 返回所有实体
     */
    @GetMapping("/listSelectedEntities")
    public JsonResult listSelectedEntities() {
        List<Map<String, Object>> entities = customEntityHandel.listSelectedEntities();
        return RetResponse.makeOKRsp(entities);
    }

    /**
     * 功能描述: 根据实体classId和typeId获取所属服务
     *
     * @param dto 实体classId和typeId
     * @return 返回实体所属服务
     */
    @GetMapping("/listServiceByTypeIdAndClassId")
    public JsonResult listServiceByTypeIdAndClassId(EntityDetailDTO dto) {
        List<Map<String, Object>> entities = customEntityHandel.listServiceByTypeIdAndClassId(dto);
        return RetResponse.makeOKRsp(entities);
    }

    /**
     * 功能描述: 根据单据实体获取其昵称
     *
     * @param dto 实体classId和typeId
     * @return 返回map key为typeId value为要替换的值
     */
    @PostMapping("/listBillNamesByIds")
    public JsonResult<Map<String, String>> listBillNamesByIds(@RequestBody Set<IdReplaceDTO> dto) {
        Map<String, String> nameMap = customEntityHandel.listBillNamesByIds(dto);
        return RetResponse.makeOKRsp(nameMap);
    }

    /**
     * 功能描述: 判断实体表单typeId是否重复
     *
     * @param classId 实体classId
     * @param typeId  实体typeId
     * @return 返回map key为typeId value为要替换的值
     */
    @GetMapping("/typeIdCheck")
    public JsonResult typeIdCheck(String classId, String typeId) {
        return RetResponse.makeOKRsp(customEntityHandel.typeIdCheck(classId, typeId));
    }
}
