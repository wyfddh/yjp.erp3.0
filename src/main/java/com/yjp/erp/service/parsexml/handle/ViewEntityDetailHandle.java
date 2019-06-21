package com.yjp.erp.service.parsexml.handle;

import com.yjp.erp.constants.EntityClassEnum;
import com.yjp.erp.constants.ModuleConstant;
import com.yjp.erp.model.dto.bill.EntityDetailDTO;
import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.po.view.ViewAlias;
import com.yjp.erp.model.po.view.ViewEntity;
import com.yjp.erp.model.po.view.ViewMemberEntity;
import com.yjp.erp.model.vo.view.ViewEntityVO;
import com.yjp.erp.service.BaseEntityService;
import com.yjp.erp.service.BillService;
import com.yjp.erp.service.EnumService;
import com.yjp.erp.service.ModuleService;
import com.yjp.erp.service.parsexml.model.EntityParseMember;
import com.yjp.erp.service.view.ViewAliasesService;
import com.yjp.erp.service.view.ViewEntityService;
import com.yjp.erp.service.view.ViewMemberEntityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ViewEntityDetailHandle {
    @Resource
    private ModuleService moduleService;
    @Resource
    private ViewMemberEntityService viewMemberEntityService;
    @Resource
    private ViewEntityService viewEntityService;
    @Resource
    private ViewAliasesService viewAliasesService;
    @Resource
    private BillService billService;
    @Resource
    private BaseEntityService baseEntityService;
    @Resource
    private EnumService enumService;


    public ViewEntityVO handleViewEntityDetail(EntityDetailDTO dto) {
        ViewEntityVO viewEntityVO = new ViewEntityVO();
        Module simpleModule = new Module();
        simpleModule.setClassId(dto.getClassId());
        simpleModule.setTypeId(dto.getTypeId());

        Module module = moduleService.getModuleByClassIdAndTypeId(simpleModule);
        viewEntityVO.setTypeId(module.getTypeId());
        viewEntityVO.setClassId(module.getClassId());

        ViewEntity viewEntity = addViewEntityVoFields(viewEntityVO, module);
        addViewVoMembers(viewEntityVO, viewEntity);
        return viewEntityVO;
    }

    private void addViewVoMembers(ViewEntityVO viewEntityVO, ViewEntity viewEntity) {
        List<ViewMemberEntity> viewMemberEntities = viewMemberEntityService.getViewMemberEntitiesByViewEntityId(viewEntity.getId());

        List<ViewEntityVO.MemberVO> fields = new ArrayList<>();
        viewMemberEntities.forEach(viewMemberEntity -> {
            ViewEntityVO.MemberVO memberVO = viewEntityVO.new MemberVO();
            memberVO.setEntityAlias(viewMemberEntity.getEntityAlias());
            memberVO.setEntityName(viewMemberEntity.getEntityName());
            memberVO.setJoinFromAlias(viewMemberEntity.getJoinFromAlias());
            memberVO.setEntityParentValue(viewMemberEntity.getFieldName());
            memberVO.setEntityValue(viewMemberEntity.getRelated());

            List<ViewAlias> viewAlias = viewAliasesService.getViewAliasByViewMemberId(viewMemberEntity.getId());
            List<ViewEntityVO.MemberVO.ViewField> viewFields = new ArrayList<>();

            viewAlias.forEach(viewAlia -> {
                ViewEntityVO.MemberVO.ViewField viewField = memberVO.new ViewField();
                viewField.setName(viewAlia.getName());
                viewField.setField(viewAlia.getField());
                viewField.setExpression(viewAlia.getExpression());
                viewField.setDescription(viewAlia.getDescription());
                viewFields.add(viewField);
            });

            memberVO.setViewField(viewFields);
            fields.add(memberVO);
        });
        viewEntityVO.setFields(fields);
    }

    private ViewEntity addViewEntityVoFields(ViewEntityVO viewEntityVO, Module module) {
        ViewEntity viewEntity = viewEntityService.getViewEntityByModule(module.getId());
        viewEntityVO.setAuthorizeSkip(viewEntity.getAuthorizeSkip());
        viewEntityVO.setAutoClearCache(viewEntity.getAutoClearCache());
        viewEntityVO.setTitle(viewEntity.getLabel());
        viewEntityVO.setName(viewEntity.getEntityName());
        viewEntityVO.setCache(viewEntity.getCache());
        viewEntityVO.setPackageName(viewEntity.getEntityPackage());
        return viewEntity;
    }

    /**
     * 通过classI 和 typeId 获取字段
     *
     * @param classId
     * @param typeId
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getAllClassIdEntityFields(String classId, String typeId) throws Exception {
        Module simpleModule = new Module();
        simpleModule.setClassId(classId);
        simpleModule.setTypeId(typeId);
        Module module = moduleService.getModuleByClassIdAndTypeId(simpleModule);
        List<Map<String, Object>> maps;
        if (Objects.equals(EntityClassEnum.BILL_CLASS.getValue(), classId)) {
            maps = billService.getBillFields(module);
            return maps;
        }
        if (Objects.equals(EntityClassEnum.ENTITY_CLASS.getValue(), classId)) {
            maps = baseEntityService.listFields(module);
            return maps;
        }
        if (Objects.equals(EntityClassEnum.ENUM_CLASS.getValue(), classId)) {
            maps = enumService.listFields(module);
            return maps;
        }
        return null;
    }
}
