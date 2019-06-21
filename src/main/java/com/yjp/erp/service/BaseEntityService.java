package com.yjp.erp.service;

import com.yjp.erp.model.domain.BillDO;
import com.yjp.erp.model.dto.bill.EntityPageDTO;
import com.yjp.erp.mapper.base.*;
import com.yjp.erp.model.po.bill.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class BaseEntityService {

    @Autowired(required = false)
    private BaseEntityMapper baseEntityMapper;

    @Autowired(required = false)
    private BaseEntityFieldMapper baseEntityFieldMapper;

    @Autowired(required = false)
    private BaseEntityPropertyMapper baseEntityPropertyMapper;

    @Autowired(required = false)
    private BaseEntityFieldPropertyMapper baseEntityFieldPropertyMapper;

    @Autowired(required = false)
    private BaseEntityFieldWebPropertyMapper baseEntityFieldWebPropertyMapper;

    @Autowired(required = false)
    private BaseEntityFieldWebPropertyRelMapper baseEntityFieldWebPropertyRelMapper;

    /**
     * 插入BillDO 对象
     * @param billDO
     * @throws Exception
     */
    public void insertBill(BillDO billDO){
        log.info("=============插入 bilDO");

        if (CollectionUtils.isNotEmpty(billDO.getBills())) {
            baseEntityMapper.bathInsertBill(billDO.getBills());
        }
        if (CollectionUtils.isNotEmpty(billDO.getBillFields())) {
            baseEntityFieldMapper.bathInsertBillFields(billDO.getBillFields());
        }
        if (CollectionUtils.isNotEmpty(billDO.getBillFieldWebProperties())) {
            baseEntityFieldWebPropertyMapper.bathInsertBillFieldsWebProperties(billDO.getBillFieldWebProperties());
        }
        if (CollectionUtils.isNotEmpty(billDO.getBillFieldProperties())) {
            baseEntityFieldPropertyMapper.bathInsertBillFieldsProperties(billDO.getBillFieldProperties());
        }
        if (CollectionUtils.isNotEmpty(billDO.getBillProperties())) {
            baseEntityPropertyMapper.bathInsertBillProperties(billDO.getBillProperties());
        }
        if (CollectionUtils.isNotEmpty(billDO.getBillFieldWebPropertyRels())) {
            baseEntityFieldWebPropertyRelMapper.bathInsertFieldsRefs(billDO.getBillFieldWebPropertyRels());
        }
    }

    public Bill getBillByModuleId(Long moduleId){

        return baseEntityMapper.getBillByModuleId(moduleId);
    }

    public List<BillField> getBillFieldByBillId(Long id){

        return baseEntityFieldMapper.getBillFieldByBillId(id);
    }

    public List<BillProperty> getBillPropertyByBillId(Long id){

        return baseEntityPropertyMapper.getBillPropertyByBillId(id);
    }

    public List<BillFieldProperty> getBillFieldPropertyByBillId(Long id){

        return baseEntityFieldPropertyMapper.getBillFieldPropertyByBillId(id);
    }

    public List<BillFieldWebProperty> getFieldWebProperties(Long id){

        return baseEntityFieldWebPropertyMapper.getFieldWebProperties(id);
    }

    public Map<String,Object> getRefProperties(Long id){

        return baseEntityFieldWebPropertyRelMapper.getRefProperties(id);
    }

    public Map<String,Object> getAllEntities(EntityPageDTO dto){

        Map<String,Object> param = new HashMap<>(8);
        param.put("entityName",dto.getTitle());
        param.put("status",dto.getStatus());
        int count = baseEntityMapper.getAllEntitiesCount(param);
        List<Map<String,Object>> list = new ArrayList<>();
        if(count > 0){
            param.put("pageSize",dto.getPageSize());
            param.put("startIndex",(dto.getPageNo()-1)*dto.getPageSize());
            list = baseEntityMapper.getAllEntities(param);
        }

        Map<String,Object> retMap = new HashMap<>(8);
        retMap.put("pageNo",dto.getPageNo());
        retMap.put("pageSize",dto.getPageSize());
        retMap.put("total",count);
        retMap.put("dataList",list);

        return retMap;
    }

    public List<Bill> getChildrenBillByParentId(Long billId){

        return baseEntityMapper.getChildrenBillByParentId(billId);
    }

    public List<BillField> getBillMoquiProperties(Long billId){

        return baseEntityFieldMapper.getBillMoquiProperties(billId);
    }

    public List<BillField> getBillWebProperties(Long billId){

        return baseEntityFieldMapper.getBillWebProperties(billId);
    }

    public Bill getEntityDetail(Module module){

        return baseEntityMapper.getBillDetail(module);
    }

    /**
     *  根据实体label模糊搜索实体
     * @param label 实体中文名称
     * @return 返回实体信息
     */
    public List<Map<String,Object>> searchEntities(String label){

        return baseEntityMapper.searchEntities(label);
    }

    public List<Map<String,Object>> getEntityFields(Long id){

        return baseEntityMapper.getEntityFields(id);
    }

    /**
     *  根据实体类型获取所有的实体数据
     * @param classId 实体类型
     * @return 返回所有的实体数据
     */
    public List<Map<String,Object>> listEntities(String classId){

        return baseEntityMapper.listEntities(classId);
    }

    /**
     * 根据typeId获取实体的name
     * @param typeIds 实体typeId集合
     * @return 返回实体的name
     */
    public List<TransformPO> listEntityNamesByIds(Set<String> typeIds){

        return baseEntityMapper.listEntityNamesByIds(typeIds);
    }

    public List<Map<String,Object>> listFields(Module module)throws Exception{

        return baseEntityMapper.listFields(module);
    }
}
