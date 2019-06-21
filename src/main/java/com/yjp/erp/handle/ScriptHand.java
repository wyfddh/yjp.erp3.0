package com.yjp.erp.handle;

import com.alibaba.fastjson.JSONObject;
import com.yjp.erp.constants.BillOperatorConstant;
import com.yjp.erp.model.domain.fieldrule.OptionRules;
import com.yjp.erp.mapper.BillMapper;
import com.yjp.erp.mapper.BillOptionRulesMapper;
import com.yjp.erp.mapper.ModuleMapper;
import com.yjp.erp.mapper.base.BaseEntityMapper;
import com.yjp.erp.model.po.bill.Bill;
import com.yjp.erp.model.po.bill.BillOptionRules;
import com.yjp.erp.model.po.bill.Module;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 处理生成下推/回写脚本
 * @CreateDate: 2019/4/13 14:33
 * @EMAIL: jianghongping@yijiupi.com
 */
@Component
@Slf4j
public class ScriptHand {
    private final String EECA = "eeca";
    private final String ITEM = "item.";

    private ModuleMapper moduleMapper;

    private BillMapper billMapper;

    private BillOptionRulesMapper billOptionRulesMapper;

    private BaseEntityMapper baseEntityMapper;

    @Value("${xml.create.path}")
    private String xmlPath;
    @Value("${xml.component.name}")
    private String componentName;

    @Autowired
    public ScriptHand(ModuleMapper moduleMapper, BillMapper billMapper, BillOptionRulesMapper billOptionRulesMapper,BaseEntityMapper baseEntityMapper) {
        this.moduleMapper = moduleMapper;
        this.billMapper = billMapper;
        this.billOptionRulesMapper = billOptionRulesMapper;
        this.baseEntityMapper = baseEntityMapper;
    }

    /**
     * 根据 moduleId 查询出 List<OptionRules>:多个实体的对应规则、源单据名、源单据详情名、
     * 源单据module  然后再调用生成 下推回写的方法
     *
     * @param moduleId 要下推回写的module
     */
    public void createScriptByClassIdAndTypeId(Long moduleId) {
        Module orginModule = moduleMapper.getModuleById(moduleId);
        //热修复单据推实体
        Bill originBill=null;
        if ("entity".equals(orginModule.getClassId())) {
            originBill = baseEntityMapper.getBillDetail(orginModule);
        }else{
            originBill = billMapper.getBillDetail(orginModule);
        }

        List<Bill> orginDetailBills = billMapper.getChildrenBillByParentId(originBill.getId());
        String orginDetailBillName = null;

        if (orginDetailBills.size() > 0) {
            Bill orginDetailBill = orginDetailBills.get(0);
            orginDetailBillName = orginDetailBill.getName();
        }

        List<BillOptionRules> optionRulesByModules = billOptionRulesMapper.getOptionRulesByModule(moduleId);
        List<OptionRules> optionRules = new ArrayList<>();
        optionRulesByModules.forEach(optionRulesByModule -> {
            OptionRules optionRule = JSONObject.parseObject(optionRulesByModule.getOptionRules(), OptionRules.class);
            optionRules.add(optionRule);
        });

        createScript(optionRules, originBill.getName(), orginDetailBillName, orginModule);
    }

