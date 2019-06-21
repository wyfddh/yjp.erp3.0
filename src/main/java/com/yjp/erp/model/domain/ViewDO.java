package com.yjp.erp.model.domain;

import com.yjp.erp.model.po.view.ViewField;
import com.yjp.erp.model.po.view.ViewParent;
import com.yjp.erp.model.po.view.ViewSon;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * @author wyf
 * @date 2019/3/27 下午 2:58
 **/
@Data
public class ViewDO {

    private List<ViewSon> viewSonList = new ArrayList<>();

    private List<ViewField> viewFieldList = new ArrayList<>();

    private ViewParent viewParent = new ViewParent();

}
