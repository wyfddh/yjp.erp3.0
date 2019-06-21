package com.yjp.erp.service.enumservice.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yjp.erp.model.dto.bill.EnumEntity;
import com.yjp.erp.model.dto.bill.EnumTypeDTO;
import com.yjp.erp.model.dto.bill.EnumValueDTO;
import com.yjp.erp.model.dto.bill.PageQueryEnumDTO;
import com.yjp.erp.mapper.EnumEntityMapper;
import com.yjp.erp.mapper.ModuleMapper;
import com.yjp.erp.model.po.bill.Module;
import com.yjp.erp.service.enumservice.MoquiEnumService;
import com.yjp.erp.util.HttpClientUtils;
import com.yjp.erp.util.ObjectUtils;
import com.yjp.erp.util.SnowflakeIdWorker;
import com.yjp.erp.model.vo.bill.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: 江洪平
 * @CreateDate: 2019/4/22 14:23
 * @Email: jianghongping@yijiupi.com
 */
@Service
@Slf4j
public class MoquiEnumServiceImpl implements MoquiEnumService {
    @Value("${moqui.server}")
    private String moquiServer;

    @Value("${moqui.pre}")
    private String pre;

    @Autowired
    private ModuleMapper moduleMapper;

    @Autowired
    EnumEntityMapper enumEntityMapper;

    @Override
    public List<EnumTypeDTO> createEnum(List<EnumTypeDTO> enumTypeDTO) throws Exception {
        insertEnumConfig(enumTypeDTO);

        List<EnumTypeDTO> listEnumValueDTO = new ArrayList<>();
        String parameters = JSONObject.toJSONString(enumTypeDTO, SerializerFeature.WriteMapNullValue);
        String result = HttpClientUtils.postParameters(moquiServer + "/rest/s1/common/createEnum", parameters, null);
        log.info("创建枚举后的结果字符串" + result);
        List<MoquiEnumVO> moquiEnumVOs = JSONObject.parseArray(result, MoquiEnumVO.class);
        moquiEnumVOs = moquiEnumVOs != null ? moquiEnumVOs : new ArrayList<>();

        moquiEnumVOToEnumTypeDTO(listEnumValueDTO, moquiEnumVOs);
        log.info("创建枚举后的结果" + listEnumValueDTO);
        return listEnumValueDTO;
    }

    private void insertEnumConfig(List<EnumTypeDTO> enumTypeDTO) {
        enumTypeDTO.forEach(enumTypeDTOCell -> {

            Long moduleId = SnowflakeIdWorker.nextId();
            Module module = new Module();
            module.setClassId("enum");
            module.setTypeId(enumTypeDTOCell.getTypeId());
            Module existModule = moduleMapper.getModuleByClassIdAndTypeId(module);
            if (null != existModule) {
                return;
            }

            module.setActiveState(1);
            module.setPublishState(1);
            module.setId(moduleId);
            moduleMapper.insertModule(module);

            EnumEntity enumEntity = new EnumEntity();
            enumEntity.setId(SnowflakeIdWorker.nextId());
            enumEntity.setLabel(enumTypeDTOCell.getTypeName());
            enumEntity.setModuleId(moduleId);
            enumEntity.setName("yjp_EnumEntityValue");
            enumEntityMapper.insertEnumEntity(enumEntity);
        });
    }

    private void moquiEnumVOToEnumTypeDTO(List<EnumTypeDTO> listEnumValueDTO, List<MoquiEnumVO> moquiEnumVOs) {
        moquiEnumVOs.forEach(moquiEnumVO -> {

            EnumTypeVO enumTypeVO = moquiEnumVO.getEnumType();
            List<EnumValueVO> enumValueVOs = moquiEnumVO.getEnumValues();

            EnumTypeDTO enumTypeDTOCell = new EnumTypeDTO();
            enumTypeDTOCell.setId(enumTypeVO.getId());
            enumTypeDTOCell.setTypeId(enumTypeVO.getTypeId());
            enumTypeDTOCell.setTypeName(enumTypeVO.getTypeName());
            enumTypeDTOCell.setDescription(enumTypeVO.getDescription());

            List<EnumValueDTO> enumValueDTOS = new ArrayList<>();
            enumValueVOs.forEach(enumValueVO -> {
                EnumValueDTO cellEnumValueDTO = new EnumValueDTO();
                cellEnumValueDTO.setId(enumValueVO.getId());
                cellEnumValueDTO.setEnumId(enumValueVO.getEnumId());
                cellEnumValueDTO.setTypeId(enumValueVO.getTypeId());
                cellEnumValueDTO.setValue(enumValueVO.getValue());
                cellEnumValueDTO.setDescription(enumValueVO.getDescription());
                enumValueDTOS.add(cellEnumValueDTO);
            });
            enumTypeDTOCell.setEnumValue(enumValueDTOS);

            listEnumValueDTO.add(enumTypeDTOCell);
        });
    }

    @Override
    public CountEnumTypeVO listEnum(PageQueryEnumDTO pageQueryEnumDTO) throws Exception {
        List<EnumTypeDTO> listEnumValueDTO = new ArrayList<>();

        String parameters = JSONObject.toJSONString(pageQueryEnumDTO);
        String result = HttpClientUtils.postParameters(moquiServer + "/rest/s1/common/getListEnum", parameters, null);
        CountMoquiEnumVO countMoquiEnumVO = JSONObject.parseObject(result, CountMoquiEnumVO.class);
        countMoquiEnumVO = ObjectUtils.isNotEmpty(countMoquiEnumVO) ? countMoquiEnumVO : new CountMoquiEnumVO();

        moquiEnumVOToEnumTypeDTO(listEnumValueDTO, countMoquiEnumVO.getListMoquiEnumVO());
        CountEnumTypeVO countEnumTypeVO=new CountEnumTypeVO();
        countEnumTypeVO.setCount(countMoquiEnumVO.getCount());
        countEnumTypeVO.setListEnumTypeVO(listEnumValueDTO);
        return countEnumTypeVO;
    }

//    @Override
//    public ListEnumVO detailEnum(String id) throws Exception {
//        JSONObject idJson = new JSONObject();
//        idJson.put("id", id);
//        String result = HttpClientUtils.postParameters(moquiServer + "/rest/s1/common/detailEnum", idJson.toJSONString(), null);
//        log.info("根据Id查询实体枚举的详情" + result);
//        return JSONObject.parseObject(result, ListEnumVO.class);
//    }
}
