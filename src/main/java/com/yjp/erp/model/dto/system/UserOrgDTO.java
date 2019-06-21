package com.yjp.erp.model.dto.system;

import com.yjp.erp.model.po.system.Organization;
import com.yjp.erp.model.po.system.User;
import java.util.List;
import lombok.Data;

/**
 * @author wyf
 * @date 2019/4/8 下午 4:17
 **/
@Data
public class UserOrgDTO {
    private List<Organization> orgList;

    private List<User> userList;
}
