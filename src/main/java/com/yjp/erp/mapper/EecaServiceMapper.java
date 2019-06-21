package com.yjp.erp.mapper;

import com.yjp.erp.model.po.eeca.EecaService;

import java.util.List;

public interface EecaServiceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EecaService record);

    int insertSelective(EecaService record);

    EecaService selectByPrimaryKey(Integer id);

    List<EecaService> getByEecaId(Long eecaId);

    int updateByPrimaryKeySelective(EecaService record);

    int updateByPrimaryKey(EecaService record);

    void bathInsertEecaService(List<EecaService> eecaServices);
}