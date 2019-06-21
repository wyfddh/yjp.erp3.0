package com.yjp.erp.model.dto.bill;

import lombok.Data;

import java.util.Map;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/4/23 14:20
 * @Email: jianghongping@yijiupi.com
 */
@Data
public class PageQueryEnumDTO {
    private Pager pager;
    private Map<String, String> conditions;

    @Data
    public class Pager {
        private Integer total;
        private Integer pageIndex;
        private Integer pageSize;
    }
}
