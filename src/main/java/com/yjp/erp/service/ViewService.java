package com.yjp.erp.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yjp.erp.constants.EntityClassEnum;
import com.yjp.erp.constants.ModuleConstant;
import com.yjp.erp.constants.SystemConstant;
import com.yjp.erp.model.domain.ViewDO;
import com.yjp.erp.model.dto.view.ViewDTO;
import com.yjp.erp.conf.exception.BusinessException;
import com.yjp.erp.mapper.ViewFieldMapper;
import com.yjp.erp.mapper.ViewParentMapper;
import com.yjp.erp.mapper.ViewSonMapper;
import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.po.view.View;
import com.yjp.erp.model.po.view.ViewField;
import com.yjp.erp.model.po.view.ViewParent;
import com.yjp.erp.model.po.view.ViewSon;
import com.yjp.erp.util.HttpClientUtils;
import com.yjp.erp.util.ObjectUtils;
import com.yjp.erp.model.vo.RetCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author wyf
 * @date 2019/3/28 上午 10:55
 **/
@Service
public class ViewService {

    @Autowired
    private ViewFieldMapper viewFieldMapper;
    @Autowired
    private ViewParentMapper viewParentMapper;
    @Autowired
    private ViewSonMapper viewSonMapper;

    @Autowired
    private BaseEntityService baseEntityService;
    @Autowired
    private BillService billService;

    @Value("${moqui.base.rest}")
    private String baseRest;

    public void insertView(ViewDO viewDO) throws Exception {

        if (CollectionUtils.isNotEmpty(viewDO.getViewSonList())) {
            viewSonMapper.insertList(viewDO.getViewSonList());
        }
        if (CollectionUtils.isNotEmpty(viewDO.getViewFieldList())) {
            viewFieldMapper.insertList(viewDO.getViewFieldList());
        }
        if (ObjectUtils.isNotEmpty(viewDO.getViewParent())) {
            viewParentMapper.insert(viewDO.getViewParent());
        }
    }

    public ViewParent getViewByModuleId(Long moduleId) {

        return viewParentMapper.getViewByModuleId(moduleId);
    }

    public List<ViewSon> getViewSonByParentId(Long parentId) {

        return viewSonMapper.getViewSonByParentId(parentId);
    }

    public List<ViewField> getViewFieldByParentId(Long parentId) {

        return viewFieldMapper.getViewFieldByParentId(parentId);
    }

    public List<ViewSon> getAllViewSon() {

        return viewSonMapper.getAllViewSon();
    }

    public List<ViewField> getAllViewField() {

        return viewFieldMapper.getAllViewField();
    }

    public int countViewByMap(Map<String, Object> map) {

        return viewParentMapper.countViewByMap(map);
    }

    public List<Map<String, Object>> getViewByMap(Map<String, Object> map) {

        return viewParentMapper.getViewByMap(map);
    }

    /**
     * 根据实体类型获取所有的实体数据
     *
     * @param classId 实体classId
     * @return 返回所有视图实体数据
     */
    public List<Map<String, Object>> listViews(String classId) {
        return viewParentMapper.listViews(classId);
    }

    /**
     * @param viewDTO view查询入参
     * @author wyf
     * @description 分页查询视图结果
     * @date 2019/4/25 下午 3:54
     */
    public Map<String, Object> pageListView(ViewDTO viewDTO) throws Exception {

        Integer pageNo = viewDTO.getPage().getMoquiStart();
        Integer pageSize = viewDTO.getPage().getPageSize();
        String viewName = viewDTO.getPackagePath() + "." + viewDTO.getEntityName();

        String restParentName = viewDTO.getRestParentName();
        String restSonName = viewDTO.getRestSonName();
        String restType = viewDTO.getRestType();
        String url = baseRest + "/" + restParentName + "/" + restSonName;
        String result = "";
        if (Objects.equals(restType.toUpperCase(), SystemConstant.GET)) {
            url = url + "?pageNo=" + pageNo + "&pageSize=" + pageSize + "&viewName=" + viewName;
            result = HttpClientUtils.get(url);
        } else if (Objects.equals(restType.toUpperCase(), SystemConstant.POST)) {
            View view = new View();
            view.setPageNo(pageNo);
            view.setPageSize(pageSize);
            view.setViewName(viewName);
            result = HttpClientUtils.postSimple(url, view);
        }
        if (ObjectUtils.isEmpty(result)) {
            throw new BusinessException(RetCode.PARAM_EMPTY, "参数为空");
        }
        JSONObject jsonObject = JSON.parseObject(result);
        int code = jsonObject.getIntValue("code");
        if (!Objects.equals(code, SystemConstant.SUCCESS_CODE)) {
            throw new BusinessException(RetCode.MOQUI_ERROR, "moqui服务异常");
        }
        JSONObject data = jsonObject.getJSONObject("data");
        Map<String, Object> map = new HashMap<>(16);
        if (ObjectUtils.isNotEmpty(data)) {
            map = data;
        }
        return map;
    }

    /**
     * @param entityName 实体名
     * @author wyf
     * @description 根据实体名获取其字段列表
     * @date 2019/4/25 下午 4:49
     */
    public List<Map<String, Object>> getFieldsByEntityName(String entityName) throws Exception {

        //根据实体名查询其classid和typeid

        Map<String, String> map = viewParentMapper.getFieldsByEntityName(entityName);

        Module module = new Module();
        module.setClassId(map.get("classId"));
        module.setTypeId(map.get("typeId"));
        List<Map<String, Object>> maps;
        if (Objects.equals(EntityClassEnum.BILL_CLASS.getValue(), map.get(ModuleConstant.CLASS_ID))) {
            maps = billService.getBillFields(module);
        } else {
            maps = baseEntityService.listFields(module);
        }
        return maps;
    }
}
