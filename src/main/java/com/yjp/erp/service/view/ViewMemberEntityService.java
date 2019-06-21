package com.yjp.erp.service.view;

import com.yjp.erp.model.po.view.ViewMemberEntity;

import java.util.List;

public interface ViewMemberEntityService {
    void saveViewMemberEntities(List<ViewMemberEntity> viewMemberEntity);

    List<ViewMemberEntity> getViewMemberEntitiesByViewEntityId(Long viewEntityId);
}
