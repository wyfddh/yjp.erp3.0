/*
package com.yjp.erp.controller.activiti;


import com.baomidou.mybatisplus.plugins.Page;
import com.yjp.erp.constants.StatusEnum;
import com.yjp.erp.model.dto.activiti.AuditConfigDTO;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.model.vo.activiti.AuditConfigVO;
import com.yjp.erp.service.activiti.IAuditConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

*/
/**
 * <p>
 * 审核配置表 前端控制器
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-03
 *//*

@RestController
@RequestMapping("/config/auditConfig")
public class AuditConfigController {

    private static final Logger log = LoggerFactory.getLogger(AuditConfigController.class);

    private final IAuditConfigService auditConfigServiceImpl;

    @Autowired
    public AuditConfigController(IAuditConfigService auditConfigServiceImpl) {
        this.auditConfigServiceImpl = auditConfigServiceImpl;
    }

    */
/**
     * @throws Exception
     * @Title: saveAuditConfig
     * @Description: 新增审核配置
     * @param: auditConfigDTO 入参
     * @return: JsonResult
     *//*

    @PostMapping("/save")
    public JsonResult<Boolean> saveAuditConfig(@RequestBody AuditConfigDTO auditConfigDTO) throws Exception {
        if (Objects.isNull(auditConfigDTO.getTypeId())) {
            return RetResponse.makeErrRsp("实体名称不能为空！");
        }
        if (Objects.isNull(auditConfigDTO.getClassId())) {
            return RetResponse.makeErrRsp("实体类型不能为空！");
        }
        if (Objects.isNull(auditConfigDTO.getOrgId())) {
            return RetResponse.makeErrRsp("区域id不能为空！");
        }
        if (Objects.isNull(auditConfigDTO.getWorkflowId())) {
            return RetResponse.makeErrRsp("工作流id不能为空！");
        }
        return auditConfigServiceImpl.insertAuditConfig(auditConfigDTO);
    }

    */
/**
     * @throws Exception
     * @Title: updateAuditConfig
     * @Description: 修改审核配置
     * @param: auditConfigDTO 入参
     * @return: JsonResult
     *//*

    @PostMapping("/update")
    public JsonResult<Boolean> updateAuditConfig(@RequestBody AuditConfigDTO auditConfigDTO) throws Exception {
        if (Objects.isNull(auditConfigDTO.getId())) {
            return RetResponse.makeErrRsp("审核配置id不能为空！");
        }
        if (Objects.isNull(auditConfigDTO.getTypeId())) {
            return RetResponse.makeErrRsp("实体名称不能为空！");
        }
        if (Objects.isNull(auditConfigDTO.getClassId())) {
            return RetResponse.makeErrRsp("实体类型不能为空！");
        }
        if (Objects.isNull(auditConfigDTO.getOrgId())) {
            return RetResponse.makeErrRsp("区域id不能为空！");
        }
        if (Objects.isNull(auditConfigDTO.getWorkflowId())) {
            return RetResponse.makeErrRsp("工作流id不能为空！");
        }
        return auditConfigServiceImpl.updateAuditConfig(auditConfigDTO);
    }

    */
/**
     * @throws Exception
     * @Title: listAuditConfig
     * @Description: 查询审核配置信息
     * @param: auditConfigDTO 入参
     * @return: JsonResult
     *//*

    @PostMapping("/list")
    public JsonResult<Page<AuditConfigVO>> listAuditConfig(@RequestBody AuditConfigDTO auditConfigDTO) throws Exception {
        return auditConfigServiceImpl.listAuditConfigPage(auditConfigDTO);
    }

    */
/**
     * @throws
     * @Title: viewAuditConfig
     * @Description: 查看审核配置信息
     * @param: auditConfigDTO
     * @return: JsonResult
     *//*

    @PostMapping("/view")
    public JsonResult<AuditConfigVO> viewAuditConfig(@RequestBody AuditConfigDTO auditConfigDTO) {
        return auditConfigServiceImpl.viewAuditConfig(auditConfigDTO.getId());
    }

    */
/**
     * @throws Exception
     * @Title: deleteAuditConfig
     * @Description: 删除审核配置
     * @param: auditConfigDTO 入参：审核配置id
     * @return: JsonResult
     *//*

    @PostMapping("/delete")
    public JsonResult<Boolean> deleteAuditConfig(@RequestBody AuditConfigDTO auditConfigDTO) {
        return auditConfigServiceImpl.deleteAuditConfig(auditConfigDTO.getId());
    }

    */
/**
     * @throws
     * @Title: enableAuditConfig
     * @Description: 启用审核配置
     * @param: auditConfigDTO 入参：审核配置id
     * @return: JsonResult
     *//*

    @PostMapping("/enable")
    public JsonResult<Boolean> enableAuditConfig(@RequestBody AuditConfigDTO auditConfigDTO) throws Exception {
        return auditConfigServiceImpl.updateStatus(auditConfigDTO.getId(), StatusEnum.RELEASE.getKey());
    }

    */
/**
     * @throws
     * @Title: disableAuditConfig
     * @Description: 禁用审核配置
     * @param: auditConfigDTO 入参：审核配置id
     * @return: JsonResult
     *//*

    @PostMapping("/disable")
    public JsonResult<Boolean> disableAuditConfig(@RequestBody AuditConfigDTO auditConfigDTO) throws Exception {
        return auditConfigServiceImpl.updateStatus(auditConfigDTO.getId(), StatusEnum.TEMPORARY_STORAGE.getKey());
    }
}

*/
