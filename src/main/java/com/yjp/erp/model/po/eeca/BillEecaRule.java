package com.yjp.erp.model.po.eeca;
import lombok.Data;

@Data
public class BillEecaRule {
    private Long id;

    private Long billEecaId;

    private String srcEntity;

    private String destEntity;
    /**
     * 主表关联id
     */
    private String parentId;

    /**
     * 明细表关联id
     */
    private Long detailId;
}
