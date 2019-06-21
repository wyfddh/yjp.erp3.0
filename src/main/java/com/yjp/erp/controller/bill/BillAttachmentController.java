package com.yjp.erp.controller.bill;

import com.yjp.erp.model.dto.bill.BillAttachmentDTO;
import com.yjp.erp.conf.exception.BaseException;
import com.yjp.erp.service.moqui.attachment.AttachmentService;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.model.vo.bill.BillAttachmentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * 附件的上传和CRUD
 *
 * @author wuyizhe@yijiupi.cn
 * @date 2019/4/15
 */
@RestController
@RequestMapping("/apps/attach")
@Slf4j
public class BillAttachmentController {

    @Resource
    private AttachmentService attachmentService;

    /**
     * 上传附件到ucloud
     *
     * @param billAttachment 请求上传的附件
     * @return ucloud返回的文件url
     * @throws BaseException 上传异常
     */
    @PostMapping("/upload")
    public JsonResult uploadAttachment(MultipartFile[] billAttachment) throws BaseException {
        if (null != billAttachment && billAttachment.length > 0) {
            List<BillAttachmentVO> billAttachmentUrls = attachmentService.uploadAttachmentService(billAttachment);
            return RetResponse.makeOKRsp(billAttachmentUrls);
        }
        return RetResponse.makeOKRsp();
    }

    /**
     * 将实体或单据与附件进行绑定
     *
     * @param billAttachmentDTO 附件的详细信息和单据或实体的唯一标识
     * @return 状态码
     * @throws BaseException 绑定异常
     * @throws IOException   请求异常
     */
    @PostMapping("/add")
    public JsonResult addAttachment(@RequestBody BillAttachmentDTO[] billAttachmentDTO) throws BaseException, IOException {
        attachmentService.addAttachmentService(billAttachmentDTO);
        return RetResponse.makeOKRsp();
    }

    /**
     * 查找附件信息
     *
     * @param businessId 单据或实体唯一标识
     * @return 附件详细数据
     * @throws BaseException 查询异常
     */
    @GetMapping("/find")
    public JsonResult findAttachment(String businessId) throws BaseException {
        List<BillAttachmentVO> billAttachments = this.attachmentService.findAttachmentService(businessId);
        return RetResponse.makeOKRsp(billAttachments);
    }

    /**
     * 逻辑删除实体或单据绑定的附件信息
     *
     * @param billAttachmentDTO 请求参数
     * @return 无
     * @throws BaseException 解绑异常
     */
    @PostMapping("/update")
    public JsonResult updateAttachment(@RequestBody BillAttachmentDTO billAttachmentDTO) throws BaseException {
        attachmentService.updateAttachmentService(billAttachmentDTO);
        return RetResponse.makeOKRsp();
    }
}
