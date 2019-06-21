package com.yjp.erp.service.activiti.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yjp.erp.conf.UserInfoManager;
import com.yjp.erp.mapper.activiti.ReviewerBindingMapper;
import com.yjp.erp.model.dto.activiti.ReviewerBindingDTO;
import com.yjp.erp.model.po.activiti.ReviewerBinding;
import com.yjp.erp.model.po.system.Organization;
import com.yjp.erp.model.po.system.User;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.model.vo.ReturnDataVO;
import com.yjp.erp.model.vo.activiti.ReviewerBindingVO;
import com.yjp.erp.service.MultiThreadService;
import com.yjp.erp.service.activiti.IReviewerBindingService;
import com.yjp.erp.util.FormatReturnEntity;
import com.yjp.erp.util.ObjectUtils;
import com.yjp.erp.util.SnowflakeIdWorker;
import org.springframework.beans.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 审核人绑定表 服务实现类
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-04
 */
@Service
public class ReviewerBindingServiceImpl extends ServiceImpl<ReviewerBindingMapper, ReviewerBinding> implements IReviewerBindingService {

    private static final Logger log = LoggerFactory.getLogger(ReviewerBindingServiceImpl.class);

    private final MultiThreadService multiThreadService;

    private final UserInfoManager userInfoManager;

    @Autowired
    public ReviewerBindingServiceImpl(MultiThreadService multiThreadService, UserInfoManager userInfoManager) {
        this.multiThreadService = multiThreadService;
        this.userInfoManager = userInfoManager;
    }

    @Override
    public Boolean insertReviewerBinding(ReviewerBindingDTO reviewerBindingDTO) throws InvocationTargetException, IllegalAccessException {
        ReviewerBinding reviewerBinding = new ReviewerBinding();
        BeanUtils.copyProperties(reviewerBinding, reviewerBindingDTO);
        reviewerBinding.setId(SnowflakeIdWorker.nextId());
        exchangeData(reviewerBindingDTO, reviewerBinding);
        return this.insert(reviewerBinding);
    }

    @Override
    public Boolean updateReviewerBinding(ReviewerBindingDTO reviewerBindingDTO) throws InvocationTargetException, IllegalAccessException {
        ReviewerBinding reviewerBinding = new ReviewerBinding();
        BeanUtils.copyProperties(reviewerBinding, reviewerBindingDTO);
        exchangeData(reviewerBindingDTO, reviewerBinding);
        return this.updateById(reviewerBinding);
    }

    @Override
    public Boolean deleteReviewerBinding(Long id) {
        return this.deleteById(id);
    }

    @Override
    public JsonResult<Page<ReviewerBindingVO>> listReviewerBindingPage(ReviewerBindingDTO reviewerBindingDTO) throws InvocationTargetException, IllegalAccessException {
        Page<ReviewerBinding> page = new Page<>(reviewerBindingDTO.getPageNo(), reviewerBindingDTO.getPageSize());
        EntityWrapper<ReviewerBinding> ew = new EntityWrapper<>();
        ReviewerBinding reviewerBinding = new ReviewerBinding();
        BeanUtils.copyProperties(reviewerBinding, reviewerBindingDTO);
        reviewerBinding.setOrgId(reviewerBindingDTO.getOrgId());
        ew.setEntity(reviewerBinding);
        ew.orderDesc(Collections.singletonList("create_date"));

        Page<ReviewerBinding> pageList = this.selectPage(page, ew);
        List<ReviewerBinding> list = pageList.getRecords();
        //组织或区域id集合
        List<Long> orgIds = list.stream().map(ReviewerBinding::getOrgId).distinct().collect(Collectors.toList());
        //创建人
        List<Long> userIds = list.stream().map(ReviewerBinding::getCreator).distinct().collect(Collectors.toList());

        //一级审核组
        List<String> oneLevels = list.stream().map(ReviewerBinding::getFirstLevelGroup).distinct().collect(Collectors.toList());
        transferList(userIds, oneLevels);
        //二级审核组
        List<String> twoLevels = list.stream().map(ReviewerBinding::getSecondLevelGroup).distinct().collect(Collectors.toList());
        transferList(userIds, twoLevels);
        //三级审核组
        List<String> threeLevels = list.stream().map(ReviewerBinding::getThirdLevelGroup).distinct().collect(Collectors.toList());
        transferList(userIds, threeLevels);
        //用户id集合
        userIds = userIds.stream().distinct().collect(Collectors.toList());
        ReturnDataVO returnDataVO = multiThreadService.getBaseDataMap(null, userIds, orgIds);
        Map<Long, User> userMap = returnDataVO.getUserMap();
        Map<Long, Organization> areaMap = returnDataVO.getOrgMap();
        Page<ReviewerBindingVO> formatReviewerBindingPageList = FormatReturnEntity.formatReviewerBindingPageList(pageList, areaMap, userMap);
        return RetResponse.makeOKRsp(formatReviewerBindingPageList);
    }


    @Override
    public JsonResult<ReviewerBindingVO> viewReviewerBinding(Long id) {
        ReviewerBinding reviewerBinding = this.selectById(id);
        List<Long> userIds = new ArrayList<>();
        userIds.add(reviewerBinding.getCreator());
        transferString(userIds, reviewerBinding.getFirstLevelGroup());
        transferString(userIds, reviewerBinding.getSecondLevelGroup());
        transferString(userIds, reviewerBinding.getThirdLevelGroup());

        userIds = userIds.stream().distinct().collect(Collectors.toList());
        ReturnDataVO returnDataVO = multiThreadService.getBaseDataMap(null, userIds, Collections.singletonList(reviewerBinding.getOrgId()));
        Map<Long, User> userMap = returnDataVO.getUserMap();
        Map<Long, Organization> areaMap = returnDataVO.getOrgMap();
        ReviewerBindingVO formatReviewerBinding = FormatReturnEntity.formatReviewerBinding(reviewerBinding, areaMap, userMap);
        return RetResponse.makeOKRsp(formatReviewerBinding);
    }

    private void transferList(List<Long> userIds, List<String> threeLevels) {
        for (String levelThree : threeLevels) {
            if (StringUtils.isNotBlank(levelThree)) {
                String[] threeArr = levelThree.split(",");
                for (String v : threeArr) {
                    userIds.add(Long.valueOf(v));
                }
            }
        }
    }

    private void transferString(List<Long> userIds, String second) {
        if (StringUtils.isNotBlank(second)) {
            String[] secondArr = second.split(",");
            for (String v : secondArr) {
                userIds.add(Long.valueOf(v));
            }
        }
    }

    private void exchangeData(ReviewerBindingDTO reviewerBindingDTO, ReviewerBinding reviewerBinding) {
        reviewerBinding.setCreateDate(new Date());
        reviewerBinding.setCreator(Long.valueOf(userInfoManager.getUserInfo().getUserId()));
        String[] firstLevelGroupArr = ObjectUtils.filterArray(reviewerBindingDTO.getFirstLevelGroup());
        reviewerBinding.setFirstLevelGroup(StringUtils.join(firstLevelGroupArr, ","));
        String[] secondLevelGroupArr = ObjectUtils.filterArray(reviewerBindingDTO.getSecondLevelGroup());
        reviewerBinding.setSecondLevelGroup(StringUtils.join(secondLevelGroupArr, ","));
        String[] thirdLevelGroupArr = ObjectUtils.filterArray(reviewerBindingDTO.getThirdLevelGroup());
        reviewerBinding.setThirdLevelGroup(StringUtils.join(thirdLevelGroupArr, ","));
        reviewerBinding.setOrgId(reviewerBindingDTO.getOrgId());
    }
}
