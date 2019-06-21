package com.yjp.erp.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import com.yjp.erp.model.vo.JsonResult;
/**
 * 
 * @ClassName: InvokeMoquiUtils   
 * @Description: 调用moqui工具类
 * @author: jihongjin@yijiupi.com
 * @date: 2019年4月16日 下午4:36:48   
 *  
 * @Copyright: 2019 www.yijiupi.com Inc. All rights reserved.
 */
public class InvokeMoquiUtils {
	
	private static final Logger log = LoggerFactory.getLogger(InvokeMoquiUtils.class);
	
	/**
	 * 
	 * @Title: remoteCallMoqui
	 * @Description: 远程调用moqui
	 * @param: reqPath 请求路径
	 * @param: paramStr 入参（json字符串）
	 * @param: @throws Exception
	 * @return: JsonResult
	 * @throws
	 */
	@SuppressWarnings("rawtypes")
	public static JsonResult remoteCallMoqui(String moquiBaseRest, String reqPath, String paramStr) throws Exception {
		/**
		 * 组装moqui请求url地址
		 */
		StringBuffer sb = new StringBuffer();
		sb.append(moquiBaseRest).append(reqPath);
		String url = sb.toString();
		log.debug("url：" + url);
		/**
		 * 请求token将其放入请求头并设置以json格式传输
		 */
		Map<String, String> headers = Maps.newConcurrentMap();// MoquiTokenUtil.getMoquiToken(moquiUrl);
		headers.put("Content-Type", "application/json");
		/**
		 * 远程调用moqui
		 */
		log.debug("param：" + paramStr);
		long start = System.currentTimeMillis();
		String ret = HttpClientUtils.postParameters(url, paramStr, headers);
		/**
		 * 格式化返回结果
		 */
		JsonResult result = JSON.parseObject(ret, JsonResult.class);
		long end = System.currentTimeMillis();
		log.debug("调用moqui耗时："+(end-start)+"毫秒，返回结果：" + JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));
		return ObjectUtils.isNotEmpty(result) ? result : new JsonResult();
	}
}
