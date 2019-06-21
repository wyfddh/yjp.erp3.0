package com.yjp.erp.service.externalapi.impl;


import com.yjp.erp.mapper.ExternalApiConfigMapper;
import com.yjp.erp.model.domain.ActionDO;
import com.yjp.erp.model.dto.externalapi.ExternalApiConfigDTO;
import com.yjp.erp.model.externalapi.ExternalApiConfig;
import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.vo.externalapi.ExternalApiConfigVO;
import com.yjp.erp.service.ModuleService;
import com.yjp.erp.service.externalapi.ExternalApiConfigService;
import com.yjp.erp.service.system.ApplicationActionService;
import com.yjp.erp.util.SnowflakeIdWorker;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author xialei
 * @date 2019-06-17
 */
@Service
public class ExternalApiConfigServiceImpl implements ExternalApiConfigService {
    @Resource
    private ExternalApiConfigMapper externalApiConfigMapper;


    @Resource
    private ModuleService moduleService;

    @Resource
    private ApplicationActionService applicationActionService;

    @Override
    public ExternalApiConfigVO get(String id) {
        return externalApiConfigMapper.get(id);
    }


    @Override
    public Map<String, Object> findAllList(ExternalApiConfigDTO dto) {
        Map<String, Object> param = new HashMap<>(8);
        param.put("status", dto.getStatus());
        param.put("name", dto.getName());
        List<Map<String, Object>> list = new ArrayList<>();
        int count = externalApiConfigMapper.findAllListCount(param);
        if (count > 0) {
            param.put("pageSize", dto.getPageSize());
            param.put("startIndex", (dto.getPageNo() - 1) * dto.getPageSize());
            list = externalApiConfigMapper.findAllList(param);
        }
        Map<String, Object> retMap = new HashMap<>(8);
        retMap.put("pageNo", dto.getPageNo());
        retMap.put("pageSize", dto.getPageSize());
        retMap.put("total", count);
        retMap.put("dataList", list);
        return retMap;
    }


    @Override
    public List<ExternalApiConfig> findByParams(ExternalApiConfig config) {
        Map<String, Object> param = new HashMap<>(8);
        param.put("status", config.getStatus());
        param.put("classId", config.getClassId());
        param.put("typeId", config.getTypeId());
        param.put("operateType", config.getOperateType());
        param.put("systemType", config.getSystemType());

        List<ExternalApiConfig> list = new ArrayList<>();
        int count = externalApiConfigMapper.findAllListCount(param);
        if (count > 0) {
            list = externalApiConfigMapper.findByParams(param);
        }
        return list;
    }

    @Override
    public int insert(ExternalApiConfigDTO dto) {
        ExternalApiConfig config = new ExternalApiConfig();
        BeanUtils.copyProperties(dto, config);
        config.setStatus(new Byte(dto.getStatus()));
        long id = SnowflakeIdWorker.nextId();
        config.setId(id);
        getActionUrl(config);
        return externalApiConfigMapper.insert(config);
    }


    @Override
    public int update(ExternalApiConfigDTO dto) {
        ExternalApiConfig config = new ExternalApiConfig();
        BeanUtils.copyProperties(dto, config);
        config.setStatus(new Byte(dto.getStatus()));
        config.setId(new Long(dto.getId()));
        getActionUrl(config);
        return externalApiConfigMapper.update(config);
    }

    @Override
    public void delete(String id) {
        String status = "-1";
        externalApiConfigMapper.updateStatus(id, status);
    }

    @Override
    public void enable(String id) {
        String status = "1";
        externalApiConfigMapper.updateStatus(id, status);
    }

    @Override
    public void stop(String id) {
        String status = "0";
        externalApiConfigMapper.updateStatus(id, status);
    }


    private void getActionUrl(ExternalApiConfig config) {
        Module param = new Module();
        param.setTypeId(config.getTypeId());
        param.setClassId(config.getClassId());
        Module module = moduleService.getModuleByClassIdAndTypeId(param);

        List<ActionDO> actionList = applicationActionService.getBillActions(module.getId());
        actionList.forEach(action -> {
            if (Objects.equals(config.getOperateType(), action.getActionName())) {
                config.setUrl(action.getRpcUrl());
                config.setMethod(action.getRpcMethod());
            }
        });
    }

}
