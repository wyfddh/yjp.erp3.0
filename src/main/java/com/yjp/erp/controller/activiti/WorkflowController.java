package com.yjp.erp.controller.activiti;

import com.baomidou.mybatisplus.plugins.Page;
import com.yjp.erp.model.dto.activiti.WorkflowDTO;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.model.vo.activiti.WorkflowVO;
import com.yjp.erp.service.activiti.IWorkflowService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 工作流引擎 前端控制器
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-03
 */
@RestController
@RequestMapping("/config/workflow")
public class WorkflowController {


    private final IWorkflowService workflowServiceImpl;

    @Autowired
    public WorkflowController(IWorkflowService workflowServiceImpl) {
        this.workflowServiceImpl = workflowServiceImpl;
    }

    /**
     * @throws Exception
     * @Title: saveWorkflow
     * @Description: 新增工作流引擎
     * @param: workflowDTO 入参
     * @return: JsonResult
     */
    @PostMapping("/save")
    public JsonResult saveWorkflow(@RequestBody WorkflowDTO workflowDTO) throws Exception {
        if (StringUtils.isBlank(workflowDTO.getName())) {
            return RetResponse.makeErrRsp("工作流名称不能为空");
        }
        if (StringUtils.isBlank(workflowDTO.getIdentifier())) {
            return RetResponse.makeErrRsp("工作流标识符不能为空");
        }
        if (!workflowServiceImpl.insertWorkflow(workflowDTO)) {
            return RetResponse.makeErrRsp("新增保存工作流失败");
        }
        return RetResponse.makeOKRsp();
    }

    /**
     * @throws Exception
     * @Title: updateWorkflow
     * @Description: 修改工作流引擎
     * @param: workflowDTO 入参
     * @return: JsonResult
     */
    @PostMapping("/update")
    public JsonResult updateWorkflow(@RequestBody WorkflowDTO workflowDTO) throws Exception {
        if (Objects.isNull(workflowDTO.getId())) {
            return RetResponse.makeErrRsp("工作流id不能为空！");
        }
        if (StringUtils.isBlank(workflowDTO.getName())) {
            return RetResponse.makeErrRsp("工作流名称不能为空！");
        }
        if (StringUtils.isBlank(workflowDTO.getIdentifier())) {
            return RetResponse.makeErrRsp("工作流标识符不能为空！");
        }
        if (!workflowServiceImpl.updateWorkflow(workflowDTO)) {
            return RetResponse.makeErrRsp("更新保存工作流失败！");
        }
        return RetResponse.makeOKRsp();
    }

    /**
     * @throws Exception
     * @Title: deleteWorkflow
     * @Description: 删除工作流引擎
     * @param: workflowDTO 入参：工作流id
     * @return: JsonResult
     */
    @PostMapping("/delete")
    public JsonResult deleteWorkflow(@RequestBody WorkflowDTO workflowDTO) throws Exception {
        Long id = workflowDTO.getId();
        if (Objects.isNull(id)) {
            return RetResponse.makeErrRsp("工作流id不能为空！");
        }
        if (!workflowServiceImpl.deleteWorkflow(id)) {
            return RetResponse.makeErrRsp("删除工作流失败");
        }
        return RetResponse.makeOKRsp();
    }

    /**
     * @throws Exception
     * @Title: listWorkflowPage
     * @Description: 分页查询工作流引擎
     * @param: workflowDTO 入参
     * @return: JsonResult
     */
    @PostMapping("/list")
    public JsonResult<Page<WorkflowVO>> listWorkflowPage(@RequestBody WorkflowDTO workflowDTO) throws Exception {
        return workflowServiceImpl.listWorkflowPage(workflowDTO);
    }

    /**
     * @throws
     * @Title: viewWorkflow
     * @Description: 查看工作流详情
     * @param: workflowDTO 入参：工作流id
     * @return: JsonResult
     */
    @PostMapping("/view")
    public JsonResult<WorkflowVO> viewWorkflow(@RequestBody WorkflowDTO workflowDTO) throws Exception {
        Long id = workflowDTO.getId();
        if (Objects.isNull(id)) {
            return RetResponse.makeErrRsp("工作流id不能为空！");
        }
        return workflowServiceImpl.viewWorkflow(id);
    }

    /**
     * @throws Exception
     * @Title: listAllWorkflow
     * @Description: 查询所有有效工作流
     * @param: workflowDTO 入参
     * @return: JsonResult
     */
    @PostMapping("/listAll")
    public JsonResult<List<WorkflowVO>> listAllWorkflow(@RequestBody WorkflowDTO workflowDTO) throws Exception {
        return workflowServiceImpl.listAllWorkflow(workflowDTO);
    }


}

