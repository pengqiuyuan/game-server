package com.enlight.game.schedual.fb;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enlight.game.service.store.StoreService;
import com.enlight.game.util.EsUtil;

/**
 * 
 * @author apple
 * 活跃玩家
 */
@Transactional(readOnly = true)
public class FbUserIncomeScheduled {
	
	@Autowired
	private Client client;
	
	//项目名称
	private static final String game = "FB";
	
	private static final String index = "logstash-fb-*";
	
	private static final String type = "fb_money.log";
	
	private static final String bulk_index = "log_fb_money";
	
	private static final String bulk_type_money_sum = "fb_money_income_sum";
	
	private static final String bulk_type_money_count = "fb_money_income_count";
	
	private static final String bulk_type_money_peoplenum = "fb_money_income_peoplenum";
	
	private static final Integer szsize = 10; //运营大区
	
	private static final Integer pfsize = 300; //渠道
	
	private static final Integer srsize = 300; //服务器
	
	@Autowired
	private StoreService storeService;
	
	EsUtil esUtilTest = new EsUtil();
	
	//收入金额
	public void test1() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        ));
		SearchResponse sum = client.prepareSearch(index).setSearchType("count").setQuery(builder)
		        .addAggregation(
		        		AggregationBuilders.sum("sum").field("支付金额")
			    ).execute().actionGet();
		Sum agg = sum.getAggregations().get("sum");
		bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_sum)
		        .setSource(jsonBuilder()
			           	 .startObject()
	                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
	                        .field("gameId", game)
	                        .field("@timestamp", new Date())
	                        .field("key","all")
	                        .field("ct","sum")
	                        .field("cv",agg.getValue())
	                    .endObject()
		                  )
		        );
		bulkRequest.execute().actionGet();	
	}
	
	public void test1serverZone() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        ));
		SearchResponse sum = client.prepareSearch(index).setSearchType("count").setQuery(builder)
		        .addAggregation(
						AggregationBuilders.terms("serverZone").field("运营大区ID").size(szsize).subAggregation(
								AggregationBuilders.sum("sum").field("支付金额")
								)
			    ).execute().actionGet();
		Terms genders = sum.getAggregations().get("serverZone");
		for (Terms.Bucket e : genders.getBuckets()) {
			 Sum agg = e.getAggregations().get("sum");
				bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_sum)
				        .setSource(jsonBuilder()
					           	 .startObject()
			                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
			                        .field("gameId", game)
			                        .field("@timestamp", new Date())
			                        .field("key","serverZone")
			                        .field("value",e.getKey())
			                        .field("ct","sum")
			                        .field("cv",agg.getValue())
			                    .endObject()
				                  )
				        );
		}
		bulkRequest.execute().actionGet();	
	}	
	
	public void test1platForm() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        ));
		SearchResponse sum = client.prepareSearch(index).setSearchType("count").setQuery(builder)
		        .addAggregation(
						AggregationBuilders.terms("platForm").field("渠道ID").size(pfsize).subAggregation(
								AggregationBuilders.sum("sum").field("支付金额")
								)
			    ).execute().actionGet();
		Terms genders = sum.getAggregations().get("platForm");
		for (Terms.Bucket e : genders.getBuckets()) {
			 Sum agg = e.getAggregations().get("sum");
				bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_sum)
				        .setSource(jsonBuilder()
					           	 .startObject()
			                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
			                        .field("gameId", game)
			                        .field("@timestamp", new Date())
			                        .field("key","platForm")
			                        .field("value",e.getKey())
			                        .field("ct","sum")
			                        .field("cv",agg.getValue())
			                    .endObject()
				                  )
				        );
		}
		bulkRequest.execute().actionGet();	
	}
	
	public void test1server() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        ));
		SearchResponse sum = client.prepareSearch(index).setSearchType("count").setQuery(builder)
		        .addAggregation(
						AggregationBuilders.terms("server").field("服务器ID").size(srsize).subAggregation(
								AggregationBuilders.sum("sum").field("支付金额")
								)
			    ).execute().actionGet();
		Terms genders = sum.getAggregations().get("server");
		for (Terms.Bucket e : genders.getBuckets()) {
			 Sum agg = e.getAggregations().get("sum");
				bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_sum)
				        .setSource(jsonBuilder()
					           	 .startObject()
			                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
			                        .field("gameId", game)
			                        .field("@timestamp", new Date())
			                        .field("key","server")
			                        .field("value",e.getKey())
			                        .field("ct","sum")
			                        .field("cv",agg.getValue())
			                    .endObject()
				                  )
				        );
		}
		bulkRequest.execute().actionGet();	
	}
	
	
	//充值次数
	public void test2() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        ));
		SearchResponse count = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(builder)
		        .execute().actionGet();
		bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_count)
		        .setSource(jsonBuilder()
			           	 .startObject()
	                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
	                        .field("gameId", game)
	                        .field("key", "all")
	                        .field("@timestamp", new Date())
	                        .field("ct","count")
	                        .field("cv", count.getHits().getTotalHits())
	                    .endObject()
		                  )
		        );
		bulkRequest.execute().actionGet();	
	}
	
	public void test2serverZone() throws IOException, ParseException {	
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        ));
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		SearchResponse count = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(builder)
		        .addAggregation(
						AggregationBuilders.terms("serverZone").field("运营大区ID").size(szsize)
			    ).execute().actionGet();
		Terms genders = count.getAggregations().get("serverZone");	
		for (Terms.Bucket e : genders.getBuckets()) {
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_count)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("@timestamp", new Date())
		                        .field("key","serverZone")
		                        .field("value",e.getKey())
		                        .field("ct","count")
		                        .field("cv",e.getDocCount())
		                    .endObject()
			                  )
			        );
		}   
		bulkRequest.execute().actionGet();	
	}
	
	public void test2platForm() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        ));
		SearchResponse count = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(builder)
		        .addAggregation(
						AggregationBuilders.terms("platForm").field("渠道ID").size(pfsize)
			    ).execute().actionGet();
		Terms genders = count.getAggregations().get("platForm");	
		for (Terms.Bucket e : genders.getBuckets()) {
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_count)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("@timestamp", new Date())
		                        .field("key","platForm")
		                        .field("value",e.getKey())
		                        .field("ct","count")
		                        .field("cv",e.getDocCount())
		                    .endObject()
			                  )
			        );
		}   
		bulkRequest.execute().actionGet();	
	}
	
	public void test2server() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        ));
		SearchResponse count = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(builder)
		        .addAggregation(
						AggregationBuilders.terms("server").field("服务器ID").size(srsize)
			    ).execute().actionGet();
		Terms genders = count.getAggregations().get("server");	
		for (Terms.Bucket e : genders.getBuckets()) {
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_count)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("@timestamp", new Date())
		                        .field("key","server")
		                        .field("value",e.getKey())
		                        .field("ct","count")
		                        .field("cv",e.getDocCount())
		                    .endObject()
			                  )
			        );
		}   
		bulkRequest.execute().actionGet();	
	}
	
	//充值人数
	public void test3() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        ));
		SearchResponse peoplenum = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(builder)
		        .addAggregation(
		        		AggregationBuilders.cardinality("agg").field("玩家GUID")
			    ).execute().actionGet();
		Cardinality agg = peoplenum.getAggregations().get("agg");
		
		bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_peoplenum)
		        .setSource(jsonBuilder()
			           	 .startObject()
	                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
	                        .field("gameId", game)
	                        .field("key", "all")
	                        .field("@timestamp", new Date())
	                        .field("ct","peoplenum")
	                        .field("cv", agg.getValue())
	                    .endObject()
		                  )
		        );
		bulkRequest.execute().actionGet();	
	}
	
	public void test3serverZone() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        ));
		SearchResponse peoplenum = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(builder)
		        .addAggregation(
						AggregationBuilders.terms("serverZone").field("运营大区ID").size(szsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		Terms genders = peoplenum.getAggregations().get("serverZone");	
		for (Terms.Bucket e : genders.getBuckets()) {
			Cardinality agg = e.getAggregations().get("agg");
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_peoplenum)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("@timestamp", new Date())
		                        .field("key","serverZone")
		                        .field("value",e.getKey())
		                        .field("ct","peoplenum")
		                        .field("cv",agg.getValue())
		                    .endObject()
			                  )
			        );
		}   
		bulkRequest.execute().actionGet();	
	}
	
	public void test3platForm() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        ));
		SearchResponse peoplenum = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(builder)
		        .addAggregation(
						AggregationBuilders.terms("platForm").field("渠道ID").size(pfsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		Terms genders = peoplenum.getAggregations().get("platForm");	
		for (Terms.Bucket e : genders.getBuckets()) {
			Cardinality agg = e.getAggregations().get("agg");
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_peoplenum)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("@timestamp", new Date())
		                        .field("key","platForm")
		                        .field("value",e.getKey())
		                        .field("ct","peoplenum")
		                        .field("cv",agg.getValue())
		                    .endObject()
			                  )
			        );
		}   
		bulkRequest.execute().actionGet();	
	}
	
	public void test3server() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
		        ));
		SearchResponse peoplenum = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(builder)
		        .addAggregation(
						AggregationBuilders.terms("server").field("服务器ID").size(srsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		System.out.println(peoplenum);
		Terms genders = peoplenum.getAggregations().get("server");	
		for (Terms.Bucket e : genders.getBuckets()) {
			Cardinality agg = e.getAggregations().get("agg");
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_peoplenum)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("@timestamp", new Date())
		                        .field("key","server")
		                        .field("value",e.getKey())
		                        .field("ct","peoplenum")
		                        .field("cv",agg.getValue())
		                    .endObject()
			                  )
			        );
		}   
		bulkRequest.execute().actionGet();	
	}
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void schedual() throws Exception{
		System.out.println("----------------收入金额 充值次数 充值人数 income 调度开始");
		test1();
		test1serverZone();
		test1platForm();
		test1server();
		test2();
		test2serverZone();
		test2platForm();
		test2server();
		test3();
		test3serverZone();
		test3platForm();
		test3server();
		System.out.println("----------------收入金额 充值次数 充值人数 income 调度结束");
	}
}