    /**
     * 创建下推或回写的脚本
     *
     * @param optionRules            多个目标实体的对应规则
     * @param originEntityName       源实体名
     * @param originDetailEntityName 源实体明细
     * @param originModule           源实体module
     */
    private void createScript(List<OptionRules> optionRules, String originEntityName, String originDetailEntityName, Module originModule) {
        optionRules.forEach(optionRule -> {
            Module targetModule = getModuleByClassIdAndTypeId(optionRule.getClassId(), optionRule.getTypeId());
            Bill targetBill=null;
            if("entity".equals(targetModule.getClassId())){
                targetBill= baseEntityMapper.getBillByModuleId(targetModule.getId());
            }else{
                targetBill= billMapper.getBillByModuleId(targetModule.getId());
            }
            Bill targetDetailBill = billMapper.getBillByParentId(targetBill.getId());

            //处理下推
            if (BillOperatorConstant.PUSH_DOWN.equals(optionRule.getType())) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("originEntityName", originEntityName);
                dataMap.put("originDetailEntityName", originDetailEntityName);
                dataMap.put("targetEntityName", targetBill.getName());
                dataMap.put("targetEntityDetailName", targetDetailBill.getName());

                StringBuilder mainScript = new StringBuilder();
                StringBuilder detailScript = new StringBuilder();
                //拼接下推的代码块
                optionRule.getMappingRules().forEach(mappingField -> {
                    String targetField = mappingField.getTargetField();
                    String originField = mappingField.getTargetField();

                    if (originField.contains(ITEM)) {
                        detailScript.append("targetEntityDetailValue.")
                                .append(mappingField.getTargetField().replace(ITEM, "")).append("=")
                                .append("value.")
                                .append(originField.replace(ITEM, ""))
                                .append("/n");
                    } else {
                        if (targetField.contains(ITEM)) {
                            detailScript.append("targetEntityDetailValue.")
                                    .append(targetField.replace(ITEM, ""))
                                    .append("=").append("originEntityDetailValue.")
                                    .append(originField)
                                    .append("/n");
                        }
                        mainScript.append("targetEntityValue.")
                                .append(mappingField.getTargetField())
                                .append("=").append("originEntityValue.")
                                .append(mappingField.getOriginField())
                                .append("/n");
                    }
                });

                dataMap.put("fillTargetEntityValueMapping", mainScript.toString());
                dataMap.put("fillTargetEntityDetailValueMapping", detailScript.toString());
                try {
                    createPushDown(dataMap, targetModule, originModule);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            //处理回写
            if (BillOperatorConstant.WRITE_BACK.equals(optionRule.getType())) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("originEntityName", originEntityName);
                dataMap.put("originDetailEntityName", originDetailEntityName);
                dataMap.put("targetEntityName", targetBill.getName());

                StringBuilder targetEntityValueMapping = new StringBuilder();
                //拼接回写的代码块
                optionRule.getMappingRules().forEach(mappingField -> {
                    if (mappingField.getOriginField().contains(ITEM)) {
                        targetEntityValueMapping.append("targetEntityValue.")
                                .append(mappingField.getTargetField())
                                .append("=")
                                .append("value.")
                                .append(mappingField.getOriginField().replace(ITEM, "")).append("/n");
                    } else {
                        targetEntityValueMapping.append("targetEntityValue.")
                                .append(mappingField.getTargetField())
                                .append("=").append("originEntityValue.")
                                .append(mappingField.getOriginField()).append("/n");
                    }
                });
                dataMap.put("fillTargetEntityValueMapping", targetEntityValueMapping.toString());
                try {
                    createBackWrite(dataMap, targetModule, originModule);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * 根据 classId 和typeId 获取module 对象
     *
     * @param classId classId的值
     * @param typeId  typeId的值
     */
    private Module getModuleByClassIdAndTypeId(String classId, String typeId) {
        Module module = new Module();
        module.setClassId(classId);
        module.setTypeId(typeId);
        return moduleMapper.getModuleByClassIdAndTypeId(module);
    }

    /**
     * 根据module生成文件名 和文件路径 并调用模板的处理方法生成脚本文件
     * 然后掉用生成服务的方法
     *
     * @param dataMap tfl的替换片段
     * @throws Exception 创建下推脚本失败
     */
    private void createPushDown(Map<String, Object> dataMap, Module module, Module originModule) throws Exception {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);

//        java.net.URL fileURL = this.getClass().getResource("/template");
//        configuration.setDirectoryForTemplateLoading(new File(fileURL.toURI()));

        String templatePath = this.getClass().getResource("/template").getFile();
        configuration.setDirectoryForTemplateLoading(new File(templatePath));
        Template template = configuration.getTemplate("pushDownTemplateScript.tfl");

        String filePath = createFilePath(module, "script");
        String componentFilePath = createScriptPath(module);
        String fileFileName = createScriptFileName(dataMap);
        String scriptRef = "component://" + componentFilePath + File.separator + fileFileName;

        dealTemplate(dataMap, template, filePath, fileFileName);

        createService(fileFileName.replace(".groovy", ""), scriptRef, module, originModule);
    }

    /**
     * @param dataMap      template 需要替换的值
     * @param module       目标实体的module
     * @param originModule 源实体的module
     */
    private void createBackWrite(Map<String, Object> dataMap, Module module, Module originModule) throws Exception {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);
        String templatePath = this.getClass().getResource("/template").getFile();
        configuration.setDirectoryForTemplateLoading(new File(templatePath));
        Template template = configuration.getTemplate("writeBackTemplateScript.tfl");

        String filePath = createFilePath(module, "script");
        String fileFileName = createScriptFileName(dataMap);
        String componentFilePath = createScriptPath(module);
        String scriptRef = "component://" + componentFilePath + File.separator + fileFileName;

        //
        dealTemplate(dataMap, template, filePath, fileFileName);

        createService(fileFileName.replace(".groovy", ""), scriptRef, module, originModule);
    }

    /**
     * 更具参数生成服务的xml
     *
     * @param serviceName  服务名
     * @param scriptRef    脚本的引用路径
     * @param module       目标实体的module
     * @param originModule 源实体的module
     */
    private void createService(String serviceName, String scriptRef, Module module, Module originModule) throws Exception {
        String serviceVerb = serviceName + "Service";
        String serviceXmlPath = createFilePath(module, "service");
        String serviceRef = module.getClassId() + "." + module.getTypeId() + "." + serviceVerb + "." + serviceVerb;
        String serviceXmlName = serviceVerb + ".xml";

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("serviceVerb", serviceVerb);
        dataMap.put("serviceScript", scriptRef.replace("\\", "/"));

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);
        String templatePath = this.getClass().getResource("/template").getFile();
        configuration.setDirectoryForTemplateLoading(new File(templatePath));
        Template template = configuration.getTemplate("pushService.tfl");

        dealTemplate(dataMap, template, serviceXmlPath, serviceXmlName);

        //生成eeca
        createEeca(module, serviceRef, serviceVerb.replace("Service", "Eeca"), originModule);

    }

    /**
     * 生成eeca
     *
     * @param module       源实体module
     * @param serviceRef   服务的应用名
     * @param eecaName     eeca文件名
     * @param originModule 源实体的module
     */
    private void createEeca(Module module, String serviceRef, String eecaName, Module originModule) throws Exception {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);
        String templatePath = this.getClass().getResource("/template").getFile();
        configuration.setDirectoryForTemplateLoading(new File(templatePath));
        Template template = configuration.getTemplate("pushEeca.tfl");

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("entityName", "yjp." + billMapper.getBillDetail(originModule).getName());
        dataMap.put("serviceName", serviceRef);

        String eecaXmlPath = createFilePath(module, "eeca");
        dealTemplate(dataMap, template, eecaXmlPath, eecaName + ".eecas.xml");
    }

    /**
     * 根据模板及其参数生成文件
     *
     * @param dataMap      模板中要填入的数据
     * @param template     模板对象
     * @param filePath     文件目录
     * @param fileFileName 文件名
     */
    private void dealTemplate(Map<String, Object> dataMap, Template template, String filePath, String fileFileName) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter(filePath + File.separator + fileFileName);
            template.process(dataMap, writer);
            writer.close();
        } catch (Exception e) {
            if (null != writer) {
                writer.close();
            }
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
    }

