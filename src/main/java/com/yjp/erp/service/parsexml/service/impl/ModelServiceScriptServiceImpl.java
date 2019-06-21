package com.yjp.erp.service.parsexml.service.impl;

import com.yjp.erp.mapper.parsexml.ModelServiceScriptMapper;
import com.yjp.erp.model.domain.ModelServiceScript;
import com.yjp.erp.service.parsexml.service.ModelServiceScriptService;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/29 15:34
 * @Email: jianghongping@yijiupi.com
 */
@Repository
public class ModelServiceScriptServiceImpl implements ModelServiceScriptService {
    @Resource
    ModelServiceScriptMapper modelServiceScriptMapper;

    @Override
    public List<ModelServiceScript> getAllModelServiceScript() {
        return modelServiceScriptMapper.getAllModelServiceScript();
    }
}
