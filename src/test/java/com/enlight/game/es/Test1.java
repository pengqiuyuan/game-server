package com.enlight.game.es;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import net.sf.json.JSONObject;

import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

import com.enlight.game.entity.fb.gm.ServerStatus;
import com.enlight.game.util.HttpClientUts;
import com.enlight.game.util.JsonBinder;



@ContextConfiguration(locations = {"/applicationContext.xml"})
public class Test1 extends SpringTransactionalTestCase{
	
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	
	@Test
	public void test13() throws IOException, ParseException {	
		String gs = "[{\"id\":1,\"serverzoneId\":\"Sample text\",\"gameId\":\"Sample text\",\"serverId\":\"Sample text\",\"serverName\":\"Sample text\",\"status\":\"Sample text\"}]";
		//gs = HttpClientUts.doGet("http://playground.apistudio.io/try/346bf9ed-3939-4c88-bcfb-a24a7339abb2/fbserver/server/getAllServer"+"?serverZoneId="+1+"&gameId="+1+"&pageNumber="+1+"&pageSize="+1, "utf-8");
        List<ServerStatus> beanList = binder.getMapper().readValue(gs, new TypeReference<List<ServerStatus>>() {}); 
        
		String total;
		try {
			total = HttpClientUts.doGet("http://playground.apistudio.io/try/346bf9ed-3939-4c88-bcfb-a24a7339abb2/fbserver/server/getAllServerTotal"+"?serverZoneId="+1+"&gameId="+1, "utf-8");
			JSONObject  dataJson=JSONObject.fromObject(total);
			System.out.println("22222  "+dataJson.get("num"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
	}
	
	
}
