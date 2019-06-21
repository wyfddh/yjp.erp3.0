package com.yjp.erp.service.parsexml.util;

import com.yjp.erp.service.parsexml.constant.ParseXmlConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/5/22 22:01
 * @Email: jianghongping@yijiupi.com
 */
@Component
public class PathUtils {
    /**
     * 包含moqui组件名
     */
    private static String xmlPath;

    /**
     * 组件名
     */
    private static String componentName;

    @Value("${xml.create.path}")
    public void setXmlPath(String xmlPath) {
        PathUtils.xmlPath = exchangeStandardSystemFilePath(xmlPath);
    }

    @Value("${xml.component.name}")
    public void setComponentName(String componentName) {
        PathUtils.componentName = componentName;
    }

    public static String exchangeStandardSystemFilePath(String pathStr) {
        return pathStr.replace("/", File.separator).replace("\\", File.separator);
    }

    /**
     *
     * @param viewEntityName 视图实体名
     * @return 实体实体xml 文件名
     */
    public static String getViewEntityXmlFileName(String viewEntityName) {
        return "View-"+viewEntityName+".xml";
    }

    /**
     * 获取服务文件夹的绝对路径
     *
     * @return
     */
    public static String getServceFileAbsolutePath() {
        return xmlPath + File.separator + componentName + File.separator + "service";
    }

    /**
     * 获取实体文件夹的绝对路径
     *
     * @return
     */
    public static String getEntityFileAbsolutePath() {
        return xmlPath + File.separator + componentName + File.separator + "entity";
    }


    /**
     * 根据实体名和服务名获取到服务的调用url
     *
     * @param entityName
     * @param serviceName
     * @return
     */
    public static String getServiceCallUrl(String entityName, String serviceName) {
        String serviceFilePath = getRelativeServicePath(entityName).replace(File.separator, ".");
        int strLength = serviceFilePath.length();
        if (ParseXmlConstant.SERVICE_SEPARATOR.equals(serviceFilePath.substring(strLength - 1, strLength))) {
            serviceFilePath = serviceFilePath.substring(0, strLength - 1);
        }
        return serviceFilePath + ParseXmlConstant.SERVICE_SEPARATOR + getServiceFileName(serviceName) + ParseXmlConstant.SERVICE_SEPARATOR + serviceName;
    }

    /**
     * 根据entityName获取一个服务的生成目录规则（service文件夹下的路径规则）相对路径
     *
     * @param entityName
     * @return
     */
    public static String getRelativeServicePath(String entityName) {
        return entityName;
    }

    /**
     * 根据entityName 和serviceName 返回一个服务脚本引用路径的location
     *
     * @param entityName
     * @return
     */
    public static String getRelatedScriptLocation(String entityName, String serviceName) {
        return "component:" + File.separator + File.separator + componentName + File.separator + "script" + File.separator + entityName + File.separator + serviceName + ".groovy";
    }

    public static String scriptRelatedLocationChangeAbsoluteLocation(String locationStr) {
        return xmlPath + File.separator + locationStr.replace("component:" + File.separator + File.separator, "");
    }

    /**
     * 获取服务文件可持久化的绝对路径
     *
     * @param entityName
     * @return
     */
    public static String getAbsoluteServicePath(String entityName) {
        return xmlPath + File.separator + componentName + File.separator + "service" + File.separator + entityName;
    }

    /**
     * 获取实体文件可持久化的绝对路径
     *
     * @param entityName
     * @return
     */
    public static String getAbsoluteEntityPath(String entityName) {
        return xmlPath + File.separator + componentName + File.separator + "entity";
    }

    /**
     * 根据entityName获取到一个实体的服务文件的文件名(不含.xml)
     *
     * @param entityName
     * @return
     */
    public static String getServiceFileName(String entityName) {
        return entityName + "Service";
    }

    /**
     * 根据entityName获取到一个实体的实体xml文件的文件名(不含.xml)
     *
     * @param entityName
     * @return
     */
    public static String getEntityFileName(String entityName) {
        return entityName;
    }

    /**
     * 根据entityName获取到一个实体的eecas文件的文件名(不含.xml)
     *
     * @param entityName
     * @return
     */
    public static String getSeecaFileName(String entityName) {
        return entityName + ".eecas";
    }
}
