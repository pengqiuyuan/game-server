package com.enlight.game.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;  
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class HttpClientUtil<T>{
	
	private String login_url = "http://fx.icrazy800.com/user/login";

	private String find_url = "http://fx.icrazy800.com/find/marks";
	
	
	private String add_url = "http://fx.icrazy800.com/add/marks";

	private String reduction_url = "http://fx.icrazy800.com/reduction/marks";
	
	private String consumption_url = "http://fx.icrazy800.com/consumption/record";
	
	private String register_url = "http://fx.icrazy800.com/register";
	
	private String partner = "CEA2B707931A57F569FD6D961637D002";

	public Map<String, Object>  getMethod(String url,Map<String,String> map) throws ClientProtocolException, IOException {
	    if(map!=null){
		Set<String> set = map.keySet();
	     if(set.size() != 0)
	     {
	    	   url += "?";
	    	   for (String string : set ) 
	    	   {
	    		 url += (string+"="+map.get(string)+"&");
	    	   }
	     }
	    }
	    url = url.substring(0,url.length()-1);
	    HttpGet httpgets = new HttpGet(url);
	    HttpClient httpclient = new DefaultHttpClient();  
        HttpResponse response = httpclient.execute(httpgets);  
        HttpEntity entity = response.getEntity();
        String str =  "";
        if (entity != null) {    
            InputStream instreams = entity.getContent();    
            str = convertStreamToString(instreams);  
            ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map1 = mapper.readValue(str, Map.class);     
            httpgets.abort();    
            return map1;
        }  
		return null;
		
	}
	
	public Map<String, Object>  postMethod(String url,Map<String,String> map)  throws ClientProtocolException, IOException  {
		   HttpPost httpposts = new HttpPost(url);
		Set<String> set = map.keySet();
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
	     if(set.size() != 0)
	     {
	    	   for (String string : set ) 
	    	   {
	    		   nvps.add(new BasicNameValuePair(string,map.get(string)));
	    		
	    	   }
	     }
	   httpposts.setEntity(new UrlEncodedFormEntity(nvps)); 
	   HttpClient httpclient = new DefaultHttpClient();  
       HttpResponse response = httpclient.execute(httpposts);  
       HttpEntity entity = response.getEntity();
       String str =  "";
       if (entity != null) {    
           InputStream instreams = entity.getContent();    
           str = convertStreamToString(instreams);  
           ObjectMapper mapper = new ObjectMapper();   
		   Map<String, Object> map1 = mapper.readValue(str, Map.class);     
		   httpposts.abort();  
           return map1;
       }  
		return null;
		
	}
	
	
    private static String convertStreamToString(InputStream is) {      
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));      
        StringBuilder sb = new StringBuilder();      
       
        String line = null;      
        try {      
            while ((line = reader.readLine()) != null) {  
                sb.append(line + "\n");      
            }      
        } catch (IOException e) {      
            e.printStackTrace();      
        } finally {      
            try {      
                is.close();      
            } catch (IOException e) {      
               e.printStackTrace();      
            }      
            
        }      
       
        return sb.toString();      
    }  
    
  

public <T> T getMethod(String url,Map<String,String> map,Class<T> clazz) throws ClientProtocolException, IOException {
	    if(map!=null){
		Set<String> set = map.keySet();
	     if(set.size() != 0)
	     {
	    	   url += "?";
	    	   for (String string : set ) 
	    	   {
	    		 url += (string+"="+map.get(string)+"&");
	    	   }
	     }
	    }
	    url = url.substring(0,url.length()-1);
	    HttpGet httpgets = new HttpGet(url);
	    HttpClient httpclient = new DefaultHttpClient();  
        HttpResponse response = httpclient.execute(httpgets);  
        HttpEntity entity = response.getEntity();
        String str =  "";
        if (entity != null) {    
            InputStream instreams = entity.getContent();    
            str = convertStreamToString(instreams);  
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            httpgets.abort();    
            return  mapper.readValue(str, clazz);
        }  
		return null;
		
	}
	
	public <T> T  postMethod(String url,Map<String,String> map,Class<T> clazz)  throws ClientProtocolException, IOException,JsonParseException,JsonMappingException  {
		   HttpPost httpposts = new HttpPost(url);
		Set<String> set = map.keySet();
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
	     if(set.size() != 0)
	     {
	    	   for (String string : set ) 
	    	   {
	    		   nvps.add(new BasicNameValuePair(string,map.get(string)));
	    		
	    	   }
	     }
	   httpposts.setEntity(new UrlEncodedFormEntity(nvps)); 
	   HttpClient httpclient = new DefaultHttpClient();  
       HttpResponse response = httpclient.execute(httpposts);  
       HttpEntity entity = response.getEntity();
       String str =  "";
       if (entity != null) {    
           InputStream instreams = entity.getContent();    
           str = convertStreamToString(instreams);  
           ObjectMapper mapper = new ObjectMapper();
           mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		   httpposts.abort();  
		  
		   return  mapper.readValue(str, clazz);
       }  
		return null;
		
	}

	

public <T> T getMethod(String url,Map<String,String> map,Class<? extends T> clazz,Class<? extends T> clazz2) throws ClientProtocolException, IOException {
	    if(map!=null){
		Set<String> set = map.keySet();
	     if(set.size() != 0)
	     {
	    	   url += "?";
	    	   for (String string : set ) 
	    	   {
	    		 url += (string+"="+map.get(string)+"&");
	    	   }
	     }
	    }
	    url = url.substring(0,url.length()-1);
	    HttpGet httpgets = new HttpGet(url);
	    HttpClient httpclient = new DefaultHttpClient();  
        HttpResponse response = httpclient.execute(httpgets);  
        HttpEntity entity = response.getEntity();
        String str =  "";
        if (entity != null) {    
            InputStream instreams = entity.getContent();    
            str = convertStreamToString(instreams);  
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            httpgets.abort();  
            T t = null;
            		try {
            			t = mapper.readValue(str, clazz);
					} catch (JsonParseException e) {
						e.printStackTrace();
						t = mapper.readValue(str, clazz2);
					}
            return  t;
        }  
		return null;
		
	}
	
	public <T> T  postMethod(String url,Map<String,String> map,Class<? extends T> clazz,Class<? extends T> clazz2)  throws ClientProtocolException, IOException,JsonParseException,JsonMappingException  {
		   HttpPost httpposts = new HttpPost(url);
		Set<String> set = map.keySet();
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
	     if(set.size() != 0)
	     {
	    	   for (String string : set ) 
	    	   {
	    		   nvps.add(new BasicNameValuePair(string,map.get(string)));
	    		
	    	   }
	     }
	   httpposts.setEntity(new UrlEncodedFormEntity(nvps)); 
	   HttpClient httpclient = new DefaultHttpClient();  
       HttpResponse response = httpclient.execute(httpposts);  
       HttpEntity entity = response.getEntity();
       String str =  "";
       if (entity != null) {    
           InputStream instreams = entity.getContent();    
           str = convertStreamToString(instreams);  
           ObjectMapper mapper = new ObjectMapper();
           mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		   httpposts.abort();  
		  
		   T t = null;
   		   try {
   			t = mapper.readValue(str, clazz);
			   } catch (JsonMappingException e) {
				t = mapper.readValue(str, clazz2);
			   }
           return  t;
       }  
		return null;
		
	}

	
	public static void main(String[] args) {
		
	}
	
}
