package com.yjp.erp.service.system;

import com.yjp.erp.model.domain.ActionDO;
import com.yjp.erp.mapper.ApplicationActionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liushui
 * @date 2019/4/11
 */
@Service
public class ApplicationActionService {

    private final ApplicationActionMapper applicationActionMapper;

    @Autowired
    public ApplicationActionService(ApplicationActionMapper applicationActionMapper) {
        this.applicationActionMapper = applicationActionMapper;
    }

    /**
     * @author liushui
     * @description 根据模块id获取单据行为
     * @param  moduleId 模块id
     * @return java.util.List<com.yjp.erp.model.domain.ActionDO>
     */
    public List<ActionDO> getBillActions(Long moduleId){

        return applicationActionMapper.getBillActions(moduleId);
    }
}
