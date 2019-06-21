package com.yjp.erp.service.dbxml;

import com.yjp.erp.model.dto.dbxml.XmlFileRoules;
import com.yjp.erp.conf.exception.BaseException;

/**
 * @Description
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/3/20
 */
public interface DbToXml {

    Object getDBData(Long id);

    /**
     * service、entity、action xml文件的生成
     * @param o
     * @param xmlFileRoules
     * @return
     * @throws BaseException
     */
    void dataToXmlFile(Object o, XmlFileRoules xmlFileRoules) throws BaseException;

    void analysisData(Long id, XmlFileRoules xmlFileRoules) throws BaseException;
}
