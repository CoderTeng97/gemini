package com.mm.gemini.helper.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * http请求发送工具
 */
public class HttpClientUtil {


	/**
	 * 发送GET请求并返回结果
	 */
	public static String doGet(String url) {
		String result = null;
		// 创建默认的httpClient实例
		CloseableHttpClient httpClient = getHttpClient();
		try {
			// 用get方法发送http请求
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("token",getRequest().getHeader("token"));
			System.out.println("执行get请求:" + httpGet.getURI());
			CloseableHttpResponse httpResponse = null;
			// 发送get请求
			httpResponse = httpClient.execute(httpGet);
			try {
				// response实体
				HttpEntity entity = httpResponse.getEntity();
				result = EntityUtils.toString(entity, "UTF-8");
			} finally {
				httpResponse.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				closeHttpClient(httpClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;

	}
	public static String doPostForJson(String url, String jsonParams){
		String result = "";
		CloseableHttpResponse httpResponse = null;
		CloseableHttpClient httpClient = getHttpClient();
		RequestConfig requestConfig = RequestConfig.custom().
				setConnectTimeout(180 * 1000).setConnectionRequestTimeout(180 * 1000)
				.setSocketTimeout(180 * 1000).setRedirectsEnabled(true).build();
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-type", "application/json; charset=utf-8");
		httpPost.addHeader("token",getRequest().getHeader("token"));
		httpPost.setConfig(requestConfig);
		try {
			httpPost.setEntity(new StringEntity(jsonParams,"utf-8"));
			httpResponse=httpClient.execute(httpPost);
			try {
				HttpEntity entity = httpResponse.getEntity();
				result = EntityUtils.toString(entity);
			}finally {
				httpResponse.close();
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				closeHttpClient(httpClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return  result;
	}

	private static  HttpServletRequest getRequest(){
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = sra.getRequest();
		return request;
	}


	/**
	 * 打开一个默认的httpclient连接
	 * @return
	 */
	private static CloseableHttpClient getHttpClient() {
		return HttpClients.createDefault();
	}
	/**
	 * 关闭client连接
	 * @return
	 */
	private static void closeHttpClient(CloseableHttpClient client) throws IOException {
		if (null!=client) {
			client.close();
		}
	}
}
