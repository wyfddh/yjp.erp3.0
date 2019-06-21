package com.yjp.erp.util;


import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.omg.CORBA.TIMEOUT;
import org.springframework.http.HttpStatus;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * http服务请求工具
 */
@SuppressWarnings("deprecation")
public class HttpClientUtils
{

	private static final int CONNECT_TIMEOUT = 10000;
	private static final int READ_TIMEOUT = 10000;
	private static final String CHARSET = "UTF-8";
	private static HttpClient client = null;

	static
	{
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(128);
		cm.setDefaultMaxPerRoute(128);
		client = HttpClients.custom().setConnectionManager(cm).build();
		// client = HttpClients.createDefault();
	}

	public static String postParameters(String url, String params,Map<String, String> header) throws Exception
	{
		HttpEntity entity = new StringEntity(params, CHARSET);
		return post(url, entity, header, CONNECT_TIMEOUT, READ_TIMEOUT);
	}

	/**
	 * @author wyf
	 * @description   简单封装post
	 * @date  2019/4/23 上午 10:09
	 * @param url  链接
	 * @param object  需传入的对象
	 * @return java.lang.String
	 */
	public static String postSimple(String url,Object object) throws Exception{

		Map<String, Object> params = ObjectUtils.objectToMap(object);
		Map<String,String> headers = new HashMap<>(4);
		headers.put("Content-Type","application/json");
		return postParameters(url, JSON.toJSONString(params), headers);
	}

	public static String get(String url) throws Exception
	{
		return get(url, CHARSET, null, null);
	}

	public static HttpResponse getResponse(String url) throws Exception
	{
		return getResponse(url, CHARSET, CONNECT_TIMEOUT, READ_TIMEOUT);
	}

	public static String putParameters(String url, Map<String, Object> params,Map<String, String> headers) throws Exception
	{
		StringEntity se = new StringEntity(JSON.toJSONString(params), CHARSET);
		return put(url, se, headers, CONNECT_TIMEOUT, READ_TIMEOUT);
	}

	public static String delete(String url) throws Exception
	{
		return delete(url, CHARSET, null, null);
	}

	/**
	 * 发送一个 Post 请求, 使用指定的字符集编码.
	 * @param url
	 * @param body RequestBody
	 * @param mimeType 例如 application/xml "application/x-www-form-urlencoded" a=1&b=2&c=3
	 * @param charset 编码
	 * @param connTimeout 建立链接超时时间,毫秒.
	 * @param readTimeout 响应超时时间,毫秒.
	 * @return ResponseBody, 使用指定的字符集编码.
	 * @throws ConnectTimeoutException 建立链接超时异常
	 * @throws SocketTimeoutException 响应超时
	 * @throws Exception
	 */
	public static String post(String url, String body, String mimeType, String charset, Integer connTimeout, Integer readTimeout) throws Exception
	{
		HttpClient client = null;
		HttpPost post = new HttpPost(url);
		String result = "";
		try
		{
			if(StringUtils.isNotBlank(body))
			{
				HttpEntity entity = new StringEntity(body, ContentType.create(mimeType, charset));
				post.setEntity(entity);
			}
			// 设置参数
			Builder customReqConf = RequestConfig.custom();
			if(connTimeout != null)
			{
				customReqConf.setConnectTimeout(connTimeout);
			}
			if(readTimeout != null)
			{
				customReqConf.setSocketTimeout(readTimeout);
			}
			post.setConfig(customReqConf.build());

			HttpResponse res;
			if(url.startsWith("https"))
			{
				// 执行 Https 请求.
				client = createSSLInsecureClient();
				res = client.execute(post);
			}
			else
			{
				// 执行 Http 请求.
				client = HttpClientUtils.client;
				res = client.execute(post);
			}
			result = IOUtils.toString(res.getEntity().getContent(), charset);
		}
		finally
		{
			post.releaseConnection();
			if(url.startsWith("https") && client != null && client instanceof CloseableHttpClient)
			{
				((CloseableHttpClient) client).close();
			}
		}
		return result;
	}

