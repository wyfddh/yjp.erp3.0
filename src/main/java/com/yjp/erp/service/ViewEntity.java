package com.yjp.erp.service;

import com.yjp.erp.constants.XmlConstant;
import com.yjp.erp.model.dto.dbxml.ViewToXmlFile;
import com.yjp.erp.model.dto.dbxml.XmlFileRoules;
import com.yjp.erp.conf.exception.BaseException;
import com.yjp.erp.mapper.ViewFieldMapper;
import com.yjp.erp.mapper.ViewParentMapper;
import com.yjp.erp.mapper.ViewSonMapper;
import com.yjp.erp.model.po.view.ViewField;
import com.yjp.erp.model.po.view.ViewParent;
import com.yjp.erp.model.po.view.ViewSon;
import com.yjp.erp.service.dbxml.DbToXml;
import com.yjp.erp.util.DomUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.dom.DOMDocument;
import org.dom4j.dom.DOMElement;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wyf
 * @description  将视图转化成xml
 * @date  2019/3/26 下午 5:59
 **/
@Service("viewEntity")
public class ViewEntity implements DbToXml {

    private final ViewParentMapper viewParentMapper;

    private final ViewSonMapper viewSonMapper;

    private final ViewFieldMapper viewFieldMapper;

    public ViewEntity (ViewParentMapper viewParentMapper,ViewSonMapper viewSonMapper,ViewFieldMapper viewFieldMapper) {
        this.viewParentMapper = viewParentMapper;
        this.viewSonMapper = viewSonMapper;
        this.viewFieldMapper = viewFieldMapper;
    }


    @Override
    public Object getDBData(Long id) {
        Map<Long,List<ViewField>> map = new HashMap<>(16);
        ViewToXmlFile viewToXmlFile = new ViewToXmlFile();
        //获取视图主表数据
        ViewParent viewParent = viewParentMapper.getViewParentById(id);
        //获取视图子表数据
        List<ViewSon> viewSonList = viewSonMapper.getViewSonByParentId(id);
        for (ViewSon viewSon : viewSonList) {
            Long viewSonId = viewSon.getId();
            //获取视图子表列数据
            List<ViewField> viewFieldList = viewFieldMapper.getViewFieldBySonId(viewSonId);
            map.put(viewSonId,viewFieldList);
        }
        viewToXmlFile.setViewField(map);
        viewToXmlFile.setViewSonList(viewSonList);
        viewToXmlFile.setViewParent(viewParent);
        
        return viewToXmlFile;
    }


    @Override
    public void dataToXmlFile(Object data, XmlFileRoules xmlFileRoules) throws BaseException {
        ViewToXmlFile viewToXmlFile = (ViewToXmlFile)data;
        //生成dom结构
        Document dom = new DOMDocument();
        Element entities = DomUtils.getRootElement(XmlConstant.XML_LABEL_ENTITY_ROOT, XmlConstant.XML_HEAD_ENTITY_VALUE);
        dom.add(entities);
        addEntity(viewToXmlFile,entities);

        String entityFileName = viewToXmlFile.getViewParent().getEntityName() + ".xml";
        DomUtils.outputDom(dom, xmlFileRoules.getViewFileName(), entityFileName);
    }
    private void addEntity (ViewToXmlFile viewData,Element entities) {
        Element entity = new DOMElement(XmlConstant.XML_LABEL_VIEW_ENTITY);
        //最外层数据
        ViewParent viewParent = viewData.getViewParent();
        entity.addAttribute("entity-name",viewParent.getEntityName());
        entity.addAttribute("package",viewParent.getPackagePath());
        entity.addAttribute("authorize-skip","true");
        //内层
        List<ViewSon> viewSonList = viewData.getViewSonList();
        Map<Long, List<ViewField>> viewField = viewData.getViewField();
        if (null != viewSonList && viewSonList.size() > 0) {
            for (ViewSon viewSon : viewSonList) {
                Element fields = new DOMElement(XmlConstant.XML_LABEL_VIEW_MEMBER_ENTITY);
                fields.addAttribute("entity-alias",viewSon.getEntityAlias());
                fields.addAttribute("entity-name",viewSon.getEntityName());
                if (StringUtils.isNotBlank(viewSon.getJoinFromAlias())) {
                    fields.addAttribute("join-from-alias",viewSon.getJoinFromAlias());
                    DOMElement keyMap = new DOMElement(XmlConstant.XML_LABEL_ENTITY_KEY_MAP);
                    keyMap.addAttribute("field-name",viewSon.getEntityParentValue());
                    keyMap.addAttribute("related",viewSon.getEntityValue());
                    fields.add(keyMap);
                }
                entity.add(fields);
                //alias-all暂时不考虑

                List<ViewField> viewFieldList = viewField.get(viewSon.getId());
                for (ViewField field : viewFieldList) {
                    DOMElement alias = new DOMElement(XmlConstant.XML_LABEL_VIEW_ALIAS);
                    alias.addAttribute("entity-alias",field.getAlias());
                    alias.addAttribute("name",field.getName());
                    alias.addAttribute("field",field.getField());
                    entity.add(alias);
                }
            }
        }
        entities.add(entity);
    }

    @Override
    public void analysisData(Long id, XmlFileRoules xmlFileRoules) throws BaseException {
         dataToXmlFile(getDBData(id), xmlFileRoules);
    }


}
