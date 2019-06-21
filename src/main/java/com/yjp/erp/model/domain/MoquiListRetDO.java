package com.yjp.erp.model.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * description:
 *
 * @author liushui
 * @date 2019/4/19
 */
@Data
public class MoquiListRetDO {

    private Integer count;

    private List<Map> list;
}
