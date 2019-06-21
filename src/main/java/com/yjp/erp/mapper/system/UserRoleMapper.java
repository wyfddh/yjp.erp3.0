package com.yjp.erp.mapper.system;

import com.yjp.erp.model.po.system.User;
import com.yjp.erp.model.po.system.UserRole;
import com.yjp.erp.model.vo.system.UserVO;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author wyf
 * @date 2019/4/3 下午 8:47
 **/
@Repository
public interface UserRoleMapper {
    /**
     * 批量插入用户角色关联信息
     * @author wyf
     * @date  2019/4/12 下午 3:17
     * @param userRoleList 用户角色关联信息
     */
    void insertList(List<UserRole> userRoleList);
    /**
     * 根据用户集合删除用户角色关联
     * @author wyf
     * @date  2019/4/12 下午 3:19
     * @param userList 用户集合
     */
    void deleteRoleByUserList(List<User> userList);
    /**
     * 根据用户集合查询角色集合
     * @author wyf
     * @date  2019/5/21 下午 5:30
     * @param userList 用户集合
     * @return java.util.List<com.yjp.erp.model.po.system.UserRole>
     */
    List<UserRole> listRoleByUserList(List<UserVO> userList);
}
