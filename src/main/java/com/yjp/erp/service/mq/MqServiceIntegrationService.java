package com.yjp.erp.service.mq;


import com.yjp.erp.model.dto.mq.MqConfigDTO;
import com.yjp.erp.model.dto.mq.MqServiceIntegrationDTO;
import com.yjp.erp.model.mq.MqServiceIntegration;
import com.yjp.erp.model.vo.mq.MqServiceIntegrationVO;

import java.util.List;
import java.util.Map;

/**
 * @author xialei
 * @date  2019-06-04
 */
public interface MqServiceIntegrationService {

    MqServiceIntegrationVO get(String id);

    List<MqServiceIntegration> findList(MqServiceIntegration mqServiceIntegration);

    Map<String, Object> findAllList(MqConfigDTO dto);

    void insert(MqServiceIntegrationDTO dto);

    void insertBatch(List<MqServiceIntegration> mqServiceIntegrations);

    void update(MqServiceIntegrationDTO dto);

    void delete(String id);

    void bind(String id);

    void unbind(String id);

}
