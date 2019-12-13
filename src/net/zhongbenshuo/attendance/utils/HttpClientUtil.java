package net.zhongbenshuo.attendance.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {
    private static PoolingHttpClientConnectionManager conManager; 
    private static String ENCODING_UTF_8 = "UTF-8";  
  
    
    /**
     * 初始化
     * setMaxTotal 连接池管理最大连接数
     * setDefaultMaxPerRoute 路由到主机的默认最大限制连接数，即允许并发数量
     */
    private static void init() {
        if (conManager == null) {
            conManager = new PoolingHttpClientConnectionManager();
            conManager.setMaxTotal(50);
            conManager.setDefaultMaxPerRoute(20);
        }
    }
  
    
    /** 
     * 通过 HttpClientBuilder 从连接池获取HttpClient
     * CloseableHttpClient 抽象子类 AbstractHttpClient
     */  
    private static CloseableHttpClient getHttpClient() {
        init();
        HttpClientBuilder builder = HttpClients.custom();
        return builder.setConnectionManager(conManager).build();
    }  
  
    
    /** 
     * 不入参get请求处理 并返回值
     * 
     * AbstractHttpMessage
     *    - HttpRequestsBase
     *       - HttpGet
     * @param url 请求地址
     * @return 请求响应字符串
     */  
    public static String httpGetRequest(String url) {
        HttpGet httpGet = new HttpGet(url);
        return getResult(httpGet);
    }  
  
    /**
     * 入参get请求处理 并返回值
     * @param url 请求地址
     * @param map 请求参数
     * @return 请求响应字符串
     * @throws URISyntaxException
     */
    public static String httpGetRequest(String url, Map<String, Object> map) throws URISyntaxException {
        ArrayList<NameValuePair> pairs = covertParamsIntoNVP(map);
        // builder 设参
        URIBuilder ubuilder = new URIBuilder();
        ubuilder.setPath(url);
        ubuilder.setParameters(pairs);
        
        HttpGet httpGet = new HttpGet(ubuilder.build());
        return getResult(httpGet);
    }  
  
    
    /**
     * 请求url入参  请求头入参 并返回请求响应
     * @param url 请求地址
     * @param headers 请求头参数
     * @param params 请求url参数
     * @return 请求响应字符串
     * @throws URISyntaxException
     */
    public static String httpGetRequest(String url, Map<String, Object> headers, Map<String, Object> params)
            throws URISyntaxException {
        URIBuilder ubuilder = new URIBuilder();
        ubuilder.setPath(url);
        
        // builder 设参
        ArrayList<NameValuePair> pairs = covertParamsIntoNVP(params);
        ubuilder.setParameters(pairs);
        
        // get header 设参
        HttpGet httpGet = new HttpGet(ubuilder.build());
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }  
        // 返回响应
        return getResult(httpGet);
    }  
  
    
    
    /** 
     * 不入参post请求处理 并返回值
     * @param url 请求地址
     * @return 请求响应字符串
     */  
    public static String httpPostRequest(String url) {
        HttpPost httpPost = new HttpPost(url);
        return getResult(httpPost);
    }  
  
    
    /**
     * 入参post请求处理 并返回值
     * @param url 请求地址
     * @param map 请求参数
     * @return 请求响应字符串
     * @throws URISyntaxException
     */
    public static String httpPostRequest(String url, Map<String, Object> map) throws UnsupportedEncodingException {
        ArrayList<NameValuePair> pairs = covertParamsIntoNVP(map);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, ENCODING_UTF_8));
        return getResult(httpPost);
    }  
  
    
    /**
     * post 请求url入参  请求头入参 并返回请求响应
     * @param url 请求地址
     * @param headers 请求头参数
     * @param params 请求url参数
     * @return 请求响应字符串
     * @throws URISyntaxException
     */
    public static String httpPostRequest(String url, Map<String, Object> headers, Map<String, Object> params)
            throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        
        // header 设参
        for (Map.Entry<String, Object> p : headers.entrySet()) {
            httpPost.addHeader(p.getKey(), String.valueOf(p.getValue()));
        }

        // post UrlEncodedFormEntity 设参
        ArrayList<NameValuePair> pairs = covertParamsIntoNVP(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, ENCODING_UTF_8));
        // 请求响应
        return getResult(httpPost);
    }
  
    
    /**
     * 参数设置 工具方法
     * @param map
     * @return
     */
    private static ArrayList<NameValuePair> covertParamsIntoNVP(Map<String, Object> map) {
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        if (!map.isEmpty()) {
            for (Map.Entry<String, Object> p : map.entrySet()) {
                pairs.add(new BasicNameValuePair(p.getKey(), String.valueOf(p.getValue())));
            }
        }
        return pairs;
    }  
  
    /** 
     * 处理Http请求 
     *  
     * @param request 
     * @return 
     */  
    private static String getResult(HttpRequestBase request) {
        //创建默认方式的httpClient实例
        //CloseableHttpClient httpClient = HttpClients.createDefault();
        
        // 从连接池获取一个client
        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse response = null;
        try {
             response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity,"utf-8");
                response.close();
                return result;
            }  
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace(); 
        } finally {
        	try {
        		 response.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
        	
        }
        return "";
    }


	public static String httpPostRequestByEntity(String url, StringEntity entity) {
		HttpPost httpPost = new HttpPost(url);
    	httpPost.setEntity(entity);
    	return getResult(httpPost);
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
			resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return resultString;
	}
	
	public static String doPostJsonUtf8(String url, String json) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建请求内容
			StringEntity entity = new StringEntity(json, "UTF-8");
			httpPost.setEntity(entity);
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return resultString;
	}
	
	public static String doPostJsonParams(String url, String json) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建请求内容
			StringEntity entity = new StringEntity(json, "UTF-8");
			entity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(entity);
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultString;
	}


	/**
     * 发送请求
     * @param url 请求地址
     * @param json  数据
     * @param contentType  编码
     * @return
     */
    public static  String post(String url, String json,String contentType)
    {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        //HttpClient
        CloseableHttpClient client = httpClientBuilder.build();
        client = (CloseableHttpClient) wrapClient(client);
        HttpPost post = new HttpPost(url);
        try
        {
            StringEntity s = new StringEntity(json,"utf-8");
            if(StringUtils.isBlank(contentType)){
                s.setContentType("application/json");
            }
            s.setContentType(contentType);
            post.setEntity(s);
            HttpResponse res = client.execute(post);
            HttpEntity entity = res.getEntity();
            String str= EntityUtils.toString(entity, "utf-8");
            return str;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    private static HttpClient wrapClient(HttpClient base) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLSv1");
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs,
                                               String string) throws CertificateException {
                }
 
                public void checkServerTrusted(X509Certificate[] xcs,
                                               String string) throws CertificateException {
                }
 
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(ctx, new String[] { "TLSv1" }, null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            return httpclient;
 
        } catch (Exception ex) {
            return null;
        }
    }
}
