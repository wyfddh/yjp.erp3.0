package com.yjp.erp.model.dto.gateway;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * description:前端字段替换参数
 * @author liushui
 * @date 2019/4/11
 */
@Data
public class RefDTO {

    /**
     * 实体类型
     */
    private String classId;

    /**
     * 实体标识
     */
    private String typeId;

    /**
     * 实体id
     */
    private Long id;

    /**
     * 目标替换字段
     */
    private List<String> targetFields;

    /**
     * 实体字段list
     */
    private Set<Long> ids;

}
