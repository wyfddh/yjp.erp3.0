package com.yjp.erp.model.po.system;

import java.sql.Timestamp;
import lombok.Data;

/**
 * @author wyf
 * @date 2019/4/2 上午 9:34
 **/
@Data
public class BaseSys {

    private Long id;

    private Integer status;

    private Timestamp createDate;

    private Long creator;

    private Timestamp updateDate;

    private Long updater;

}
