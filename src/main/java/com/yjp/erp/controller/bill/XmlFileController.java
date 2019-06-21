package com.yjp.erp.controller.bill;

import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.model.dto.dbxml.BillActionToXmlFile;
import com.yjp.erp.model.dto.dbxml.XmlFileRoules;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.service.dbxml.DbToXml;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 单据配置相关接口
 *
 * @author wuyizhe@yijiupi.cn
 * @date 2019/3/21
 */
@RestController
@Slf4j
@RequestMapping(value = "/xml")
@Api(tags = "单据配置相关接口")
public class XmlFileController {

    @Resource
    @Qualifier("entity")
    private DbToXml entity;

    @Resource
    @Qualifier("service")
    private DbToXml service;

    @Resource
    @Qualifier("action")
    private DbToXml action;


    /**
     * 将实体元数据解析为xml
     *
     * @return 返回解析的xml
     * @throws Exception http请求异常
     */
    @GetMapping("/entityXml")
    public JsonResult test() throws Exception {
        XmlFileRoules xmlFileRoules = new XmlFileRoules(new Module());
        Object dbData = entity.getDBData(1L);
        entity.dataToXmlFile(dbData, xmlFileRoules);
        return RetResponse.makeOKRsp(dbData);
    }

    /**
     * 将动作元数据解析为xml
     *
     * @return 返回解析的xml
     * @throws Exception http请求异常
     */
    @GetMapping("/actionXml")
    public JsonResult test2() throws Exception {
        Object dbData = action.getDBData(1L);
        XmlFileRoules xmlFileRoules = new XmlFileRoules(new Module());
        action.dataToXmlFile(dbData, xmlFileRoules);
        BillActionToXmlFile data = (BillActionToXmlFile) dbData;
        service.dataToXmlFile(data.getServices(), xmlFileRoules);
        return RetResponse.makeOKRsp(dbData);
    }
}
