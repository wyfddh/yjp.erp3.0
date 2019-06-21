package com.yjp.erp.service.view;

import com.yjp.erp.model.po.view.ViewAlias;
import com.yjp.erp.model.po.view.ViewEntity;
import com.yjp.erp.service.parsexml.model.ViewEntityFilter;
import com.yjp.erp.service.parsexml.model.vo.ViewEntityListVO;

import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/31 9:07
 * @Email: jianghongping@yijiupi.com
 */
public interface ViewEntityService {
    void saveViewEntity(ViewEntity viewEntity);

    ViewEntity getViewEntityByModule(Long moduleId);

    Integer countViewEntity(ViewEntityFilter viewEntityFilter);

    List<ViewEntityListVO> getViewEntityByFilter(ViewEntityFilter viewEntityFilter);
}
