package com.yjp.erp.service;

import com.alibaba.fastjson.JSONObject;
import com.yjp.erp.model.domain.fieldrule.OptionRules;
import com.yjp.erp.mapper.BillOptionRulesMapper;
import com.yjp.erp.mapper.ModuleMapper;
import com.yjp.erp.model.po.bill.BillOptionRules;
import com.yjp.erp.model.po.bill.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 单据下推/回写服务层
 * @Author: 江洪平
 * @CreateDate: 2019/4/16 13:54
 * @Email: jianghongping@yijiupi.com
 */
@Service
public class PushService {

    @Autowired
    BillOptionRulesMapper billOptionRulesMapper;

    @Autowired
    ModuleMapper moduleMapper;

    /**
     * 根据实体的moduleId获取到实体的所有的下推/回写的规则数据
     *
     * @return
     */
    public List<OptionRules> listOptionRules(Module module) {
        List<OptionRules> listOptionRules = new ArrayList<>();
        Module moduleResult=moduleMapper.getModuleByClassIdAndTypeId(module);
        List<BillOptionRules> optionRulesByModules = billOptionRulesMapper.getOptionRulesByModule(moduleResult.getId());
        optionRulesByModules.forEach(optionRulesByModule -> {
            OptionRules optionRules = JSONObject.parseObject(optionRulesByModule.getOptionRules(), OptionRules.class);
            listOptionRules.add(optionRules);
        });
        return listOptionRules;
    }


}

