package com.yjp.erp.model.dto.system;

import java.sql.Timestamp;
import lombok.Data;

/**
 * @author wyf
 * @date 2019/4/15 下午 7:19
 **/
@Data
public class UserDTO {

    private Long id;

    private String userName;

    private String displayName;

    private String phone;

    private String password;

    private String newPassword;

    private String token;

    private Long createTime;

    private Timestamp lastLoginTime;

    private String lastLoginTimeStr;

    private Integer status;

    private Integer mark;

}
