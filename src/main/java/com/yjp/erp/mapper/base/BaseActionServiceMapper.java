package com.yjp.erp.mapper.base;

import com.yjp.erp.model.po.service.ActionService;

import java.util.List;

/**
 * @Description
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/3/23
 */
public interface BaseActionServiceMapper {

    List<ActionService> getServiceIdByActionId(Long id);

    void bathInsertActionService(List<ActionService> list);
}
