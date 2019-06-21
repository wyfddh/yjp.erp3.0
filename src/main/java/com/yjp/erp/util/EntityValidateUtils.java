package com.yjp.erp.util;

import com.yjp.erp.conf.exception.BusinessException;
import com.yjp.erp.model.dto.bill.EntityAndServiceDTO;
import com.yjp.erp.model.dto.bill.ViewEntityAndServiceDTO;
import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.vo.RetCode;
import com.yjp.erp.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class EntityValidateUtils {
    private static ModuleService moduleService;

    @Autowired
    public void setModuleService(ModuleService moduleService) {
        EntityValidateUtils.moduleService = moduleService;
    }

    public static void validate(EntityAndServiceDTO dto) throws Exception {

        if (dto.getType() == 1) {
            Module param = new Module();
            param.setClassId(dto.getClassId());
            param.setTypeId(dto.getTypeId());
            Module temp = moduleService.getModuleByClassIdAndTypeId(param);
            if (Objects.nonNull(temp)) {
                throw new BusinessException(RetCode.DUPLICATE_NAME, "类型重复");
            }
        }
    }

    public static void validateViewEntity(ViewEntityAndServiceDTO dto) throws Exception {

        if (dto.getType() == 1) {
            Module param = new Module();
            param.setClassId(dto.getClassId());
            param.setTypeId(dto.getTypeId());
            Module temp = moduleService.getModuleByClassIdAndTypeId(param);
            if (Objects.nonNull(temp)) {
                throw new BusinessException(RetCode.DUPLICATE_NAME, "类型重复");
            }
        }
    }
}
