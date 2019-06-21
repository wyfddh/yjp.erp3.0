package com.yjp.erp.mapper;

import com.yjp.erp.model.po.view.ViewField;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author wyf
 * @date 2019/3/27 下午 8:04
 **/
@Repository
public interface ViewFieldMapper {
    /**
     * 批量插入视图列属性
     * @author wyf
     * @date  2019/4/12 下午 5:21
     * @param viewFieldList 视图列集合
     */
    void insertList(List<ViewField> viewFieldList);
    /**
     * 获取视图列属性集合
     * @author wyf
     * @date  2019/4/12 下午 5:21
     * @param parentId 上级id
     * @return java.util.List<com.yjp.erp.model.po.view.ViewField>
     */
    List<ViewField> getViewFieldByParentId(Long parentId);
    /**
     * 通过子视图获取视图列属性集合
     * @author wyf
     * @date  2019/4/12 下午 5:21
     * @param viewSonId 子级id
     * @return java.util.List<com.yjp.erp.model.po.view.ViewField>
     */
    List<ViewField> getViewFieldBySonId(Long viewSonId);
    /**
     * 获取所有视图列属性集合
     * @author wyf
     * @date  2019/4/12 下午 5:22
     * @return java.util.List<com.yjp.erp.model.po.view.ViewField>
     */
    List<ViewField> getAllViewField();
}
