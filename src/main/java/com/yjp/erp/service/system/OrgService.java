package com.yjp.erp.service.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yjp.erp.conf.UserInfo;
import com.yjp.erp.conf.UserInfoManager;
import com.yjp.erp.constants.SystemConstant;
import com.yjp.erp.mapper.system.VirtualRelateVirtualOrgMapper;
import com.yjp.erp.model.dto.PageDTO;
import com.yjp.erp.model.dto.system.OrgDTO;
import com.yjp.erp.model.dto.system.UserDTO;
import com.yjp.erp.conf.exception.BusinessException;
import com.yjp.erp.mapper.system.BaseOrgRelateOrgMapper;
import com.yjp.erp.mapper.system.OrgMapper;
import com.yjp.erp.mapper.system.UserOrgMapper;
import com.yjp.erp.model.po.system.BaseRelateOrg;
import com.yjp.erp.model.po.system.Organization;
import com.yjp.erp.model.po.system.User;
import com.yjp.erp.model.po.system.UserOrg;
import com.yjp.erp.model.po.system.VorgRelateVorg;
import com.yjp.erp.model.po.system.moquiOrg;
import com.yjp.erp.util.HttpClientUtils;
import com.yjp.erp.util.ObjectUtils;
import com.yjp.erp.util.SnowflakeIdWorker;
import com.yjp.erp.model.vo.PageListVO;
import com.yjp.erp.model.vo.RetCode;
import com.yjp.erp.model.vo.system.OrgVO;
import com.yjp.erp.model.vo.system.UserVO;
import java.sql.Timestamp;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * @author wyf
 * @date 2019/4/8 上午 11:36
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class OrgService {

    @Autowired
    private UserInfoManager userInfoManager;

    @Autowired
    private OrgMapper orgMapper;

    @Autowired
    private BaseOrgRelateOrgMapper baseOrgRelateOrgMapper;

    @Autowired
    private VirtualRelateVirtualOrgMapper virtualRelateVirtualOrgMapper;

    @Autowired
    private UserOrgMapper userOrgMapper;

    @Autowired
    private UserService userService;

    @Value("${moqui.token.url}")
    private String moquiUrl;
    @Value("${moqui.rest.org}")
    private String restOrg;
    @Value("${moqui.rest.updateOrg}")
    private String restUpdateOrg;
    @Value("${moqui.rest.getOrgByName}")
    private String restGetOrgByNama;
    @Value("${moqui.rest.getErpOrg}")
    private String restGetErpOrg;
    @Value("${moqui.rest.getOrgByIds}")
    private String restGetOrgByIds;
    @Value("${moqui.rest.createVirtualOrg}")
    private String createVirtualOrgUrl;
    @Value("${moqui.rest.updateVirtualOrg}")
    private String updateVirtualOrgUrl;
    @Value("${moqui.rest.addVirtualOrgRelation}")
    private String addVirtualOrgRelationUrl;
    @Value("${moqui.rest.deleteVirtualOrgRelation}")
    private String deleteVirtualOrgRelationUrl;

    /**
     * @author wyf
     * @description  获取所有组织包括冻结和正常
     * @param type 过滤数据类型 1只保留正常数据，2保留正常和停用
     * @param orgType 查询的组织类型
     * @date  2019/4/8 上午 11:46
     */
    public PageListVO<OrgVO> listOrg(int type,String orgType) throws Exception{
        List<OrgVO> resList = new ArrayList<>();
        //基础组织查询moqui
        if (Objects.equals(String.valueOf(SystemConstant.ORG_TYPE_BASE),orgType)) {
            String url = restOrg;
            String s = HttpClientUtils.get(url);
            List<OrgVO> orgList = JSONArray.parseArray(s, OrgVO.class);

            if (CollectionUtils.isNotEmpty(orgList)) {
                for (OrgVO org : orgList) {
                    org.setKey(org.getId());
                    org.setValue(org.getId());
                    org.setTitle(org.getName());
                    if (type == SystemConstant.NORMAL_TYPE) {
                        if (Objects.equals(org.getStatus(), SystemConstant.NORMAL_STATUS)) {
                            resList.add(org);
                        }
                    } else {
                        if (Objects.equals(org.getStatus(), SystemConstant.NORMAL_STATUS) ||
                                Objects.equals(org.getStatus(), SystemConstant.FROZEN_STATUS)) {
                            resList.add(org);
                        }
                    }
                }
            }
        } else {
            Map<String,Object> map = new HashMap<>(4);
            if (ObjectUtils.isNotEmpty(orgType)) {
                map.put("orgType",orgType);
            }
            map.put("status",type);

            map.put("start",SystemConstant.PAGE_NO);
            map.put("end",SystemConstant.PAGE_SIZE);
            resList = orgMapper.listVirtualOrgByMap(map);
        }

        PageDTO pageDTO = new PageDTO();
        pageDTO.setPageNo(SystemConstant.PAGE_NO);
        pageDTO.setPageSize(SystemConstant.PAGE_SIZE);
        pageDTO.setTotal(resList.size());

        PageListVO<OrgVO> orgVOPageListVO = new PageListVO<>();
        orgVOPageListVO.setList(resList);
        orgVOPageListVO.setPageDTO(pageDTO);
        return orgVOPageListVO;
    }

    /**
     * @author wyf
     * @description  分页获取所有组织包括冻结和正常
     * @date  2019/4/12 上午 10:29
     * @param pageDTO page对象
     * @return java.util.List<com.yjp.erp.model.po.system.Organization>
     */
    public PageListVO<OrgVO> listPageOrg(PageDTO pageDTO) throws Exception{
        Integer type = pageDTO.getOrgType();
        List<OrgVO> orgList = new ArrayList<>();
        Integer total = 0;
        //基本节点查询moqui
        if (Objects.equals(SystemConstant.ORG_TYPE_BASE,type)) {
            int start = pageDTO.getMoquiStart();
            String url = restGetErpOrg + "?pageNo=" + start + "&pageSize=" + pageDTO.getPageSize();
            String result = HttpClientUtils.get(url);
            JSONObject jsonObject = JSON.parseObject(result);
            jsonObject = ObjectUtils.isNotEmpty(jsonObject) ? jsonObject : new JSONObject();
            int code = jsonObject.getIntValue("code");
            if (!Objects.equals(code,SystemConstant.SUCCESS_CODE)) {
                throw  new BusinessException(RetCode.MOQUI_ERROR,"moqui服务异常");
            }
            JSONObject data = jsonObject.getJSONObject("data");

            //查询moqui的组织数据

            if (ObjectUtils.isNotEmpty(data)) {
                JSONArray list = data.getJSONArray("list");
                orgList = list.toJavaList(OrgVO.class);
                total = data.getInteger("dataCount");
            }
        } else {
            Map<String,Object> map = new HashMap<>(4);
            map.put("orgType",type);
            map.put("status",SystemConstant.ALL_TYPE);
            total = orgMapper.countVirtualOrgByMap(map);

            int start = pageDTO.getMysqlStart();
            map.put("start",start);
            map.put("end",start + pageDTO.getPageSize());
            orgList = orgMapper.listVirtualOrgByMap(map);

        }
        List<OrgVO> resOrgList = new ArrayList<>();
        handleOrgVoList(orgList,resOrgList);

        pageDTO.setTotal(total);
        PageListVO<OrgVO> vo = new PageListVO<>();
        vo.setList(resOrgList);
        vo.setPageDTO(pageDTO);
        return vo;
    }

    /**
     * @author wyf
     * @description      组装前端需要的组织层级结构
     * @date  2019/5/21 下午 3:36
     * @param orgList 查询的数据结果
     * @param resOrgList 返回的数据集合
     */
    private void handleOrgVoList(List<OrgVO> orgList,List<OrgVO> resOrgList) throws Exception{

        for (OrgVO org : orgList) {
            if (!Objects.equals(org.getParentId(),SystemConstant.ORG_PARENT_ID) &&
                    Objects.equals(org.getBaseNode(),SystemConstant.LEAF_NOTE)) {
                continue;
            }
            org.setKey(org.getId());
            org.setValue(org.getId());
            org.setTitle(org.getName());
            List<OrgVO> orgChilds = new ArrayList<>();
            for (OrgVO orgVO : orgList) {
                Long parentId = orgVO.getParentId();
                if (Objects.equals(org.getId(),parentId)) {
                    OrgVO newOrgVO = new OrgVO();
                    BeanUtils.copyProperties(orgVO,newOrgVO);
                    newOrgVO.setKey(newOrgVO.getId());
                    newOrgVO.setValue(newOrgVO.getId());
                    newOrgVO.setTitle(newOrgVO.getName());
                    //查询该组织引用的基础组织
                    List<Long> vIds = Collections.singletonList(newOrgVO.getId());
                    List<Organization> bOrgByVId = getBOrgByVId(vIds);
                    //查询该组织引用的虚拟组织
                    List<VorgRelateVorg> relateByVIds = virtualRelateVirtualOrgMapper.getRelateByVId(newOrgVO.getId());
                    List<Long> relateVIds = relateByVIds.stream().map(VorgRelateVorg::getRelateVirtualOrgId).collect(
                            Collectors.toList());
                    //设置前端需要的基础组织名
                    List<String> baseOrgNameList = bOrgByVId.stream().map(Organization::getName).collect(Collectors.toList());
                    //设置前端需要的基础组织id
                    List<Long> baseOrgIdList = bOrgByVId.stream().map(Organization::getId).collect(Collectors.toList());
                    newOrgVO.setBaseOrgIdList(baseOrgIdList);
                    newOrgVO.setBaseOrgNameList(baseOrgNameList);
                    newOrgVO.setVOrgIdList(relateVIds);
                    orgChilds.add(newOrgVO);
                }
            }
            if (ObjectUtils.isNotEmpty(orgChilds)) {
                org.setChildren(orgChilds);
            }
            if (Objects.equals(org.getParentId(),SystemConstant.ORG_PARENT_ID)) {
                //查询该组织引用的基础组织
                List<Long> vIds = Collections.singletonList(org.getId());
                List<Organization> bOrgByVId = getBOrgByVId(vIds);
                //查询该组织引用的虚拟组织
                List<VorgRelateVorg> relateByVIds = virtualRelateVirtualOrgMapper.getRelateByVId(org.getId());
                List<Long> relateVIds = relateByVIds.stream().map(VorgRelateVorg::getRelateVirtualOrgId).collect(
                        Collectors.toList());
                //设置前端需要的基础组织名
                List<String> baseOrgNameList = bOrgByVId.stream().map(Organization::getName).collect(Collectors.toList());
                //设置前端需要的基础组织id
                List<Long> baseOrgIdList = bOrgByVId.stream().map(Organization::getId).collect(Collectors.toList());
                org.setBaseOrgIdList(baseOrgIdList);
                org.setBaseOrgNameList(baseOrgNameList);
                org.setVOrgIdList(relateVIds);
                resOrgList.add(org);
            }
        }
    }

    /**
     * @author wyf
     * @description  获取当前用户能看到的正常组织
     * @date  2019/4/8 上午 11:46
     * @param userId 用户id
     */
    public List<OrgVO> listNormalOrg(Long userId) throws Exception{

        return listOrgBydUserId(userId);
    }

    /**
     * @author wyf
     * @description  根据用户id获取组织
     * @date  2019/4/12 下午 4:12
     * @param userId 用户id
     * @return java.util.List<com.yjp.erp.model.po.system.Organization>
     */
    private List<OrgVO> listOrgBydUserId(Long userId) throws Exception {
        List<OrgVO> resultOrgList = new ArrayList<>();
        List<Long> idList = new ArrayList<>();
        //获取当前用户的组织关联
        List<UserOrg> userOrgList = userOrgMapper.getListByUserId(userId);

        userOrgList.forEach(u -> idList.add(u.getOrgId()));
        if (ObjectUtils.isNotEmpty(userOrgList)) {

            List<Organization> orgByVIds = getOrgByVIds(idList);
            for (Organization orgByVId : orgByVIds) {
                OrgVO orgVO = new OrgVO();
                BeanUtils.copyProperties(orgByVId,orgVO);
                resultOrgList.add(orgVO);
            }
        }
        return resultOrgList;
    }

    /**
     * @author wyf
     * @description   获取组织的树结构
     * @date  2019/4/16 下午 2:44
     * @return com.yjp.erp.model.vo.system.OrgVO
     */
    public OrgVO getOrgTree(String orgType) throws Exception{

        List<OrgVO> list = listOrg(2,orgType).getList();
        return handleOrg(list);
    }

    /**
     * @author wyf
     * @description  组装orgList，返回一个json格式的org
     * @date  2019/4/4 上午 10:09
     * @param orgList 组织集合
     * @return com.yjp.erp.model.po.system.Menu
     */
    private OrgVO handleOrg(List<OrgVO> orgList) throws Exception{
        //设置根节点
        OrgVO org = new OrgVO();
        org.setId(SystemConstant.MENU_PARENT_ID);
        org.setTitle("ERP业务应用");
        org.setKey(org.getId());
        org.setValue(org.getId());
        //找到所有一级组织
        List<OrgVO> pOrgs = orgList.stream()
                .filter(p -> Objects.equals(p.getParentId(),SystemConstant.ORG_PARENT_ID))
                .collect(Collectors.toList());
        //为一级菜单设置子组织准备递归
        pOrgs.forEach(p -> p.setChildren(getChildList(p.getId(),orgList)));
        org.setChildren(pOrgs);
        return org;
    }

    private List<OrgVO> getChildList(Long id, List<OrgVO> orgList){
        //构建子组织
        List<OrgVO> childList = orgList.stream()
                .filter(o -> ObjectUtils.isNotEmpty(o.getParentId()) && Objects.equals(id,o.getParentId()))
                .collect(Collectors.toList());
        //递归
        childList.forEach(o -> o.setChildren(getChildList(o.getId(),orgList)));
        if (childList.size() == 0){
            return null;
        }
        return childList;
    }

    /**
     * @author wyf
     * @description   新增组织
     * @date  2019/4/23 上午 9:32
     * @param  orgDTO 组织入参实体
     */
    public void addOrg(OrgDTO orgDTO) throws Exception{
        if (ObjectUtils.isEmpty(orgDTO) || ObjectUtils.isEmpty(orgDTO.getName())
                || ObjectUtils.isEmpty(orgDTO.getOrgType())) {
            throw new BusinessException(RetCode.PARAM_EMPTY,"参数为空");
        }
        //检测组织名是否重复
        if (!checkOrgName(orgDTO.getName(),orgDTO.getOrgType().toString())) {
            throw new BusinessException(RetCode.DUPLICATE_ORGNAME,"组织名重复");
        }
        Organization org = new Organization();
        BeanUtils.copyProperties(orgDTO,org);
        String userId = userInfoManager.getUserInfo().getUserId();
        if (ObjectUtils.isEmpty(org.getStatus())) {
            org.setStatus(SystemConstant.NORMAL_STATUS);
        }
        org.setCreator(userId);
        org.setUpdater(userId);
        //如果新增时不选择父节点，则该节点为父节点
        if (ObjectUtils.isEmpty(org.getParentId())) {
            org.setParentId(SystemConstant.ORG_PARENT_ID);
        }
        //基本组织插入moqui表
        if (Objects.equals(org.getOrgType(),SystemConstant.ORG_TYPE_BASE)) {

            String url = restOrg;
            HttpClientUtils.postSimple(url,org);
        } else {
            if (ObjectUtils.isEmpty(org.getParentId())) {
                org.setBaseNode(SystemConstant.LEAF_NOT_NOTE);
            } else {
                org.setBaseNode(SystemConstant.LEAF_NOTE);
            }
            org.setId(SnowflakeIdWorker.nextId());
            Timestamp nowTime = new Timestamp(System.currentTimeMillis());
            org.setLastUpdateTime(nowTime);
            org.setCreateTime(nowTime);
            //同步更新数据到moqui的虚拟组织
            moquiOrg moquiOrg = new moquiOrg();
            BeanUtils.copyProperties(org,moquiOrg);
            HttpClientUtils.postSimple(createVirtualOrgUrl,moquiOrg);
            orgMapper.insert(org);
            orgDTO.setId(org.getId());
            //批量更新组织的节点信息
            handleOrgNode();
            //处理虚拟组织和基本组织的关系
            handleOrg(orgDTO);
        }
    }
    
    /**
     * @author wyf
     * @description  修改组织
     * @date  2019/4/8 下午 2:58
     * @param  orgDTO 组织dto
     */
    public void updateOrg(OrgDTO orgDTO) throws Exception{
        if (ObjectUtils.isEmpty(orgDTO) || ObjectUtils.isEmpty(orgDTO.getId())
                || ObjectUtils.isEmpty(orgDTO.getOrgType())) {
            throw new BusinessException(RetCode.PARAM_EMPTY,"参数为空");
        }
        Organization org = new Organization();
        BeanUtils.copyProperties(orgDTO,org);
        Long userId = Long.valueOf(userInfoManager.getUserInfo().getUserId());
        org.setUpdater(userId.toString());
        //基础组织更新moqui
        if (Objects.equals(orgDTO.getOrgType(),SystemConstant.ORG_TYPE_BASE)) {
            //删除基本组织时
            if (Objects.equals(orgDTO.getStatus(),SystemConstant.DELETE_STATUS)) {
                //判断该组织是否有下级
                List<OrgVO> list = listOrg(SystemConstant.NORMAL_TYPE, String.valueOf(SystemConstant.ORG_TYPE_BASE)).getList();
                for (OrgVO orgVO : list) {
                    if (Objects.equals(orgVO.getParentId(),orgDTO.getId())) {
                        throw new BusinessException(RetCode.ORG_DELETE_ERROR,"该基础组织下有其他组织，不允许删除");
                    }
                }
                //判断该基础组织是否被引用
                int count = baseOrgRelateOrgMapper.countByBaseOrgId(orgDTO.getId());
                if (count > 0) {
                    throw new BusinessException(RetCode.ORG_DELETE_ERROR,"该基础组织已被其他组织引用，不允许删除");
                }
            }
            String url = restUpdateOrg;

            HttpClientUtils.postSimple(url,org);
        } else {
            org.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
            //同步更新数据到moqui的虚拟组织
            moquiOrg moquiOrg = new moquiOrg();
            BeanUtils.copyProperties(org,moquiOrg);
            HttpClientUtils.postSimple(updateVirtualOrgUrl,moquiOrg);

            orgMapper.update(org);

            //批量更新组织的节点信息
            handleOrgNode();
            //处理虚拟组织和基本组织的关系
            handleOrg(orgDTO);
        }
    }

    /**
     * @author wyf
     * @description   每次新增和修改后调用，用于修改所有组织的节点
     * @date  2019/4/25 上午 10:15
     */
    private void handleOrgNode() throws Exception{
        //查询所有组织包括冻结和正常
        List<OrgVO> orgList = listOrg(SystemConstant.ALL_TYPE, null).getList();
        List<OrgVO> list = new ArrayList<>();
        for (OrgVO orgVO : orgList) {
            for (int i = 0; i < orgList.size(); i++) {
                //当该组织下有任意组织的pid等于自己，并且该组织当前的节点不是非叶子节点，则更新其节点为非叶子节点
                if (Objects.equals(orgList.get(i).getParentId(),orgVO.getId())) {
                    if (!Objects.equals(orgVO.getBaseNode(),SystemConstant.LEAF_NOT_NOTE)) {
                        orgVO.setBaseNode(SystemConstant.LEAF_NOT_NOTE);
                        list.add(orgVO);
                    }
                    break;
                }
                //当这一轮循环结束，没有组织pid等于自己，并且该组织的节点不是叶子节点。则更新其节点为叶子节点
                if (i == orgList.size() - 1 && !Objects.equals(orgList.get(i).getParentId(),orgVO.getId())
                        && !Objects.equals(orgVO.getBaseNode(),SystemConstant.LEAF_NOTE)) {
                    orgVO.setBaseNode(SystemConstant.LEAF_NOTE);
                    list.add(orgVO);
                }
            }
        }
        if (ObjectUtils.isNotEmpty(list)) {
            orgMapper.batchUpdateOrgNode(list);
        }
    }

    /**
     * @author wyf
     * @description  新增或修改虚拟组织后调用，用于处理虚拟组织和基本组织的关系
     * @date  2019/4/23 上午 10:50
     * @param orgDTO 组织dto入参
     */
    private void handleOrg(OrgDTO orgDTO) throws Exception {


        //同步删除moqui那边的组织对应关系
        Map<String,String> headers = new HashMap<>(4);
        headers.put("Content-Type","application/json");
        HashMap<String, Long> map = new HashMap<>(1);
        map.put("virtualOrgId", orgDTO.getId());
        HttpClientUtils.postParameters(deleteVirtualOrgRelationUrl, JSON.toJSONString(map), headers);

        //删除该组织与基本组织的关系
        baseOrgRelateOrgMapper.deleteByVOrgId(orgDTO.getId());
        //删除该组织与引用的虚拟组织关系
        virtualRelateVirtualOrgMapper.deleteByVOrgId(orgDTO.getId());

        List<Organization> baseOrgList = orgDTO.getBaseOrgList();
        List<Organization> vOrgList = orgDTO.getVOrgList();
        if (ObjectUtils.isNotEmpty(vOrgList)) {
            //根据虚拟组织查询其所有基础组织
            List<Long> vIdList = vOrgList.stream().map(Organization::getId).collect(Collectors.toList());
            List<VorgRelateVorg> vorgRelateVorgs = new ArrayList<>();
            for (Long relateId : vIdList) {
                VorgRelateVorg vorgRelateVorg = new VorgRelateVorg();
                vorgRelateVorg.setId(SnowflakeIdWorker.nextId());
                vorgRelateVorg.setRelateVirtualOrgId(relateId);
                vorgRelateVorg.setVirtualOrgId(orgDTO.getId());
                vorgRelateVorgs.add(vorgRelateVorg);
            }
            //批量新增该组织与引用的虚拟组织关系
            virtualRelateVirtualOrgMapper.batchInsert(vorgRelateVorgs);
            baseOrgList.addAll(getBOrgByVId(vIdList));
        }

        //根据id获取组织
        Organization org = orgMapper.getOrgById(orgDTO.getId());
        //如果当前组织是非叶子节点，则把该组织下的子节点的基础组织也引入该组织下
        if (Objects.equals(org.getBaseNode(),SystemConstant.LEAF_NOT_NOTE)) {
            List<OrgVO> orgList = listVSonOrgByVOrgId(org.getId());
            List<Long> vIds = orgList.stream().map(OrgVO::getId).collect(Collectors.toList());
            List<Organization> bOrgByVId = getBOrgByVId(vIds);
            baseOrgList.addAll(bOrgByVId);
        }
        if (ObjectUtils.isNotEmpty(baseOrgList)) {
            List<Long> orgIds = baseOrgList.stream().map(Organization::getId).collect(Collectors.toList());
            //去重
            LinkedHashSet<Long> set = new LinkedHashSet<>(orgIds.size());
            set.addAll(orgIds);
            orgIds.clear();
            orgIds.addAll(set);
            if (ObjectUtils.isNotEmpty(orgIds)) {
                List<BaseRelateOrg> baseRelateOrgs = new ArrayList<>();
                // 同步新增moqui那边的组织对应关系
                JSONArray array = new JSONArray();
                for (Long orgId : orgIds) {
                    BaseRelateOrg baseRelateOrg = new BaseRelateOrg();
                    baseRelateOrg.setId(SnowflakeIdWorker.nextId());
                    baseRelateOrg.setBaseOrgId(orgId);
                    baseRelateOrg.setVirtualOrgId(orgDTO.getId());
                    baseRelateOrgs.add(baseRelateOrg);
                    array.add(ObjectUtils.objectToMap(baseRelateOrgs));
                }
                HttpClientUtils.postParameters(addVirtualOrgRelationUrl, JSON.toJSONString(array), headers);
                //批量新增
                baseOrgRelateOrgMapper.batchInsert(baseRelateOrgs);
            }
        }
    }

    /**
     * @author wyf
     * @description  根据基本组织id集合查询基本组织列表
     * @date  2019/4/22 上午 11:41
     * @param idList 组织id集合
     * @return java.util.List<com.yjp.erp.model.po.system.Organization>
     */
    private List<Organization> getOrgByBIds(List<Long> idList) throws Exception{
        StringBuilder ids = new StringBuilder();
        List<Organization> data;
        if (ObjectUtils.isNotEmpty(idList)) {
            for (Long id : idList) {
                ids.append(",").append(id.toString());
            }
            ids = new StringBuilder(ids.substring(1, ids.length()));
            String url = restGetOrgByIds + "?ids=" + ids.toString();
            String result = HttpClientUtils.get(url);
            JSONObject jsonObject = JSON.parseObject(result);
            jsonObject = ObjectUtils.isNotEmpty(jsonObject) ? jsonObject : new JSONObject();
            int code =jsonObject.getIntValue("code");
            if (!Objects.equals(code,SystemConstant.SUCCESS_CODE)) {
                throw  new BusinessException(RetCode.MOQUI_ERROR,"moqui服务异常");
            }
            data = jsonObject.getJSONArray("data").toJavaList(Organization.class);
        } else {
            data = new ArrayList<>();
        }
        return data;
    }

    /**
     * @author wyf
     * @description  根据虚拟组织获取其基本组织
     * @date  2019/4/24 上午 10:04
     * @param vIds 虚拟组织id集合
     * @return java.util.List<com.yjp.erp.model.po.system.Organization>
     */
    public List<Organization> getBOrgByVId(List<Long> vIds) throws Exception{
        if (ObjectUtils.isEmpty(vIds)) {
            return new ArrayList<>();
        }
        List<BaseRelateOrg> baseRelateOrgs = baseOrgRelateOrgMapper.listOrgRelateByVOrgIds(vIds);
        List<Long> bIds = baseRelateOrgs.stream().map(BaseRelateOrg::getBaseOrgId).collect(Collectors.toList());

        return getOrgByBIds(bIds);
    }

    /**
     * @author wyf
     * @description   根据虚拟组织id集合查询虚拟组织列表
     * @date  2019/4/23 下午 2:48
     * @param idList 组织id集合
     * @return java.util.List<com.yjp.erp.model.po.system.Organization>
     */
    List<Organization> getOrgByVIds(List<Long> idList) throws Exception{

        return orgMapper.listOrgByVids(idList);
    }

    /**
     * @author wyf
     * @description  检测组织名是否可用。可用返回true
     * @date  2019/4/8 下午 2:33
     * @param name 组织名
     * @return java.lang.Boolean
     */
    public Boolean checkOrgName(String name,String orgType) throws Exception{
        Organization org = getOrgByName(name,orgType);
        return ObjectUtils.isEmpty(org);
    }

    /**
     * @author wyf
     * @description  根据组织名准确获取组织
     * @date  2019/4/8 下午 2:43
     * @param name 组织名
     * @param orgType 组织类型
     */
    private Organization getOrgByName(String name,String orgType) throws Exception{
        if (ObjectUtils.isEmpty(name) || ObjectUtils.isEmpty(orgType)) {
            throw new BusinessException(RetCode.PARAM_EMPTY,"参数为空");
        }
        Organization org;
        //orgType为基本组织时查询moqui
        if (Objects.equals(orgType,String.valueOf(SystemConstant.ORG_TYPE_BASE))) {
            String url = restGetOrgByNama + "?name=" + name;

            String result = HttpClientUtils.get(url);
            JSONObject jsonObject = JSON.parseObject(result);
            jsonObject = ObjectUtils.isNotEmpty(jsonObject) ? jsonObject : new JSONObject();
            int code = jsonObject.getIntValue("code");
            if (!Objects.equals(code,SystemConstant.SUCCESS_CODE)) {
                throw  new BusinessException(RetCode.MOQUI_ERROR,"moqui服务异常");
            }
            org = jsonObject.getObject("data", Organization.class);
        } else {
            org = orgMapper.getOrgByName(name,orgType);
        }

        return org;
    }

    /**
     * @author wyf
     * @description  根据组织名模糊获取组织 暂时未提供
     * @date  2019/4/8 下午 2:45
     * @param name 组织名
     */
    public PageListVO<OrgVO> likeOrgByName(String name) throws Exception{
        if (ObjectUtils.isNotEmpty(name)) {

            return null;
        } else {
            throw new BusinessException(RetCode.PARAM_EMPTY,"参数为空");
        }
    }

    /**
     * @author wyf
     * @description  根据用户列表删除其用户组织关联信息
     * @date  2019/4/17 下午 8:43
     * @param userList 用户列表
     */
    void deleteUserOrgByUserId(List<User> userList) throws Exception{

        userOrgMapper.deleteUserOrgByUserList(userList);
    }

    /**
     * @author wyf
     * @description  批量插入用户与组织关系信息
     * @date  2019/4/17 下午 8:45
     * @param userOrgList 用户与组织关联集合
     */
    void batchInsertUserOrg(List<UserOrg> userOrgList) throws Exception{

        userOrgMapper.insertList(userOrgList);
    }

    /**
     * @author wyf
     * @description  根据用户查询用户组织关联信息
     * @date  2019/4/8 下午 4:43
     * @param userDTO 用户信息dto
     * @return com.yjp.erp.model.vo.system.UserOrgVO
     */
    public OrgVO getOrgByUser(UserDTO userDTO) throws Exception{
        if (ObjectUtils.isNotEmpty(userDTO) && ObjectUtils.isNotEmpty(userDTO.getId())) {

            List<OrgVO> orgVOS = listOrgBydUserId(userDTO.getId());
            List<Long> idList = new ArrayList<>();
            for (OrgVO org : orgVOS) {
                idList.add(org.getId());
            }
            OrgVO orgTree = getOrgTree(null);

            orgTree.setCheckedOrgList(idList);

            return orgTree;
        } else {
            throw new BusinessException(RetCode.PARAM_EMPTY,"参数为空");
        }
    }

    /**
     * @author wyf
     * @description  根据组织查询用户信息
     * @date  2019/4/8 下午 4:56
     * @param orgDTO 组织入参dto
     * @return com.yjp.erp.model.vo.system.UserOrgVO
     */
    public PageListVO<UserVO> getUserByOrg(OrgDTO orgDTO) throws Exception{
        if (ObjectUtils.isNotEmpty(orgDTO) && ObjectUtils.isNotEmpty(orgDTO.getId())) {
            List<UserVO> list = new ArrayList<>();
            //查询所有用户
            PageDTO page = orgDTO.getPage();
            PageListVO<UserVO> userVOPageListVO = userService.listAllUser(page, 2);
            List<UserVO> userList = userVOPageListVO.getList();

            //查询该组织的用户关联
            List<UserOrg> userOrgList = userOrgMapper.getListByOrgId(orgDTO.getId());
            for (UserVO userVO : userList) {
                for (UserOrg userOrg : userOrgList) {
                    if (Objects.equals(userVO.getId(),userOrg.getUserId())) {
                        list.add(userVO);
                        break;
                    }
                }
            }
            PageListVO<UserVO> pageListVO = new PageListVO<>();
            page.setTotal(list.size());
            pageListVO.setPageDTO(page);
            pageListVO.setList(list);

            return pageListVO;
        } else {
            throw new BusinessException(RetCode.PARAM_EMPTY,"参数为空");
        }
    }

    /**
     * 
     * @description: 根据组织id集合查询组织信息
     * @param ids 组织id集合
     */
	public Map<Long, Organization> getOrgMapByIds(List<Long> ids) throws Exception{
		if (ids != null && !ids.isEmpty()) {
            Map<Long, Organization> map = new HashMap<>(ids.size());
            //根据传入的虚拟组织ids查询基本组织集合
            List<BaseRelateOrg> baseRelateOrgList = baseOrgRelateOrgMapper.listOrgRelateByVOrgIds(ids);
            for (BaseRelateOrg baseRelateOrg : baseRelateOrgList) {
                List<Long> baseOrgIds = Collections.singletonList(baseRelateOrg.getBaseOrgId());
                List<Organization> orgByIds = getOrgByBIds(baseOrgIds);
                for (Organization orgById : orgByIds) {
                	if(baseRelateOrg.getBaseOrgId().equals(orgById.getId())){
                		map.put(baseRelateOrg.getVirtualOrgId(),orgById);
                	}
                }
            }
            return map;
        }
		return new HashMap<>(1);
	}

    /**
     * @author wyf
     * @description  根据传入的虚拟获取当前的虚拟组织和其下级虚拟组织集合
     * @date  2019/4/12 下午 1:40
     * @param  id 组织id
     * @return java.util.List<com.yjp.erp.model.po.system.Organization>
     */
    private List<OrgVO> listVSonOrgByVOrgId(Long id) throws Exception{
        //获取所有组织
        List<OrgVO> list = listOrg(1,null).getList();
        return getSonOrg(list, id,true);
    }

    /**
     * @description 返回当前组织及其所有下级的组织集合
     * @param orgList 所有组织集合
     * @param id      当前组织ID
     * @param type true返回加入自己，false不加入
     */
    private static List<OrgVO> getSonOrg(List<OrgVO> orgList, Long id,Boolean type) throws Exception{
        List<OrgVO> sonOrgs = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        OrgVO organization = null ;
        Iterator<OrgVO> iter = orgList.iterator();
        while(iter.hasNext()){
            OrgVO org = iter.next();
            if (Objects.equals(org.getId(),id)) {
                organization = org;
            }
            //查找除自己外第一层
            if(ObjectUtils.isNotEmpty(org.getParentId())){
                if (Objects.equals(org.getParentId(),id)) {
                    sonOrgs.add(org);
                    ids.add(org.getId());
                    iter.remove();
                }
            }
        }
        List<OrgVO> recursion = recursion(orgList,ids,sonOrgs);
        if (type) {
            recursion.add(organization);
        }

        return recursion;
    }

    /**
     * @author wyf
     * @description  递归方法查询该层组织及其存在的下级所有组织
     * @date  2019/4/12 下午 4:12
     * @param orgList  总组织列表
     * @param ids       父级id集合
     * @param sonOrgList    子组织列表
     * @return java.util.List<com.yjp.erp.model.po.system.Organization>
     */
    private static List<OrgVO> recursion(List<OrgVO> orgList, List<Long> ids, List<OrgVO> sonOrgList) throws Exception{
        if (ids.size() > 0) {
            List<Long> newIds = new ArrayList<>();

            Iterator<OrgVO> iter = orgList.iterator();
            while(iter.hasNext()){
                OrgVO org = iter.next();
                for (Long id : ids) {
                    if(ObjectUtils.isNotEmpty(org.getParentId())){
                        Long pId = org.getParentId();
                        if (Objects.equals(pId,id)) {
                            sonOrgList.add(org);
                            ids.add(org.getId());
                            iter.remove();
                        }
                    }
                }
            }
            return recursion(orgList,newIds,sonOrgList);
        } else {
            return sonOrgList;
        }
    }

    /**
     * @author wyf
     * @description  获取当前用户基本组织
     * @date  2019/4/13 下午 7:49
     * @param userId 用户id
     * @return com.yjp.erp.model.po.system.Organization
     */
    public List<Organization> getBaseOrg(Long userId) throws Exception{
        List<OrgVO> orgVOList = listNormalOrg(userId);
        List<Long> vIds = orgVOList.stream().map(OrgVO::getId).collect(Collectors.toList());
        return getBOrgByVId(vIds);
    }

    /**
     * @author wyf
     * @description  获取当前用户业务组织
     * @date  2019/4/13 下午 7:49
     * @param userId 用户id
     */
    public List<OrgVO> getBusinessOrg(Long userId) throws Exception{
        List<OrgVO> orgVOList = getAllVOrgByUserId(userId);
        List<OrgVO> list = new ArrayList<>();
        if (ObjectUtils.isEmpty(orgVOList)) {
            return list;
        }
        for (OrgVO orgVO : orgVOList) {
           if (orgVO.getOrgType() == SystemConstant.ORG_TYPE_ONE) {
               list.add(orgVO);
           }
        }
        return list;
    }

    /**
     * @author wyf
     * @description  获取当前用户财务组织
     * @date  2019/4/13 下午 7:49
     * @param userId 用户id
     */
    public List<OrgVO> getFinanceOrg(Long userId) throws Exception{
        List<OrgVO> orgVOList = getAllVOrgByUserId(userId);
        List<OrgVO> list = new ArrayList<>();
        if (ObjectUtils.isEmpty(orgVOList)) {
            return list;
        }
        for (OrgVO orgVO : orgVOList) {
            if (orgVO.getOrgType() == SystemConstant.ORG_TYPE_TWO) {
                list.add(orgVO);
            }
        }
        return list;
    }

    /**
     * @author wyf
     * @description   根据用户id查询其基本组织
     * @date  2019/4/23 下午 3:49
     * @param userId 用户id
     * @return java.util.List<com.yjp.erp.model.po.system.Organization>
     */
    public List<Organization> getBaseOrgByUserId(Long userId) throws Exception{

        List<OrgVO> orgVOList = listOrgBydUserId(userId);
        List<Long> vIds = new ArrayList<>();
        for (OrgVO orgVO : orgVOList) {
            vIds.add(orgVO.getId());
        }
        List<Long> bIds = new ArrayList<>();
        List<BaseRelateOrg> baseRelateOrgs = baseOrgRelateOrgMapper.listOrgRelateByVOrgIds(vIds);
        for (BaseRelateOrg baseRelateOrg : baseRelateOrgs) {
            bIds.add(baseRelateOrg.getBaseOrgId());
        }
        return getOrgByBIds(bIds);
    }

    /**
     * @author wyf
     * @description       根据用户集合查询用户组织关系
     * @date  2019/5/21 下午 3:44
     * @param userList 用户集合
     * @return java.util.List<com.yjp.erp.model.po.system.UserOrg>
     */
    List<UserOrg> listUserOrgByUserList(List<UserVO> userList) throws Exception{
        return userOrgMapper.listUserOrgByUserList(userList);
    }

    /**
     * @author wyf
     * @description       根据组织名模糊定位组织
     * @date  2019/4/18 下午 8:25
     * @param name 组织名
     * @param orgType 组织类型
     * @return java.util.List<java.lang.Long>
     */
    public List<String> orgLocation(String name,String orgType) throws Exception{

        List<OrgVO> orgVOList = listOrg(2,orgType).getList();
        List<String> idlist = new ArrayList<>();
        for (OrgVO orgVO : orgVOList) {
            if (orgVO.getName().contains(name)) {
                if (Objects.equals(orgVO.getParentId(),SystemConstant.ORG_PARENT_ID)) {
                    continue;
                }
                Long parentId = orgVO.getParentId();
                idlist.add(parentId.toString());
                recurOrgLocation(orgVOList,idlist,parentId);

            }
        }
        //去重
        LinkedHashSet<String> set = new LinkedHashSet<>(idlist.size());
        set.addAll(idlist);
        idlist.clear();
        idlist.addAll(set);
        return idlist;
    }

    private void recurOrgLocation(List<OrgVO> orgVOList,List<String> idlist,Long id) throws Exception{

        for (OrgVO orgVO : orgVOList) {
            if (Objects.equals(id,orgVO.getId())) {
                Long parentId = orgVO.getParentId();
                if (Objects.equals(parentId,SystemConstant.ORG_PARENT_ID)) {
                    continue;
                }
                idlist.add(parentId.toString());
                recurOrgLocation(orgVOList,idlist,parentId);
            }
        }
    }

    /**
     * @author wyf
     * @description      切换组织
     * @date  2019/5/14 下午 8:38
     * @return com.yjp.erp.conf.UserInfo
     */
    public UserInfo changeOrg(String orgId) throws Exception{

        List<Long> orgIds = Collections.singletonList(Long.valueOf(orgId));
        List<Organization> orgByVIds = getOrgByVIds(orgIds);
        List<OrgVO> vOrgVOList = new ArrayList<>();
        for (Organization orgByVId : orgByVIds) {
            List<OrgVO> list = listVSonOrgByVOrgId(orgByVId.getId());
            vOrgVOList.addAll(list);
        }

        List<OrgVO> vOrgVOList1 = new ArrayList<>();
        List<OrgVO> vOrgVOList2 = new ArrayList<>();
        for (OrgVO vOrgVO : vOrgVOList) {
            if (SystemConstant.ORG_TYPE_ONE == vOrgVO.getOrgType()) {
                vOrgVOList1.add(vOrgVO);
            } else if (SystemConstant.ORG_TYPE_TWO == vOrgVO.getOrgType()) {
                vOrgVOList2.add(vOrgVO);
            }
        }

        BeanUtils.copyProperties(orgByVIds,vOrgVOList1);

        UserInfo userInfo = userInfoManager.getUserInfo();
        //设置该用户缓存中的业务组织
        userInfo.setBusinessOrgList(vOrgVOList1);
        //设置该用户缓存中的财务组织
        userInfo.setFinanceOrgList(vOrgVOList2);
        //设置该用户缓存中的基本组织（仓库）
        List<Organization> bOrgByVId = getBOrgByVId(orgIds);
        List<OrgVO> bOrgVOList = new ArrayList<>();
        for (Organization organization : bOrgByVId) {
            OrgVO orgVO = new OrgVO();
            BeanUtils.copyProperties(organization,orgVO);
            bOrgVOList.add(orgVO);
        }
        userInfo.setBaseOrgList(bOrgVOList);

        return userInfo;
    }

    /**
     * @author wyf
     * @description     根据用户id获取其所有的虚拟组织
     * @date  2019/5/21 下午 3:43
     * @param userId 用户id
     * @return java.util.List<com.yjp.erp.model.vo.system.OrgVO>
     */
    public List<OrgVO> getAllVOrgByUserId(Long userId) throws Exception{
        List<OrgVO> list = new ArrayList<>();
        List<OrgVO> orgVOList = listOrgBydUserId(userId);
        for (OrgVO orgVO : orgVOList) {
            List<OrgVO> orgVOs = listVSonOrgByVOrgId(orgVO.getId());
            list.addAll(orgVOs);
        }
        HashSet<OrgVO> orgVOSet = new HashSet<>(list);
        list.clear();
        list.addAll(orgVOSet);
        return list;
    }

    /**
     * @author wyf
     * @description    根据传入的基础组织id查询其层级最低的所属虚拟组织
     * @date  2019/5/18 上午 9:48
     * @param bOrgId 基本组织id
     * @return com.yjp.erp.model.vo.system.OrgVO
     */
    Organization getVOrgByBOrgId(String bOrgId) throws Exception{

        //查询应用该基本组织的所有虚拟组织
        List<BaseRelateOrg> baseRelateOrgs = baseOrgRelateOrgMapper.listVOrgRelateByBOrgId(Long.valueOf(bOrgId));
        List<Long> vIds = baseRelateOrgs.stream().map(BaseRelateOrg::getVirtualOrgId).collect(Collectors.toList());
        List<Organization> vOrgs = getOrgByVIds(vIds);
        //找出层级最低的
        for (Organization vOrg : vOrgs) {
            //选择非叶子节点
            if (Objects.equals(vOrg.getBaseNode(),SystemConstant.LEAF_NOTE)
                    //选择业务组织
                    && Objects.equals(vOrg.getOrgType(),SystemConstant.ORG_TYPE_ONE)) {
                return vOrg;
            }
        }
        return new Organization();
    }
}