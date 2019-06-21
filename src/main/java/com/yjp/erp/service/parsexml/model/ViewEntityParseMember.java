package com.yjp.erp.service.parsexml.model;

import lombok.Data;

import java.util.List;

@Data
public class ViewEntityParseMember {
    /**
     * 视图实体名
     */
    private String entityName;
    /**
     * 包名
     */
    private String packageName;
    /**
     * 是否跳过权限验证
     */
    private String authorizeSkip;
    /**
     * 是否自动清理缓存
     */
    private String autoClearCache;
    /**
     * 是否开启缓存
     */
    private String cache;
    /**
     * 视图实体成员
     */
    private List<MemberEntity> memberEntities;
    /**
     * 视图实体字段
     */
    private List<Alias> aliases;

    @Data
    public class MemberEntity {
        /**
         * 成员实体的别名
         */
        private String entityAlias;
        /**
         * 成员实体的实体名
         */
        private String entityName;
        /**
         * this成员实体与此视图实体的哪个other成员实体关联（别名）
         */
        private String joinFromAlias;
        /**
         * other成员实体被关联的字段
         */
        private String fieldName;
        /**
         * this成员实体建立关联的字段
         */
        private String related;
    }

    @Data
    public class Alias {
        /**
         * 视图实体的字段名
         */
        private String name;
        /**
         * 此字段来源的成员实体（别名）
         */
        private String entityAlias;
        /**
         * 此字段来源成员实体的哪个字段(与expression 二选一)
         */
        private String field;
        /**
         * 此视图实体的字段类型
         */
        private String type;
        /**
         * 此视图实体计算规则的表达式(field 二选一)
         */
        private String expression;
        /**
         * 视图实体字段的描述
         */
        private String description;
    }

}
