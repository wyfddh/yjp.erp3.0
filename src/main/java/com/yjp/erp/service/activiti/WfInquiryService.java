package com.yjp.erp.service.activiti;

import com.alibaba.fastjson.JSONObject;
import com.yjp.erp.model.dto.gateway.ListEntityDTO;

/**
 * @Description
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/6/5
 */
public interface WfInquiryService {

    /**
     * 获取新建和处理中状态下的单据所处工作流节点
     *
     * @param listEntityDTO moqui接口请求参数
     * @return moqui请求返回
     * @throws Exception http请求异常
     */
    JSONObject inquiryBacklogByNode(ListEntityDTO listEntityDTO) throws Exception;
}
