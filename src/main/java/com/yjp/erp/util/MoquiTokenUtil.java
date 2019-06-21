package com.yjp.erp.util;

import com.yjp.erp.conf.exception.BusinessException;
import com.yjp.erp.model.vo.RetCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * description:
 * @author liushui
 * @date 2019/4/9
 */
public class MoquiTokenUtil {

    public static Map<String,String> getMoquiToken(String url)throws Exception{

        HttpResponse response = HttpClientUtils.getResponse(url);
        if (ObjectUtils.isEmpty(response.getFirstHeader("moquiSessionToken"))) {
            throw new BusinessException(RetCode.ERROR_TOKEN,"获取token失败");
        }
        String token = response.getFirstHeader("moquiSessionToken").getValue();
        if(StringUtils.isBlank(token)){
            throw new BusinessException(RetCode.UNAUTHORIZED,"权限获取失败");
        }

        Map<String,String> map = new HashMap<>();
        map.put("moquiSessionToken",token);
        return map;
    }
}
