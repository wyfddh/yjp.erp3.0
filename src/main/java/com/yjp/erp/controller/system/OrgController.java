package com.yjp.erp.controller.system;

import com.yjp.erp.conf.UserInfo;
import com.yjp.erp.conf.UserInfoManager;
import com.yjp.erp.model.dto.PageDTO;
import com.yjp.erp.model.dto.system.OrgDTO;
import com.yjp.erp.model.dto.system.UserDTO;
import com.yjp.erp.conf.exception.BusinessException;
import com.yjp.erp.model.po.system.Organization;
import com.yjp.erp.service.system.OrgService;
import com.yjp.erp.util.ObjectUtils;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.PageListVO;
import com.yjp.erp.model.vo.RetCode;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.model.vo.system.OrgVO;
import com.yjp.erp.model.vo.system.UserVO;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 组织相关接口
 * @author wyf
 * @date 2019/4/8 上午 11:32
 **/
@RestController
@Slf4j
@RequestMapping({"/config/org","/apps/org"})
public class OrgController {

    @Autowired
    private UserInfoManager userInfoManager;
    
    @Autowired
    private OrgService orgService;

    /**
     * @author wyf
     * @description       分页获取所有组织（包括冻结和正常）
     * @param pageDTO f分页对象
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("listPageOrg")
    public JsonResult listPageOrg(@RequestBody PageDTO pageDTO) throws Exception{

        PageListVO<OrgVO> vo = orgService.listPageOrg(pageDTO);
        return RetResponse.makeOKRsp(vo);
    }

    /**
     * @author wyf
     * @description       获取组织树结构（包括冻结和正常）
     * @param  orgType 组织类型
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @GetMapping("orgTree")
    public JsonResult getOrg(String orgType) throws Exception{

        OrgVO orgTree = orgService.getOrgTree(orgType);
        return RetResponse.makeOKRsp(orgTree);
    }

    /**
     * @author wyf
     * @description    获取所有正常的组织   
     * @param orgType 组织类型
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @GetMapping("normalOrg")
    public JsonResult normalOrg(String orgType) throws Exception{

        int type = 1;
        PageListVO<OrgVO> vo = orgService.listOrg(type,orgType);
        return RetResponse.makeOKRsp(vo);
    }

    /**
     * @author wyf
     * @description       获取当前用户正常的组织
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @GetMapping("listNormalOrg")
    public JsonResult listNormalOrg() throws Exception{
        
        Long userId = Long.valueOf(userInfoManager.getUserInfo().getUserId());
        List<OrgVO> orgVOS = orgService.listNormalOrg(userId);
        return RetResponse.makeOKRsp(orgVOS);
    }

    /**
     * @author wyf
     * @description       根据用户获取组织
     * @param userDTO 用户相关参数
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("getOrgByUser")
    public JsonResult getOrgByUser(@RequestBody UserDTO userDTO) throws Exception{

        OrgVO orgByUser = orgService.getOrgByUser(userDTO);
        return RetResponse.makeOKRsp(orgByUser);
    }

    /**
     * @author wyf
     * @description       根据组织获取用户
     * @param orgDTO 组织相关入参
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("getUserByOrg")
    public JsonResult getUserByOrg(@RequestBody OrgDTO orgDTO) throws Exception{

        PageListVO<UserVO> userByOrg = orgService.getUserByOrg(orgDTO);
        return RetResponse.makeOKRsp(userByOrg);
    }

    /**
     * @author wyf
     * @description      添加组织 
     * @param orgDTO 组织相关入参
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("addOrg")
    public JsonResult addOrg(@RequestBody OrgDTO orgDTO) throws Exception{

        orgService.addOrg(orgDTO);
        return RetResponse.makeOKRsp();
    }

    /**
     * @author wyf
     * @description       更新组织
     * @param orgDTO 组织相关入参
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("updateOrg")
    public JsonResult updateOrg(@RequestBody OrgDTO orgDTO) throws Exception{

        orgService.updateOrg(orgDTO);
        return RetResponse.makeOKRsp();
    }

    /**
     * @author wyf
     * @description       根据名字模糊查询
     * @param orgDTO 组织相关入参
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("likeOrgByName")
    public JsonResult likeOrgByName(@RequestBody OrgDTO orgDTO) throws Exception{

        PageListVO<OrgVO> vo = orgService.likeOrgByName(orgDTO.getName());
        return RetResponse.makeOKRsp(vo);
    }

    /**
     * @author wyf
     * @description       根据组织名定位组织
     * @param name 组织名
     * @param orgType 组织类型
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @GetMapping("orgLocation")
    public JsonResult orgLocation(String name,String orgType) throws Exception{

        List<String> ids = orgService.orgLocation(name,orgType);
        return RetResponse.makeOKRsp(ids);
    }

    /**
     * @author wyf
     * @description      检测组织名是否可用
     * @param orgDTO 组织相关入参
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @PostMapping("checkOrgName")
    public JsonResult checkOrgName(@RequestBody OrgDTO orgDTO) throws Exception{

        Boolean bo = orgService.checkOrgName(orgDTO.getName(),orgDTO.getOrgType().toString());
        String msg;
        if (bo) {
            msg = "true";
        } else {
            msg = "false";
        }
        return RetResponse.makeRsp(RetCode.SUCCESS,msg);
    }

    /**
     * @author wyf
     * @description   切换组织
     * @param  orgId 切换后的组织id
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @GetMapping("changeOrg")
    public JsonResult changeOrg(String orgId) throws Exception{

        if (ObjectUtils.isEmpty(orgId)) {
            throw new BusinessException(RetCode.PARAM_EMPTY,"参数为空");
        }
        UserInfo userInfo = orgService.changeOrg(orgId);
        return RetResponse.makeOKRsp(userInfo);
    }

    /**
     * @author wyf
     * @description   获取当前登录用户的业务组织集合
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @GetMapping("getVOrg")
    public JsonResult getVOrg() throws Exception{

        UserInfo userInfo = userInfoManager.getUserInfo();
        List<OrgVO> businessOrgList = userInfo.getBusinessOrgList();
        return RetResponse.makeOKRsp(businessOrgList);
    }

    /**
     * @author wyf
     * @description   获取所有的虚拟组织
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @GetMapping("getAllVOrg")
    public JsonResult getAllVOrg() throws Exception{

        Long userId = Long.valueOf(userInfoManager.getUserInfo().getUserId());
        List<OrgVO> orgVOList = orgService.getAllVOrgByUserId(userId);
        return RetResponse.makeOKRsp(orgVOList);
    }

    /**
     * @author wyf
     * @description    根据虚拟组织id获取基本组织（仓库）
     * @param orgId 虚拟组织id
     * @return com.yjp.erp.model.vo.JsonResult
     */
    @GetMapping("getBOrgByVOrg")
    public JsonResult getBOrgByVOrg(String orgId) throws Exception{

        List<Long> vids = Collections.singletonList(Long.valueOf(orgId));
        List<Organization> bOrgByVId = orgService.getBOrgByVId(vids);
        return RetResponse.makeOKRsp(bOrgByVId);
    }

    

}
