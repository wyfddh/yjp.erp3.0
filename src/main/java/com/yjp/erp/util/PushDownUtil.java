package com.yjp.erp.util;

import com.yjp.erp.model.dto.script.EntityFieldTransmit;
import com.yjp.erp.model.po.eeca.BillEecaRuleField;
import org.springframework.util.Assert;

import java.util.List;

public class PushDownUtil {
    public static String scriptFormat = "" +
            "def downEntity= ec.entity.makeValue(\"%downEntityName%\")\n" +
            "def downEntityDeatil = ec.entity.makeValue(\"%downEntityDetailName%\")\n" +
            "def downFkValue\n" +
            "\n" +
            "downEntity.setFields(context, true, null, null)\n" +
            "%relatedMainFields%\n" +
            "downEntity.setSequencedIdPrimary()\n" +
            "%fillDownFkValue%\n" +
            "downEntity.create()\n" +
            "\n" +
            "def upEntityDetailList = ec.entity.find(\"%upEntityDetailName%\").condition(\"billId\", id).list()\n" +
            "def size = upEntityDetailList.size()\n" +
            "for (def i = 0; i < size; i++) {\n" +
            "   %relatedDetailFields%\n" +
            "   downEntityDetail.setSequencedIdPrimary()\n" +
            "   %setDetailDownFkValue%\n" +
            "   downEntityDetail.create()\n" +
            "}";

    public static String execute(EntityFieldTransmit fieldMapper) {
        checkEntityFieldMapper(fieldMapper);
        return scriptFormat.replace("%downEntityName%", fieldMapper.getDownEntity())
                            .replace("%downEntityDetailName%", fieldMapper.getDownEntityDetail())
                            .replace("%upEntityDetailName%", fieldMapper.getDownEntityDetail())
                            .replace("%relatedMainFields%", jointScript("downEntity",fieldMapper.getMainEntityRelated()))
                            .replace("%fillDownFkValue%","downFkValue = downEntity."+fieldMapper.getDownFkRelated().getDestField())
                            .replace("%relatedDetailFields%",jointScript(fieldMapper.getDownEntityDetail(),fieldMapper.getDetailEntityRelated()))
                            .replace("%setDetailDownFkValue%","downEntityDetail."+fieldMapper.getDownFkRelated().getDestField()+"=downFkValue");
    }

    /**
     * 拼接 上端实体传递关联字段到下端实体的脚本片段
     * @param downDef
     * @param listFieldRelated
     * @return
     */
    private static String jointScript(String downDef,List<BillEecaRuleField> listFieldRelated) {
       StringBuilder strBuilder=new StringBuilder();
       if(null !=listFieldRelated){
           listFieldRelated.forEach(relatedField -> {
               strBuilder.append(downDef).append(".")
                       .append(relatedField.getSrcField()).append(" = ")
                       .append("context.").append(relatedField.getDestField())
                       .append("\n");
           });
       }
        return strBuilder.toString();
    }

    private static void checkEntityFieldMapper(EntityFieldTransmit fieldMapper) {
        Assert.notNull(fieldMapper.getUpEntity());
        Assert.notNull(fieldMapper.getUpEntity());
    }
}
