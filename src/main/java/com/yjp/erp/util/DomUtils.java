package com.yjp.erp.util;

import com.yjp.erp.constants.EntityConstant;
import com.yjp.erp.constants.XmlConstant;
import com.yjp.erp.conf.exception.BaseException;
import com.yjp.erp.model.vo.RetCode;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.dom.DOMDocument;
import org.dom4j.dom.DOMElement;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @Description
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/3/22
 */
@Component
public class DomUtils {

    private static String xmlPath;

    @Value("${xml.create.path}")
    public void setXmlPath(String xmlPath) {
        DomUtils.xmlPath = xmlPath;
    }

    public static Document getDocument() {
        return new DOMDocument();
    }

    public static Element getElement(String elementName) {
        return new DOMElement(elementName);
    }

    public static Element getRootElement(String elementName, String xmlType) {
        Element root = DomUtils.getElement(elementName);
        root.addAttribute(XmlConstant.XML_HEAD_1, XmlConstant.XML_HEAD_1_VALUE);
        root.addAttribute(XmlConstant.XML_HEAD_2, xmlType);
        return root;
    }

    public static void outputDom(Document dom, String path, String name) throws BaseException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        try {
//            path = xmlPath + path;
            File file1 = new File(path);
            if (!file1.exists()) {
                file1.mkdirs();
            }
            File file = new File(file1, name);
            XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
            writer.setEscapeText(false);
            writer.write(dom);
            writer.close();
        } catch (IOException e) {
            System.err.println(" = " + e.getMessage());
            throw new BaseException(RetCode.FAIL, "输出dom对象失败");
        }
    }

    /**
     * 用于创建实体时,dom输出到xml文件,附加创建 EntityRecord的eeca
     *
     * @param dom
     * @param path
     * @param name
     * @throws BaseException
     */
    public static void outputDomEntity(Document dom, String path, String name) throws BaseException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        try {
            path = xmlPath + path;
            File filePath = new File(path);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            File file = new File(filePath, name);
            XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
            writer.setEscapeText(false);
            writer.write(dom);
            writer.close();

            //输出Record eeca
            outRecordEeca(dom, filePath);

        } catch (IOException e) {
            System.err.println(" = " + e.getMessage());
            throw new BaseException(RetCode.FAIL, "输出dom对象失败");
        }
    }

    /**
     *
     * @param dom 源eeca的dom
     * @param filePath 文件路径
     * @throws IOException
     */
    private static void outRecordEeca(Document dom, File filePath) throws IOException {

        List entitys = dom.getRootElement().elements("entity");
        if (entitys.size() == 0) {
            return;
        }
        Element mainEntity = (Element) entitys.get(0);
        String entityName = mainEntity.attributeValue("entity-name");
        File fileRecoreEeca = new File(filePath, entityName + ".record.eecas.xml");
        String realEntityName = mainEntity.attributeValue("package") + "." + entityName;
        FileWriter fileWriter = new FileWriter(fileRecoreEeca);
        fileWriter.write(EntityConstant.ENTITY_RECORD_EECAS_FORMAT.replace("%realEntityName%", realEntityName));
        fileWriter.close();
    }
}
