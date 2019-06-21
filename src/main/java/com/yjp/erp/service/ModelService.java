package com.yjp.erp.service;

import com.yjp.erp.model.domain.ServicePropertyDO;
import com.yjp.erp.mapper.ModelMapper;
import com.yjp.erp.model.po.bill.BillField;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * model的操作
 * @author liushui
 * @date 2019/3/27
 */
@Service
public class ModelService {

    @Resource
    private ModelMapper modelMapper;

    public List<BillField> getModelMoquiProperties() {
        return modelMapper.getModelMoquiProperties();
    }

    public List<BillField> getModelWebProperties() {
        return modelMapper.getModelWebProperties();
    }

    public List<ServicePropertyDO> getGlobalServices() {
        return modelMapper.getGlobalServices();
    }
}
