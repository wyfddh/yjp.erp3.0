package com.yjp.erp.mapper.base;

import com.yjp.erp.model.po.bill.Bill;
import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.po.bill.TransformPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
@Repository
public interface BaseEntityMapper {

    void bathInsertBill(List<Bill> billModels);

    Bill getBillById(Long id);

    Bill getBillByModuleId(Long id);

    Bill getBillByParentId(Long id);

    Bill getBillDetail(Module module);

    int getAllEntitiesCount(Map<String, Object> map);

    List<Map<String,Object>> getAllEntities(Map<String, Object> map);

    List<Bill> getChildrenBillByParentId(Long id);

    /**
     *  根据实体label模糊搜索实体
     * @param label 实体中文名称
     * @return 返回实体信息
     */
    List<Map<String,Object>> searchEntities(String label);

    List<Map<String,Object>> getEntityFields(Long id);

    List<Map<String,Object>> listEntities(String classId);

    /**
     * 根据typeId获取实体的name
     * @param ids 实体typeId集合
     * @return 返回实体的name
     */
    List<TransformPO> listEntityNamesByIds(@Param("ids") Set<String> ids);

    List<Map<String,Object>> getFilterFields(Map<String,Object> map);

    List<Map<String,Object>> listFields(Module module);

    List<Map<String,Object>> listFieldRules(Map<String,Object> map);
}
