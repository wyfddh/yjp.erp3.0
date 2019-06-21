package com.yjp.erp.model.dto.system;

import com.yjp.erp.model.po.system.Organization;
import com.yjp.erp.model.po.system.Role;
import com.yjp.erp.model.po.system.User;
import java.util.List;
import lombok.Data;

/**
 * @author wyf
 * @date 2019/4/17 下午 8:27
 **/
@Data
public class UserRoleOrgDTO {

    private List<Organization> orgList;

    private List<User> userList;

    private List<Role> roleList;
}
