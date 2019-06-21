package com.yjp.erp.controller.bill;

import com.yjp.erp.model.dto.bill.EntityDetailDTO;
import com.yjp.erp.handle.BillApplicationHandel;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.model.vo.bill.BillElementsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * description:表单应用平台接口
 *
 * @author liushui
 * @date 2019/4/11
 */
@RestController
@RequestMapping("/apps")
@Slf4j
public class BillApplicationController {

    private final BillApplicationHandel billApplicationHandel;

    @Autowired
    public BillApplicationController(BillApplicationHandel billApplicationHandel) {
        this.billApplicationHandel = billApplicationHandel;
    }

    /**
     * 功能描述: 根据classId和typeId获取实体元数据
     *
     * @author liushui
     * @date 2019/4/11
     */
    @PostMapping("/getMetaData")
    public JsonResult<BillElementsVO> getElements(@Valid @RequestBody EntityDetailDTO dto) {
        BillElementsVO elements = billApplicationHandel.handelElements(dto);
        return RetResponse.makeOKRsp(elements);
    }
}
