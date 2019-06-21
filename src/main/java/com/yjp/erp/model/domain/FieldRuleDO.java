package com.yjp.erp.model.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author liushui
 * @description:
 * @date 2019/3/22
 */
@Data
public class FieldRuleDO {
    /**
     * 字段名
     */
    private String fieldName;
    /**
     * 展示规则
     */
    private Map<String,Object> displayRule;
    /**
     * 校验规则
     */
    private List<Map<String,Object>> validRule;
    /**
     *
     */
    private Map<String,Object> modelRule;
    /**
     * 搜索规则
     */
    private Map<String,Object> searchRule;
    /**
     * 字段索引
     */
    private String fieldIndex;

    private Boolean isSub;

    private Boolean isInit;
}
