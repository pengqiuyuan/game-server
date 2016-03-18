package com.enlight.game.es;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

import com.enlight.game.service.store.StoreService;
import com.enlight.game.util.EsUtil;



@ContextConfiguration(locations = {"/applicationContext.xml"})
public class EsActiveTest extends SpringTransactionalTestCase{
	@Autowired
	private Client client;
	
	//项目名称
	private static final String game = "FB";
	
	private static final String index = "logstash-fb-user-*";
	
	private static final String type = "fb_user.log";
	
	private static final String bulk_index = "log_fb_user";
	
	private static final String bulk_type_active_day = "fb_user_active_day";
	
	private static final String bulk_type_active_week = "fb_user_active_week";
	
	private static final String bulk_type_active_mouth = "fb_user_active_mouth";
	
	private static final Integer szsize = 10; //运营大区
	
	private static final Integer pfsize = 300; //渠道
	
	private static final Integer srsize = 300; //服务器
	
	@Autowired
	private StoreService storeService;
	
	EsUtil esUtilTest = new EsUtil();
	
	//@Test
	public void test13() throws IOException, ParseException {	
		SearchResponse sr = client.prepareSearch("logstash-fb-*").setSearchType("count").setTypes("fb_user.log")
			    .addAggregation(
			    		AggregationBuilders.cardinality("agg").field("人物等级")
			    ).execute().actionGet();
		System.out.println(sr);
	}
	
