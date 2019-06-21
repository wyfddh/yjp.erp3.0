package com.yjp.erp.mapper;

import com.yjp.erp.model.po.bill.Bill;
import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.po.bill.TransformPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
@Repository
public interface BillMapper {

    void bathInsertBill(List<Bill> billModels);

    Bill getBillById(Long id);

    Bill getBillByModuleId(Long id);

    Bill getBillByParentId(Long id);

    Bill getBillDetail(Module module);

    int getAllEntitiesCount(Map<String, Object> map);

    List<Map<String, Object>> getAllEntities(Map<String, Object> map);

    List<Map<String, Object>> getBillByAllFields(Module module);

    /**
     * 通过实体主表id获取明细数据
     * @param id 实体主表id
     * @return 明细数据
     */
    List<Bill> getChildrenBillByParentId(Long id);

    List<Map<String, Object>> getBillFields(Module module);

    List<Map<String, Object>> listBills(String typeId);

    /**
     * 判断eeca id是否重复
     *
     * @param id  eeca id
     * @return 返回eeca id是否重复
     */
    Long repetitionCheck(String id);

    /**
     * 根据typeId获取单据的name
     * @param ids 实体typeId集合
     * @return 返回单据的name
     */
    List<TransformPO> listBillNamesByIds(@Param("ids") Set<String> ids);

    List<Map<String,Object>> getFilterFields(Map<String,Object> map);

    String getTypeIdByModuleId(Long moduleId);

    List<Map<String,Object>> listFieldRules(Map<String,Object> map);
}
