package com.yjp.erp.model.dto.dbxml;

import com.yjp.erp.model.po.view.ViewField;
import com.yjp.erp.model.po.view.ViewParent;
import com.yjp.erp.model.po.view.ViewSon;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * @author wyf
 * @date 2019/3/29 下午 3:07
 **/
@Data
public class ViewToXmlFile {

    private ViewParent viewParent;

    private List<ViewSon> viewSonList;

    private Map<Long,List<ViewField>> viewField;

}
