package com.yjp.erp.service.view.impl;

import com.yjp.erp.mapper.parsexml.ViewMemberEntityMapper;
import com.yjp.erp.model.po.view.ViewMemberEntity;
import com.yjp.erp.service.view.ViewMemberEntityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ViewMemberEntityServiceImpl implements ViewMemberEntityService {
    @Resource
    private ViewMemberEntityMapper viewMemberEntityMapper;

    @Override
    public void saveViewMemberEntities(List<ViewMemberEntity> viewMemberEntity) {
        viewMemberEntityMapper.bathInsertViewMemberEntities(viewMemberEntity);
    }

    public List<ViewMemberEntity> getViewMemberEntitiesByViewEntityId(Long viewEntityId){
        return viewMemberEntityMapper.listViewMemberEntitiesByViewEntityId(viewEntityId);
    }
}
