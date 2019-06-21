package com.yjp.erp.service.externalapi;


import com.yjp.erp.model.dto.externalapi.ExternalApiConfigDTO;
import com.yjp.erp.model.externalapi.ExternalApiConfig;
import com.yjp.erp.model.vo.externalapi.ExternalApiConfigVO;

import java.util.List;
import java.util.Map;

/**
 * @author xialei
 * @date 2019-06-17
 */
public interface ExternalApiConfigService {

    ExternalApiConfigVO get(String id);

    Map<String, Object> findAllList(ExternalApiConfigDTO dto);

    int insert(ExternalApiConfigDTO dto);


    int update(ExternalApiConfigDTO dto);

    List<ExternalApiConfig> findByParams(ExternalApiConfig config);

    void delete(String id);

    void enable(String id);

    void stop(String id);

}
