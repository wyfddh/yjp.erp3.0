package com.yjp.erp.model.dto.view;

import com.yjp.erp.model.dto.PageDTO;
import lombok.Data;

/**
 * @author wyf
 * @date 2019/4/25 下午 3:41
 **/
@Data
public class ViewDTO {

    private PageDTO page;

    private Long id;

    private String packagePath;

    private String entityName;

    private String restParentName;

    private String restSonName;

    private String restType;
}
