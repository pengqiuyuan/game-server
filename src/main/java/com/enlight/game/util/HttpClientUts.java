package com.enlight.game.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * HTTP工具类
 * 
 */
public class HttpClientUts {

	public static void main(String arg[]) throws Exception {
/*	    String url = "http://10.0.10.251:8081/api/gameserver/v1/gift/add";
	    JSONObject params = new JSONObject();
	    params.put("SRC_STM_CODE", "wsf");
	    params.put("TENANT_ID", "123");
	    params.put("NM", "张三");
	    params.put("BRTH_DT", "1983-01-20");
	    params.put("GND_CODE", "1");
	    JSONArray params2 = new JSONArray();
	    JSONObject param3 = new JSONObject();
	    param3.put("DOC_TP_CODE", "10100");
	    param3.put("DOC_NBR", "100200198301202210");
	    param3.put("DOC_CUST_NM", "test");
	    params2.add(param3);
	    params.put("DOCS", params2);
	    String ret = doPost(url, params).toString();
	    System.out.println(ret);*/
	    
/*	    System.out.println(doGet("http://localhost:8080/game-server/login/findStores?loginName=test2", "utf-8") );
	    
	    String jsonOut = doGet("http://localhost:8080/game-server/login/findStores?loginName=test2", "utf-8");
	    
        JsonFlattener parser = new JsonFlattener();
        CSVWriter writer = new CSVWriter();
        List<Map<String, String>> flatJson = parser.parseJson(jsonOut);
        System.out.println("2222222222222   "  +flatJson);
        writer.writeAsCSV(flatJson, "D://sample.csv");*/
		
	  }

	  /**
	   httpClient的get请求方式2
	   * @return
	   * @throws Exception
	   */
	  public static String doGet(String url, String charset)
	      throws Exception {
	    /*
	     * 使用 GetMethod 来访问一个 URL 对应的网页,实现步骤: 1:生成一个 HttpClinet 对象并设置相应的参数。
	     * 2:生成一个 GetMethod 对象并设置响应的参数。 3:用 HttpClinet 生成的对象来执行 GetMethod 生成的Get
	     * 方法。 4:处理响应状态码。 5:若响应正常，处理 HTTP 响应内容。 6:释放连接。
	     */
	    /* 1 生成 HttpClinet 对象并设置参数 */
		System.out.println("111  "  + url);
	    HttpClient httpClient = new HttpClient();
	    // 设置 Http 连接超时为5秒
	    httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
	    /* 2 生成 GetMethod 对象并设置参数 */
	    GetMethod getMethod = new GetMethod(url);
	    // 设置 get 请求超时为 5 秒
	    getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
	    // 设置请求重试处理，用的是默认的重试处理：请求三次
	    getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,	new DefaultHttpMethodRetryHandler());
	    String response = "";
	    /* 3 执行 HTTP GET 请求 */
	    try {
	      int statusCode = httpClient.executeMethod(getMethod);
	      /* 4 判断访问的状态码 */
	      if (statusCode != HttpStatus.SC_OK) {
	        System.err.println("请求出错: "+ getMethod.getStatusLine());
	      }
	      /* 5 处理 HTTP 响应内容 */
	      // HTTP响应头部信息，这里简单打印
	      Header[] headers = getMethod.getResponseHeaders();
/*	      for (Header h : headers)
	        System.out.println(h.getName() + "------------ " + h.getValue());*/
	      // 读取 HTTP 响应内容，这里简单打印网页内容
	      byte[] responseBody = getMethod.getResponseBody();// 读取为字节数组
	      response = new String(responseBody, charset);
	      System.out.println("----------response:" + response);
	      // 读取为 InputStream，在网页内容数据量大时候推荐使用
	      // InputStream response = getMethod.getResponseBodyAsStream();
	    } catch (HttpException e) {
	      // 发生致命的异常，可能是协议不对或者返回的内容有问题
	      System.out.println("请检查输入的URL!");
	      e.printStackTrace();
	    } catch (IOException e) {
	      // 发生网络异常
	      System.out.println("发生网络异常!");
	      e.printStackTrace();
	    } finally {
	      /* 6 .释放连接 */
	      getMethod.releaseConnection();
	    }
	    return response;
	  }

	  /**
	   * post请求
	   * @param url
	   * @param json
	   * @return
	   */
	  
	  public static JSONObject doPost(String url,JSONObject json){
	    DefaultHttpClient client = new DefaultHttpClient();
	    HttpPost post = new HttpPost(url);
	    JSONObject response = null;
	    try {
	    	
 /*         StringEntity entity1 = new StringEntity(json.toString(), "UTF-8");  
          entity1.setContentType("text/xml;charset=UTF-8");  
          entity1.setContentEncoding("UTF-8");  
          post.setHeader("Content-Type", "text/xml;charset=UTF-8");  
          post.setEntity(entity1);  */
	    	
	      StringEntity s = new StringEntity(json.toString(), "UTF-8");
	      s.setContentEncoding("UTF-8");
	      s.setContentType("text/xml; charset=utf-8");//发送json数据需要设置contentType
	      post.setHeader("Content-Type", "text/xml;charset=UTF-8");  
	      post.setEntity(s);
	      
	      
	      
	      HttpResponse res = client.execute(post);
	      if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
	        HttpEntity entity = res.getEntity();
	        String result = EntityUtils.toString(res.getEntity());// 返回json格式：
	        response = JSONObject.fromObject(result);
	      }
	    } catch (Exception e) {
	      throw new RuntimeException(e);
	    }
	    return response;
	  }
	  
}
