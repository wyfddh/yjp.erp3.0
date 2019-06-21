package com.yjp.erp.mapper.mq;

import com.yjp.erp.model.mq.MqServiceIntegration;
import com.yjp.erp.model.vo.mq.MqServiceIntegrationVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author xialei
 * @date 2019-06-04
 */
@Repository
public interface MqServiceIntegrationMapper {

    MqServiceIntegrationVO get(String id);

    MqServiceIntegration getByExchangeName(String exchangeName);

    List<MqServiceIntegration> findList(MqServiceIntegration mqServiceIntegration);

    List<Map<String, Object>> findAllList(Map<String, Object> map);

    Map<String,String> getEntityNameByTypeIdAndClassId(Map<String, Object> map);

    int findAllListCount(Map<String, Object> map);

    int insert(MqServiceIntegration mqServiceIntegration);

    int insertBatch(List<MqServiceIntegration> mqServiceIntegrations);

    int update(MqServiceIntegration mqServiceIntegration);

    int delete(String id);

    int updateStatus(@Param("id") String id, @Param("status") String status);

}