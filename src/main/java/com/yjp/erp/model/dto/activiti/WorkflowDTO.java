package com.yjp.erp.model.dto.activiti;

import com.yjp.erp.model.dto.PageDTO;
import com.yjp.erp.model.po.activiti.Workflow;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;


/**
 * <p>
 * 工作流引擎入参
 * </p>
 *
 * @author jihongjin@yijiupi.com
 * @since 2019-04-03
 */

public class WorkflowDTO extends PageDTO implements Serializable {
    /**
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
     */
    private static final long serialVersionUID = 1L;
    /**
     * 工作流id
     */
    private Long id;
    /**
     * 工作流名称
     */
    private String name;
    /**
     * 标识符(workflow_1,workflow_2...)
     */
    private String identifier;
    /**
     * 描述
     */
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Workflow toPo() {
        Workflow workflow = new Workflow();
        try {
            BeanUtils.copyProperties(workflow, this);
        } catch (Exception e) {
            throw new RuntimeException("实体对象拷贝异常：" + e.getMessage());
        }
        return workflow;
    }

}
