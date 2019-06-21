package com.yjp.erp.service.view.impl;

import com.yjp.erp.mapper.parsexml.ViewEntityMapper;
import com.yjp.erp.model.po.view.ViewAlias;
import com.yjp.erp.model.po.view.ViewEntity;
import com.yjp.erp.service.parsexml.model.ViewEntityFilter;
import com.yjp.erp.service.parsexml.model.vo.ViewEntityListVO;
import com.yjp.erp.service.view.ViewEntityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/31 9:08
 * @Email: jianghongping@yijiupi.com
 */
@Service
public class ViewEntityServiceImpl implements ViewEntityService {
    @Resource
    ViewEntityMapper viewEntityMapper;

    @Override
    public void saveViewEntity(ViewEntity viewEntity) {
        viewEntityMapper.insertViewEntity(viewEntity);
    }

    @Override
    public ViewEntity getViewEntityByModule(Long moduleId) {
        return viewEntityMapper.getViewEntityByModuleId(moduleId);
    }

    @Override
    public Integer countViewEntity(ViewEntityFilter viewEntityFilter) {
        return viewEntityMapper.countViewByFilter(viewEntityFilter);
    }

    @Override
    public List<ViewEntityListVO> getViewEntityByFilter(ViewEntityFilter viewEntityFilter)  {
        return viewEntityMapper.getModulesByClassIdAndPublishState(viewEntityFilter);
    }
}
