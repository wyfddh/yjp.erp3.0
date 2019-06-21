package com.yjp.erp.mapper;

import com.yjp.erp.model.domain.ActionDO;

import java.util.List;

public interface ApplicationActionMapper {

    List<ActionDO> getBillActions(Long moduleId);
}
