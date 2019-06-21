package com.yjp.erp.model.dto.bill;

import lombok.Data;

import java.util.Objects;
import java.util.Set;

/**
 * description:
 * @author liushui
 * @date 2019/4/11
 */
@Data
public class IdReplaceDTO {

    private String classId;

    private String typeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IdReplaceDTO that = (IdReplaceDTO) o;
        return classId.equals(that.classId) &&
                typeId.equals(that.typeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classId, typeId);
    }
}
