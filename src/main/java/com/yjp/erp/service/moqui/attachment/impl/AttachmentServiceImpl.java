package com.yjp.erp.service.moqui.attachment.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yijiupi.himalaya.basic.file.domain.FileConfig;
import com.yijiupi.himalaya.basic.file.dto.FileInfoDTO;
import com.yijiupi.himalaya.basic.file.utility.FileUpdateHelper;
import com.yjp.erp.conf.BillAttachmentProperties;
import com.yjp.erp.model.dto.bill.BillAttachmentDTO;
import com.yjp.erp.conf.exception.BaseException;
import com.yjp.erp.model.po.bill.BillAttachmentPO;
import com.yjp.erp.service.moqui.attachment.AttachmentService;
import com.yjp.erp.util.HttpClientUtils;
import com.yjp.erp.util.ObjectUtils;
import com.yjp.erp.model.vo.RetCode;
import com.yjp.erp.model.vo.bill.BillAttachmentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 将附件上传到ucloud 单据或实体附件的绑定,解绑和查看
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/4/15
 */
@Service
@Slf4j
public class AttachmentServiceImpl implements AttachmentService {

    /**
     * 附件逻辑删除标记(-1已删除1正常)
     */
    private static final Integer ATTACH_DELETE_STATUS = -1;
    private static final Integer ATTACH_USEFUL_STATUS = 1;
    private static final String UPDATE_SUCCESS_SIGN="{ }";
    private static final String HANDLE_SUCCESS_CODE="200";
    private static final String HANDLE_RETURN_MSG="code";

    @Value("${moqui.rest.createYjpAttachment}")
    private String createYjpAttachmentUrl;

    @Value("${moqui.rest.updateYjpAttachment}")
    private String updateYjpAttachmentUrl;

    @Value("${moqui.rest.findYjpAttachment}")
    private String findYjpAttachmentUrl;

    @Value("${moqui.rest.uploadAttachmentUrl}")
    private String uploadAttachmentUrl;

    @Resource
    private BillAttachmentProperties billAttachmentProperties;

    @Override
    public void addAttachmentService(BillAttachmentDTO[] billAttachmentDTO) throws BaseException {
        log.info("addAttachmentService: 请求添加附件:" + JSON.toJSON(billAttachmentDTO));
        for (BillAttachmentDTO dto : billAttachmentDTO) {
          /*  if (null != dto.getId()) {
                updateAttachmentService(dto);
            }*/
            if (null == dto.getId()) {
                Map<String, String> headers = new HashMap<>(4);
                headers.put("Content-Type", "application/json");
                BillAttachmentPO billAttachmentPO = new BillAttachmentPO();
                billAttachmentPO.setAttachmentName(dto.getAttachmentName());
                billAttachmentPO.setStatus(ATTACH_USEFUL_STATUS);
                billAttachmentPO.setCreateTime(String.valueOf(System.currentTimeMillis()));
                billAttachmentPO.setAttachmentUrl(dto.getAttachmentUrl());
                billAttachmentPO.setBusinessId(dto.getBusinessId());
                billAttachmentPO.setDescription(dto.getDescription());
                try {
                    JSONObject jsonObject = JSON.parseObject(HttpClientUtils.postParameters(createYjpAttachmentUrl, JSON.toJSONString(billAttachmentPO), headers));
                    if (null != jsonObject && null != jsonObject.getString("id")) {
                        log.info("addAttachmentService: 添加附件:" + JSON.toJSON(billAttachmentPO) + " 请求返回:" + JSON.toJSON(jsonObject));
                        continue;
                    }
                    log.error("addAttachmentService: 添加附件失败:" + JSON.toJSON(billAttachmentPO) + " 请求返回:" + JSON.toJSON(jsonObject));
                } catch (Exception e) {
                    throw new BaseException(RetCode.INTERNAL_SERVER_ERROR, e.getMessage());
                }
            }
        }
    }

