package com.yjp.erp.model.domain;

import com.yjp.erp.model.po.bill.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liushui
 * @description:
 * @date 2019/3/22
 */
@Data
public class BillDO {

    private List<Bill> bills = new ArrayList<>();

    private List<BillField> billFields = new ArrayList<>();

    private List<BillFieldProperty> billFieldProperties = new ArrayList<>();

    private List<BillFieldWebProperty> billFieldWebProperties = new ArrayList<>();

    private List<BillFieldWebPropertyRel> billFieldWebPropertyRels = new ArrayList<>();

    private List<BillProperty> billProperties = new ArrayList<>();
}
