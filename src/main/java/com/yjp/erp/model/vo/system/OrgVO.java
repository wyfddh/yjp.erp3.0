package com.yjp.erp.model.vo.system;

import java.sql.Timestamp;
import java.util.List;
import lombok.Data;

/**
 * @author wyf
 * @date 2019/4/15 下午 2:06
 **/
@Data
public class OrgVO {

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
    /**
     * @description  //仓库类型：1.城市仓库、2.总部仓库、3.轻加盟仓库、4.经销商仓库等
     */
    private Integer storeHouseType;

    private Timestamp createTime;

    private Timestamp lastUpdateTime;
    /**
     * @description  //删除标记(-1已删除1正常0冻结/停用)
     */
    private Integer status;

    private Long key;

    private Long value;

    private String title;

    private List<Long> checkedOrgList;

    private List<OrgVO> children;

    private List<String> baseOrgNameList;

    private List<Long> baseOrgIdList;

    private List<Long> vOrgIdList;
}
