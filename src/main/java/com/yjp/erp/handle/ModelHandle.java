package com.yjp.erp.handle;

import com.yjp.erp.model.domain.BillFieldsRulesDO;
import com.yjp.erp.model.domain.ServicePropertyDO;
import com.yjp.erp.model.po.bill.BillField;
import com.yjp.erp.service.ModelService;
import com.yjp.erp.model.vo.bill.ModelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author liushui
 * @description:
 * @date 2019/3/27
 */
@Component
public class ModelHandle extends BaseFieldHandel{

    @Autowired
    private ModelService modelService;

    public ModelVO getModelFields(){

        List<BillField> childMoqui = modelService.getModelMoquiProperties();
        List<BillField> childWeb = modelService.getModelWebProperties();

        BillFieldsRulesDO billFieldsRulesDO = fieldDbDataToVoData(childMoqui,childWeb);
        List<ServicePropertyDO> servicePropertyDOS = modelService.getGlobalServices();

        ModelVO modelVO = new ModelVO();
        modelVO.setFields(billFieldsRulesDO.getFields());
        modelVO.setRules(billFieldsRulesDO.getRules());
        modelVO.setServices(servicePropertyDOS);

        return modelVO;
    }
}
