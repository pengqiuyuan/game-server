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
public class EsTest extends SpringTransactionalTestCase{
	@Autowired
	private Client client;

	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	
	//@Test
	public void testStart() {
		assertNotNull(client);
	}
	
	@Test
	public void test7() {
		SearchResponse sr = client.prepareSearch().setSearchType("count")
				.setTypes("fb_user.log")
			    .setQuery(QueryBuilders.matchAllQuery())
			    .addAggregation(
			    		AggregationBuilders
			    		.filter("agg")
			            .filter(FilterBuilders.termFilter("日志分类关键字", "create"))
			    ).execute().actionGet();
		Filter agg = sr.getAggregations().get("agg");
		agg.getDocCount();
		System.out.println(sr);
	}

}
