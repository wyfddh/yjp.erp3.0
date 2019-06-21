package com.yjp.erp.service.enumservice;

import com.yjp.erp.model.dto.bill.EnumTypeDTO;
import com.yjp.erp.model.dto.bill.PageQueryEnumDTO;
import com.yjp.erp.model.vo.bill.CountEnumTypeVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MoquiEnumService {
    /**
     * 保存枚举
     * @param enumTypeDTO 枚举集合
     * @return
     * @throws Exception
     */
    List<EnumTypeDTO> createEnum(List<EnumTypeDTO> enumTypeDTO) throws Exception;

    CountEnumTypeVO listEnum(PageQueryEnumDTO pageQueryEnumDTO) throws Exception;
}
