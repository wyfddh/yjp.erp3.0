package com.yjp.erp.mapper.parsexml;

import com.yjp.erp.model.po.service.Service;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceMapperP {
    List<Service> listServiceByModuleId(Long moduleId);
}
