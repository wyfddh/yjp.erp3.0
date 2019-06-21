package com.yjp.erp.model.dto.bill;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

/**
 * @Description
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/4/15
 */
public class BillAttachmentDTO {

    private String id;

    private String businessId;

    private String attachmentName;

    private String attachmentUrl;

    private String description;

    private String createTime;

    private MultipartFile[] billAttachment;

    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public MultipartFile[] getBillAttachment() {
        return billAttachment;
    }

    public void setBillAttachment(MultipartFile[] billAttachment) {
        this.billAttachment = billAttachment;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BillAttachmentDTO{" +
                "id='" + id + '\'' +
                ", businessId='" + businessId + '\'' +
                ", attachmentName='" + attachmentName + '\'' +
                ", attachmentUrl='" + attachmentUrl + '\'' +
                ", description='" + description + '\'' +
                ", createTime='" + createTime + '\'' +
                ", billAttachment=" + Arrays.toString(billAttachment) +
                ", status=" + status +
                '}';
    }
}
