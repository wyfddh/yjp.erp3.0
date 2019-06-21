package com.yjp.erp.model.vo.view;

import com.yjp.erp.model.domain.*;
import com.yjp.erp.model.domain.fieldrule.OptionRules;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
public class ViewEntityVO {
    @NotBlank(message = "表单或实体中文名称不能为空")
    private String title;

    @NotBlank(message = "classId不能为空")
    private String classId;

    @NotBlank(message = "typeId不能为空")
    private String typeId;
    /**
     * 视图实体名
     */
    private String name;
    /**
     * 视图实体包名
     */
    private String packageName;
    /**
     * 添加缓存
     */
    private String cache;
    /**
     * 自动清理缓存
     */
    private String autoClearCache;
    /**
     * 跳过权限验证
     */
    private String authorizeSkip;

    @NotNull
    private Integer status;

    private List<MemberVO> fields;

    @Data
    public class MemberVO {
        /**
         * 成员实体名
         */
        private String entityName;
        /**
         * 成员实体别名
         */
        private String entityAlias;
        /**
         * 关联实体别名
         */
        private String joinFromAlias;
        /**
         * 上一级实体的关联字段
         */
        private String entityParentValue;
        /**
         * 当前实体关联字段
         */
        private String entityValue;
        /**
         * 视图实体的字段
         */
        private List<ViewField> viewField;

        @Data
        public class ViewField {
            /**
             * 视图实体名
             */
            private String name;
            /**
             * 视图实体字段
             */
            private String field;
            /**
             * 视图实体字段计算表达式
             */
            private String expression;
            /**
             * 视图实体字段描述
             */
            private String description;
        }

    }

//    private List<FieldRuleDO> rules;
//
//    private List<ServicePropertyDO> services;
//
//    private List<ActionDO> actions;
//
//    private List<EventDO> events;
//
//    private List<BillFieldsRulesDO> children;
//    /**
//     * 下推或回写的规则定义
//     */
//    private List<OptionRules> optionRules;
//
//    //1-插入 2-更新
//    private Integer type;
}
