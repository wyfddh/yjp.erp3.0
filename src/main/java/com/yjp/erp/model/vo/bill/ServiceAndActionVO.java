package com.yjp.erp.model.vo.bill;

import com.yjp.erp.model.domain.ActionDO;
import com.yjp.erp.model.domain.ServicePropertyDO;
import lombok.Data;

import java.util.List;

/**
 * description:
 * @author liushui
 * @date 2019/4/9
 */
@Data
public class ServiceAndActionVO {

    private List<ActionDO> actions;

    private List<ServicePropertyDO> services;
}
