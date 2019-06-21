package com.yjp.erp.handle;

import com.yjp.erp.model.dto.dbxml.BillActionToXmlFile;
import com.yjp.erp.model.dto.dbxml.XmlFileRoules;
import com.yjp.erp.conf.exception.BaseException;
import com.yjp.erp.mapper.BillActionMapper;
import com.yjp.erp.mapper.ModuleMapper;
import com.yjp.erp.mapper.ViewParentMapper;
import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.po.service.BillAction;
import com.yjp.erp.model.po.view.ViewParent;
import com.yjp.erp.service.dbxml.DbToXml;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author wyf
 * @date 2019/3/29 下午 7:34
 **/
@Component
public class ViewXmlHandle {
    private final ViewParentMapper viewParentMapper;

    private final ModuleMapper moduleMapper;

    private final BillActionMapper billActionMapper;

    @Resource
    @Qualifier("viewEntity")
    private DbToXml viewEntity;
    @Resource
    @Qualifier("action")
    private DbToXml action;
    @Resource
    @Qualifier("service")
    private DbToXml service;

    public ViewXmlHandle (ViewParentMapper viewParentMapper,ModuleMapper moduleMapper,BillActionMapper billActionMapper) {
        this.viewParentMapper = viewParentMapper;
        this.billActionMapper = billActionMapper;
        this.moduleMapper = moduleMapper;
    }
    public Boolean dbXmlDataRelease(String classId,String typeId) throws BaseException {
        Module module = new Module();
        module.setClassId(classId);
        module.setTypeId(typeId);
        module = moduleMapper.getModuleByClassIdAndTypeId(module);
        // 1 表示可用,0 表示不可用
        if (module.getActiveState() == 1) {
            XmlFileRoules xmlFileRoules = new XmlFileRoules(module);
            ViewParent viewParent = viewParentMapper.getViewByModuleId(module.getId());
            viewEntity.analysisData(viewParent.getId(),xmlFileRoules);
            List<BillAction> billActions = billActionMapper.getBillActionByModuleId(module.getId());
            billActions.forEach(billAction -> {
                try {
                    Object dbData = action.getDBData(billAction.getId());
                    action.dataToXmlFile(dbData, xmlFileRoules);
                    service.dataToXmlFile(((BillActionToXmlFile) dbData).getServices(), xmlFileRoules);
                } catch (BaseException e) {
                    e.printStackTrace();
                }
            });

            return true;
        }
        return false;
    }
}