    /**
     * 根据module 和传入的文件路径类型生成文件的路径
     *
     * @param module   要生成文件的实体module
     * @param pathType 路径类型
     */
    private String createFilePath(Module module, String pathType) {
        StringBuilder scriptFilePath = new StringBuilder();
        scriptFilePath.append(xmlPath)
                .append(File.separator)
                .append(componentName)
                .append(File.separator);
        if (EECA.equals(pathType)) {
            scriptFilePath.append("entity");
            return scriptFilePath.toString();
        }
        scriptFilePath.append(pathType)
                .append(File.separator)
                .append(module.getClassId())
                .append(File.separator)
                .append(module.getTypeId());
        return scriptFilePath.toString();
    }

    /**
     * 根据module 和 路径类型来生成文件component下的应用路径
     *
     * @param module 实体moduel
     */
    private String createScriptPath(Module module) {
        StringBuilder scriptFilePath = new StringBuilder();
        scriptFilePath.append(componentName)
                .append(File.separator)
                .append("script")
                .append(File.separator)
                .append(module.getClassId())
                .append(File.separator)
                .append(module.getTypeId());
        return scriptFilePath.toString();
    }

    /**
     * 创建脚本的文件名
     *
     * @param dataMap 模板的填充数据
     */
    private String createScriptFileName(Map<String, Object> dataMap) {
        StringBuilder scriptFileName = new StringBuilder();
        scriptFileName.append(dataMap.get("originDetailEntityName"))
                .append(dataMap.get("targetEntityName"))
                .append(dataMap.get("targetEntityDetailName"))
                .append(".groovy");
        return scriptFileName.toString();
    }

}
