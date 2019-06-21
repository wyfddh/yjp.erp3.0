package com.yjp.erp.mapper.mq;

import com.yjp.erp.model.mq.MqConversionRelation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author xialei
 * Date  2019-06-04
 */
@Repository
public interface MqConversionRelationMapper {

    MqConversionRelation get(String id);

    List<MqConversionRelation> findList(MqConversionRelation mqConversionRelation);

    List<MqConversionRelation> findAllList();

    int insert(MqConversionRelation mqConversionRelation);

    int insertBatch(List<MqConversionRelation> mqConversionRelations);

    int update(MqConversionRelation mqConversionRelation);

    int bathUpdate(List<MqConversionRelation> mqConversionRelations);

    int delete(String id);

}