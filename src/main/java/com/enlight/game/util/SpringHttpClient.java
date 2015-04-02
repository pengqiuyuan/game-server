package com.enlight.game.util;

import java.io.IOException;

import java.util.Map;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class SpringHttpClient {


	
	private RestTemplate restTemplate = new RestTemplate();
	

	/**
	 * 通过GET 发起请求
	 * @param url
	 * @param t
	 * @param prams
	 * @return
	 */
    public Map<String,Object> getMethod(String url ,String... prams){
    	
    	ResponseEntity<String> resEntity = restTemplate.getForEntity(url, String.class, prams);
        ObjectMapper mapper = new ObjectMapper();
    	Map<String, Object> map = null;
		try {
			map = mapper.readValue(resEntity.getBody(), Map.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
    	
    	return map;	
    }

    
    public String getMethodStr(String url ,String... prams){
    	ResponseEntity<String> resEntity = restTemplate.getForEntity(url, String.class, prams);
        return resEntity.getBody();
    }
    
 
//  public static void main(String[] args) {
//	       new SpringHttpClient().getMethodStr("http://117.27.88.66:82/protFace/subSmsApi.aspx?userID={0}&cpKey={1}&uPhone={2}&content={3}", "52","389344EE6D521EB1AD0C249071921BCC","15859136519","中国人");
//  }

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
    
}
