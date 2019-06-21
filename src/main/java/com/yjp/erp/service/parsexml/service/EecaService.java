package com.yjp.erp.service.parsexml.service;

import com.yjp.erp.model.po.eeca.BillEeca;

import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/23 17:54
 * @Email: jianghongping@yijiupi.com
 */
public interface EecaService {
    /**
     * 根据moduleId查询出服务的集合
     * @param moduleId
     * @return
     */
    List<BillEeca> listBillEecaByModuleId(Long moduleId);
}
