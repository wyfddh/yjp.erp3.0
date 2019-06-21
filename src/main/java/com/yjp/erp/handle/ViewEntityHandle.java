package com.yjp.erp.handle;

import com.yjp.erp.conf.UserInfoManager;
import com.yjp.erp.constants.ModuleConstant;
import com.yjp.erp.model.domain.ViewEntityMemberDO;
import com.yjp.erp.model.dto.bill.EntityPageDTO;
import com.yjp.erp.model.dto.bill.ViewEntityAndServiceDTO;
import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.po.view.ViewAlias;
import com.yjp.erp.model.po.view.ViewEntity;
import com.yjp.erp.model.po.view.ViewMemberEntity;
import com.yjp.erp.model.vo.view.ViewEntityVO;
import com.yjp.erp.service.ModuleService;
import com.yjp.erp.service.parsexml.model.ViewEntityFilter;
import com.yjp.erp.service.parsexml.model.vo.PageViewEntityVO;
import com.yjp.erp.service.parsexml.model.vo.ViewEntityListVO;
import com.yjp.erp.service.view.ViewAliasesService;
import com.yjp.erp.service.view.ViewEntityService;
import com.yjp.erp.service.view.ViewMemberEntityService;
import com.yjp.erp.util.EntityValidateUtils;
import com.yjp.erp.util.SnowflakeIdWorker;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/30 12:02
 * @Email: jianghongping@yijiupi.com
 */
@Component
public class ViewEntityHandle {
    @Resource
    private ModuleService moduleService;
    @Resource
    private ViewAliasesService viewAliasesService;
    @Resource
    private ViewEntityService viewEntityService;
    @Resource
    private ViewMemberEntityService viewMemberEntityService;
    @Resource
    private UserInfoManager userInfoManager;


    public void handleViewAndService(ViewEntityAndServiceDTO dto) throws Exception {
        EntityValidateUtils.validateViewEntity(dto);
        //todo 先写入module 获取到moduleId
        ViewEntityMemberDO viewEntityMemberDO = getViewEntityMemberDO(dto);

        saveViewEntityMemberDO(viewEntityMemberDO);
    }


    public PageViewEntityVO getAllActiveViews(EntityPageDTO dto) {
        ViewEntityFilter viewEntityFilter = new ViewEntityFilter();
        if (com.yjp.erp.util.ObjectUtils.isNotEmpty(dto.getTitle())) {
            viewEntityFilter.setLabel(dto.getTitle());
        }
        if (com.yjp.erp.util.ObjectUtils.isNotEmpty(dto.getStatus())) {
            viewEntityFilter.setPublishStatus(dto.getStatus());
        }
        viewEntityFilter.setClassId(dto.getClassId());

        Integer total = viewEntityService.countViewEntity(viewEntityFilter);
        List<ViewEntityListVO> viewEntitis = viewEntityService.getViewEntityByFilter(viewEntityFilter);

        PageViewEntityVO pageViewEntityVO = new PageViewEntityVO();
        pageViewEntityVO.setTotal(total);
        pageViewEntityVO.setPageNo(dto.getPageNo());
        pageViewEntityVO.setPageSize(dto.getPageSize());
        pageViewEntityVO.setDataList(viewEntitis);

        return pageViewEntityVO;
    }

    /**
     * 从dto对象中取出视图实体的实体成员
     *
     * @param dto
     * @return
     */
    private ViewEntityMemberDO getViewEntityMemberDO(ViewEntityAndServiceDTO dto) {
        Long moduleId = SnowflakeIdWorker.nextId();
        Module module = new Module();
        module.setId(moduleId);
        module.setTypeId(dto.getTypeId());
        module.setClassId(dto.getClassId());
        if (dto.getType() == 1) {
            module.setPublishState(0);
        }

        ViewEntity viewEntity = generateViewEntity(dto, moduleId);
        List<ViewMemberEntity> viewMemberEntities = new ArrayList<>();
        List<ViewAlias> viewAliases = new ArrayList<>();
        generateViewMemberEntitiesAndViewAlias(dto, viewEntity.getId(), viewMemberEntities, viewAliases);

        ViewEntityMemberDO viewEntityMemberDO = new ViewEntityMemberDO();
        viewEntityMemberDO.setModule(module);
        viewEntityMemberDO.setViewAliases(viewAliases);
        viewEntityMemberDO.setViewEntity(viewEntity);
        viewEntityMemberDO.setViewMemberEntities(viewMemberEntities);
        return viewEntityMemberDO;
    }

