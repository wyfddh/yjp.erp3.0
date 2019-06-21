package com.yjp.erp.controller.activiti;

import com.baomidou.mybatisplus.plugins.Page;
import com.yjp.erp.model.dto.activiti.BillBindingDTO;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.model.vo.activiti.BillBindingVO;
import com.yjp.erp.service.activiti.IBillBindingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 单据绑定表 前端控制器
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-04
 */
@RestController
@RequestMapping({"/config/billBinding", "/apps/billBinding"})
public class BillBindingController {

    @Autowired
    IBillBindingService billBindingServiceImpl;

    /**
     * @throws Exception
     * @Title: saveBillBinding
     * @Description: 新增单据绑定
     * @param: billBindingDTO 入参
     * @return: JsonResult
     */
    @PostMapping("/save")
    public JsonResult<Boolean> saveBillBinding(@RequestBody BillBindingDTO billBindingDTO) throws Exception {
        if (StringUtils.isBlank(billBindingDTO.getClassId())) {
            return RetResponse.makeErrRsp("实体类型不能为空！");
        }
        if (StringUtils.isBlank(billBindingDTO.getTypeId())) {
            return RetResponse.makeErrRsp("实体名称不能为空！");
        }
        if (Objects.isNull(billBindingDTO.getWorkflowId())) {
            return RetResponse.makeErrRsp("工作流id不能为空！");
        }
        return billBindingServiceImpl.insertBillBinding(billBindingDTO);
    }

    /**
     * @throws Exception
     * @Title: updateBillBinding
     * @Description: 修改单据绑定
     * @param: billBindingDTO 入参
     * @return: JsonResult
     */
    @PostMapping("/update")
    public JsonResult<Boolean> updateBillBinding(@RequestBody BillBindingDTO billBindingDTO) throws Exception {
        if (Objects.isNull(billBindingDTO.getId())) {
            return RetResponse.makeErrRsp("单据绑定id不能为空！");
        }
        if (StringUtils.isBlank(billBindingDTO.getClassId())) {
            return RetResponse.makeErrRsp("实体类型不能为空！");
        }
        if (StringUtils.isBlank(billBindingDTO.getTypeId())) {
            return RetResponse.makeErrRsp("实体名称不能为空！");
        }
        if (Objects.isNull(billBindingDTO.getWorkflowId())) {
            return RetResponse.makeErrRsp("工作流不能为空！");
        }
        return billBindingServiceImpl.updateBillBinding(billBindingDTO);
    }

    /**
     * @throws Exception
     * @Title: deleteBillBinding
     * @Description: 删除单据绑定
     * @param: billBindingDTO 入参：单据绑定id
     * @return: JsonResult
     */
    @PostMapping("/delete")
    public JsonResult<Boolean> deleteBillBinding(@RequestBody BillBindingDTO billBindingDTO) throws Exception {
        Long id = billBindingDTO.getId();
        if (Objects.isNull(id)) {
            return RetResponse.makeErrRsp("单据绑定id不能为空！");
        }
        return billBindingServiceImpl.deleteBillBinding(id);
    }

    /**
     * @throws Exception
     * @Title: listBillBinding
     * @Description: 分页查询单据绑定
     * @param: billBindingDTO 入参
     * @return: JsonResult
     */
    @PostMapping("/list")
    public JsonResult<Page<BillBindingVO>> listBillBinding(@RequestBody BillBindingDTO billBindingDTO) throws Exception {
        return billBindingServiceImpl.listBillBindingPage(billBindingDTO);
    }

    /**
     * @throws
     * @Title: viewBillBinding
     * @Description: 查看单据绑定信息
     * @param: billBindingDTO 入参：单据绑定id
     * @return: JsonResult
     */
    @PostMapping("/view")
    public JsonResult<BillBindingVO> viewBillBinding(@RequestBody BillBindingDTO billBindingDTO) throws Exception {
        Long id = billBindingDTO.getId();
        if (Objects.isNull(id)) {
            return RetResponse.makeErrRsp("单据绑定id不能为空！");
        }
        return billBindingServiceImpl.viewBillBinding(id);
    }

    /**
     * @throws Exception
     * @Title: listBillBinding
     * @Description: 查询所有单据绑定
     * @return: JsonResult
     */
    @GetMapping("/listAll")
    public JsonResult<List<BillBindingVO>> listBillBindings() throws Exception {
        return billBindingServiceImpl.listBillBindings();
    }
}

