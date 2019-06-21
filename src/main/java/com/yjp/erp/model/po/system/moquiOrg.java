package com.yjp.erp.model.po.system;

import java.sql.Date;
import java.sql.Timestamp;
import lombok.Data;

/**
 * @author wyf
 * @date 2019/6/10 上午 10:39
 **/
@Data
public class moquiOrg{

    private Long id;

    private Long parentId;
    /**
     * @description  0叶子节点1非叶子节点
     */
    private Integer baseNode;
    /**
     * @description  组织类型1业务组织，2财务组织
     */
    private Integer orgType;

    private String name;

    private String description;

    private String creator;

    private String updater;

    private String addressProvince;

    private String addressCity;

    private String addressCounty;

    private String addressDetailAddress;

    private Date createTime;

    /**
     * @description  //删除标记(-1已删除1正常0冻结/停用)
     */
    private Integer status;
}
