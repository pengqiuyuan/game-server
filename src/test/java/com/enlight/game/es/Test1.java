package com.enlight.game.es;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.codehaus.jackson.type.TypeReference;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.Client;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

import com.enlight.game.entity.gm.fb.ServerStatus;
import com.enlight.game.util.HttpClientUts;
import com.enlight.game.util.JsonBinder;



@ContextConfiguration(locations = {"/applicationContext.xml"})
public class Test1 extends SpringTransactionalTestCase{
	
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	
	@Autowired
	private Client client;
	
	@Test
	public void test1() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		long startTime=System.currentTimeMillis();  
		System.out.println("开始");
		for (int i = 0; i < 20000; i++) {
			//String temp="create|2014-12-11 11:54:35.107|1|1|1|2|4|92947747373056|player_5|{\"createuser\":\"0\",\"role_id\":\"92947747373056\",\"level\":\"1\",\"ip\":\"183.60.92.253\",\"from\":\"0\"}\r\n";  
			bulkRequest.add(client.prepareIndex("logstash-fb-user-2015.11", "fb_user.log")
			        .setSource(jsonBuilder()
				           	 .startObject()
	                        .field("@version", "1")
	                        .field("索引", "0")
	                        .field("创建角色游戏ID", "11111111")
	                        .field("人物等级", "11111")
	                        .field("玩家登陆ip","183.60.92.253")
	                        .field("注册渠道", "0")
	                        .field("日志分类关键字", "create")
	                        .field("日期", "2014-12-11 11:54:35.107")
	                        .field("游戏ID", "111")
	                        .field("运营大区ID","111")
	                        .field("渠道ID", "111")
	                        .field("服务器ID", "111")
	                        .field("玩家平台ID", "1111")
	                        .field("玩家GUID", "111")
	                        .field("渠道ID", "111")
		                    .endObject()
			                  )
			        );
		}
		System.out.println("结束");
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}
		long endTime=System.currentTimeMillis(); //获取结束时间
		System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
	}
	
	//@Test
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
