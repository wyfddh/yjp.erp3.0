package com.yjp.erp.model.po.bill;

import lombok.Data;

import java.io.Serializable;

/**
 * description:
 * @author liushui
 * @date 2019/4/11
 */
@Data
public class TransformPO implements Serializable {

    private String typeId;

    private String label;
}
