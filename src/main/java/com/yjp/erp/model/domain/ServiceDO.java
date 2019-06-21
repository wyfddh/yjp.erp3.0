package com.yjp.erp.model.domain;

import com.yjp.erp.model.po.eeca.BillEeca;
import com.yjp.erp.model.po.eeca.EecaService;
import com.yjp.erp.model.po.service.*;
import com.yjp.erp.model.po.eeca.BillEecaRule;
import com.yjp.erp.model.po.eeca.BillEecaRuleField;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liushui
 * @description:
 * @date 2019/3/23
 */
@Data
public class ServiceDO {

    private List<ServiceInpara> serviceInparas = new ArrayList<>();

    private List<Service> services = new ArrayList<>();

    private List<BillAction> billActions = new ArrayList<>();

    private List<ActionService> actionServices = new ArrayList<>();

    private List<BillEeca> billEecas = new ArrayList<>();

    private List<BillEecaRule> billEecaRules=new ArrayList<>();

    private List<BillEecaRuleField> billEecaRuleFields=new ArrayList<>();

    private List<EecaService> eecaServices=new ArrayList<>();
}
