package com.enlight.game.es;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

import com.enlight.game.entity.analysis.UserRetained;
import com.enlight.game.service.serverZone.ServerZoneService;
import com.enlight.game.util.JsonBinder;

@ContextConfiguration(locations = {"/applicationContext.xml"})
public class EsServerTest extends SpringTransactionalTestCase{
	@Autowired
	private Client client;
	
	@Autowired
	private ServerZoneService serverZoneService;
	
	EsUtilTest esUtilTest = new EsUtilTest();
	
	public Long createServerCount(String key,String from , String to){
		FilteredQueryBuilder builder2 = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("日期").from(from).to(to),
		        		FilterBuilders.termFilter("日志分类关键字", "create"),
		        		FilterBuilders.termFilter("服务器ID", key))
		        );
		SearchResponse sr = client.prepareSearch().setSearchType("count").setTypes("fb_userlog").setQuery(builder2).execute().actionGet();
		System.out.println(sr);
		Long count = sr.getHits().getTotalHits();
		return count;
	}
	
	public void bulk(UserRetained userRetained) throws IOException{
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		bulkRequest.add(client.prepareIndex("log_retained", "fb_retained")
		        .setSource(jsonBuilder()
			           	 .startObject()
	                        .field("date", userRetained.getDate().split("T")[0])
	                        .field("gameId", userRetained.getGameId())
	                        .field("ctRetained", userRetained.getCtRetained())
	                        .field("retained", userRetained.getRetained())
	                        .field("key", userRetained.getKey())
	                        .field("ts","["+userRetained.getTs()+","+userRetained.getRetained()+"]")
	                        .field("value", userRetained.getValue())
	                    .endObject()
		                  )
		        ).execute().actionGet();
	}
	
	//platForm
	//@Test
	public void test9() throws IOException {	
		//计算时间（当前）2015-04-15 ，统计出2015-04-14到2015-04-15的数据 ，得出2015-04-13的次日留存、得出2015-04-07的7日留存、得出2015-03-15的30日留存
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数  
		
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(
		        QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
		        		FilterBuilders.termFilter("日志分类关键字", "login"))
		        );
		SearchResponse sr = client.prepareSearch().setSearchType("count").setTypes("fb_userlog").setQuery(builder)
			    .addAggregation(
			    		AggregationBuilders.terms("create").field("注册时间").order(Terms.Order.term(false)).size(35)
			    		.subAggregation(AggregationBuilders.cardinality("agg").field("玩家GUID"))
			    		.subAggregation(AggregationBuilders.terms("server").field("服务器ID").size(300).subAggregation(AggregationBuilders.cardinality("agg").field("玩家GUID")))
			    ).execute().actionGet();
		System.out.println(sr);
		System.out.println("--------------------------");
		
		Terms genders = sr.getAggregations().get("create");	
		for (Terms.Bucket entry : genders.getBuckets()) {
			String dateBucket = sdf.format(new Date((Long) entry.getKeyAsNumber()));
			if(dateBucket.equals(esUtilTest.twoDayAgoFrom())){
				Terms serverZone = entry.getAggregations().get("server");
				for (Terms.Bucket e : serverZone.getBuckets()) {
				    Cardinality agg = e.getAggregations().get("agg");
				    Long aggcount = agg.getValue();
				    Double RetentionTwo ;
				    
			    	RetentionTwo = (double)aggcount*100/createServerCount(e.getKey(),esUtilTest.twoDayAgoFrom(), esUtilTest.twoDayAgoTo());
			    	UserRetained userRetained = new UserRetained();
			    	userRetained.setDate(esUtilTest.twoDayAgoFrom());
			    	userRetained.setGameId("fb");
			    	userRetained.setKey(UserRetained.KEY_SEVSER);
			    	userRetained.setCtRetained(UserRetained.CT_NEXTDAY);
			    	userRetained.setRetained(df.format(RetentionTwo));
			    	userRetained.setValue(e.getKey());
			    	userRetained.setTs(entry.getKey());
			    	bulk(userRetained);
			    	
				}
			}else if(dateBucket.equals(esUtilTest.eightDayAgoFrom())){
				Terms serverZone = entry.getAggregations().get("server");
				for (Terms.Bucket e : serverZone.getBuckets()) {
				    Cardinality agg = e.getAggregations().get("agg");
				    Long aggcount = agg.getValue();
				    Double RetentionEight ;
				    
				    RetentionEight = (double)aggcount*100/createServerCount(e.getKey(),esUtilTest.eightDayAgoFrom(), esUtilTest.eightDayAgoTo());
			    	UserRetained userRetained = new UserRetained();
			    	userRetained.setDate(esUtilTest.eightDayAgoFrom());
			    	userRetained.setGameId("fb");
			    	userRetained.setKey(UserRetained.KEY_SEVSER);
			    	userRetained.setCtRetained(UserRetained.CT_SEVENDAY);
			    	userRetained.setRetained(df.format(RetentionEight));
			    	userRetained.setValue(e.getKey());
			    	userRetained.setTs(entry.getKey());
			    	bulk(userRetained);
				}
			}else if(dateBucket.equals(esUtilTest.thirtyOneDayAgoFrom())){
				Terms serverZone = entry.getAggregations().get("server");
				for (Terms.Bucket e : serverZone.getBuckets()) {
				    Cardinality agg = e.getAggregations().get("agg");
				    Long aggcount = agg.getValue();
				    Double RetentionThirty ;
				    
			    	RetentionThirty = (double)aggcount*100/createServerCount(e.getKey(),esUtilTest.thirtyOneDayAgoFrom(), esUtilTest.thirtyOneDayAgoTo());
			    	UserRetained userRetained = new UserRetained();
			    	userRetained.setDate(esUtilTest.thirtyOneDayAgoFrom());
			    	userRetained.setGameId("fb");
			    	userRetained.setKey(UserRetained.KEY_SEVSER);
			    	userRetained.setCtRetained(UserRetained.CT_THIRYTDAY);
			    	userRetained.setRetained(df.format(RetentionThirty));
			    	userRetained.setValue(e.getKey());
			    	userRetained.setTs(entry.getKey());
			    	bulk(userRetained);
				}
			}
		}
		
	}
	

	//散列表 无序
	private static final String index = "log_fb_user";
	
	private static final String type = "fb_user_total";
	
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	
	@Test
	public void test() throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index)
		        .setTypes(type)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from("2014-04-12").to("2015-05-14"),
		                		FilterBuilders.termFilter("key", "server"),
		                		FilterBuilders.termFilter("value", "3"))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween("2014-04-12","2015-05-14")).setExplain(true)
		        .execute()
		        .actionGet();
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (SearchHit hit : response.getHits()) {
			Map<String, Object> source = hit.getSource();
			System.out.println(source);
			map.put(source.get("date").toString(), source.get("userTotal").toString());
		}
		System.out.println(map);
		
		System.out.println(binder.toJson(map));
		
	}
	
	public static int daysBetween(String smdate,String bdate) throws ParseException{  
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(sdf.parse(smdate));    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(sdf.parse(bdate));    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
        return Integer.parseInt(String.valueOf(between_days));     
    }  
	
	
}
