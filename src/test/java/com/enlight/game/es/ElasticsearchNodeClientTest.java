package com.enlight.game.es;

import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import org.codehaus.jackson.map.DeserializationConfig;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
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
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;
import static org.elasticsearch.search.aggregations.AggregationBuilders.cardinality;

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
	
	EsUtilTest esUtilTest = new EsUtilTest();
	
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	
	public Long createCount(String from , String to){
		FilteredQueryBuilder builder2 = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("@timestamp").from(from).to(to),
		        		FilterBuilders.termFilter("日志分类关键字", "create"))
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
	
	@Test
	public void test7() {	
		//计算时间（当前）2015-04-15 ，统计出2015-04-14到2015-04-15的数据 ，得出2015-04-13的次日留存、得出2015-04-07的7日留存、得出2015-03-15的30日留存
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
		
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(
		        QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
		        		FilterBuilders.termFilter("日志分类关键字", "login"))
		        );
		SearchResponse sr = client.prepareSearch().setSearchType("count").setTypes("fb_user.log").setQuery(builder)
			    .addAggregation(
			    		AggregationBuilders.terms("create").field("注册时间")
			    		.subAggregation(AggregationBuilders.cardinality("agg").field("玩家GUID"))
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
				    System.out.println("2222222222  " + RetentionTwo);
			    }else{
			    	RetentionTwo = (double)aggcount/createCount(esUtilTest.eightDayAgoFrom(), esUtilTest.eightDayAgoTo());
				    System.out.println("22222222222  " + RetentionTwo);
			    }
			    boolTwo = true;
			}else if(dateBucket.equals(esUtilTest.eightDayAgoFrom())){
			    Cardinality agg = entry.getAggregations().get("agg");
			    Long aggcount = agg.getValue();
			    System.out.println(aggcount  + " 88888 "  + createCount(esUtilTest.eightDayAgoFrom(), esUtilTest.eightDayAgoTo()));
			    Double RetentionEight ;
			    if(createCount(esUtilTest.eightDayAgoFrom(), esUtilTest.eightDayAgoTo()) == 0){
			    	RetentionEight = (double) 0;
				    System.out.println("8888888888  " + RetentionEight);
			    }else{
				    RetentionEight = (double)aggcount/createCount(esUtilTest.eightDayAgoFrom(), esUtilTest.eightDayAgoTo());
				    System.out.println("8888888888  " + RetentionEight);
			    }
			    boolEight = true;
			}else if(dateBucket.equals(esUtilTest.thirtyOneDayAgoFrom())){
			    Cardinality agg = entry.getAggregations().get("agg");
			    Long aggcount = agg.getValue();
			    System.out.println(aggcount  + " 3030303030 "  + createCount(esUtilTest.thirtyOneDayAgoFrom(), esUtilTest.thirtyOneDayAgoTo()));
			    
			    Double RetentionThirty ;
			    if(createCount(esUtilTest.thirtyOneDayAgoFrom(), esUtilTest.thirtyOneDayAgoTo()) == 0){
			    	RetentionThirty = (double) 0;
				    System.out.println("3333333333  " + RetentionThirty);
			    }else{
			    	RetentionThirty = (double)aggcount/createCount(esUtilTest.eightDayAgoFrom(), esUtilTest.eightDayAgoTo());
				    System.out.println("3333333333  " + RetentionThirty);
			    }
			    
			    boolThirtyOne = true;
			}
		}
		if(!boolTwo){
			System.out.println(esUtilTest.twoDayAgoFrom()+"  次日无用户登陆，留存率为0");
		}
		if(!boolEight){
			System.out.println(esUtilTest.eightDayAgoFrom()+"的7日留存率为0  7日无用户登陆");
		}
		if(!boolThirtyOne){
			System.out.println(esUtilTest.thirtyOneDayAgoFrom()+"的30日留存率为0  30日无用户登陆");
		}
		
	}
}
