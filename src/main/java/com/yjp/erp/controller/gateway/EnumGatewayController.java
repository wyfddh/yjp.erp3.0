package com.yjp.erp.controller.gateway;

import com.yjp.erp.model.dto.PageDTO;
import com.yjp.erp.model.dto.bill.EnumTypeDTO;
import com.yjp.erp.model.dto.bill.PageQueryEnumDTO;
import com.yjp.erp.service.enumservice.MoquiEnumService;
import com.yjp.erp.model.vo.JsonResult;
import com.yjp.erp.model.vo.RetResponse;
import com.yjp.erp.model.vo.bill.CountEnumTypeVO;
import com.yjp.erp.model.vo.bill.EnumDetailVO;
import com.yjp.erp.model.vo.bill.EnumTypePageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/4/22 13:36
 * @Email: jianghongping@yijiupi.com
 */
@RestController
@RequestMapping("/config/gateway")
@Slf4j
public class EnumGatewayController {
    @Autowired
    MoquiEnumService moquiEnumService;

    @PostMapping("/salveEnum")
    public JsonResult salveEnum(@RequestBody List<EnumTypeDTO> enumTypeDTO) throws Exception {
        return RetResponse.makeOKRsp(moquiEnumService.createEnum(enumTypeDTO));
    }

    @PostMapping("/listEnum")
    public JsonResult listEnum(@RequestBody PageQueryEnumDTO pageQueryEnumDTO) throws Exception {
        CountEnumTypeVO listEnumTypeVO = moquiEnumService.listEnum(pageQueryEnumDTO);
        EnumTypePageVO enumTypePageVO = new EnumTypePageVO();
        enumTypePageVO.setListEnumTypeVO(listEnumTypeVO.getListEnumTypeVO());
        PageQueryEnumDTO.Pager pager = pageQueryEnumDTO.getPager();
        pager.setTotal(listEnumTypeVO.getCount());
        enumTypePageVO.setPager(pager);
        return RetResponse.makeOKRsp(enumTypePageVO);
    }
}
