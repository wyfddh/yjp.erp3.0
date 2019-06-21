package com.yjp.erp.handle;

import com.alibaba.fastjson.JSON;
import com.yjp.erp.conf.UserInfoManager;
import com.yjp.erp.constants.EntityClassEnum;
import com.yjp.erp.model.domain.ActionDO;
import com.yjp.erp.model.domain.BillFieldsRulesDO;
import com.yjp.erp.model.dto.bill.EntityDetailDTO;
import com.yjp.erp.model.po.bill.Bill;
import com.yjp.erp.model.po.bill.BillField;
import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.po.system.Role;
import com.yjp.erp.model.vo.bill.BillElementsVO;
import com.yjp.erp.service.BaseEntityService;
import com.yjp.erp.service.BillService;
import com.yjp.erp.service.ModuleService;
import com.yjp.erp.service.system.ApplicationActionService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * description:
 *
 * @author liushui
 * @date 2019/4/11
 */
@Component
public class BillApplicationHandel extends BaseFieldHandel {

    private final ModuleService moduleService;

    private final BillService billService;

    private final BaseEntityService baseEntityService;

    private final ApplicationActionService applicationActionService;

    private final UserInfoManager userInfoManager;

    @Autowired
    public BillApplicationHandel(ModuleService moduleService, BillService billService, BaseEntityService baseEntityService, ApplicationActionService applicationActionService, UserInfoManager userInfoManager) {
        this.moduleService = moduleService;
        this.billService = billService;
        this.baseEntityService = baseEntityService;
        this.applicationActionService = applicationActionService;
        this.userInfoManager = userInfoManager;
    }

    /**
     * 组装实体的数据
     *
     * @param dto 实体明细查询的dto对象
     * @return
     */
    public BillElementsVO handelElements(EntityDetailDTO dto) {

        BillElementsVO vo = new BillElementsVO();
        List<BillField> moquiProperties = null;
        List<BillField> webProperties = null;
        Bill bill = new Bill();

        Module param = new Module();
        param.setTypeId(dto.getTypeId());
        param.setClassId(dto.getClassId());
        Module module = moduleService.getModuleByClassIdAndTypeId(param);

        //如果为表单
        if (Objects.equals(EntityClassEnum.BILL_CLASS.getValue(), dto.getClassId())) {
            bill = billService.getBillByModuleId(module.getId());
            moquiProperties = billService.getBillMoquiProperties(bill.getId());
            webProperties = billService.getBillWebProperties(bill.getId());
        }
        //如果为实体
        if (Objects.equals(EntityClassEnum.ENTITY_CLASS.getValue(), dto.getClassId())) {
            bill = baseEntityService.getBillByModuleId(module.getId());
            moquiProperties = baseEntityService.getBillMoquiProperties(bill.getId());
            webProperties = baseEntityService.getBillWebProperties(bill.getId());
        }
        //读取子表数据
        handelChildrenData(vo, bill.getId(), dto.getClassId());
        BillFieldsRulesDO billFieldsRulesDO = fieldDbDataToVoData(moquiProperties, webProperties);

        vo.setTitle(bill.getLabel());
        vo.setRules(billFieldsRulesDO.getRules());
        vo.setClassId(dto.getClassId());
        vo.setFields(billFieldsRulesDO.getFields());
        vo.setTypeId(dto.getTypeId());

        handelActionData(vo, module.getId());
        return vo;
    }

    /**
     * 获取子表信息
     */
    private void handelChildrenData(BillElementsVO vo, Long billId, String classId) {
        //如果为表单
        List<Bill> childrenBill = new ArrayList<>();
        if (Objects.equals(EntityClassEnum.BILL_CLASS.getValue(), classId)) {
            childrenBill = billService.getChildrenBillByParentId(billId);
        }
        if (Objects.equals(EntityClassEnum.ENTITY_CLASS.getValue(), classId)) {
            childrenBill = baseEntityService.getChildrenBillByParentId(billId);
        }
        List<BillFieldsRulesDO> children = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(childrenBill)) {
            childrenBill.forEach(child -> {
                List<BillField> childMoqui = new ArrayList<>();
                List<BillField> childWeb = new ArrayList<>();
                if (Objects.equals(EntityClassEnum.BILL_CLASS.getValue(), classId)) {
                    childMoqui = billService.getBillMoquiProperties(child.getId());
                    childWeb = billService.getBillWebProperties(child.getId());
                }
                if (Objects.equals(EntityClassEnum.ENTITY_CLASS.getValue(), classId)) {
                    childMoqui = baseEntityService.getBillMoquiProperties(child.getId());
                    childWeb = baseEntityService.getBillWebProperties(child.getId());
                }
                BillFieldsRulesDO childProperties = fieldDbDataToVoData(childMoqui, childWeb);
                childProperties.setPrimaryKey(child.getPrimaryKey());
                childProperties.setForeignKey(child.getForeignKey());
                childProperties.setTitle(child.getLabel());
                children.add(childProperties);
            });
        }
        vo.setChildren(children);
    }

    private void handelActionData(BillElementsVO vo, Long moduleId) {
        List<Role> roleList = userInfoManager.getUserInfo().getRoleList();
        List<Long> roleIds = roleList.parallelStream().map(Role::getId).collect(Collectors.toList());

        List<ActionDO> actionList = applicationActionService.getBillActions(moduleId);
            if (Objects.nonNull(actionList)) {
                Iterator<ActionDO> iterator = actionList.iterator();
                while (iterator.hasNext()) {
                    ActionDO actionDO = iterator.next();
                    String roleAuthorization = actionDO.getRoleAuthorization();
                    if (Objects.nonNull(roleAuthorization)) {
                        List<Long> ids = JSON.parseArray(roleAuthorization, Long.class);
                        if (Collections.disjoint(roleIds, ids)) {
                            iterator.remove();
                        }
                    }
                }
            }
        vo.setActions(actionList);
    }
}
