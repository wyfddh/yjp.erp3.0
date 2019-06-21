package com.yjp.erp.model.dto.system;

import com.yjp.erp.model.po.system.Role;
import com.yjp.erp.model.po.system.User;
import java.util.List;
import lombok.Data;

/**
 * @author wyf
 * @date 2019/4/3 下午 8:28
 **/
@Data
public class UserRoleDTO {

    private List<User> userList;

    private List<Role> roleList;
}
