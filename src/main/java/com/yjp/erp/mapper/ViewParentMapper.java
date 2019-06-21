package com.yjp.erp.mapper;

import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.po.view.ViewParent;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 * @author wyf
 * @date 2019/3/26 下午 8:57
 **/
@Repository
public interface ViewParentMapper {
    /**
     * 通过id获取父级视图
     * @author wyf
     * @date  2019/4/12 下午 5:27
     * @param  id 视图父级id
     * @return com.yjp.erp.model.po.view.ViewParent
     */
    ViewParent getViewParentById(Long id);
    /**
     * 插入父级视图
     * @author wyf
     * @date  2019/4/12 下午 5:27
     * @param viewParent 父级视图
     */
    void insert(ViewParent viewParent);
    /**
     * 更新父级视图
     * @author wyf
     * @date  2019/4/12 下午 5:28
     * @param viewParent 视图父级
     */
    void update(ViewParent viewParent);
    /**
     * 根据moduleId获取父级视图
     * @author wyf
     * @date  2019/4/12 下午 5:28
     * @param moduleId 模板id
     * @return com.yjp.erp.model.po.view.ViewParent
     */
    ViewParent getViewByModuleId(Long moduleId);
    /**
     * 通过条件查询数量
     * @author wyf
     * @date  2019/4/12 下午 5:28
     * @param map 条件
     * @return int
     */
    int countViewByMap(Map<String, Object> map);
    /**
     * 查询视图
     * @author wyf
     * @date  2019/4/12 下午 5:29
     * @param map 条件
     * @return java.util.List<>
     */
    List<Map<String,Object>> getViewByMap(Map<String, Object> map);
    /**
     * 获取视图名
     * @author wyf
     * @date  2019/4/12 下午 5:29
     * @param module 模板
     * @return java.lang.String
     */
    String getViewNameByClassIdAndTypeId(Module module);

    /**
     * 根据classId查询视图
     * @author wyf
     * @date  2019/4/12 下午 5:29
     * @param classId 实体classId
     * @return 返回视图实体数据
     */
    List<Map<String,Object>> listViews(String classId);
    /**
     * 根据entityName查询视图列属性
     * @author wyf
     * @date  2019/5/21 下午 5:51
     * @param  entityName 实体名
     * @return java.util.Map<>
     */
    Map<String,String> getFieldsByEntityName(String entityName);
}
