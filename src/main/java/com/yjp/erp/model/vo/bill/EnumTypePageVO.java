package com.yjp.erp.model.vo.bill;

import com.yjp.erp.model.dto.bill.EnumTypeDTO;
import com.yjp.erp.model.dto.bill.PageQueryEnumDTO;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/4/22 13:33
 * @Email: jianghongping@yijiupi.com
 */
@Data
public class EnumTypePageVO {
    List<EnumTypeDTO> listEnumTypeVO;
    PageQueryEnumDTO.Pager pager;
}