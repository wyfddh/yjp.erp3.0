package com.yjp.erp.service.moqui.attachment;

import com.yjp.erp.model.dto.bill.BillAttachmentDTO;
import com.yjp.erp.conf.exception.BaseException;
import com.yjp.erp.model.vo.bill.BillAttachmentVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Description
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/4/15
 */
public interface AttachmentService {

    void addAttachmentService(BillAttachmentDTO[] billAttachmentDTO) throws BaseException, IOException;

    void updateAttachmentService(BillAttachmentDTO billAttachmentDTO) throws BaseException;

    List<BillAttachmentVO> findAttachmentService(String businessId) throws BaseException;

    List<BillAttachmentVO> uploadAttachmentService(MultipartFile[] attachments) throws BaseException;
}