	//@Test
	public void test14() throws IOException, ParseException {	//all
		SearchResponse dayactive = client.prepareSearch("logstash-fb-*").setSearchType("count").setTypes("fb_user.log")
		        .setPostFilter(FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "login")
						))
		        .addAggregation(
			    		AggregationBuilders.cardinality("agg").field("玩家平台ID")
			    ).execute().actionGet();
		Cardinality agg = dayactive.getAggregations().get("agg");
		long value = agg.getValue();
		System.out.println(value);
		System.out.println(dayactive);
	}
	
	//@Test
	public void test17() throws IOException, ParseException {	
		
		SearchResponse weekactive = client.prepareSearch(index).setSearchType("count").setTypes(type)
		        .setPostFilter(FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.weekFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "login")
						))
		        .addAggregation(
						AggregationBuilders.terms("serverZone").field("运营大区ID").size(szsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		System.out.println(weekactive);
		Terms weekgenders = weekactive.getAggregations().get("serverZone");	
		for (Terms.Bucket e : weekgenders.getBuckets()) {
		    Cardinality agg = e.getAggregations().get("agg");
		    Long aggcount = agg.getValue();
		    System.out.println(aggcount);
		}
	}
	 
	
	//@Test 
	public void test15() throws IOException, ParseException {	//serverzone
		SearchResponse dayactive = client.prepareSearch("logstash-fb-*").setSearchType("count").setTypes("fb_user.log")
		        .setPostFilter(FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "login")
						))
		        .addAggregation(
						AggregationBuilders.terms("serverZone").field("运营大区ID").size(10).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家平台ID")
								)
			    ).execute().actionGet();
		System.out.println(dayactive);
		
		Terms genders = dayactive.getAggregations().get("serverZone");	
		for (Terms.Bucket e : genders.getBuckets()) {
		    Cardinality agg = e.getAggregations().get("agg");
		    Long aggcount = agg.getValue();
		    System.out.println(e.getKey()  + "  "  + e.getDocCount() + " " + aggcount);
		}    
	}
	
	
	//all
	@Test 
	public void esAll() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		System.out.println("###############  all 活跃玩家");
		SearchResponse dayactive = client.prepareSearch(index).setSearchType("count").setTypes(type)
		        .setPostFilter(FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "login")
						))
		        .addAggregation(
			    		AggregationBuilders.cardinality("agg").field("玩家GUID")
			    ).execute().actionGet();
		
		Cardinality aggDay = dayactive.getAggregations().get("agg");
		long valueDay = aggDay.getValue();
		
		bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_active_day)
		        .setSource(jsonBuilder()
			           	 .startObject()
	                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
	                        .field("gameId", game)
	                        .field("key", "all")
	                        .field("@timestamp", new Date())
	                        .field("ct","day")
	                        .field("cv", valueDay)
	                    .endObject()
		                  )
		        );
		
		SearchResponse weekactive = client.prepareSearch(index).setSearchType("count").setTypes(type)
		        .setPostFilter(FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.weekFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "login")
						))
		        .addAggregation(
			    		AggregationBuilders.cardinality("agg").field("玩家GUID")
			    ).execute().actionGet();
		
		Cardinality aggWeek = weekactive.getAggregations().get("agg");
		long valueWeek = aggWeek.getValue();
		
		bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_active_week)
		        .setSource(jsonBuilder()
			           	 .startObject()
	                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
	                        .field("gameId", game)
	                        .field("key", "all")
	                        .field("@timestamp", new Date())
	                        .field("ct","week")
	                        .field("cv", valueWeek)
	                    .endObject()
		                  )
		        );
		
		SearchResponse mouthactive = client.prepareSearch(index).setSearchType("count").setTypes(type)
		        .setPostFilter(FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.mouthFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "login")
						))
		        .addAggregation(
			    		AggregationBuilders.cardinality("agg").field("玩家GUID")
			    ).execute().actionGet();
		
		SearchResponse mou= client.prepareSearch(index).setTypes(type)
		        .setPostFilter(FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.mouthFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "login")
						))
		        .addAggregation(
			    		AggregationBuilders.cardinality("agg").field("玩家GUID")
			    ).execute().actionGet();
		
		System.out.println("1111111111  "  + mou  +  "   " + esUtilTest.mouthFrom() + "    " +  esUtilTest.nowDate() + "    "  +  esUtilTest.oneDayAgoFrom().split("T")[0]);
		
		Cardinality aggMouth = mouthactive.getAggregations().get("agg");
		long valueMouth = aggMouth.getValue();
		
		bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_active_mouth)
		        .setSource(jsonBuilder()
			           	 .startObject()
	                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
	                        .field("gameId", game)
	                        .field("key", "all")
	                        .field("@timestamp", new Date())
	                        .field("ct","mouth")
	                        .field("cv", valueMouth)
	                    .endObject()
		                  )
		        );
		//bulkRequest.execute().actionGet();	
	}	
	
	//serverzone
	public void esServerZone() throws IOException, ParseException {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		System.out.println("###############  serverzone 活跃玩家");
		SearchResponse dayactive = client.prepareSearch(index).setSearchType("count").setTypes(type)
		        .setPostFilter(FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "login")
						))
		        .addAggregation(
						AggregationBuilders.terms("serverZone").field("运营大区ID").size(szsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		Terms daygenders = dayactive.getAggregations().get("serverZone");	
		for (Terms.Bucket e : daygenders.getBuckets()) {
		    Cardinality agg = e.getAggregations().get("agg");
		    Long aggcount = agg.getValue();
		    
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_active_day)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("key", "serverZone")
		                        .field("value", e.getKey())
		                        .field("@timestamp", new Date())
		                        .field("ct","day")
		                        .field("cv", aggcount.toString())
		                    .endObject()
			                  )
			        );
		}   
		
		SearchResponse weekactive = client.prepareSearch(index).setSearchType("count").setTypes(type)
		        .setPostFilter(FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.weekFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "login")
						))
		        .addAggregation(
						AggregationBuilders.terms("serverZone").field("运营大区ID").size(szsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		Terms weekgenders = weekactive.getAggregations().get("serverZone");	
		for (Terms.Bucket e : weekgenders.getBuckets()) {
		    Cardinality agg = e.getAggregations().get("agg");
		    Long aggcount = agg.getValue();
		    
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_active_week)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("key", "serverZone")
		                        .field("value", e.getKey())
		                        .field("@timestamp", new Date())
		                        .field("ct","week")
		                        .field("cv", aggcount.toString())
		                    .endObject()
			                  )
			        );
		}  
		
		
		SearchResponse mouthactive = client.prepareSearch(index).setSearchType("count").setTypes(type)
		        .setPostFilter(FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.mouthFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "login")
						))
		        .addAggregation(
						AggregationBuilders.terms("serverZone").field("运营大区ID").size(szsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		Terms mouthgenders = mouthactive.getAggregations().get("serverZone");	
		for (Terms.Bucket e : mouthgenders.getBuckets()) {
		    Cardinality agg = e.getAggregations().get("agg");
		    Long aggcount = agg.getValue();
		    
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_active_mouth)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("key", "serverZone")
		                        .field("value", e.getKey())
		                        .field("@timestamp", new Date())
		                        .field("ct","mouth")
		                        .field("cv", aggcount.toString())
		                    .endObject()
			                  )
			        );
		}  
		bulkRequest.execute().actionGet();	
	}
	
	//platform
	public void esPlatForm() throws IOException, ParseException {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		System.out.println("###############  platform 活跃玩家");
		SearchResponse dayactive = client.prepareSearch(index).setSearchType("count").setTypes(type)
		        .setPostFilter(FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "login")
						))
		        .addAggregation(
						AggregationBuilders.terms("platform").field("渠道ID").size(pfsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		Terms daygenders = dayactive.getAggregations().get("platform");	
		for (Terms.Bucket e : daygenders.getBuckets()) {
		    Cardinality agg = e.getAggregations().get("agg");
		    Long aggcount = agg.getValue();
		    
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_active_day)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("key", "platForm")
		                        .field("value", e.getKey())
		                        .field("@timestamp", new Date())
		                        .field("ct","day")
		                        .field("cv", aggcount.toString())
		                    .endObject()
			                  )
			        );
		}   
		
		SearchResponse weekactive = client.prepareSearch(index).setSearchType("count").setTypes(type)
		        .setPostFilter(FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.weekFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "login")
						))
		        .addAggregation(
		        		AggregationBuilders.terms("platform").field("渠道ID").size(pfsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		Terms weekgenders = weekactive.getAggregations().get("platform");	
		for (Terms.Bucket e : weekgenders.getBuckets()) {
		    Cardinality agg = e.getAggregations().get("agg");
		    Long aggcount = agg.getValue();
		    
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_active_week)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("key", "platForm")
		                        .field("value", e.getKey())
		                        .field("@timestamp", new Date())
		                        .field("ct","week")
		                        .field("cv", aggcount.toString())
		                    .endObject()
			                  )
			        );
		}  
		
		
		SearchResponse mouthactive = client.prepareSearch(index).setSearchType("count").setTypes(type)
		        .setPostFilter(FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.mouthFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "login")
						))
		        .addAggregation(
		        		AggregationBuilders.terms("platform").field("渠道ID").size(pfsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		Terms mouthgenders = mouthactive.getAggregations().get("platform");	
		for (Terms.Bucket e : mouthgenders.getBuckets()) {
		    Cardinality agg = e.getAggregations().get("agg");
		    Long aggcount = agg.getValue();
		    
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_active_mouth)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("key", "platForm")
		                        .field("value", e.getKey())
		                        .field("@timestamp", new Date())
		                        .field("ct","mouth")
		                        .field("cv", aggcount.toString())
		                    .endObject()
			                  )
			        );
		}  
		bulkRequest.execute().actionGet();	
	}	
	
	//server
	public void esServer() throws IOException, ParseException {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		System.out.println("###############  server 活跃玩家");
		SearchResponse dayactive = client.prepareSearch(index).setSearchType("count").setTypes(type)
		        .setPostFilter(FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "login")
						))
		        .addAggregation(
						AggregationBuilders.terms("server").field("服务器ID").size(srsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		Terms daygenders = dayactive.getAggregations().get("server");	
		for (Terms.Bucket e : daygenders.getBuckets()) {
		    Cardinality agg = e.getAggregations().get("agg");
		    Long aggcount = agg.getValue();
		    
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_active_day)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("key", "server")
		                        .field("value", e.getKey())
		                        .field("@timestamp", new Date())
		                        .field("ct","day")
		                        .field("cv", aggcount.toString())
		                    .endObject()
			                  )
			        );
		}   
		
		SearchResponse weekactive = client.prepareSearch(index).setSearchType("count").setTypes(type)
		        .setPostFilter(FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.weekFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "login")
						))
		        .addAggregation(
		        		AggregationBuilders.terms("server").field("服务器ID").size(srsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		Terms weekgenders = weekactive.getAggregations().get("server");	
		for (Terms.Bucket e : weekgenders.getBuckets()) {
		    Cardinality agg = e.getAggregations().get("agg");
		    Long aggcount = agg.getValue();
		    
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_active_week)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("key", "server")
		                        .field("value", e.getKey())
		                        .field("@timestamp", new Date())
		                        .field("ct","week")
		                        .field("cv", aggcount.toString())
		                    .endObject()
			                  )
			        );
		}  
		
		
		SearchResponse mouthactive = client.prepareSearch(index).setSearchType("count").setTypes(type)
		        .setPostFilter(FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.mouthFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "login")
						))
		        .addAggregation(
		        		AggregationBuilders.terms("server").field("服务器ID").size(srsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		Terms mouthgenders = mouthactive.getAggregations().get("server");	
		for (Terms.Bucket e : mouthgenders.getBuckets()) {
		    Cardinality agg = e.getAggregations().get("agg");
		    Long aggcount = agg.getValue();
		    
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_active_mouth)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("key", "server")
		                        .field("value", e.getKey())
		                        .field("@timestamp", new Date())
		                        .field("ct","mouth")
		                        .field("cv", aggcount.toString())
		                    .endObject()
			                  )
			        );
		}  
		bulkRequest.execute().actionGet();	
	}	
	
	//@Test 
	public void test16() throws IOException, ParseException {
		System.out.println("----------------活跃用户 active 调度开始");
		esAll();
		esServerZone();
		esPlatForm();
		esServer();
		System.out.println("----------------活跃用户 active 调度结束");
	}

	
}
