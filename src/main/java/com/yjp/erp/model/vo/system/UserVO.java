package com.yjp.erp.model.vo.system;

import com.yjp.erp.model.po.system.Role;
import java.sql.Timestamp;
import java.util.List;
import lombok.Data;

/**
 * @author wyf
 * @date 2019/4/2 下午 5:41
 **/
@Data
public class UserVO {

    private Long id;

    private String userName;

    private String displayName;

    private String phone;

    private String password;

    private String token;

    private Long createTime;

    private Timestamp lastLoginTime;

    private String lastLoginTimeStr;

    private Integer status;

    private Integer mark;

    private List<Role> roleList;

    private List<OrgVO> orgList;

}
