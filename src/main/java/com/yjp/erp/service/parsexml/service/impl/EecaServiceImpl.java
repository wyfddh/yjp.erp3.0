package com.yjp.erp.service.parsexml.service.impl;

import com.yjp.erp.mapper.parsexml.BillEecaMapperP;
import com.yjp.erp.model.po.eeca.BillEeca;
import com.yjp.erp.service.parsexml.service.EecaService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/24 20:56
 * @Email: jianghongping@yijiupi.com
 */
@Service
public class EecaServiceImpl implements EecaService {
    @Resource
    private BillEecaMapperP billEecaMapperP;
    @Override
    public List<BillEeca> listBillEecaByModuleId(Long moduleId) {
        return billEecaMapperP.listBillEecaByModuleId(moduleId);
    }
}
