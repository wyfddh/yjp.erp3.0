package com.yjp.erp.model.dto.system;

import java.io.Serializable;
import lombok.Data;

/**
 * @author wyf
 * @date 2019/4/2 下午 9:05
 **/
@Data
public class LoginParam implements Serializable{

    private static final long serialVersionUID = 1L;

    private String userName;

    private String password;

    private String captchaCode;

    private boolean rememberMe;

    private String token;

}
