package com.taotao.common;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 网络请求工具类
 */
public class HttpUtil {
	private static HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
		@Override
		public boolean retryRequest(IOException e, int i, HttpContext httpContext) {
			return false;
		}
	};

	private static CloseableHttpClient httpClient = HttpClientBuilder.create()
			.setUserAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36")
//			.setDefaultHeaders()
			.setRetryHandler(retryHandler)
			.build();

	public static String doGet(String url) {
		return doGet(url, "UTF-8");
	}

	public static String doGet(String url, String charset) {
		HttpGet getMethod = new HttpGet(url);
		try {
			HttpResponse resp = httpClient.execute(getMethod);
			return EntityUtils.toString(resp.getEntity(), charset);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String doGet(String url, Map<String, String> params) {
		return doGet(url, params, "UTF-8");
	}

	public static String doGet(String url, Map<String, String> params, String charset) {
		try {
			URIBuilder uriBuilder = new URIBuilder(url);
			uriBuilder.setCharset(Charset.forName(charset));
			if(params != null && params.size() > 0) {
				for(String key : params.keySet()) {
					uriBuilder.addParameter(key, params.get(key));
				}
			}
			HttpGet getMethod = new HttpGet(url);
			HttpResponse resp = httpClient.execute(getMethod);
			return EntityUtils.toString(resp.getEntity(), charset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String doPost(String url) {
		return doPost(url, null, "UTF-8");
	}

	public static String doPost(String url, Map<String, String> params) {
		return doPost(url, params, "UTF-8");
	}

	public static String doPost(String url, Map<String, String> params, String charset) {

		HttpPost postMethod = new HttpPost(url);
		try {
			if(params != null && params.size() > 0) {
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				for(String key : params.keySet()) {
					nvps.add(new BasicNameValuePair(key, params.get(key)));
				}
				postMethod.setEntity(new UrlEncodedFormEntity(nvps,charset));
			}
			HttpResponse resp = httpClient.execute(postMethod);
			return EntityUtils.toString(resp.getEntity(), charset);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	public static String doPostJson(String url, String json) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建请求内容
			StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resultString;
	}
	public static String getFullUrl(HttpServletRequest request) {
		return request.getRequestURL().toString();
	}


}
