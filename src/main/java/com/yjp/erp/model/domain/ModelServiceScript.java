package com.yjp.erp.model.domain;

import lombok.Data;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/29 15:37
 * @Email: jianghongping@yijiupi.com
 */
@Data
public class ModelServiceScript {
    /**
     * id
     */
    private Long id;
    /**
     * 服务名
     */
    private String serviceName;
    /**
     * verb
     */
    private String verb;
    /**
     * 脚本编码
     */
    private String scriptCode;
    /**
     * 脚本类容
     */
    private String serviceScript;
    /**
     * 脚本描述
     */
    private String lable;
    /**
     * 通用脚本位置
     */
    private String scriptLocation;
}
