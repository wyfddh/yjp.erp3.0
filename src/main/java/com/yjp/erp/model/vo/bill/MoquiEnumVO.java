package com.yjp.erp.model.vo.bill;

import com.yjp.erp.model.dto.bill.EnumTypeDTO;
import com.yjp.erp.model.dto.bill.EnumValueDTO;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/4/22 13:33
 * @Email: jianghongping@yijiupi.com
 */
@Data
public class MoquiEnumVO {
    private EnumTypeVO  enumType;
    private List<EnumValueVO> enumValues;
}