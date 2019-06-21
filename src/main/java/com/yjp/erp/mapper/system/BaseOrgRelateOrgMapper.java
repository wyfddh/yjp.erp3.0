package com.yjp.erp.mapper.system;

import com.yjp.erp.model.po.system.BaseRelateOrg;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author wyf
 * @date 2019/4/22 下午 7:41
 **/
@Repository
public interface BaseOrgRelateOrgMapper {

    /**
     * 根据虚拟组织id删除其与基本组织的关系
     * @author wyf
     * @date  2019/4/23 上午 10:54
     * @param vId 虚拟组织id
     */
    void deleteByVOrgId(Long vId);

    /**
     * 批量新增
     * @author wyf
     * @date  2019/4/23 上午 11:01
     * @param list 集合
     */
    void batchInsert(List<BaseRelateOrg> list);

    /**
     * 查询该基本组织被引用的数量
     * @author wyf
     * @date  2019/4/23 上午 11:11
     * @param bId 基本组织id
     * @return int
     */
    int countByBaseOrgId(Long bId);

    /**
     * 根据虚拟组织id集合查询
     * @author wyf
     * @date  2019/4/23 下午 2:30
     * @param ids 虚拟组织id集合
     * @return java.util.List<com.yjp.erp.model.po.system.BaseRelateOrg>
     */
    List<BaseRelateOrg> listOrgRelateByVOrgIds(List<Long> ids);

    /**
     * 根据基础组织id查询集合
     * @author wyf
     * @date  2019/5/18 上午 10:06
     * @param  bId 基础组织id
     * @return java.util.List<com.yjp.erp.model.po.system.BaseRelateOrg>
     */
    List<BaseRelateOrg> listVOrgRelateByBOrgId(Long bId);
}
