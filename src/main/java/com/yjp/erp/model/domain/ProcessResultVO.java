package com.yjp.erp.model.domain;

import lombok.Data;

/**
 * description:发起工作流返回值
 *
 * @author liushui
 * @date 2019/4/18
 */
@Data
public class ProcessResultVO {

    private String msg;

    private Integer code;

    private ProcessData data;

    @Data
    public static class ProcessData{

        private String processId;

        private String taskId;
    }
}
