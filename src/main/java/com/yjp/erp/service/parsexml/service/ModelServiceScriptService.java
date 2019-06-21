package com.yjp.erp.service.parsexml.service;

import com.yjp.erp.model.domain.ModelServiceScript;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ModelServiceScriptService {
    List<ModelServiceScript> getAllModelServiceScript();
}
