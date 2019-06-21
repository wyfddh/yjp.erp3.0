package com.yjp.erp.model.dto.dbxml;

import com.yjp.erp.model.po.bill.Module;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description xml 文件生成的路径规则
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/3/27
 */

@Component
public class XmlFileRoules {
    private static final String SEPARATOR = "/";

    private static String runtimePath;

    private static String componentName;

    private String fileDir;

    private Module module;

    private String entityName;

    public XmlFileRoules() {
    }

    @Value("${xml.create.path}")
    public void setRuntimePath(String runtimePath) {
        XmlFileRoules.runtimePath = runtimePath;
    }

    @Value("${xml.component.name}")
    public void setComponentName(String componentName) {
        XmlFileRoules.componentName = componentName;
    }

    public XmlFileRoules(Module module, String entityName) {
        this.module = module;
        this.entityName = entityName;
        fileDir = SEPARATOR + module.getClassId() + SEPARATOR + module.getTypeId() + SEPARATOR;
    }

    public XmlFileRoules(Module module, String entityName, String serviceName) {
        this.module = module;
        this.entityName = entityName;
        fileDir = SEPARATOR + module.getClassId() + SEPARATOR + module.getTypeId() + SEPARATOR;
    }

    public XmlFileRoules(Module module) {
        this.module = module;
        fileDir = SEPARATOR + module.getClassId() + SEPARATOR + module.getTypeId() + SEPARATOR;
    }

    public String getActionFileName() {
        return "/" + componentName + "/" + "service/";
    }

    public String getServiceFilePath() {
        return "/" + componentName + "/" + "service" + fileDir;
    }

    public String getServiceRelPath() {
        String path = fileDir.replace("/", ".");
        return path.substring(1, path.length());
    }

    public String getEecaFileName() {
        return "/" + componentName + "/" + "entity/";
    }

    public String getEntityFileName() {
        return "/" + componentName + "/" + "entity";
    }

    public String getViewFileName() {
        return "/" + componentName + "/" + "entity/";
    }

    public String getScriptFileName() {
        return "/" + componentName + "/" + "script" + fileDir;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getRuntimePath() {
        return runtimePath;
    }

    /**
     * 获取module
     *
     * @return
     */
    public Module getModule() {
        return this.module;
    }
}
