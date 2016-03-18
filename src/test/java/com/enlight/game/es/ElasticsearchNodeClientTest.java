package com.enlight.game.es;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.DeserializationConfig;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.filters.Filters;
import org.elasticsearch.search.aggregations.bucket.global.Global;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.MetricsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;
import static org.elasticsearch.search.aggregations.AggregationBuilders.cardinality;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import com.enlight.game.entity.analysis.UserRetained;
import com.enlight.game.service.serverZone.ServerZoneService;
import com.enlight.game.util.JsonBinder;

/**
 * 测试Elasticsearch 连接
 * @author pengqiuyuan
 * @ContextConfiguration(locations = {"/applicationContext.xml", "/application_es.xml" })
 *
 */
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class ElasticsearchNodeClientTest extends SpringTransactionalTestCase{
	@Autowired
	private Client client;
	
	@Autowired
	private ServerZoneService serverZoneService;
	
	EsUtilTest esUtilTest = new EsUtilTest();
	
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	
	public class Retained{
		
		private String date ="---";
		
		private String nextDayRetain="---";
		
		private String sevenDayRetain="---";
		
		private String thirtyDayRetain="---";

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getNextDayRetain() {
			return nextDayRetain;
		}

		public void setNextDayRetain(String nextDayRetain) {
			this.nextDayRetain = nextDayRetain;
		}

		public String getSevenDayRetain() {
			return sevenDayRetain;
		}

		public void setSevenDayRetain(String sevenDayRetain) {
			this.sevenDayRetain = sevenDayRetain;
		}

		public String getThirtyDayRetain() {
			return thirtyDayRetain;
		}

		public void setThirtyDayRetain(String thirtyDayRetain) {
			this.thirtyDayRetain = thirtyDayRetain;
		}
		
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
	
	public Long createCount(String from , String to){
		FilteredQueryBuilder builder2 = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("日期").from(from).to(to),
		        		FilterBuilders.termFilter("日志分类关键字", "create"))
		        );
		SearchResponse sr = client.prepareSearch().setSearchType("count").setTypes("fb_user.log").setQuery(builder2).execute().actionGet();
		System.out.println(sr);
		Long count = sr.getHits().getTotalHits();
		return count;
	}
	
	public Long createServerZoneCount(String key,String from , String to){
		FilteredQueryBuilder builder2 = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("日期").from(from).to(to),
		        		FilterBuilders.termFilter("日志分类关键字", "create"),
		        		FilterBuilders.termFilter("服务器ID", key))
		        );
		SearchResponse sr = client.prepareSearch().setSearchType("count").setTypes("fb_user.log").setQuery(builder2).execute().actionGet();
		System.out.println(sr);
		Long count = sr.getHits().getTotalHits();
		return count;
	}
	
	public Long createPlatFormCount(String key,String from , String to){
		FilteredQueryBuilder builder2 = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("日期").from(from).to(to),
		        		FilterBuilders.termFilter("日志分类关键字", "create"),
		        		FilterBuilders.termFilter("渠道ID", key))
		        );
		SearchResponse sr = client.prepareSearch().setSearchType("count").setTypes("fb_user.log").setQuery(builder2).execute().actionGet();
		System.out.println(sr);
		Long count = sr.getHits().getTotalHits();
		return count;
	}
	
	//@Test
	public void testStart() {
		assertNotNull(client);
	}
	
	//@Test
	public void test1() {
		SearchResponse sr = client.prepareSearch().setSearchType("count")
			    .setQuery(QueryBuilders.matchAllQuery())
			    .addAggregation(
			            AggregationBuilders.terms("agg1").field("日志分类关键字").size(30)
			    ).execute().actionGet();
		Terms agg1 = sr.getAggregations().get("agg1");
		System.out.println(sr);
		System.out.println(agg1.getBucketByKey("item_get"));
		
	}

	//@Test
	public void test2() {
		SearchResponse sr = client.prepareSearch().setSearchType("count")
			    .setQuery(QueryBuilders.matchAllQuery())
			    .addAggregation(
				        AggregationBuilders
		                .count("agg")
		                .field("日志分类关键字")
			    ).execute().actionGet();
		Terms agg1 = sr.getAggregations().get("agg1");
		System.out.println(sr);
		
	}
	
	//@Test
	public void test3() {
		SearchResponse sr = client.prepareSearch().setSearchType("count")
			    .setQuery(QueryBuilders.matchAllQuery())
			    .addAggregation(
			            AggregationBuilders
		                .percentiles("agg")
		                .field("游戏平台ID")
			    ).execute().actionGet();
		Terms agg1 = sr.getAggregations().get("agg1");
		System.out.println(sr);

	}
	
	//@Test
	public void test4() {
		SearchResponse sr = client.prepareSearch().setSearchType("count")
			    .setQuery(QueryBuilders.matchAllQuery())
			    .addAggregation(
			    		AggregationBuilders
			    	    .global("agg")
			    	    .subAggregation(AggregationBuilders.terms("agg1").field("日志分类关键字").size(30))
			    ).execute().actionGet();
		System.out.println(sr);
		Global agg = sr.getAggregations().get("agg");
		Terms name = agg.getAggregations().get("agg1");
		for(Terms.Bucket entry : name.getBuckets()){
			String key = entry.getKey();
			long docCount = entry.getDocCount();
			System.out.println("key [{}]: " + key+" , doc_count [{}]: "+ docCount);
		}
	}

	//@Test
	public void test5() {
		SearchResponse sr = client.prepareSearch().setSearchType("count")
			    .setQuery(QueryBuilders.matchAllQuery())
			    .addAggregation(
			            AggregationBuilders.terms("agg1").field("日志分类关键字").size(30)
			    ).execute().actionGet();
		Terms agg1 = sr.getAggregations().get("agg1");
		System.out.println(sr);

		for(Terms.Bucket entry : agg1.getBuckets()){
			String key = entry.getKey();
			long docCount = entry.getDocCount();
			System.out.println("key [{}]: " + key+" , doc_count [{}]: "+ docCount);
		}
	}
	
	//@Test
	public void test6() {
		SearchResponse sr = client.prepareSearch().setSearchType("count")
			    .setQuery(QueryBuilders.matchAllQuery())
			    .addAggregation(
			    		AggregationBuilders
			    		.filters("agg")
			            .filter("login", FilterBuilders.termFilter("日志分类关键字", "login"))
			    ).execute().actionGet();
		System.out.println(sr);
		Filters agg = sr.getAggregations().get("agg");
		for (Filters.Bucket entry : agg.getBuckets()) {
		    String key = entry.getKey();                    // bucket key
		    long docCount = entry.getDocCount();            // Doc count
			System.out.println("key [{}]: " + key+" , doc_count [{}]: "+ docCount);
		}	
	}
	
	//time
	//@Test
	public void test7() {	
		//计算时间（当前）2015-04-15 ，统计出2015-04-14到2015-04-15的数据 ，得出2015-04-13的次日留存、得出2015-04-07的7日留存、得出2015-03-15的30日留存
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
		
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(
		        QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
		        		FilterBuilders.termFilter("日志分类关键字", "login"))
		        );
		SearchResponse sr = client.prepareSearch().setSearchType("count").setTypes("fb_user.log").setQuery(builder)
			    .addAggregation(
			    		AggregationBuilders.terms("create").field("注册时间").order(Terms.Order.term(false))
			    		.subAggregation(AggregationBuilders.cardinality("agg").field("玩家GUID")).size(35)
			    ).execute().actionGet();
		System.out.println(sr);
		System.out.println("--------------------------");
		/**
		 * 如果次日、7日、30日无用户登陆为false
		 */
		Boolean boolTwo = false;
		Boolean boolEight = false;
		Boolean boolThirtyOne = false;
		
		Terms genders = sr.getAggregations().get("create");	
		for (Terms.Bucket entry : genders.getBuckets()) {
			String dateBucket = sdf.format(new Date((Long) entry.getKeyAsNumber()));
			if(dateBucket.equals(esUtilTest.twoDayAgoFrom())){
			    Cardinality agg = entry.getAggregations().get("agg");
			    Long aggcount = agg.getValue();
			    System.out.println(aggcount  + " 22222 "  + createCount(esUtilTest.twoDayAgoFrom(), esUtilTest.twoDayAgoTo()));
			    Double RetentionTwo ;
			    if(createCount(esUtilTest.twoDayAgoFrom(), esUtilTest.twoDayAgoTo()) == 0){
			    	RetentionTwo = (double) 0;
				    System.out.println(esUtilTest.twoDayAgoFrom()+" 的次日留存率为  " + RetentionTwo);
			    }else{
			    	RetentionTwo = (double)aggcount/createCount(esUtilTest.twoDayAgoFrom(), esUtilTest.twoDayAgoTo());
				    System.out.println(esUtilTest.twoDayAgoFrom()+"注册："+createCount(esUtilTest.twoDayAgoFrom(), esUtilTest.twoDayAgoTo())+"人，次日"+esUtilTest.oneDayAgoFrom()+"登陆人数为："+ aggcount+"。次日留存率为：" + RetentionTwo);
			    }
			    boolTwo = true;
			}else if(dateBucket.equals(esUtilTest.eightDayAgoFrom())){
			    Cardinality agg = entry.getAggregations().get("agg");
			    Long aggcount = agg.getValue();
			    System.out.println(aggcount  + " 88888 "  + createCount(esUtilTest.eightDayAgoFrom(), esUtilTest.eightDayAgoTo()));
			    Double RetentionEight ;
			    if(createCount(esUtilTest.eightDayAgoFrom(), esUtilTest.eightDayAgoTo()) == 0){
			    	RetentionEight = (double) 0;
			    	System.out.println(esUtilTest.eightDayAgoFrom()+" 的7日留存率为  " + RetentionEight);
			    }else{
				    RetentionEight = (double)aggcount/createCount(esUtilTest.eightDayAgoFrom(), esUtilTest.eightDayAgoTo());
				    System.out.println(esUtilTest.eightDayAgoFrom()+"注册："+createCount(esUtilTest.eightDayAgoFrom(), esUtilTest.eightDayAgoTo())+"人，7日后"+esUtilTest.oneDayAgoFrom()+"登陆人数为："+ aggcount+"。7日后留存率为：" + RetentionEight);
			    }
			    boolEight = true;
			}else if(dateBucket.equals(esUtilTest.thirtyOneDayAgoFrom())){
			    Cardinality agg = entry.getAggregations().get("agg");
			    Long aggcount = agg.getValue();
			    System.out.println(aggcount  + " 3030303030 "  + createCount(esUtilTest.thirtyOneDayAgoFrom(), esUtilTest.thirtyOneDayAgoTo()));
			    Double RetentionThirty ;
			    if(createCount(esUtilTest.thirtyOneDayAgoFrom(), esUtilTest.thirtyOneDayAgoTo()) == 0){
			    	RetentionThirty = (double) 0;
			    	System.out.println(esUtilTest.thirtyOneDayAgoFrom()+" 的30日留存率为  " + RetentionThirty);
			    }else{
			    	RetentionThirty = (double)aggcount/createCount(esUtilTest.thirtyOneDayAgoFrom(), esUtilTest.thirtyOneDayAgoTo());
				    System.out.println("3333333333  " + RetentionThirty);
				    System.out.println(esUtilTest.thirtyOneDayAgoFrom()+"注册："+createCount(esUtilTest.thirtyOneDayAgoFrom(), esUtilTest.thirtyOneDayAgoTo())+"人，30日后"+esUtilTest.oneDayAgoFrom()+"登陆人数为："+ aggcount+"。30日后留存率为：" + RetentionThirty);
			    }
			    
			    boolThirtyOne = true;
			}
		}
		if(!boolTwo){
			System.out.println(esUtilTest.twoDayAgoFrom()+"  次日留存率为0，无次日注册的用户登陆");
		}
		if(!boolEight){
			System.out.println(esUtilTest.eightDayAgoFrom()+"的7日留存率为0  无7日前注册的用户登陆");
		}
		if(!boolThirtyOne){
			System.out.println(esUtilTest.thirtyOneDayAgoFrom()+"的30日留存率为0  无30日前注册的用户登陆");
		}
		
	}
	
	//serverZone
	//@Test
	public void test8() {	
		//计算时间（当前）2015-04-15 ，统计出2015-04-14到2015-04-15的数据 ，得出2015-04-13的次日留存、得出2015-04-07的7日留存、得出2015-03-15的30日留存
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
		
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(
		        QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
		        		FilterBuilders.termFilter("日志分类关键字", "login"))
		        );
		SearchResponse sr = client.prepareSearch().setSearchType("count").setTypes("fb_user.log").setQuery(builder)
			    .addAggregation(
			    		AggregationBuilders.terms("create").field("注册时间").order(Terms.Order.term(false)).size(35)
			    		.subAggregation(AggregationBuilders.cardinality("agg").field("玩家GUID"))
			    		.subAggregation(AggregationBuilders.terms("serverZone").field("服务器ID").size(10).subAggregation(AggregationBuilders.cardinality("agg").field("玩家GUID")))
			    ).execute().actionGet();
		System.out.println(sr);
		System.out.println("--------------------------");
		/**
		 * 如果次日、7日、30日无用户登陆为false
		 */
		Boolean boolTwo = false;
		Boolean boolEight = false;
		Boolean boolThirtyOne = false;
		
		Terms genders = sr.getAggregations().get("create");	
		for (Terms.Bucket entry : genders.getBuckets()) {
			String dateBucket = sdf.format(new Date((Long) entry.getKeyAsNumber()));
			if(dateBucket.equals(esUtilTest.twoDayAgoFrom())){
				Terms serverZone = entry.getAggregations().get("serverZone");
				for (Terms.Bucket e : serverZone.getBuckets()) {
				    Cardinality agg = e.getAggregations().get("agg");
				    Long aggcount = agg.getValue();
				    Double RetentionTwo ;
				    if(createServerZoneCount(e.getKey(),esUtilTest.twoDayAgoFrom(), esUtilTest.twoDayAgoTo()) == 0){
				    	RetentionTwo = (double) 0;
				    }else{
				    	RetentionTwo = (double)aggcount/createServerZoneCount(e.getKey(),esUtilTest.twoDayAgoFrom(), esUtilTest.twoDayAgoTo());
				    }
				    boolTwo = true;
				}
			}else if(dateBucket.equals(esUtilTest.eightDayAgoFrom())){
				Terms serverZone = entry.getAggregations().get("serverZone");
				for (Terms.Bucket e : serverZone.getBuckets()) {
				    Cardinality agg = e.getAggregations().get("agg");
				    Long aggcount = agg.getValue();
				    Double RetentionEight ;
				    if(createServerZoneCount(e.getKey(),esUtilTest.eightDayAgoFrom(), esUtilTest.eightDayAgoTo()) == 0){
				    	RetentionEight = (double) 0;
				    }else{
					    RetentionEight = (double)aggcount/createServerZoneCount(e.getKey(),esUtilTest.eightDayAgoFrom(), esUtilTest.eightDayAgoTo());
				    }
				    boolEight = true;
				}
			}else if(dateBucket.equals(esUtilTest.thirtyOneDayAgoFrom())){
				Terms serverZone = entry.getAggregations().get("serverZone");
				for (Terms.Bucket e : serverZone.getBuckets()) {
				    Cardinality agg = e.getAggregations().get("agg");
				    Long aggcount = agg.getValue();
				    Double RetentionThirty ;
				    if(createServerZoneCount(e.getKey(),esUtilTest.thirtyOneDayAgoFrom(), esUtilTest.thirtyOneDayAgoTo()) == 0){
				    	RetentionThirty = (double) 0;
				    }else{
				    	RetentionThirty = (double)aggcount/createServerZoneCount(e.getKey(),esUtilTest.thirtyOneDayAgoFrom(), esUtilTest.thirtyOneDayAgoTo());
				    }
				    boolThirtyOne = true;
				}
			}
		}
		if(!boolTwo){
			System.out.println(esUtilTest.twoDayAgoFrom()+"  次日留存率为0，无次日注册的用户登陆");
		}
		if(!boolEight){
			System.out.println(esUtilTest.eightDayAgoFrom()+"的7日留存率为0  无7日前注册的用户登陆");
		}
		if(!boolThirtyOne){
			System.out.println(esUtilTest.thirtyOneDayAgoFrom()+"的30日留存率为0  无30日前注册的用户登陆");
		}
		
	}
	
	//platForm
	//@Test
	public void test9() {	
		//计算时间（当前）2015-04-15 ，统计出2015-04-14到2015-04-15的数据 ，得出2015-04-13的次日留存、得出2015-04-07的7日留存、得出2015-03-15的30日留存
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
		
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(
		        QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
		        		FilterBuilders.termFilter("日志分类关键字", "login"))
		        );
		SearchResponse sr = client.prepareSearch().setSearchType("count").setTypes("fb_user.log").setQuery(builder)
			    .addAggregation(
			    		AggregationBuilders.terms("create").field("注册时间").order(Terms.Order.term(false)).size(35)
			    		.subAggregation(AggregationBuilders.cardinality("agg").field("玩家GUID"))
			    		.subAggregation(AggregationBuilders.terms("platForm").field("渠道ID").size(30).subAggregation(AggregationBuilders.cardinality("agg").field("玩家GUID")))
			    ).execute().actionGet();
		System.out.println(sr);
		System.out.println("--------------------------");
		/**
		 * 如果次日、7日、30日无用户登陆为false
		 */
		Boolean boolTwo = false;
		Boolean boolEight = false;
		Boolean boolThirtyOne = false;
		
		Terms genders = sr.getAggregations().get("create");	
		for (Terms.Bucket entry : genders.getBuckets()) {
			String dateBucket = sdf.format(new Date((Long) entry.getKeyAsNumber()));
			if(dateBucket.equals(esUtilTest.twoDayAgoFrom())){
				Terms serverZone = entry.getAggregations().get("platForm");
				for (Terms.Bucket e : serverZone.getBuckets()) {
				    Cardinality agg = e.getAggregations().get("agg");
				    Long aggcount = agg.getValue();
				    Double RetentionTwo ;
				    if(createPlatFormCount(e.getKey(),esUtilTest.twoDayAgoFrom(), esUtilTest.twoDayAgoTo()) == 0){
				    	RetentionTwo = (double) 0;
				    }else{
				    	RetentionTwo = (double)aggcount/createPlatFormCount(e.getKey(),esUtilTest.twoDayAgoFrom(), esUtilTest.twoDayAgoTo());
				    }
				    boolTwo = true;
				}
			}else if(dateBucket.equals(esUtilTest.eightDayAgoFrom())){
				Terms serverZone = entry.getAggregations().get("platForm");
				for (Terms.Bucket e : serverZone.getBuckets()) {
				    Cardinality agg = e.getAggregations().get("agg");
				    Long aggcount = agg.getValue();
				    Double RetentionEight ;
				    if(createPlatFormCount(e.getKey(),esUtilTest.eightDayAgoFrom(), esUtilTest.eightDayAgoTo()) == 0){
				    	RetentionEight = (double) 0;
				    }else{
					    RetentionEight = (double)aggcount/createPlatFormCount(e.getKey(),esUtilTest.eightDayAgoFrom(), esUtilTest.eightDayAgoTo());
				    }
				    boolEight = true;
				}
			}else if(dateBucket.equals(esUtilTest.thirtyOneDayAgoFrom())){
				Terms serverZone = entry.getAggregations().get("platForm");
				for (Terms.Bucket e : serverZone.getBuckets()) {
				    Cardinality agg = e.getAggregations().get("agg");
				    Long aggcount = agg.getValue();
				    Double RetentionThirty ;
				    if(createPlatFormCount(e.getKey(),esUtilTest.thirtyOneDayAgoFrom(), esUtilTest.thirtyOneDayAgoTo()) == 0){
				    	RetentionThirty = (double) 0;
				    }else{
				    	RetentionThirty = (double)aggcount/createPlatFormCount(e.getKey(),esUtilTest.thirtyOneDayAgoFrom(), esUtilTest.thirtyOneDayAgoTo());
				    }
				    boolThirtyOne = true;
				}
			}
		}
		if(!boolTwo){
			System.out.println(esUtilTest.twoDayAgoFrom()+"  次日留存率为0，无次日注册的用户登陆");
		}
		if(!boolEight){
			System.out.println(esUtilTest.eightDayAgoFrom()+"的7日留存率为0  无7日前注册的用户登陆");
		}
		if(!boolThirtyOne){
			System.out.println(esUtilTest.thirtyOneDayAgoFrom()+"的30日留存率为0  无30日前注册的用户登陆");
		}
		
	}
	
	//@Test
	public void test10() {	
		String typeName = "fb_user.log";
		//计算时间（当前）2015-04-15 ，统计出2015-04-14到2015-04-15的数据 ，得出2015-04-13的次日留存、得出2015-04-07的7日留存、得出2015-03-15的30日留存
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
		
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(
		        QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
		        		FilterBuilders.termFilter("日志分类关键字", "login"))
		        );
		SearchResponse sr = client.prepareSearch().setSearchType("count").setTypes(typeName).setQuery(builder)
			    .addAggregation(
			    		AggregationBuilders.terms("create").field("注册时间").order(Terms.Order.term(false))
			    		.subAggregation(AggregationBuilders.cardinality("agg").field("玩家GUID")).size(35)
			    ).execute().actionGet();
		System.out.println(sr);
		System.out.println("--------------------------");
		/**
		 * 如果次日、7日、30日无用户登陆为false
		 */
		Boolean boolTwo = false;
		Boolean boolEight = false;
		Boolean boolThirtyOne = false;
		
		Terms genders = sr.getAggregations().get("create");	
		for (Terms.Bucket entry : genders.getBuckets()) {
			String dateBucket = sdf.format(new Date((Long) entry.getKeyAsNumber()));
			if(dateBucket.equals(esUtilTest.twoDayAgoFrom())){
			    Cardinality agg = entry.getAggregations().get("agg");
			    Long aggcount = agg.getValue();
			    System.out.println(aggcount  + " 22222 "  + createCount(esUtilTest.twoDayAgoFrom(), esUtilTest.twoDayAgoTo()));
			    Double RetentionTwo ;
			    if(createCount(esUtilTest.twoDayAgoFrom(), esUtilTest.twoDayAgoTo()) == 0){
			    	RetentionTwo = (double) 0;
				    System.out.println(esUtilTest.twoDayAgoFrom()+" 的次日留存率为  " + RetentionTwo);
			    }else{
			    	RetentionTwo = (double)aggcount/createCount(esUtilTest.twoDayAgoFrom(), esUtilTest.twoDayAgoTo());
				    System.out.println(esUtilTest.twoDayAgoFrom()+"注册："+createCount(esUtilTest.twoDayAgoFrom(), esUtilTest.twoDayAgoTo())+"人，次日"+esUtilTest.oneDayAgoFrom()+"登陆人数为："+ aggcount+"。次日留存率为：" + RetentionTwo);
			    }
			    boolTwo = true;
			}else if(dateBucket.equals(esUtilTest.eightDayAgoFrom())){
			    Cardinality agg = entry.getAggregations().get("agg");
			    Long aggcount = agg.getValue();
			    System.out.println(aggcount  + " 88888 "  + createCount(esUtilTest.eightDayAgoFrom(), esUtilTest.eightDayAgoTo()));
			    Double RetentionEight ;
			    if(createCount(esUtilTest.eightDayAgoFrom(), esUtilTest.eightDayAgoTo()) == 0){
			    	RetentionEight = (double) 0;
			    	System.out.println(esUtilTest.eightDayAgoFrom()+" 的7日留存率为  " + RetentionEight);
			    }else{
				    RetentionEight = (double)aggcount/createCount(esUtilTest.eightDayAgoFrom(), esUtilTest.eightDayAgoTo());
				    System.out.println(esUtilTest.eightDayAgoFrom()+"注册："+createCount(esUtilTest.eightDayAgoFrom(), esUtilTest.eightDayAgoTo())+"人，7日后"+esUtilTest.oneDayAgoFrom()+"登陆人数为："+ aggcount+"。7日后留存率为：" + RetentionEight);
			    }
			    boolEight = true;
			}else if(dateBucket.equals(esUtilTest.thirtyOneDayAgoFrom())){
			    Cardinality agg = entry.getAggregations().get("agg");
			    Long aggcount = agg.getValue();
			    System.out.println(aggcount  + " 3030303030 "  + createCount(esUtilTest.thirtyOneDayAgoFrom(), esUtilTest.thirtyOneDayAgoTo()));
			    Double RetentionThirty ;
			    if(createCount(esUtilTest.thirtyOneDayAgoFrom(), esUtilTest.thirtyOneDayAgoTo()) == 0){
			    	RetentionThirty = (double) 0;
			    	System.out.println(esUtilTest.thirtyOneDayAgoFrom()+" 的30日留存率为  " + RetentionThirty);
			    }else{
			    	RetentionThirty = (double)aggcount/createCount(esUtilTest.thirtyOneDayAgoFrom(), esUtilTest.thirtyOneDayAgoTo());
				    System.out.println("3333333333  " + RetentionThirty);
				    System.out.println(esUtilTest.thirtyOneDayAgoFrom()+"注册："+createCount(esUtilTest.thirtyOneDayAgoFrom(), esUtilTest.thirtyOneDayAgoTo())+"人，30日后"+esUtilTest.oneDayAgoFrom()+"登陆人数为："+ aggcount+"。30日后留存率为：" + RetentionThirty);
			    }
			    
			    boolThirtyOne = true;
			}
		}
		if(!boolTwo){
			System.out.println(esUtilTest.twoDayAgoFrom()+"  次日留存率为0，无次日注册的用户登陆");
		}
		if(!boolEight){
			System.out.println(esUtilTest.eightDayAgoFrom()+"的7日留存率为0  无7日前注册的用户登陆");
		}
		if(!boolThirtyOne){
			System.out.println(esUtilTest.thirtyOneDayAgoFrom()+"的30日留存率为0  无30日前注册的用户登陆");
		}
		
	}
	
	//@Test
	public void test11() throws ElasticsearchException, IOException{
		IndexResponse response = client.prepareIndex("twitter", "tweet", "11")
		        .setSource(jsonBuilder()
		                    .startObject()
		                        .field("user", "kimchy1212")
		                        .field("postDate", new Date())
		                        .field("message", "trying o1212ut Elasticsearch")
		                    .endObject()
		                  )
		        .execute()
		        .actionGet();
	}
	
	//@Test
	public void test12() throws ElasticsearchException, IOException{
		BulkRequestBuilder bulkRequest = client.prepareBulk();

		bulkRequest.add(client.prepareIndex("twitter1", "tweet1")
		        .setSource(jsonBuilder()
		                    .startObject()
		                        .field("user", "kimchy3442323434444")
		                        .field("postDate", new Date())
		                        .field("message", "trying out asdadsE344444lasti2323csear2222ch33")
		                    .endObject()
		                  )
		        ).execute().actionGet();
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
	                    .endObject()
		                  )
		        ).execute().actionGet();
	}
	
	
	//@Test
	public void searchAllRetained() throws IOException, ElasticsearchException, ParseException{
		String dateFrom = "2015-04-12";
		String dateTo = "2015-04-27";
		
		System.out.println(daysBetween(dateFrom,dateTo));
		
		SearchResponse response = client.prepareSearch("log_retained")
		        .setTypes("fb_retained")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "all"))
		        		)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		StringBuilder datenext = new StringBuilder();
		StringBuilder dateSeven = new StringBuilder();
		StringBuilder datethirty = new StringBuilder();
		datenext.append( "[" );
		dateSeven.append( "[" );
		datethirty.append( "[" );
		//HashMap<String, Retained> retaineds = new HashMap<String, ElasticsearchNodeClientTest.Retained>();
		HashMap<String, Retained> map = new HashMap<String, ElasticsearchNodeClientTest.Retained>();
		
		for (SearchHit hit : response.getHits()) {
			Map<String, Object> source = hit.getSource();
			System.out.println("111  "+source);
			if(source.get("ctRetained").equals("nextDay")){
				datenext.append(source.get("ts"));
				datenext.append(",");				
				System.out.println(map.get(source.get("date").toString())  + " 11111111");
				if(map.get(source.get("date").toString())==null){
					Retained retained = new Retained();
					retained.setDate(source.get("date").toString());
					retained.setNextDayRetain(source.get("retained").toString()+"%");
					map.put(source.get("date").toString(), retained);
				}else{
					Retained retained = map.get(source.get("date").toString());
					retained.setNextDayRetain(source.get("retained").toString()+"%");
					map.put(source.get("date").toString(), retained);
				}

			}else if(source.get("ctRetained").equals("sevenDay")){
				dateSeven.append(source.get("ts"));
				dateSeven.append(",");
				
				System.out.println(map.get(source.get("date").toString())  + " 2222222");
				if(map.get(source.get("date").toString())==null){
					Retained retained = new Retained();
					retained.setDate(source.get("date").toString());
					retained.setSevenDayRetain(source.get("retained").toString()+"%");
					map.put(source.get("date").toString(), retained);
				}else{
					Retained retained = map.get(source.get("date").toString());
					retained.setSevenDayRetain(source.get("retained").toString()+"%");
					map.put(source.get("date").toString(), retained);
				}
			}else if(source.get("ctRetained").equals("thirtyDay")){
				datethirty.append(source.get("ts"));
				datethirty.append(",");
				
				System.out.println(map.get(source.get("date").toString())  + "3333333");
				if(map.get(source.get("date").toString())==null){
					Retained retained = new Retained();
					retained.setDate(source.get("date").toString());
					retained.setThirtyDayRetain(source.get("retained").toString()+"%");
					map.put(source.get("date").toString(), retained);
				}else{
					Retained retained = map.get(source.get("date").toString());
					retained.setThirtyDayRetain(source.get("retained").toString()+"%");
					map.put(source.get("date").toString(), retained);
				}
			}
		}
		datenext.append( "]" );
		dateSeven.append( "]" );
		datethirty.append( "]" );
		
		System.out.println(datenext);
		System.out.println(dateSeven);
		System.out.println(datethirty);

		System.out.println(map);
		Iterator<String> it= map.keySet().iterator();  
		while (it.hasNext())  
		{  
		 Object key=it.next();  
		 System.out.println("key:"+key);  
		 System.out.println("value:"+map.get(key).getDate() + "  " + map.get(key).getNextDayRetain() + "  " + map.get(key).getSevenDayRetain() + "  " + map.get(key).getThirtyDayRetain());  
		}
	}
	

	
	//@Test
	public void searchServerZoneRetained() throws IOException, ParseException{
		//哪一个运营大区
		String value = "3";
		String dateFrom = "2015-04-12";
		String dateTo = "2015-04-27";
		
		System.out.println(daysBetween(dateFrom,dateTo));
		
		SearchResponse response = client.prepareSearch("log_retained")
		        .setTypes("fb_retained")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "serverZone"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		StringBuilder datenext = new StringBuilder();
		StringBuilder dateSeven = new StringBuilder();
		StringBuilder datethirty = new StringBuilder();
		datenext.append( "[" );
		dateSeven.append( "[" );
		datethirty.append( "[" );
		//HashMap<String, Retained> retaineds = new HashMap<String, ElasticsearchNodeClientTest.Retained>();
		HashMap<String, Retained> map = new HashMap<String, ElasticsearchNodeClientTest.Retained>();
		
		for (SearchHit hit : response.getHits()) {
			Map<String, Object> source = hit.getSource();
			System.out.println("111  "+source);
			if(source.get("ctRetained").equals("nextDay")){
				datenext.append(source.get("ts"));
				datenext.append(",");				
				System.out.println(map.get(source.get("date").toString())  + " 11111111");
				if(map.get(source.get("date").toString())==null){
					Retained retained = new Retained();
					retained.setDate(source.get("date").toString());
					retained.setNextDayRetain(source.get("retained").toString()+"%");
					map.put(source.get("date").toString(), retained);
				}else{
					Retained retained = map.get(source.get("date").toString());
					retained.setNextDayRetain(source.get("retained").toString()+"%");
					map.put(source.get("date").toString(), retained);
				}

			}else if(source.get("ctRetained").equals("sevenDay")){
				dateSeven.append(source.get("ts"));
				dateSeven.append(",");
				
				System.out.println(map.get(source.get("date").toString())  + " 2222222");
				if(map.get(source.get("date").toString())==null){
					Retained retained = new Retained();
					retained.setDate(source.get("date").toString());
					retained.setSevenDayRetain(source.get("retained").toString()+"%");
					map.put(source.get("date").toString(), retained);
				}else{
					Retained retained = map.get(source.get("date").toString());
					retained.setSevenDayRetain(source.get("retained").toString()+"%");
					map.put(source.get("date").toString(), retained);
				}
			}else if(source.get("ctRetained").equals("thirtyDay")){
				datethirty.append(source.get("ts"));
				datethirty.append(",");
				
				System.out.println(map.get(source.get("date").toString())  + "3333333");
				if(map.get(source.get("date").toString())==null){
					Retained retained = new Retained();
					retained.setDate(source.get("date").toString());
					retained.setThirtyDayRetain(source.get("retained").toString()+"%");
					map.put(source.get("date").toString(), retained);
				}else{
					Retained retained = map.get(source.get("date").toString());
					retained.setThirtyDayRetain(source.get("retained").toString()+"%");
					map.put(source.get("date").toString(), retained);
				}
			}
		}
		datenext.append( "]" );
		dateSeven.append( "]" );
		datethirty.append( "]" );
		
		System.out.println(datenext);
		System.out.println(dateSeven);
		System.out.println(datethirty);

		System.out.println(map);
		Iterator<String> it= map.keySet().iterator();  
		while (it.hasNext())  
		{  
		 Object key=it.next();  
		 System.out.println("key:"+key);  
		 System.out.println("value:"+map.get(key).getDate() + "  " + map.get(key).getNextDayRetain() + "  " + map.get(key).getSevenDayRetain() + "  " + map.get(key).getThirtyDayRetain());  
		}
		
	}
	
	//@Test
	public void searchPlatFormRetained() throws IOException, ParseException{
		//哪一个渠道
		String value = "1";
		String dateFrom = "2015-04-12";
		String dateTo = "2015-04-27";
		
		System.out.println(daysBetween(dateFrom,dateTo));
		
		SearchResponse response = client.prepareSearch("log_retained")
		        .setTypes("fb_retained")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "platForm"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		StringBuilder datenext = new StringBuilder();
		StringBuilder dateSeven = new StringBuilder();
		StringBuilder datethirty = new StringBuilder();
		datenext.append( "[" );
		dateSeven.append( "[" );
		datethirty.append( "[" );
		//HashMap<String, Retained> retaineds = new HashMap<String, ElasticsearchNodeClientTest.Retained>();
		HashMap<String, Retained> map = new HashMap<String, ElasticsearchNodeClientTest.Retained>();
		
		for (SearchHit hit : response.getHits()) {
			Map<String, Object> source = hit.getSource();
			System.out.println("111  "+source);
			if(source.get("ctRetained").equals("nextDay")){
				datenext.append(source.get("ts"));
				datenext.append(",");				
				System.out.println(map.get(source.get("date").toString())  + " 11111111");
				if(map.get(source.get("date").toString())==null){
					Retained retained = new Retained();
					retained.setDate(source.get("date").toString());
					retained.setNextDayRetain(source.get("retained").toString()+"%");
					map.put(source.get("date").toString(), retained);
				}else{
					Retained retained = map.get(source.get("date").toString());
					retained.setNextDayRetain(source.get("retained").toString()+"%");
					map.put(source.get("date").toString(), retained);
				}

			}else if(source.get("ctRetained").equals("sevenDay")){
				dateSeven.append(source.get("ts"));
				dateSeven.append(",");
				
				System.out.println(map.get(source.get("date").toString())  + " 2222222");
				if(map.get(source.get("date").toString())==null){
					Retained retained = new Retained();
					retained.setDate(source.get("date").toString());
					retained.setSevenDayRetain(source.get("retained").toString()+"%");
					map.put(source.get("date").toString(), retained);
				}else{
					Retained retained = map.get(source.get("date").toString());
					retained.setSevenDayRetain(source.get("retained").toString()+"%");
					map.put(source.get("date").toString(), retained);
				}
			}else if(source.get("ctRetained").equals("thirtyDay")){
				datethirty.append(source.get("ts"));
				datethirty.append(",");
				
				System.out.println(map.get(source.get("date").toString())  + "3333333");
				if(map.get(source.get("date").toString())==null){
					Retained retained = new Retained();
					retained.setDate(source.get("date").toString());
					retained.setThirtyDayRetain(source.get("retained").toString()+"%");
					map.put(source.get("date").toString(), retained);
				}else{
					Retained retained = map.get(source.get("date").toString());
					retained.setThirtyDayRetain(source.get("retained").toString()+"%");
					map.put(source.get("date").toString(), retained);
				}
			}
		}
		datenext.append( "]" );
		dateSeven.append( "]" );
		datethirty.append( "]" );
		
		System.out.println(datenext);
		System.out.println(dateSeven);
		System.out.println(datethirty);

		System.out.println(map);
		Iterator<String> it= map.keySet().iterator();  
		while (it.hasNext())  
		{  
		 Object key=it.next();  
		 System.out.println("key:"+key);  
		 System.out.println("value:"+map.get(key).getDate() + "  " + map.get(key).getNextDayRetain() + "  " + map.get(key).getSevenDayRetain() + "  " + map.get(key).getThirtyDayRetain());  
		}
		
	}

	//@Test
	public void searchServerRetained() throws IOException, ParseException{
		//哪一个服务器
		String value = "3";
		String dateFrom = "2015-04-12";
		String dateTo = "2015-04-27";
		
		System.out.println(daysBetween(dateFrom,dateTo));
		
		SearchResponse response = client.prepareSearch("log_retained")
		        .setTypes("fb_retained")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "server"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		StringBuilder datenext = new StringBuilder();
		StringBuilder dateSeven = new StringBuilder();
		StringBuilder datethirty = new StringBuilder();
		datenext.append( "[" );
		dateSeven.append( "[" );
		datethirty.append( "[" );
		//HashMap<String, Retained> retaineds = new HashMap<String, ElasticsearchNodeClientTest.Retained>();
		HashMap<String, Retained> map = new HashMap<String, ElasticsearchNodeClientTest.Retained>();
		
		for (SearchHit hit : response.getHits()) {
			Map<String, Object> source = hit.getSource();
			System.out.println("111  "+source);
			if(source.get("ctRetained").equals("nextDay")){
				datenext.append(source.get("ts"));
				datenext.append(",");				
				System.out.println(map.get(source.get("date").toString())  + " 11111111");
				if(map.get(source.get("date").toString())==null){
					Retained retained = new Retained();
					retained.setDate(source.get("date").toString());
					retained.setNextDayRetain(source.get("retained").toString()+"%");
					map.put(source.get("date").toString(), retained);
				}else{
					Retained retained = map.get(source.get("date").toString());
					retained.setNextDayRetain(source.get("retained").toString()+"%");
					map.put(source.get("date").toString(), retained);
				}

			}else if(source.get("ctRetained").equals("sevenDay")){
				dateSeven.append(source.get("ts"));
				dateSeven.append(",");
				
				System.out.println(map.get(source.get("date").toString())  + " 2222222");
				if(map.get(source.get("date").toString())==null){
					Retained retained = new Retained();
					retained.setDate(source.get("date").toString());
					retained.setSevenDayRetain(source.get("retained").toString()+"%");
					map.put(source.get("date").toString(), retained);
				}else{
					Retained retained = map.get(source.get("date").toString());
					retained.setSevenDayRetain(source.get("retained").toString()+"%");
					map.put(source.get("date").toString(), retained);
				}
			}else if(source.get("ctRetained").equals("thirtyDay")){
				datethirty.append(source.get("ts"));
				datethirty.append(",");
				
				System.out.println(map.get(source.get("date").toString())  + "3333333");
				if(map.get(source.get("date").toString())==null){
					Retained retained = new Retained();
					retained.setDate(source.get("date").toString());
					retained.setThirtyDayRetain(source.get("retained").toString()+"%");
					map.put(source.get("date").toString(), retained);
				}else{
					Retained retained = map.get(source.get("date").toString());
					retained.setThirtyDayRetain(source.get("retained").toString()+"%");
					map.put(source.get("date").toString(), retained);
				}
			}
		}
		datenext.append( "]" );
		dateSeven.append( "]" );
		datethirty.append( "]" );
		
		System.out.println(datenext);
		System.out.println(dateSeven);
		System.out.println(datethirty);

		System.out.println(map);
		Iterator<String> it= map.keySet().iterator();  
		while (it.hasNext())  
		{  
		 Object key=it.next();  
		 System.out.println("key:"+key);  
		 System.out.println("value:"+map.get(key).getDate() + "  " + map.get(key).getNextDayRetain() + "  " + map.get(key).getSevenDayRetain() + "  " + map.get(key).getThirtyDayRetain());  
		}
		
	}
	
	
	
}

