package com.yjp.erp.mapper;

import com.yjp.erp.model.po.view.ViewSon;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author wyf
 * @date 2019/3/26 下午 9:05
 **/
@Repository
public interface ViewSonMapper {
    /**
     * 批量插入子视图
     * @author wyf
     * @date  2019/4/12 下午 5:30
     * @param viewSonList 子视图集合
     */
    void insertList(List<ViewSon> viewSonList);
    /**
     * 根据上级id查询子视图集合
     * @author wyf
     * @date  2019/4/12 下午 5:30
     * @param parentId 上级id
     * @return java.util.List<com.yjp.erp.model.po.view.ViewSon>
     */
    List<ViewSon> getViewSonByParentId(Long parentId);
    /**
     * 获取所有子视图
     * @author wyf
     * @date  2019/4/12 下午 5:37
     * @return java.util.List<>
     */
    List<ViewSon> getAllViewSon();
}
