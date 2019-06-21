package com.yjp.erp.mapper.system;

import com.yjp.erp.model.po.system.Organization;
import com.yjp.erp.model.vo.system.OrgVO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author wyf
 * @date 2019/4/22 下午 7:40
 **/
@Repository
public interface OrgMapper {

    /**
     * 获取虚拟组织的总数
     * @author wyf
     * @date  2019/4/23 上午 9:19
     * @param map 参数
     * @return java.lang.Integer
     */
    Integer countVirtualOrgByMap(Map<String, Object> map);
    
    /**
     * 查询列表
     * @author wyf
     * @date  2019/4/23 上午 9:19
     * @param map 参数
     * @return java.util.List<com.yjp.erp.model.vo.system.OrgVO>
     */
    List<OrgVO> listVirtualOrgByMap(Map<String, Object> map);

    /**
     * 根据名字和类型查询组织
     * @author wyf
     * @date  2019/4/23 上午 9:20
     * @param name 组织名
     * @param orgType 组织类型
     * @return com.yjp.erp.model.po.system.Organization
     */
    Organization getOrgByName(@Param("name") String name,@Param("orgType") String orgType);

    /**
     * 插入组织
     * @author wyf
     * @date  2019/4/23 上午 10:13
     * @param org 组织实体
     */
    void insert(Organization org);

    /**
     * 更新组织
     * @author wyf
     * @date  2019/4/23 下午 1:40
     * @param org 组织实体
     */
    void update(Organization org);

    /**
     * 根据id集合查询
     * @author wyf
     * @date  2019/4/23 下午 3:05
     * @param idList id集合
     * @return java.util.List<com.yjp.erp.model.po.system.Organization>
     */
    List<Organization> listOrgByVids(List<Long> idList);

    /**
     * 批量更新组织的节点信息
     * @author wyf
     * @date  2019/4/25 上午 10:52
     * @param list 组织集合
     */
    void batchUpdateOrgNode(List<OrgVO> list);

    /**
     * 根据id获取组织
     * @author wyf
     * @date  2019/4/25 下午 1:43
     * @param id id
     * @return com.yjp.erp.model.po.system.Organization
     */
    Organization getOrgById(Long id);
}
