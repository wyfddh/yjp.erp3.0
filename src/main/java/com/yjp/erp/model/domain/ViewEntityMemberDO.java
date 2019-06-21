package com.yjp.erp.model.domain;

import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.po.view.ViewAlias;
import com.yjp.erp.model.po.view.ViewEntity;
import com.yjp.erp.model.po.view.ViewMemberEntity;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/30 12:07
 * @Email: jianghongping@yijiupi.com
 */
@Data
public class ViewEntityMemberDO {
    private Module module;
    private ViewEntity viewEntity;
    private List<ViewMemberEntity> viewMemberEntities;
    private List<ViewAlias> viewAliases;
}
