package com.yjp.erp.model.vo.bill;

import com.yjp.erp.model.dto.bill.EnumTypeDTO;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/4/24 16:46
 * @Email: jianghongping@yijiupi.com
 */
@Data
public class CountEnumTypeVO {
    private Integer count;
    private List<EnumTypeDTO> listEnumTypeVO;
}
