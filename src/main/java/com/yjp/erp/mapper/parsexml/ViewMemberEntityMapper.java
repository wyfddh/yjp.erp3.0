package com.yjp.erp.mapper.parsexml;

import com.yjp.erp.model.po.view.ViewEntity;
import com.yjp.erp.model.po.view.ViewMemberEntity;

import java.util.List;

public interface ViewMemberEntityMapper {
    List<ViewMemberEntity> listViewMemberEntitiesByViewEntityId(Long moduleId);

    void bathInsertViewMemberEntities(List<ViewMemberEntity> viewMemberEntity);
}
