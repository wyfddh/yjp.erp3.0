package com.yjp.erp.service.view.impl;

import com.yjp.erp.mapper.parsexml.ViewAliasMapper;
import com.yjp.erp.model.po.view.ViewAlias;
import com.yjp.erp.service.view.ViewAliasesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/31 9:26
 * @Email: jianghongping@yijiupi.com
 */
@Service
public class ViewAliasesServiceImpl implements ViewAliasesService {
    @Resource
    private ViewAliasMapper viewAliasMapper;

    @Override
    public void saveViewAliases(List<ViewAlias> viewAliases) {
        viewAliasMapper.bathInsertViewAlias(viewAliases);
    }

    @Override
    public List<ViewAlias> getViewAliasByViewMemberId(Long viewEntityId){
        return viewAliasMapper.listViewAliasByViewMemberId(viewEntityId);
    }
}
