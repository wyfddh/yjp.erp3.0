package com.yjp.erp.mapper.system;

import com.yjp.erp.model.po.system.User;
import org.springframework.stereotype.Repository;

/**
 * @author wyf
 * @date 2019/4/20 下午 5:58
 **/
@Repository
public interface UserMapper {

    /**
     * 新增用户
     * @author wyf
     * @param user 用户
     */
    void insertUser(User user);
    /**
     * 根据用户名查询用户
     * @author wyf
     * @param userName 用户名
     * @return com.yjp.erp.model.po.system.User
     */
    User getUserByUserName(String userName);
}
