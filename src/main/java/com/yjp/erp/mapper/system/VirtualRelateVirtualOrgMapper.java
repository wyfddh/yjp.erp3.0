package com.yjp.erp.mapper.system;

import com.yjp.erp.model.po.system.VorgRelateVorg;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author wyf
 * @date 2019/6/4 上午 11:30
 **/
@Repository
public interface VirtualRelateVirtualOrgMapper {

    void deleteByVOrgId(Long vId);

    void batchInsert(List<VorgRelateVorg> list);

    List<VorgRelateVorg> getRelateByVId(Long vId);
}
