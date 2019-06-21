package com.yjp.erp.model.po.bill;

import lombok.Data;

import java.util.Date;


@Data
public class BillOptionRules {
    /**
     * id
     */
    private Integer id;
    /**
     * module的Id
     */
    private Long moduleId;
    /**
     * 操作类型，下推或回写
     */
    private Integer optionType;

    private Date gmtCreate;
    /**
     * 下推或回写规则定义的json字符串
     */
    private String optionRules;

}