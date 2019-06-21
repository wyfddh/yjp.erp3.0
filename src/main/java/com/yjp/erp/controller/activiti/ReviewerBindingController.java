package com.yjp.erp.controller.activiti;


import com.baomidou.mybatisplus.plugins.Page;
import com.yjp.erp.model.dto.activiti.ReviewerBindingDTO;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.model.vo.activiti.ReviewerBindingVO;
import com.yjp.erp.service.activiti.IReviewerBindingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * <p>
 * 审核人绑定表 前端控制器
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-04
 */
@RestController
@RequestMapping("/config/reviewerBinding")
public class ReviewerBindingController {

    private final IReviewerBindingService reviewerBindingServiceImpl;

    @Autowired
    public ReviewerBindingController(IReviewerBindingService reviewerBindingServiceImpl) {
        this.reviewerBindingServiceImpl = reviewerBindingServiceImpl;
    }

    /**
     * @Title: saveReviewerBinding
     * @Description: 新增审核人绑定
     * @param: reviewerBindingDTO 入参
     * @return: JsonResult<Boolean>
     */
    @PostMapping("/save")
    public JsonResult saveReviewerBinding(@RequestBody ReviewerBindingDTO reviewerBindingDTO) throws Exception {
        if (Objects.isNull(reviewerBindingDTO.getOrgId())) {
            return RetResponse.makeErrRsp("区域id不能为空！");
        }
        if (!reviewerBindingServiceImpl.insertReviewerBinding(reviewerBindingDTO)) {
            return RetResponse.makeErrRsp("新增审核人绑定失败");
        }
        return RetResponse.makeOKRsp();
    }

    /**
     * @throws Exception
     * @Title: updateReviewerBinding
     * @Description: 修改审核人绑定
     * @param: reviewerBindingDTO 入参
     * @return: JsonResult<Boolean>
     */
    @PostMapping("/update")
    public JsonResult updateReviewerBinding(@RequestBody ReviewerBindingDTO reviewerBindingDTO) throws Exception {
        if (Objects.isNull(reviewerBindingDTO.getId())) {
            return RetResponse.makeErrRsp("工作流id不能为空！");
        }
        if (!reviewerBindingServiceImpl.updateReviewerBinding(reviewerBindingDTO)) {
            return RetResponse.makeErrRsp("更新审核人绑定失败");
        }
        return RetResponse.makeOKRsp();
    }

    /**
     * @throws Exception
     * @Title: deleteReviewerBinding
     * @Description: 删除审核人绑定
     * @param: reviewerBindingDTO 入参：审核人绑定id
     * @return: JsonResult<Boolean>
     */
    @PostMapping("/delete")
    public JsonResult deleteReviewerBinding(@RequestBody ReviewerBindingDTO reviewerBindingDTO) throws Exception {
        if (Objects.isNull(reviewerBindingDTO.getId())) {
            return RetResponse.makeErrRsp("审核人绑定id不能为空！");
        }
        if (!reviewerBindingServiceImpl.deleteReviewerBinding(reviewerBindingDTO.getId())) {
            return RetResponse.makeErrRsp("删除审核人绑定失败");
        }
        return RetResponse.makeOKRsp();
    }

    /**
     * @throws Exception
     * @Title: listReviewerBinding
     * @Description: 查询审核人绑定
     * @param: reviewerBindingDTO 入参
     */
    @PostMapping("/list")
    public JsonResult<Page<ReviewerBindingVO>> listReviewerBinding(@RequestBody ReviewerBindingDTO reviewerBindingDTO) throws Exception {
        return reviewerBindingServiceImpl.listReviewerBindingPage(reviewerBindingDTO);
    }

    /**
     * @throws
     * @Title: viewReviewerBinding
     * @Description: 查看审核人绑定信息
     * @param: reviewerBindingDTO 入参：审核人绑定id
     * @return: JsonResult<ReviewerBindingVO>
     */
    @PostMapping("/view")
    public JsonResult<ReviewerBindingVO> viewReviewerBinding(@RequestBody ReviewerBindingDTO reviewerBindingDTO) throws Exception {
        Long id = reviewerBindingDTO.getId();
        if (Objects.isNull(id)) {
            return RetResponse.makeErrRsp("审核人绑定id不能为空！");
        }
        return reviewerBindingServiceImpl.viewReviewerBinding(id);
    }


}

