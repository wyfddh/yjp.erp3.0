package com.yjp.erp.model.vo.bill;

import io.swagger.models.auth.In;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/4/24 16:35
 * @Email: jianghongping@yijiupi.com
 */
@Data
public class CountMoquiEnumVO {
    private Integer count;
    private List<MoquiEnumVO> listMoquiEnumVO;

}
