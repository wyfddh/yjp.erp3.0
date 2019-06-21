package com.yjp.erp.mapper.parsexml;

import com.yjp.erp.model.po.view.ViewEntity;
import com.yjp.erp.service.parsexml.model.ViewEntityFilter;
import com.yjp.erp.service.parsexml.model.vo.ViewEntityListVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewEntityMapper {
    ViewEntity getViewEntityByModuleId(Long moduleId);

    void insertViewEntity(ViewEntity viewEntity);

    Integer countViewByFilter(ViewEntityFilter viewEntityFilter);

    List<ViewEntityListVO> getModulesByClassIdAndPublishState(ViewEntityFilter viewEntityFilter);
}
