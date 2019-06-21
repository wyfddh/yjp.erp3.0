package com.yjp.erp.mapper.parsexml;

import com.yjp.erp.model.po.view.ViewAlias;

import java.util.List;

public interface ViewAliasMapper {
    List<ViewAlias> listViewAliasByViewMemberId(Long viewEntityId);

    void bathInsertViewAlias(List<ViewAlias> viewAliases);
}