	// region 私有方法
	/**
	 * 从 response 里获取 charset
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unused")
	private static String getCharsetFromResponse(HttpResponse response)
	{
		// Content-Type:text/html; charset=GBK
		if(response.getEntity() != null && response.getEntity().getContentType() != null && response.getEntity().getContentType().getValue() != null)
		{
			String contentType = response.getEntity().getContentType().getValue();
			if(contentType.contains("charset="))
			{
				return contentType.substring(contentType.indexOf("charset=") + 8);
			}
		}
		return null;
	}


	/**
	 * 创建 SSL连接
	 * @return
	 * @throws GeneralSecurityException
	 */
	private static CloseableHttpClient createSSLInsecureClient() throws GeneralSecurityException
	{

		try
		{
			CloseableHttpClient httpclient = new DefaultHttpClient();
			// Secure Protocol implementation.
			SSLContext ctx = SSLContext.getInstance("SSL");
			// Implementation of a trust manager for X509 certificates
			X509TrustManager tm = new X509TrustManager()
			{
				@Override
				public void checkClientTrusted(X509Certificate[] xcs, String string)
				{

				}
				@Override
				public void checkServerTrusted(X509Certificate[] xcs, String string)
				{
				}
				@Override
				public X509Certificate[] getAcceptedIssuers()
				{
					return null;
				}
			};
			ctx.init(null, new TrustManager[]{tm}, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);

			ClientConnectionManager ccm = httpclient.getConnectionManager();
			// register https protocol in httpclient's scheme registry
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", 40443, ssf));
			return httpclient;
		}
		catch(GeneralSecurityException e)
		{
			throw e;
		}


	}

	// region http 方法
	/**
	 * 提交form表单
	 * @param url
	 * @param connTimeout
	 * @param readTimeout
	 * @return
	 * @throws ConnectTimeoutException
	 * @throws SocketTimeoutException
	 * @throws Exception
	 */
	public static String post(String url, HttpEntity entity, Map<String, String> headers, Integer connTimeout, Integer readTimeout) throws Exception
	{

		HttpClient client = null;
		HttpPost post = new HttpPost(url);
		try
		{
			if(entity != null)
			{
				post.setEntity(entity);
			}

			if(headers != null && !headers.isEmpty())
			{
				for(Entry<String, String> entry : headers.entrySet())
				{
					post.addHeader(entry.getKey(), entry.getValue());
				}
			}
			// 设置参数
			Builder customReqConf = RequestConfig.custom();
			if(connTimeout != null)
			{
				customReqConf.setConnectTimeout(connTimeout);
			}
			if(readTimeout != null)
			{
				customReqConf.setSocketTimeout(readTimeout);
			}
			post.setConfig(customReqConf.build());
			HttpResponse res ;
			if(url.startsWith("https"))
			{
				// 执行 Https 请求.
				client = createSSLInsecureClient();
				//  HttpHost host = new HttpHost(post.getURI().getHost(), 443, "https");
				res = client.execute(post);
			}
			else
			{
				// 执行 Http 请求.
				client = HttpClientUtils.client;
				res = client.execute(post);
			}
			if(res.getStatusLine().getStatusCode() == HttpStatus.OK.value())
			{
				return IOUtils.toString(res.getEntity().getContent(), "UTF-8");
			}
			else
			{
				throw new HttpResponseException(res.getStatusLine().getStatusCode(), res.getStatusLine().getReasonPhrase());
			}
		}
		finally
		{
			post.releaseConnection();
			if(url.startsWith("https") && client != null && client instanceof CloseableHttpClient)
			{
				((CloseableHttpClient) client).close();
			}
		}
	}


	/**
	 * 发送一个 GET 请求
	 * @param url
	 * @param charset
	 * @param connTimeout 建立链接超时时间,毫秒.
	 * @param readTimeout 响应超时时间,毫秒.
	 * @return
	 * @throws ConnectTimeoutException 建立链接超时
	 * @throws SocketTimeoutException 响应超时
	 * @throws Exception
	 */
	public static String get(String url, String charset, Integer connTimeout, Integer readTimeout) throws Exception
	{

		HttpClient client = null;
		HttpGet get = new HttpGet(url);
		String result = "";
		try
		{
			// 设置参数
			Builder customReqConf = RequestConfig.custom();
			if(connTimeout != null)
			{
				customReqConf.setConnectTimeout(connTimeout);
			}
			if(readTimeout != null)
			{
				customReqConf.setSocketTimeout(readTimeout);
			}
			get.setConfig(customReqConf.build());

			HttpResponse res = null;

			if(url.startsWith("https"))
			{
				// 执行 Https 请求.
				client = createSSLInsecureClient();
				res = client.execute(get);
			}
			else
			{
				// 执行 Http 请求.
				client = HttpClientUtils.client;
				res = client.execute(get);
			}

			result = IOUtils.toString(res.getEntity().getContent(), charset);
		}
		finally
		{
			get.releaseConnection();
			if(url.startsWith("https") && client != null && client instanceof CloseableHttpClient)
			{
				((CloseableHttpClient) client).close();
			}
		}
		return result;
	}