    void saveViewEntityMemberDO(ViewEntityMemberDO viewEntityMemberDO) {
        dealKeepNewActiveModule(viewEntityMemberDO);

        moduleService.insertModule(viewEntityMemberDO.getModule());
        viewEntityService.saveViewEntity(viewEntityMemberDO.getViewEntity());

        List<ViewAlias> viewAliases = viewEntityMemberDO.getViewAliases();
        viewAliasesService.saveViewAliases(viewAliases);

        List<ViewMemberEntity> viewMemberEntities = viewEntityMemberDO.getViewMemberEntities();
        viewMemberEntityService.saveViewMemberEntities(viewMemberEntities);
    }

    /**
     * 通过classId 和typeId 查询出active 状态的module 如果存在就设置为un_active。
     *
     * @param viewEntityMemberDO
     */
    private void dealKeepNewActiveModule(ViewEntityMemberDO viewEntityMemberDO) {
        Module newModule = viewEntityMemberDO.getModule();
        Module module = new Module();
        module.setTypeId(newModule.getTypeId());
        module.setClassId(newModule.getClassId());
        Module resultModule = moduleService.getModuleByClassIdAndTypeId(module);
        if (null != resultModule) {
            resultModule.setActiveState(ModuleConstant.UNACTIVE);
            moduleService.updateModuleAndActive(resultModule);
        }
    }


    /**
     * 生成ViewMemberEntity
     *
     * @param dto
     * @return
     */
    private void generateViewMemberEntitiesAndViewAlias(ViewEntityAndServiceDTO dto, Long viewEntityId, List<ViewMemberEntity> viewMemberEntities, List<ViewAlias> viewAliases) {
        List<Map<String, Object>> fields = dto.getFields();
        fields.forEach(field -> {
            ViewMemberEntity viewMemberEntity = new ViewMemberEntity();
            Long viewEntityMemberId = SnowflakeIdWorker.nextId();
            viewMemberEntity.setId(viewEntityMemberId);
            //todo 获取视图实体的id
            viewMemberEntity.setViewEntityId(viewEntityId);
            viewMemberEntity.setEntityAlias((String) field.get("entityAlias"));
            viewMemberEntity.setEntityName((String) field.get("entityName"));
            viewMemberEntity.setJoinFromAlias((String) field.get("joinFromAlias"));
            viewMemberEntity.setFieldName((String) field.get("entityParentValue"));
            viewMemberEntity.setRelated((String) field.get("entityValue"));
            viewMemberEntity.setClassId((String) field.get("classId"));
            viewMemberEntity.setTypeId((String) field.get("typeId"));
            viewMemberEntities.add(viewMemberEntity);

            List<Map<String, String>> viewFields = (List<Map<String, String>>) field.get("viewField");
            viewFields.forEach(viewField -> {
                ViewAlias viewAlias = new ViewAlias();
                viewAlias.setId(SnowflakeIdWorker.nextId());
                viewAlias.setViewEntityMemberId(viewEntityMemberId);
                viewAlias.setName(viewField.get("name"));
                viewAlias.setEntityAlias((String) field.get("entityAlias"));
                viewAlias.setField(viewField.get("field"));
                viewAlias.setKeyType(viewField.get("keyType"));
                viewAlias.setExpression(viewField.get("expression"));
                viewAlias.setDescription(viewField.get("description"));
                viewAliases.add(viewAlias);
            });
        });
    }

    private ViewEntity generateViewEntity(ViewEntityAndServiceDTO dto, Long moduleId) {
        ViewEntity viewEntity = new ViewEntity();
        viewEntity.setId(SnowflakeIdWorker.nextId());
        viewEntity.setModuleId(moduleId);
        viewEntity.setLabel(dto.getTitle());
        String company = userInfoManager.getUserInfo().getCompanySpace();
        viewEntity.setEntityName(String.format("%s_%s_%s", company, dto.getClassId(), dto.getTypeId()));
        viewEntity.setEntityPackage(dto.getPackageName());
        viewEntity.setAutoClearCache(dto.getAutoClearCache());
        viewEntity.setAuthorizeSkip(dto.getAuthorizeSkip());
        return viewEntity;
    }

}
