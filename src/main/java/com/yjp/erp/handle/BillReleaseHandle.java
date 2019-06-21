package com.yjp.erp.handle;

import com.yjp.erp.constants.ModuleConstant;
import com.yjp.erp.constants.ModuleEnum;
import com.yjp.erp.model.dto.dbxml.BillActionToXmlFile;
import com.yjp.erp.model.dto.dbxml.BillEecaToXmlFile;
import com.yjp.erp.model.dto.dbxml.XmlFileRoules;
import com.yjp.erp.conf.exception.BaseException;
import com.yjp.erp.mapper.*;
import com.yjp.erp.mapper.base.BaseEntityMapper;
import com.yjp.erp.model.po.bill.Bill;
import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.po.eeca.BillEeca;
import com.yjp.erp.model.po.service.BillAction;
import com.yjp.erp.service.dbxml.DbToXml;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wuyizhe@yijiupi.cn
 * @date 2019/3/27
 */
@Slf4j
@Component
public class BillReleaseHandle {

    private final ModuleMapper moduleMapper;

    private final BillMapper billMapper;

    private final BaseEntityMapper baseEntityMapper;

    private final BillActionMapper billActionMapper;

    private final BillEecaMapper billEecaMapper;

    private final ScriptHand scriptHand;

    @Resource
    @Qualifier("entity")
    private DbToXml entity;

    @Resource
    @Qualifier("service")
    private DbToXml service;

    @Resource
    @Qualifier("action")
    private DbToXml action;

    @Resource
    @Qualifier("eeca")
    private DbToXml eeca;

    @Autowired(required = false)
    public BillReleaseHandle(ModuleMapper moduleMapper, BillMapper billMapper, BaseEntityMapper baseEntityMapper, BillActionMapper billActionMapper, BillEecaMapper billEecaMapper, ScriptHand scriptHand) {
        this.moduleMapper = moduleMapper;
        this.billMapper = billMapper;
        this.baseEntityMapper = baseEntityMapper;
        this.billActionMapper = billActionMapper;
        this.billEecaMapper = billEecaMapper;
        this.scriptHand = scriptHand;
    }

    public void dbXmlAllDataRelease() {
        List<Module> modules = moduleMapper.listAllAvailableModules();
        modules.forEach(module -> {
            if (ModuleConstant.CLASS_ENUM.equals(module.getClassId())) {
                return;
            }

            try {
                dbXmlDataRelease(module);
            } catch (BaseException e) {
                throw new RuntimeException(e + "在转db转xml文件时   classId:" + module.getClassId() + "  typeId:" + module.getTypeId() + "  这个module转化失败");
            }
        });
    }

    /**
     * 把实体及其相关的配置转成xml
     * @param module 实体信息
     * @throws BaseException 异常
     */
    @SuppressWarnings("unchecked")
    public void dbXmlDataRelease(Module module) throws BaseException {
        Assert.notNull(module);
        module = moduleMapper.getModuleByClassIdAndTypeId(module);
        if (null == module || ModuleEnum.UN_USEFUL.value == module.getActiveState()) {
            throw new RuntimeException("未发现classId:" + module.getClassId() + "  typeId:" + module.getTypeId() + " 这样的可用module");
        }

        Bill bill = getBill(module);
       if(null==bill){
           log.info("通过这个moduel取得的bill为null-----"+module);
       }
        List actionListData = new ArrayList();
        List serviceListData = new ArrayList();

        List<BillAction> billActions = billActionMapper.getBillActionByModuleId(module.getId());
        XmlFileRoules xmlFileRoules = new XmlFileRoules(module, bill.getName());

        List<BillEeca> billEecas = billEecaMapper.getBillEecaByModuleId(module.getId());
        entity.analysisData(bill.getId(), xmlFileRoules);
        //把一个实体的所有的action 和 service分别放入集合中
        billActions.forEach(billAction -> {
            Object dbData = action.getDBData(billAction.getId());
            actionListData.add(dbData);
            serviceListData.add(((BillActionToXmlFile) dbData).getServices());
        });

        action.dataToXmlFile(actionListData, xmlFileRoules);
        dataToXmlListService(serviceListData, xmlFileRoules);
        if (ModuleConstant.CLASS_BILL.equals(module.getClassId())) {
            dataToXmlListEeca(xmlFileRoules, billEecas);
            dataToXmlPushOrWriteBackEeca(module);
        }
    }

    /**
     * 根据module来生成 下推需要的xml组件
     *
     * @param module 实体信息
     */
    private void dataToXmlPushOrWriteBackEeca(Module module) {
        Assert.notNull(module);
        Assert.notNull(module.getId());
        try {
            scriptHand.createScriptByClassIdAndTypeId(module.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 通过 module 获取bill
     *
     * @param module 实体信息
     * @return 返回单据或者实体
     */
    private Bill getBill(Module module) {
        Bill bill;
        if (ModuleConstant.CLASS_ENTITY.equals(module.getClassId())) {
            bill = baseEntityMapper.getBillByModuleId(module.getId());
        } else {
            bill = billMapper.getBillByModuleId(module.getId());
        }
        return bill;
    }

    /**
     * 生成一个实体下的多个eeca
     *
     * @param xmlFileRoules xml文件生成的路径规则
     * @param billEecas     行为数据
     */
    private void dataToXmlListEeca(XmlFileRoules xmlFileRoules, List<BillEeca> billEecas) {
        billEecas.forEach(billEeca -> {
            try {
                Object dbData = eeca.getDBData(billEeca.getId());
                eeca.dataToXmlFile(dbData, xmlFileRoules);
                service.dataToXmlFile(((BillEecaToXmlFile) dbData).getServices(), xmlFileRoules);
            } catch (BaseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * action 下多个xml Service的生成
     *
     * @param serviceListData   服务数据
     * @param xmlFileRoules     xml文件生成的路径规则
     */
    private void dataToXmlListService(List serviceListData, XmlFileRoules xmlFileRoules) {
        serviceListData.forEach(serviceData -> {
            try {
                service.dataToXmlFile(serviceData, xmlFileRoules);
            } catch (BaseException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