	/**
	 * 发送一个 GET 请求，返回请求头信息
	 * @param url
	 * @param charset
	 * @param connTimeout 建立链接超时时间,毫秒.
	 * @param readTimeout 响应超时时间,毫秒.
	 * @return
	 * @throws ConnectTimeoutException 建立链接超时
	 * @throws SocketTimeoutException 响应超时
	 * @throws Exception
	 */
	public static HttpResponse getResponse(String url, String charset, Integer connTimeout, Integer readTimeout) throws Exception
	{

		HttpClient client = null;
		HttpGet get = new HttpGet(url);
		HttpResponse res = null;
		try
		{
			// 设置参数
			Builder customReqConf = RequestConfig.custom();
			if(connTimeout != null)
			{
				customReqConf.setConnectTimeout(connTimeout);
			}
			if(readTimeout != null)
			{
				customReqConf.setSocketTimeout(readTimeout);
			}
			get.setConfig(customReqConf.build());

			if(url.startsWith("https"))
			{
				// 执行 Https 请求.
				client = createSSLInsecureClient();
				res = client.execute(get);
			}
			else
			{
				// 执行 Http 请求.
				client = HttpClientUtils.client;
				res = client.execute(get);
			}
		}
		finally
		{
			get.releaseConnection();
			if(url.startsWith("https") && client != null && client instanceof CloseableHttpClient)
			{
				((CloseableHttpClient) client).close();
			}
		}
		return res;
	}

	/**
	 * 发送一个 DELETE 请求
	 * @param url
	 * @param charset
	 * @param connTimeout 建立链接超时时间,毫秒.
	 * @param readTimeout 响应超时时间,毫秒.
	 * @return
	 * @throws ConnectTimeoutException 建立链接超时
	 * @throws SocketTimeoutException 响应超时
	 * @throws Exception
	 */
	public static String delete(String url, String charset, Integer connTimeout, Integer readTimeout) throws Exception
	{

		HttpClient client = null;
		HttpDelete delete = new HttpDelete(url);
		String result = "";
		try
		{
			// 设置参数
			Builder customReqConf = RequestConfig.custom();
			if(connTimeout != null)
			{
				customReqConf.setConnectTimeout(connTimeout);
			}
			if(readTimeout != null)
			{
				customReqConf.setSocketTimeout(readTimeout);
			}
			delete.setConfig(customReqConf.build());

			HttpResponse res = null;

			if(url.startsWith("https"))
			{
				// 执行 Https 请求.
				client = createSSLInsecureClient();
				res = client.execute(delete);
			}
			else
			{
				// 执行 Http 请求.
				client = HttpClientUtils.client;
				res = client.execute(delete);
			}

			result = IOUtils.toString(res.getEntity().getContent(), charset);
		}
		finally
		{
			delete.releaseConnection();
			if(url.startsWith("https") && client != null && client instanceof CloseableHttpClient)
			{
				((CloseableHttpClient) client).close();
			}
		}
		return result;
	}

	/**
	 * 发送一个 PUT 请求
	 * @param url
	 * @param connTimeout
	 * @param readTimeout
	 * @return
	 * @throws ConnectTimeoutException
	 * @throws SocketTimeoutException
	 * @throws Exception
	 */
	public static String put(String url, HttpEntity entity, Map<String, String> headers, Integer connTimeout, Integer readTimeout) throws Exception
	{

		HttpClient client = null;
		HttpPut put = new HttpPut(url);
		try
		{
			if(entity != null)
			{
				put.setEntity(entity);
			}

			if(headers != null && !headers.isEmpty())
			{
				for(Entry<String, String> entry : headers.entrySet())
				{
					put.addHeader(entry.getKey(), entry.getValue());
				}
			}
			// 设置参数
			Builder customReqConf = RequestConfig.custom();
			if(connTimeout != null)
			{
				customReqConf.setConnectTimeout(connTimeout);
			}
			if(readTimeout != null)
			{
				customReqConf.setSocketTimeout(readTimeout);
			}
			put.setConfig(customReqConf.build());
			HttpResponse res ;
			if(url.startsWith("https"))
			{
				// 执行 Https 请求.
				client = createSSLInsecureClient();
				//  HttpHost host = new HttpHost(post.getURI().getHost(), 443, "https");
				res = client.execute(put);
			}
			else
			{
				// 执行 Http 请求.
				client = HttpClientUtils.client;
				res = client.execute(put);
			}
			if(res.getStatusLine().getStatusCode() == HttpStatus.OK.value())
			{
				return IOUtils.toString(res.getEntity().getContent(), "UTF-8");
			}
			else
			{
				throw new HttpResponseException(res.getStatusLine().getStatusCode(), res.getStatusLine().getReasonPhrase());
			}
		}
		finally
		{
			put.releaseConnection();
			if(url.startsWith("https") && client != null && client instanceof CloseableHttpClient)
			{
				((CloseableHttpClient) client).close();
			}
		}
	}

	// endregion

	/**
	 * 将map转换成url
	 * @param map
	 * @return
	 */
	public static String getUrlParamsByMap(Map<String, Object> map) {
		if (map == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			sb.append(entry.getKey() + "=" + JSON.toJSONString(entry.getValue()));
			sb.append("&");
		}
		String s = sb.toString();
		if (s.endsWith("&")) {
			s = StringUtils.substringBeforeLast(s, "&");
		}
		return s;
	}

}