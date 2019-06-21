package com.yjp.erp.mapper;

import com.yjp.erp.model.domain.fieldrule.OptionRules;

import java.util.List;

/**
 * @Description:
 * @CreateDate: 2019/4/10 15:29
 * @EMAIL: jianghongping@yijiupi.com
 */
public interface OptionRulesMapper {
    /**
     * 更具moduleId获取到 下推或回写的映射关系
     * @param moduleId
     * @return
     */
    List<OptionRules> getDownMappingMapper(Long moduleId);

    /**
     * 批量插入 下推或回写的映射关系
     * @param optionRules
     */
    void bathInsertDownMapping(List<OptionRules> optionRules);
}