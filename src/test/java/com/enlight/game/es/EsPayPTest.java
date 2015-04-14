package com.enlight.game.es;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

import com.enlight.game.service.store.StoreService;
import com.enlight.game.util.EsUtil;

/**
 * 
 * @author apple
 * ARPU(日) ARPU(月) ARPPU(日) ARPPU(月)
 */

@ContextConfiguration(locations = {"/applicationContext.xml"})
public class EsPayPTest extends SpringTransactionalTestCase{
	@Autowired
	private Client client;
	
	//项目名称
	private static final String game = "FB";
	
	private static final String index = "logstash-fb-*";
	
	private static final String type_user = "fb_user.log";
	
	private static final String type_money = "fb_money.log";
	
	
	private static final String bulk_index = "log_fb_money";
	
	private static final String bulk_type_money_arpuday = "fb_money_arpu_day";
	
	private static final String bulk_type_money_arpumouth = "fb_money_arpu_mouth";
	
	private static final String bulk_type_money_arppuday = "fb_money_arppu_day";
	
	private static final String bulk_type_money_arppumouth = "fb_money_arppu_mouth";
	
	private static final Integer szsize = 10; //运营大区
	
	private static final Integer pfsize = 300; //渠道
	
	private static final Integer srsize = 300; //服务器
	
	@Autowired
	private StoreService storeService;
	
	EsUtil esUtilTest = new EsUtil();

