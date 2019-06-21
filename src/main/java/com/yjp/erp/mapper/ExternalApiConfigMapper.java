package com.yjp.erp.mapper;

import com.yjp.erp.model.dto.externalapi.ExternalApiConfigDTO;
import com.yjp.erp.model.externalapi.ExternalApiConfig;
import com.yjp.erp.model.vo.externalapi.ExternalApiConfigVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author xialei
 * @date 2019-06-17
 */
@Mapper
public interface ExternalApiConfigMapper {

    ExternalApiConfigVO get(String id);

    List<Map<String, Object>> findAllList(Map<String, Object> map);

    int insert(ExternalApiConfig externalApiConfig);

    int update(ExternalApiConfig config);

    int delete(ExternalApiConfig externalApiConfig);

    int findAllListCount(Map<String, Object> map);

    int updateStatus(@Param("id") String id, @Param("status") String status);

    List<ExternalApiConfig> findByParams(Map<String, Object> map);

}