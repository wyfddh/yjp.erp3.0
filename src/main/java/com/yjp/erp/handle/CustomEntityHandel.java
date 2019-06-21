package com.yjp.erp.handle;

import com.yjp.erp.constants.EntityClassEnum;
import com.yjp.erp.model.dto.bill.EntityDetailDTO;
import com.yjp.erp.model.dto.bill.EecaDTO;
import com.yjp.erp.model.dto.bill.IdReplaceDTO;
import com.yjp.erp.model.po.bill.Bill;
import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.po.bill.TransformPO;
import com.yjp.erp.model.po.view.ViewAlias;
import com.yjp.erp.model.po.view.ViewEntity;
import com.yjp.erp.model.po.view.ViewMemberEntity;
import com.yjp.erp.service.*;
import com.yjp.erp.service.view.ViewAliasesService;
import com.yjp.erp.service.view.ViewEntityService;
import com.yjp.erp.service.view.ViewMemberEntityService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * description: 实体
 *
 * @author liushui
 * @date 2019/4/9
 */
@Component
public class CustomEntityHandel {

    @Autowired
    private BillService billService;

    @Autowired
    private BaseEntityService baseEntityService;

    @Autowired
    private ViewService viewService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private EnumService enumService;
    @Resource
    private ViewEntityService viewEntityService;
    @Resource
    private ViewMemberEntityService viewMemberEntityService;
    @Resource
    private ViewAliasesService viewAliasesService;


    /**
     * 根据classId获取所有表单或实体等的下拉列表
     *
     * @param dto 前端表单详情
     * @return 返回所有表单或实体
     */
    public List<Map<String, Object>> listEntities(EntityDetailDTO dto) {

        String classId = dto.getClassId();
        if (Objects.equals(EntityClassEnum.BILL_CLASS.getValue(), classId)) {
            return billService.listBills(classId);
        }
        if (Objects.equals(EntityClassEnum.ENTITY_CLASS.getValue(), classId)) {
            return baseEntityService.listEntities(classId);
        }
        if (Objects.equals(EntityClassEnum.VIEW_CLASS.getValue(), classId)) {
            return viewService.listViews(classId);
        }
        if (Objects.equals(EntityClassEnum.ENUM_CLASS.getValue(), classId)) {
            return enumService.listEnums(classId);
        }
        return null;
    }

    public List<Map<String, Object>> listFields(EntityDetailDTO dto) throws Exception {

        String classId = dto.getClassId();
        Module module = new Module();
        module.setTypeId(dto.getTypeId());
        module.setClassId(dto.getClassId());
        if (Objects.equals(EntityClassEnum.BILL_CLASS.getValue(), classId)) {
            return billService.getBillFields(module);
        }
        if (Objects.equals(EntityClassEnum.ENTITY_CLASS.getValue(), classId)) {
            return baseEntityService.listFields(module);
        }
        if (Objects.equals(EntityClassEnum.ENUM_CLASS.getValue(), classId)) {
            return enumService.listFields(module);
        }
        if (Objects.equals(EntityClassEnum.VIEW_CLASS.getValue(), classId)) {
            List<Map<String, Object>> viewFields = getViewEntityByModule(module);
            return viewFields;
        }
        return null;
    }

    private List<Map<String, Object>> getViewEntityByModule(Module module) {
        List<Map<String, Object>> viewFields = new ArrayList<>();
        Module realModule = moduleService.getModuleByClassIdAndTypeId(module);
        ViewEntity viewEntity = viewEntityService.getViewEntityByModule(realModule.getId());
        List<ViewMemberEntity> viewMemberEntities = viewMemberEntityService.getViewMemberEntitiesByViewEntityId(viewEntity.getId());
        viewMemberEntities.forEach(viewMemberEntity -> {
            List<ViewAlias> viewAlias = viewAliasesService.getViewAliasByViewMemberId(viewMemberEntity.getId());
            viewAlias.forEach(viewAlia -> {
                Map<String, Object> viewField = new HashMap<>();
                viewField.put("fieldName", viewAlia.getName());
                viewField.put("label", viewAlia.getDescription());
                viewField.put("keyType", viewAlia.getKeyType());
                viewFields.add(viewField);
            });
        });
        return viewFields;
    }

    /**
     * 根据classId 以及 typeId 获取主实体以及明细实体的字段
     *
     * @param dto classId 以及 typeId
     * @return 实体以及明细实体的字段
     * @throws Exception http请求异常
     */
    public Map<String, List<Map<String, Object>>> listBillAndChildFields(EntityDetailDTO dto) throws Exception {
        String classId = dto.getClassId();
        Module module = new Module();
        module.setTypeId(dto.getTypeId());
        module.setClassId(dto.getClassId());

        Module resultModule = moduleService.getModuleByClassIdAndTypeId(module);
        Bill resultBill = billService.getBillByModuleId(resultModule.getId());
        if (null == resultBill) {
            return null;
        }
        return getChildAndParentFields(resultBill.getId(), classId, module);
    }


