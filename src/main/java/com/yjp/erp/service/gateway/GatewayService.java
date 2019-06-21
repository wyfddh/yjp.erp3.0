package com.yjp.erp.service.gateway;

import com.yjp.erp.conf.UserInfoManager;
import com.yjp.erp.conf.exception.BusinessException;
import com.yjp.erp.constants.FieldWebElementEnum;
import com.yjp.erp.mapper.BillMapper;
import com.yjp.erp.mapper.EnumMapper;
import com.yjp.erp.mapper.ViewParentMapper;
import com.yjp.erp.mapper.base.BaseEntityMapper;
import com.yjp.erp.mapper.parsexml.ViewEntityMapper;
import com.yjp.erp.model.po.bill.Bill;
import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.po.view.ViewEntity;
import com.yjp.erp.model.vo.RetCode;
import com.yjp.erp.service.ModuleService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * description:
 *
 * @author liushui
 * @date 2019/4/10
 */
@Service
public class GatewayService {

    @Autowired(required = false)
    private BillMapper billMapper;

    @Autowired(required = false)
    private BaseEntityMapper baseEntityMapper;

    @Autowired
    private ViewParentMapper viewParentMapper;

    @Autowired
    private EnumMapper enumMapper;

    @Autowired
    private UserInfoManager userInfoManager;

    @Autowired
    private ViewEntityMapper viewEntityMapper;

    @Autowired
    private ModuleService moduleService;

    /**
     * 根据classId和typeId获取实体表单等名称
     */
    public Map<String, Object> getEntityNames(String classId, String typeId) throws Exception {
        String packageName = userInfoManager.getUserInfo().getCompanySpace();
        Map<String, Object> retMap = commonGetEntityNames(classId, typeId, packageName);
        return retMap;
    }


    public Map<String, Object> commonGetEntityNames(String classId, String typeId, String packageName) throws
            BusinessException {
        Map<String, Object> retMap = new HashMap<>(4);
        //将前端classId，typeId转换成对应的实体名称，并取出子表名称
        Module module = new Module();
        module.setTypeId(typeId);
        module.setClassId(classId);
        switch (classId) {
            //表单
            case "bill": {
                Bill bill = billMapper.getBillDetail(module);
                if (Objects.isNull(bill)) {
                    throw new BusinessException(RetCode.INTERNAL_SERVER_ERROR, "未查询到相应的表单");
                }
                retMap.put("parentEntity", String.format("%s.%s", packageName, bill.getName()));
                //-1表示该单据为主表，继续查出其子表
                if (Objects.equals(bill.getParentId(), -1L)) {
                    List<Bill> child = billMapper.getChildrenBillByParentId(bill.getId());
                    if (CollectionUtils.isNotEmpty(child)) {
                        retMap.put("childEntity", String.format("%s.%s", packageName, child.get(0).getName()));
                    }
                }
                break;
            }
            //实体
            case "entity": {
                Bill entity = baseEntityMapper.getBillDetail(module);
                retMap.put("parentEntity", String.format("%s.%s", packageName, entity.getName()));
                //-1表示该单据为主表，继续查出其子表
                if (Objects.equals(entity.getParentId(), -1L)) {
                    List<Bill> child = baseEntityMapper.getChildrenBillByParentId(entity.getId());
                    if (CollectionUtils.isNotEmpty(child)) {
                        retMap.put("childEntity", String.format("%s.%s", packageName, child.get(0).getName()));
                    }
                }
                break;
            }
            //视图
            case "view": {
                Module realModule = moduleService.getModuleByClassIdAndTypeId(module);
                ViewEntity visss = viewEntityMapper.getViewEntityByModuleId(realModule.getId());
                String viewName = viewEntityMapper.getViewEntityByModuleId(realModule.getId()).getEntityName();
                retMap.put("parentEntity", String.format("%s.%s", packageName, viewName));
                break;
            }
            //枚举
            case "enum": {
                String enumName = enumMapper.getEnumNameByClassIdAndTypeId(module);
                retMap.put("parentEntity", String.format("%s.%s", packageName, enumName));
                retMap.put("typeId", typeId);
                break;
            }
            default:
                throw new BusinessException(RetCode.ERROR_CLASSID, "classId类型错误");
        }

        return retMap;
    }

    /**
     * 根据classId和typeId获取实体表单等过滤字段
     */
    public List<Map<String, Object>> getFilterFields(String classId, String typeId) throws Exception {

        List<Map<String, Object>> retList = new ArrayList<>();
        Map<String, Object> param = new HashMap<>(4);
        //实体定位属性
        param.put("classId", classId);
        param.put("typeId", typeId);
        //要查询字段的属性
        param.put("fieldElement", FieldWebElementEnum.ELEMENT_FILTER.getValue());
        switch (classId) {
            //表单
            case "bill": {
                retList = billMapper.getFilterFields(param);
                break;
            }
            //实体
            case "entity": {
                retList = baseEntityMapper.getFilterFields(param);
                break;
            }
            case "view": {
//                retList = viewEntityMapper.getFilterFields(param);
            }
            //todo 后续要增加其他类型实体
            default:
                throw new BusinessException(RetCode.ERROR_CLASSID, "classId类型错误");
        }

        return retList;
    }

    /**
     * 根据classId和typeId获取实体表单等字段条件
     */
    public List<Map<String, Object>> listFieldRules(String classId, String typeId) throws Exception {

        List<Map<String, Object>> retList = new ArrayList<>();
        Map<String, Object> param = new HashMap<>(4);
        //实体定位属性
        param.put("classId", classId);
        param.put("typeId", typeId);
        switch (classId) {
            //表单
            case "bill": {
                retList = billMapper.listFieldRules(param);
                break;
            }
            //实体
            case "entity": {
                retList = baseEntityMapper.listFieldRules(param);
                break;
            }
            //todo 后续要增加其他类型实体
            default:
                throw new BusinessException(RetCode.ERROR_CLASSID, "classId类型错误");
        }

        return retList;
    }
}
