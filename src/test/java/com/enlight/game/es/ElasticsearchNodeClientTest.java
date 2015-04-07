package com.enlight.game.es;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.codehaus.jackson.map.DeserializationConfig;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.filters.Filters;
import org.elasticsearch.search.aggregations.bucket.global.Global;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.MetricsAggregationBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

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

	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	
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
		SearchResponse sr = client.prepareSearch().setSearchType("count")
				.setTypes("fb_user.log")
			    .setQuery(QueryBuilders.matchAllQuery())
			    .addAggregation(
			    		AggregationBuilders
			    		.filter("agg")
			            .filter(FilterBuilders.termFilter("日志分类关键字", "login"))
			    ).execute().actionGet();
		Filter agg = sr.getAggregations().get("agg");
		agg.getDocCount();
		System.out.println(sr);
	}

}
