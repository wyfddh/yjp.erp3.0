package com.yjp.erp.mapper.system;

import com.yjp.erp.model.po.system.User;
import com.yjp.erp.model.po.system.UserOrg;
import com.yjp.erp.model.vo.system.UserVO;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author wyf
 * @date 2019/4/8 上午 11:35
 **/
@Repository
public interface UserOrgMapper {
    /**
     * 批量插入用户组织关联
     * @author wyf
     * @date  2019/4/12 下午 3:16
     * @param userOrgList 用户组织关联集合
     */
    void insertList(List<UserOrg> userOrgList);
    /**
     * 根据用户id列表删除用户组织关联
     * @author wyf
     * @date  2019/4/12 下午 3:16
     * @param userList 用户集合
     */
    void deleteUserOrgByUserList(List<User> userList);
    /**
     * 根据用户id获取用户组织关联
     * @author wyf
     * @date  2019/4/12 下午 3:17
     * @param userId 用户id
     * @return java.util.List<com.yjp.erp.model.po.system.UserOrg>
     */
    List<UserOrg> getListByUserId(Long userId);
    /**
     * 根据组织id获取用户组织关联
     * @author wyf
     * @date  2019/4/12 下午 3:17
     * @param orgId 组织id
     * @return java.util.List<com.yjp.erp.model.po.system.UserOrg>
     */
    List<UserOrg> getListByOrgId(Long orgId);

    /**
     * 根据用户集合查询用户组织关联信息
     * @author wyf
     * @date  2019/4/17 下午 9:21
     * @param userList 用户集合
     * @return java.util.List<com.yjp.erp.model.po.system.UserOrg>
     */
    List<UserOrg> listUserOrgByUserList(List<UserVO> userList);
}
