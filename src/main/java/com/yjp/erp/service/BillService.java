package com.yjp.erp.service;

import com.yjp.erp.constants.EntityClassEnum;
import com.yjp.erp.model.domain.BillDO;
import com.yjp.erp.model.dto.bill.EntityPageDTO;
import com.yjp.erp.mapper.*;
import com.yjp.erp.mapper.base.*;
import com.yjp.erp.model.po.bill.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author liushui
 * @description: 表单元数据操作实现
 * @date 2019/3/20
 */
@Service
public class BillService {

    @Autowired(required = false)
    private BillMapper billMapper;

    @Autowired(required = false)
    private BillFieldMapper billFieldMapper;

    @Autowired(required = false)
    private BillPropertyMapper billPropertyMapper;

    @Autowired(required = false)
    private BillFieldPropertyMapper billFieldPropertyMapper;

    @Autowired(required = false)
    private BillFieldWebPropertyMapper billFieldWebPropertyMapper;

    @Autowired(required = false)
    private BillFieldWebPropertyRelMapper billFieldWebPropertyRelMapper;

    @Autowired(required = false)
    private BaseEntityMapper baseEntityMapper;
    @Autowired(required = false)
    private ModuleMapper moduleMapper;

    public void insertBill(BillDO billDO) throws Exception {

        if (CollectionUtils.isNotEmpty(billDO.getBills())) {
            billMapper.bathInsertBill(billDO.getBills());
        }
        if (CollectionUtils.isNotEmpty(billDO.getBillFields())) {
            billFieldMapper.bathInsertBillFields(billDO.getBillFields());
        }
        if (CollectionUtils.isNotEmpty(billDO.getBillFieldWebProperties())) {
            billFieldWebPropertyMapper.bathInsertBillFieldsWebProperties(billDO.getBillFieldWebProperties());
        }
        if (CollectionUtils.isNotEmpty(billDO.getBillFieldProperties())) {
            billFieldPropertyMapper.bathInsertBillFieldsProperties(billDO.getBillFieldProperties());
        }
        if (CollectionUtils.isNotEmpty(billDO.getBillProperties())) {
            billPropertyMapper.bathInsertBillProperties(billDO.getBillProperties());
        }
        if (CollectionUtils.isNotEmpty(billDO.getBillFieldWebPropertyRels())) {
            billFieldWebPropertyRelMapper.bathInsertFieldsRefs(billDO.getBillFieldWebPropertyRels());
        }
    }

    public Bill getBillByModuleId(Long moduleId){
        //修复无法获取 实体的字段
        Module module = moduleMapper.getModuleById(moduleId);
        if (null != module && "entity".equals(module.getClassId())) {
            return baseEntityMapper.getBillByModuleId(moduleId);
        }
        return billMapper.getBillByModuleId(moduleId);
    }

    public List<BillField> getBillFieldByBillId(Long id){

        return billFieldMapper.getBillFieldByBillId(id);
    }

    public List<BillProperty> getBillPropertyByBillId(Long id){

        return billPropertyMapper.getBillPropertyByBillId(id);
    }

    public List<BillFieldProperty> getBillFieldPropertyByBillId(Long id){

        return billFieldPropertyMapper.getBillFieldPropertyByBillId(id);
    }

    public List<BillFieldWebProperty> getFieldWebProperties(Long id){

        return billFieldWebPropertyMapper.getFieldWebProperties(id);
    }

    public Map<String,Object> getRefProperties(Long id){

        return billFieldWebPropertyRelMapper.getRefProperties(id);
    }

    public Bill getChildBillByMainId(Long billId){
       List<Bill> bills= billMapper.getChildrenBillByParentId(billId);
        if(bills.size()>0){
            return billMapper.getChildrenBillByParentId(billId).get(0);
        }
        return null;
    }

    public Map<String,Object> getAllEntities(EntityPageDTO dto){

        Map<String,Object> param = new HashMap<>(8);
        param.put("entityName",dto.getTitle());
        param.put("status",dto.getStatus());
        int count = 0;
        if(Objects.equals(dto.getClassId(), EntityClassEnum.BILL_CLASS.getValue())){
            count = billMapper.getAllEntitiesCount(param);
        }
        if(Objects.equals(dto.getClassId(), EntityClassEnum.ENTITY_CLASS.getValue())){
            count = baseEntityMapper.getAllEntitiesCount(param);
        }
        List<Map<String,Object>> list = new ArrayList<>();
        if(count > 0){
            param.put("pageSize",dto.getPageSize());
            param.put("startIndex",(dto.getPageNo()-1)*dto.getPageSize());
            if(Objects.equals(dto.getClassId(), EntityClassEnum.BILL_CLASS.getValue())){
                list = billMapper.getAllEntities(param);
            }
            if(Objects.equals(dto.getClassId(), EntityClassEnum.ENTITY_CLASS.getValue())){
                list = baseEntityMapper.getAllEntities(param);
            }
        }

        Map<String,Object> retMap = new HashMap<>(8);
        retMap.put("pageNo",dto.getPageNo());
        retMap.put("pageSize",dto.getPageSize());
        retMap.put("total",count);
        retMap.put("dataList",list);

        return retMap;
    }

    public List<Bill> getChildrenBillByParentId(Long billId){

        return billMapper.getChildrenBillByParentId(billId);
    }

    public List<BillField> getBillMoquiProperties(Long billId){

        return billFieldMapper.getBillMoquiProperties(billId);
    }

    public List<BillField> getBillWebProperties(Long billId){

        return billFieldMapper.getBillWebProperties(billId);
    }

    public List<Map<String,Object>> getBillFields(Module module)throws Exception{

        return billMapper.getBillFields(module);
    }

    public List<Map<String,Object>> getBillAllFields(Module module)throws Exception{

        return billMapper.getBillByAllFields(module);
    }


    /**
     * 根据实体类型获取对应的所有实体数据
     *
     * @param classId 实体类型
     * @return 返回所有实体数据
     */
    public List<Map<String,Object>> listBills(String classId){

        return billMapper.listBills(classId);
    }

    public Bill getBillDetailsByClassIdAndTypeId(Module module)throws Exception{

        return billMapper.getBillDetail(module);
    }

    /**
     * 判断eeca id是否重复
     *
     * @param id  eeca id
     * @return 返回eeca id是否重复
     */
    public Boolean repetitionCheck(String id){

        return Objects.nonNull(billMapper.repetitionCheck(id));
    }

    /**
     * 根据typeId获取单据的name
     * @param typeIds 实体typeId集合
     * @return 返回单据的name
     */
    public List<TransformPO> listBillNamesByIds(Set<String> typeIds){

        return billMapper.listBillNamesByIds(typeIds);
    }

    public String getTypeIdByModuleId(Long moduleId){
        return billMapper.getTypeIdByModuleId(moduleId);
    }
}