	//ARPU(日) ARPPU(日) 
	//@Test
	public void testdayall() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数  
		//收入金额
		SearchResponse paysum = client.prepareSearch(index).setSearchType("count").setTypes(type_money).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        )))
		        .addAggregation(
		        		AggregationBuilders.sum("sum").field("支付金额")
			    ).execute().actionGet();
		Sum pays = paysum.getAggregations().get("sum");
		System.out.println("pay " + paysum );
		
		//充值人数
		SearchResponse peoplenum = client.prepareSearch(index).setSearchType("count").setTypes(type_money).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        )))
		        .addAggregation(
		        		AggregationBuilders.cardinality("agg").field("玩家GUID")
			    ).execute().actionGet();
		Cardinality peoplen = peoplenum.getAggregations().get("agg");
		System.out.println("num " + peoplenum );
		
		//活跃玩家
		SearchResponse active = client.prepareSearch(index).setSearchType("count").setTypes(type_user).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "login")
						))
		        ).addAggregation(
			    		AggregationBuilders.cardinality("agg").field("玩家GUID")
			    ).execute().actionGet();
		System.out.println("active " + active );
		Cardinality ac = active.getAggregations().get("agg");
		
		double s,t;
		if(peoplen.getValue() == 0 ){
			s = 0.00;
		}else{
			s = pays.getValue()/peoplen.getValue();
		}
		if(ac.getValue() == 0 ){
			t = 0.00;
		}else{
			t = pays.getValue()/ac.getValue();
		}
		System.out.println(df.format(s) + "        "  + df.format(t));
		
		String time = esUtilTest.oneDayAgoFrom().split("T")[0]; 
		//arpu 日
		bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_arpuday)
		        .setSource(jsonBuilder()
			           	 .startObject()
	                        .field("date", time)
	                        .field("gameId", game)
	                        .field("key", "all")
	                        .field("@timestamp", new Date())
	                        .field("ct", "arpuday") 
	                        .field("cv", df.format(t)) 
	                    .endObject()
		                  )
		        );
		//arppu 日
		bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_arppuday)
		        .setSource(jsonBuilder()
			           	 .startObject()
	                        .field("date", time)
	                        .field("gameId", game)
	                        .field("key", "all")
	                        .field("@timestamp", new Date())
	                        .field("ct", "arppuday") 
	                        .field("cv", df.format(s)) 
	                    .endObject()
		                  )
		        );
		bulkRequest.execute().actionGet();	
	}
	
	//@Test
	public void testdayserverzone() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数  
		Map<String, Double> mapsum = new HashMap<String, Double>();
		Map<String, Long> mapnum = new HashMap<String, Long>();
		Map<String, Long> mapac = new HashMap<String, Long>();
		//收入金额
		SearchResponse paysum = client.prepareSearch(index).setSearchType("count").setTypes(type_money).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        )))
		        .addAggregation(
					AggregationBuilders.terms("serverZone").field("运营大区ID").size(szsize).subAggregation(
							AggregationBuilders.sum("sum").field("支付金额")
							)
				).execute().actionGet();
		System.out.println("pay " + paysum );
		Terms g1 = paysum.getAggregations().get("serverZone");
		for (Terms.Bucket e : g1.getBuckets()) {
			 Sum agg = e.getAggregations().get("sum");
			 mapsum.put(e.getKey(), agg.getValue());
		}

		
		//充值人数
		SearchResponse peoplenum = client.prepareSearch(index).setSearchType("count").setTypes(type_money).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        )))
		        .addAggregation(
						AggregationBuilders.terms("serverZone").field("运营大区ID").size(szsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		System.out.println("num " + peoplenum );
		Terms g2 = peoplenum.getAggregations().get("serverZone");
		for (Terms.Bucket e : g2.getBuckets()) {
			Cardinality peoplen = e.getAggregations().get("agg");
			mapnum.put(e.getKey(), peoplen.getValue());
		}

		
		//活跃玩家
		SearchResponse active = client.prepareSearch(index).setSearchType("count").setTypes(type_user).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "login")
						))
		        ).addAggregation(
						AggregationBuilders.terms("serverZone").field("运营大区ID").size(szsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		System.out.println("active " + active );
		Terms g3 = active.getAggregations().get("serverZone");
		for (Terms.Bucket e : g3.getBuckets()) {
			Cardinality ac = e.getAggregations().get("agg");
			 mapac.put(e.getKey(), ac.getValue());
		}
		
		String time = esUtilTest.oneDayAgoFrom().split("T")[0]; 
		for(Entry<String,Double> entry : mapsum.entrySet()){
			if(mapac.containsKey(entry.getKey())){
				bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_arpuday)
				        .setSource(jsonBuilder()
					           	 .startObject()
			                        .field("date", time)
			                        .field("gameId", game)
			                        .field("key", "serverZone")
			                        .field("value", entry.getKey())
			                        .field("@timestamp", new Date())
			                        .field("ct", "arpuday") 
			                        .field("cv", df.format(entry.getValue()/mapac.get(entry.getKey()))) 
			                    .endObject()
				                  )
				        );
			}
			if(mapnum.containsKey(entry.getKey())){
				bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_arppuday)
				        .setSource(jsonBuilder()
					           	 .startObject()
			                        .field("date", time)
			                        .field("gameId", game)
			                        .field("key", "serverZone")
			                        .field("value", entry.getKey())
			                        .field("@timestamp", new Date())
			                        .field("ct", "arppuday") 
			                        .field("cv", df.format(entry.getValue()/mapnum.get(entry.getKey()))) 
			                    .endObject()
				                  )
				        );
			}
		}
		bulkRequest.execute().actionGet();	
	}
	
	//@Test
	public void testdayplatform() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数  
		Map<String, Double> mapsum = new HashMap<String, Double>();
		Map<String, Long> mapnum = new HashMap<String, Long>();
		Map<String, Long> mapac = new HashMap<String, Long>();
		//收入金额
		SearchResponse paysum = client.prepareSearch(index).setSearchType("count").setTypes(type_money).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        )))
		        .addAggregation(
					AggregationBuilders.terms("platForm").field("渠道ID").size(pfsize).subAggregation(
							AggregationBuilders.sum("sum").field("支付金额")
							)
				).execute().actionGet();
		System.out.println("pay " + paysum );
		Terms g1 = paysum.getAggregations().get("platForm");
		for (Terms.Bucket e : g1.getBuckets()) {
			 Sum agg = e.getAggregations().get("sum");
			 mapsum.put(e.getKey(), agg.getValue());
		}

		
		//充值人数
		SearchResponse peoplenum = client.prepareSearch(index).setSearchType("count").setTypes(type_money).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        )))
		        .addAggregation(
						AggregationBuilders.terms("platForm").field("渠道ID").size(pfsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		System.out.println("num " + peoplenum );
		Terms g2 = peoplenum.getAggregations().get("platForm");
		for (Terms.Bucket e : g2.getBuckets()) {
			Cardinality peoplen = e.getAggregations().get("agg");
			mapnum.put(e.getKey(), peoplen.getValue());
		}

		
		//活跃玩家
		SearchResponse active = client.prepareSearch(index).setSearchType("count").setTypes(type_user).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "login")
						))
		        ).addAggregation(
						AggregationBuilders.terms("platForm").field("渠道ID").size(pfsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		System.out.println("active " + active );
		Terms g3 = active.getAggregations().get("platForm");
		for (Terms.Bucket e : g3.getBuckets()) {
			Cardinality ac = e.getAggregations().get("agg");
			 mapac.put(e.getKey(), ac.getValue());
		}
		
		String time = esUtilTest.oneDayAgoFrom().split("T")[0]; 
		for(Entry<String,Double> entry : mapsum.entrySet()){
			if(mapac.containsKey(entry.getKey())){
				bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_arpuday)
				        .setSource(jsonBuilder()
					           	 .startObject()
			                        .field("date", time)
			                        .field("gameId", game)
			                        .field("key", "platForm")
			                        .field("value", entry.getKey())
			                        .field("@timestamp", new Date())
			                        .field("ct", "arpuday") 
			                        .field("cv", df.format(entry.getValue()/mapac.get(entry.getKey()))) 
			                    .endObject()
				                  )
				        );
			}
			if(mapnum.containsKey(entry.getKey())){
				bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_arppuday)
				        .setSource(jsonBuilder()
					           	 .startObject()
			                        .field("date", time)
			                        .field("gameId", game)
			                        .field("key", "platForm")
			                        .field("value", entry.getKey())
			                        .field("@timestamp", new Date())
			                        .field("ct", "arppuday") 
			                        .field("cv", df.format(entry.getValue()/mapnum.get(entry.getKey()))) 
			                    .endObject()
				                  )
				        );
			}
		}
		bulkRequest.execute().actionGet();	
	}
	
	//@Test
	public void testdayserver() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数  
		Map<String, Double> mapsum = new HashMap<String, Double>();
		Map<String, Long> mapnum = new HashMap<String, Long>();
		Map<String, Long> mapac = new HashMap<String, Long>();
		//收入金额
		SearchResponse paysum = client.prepareSearch(index).setSearchType("count").setTypes(type_money).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        )))
		        .addAggregation(
					AggregationBuilders.terms("server").field("服务器ID").size(srsize).subAggregation(
							AggregationBuilders.sum("sum").field("支付金额")
							)
				).execute().actionGet();
		System.out.println("pay " + paysum );
		Terms g1 = paysum.getAggregations().get("server");
		for (Terms.Bucket e : g1.getBuckets()) {
			 Sum agg = e.getAggregations().get("sum");
			 mapsum.put(e.getKey(), agg.getValue());
		}

		
		//充值人数
		SearchResponse peoplenum = client.prepareSearch(index).setSearchType("count").setTypes(type_money).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        )))
		        .addAggregation(
						AggregationBuilders.terms("server").field("服务器ID").size(srsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		System.out.println("num " + peoplenum );
		Terms g2 = peoplenum.getAggregations().get("server");
		for (Terms.Bucket e : g2.getBuckets()) {
			Cardinality peoplen = e.getAggregations().get("agg");
			mapnum.put(e.getKey(), peoplen.getValue());
		}

		
		//活跃玩家
		SearchResponse active = client.prepareSearch(index).setSearchType("count").setTypes(type_user).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "login")
						))
		        ).addAggregation(
						AggregationBuilders.terms("server").field("服务器ID").size(srsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		System.out.println("active " + active );
		Terms g3 = active.getAggregations().get("server");
		for (Terms.Bucket e : g3.getBuckets()) {
			Cardinality ac = e.getAggregations().get("agg");
			 mapac.put(e.getKey(), ac.getValue());
		}
		
		String time = esUtilTest.oneDayAgoFrom().split("T")[0]; 
		for(Entry<String,Double> entry : mapsum.entrySet()){
			if(mapac.containsKey(entry.getKey())){
				bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_arpuday)
				        .setSource(jsonBuilder()
					           	 .startObject()
			                        .field("date", time)
			                        .field("gameId", game)
			                        .field("key", "server")
			                        .field("value", entry.getKey())
			                        .field("@timestamp", new Date())
			                        .field("ct", "arpuday") 
			                        .field("cv", df.format(entry.getValue()/mapac.get(entry.getKey()))) 
			                    .endObject()
				                  )
				        );
			}
			if(mapnum.containsKey(entry.getKey())){
				bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_arppuday)
				        .setSource(jsonBuilder()
					           	 .startObject()
			                        .field("date", time)
			                        .field("gameId", game)
			                        .field("key", "server")
			                        .field("value", entry.getKey())
			                        .field("@timestamp", new Date())
			                        .field("ct", "arppuday") 
			                        .field("cv", df.format(entry.getValue()/mapnum.get(entry.getKey()))) 
			                    .endObject()
				                  )
				        );
			}
		}
		bulkRequest.execute().actionGet();	
	}
	
	

	//@Test
	public void testmouthall() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数  
		//收入金额
		SearchResponse paysum = client.prepareSearch(index).setSearchType("count").setTypes(type_money).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.presentfirstday()).to(esUtilTest.lastmouthfirstday()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        )))
		        .addAggregation(
		        		AggregationBuilders.sum("sum").field("支付金额")
			    ).execute().actionGet();
		Sum pays = paysum.getAggregations().get("sum");
		System.out.println("pay " + paysum );
		
		//充值人数
		SearchResponse peoplenum = client.prepareSearch(index).setSearchType("count").setTypes(type_money).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.presentfirstday()).to(esUtilTest.lastmouthfirstday()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        )))
		        .addAggregation(
		        		AggregationBuilders.cardinality("agg").field("玩家GUID")
			    ).execute().actionGet();
		Cardinality peoplen = peoplenum.getAggregations().get("agg");
		System.out.println("num " + peoplenum );
		
		//活跃玩家
		SearchResponse active = client.prepareSearch(index).setSearchType("count").setTypes(type_user).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.presentfirstday()).to(esUtilTest.lastmouthfirstday()),
				        FilterBuilders.termFilter("日志分类关键字", "login")
						))
		        ).addAggregation(
			    		AggregationBuilders.cardinality("agg").field("玩家GUID")
			    ).execute().actionGet();
		System.out.println("active " + active );
		Cardinality ac = active.getAggregations().get("agg");
		
		double s,t;
		if(peoplen.getValue() == 0 ){
			s = 0.00;
		}else{
			s = pays.getValue()/peoplen.getValue();
		}
		if(ac.getValue() == 0 ){
			t = 0.00;
		}else{
			t = pays.getValue()/ac.getValue();
		}
		
		System.out.println(df.format(s) + "        "  + df.format(t));
		
		//arpu 月
		bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_arpumouth,esUtilTest.lastmouth()+"_all")
		        .setSource(jsonBuilder()
			           	 .startObject()
	                        .field("date", esUtilTest.lastmouth())
	                        .field("gameId", game)
	                        .field("key", "all")
	                        .field("@timestamp", new Date())
	                        .field("ct", "arpumouth") 
	                        .field("cv", df.format(t)) 
	                    .endObject()
		                  )
		        );
		//arppu 月
		bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_arppumouth,esUtilTest.lastmouth()+"_all")
		        .setSource(jsonBuilder()
			           	 .startObject()
	                        .field("date", esUtilTest.lastmouth())
	                        .field("gameId", game)
	                        .field("key", "all")
	                        .field("@timestamp", new Date())
	                        .field("ct", "arppumouth") 
	                        .field("cv", df.format(s)) 
	                    .endObject()
		                  )
		        );
		bulkRequest.execute().actionGet();	
	}
	
	@Test
	public void testmouthserverzone() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数  
		Map<String, Double> mapsum = new HashMap<String, Double>();
		Map<String, Long> mapnum = new HashMap<String, Long>();
		Map<String, Long> mapac = new HashMap<String, Long>();
		//收入金额
		SearchResponse paysum = client.prepareSearch(index).setSearchType("count").setTypes(type_money).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.presentfirstday()).to(esUtilTest.lastmouthfirstday()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        )))
		        .addAggregation(
					AggregationBuilders.terms("serverZone").field("运营大区ID").size(szsize).subAggregation(
							AggregationBuilders.sum("sum").field("支付金额")
							)
				).execute().actionGet();
		System.out.println("pay " + paysum );
		Terms g1 = paysum.getAggregations().get("serverZone");
		for (Terms.Bucket e : g1.getBuckets()) {
			 Sum agg = e.getAggregations().get("sum");
			 mapsum.put(e.getKey(), agg.getValue());
		}

		
		//充值人数
		SearchResponse peoplenum = client.prepareSearch(index).setSearchType("count").setTypes(type_money).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.presentfirstday()).to(esUtilTest.lastmouthfirstday()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        )))
		        .addAggregation(
						AggregationBuilders.terms("serverZone").field("运营大区ID").size(szsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		System.out.println("num " + peoplenum );
		Terms g2 = peoplenum.getAggregations().get("serverZone");
		for (Terms.Bucket e : g2.getBuckets()) {
			Cardinality peoplen = e.getAggregations().get("agg");
			mapnum.put(e.getKey(), peoplen.getValue());
		}

		
		//活跃玩家
		SearchResponse active = client.prepareSearch(index).setSearchType("count").setTypes(type_user).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.presentfirstday()).to(esUtilTest.lastmouthfirstday()),
				        FilterBuilders.termFilter("日志分类关键字", "login")
						))
		        ).addAggregation(
						AggregationBuilders.terms("serverZone").field("运营大区ID").size(szsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		System.out.println("active " + active );
		Terms g3 = active.getAggregations().get("serverZone");
		for (Terms.Bucket e : g3.getBuckets()) {
			Cardinality ac = e.getAggregations().get("agg");
			 mapac.put(e.getKey(), ac.getValue());
		}
		
		for(Entry<String,Double> entry : mapsum.entrySet()){
			if(mapac.containsKey(entry.getKey())){
				bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_arpumouth,esUtilTest.lastmouth()+"_serverZone_"+entry.getKey())
				        .setSource(jsonBuilder()
					           	 .startObject()
			                        .field("date", esUtilTest.lastmouth())
			                        .field("gameId", game)
			                        .field("key", "serverZone")
			                        .field("value", entry.getKey())
			                        .field("@timestamp", new Date())
			                        .field("ct", "arpuday") 
			                        .field("cv", df.format(entry.getValue()/mapac.get(entry.getKey()))) 
			                    .endObject()
				                  )
				        );
			}
			if(mapnum.containsKey(entry.getKey())){
				bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_arppumouth,esUtilTest.lastmouth()+"_serverZone_"+entry.getKey())
				        .setSource(jsonBuilder()
					           	 .startObject()
			                        .field("date", esUtilTest.lastmouth())
			                        .field("gameId", game)
			                        .field("key", "serverZone")
			                        .field("value", entry.getKey())
			                        .field("@timestamp", new Date())
			                        .field("ct", "arppuday") 
			                        .field("cv", df.format(entry.getValue()/mapnum.get(entry.getKey()))) 
			                    .endObject()
				                  )
				        );
			}
		}
		if(!mapsum.isEmpty()){
			bulkRequest.execute().actionGet();	
		}
		
	}
	
	@Test
	public void testmouthplatform() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数  
		Map<String, Double> mapsum = new HashMap<String, Double>();
		Map<String, Long> mapnum = new HashMap<String, Long>();
		Map<String, Long> mapac = new HashMap<String, Long>();
		//收入金额
		SearchResponse paysum = client.prepareSearch(index).setSearchType("count").setTypes(type_money).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.presentfirstday()).to(esUtilTest.lastmouthfirstday()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        )))
		        .addAggregation(
					AggregationBuilders.terms("platForm").field("渠道ID").size(pfsize).subAggregation(
							AggregationBuilders.sum("sum").field("支付金额")
							)
				).execute().actionGet();
		System.out.println("pay " + paysum );
		Terms g1 = paysum.getAggregations().get("platForm");
		for (Terms.Bucket e : g1.getBuckets()) {
			 Sum agg = e.getAggregations().get("sum");
			 mapsum.put(e.getKey(), agg.getValue());
		}

		
		//充值人数
		SearchResponse peoplenum = client.prepareSearch(index).setSearchType("count").setTypes(type_money).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.presentfirstday()).to(esUtilTest.lastmouthfirstday()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        )))
		        .addAggregation(
						AggregationBuilders.terms("platForm").field("渠道ID").size(pfsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		System.out.println("num " + peoplenum );
		Terms g2 = peoplenum.getAggregations().get("platForm");
		for (Terms.Bucket e : g2.getBuckets()) {
			Cardinality peoplen = e.getAggregations().get("agg");
			mapnum.put(e.getKey(), peoplen.getValue());
		}

		
		//活跃玩家
		SearchResponse active = client.prepareSearch(index).setSearchType("count").setTypes(type_user).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.presentfirstday()).to(esUtilTest.lastmouthfirstday()),
				        FilterBuilders.termFilter("日志分类关键字", "login")
						))
		        ).addAggregation(
						AggregationBuilders.terms("platForm").field("渠道ID").size(pfsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		System.out.println("active " + active );
		Terms g3 = active.getAggregations().get("platForm");
		for (Terms.Bucket e : g3.getBuckets()) {
			Cardinality ac = e.getAggregations().get("agg");
			 mapac.put(e.getKey(), ac.getValue());
		}
		
		for(Entry<String,Double> entry : mapsum.entrySet()){
			if(mapac.containsKey(entry.getKey())){
				bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_arpumouth,esUtilTest.lastmouth()+"_platForm_"+entry.getKey())
				        .setSource(jsonBuilder()
					           	 .startObject()
			                        .field("date", esUtilTest.lastmouth())
			                        .field("gameId", game)
			                        .field("key", "platForm")
			                        .field("value", entry.getKey())
			                        .field("@timestamp", new Date())
			                        .field("ct", "arpuday") 
			                        .field("cv", df.format(entry.getValue()/mapac.get(entry.getKey()))) 
			                    .endObject()
				                  )
				        );
			}
			if(mapnum.containsKey(entry.getKey())){
				bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_arppumouth,esUtilTest.lastmouth()+"_platForm_"+entry.getKey())
				        .setSource(jsonBuilder()
					           	 .startObject()
			                        .field("date", esUtilTest.lastmouth())
			                        .field("gameId", game)
			                        .field("key", "platForm")
			                        .field("value", entry.getKey())
			                        .field("@timestamp", new Date())
			                        .field("ct", "arppuday") 
			                        .field("cv", df.format(entry.getValue()/mapnum.get(entry.getKey()))) 
			                    .endObject()
				                  )
				        );
			}
		}
		if(!mapsum.isEmpty()){
			bulkRequest.execute().actionGet();	
		}
	}
	
	//@Test
	public void testmouthserver() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数  
		Map<String, Double> mapsum = new HashMap<String, Double>();
		Map<String, Long> mapnum = new HashMap<String, Long>();
		Map<String, Long> mapac = new HashMap<String, Long>();
		//收入金额
		SearchResponse paysum = client.prepareSearch(index).setSearchType("count").setTypes(type_money).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.presentfirstday()).to(esUtilTest.lastmouthfirstday()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        )))
		        .addAggregation(
					AggregationBuilders.terms("server").field("服务器ID").size(srsize).subAggregation(
							AggregationBuilders.sum("sum").field("支付金额")
							)
				).execute().actionGet();
		System.out.println("pay " + paysum );
		Terms g1 = paysum.getAggregations().get("server");
		for (Terms.Bucket e : g1.getBuckets()) {
			 Sum agg = e.getAggregations().get("sum");
			 mapsum.put(e.getKey(), agg.getValue());
		}

		
		//充值人数
		SearchResponse peoplenum = client.prepareSearch(index).setSearchType("count").setTypes(type_money).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.presentfirstday()).to(esUtilTest.lastmouthfirstday()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        )))
		        .addAggregation(
						AggregationBuilders.terms("server").field("服务器ID").size(srsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		System.out.println("num " + peoplenum );
		Terms g2 = peoplenum.getAggregations().get("server");
		for (Terms.Bucket e : g2.getBuckets()) {
			Cardinality peoplen = e.getAggregations().get("agg");
			mapnum.put(e.getKey(), peoplen.getValue());
		}

		
		//活跃玩家
		SearchResponse active = client.prepareSearch(index).setSearchType("count").setTypes(type_user).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.presentfirstday()).to(esUtilTest.lastmouthfirstday()),
				        FilterBuilders.termFilter("日志分类关键字", "login")
						))
		        ).addAggregation(
						AggregationBuilders.terms("server").field("服务器ID").size(srsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		System.out.println("active " + active );
		Terms g3 = active.getAggregations().get("server");
		for (Terms.Bucket e : g3.getBuckets()) {
			Cardinality ac = e.getAggregations().get("agg");
			 mapac.put(e.getKey(), ac.getValue());
		}
		
		for(Entry<String,Double> entry : mapsum.entrySet()){
			if(mapac.containsKey(entry.getKey())){
				bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_arpumouth,esUtilTest.lastmouth()+"_platForm_"+entry.getKey())
				        .setSource(jsonBuilder()
					           	 .startObject()
			                        .field("date", esUtilTest.lastmouth())
			                        .field("gameId", game)
			                        .field("key", "server")
			                        .field("value", entry.getKey())
			                        .field("@timestamp", new Date())
			                        .field("ct", "arpuday") 
			                        .field("cv", df.format(entry.getValue()/mapac.get(entry.getKey()))) 
			                    .endObject()
				                  )
				        );
			}
			if(mapnum.containsKey(entry.getKey())){
				bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_arppumouth,esUtilTest.lastmouth()+"_platForm_"+entry.getKey())
				        .setSource(jsonBuilder()
					           	 .startObject()
			                        .field("date", esUtilTest.lastmouth())
			                        .field("gameId", game)
			                        .field("key", "server")
			                        .field("value", entry.getKey())
			                        .field("@timestamp", new Date())
			                        .field("ct", "arppuday") 
			                        .field("cv", df.format(entry.getValue()/mapnum.get(entry.getKey()))) 
			                    .endObject()
				                  )
				        );
			}
		}
		if(!mapsum.isEmpty()){
			bulkRequest.execute().actionGet();	
		}
	}

	//@Test
	public void presentfirstday(){
        //获取前月的第一天
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
        Calendar   cal_1=Calendar.getInstance();//获取当前日期 
        cal_1.add(Calendar.MONTH, -1);
        cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        String firstDay = format.format(cal_1.getTime());
 
	}	
	
	//@Test
	public void lastmouthfirstday(){
        //获取当月的第一天
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
        Calendar c = Calendar.getInstance();    
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        String first = format.format(c.getTime());
	}	
	
	
	//@Test
	public void lastmouth(){
		//获取上月
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM"); 
        Calendar   cal_1=Calendar.getInstance();//获取当前日期 
        cal_1.add(Calendar.MONTH, -1);
        cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        String fd= ft.format(cal_1.getTime());
        System.out.println(fd);
        
	}	
	
	
}