    /**
     * 获取主实体以及明细实体的字段
     *
     * @param mainBillId 单据id
     * @param classId    实体classId
     * @param module     实体信息
     * @return 实体以及明细实体的字段
     * @throws Exception 异常
     */
    private Map<String, List<Map<String, Object>>> getChildAndParentFields(Long mainBillId, String classId, Module module)
            throws Exception {

        Map<String, List<Map<String, Object>>> fieldResult = new HashMap<>(16);
        Bill detailBill = billService.getChildBillByMainId(mainBillId);
        if (null != detailBill) {
            Long detailBillModuleId = billService.getChildBillByMainId(mainBillId).getModuleId();
            Module detailBillModule = moduleService.getMoudleById(detailBillModuleId);

            if (Objects.equals(EntityClassEnum.BILL_CLASS.getValue(), detailBillModule.getClassId())) {
                List<Map<String, Object>> billFields = billService.getBillAllFields(detailBillModule);
                fieldResult.put("child", billFields);
            }
            if (Objects.equals(EntityClassEnum.ENTITY_CLASS.getValue(), detailBillModule.getClassId())) {
                fieldResult.put("child", baseEntityService.listFields(detailBillModule));
            }
        }

        if (Objects.equals(EntityClassEnum.BILL_CLASS.getValue(), classId)) {
            fieldResult.put("parent", billService.getBillFields(module));
        }
        if (Objects.equals(EntityClassEnum.ENTITY_CLASS.getValue(), classId)) {
            fieldResult.put("parent", baseEntityService.listFields(module));
        }
        return fieldResult;
    }

    /**
     * 判断eeca id是否重复
     *
     * @param dto eeca id
     * @return 返回eeca id是否重复
     */
    public Boolean repetitionCheck(EecaDTO dto) {

        return billService.repetitionCheck(dto.getEecaId());
    }

    /**
     * 获取所有实体（包括表单及视图）
     *
     * @return 返回所有实体
     */
    public List<Map<String, Object>> listSelectedEntities() {

        List<Map<String, Object>> retList = new ArrayList<>();
        retList.addAll(billService.listBills(EntityClassEnum.BILL_CLASS.getValue()));
        retList.addAll(baseEntityService.listEntities(EntityClassEnum.ENTITY_CLASS.getValue()));
        retList.addAll(viewService.listViews(EntityClassEnum.VIEW_CLASS.getValue()));

        return retList;
    }

    /**
     * 根据实体classId和typeId获取所属服务
     *
     * @param dto 实体classId和typeId
     * @return 返回实体所属服务
     */
    public List<Map<String, Object>> listServiceByTypeIdAndClassId(EntityDetailDTO dto) {

        return serviceService.listServiceByTypeIdAndClassId(dto);
    }

    /**
     * 发布实体
     *
     * @param module 实体信息
     */
    public void publishEntity(Module module) {

        moduleService.publishEntity(module);
    }

    /**
     * 根据单据实体获取其昵称
     *
     * @param dto 实体classId和typeId
     * @return 返回实体昵称
     */
    public Map<String, String> listBillNamesByIds(Set<IdReplaceDTO> dto) {

        List<TransformPO> bills = new ArrayList<>();
        List<TransformPO> entities = new ArrayList<>();
        Set<String> billIds = dto.stream().filter(a -> Objects.equals(a.getClassId(), EntityClassEnum.BILL_CLASS.getValue())).map(IdReplaceDTO::getTypeId).collect(Collectors.toSet());
        Set<String> entityIds = dto.stream().filter(a -> Objects.equals(a.getClassId(), EntityClassEnum.ENTITY_CLASS.getValue())).map(IdReplaceDTO::getTypeId).collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(billIds)) {
            bills = billService.listBillNamesByIds(billIds);
        }
        if (CollectionUtils.isNotEmpty(entityIds)) {
            entities = baseEntityService.listEntityNamesByIds(entityIds);
        }

        bills.addAll(entities);
        return bills.stream().collect(Collectors.toMap(TransformPO::getTypeId, TransformPO::getLabel, (k1, k2) -> k1));
    }

    /**
     * 根据实体的classId和typeId判断实体表单typeId是否重复
     *
     * @param classId 实体classId
     * @param typeId  实体typeId
     * @return typeId是否重复
     */
    public Boolean typeIdCheck(String classId, String typeId) {

        Module module = new Module();
        module.setClassId(classId);
        module.setTypeId(typeId);
        return moduleService.typeIdCheck(module);
    }
}
