package com.yjp.erp.model.po.system;


import java.sql.Timestamp;
import lombok.Data;

/**
 * @author wyf
 * @date 2019/4/2 上午 9:19
 **/
@Data
public class User {

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
}
