package com.yjp.erp.mapper.base;

import com.yjp.erp.model.po.service.Service;

import java.util.List;

public interface BaseServiceMapper {
    Service getServiceById(Long id);

    void bathInsertService(List<Service> services);
}
