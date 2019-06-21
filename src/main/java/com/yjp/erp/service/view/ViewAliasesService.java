package com.yjp.erp.service.view;

import com.yjp.erp.model.po.view.ViewAlias;

import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/31 9:26
 * @Email: jianghongping@yijiupi.com
 */
public interface ViewAliasesService {

     void saveViewAliases(List<ViewAlias> viewAliases);

     List<ViewAlias> getViewAliasByViewMemberId(Long viewEntityId);
}
