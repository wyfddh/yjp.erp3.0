package com.yjp.erp.service;

import com.yjp.erp.model.domain.ServiceDO;
import com.yjp.erp.mapper.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liushui
 * @description: moqui service eeca等crud操作
 * @date 2019/3/23
 */
@Service
public class MoquiService {

    @Autowired(required = false)
    private ServiceMapper serviceMapper;

    @Autowired(required = false)
    private ServiceInparaMapper serviceInparaMapper;

    @Autowired(required = false)
    private ActionServiceMapper actionServiceMapper;

    @Autowired(required = false)
    private BillActionMapper billActionMapper;

    @Autowired(required = false)
    private BillEecaMapper billEecaMapper;

    public void insertMoquiService(ServiceDO serviceDO){

        if(CollectionUtils.isNotEmpty(serviceDO.getServices())){
            serviceMapper.bathInsertService(serviceDO.getServices());
        }
        if(CollectionUtils.isNotEmpty(serviceDO.getActionServices())){
            actionServiceMapper.bathInsertActionService(serviceDO.getActionServices());
        }
        if(CollectionUtils.isNotEmpty(serviceDO.getBillEecas())){
//            billEecaMapper.bathInsertBillEeca(serviceDO.getBillEecas());
        }
        if(CollectionUtils.isNotEmpty(serviceDO.getServiceInparas())){
            serviceInparaMapper.bathInsertServiceParam(serviceDO.getServiceInparas());
        }
        if(CollectionUtils.isNotEmpty(serviceDO.getBillActions())){
            billActionMapper.bathInsertBillAction(serviceDO.getBillActions());
        }
    }
}
