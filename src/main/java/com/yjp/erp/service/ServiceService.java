package com.yjp.erp.service;

import com.yjp.erp.model.domain.ActionDO;
import com.yjp.erp.model.domain.ServiceDO;
import com.yjp.erp.model.domain.ServicePropertyDO;
import com.yjp.erp.model.dto.bill.EntityDetailDTO;
import com.yjp.erp.mapper.*;
import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.po.eeca.BillEeca;
import com.yjp.erp.model.po.service.BillAction;
import com.yjp.erp.model.vo.bill.ServiceAndActionVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ServiceService {

	@Autowired(required = false)
	ServiceMapper serviceMapper;

	@Autowired(required = false)
	ServiceInparaMapper serviceInparaMapper;

	@Autowired(required = false)
	BillActionMapper billActionMapper;

	@Autowired(required = false)
	ActionServiceMapper actionServiceMapper;

	@Autowired(required = false)
	BillEecaMapper billEecaMapper;

	@Autowired(required = false)
	BillEecaRuleMapper billEecaRuleMapper;

	@Autowired(required = false)
	BillEecaRuleFieldMapper billEecaRuleFieldMapper;

	@Autowired(required = false)
	EecaServiceMapper eecaServiceMapper ;

	public void saveService(ServiceDO serviceDO) throws Exception {

		if (CollectionUtils.isNotEmpty(serviceDO.getServices())) {
			serviceMapper.bathInsertService(serviceDO.getServices());
		}
		if (CollectionUtils.isNotEmpty(serviceDO.getServiceInparas())) {
			serviceInparaMapper.bathInsertServiceParam(serviceDO.getServiceInparas());
		}
		if (CollectionUtils.isNotEmpty(serviceDO.getBillActions())) {
			billActionMapper.bathInsertBillAction(serviceDO.getBillActions());
		}
		if (CollectionUtils.isNotEmpty(serviceDO.getActionServices())) {
			actionServiceMapper.bathInsertActionService(serviceDO.getActionServices());
		}
		if (CollectionUtils.isNotEmpty(serviceDO.getBillEecas())) {
			billEecaMapper.bathInsertBillEeca(serviceDO.getBillEecas());
		}
		if(CollectionUtils.isNotEmpty(serviceDO.getBillEecaRules())){
			billEecaRuleMapper.bathInsertBillEecaRule(serviceDO.getBillEecaRules());
		}
		if(CollectionUtils.isNotEmpty(serviceDO.getBillEecaRuleFields())){
			billEecaRuleFieldMapper.bathInsertBillEecaRuleField(serviceDO.getBillEecaRuleFields());
		}
		if(CollectionUtils.isNotEmpty(serviceDO.getEecaServices())){
			eecaServiceMapper.bathInsertEecaService(serviceDO.getEecaServices());
		}
	}

	public List<BillAction> getBillActionList(Module module){
		List<BillAction> result=billActionMapper.getBillActionList(module);
		return result;
	}

	public List<BillEeca> getBillEecaList(Module module){

		return billEecaMapper.getBillEecaList(module);
	}


	/**
	 * 获取script模板
	 * @return 返回服务的script模板
	 */
	public String getScriptDemo(){

		return serviceMapper.getScriptDemo();
	}


	/**
	 * 获取service和action默认模板
	 * @return 返回service和action默认模板
	 */
	public ServiceAndActionVO getCommonServiceAndAction(){

		ServiceAndActionVO vo = new ServiceAndActionVO();
		List<ActionDO> actions = serviceMapper.listActions();
		List<ServicePropertyDO> services = serviceMapper.getCommonServices();
		vo.setActions(actions);
		vo.setServices(services);
		return vo;
	}


	/**
	 * 根据实体classId和typeId获取所属服务
	 * @param dto 实体classId和typeId
	 * @return 返回实体所属服务
	 */
	public List<Map<String,Object>> listServiceByTypeIdAndClassId(EntityDetailDTO dto){

		List<Map<String,Object>> retList = new ArrayList<>();
		Module module = new Module();
		module.setTypeId(dto.getTypeId());
		module.setClassId(dto.getClassId());
		retList.addAll(serviceMapper.listActionService(module));
		retList.addAll(serviceMapper.listEecaService(module));

		return retList;
	}
}
