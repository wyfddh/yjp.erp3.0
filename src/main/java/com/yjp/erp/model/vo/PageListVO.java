package com.yjp.erp.model.vo;

import com.yjp.erp.model.dto.PageDTO;
import java.util.List;
import lombok.Data;

/**
 * @author wyf
 * @date 2019/4/13 上午 10:37
 **/
@Data
public class PageListVO<T> {
    private PageDTO pageDTO;

    private List<T> list;
}
