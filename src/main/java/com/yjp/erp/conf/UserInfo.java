package com.yjp.erp.conf;

import com.yjp.erp.model.po.system.Role;
import com.yjp.erp.model.vo.system.OrgVO;
import java.io.Serializable;
import java.util.List;

/**
 * 会话信息
 * Created by weibaichuan on 2019/4/13
 */
public class UserInfo implements Serializable {

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 多套账，命名空间
     */
    private String companySpace = "yjp";

    /**
     * 用户角色
     */
    private List<Role> roleList;

    /**
     * 基础组织Id
     */
    private List<Long> orgList;

    private List<OrgVO> baseOrgList;

    /**
     * 用户所拥有的业务组织集合
     */
    private List<OrgVO> businessOrgList;

    /**
     * 用户所拥有的财务组织集合
     */
    private List<OrgVO> financeOrgList;

    public UserInfo(String userId, String companySpace,List<Long> orgList,
            List<OrgVO> businessOrgList,List<OrgVO> financeOrgList,List<OrgVO> baseOrgList,
            List<Role> roleList) {
        this.userId = userId;
        this.companySpace = companySpace;
        this.orgList = orgList;
        this.businessOrgList = businessOrgList;
        this.financeOrgList = financeOrgList;
        this.baseOrgList = baseOrgList;
        this.roleList = roleList;
    }

    @Override
    public String toString() {
        return "UserInfo{" + "userId='" + userId + '\'' + ", companySpace='" + companySpace + '\'' + ", orgList="
                + orgList + '}';
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    /**
     * 获取 用户Id
     *
     * @return userId 用户Id
     */
    public String getUserId() {
        return this.userId;
    }

    /**
     * 设置 用户Id
     *
     * @param userId 用户Id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取 多套账，命名空间
     *
     * @return companySpace 多套账，命名空间
     */
    public String getCompanySpace() {
        return this.companySpace;
    }

    /**
     * 设置 多套账，命名空间
     *
     * @param companySpace 多套账，命名空间
     */
    public void setCompanySpace(String companySpace) {
        this.companySpace = companySpace;
    }

    /**
     * 获取 组织Id
     *
     * @return orgId 组织Id
     */
    public List<Long> getOrgList() {
        return this.orgList;
    }

    /**
     * 设置 组织Id
     *
     * @param orgList 组织Id
     */
    public void setOrgList(List<Long> orgList) {
        this.orgList = orgList;
    }

    public List<OrgVO> getBusinessOrgList() {
        return businessOrgList;
    }

    public void setBusinessOrgList(List<OrgVO> businessOrgList) {
        this.businessOrgList = businessOrgList;
    }

    public List<OrgVO> getFinanceOrgList() {
        return financeOrgList;
    }

    public void setFinanceOrgList(List<OrgVO> financeOrgList) {
        this.financeOrgList = financeOrgList;
    }

    public List<OrgVO> getBaseOrgList() {
        return baseOrgList;
    }

    public void setBaseOrgList(List<OrgVO> baseOrgList) {
        this.baseOrgList = baseOrgList;
    }
}