    @Override
    public void updateAttachmentService(BillAttachmentDTO billAttachmentDTO) throws BaseException {
        log.info("updateAttachmentService: 删除附件: " + JSON.toJSON(billAttachmentDTO));
        BillAttachmentPO billAttachmentPO = new BillAttachmentPO();
        billAttachmentPO.setId(billAttachmentDTO.getId());
        billAttachmentPO.setCreateTime(String.valueOf(System.currentTimeMillis()));
        billAttachmentPO.setStatus(ATTACH_DELETE_STATUS);

        Map<String, String> headers = new HashMap<>(4);
        headers.put("Content-Type", "application/json");
        try {
            String response = HttpClientUtils.postParameters(updateYjpAttachmentUrl, JSON.toJSONString(billAttachmentPO), headers);
            if (UPDATE_SUCCESS_SIGN.equals(response)) {
                return;
            }
        } catch (Exception e) {
            throw new BaseException(RetCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        log.error("updateAttachmentService: 删除附件失败: " + JSON.toJSON(billAttachmentDTO));
    }

    @Override
    public List<BillAttachmentVO> findAttachmentService(String businessId) throws BaseException {
        List<BillAttachmentVO> listVO = new ArrayList<>();
        try {
            JSONObject jsonObject = JSON.parseObject(HttpClientUtils.get(findYjpAttachmentUrl + "?businessId=" + businessId));
            log.info("findAttachmentService: " + "请求参数: " + JSON.toJSONString(businessId) + " 请求返回: " + jsonObject.toJSONString());
            jsonObject = ObjectUtils.isNotEmpty(jsonObject) ? jsonObject : new JSONObject();
            if (ObjectUtils.isNotEmpty(jsonObject) && HANDLE_SUCCESS_CODE.equals(jsonObject.getString(HANDLE_RETURN_MSG))) {
                List<BillAttachmentPO> list = JSONObject.parseArray(jsonObject.getString("data"), BillAttachmentPO.class);
                if (null != list && list.size() > 0) {
                    for (BillAttachmentPO billAttachmentPO : list) {
                        BillAttachmentVO billAttachmentVO = new BillAttachmentVO();
                        billAttachmentVO.setId(billAttachmentPO.getId());
                        billAttachmentVO.setAttachmentName(billAttachmentPO.getAttachmentName());
                        billAttachmentVO.setAttachmentUrl(billAttachmentPO.getAttachmentUrl());
                        billAttachmentVO.setBusinessId(billAttachmentPO.getBusinessId());
                        billAttachmentVO.setCreateTime(billAttachmentPO.getCreateTime());
                        billAttachmentVO.setDescription(billAttachmentPO.getDescription());
                        listVO.add(billAttachmentVO);
                    }
                }
                return listVO;
            }
            throw new BaseException(RetCode.FAIL, jsonObject.getString("msg"));
        } catch (Exception e) {
            throw new BaseException(RetCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public List<BillAttachmentVO> uploadAttachmentService(MultipartFile[] attachments) {
        List<BillAttachmentVO> list = new ArrayList<>();

        FileConfig fileConfig = new FileConfig(billAttachmentProperties.getProjectName());
        fileConfig.setDownloadSuffix(billAttachmentProperties.getDownloadsuffix());
        fileConfig.setPrivateKey(billAttachmentProperties.getPrivateKey());
        fileConfig.setPublicKey(billAttachmentProperties.getPublicKey());
        fileConfig.setUrl("yjp", billAttachmentProperties.getEnv(), billAttachmentProperties.getBucketName(), billAttachmentProperties.getProxySuffix());
        List<FileInfoDTO> fileInfoDTOS = FileUpdateHelper.uploadPicFile(attachments, null, fileConfig);

        if (null != fileInfoDTOS && fileInfoDTOS.size() > 0) {
            for (FileInfoDTO fileInfoDTO : fileInfoDTOS) {
                BillAttachmentVO billAttachmentVO = new BillAttachmentVO();
                billAttachmentVO.setAttachmentUrl(fileInfoDTO.getCloudSrc());
                list.add(billAttachmentVO);
            }
        }
        log.info("附件上传: 上传文件数量: " + attachments.length + " 请求返回: " + JSON.toJSON(list));
        return list;
    }
}
