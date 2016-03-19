package com.enlight.game.schedual.kun;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enlight.game.util.EsUtil;

/**
 * 
 * @author apple
 * 收入分析
 */
@Transactional(readOnly = true)
public class KunUserIncomeScheduled {
	
	@Autowired
	private Client client;
	
	private static final Logger logger = LoggerFactory.getLogger(KunUserIncomeScheduled.class);
	
	//项目名称
	private static final String game = "KUN";
	
	private static final String index = "logstash-kun-money-*";
	
	private static final String type = "kun_moneylog";
	
	private static final String bulk_index = "log_kun_money";
	
	private static final String bulk_type_money_sum = "kun_money_income_sum";
	
	private static final String bulk_type_money_sum_total = "kun_money_income_sum_total";
	
	private static final String bulk_type_money_count = "kun_money_income_count";
	
	private static final String bulk_type_money_peoplenum = "kun_money_income_peoplenum";
	
	private static final Integer szsize = 10; //运营大区
	
	private static final Integer pfsize = 300; //渠道
	
	private static final Integer srsize = 300; //服务器
	
	private static final String paytype = "1"; //货币类型，人民币1 ，美元2 ，离线部分只统计了1 
	
	EsUtil esUtilTest = new EsUtil();
	
	//收入金额
	public void test1() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        FilterBuilders.termFilter("支付货币", paytype)
		        ));
		SearchResponse sum = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(builder)
		        .addAggregation(
		        		AggregationBuilders.sum("sum").field("支付金额")
			    ).execute().actionGet();
		Sum agg = sum.getAggregations().get("sum");
		bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_sum)
		        .setSource(jsonBuilder()
			           	 .startObject()
	                        .field("date", esUtilTest.oneDayAgoFrom().split(" ")[0])
	                        .field("gameId", game)
	                        .field("@timestamp", new Date())
	                        .field("key","all")
	                        .field("ct","sum")
	                        .field("cv",agg.getValue())
	                    .endObject()
		                  )
		        );
		
		//累计收入金额
		FilteredQueryBuilder buildertotal = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from("2014-01-11 00:00:00.000").to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        FilterBuilders.termFilter("支付货币", paytype)
		        ));
		SearchResponse sumtotal = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(buildertotal)
		        .addAggregation(
		        		AggregationBuilders.sum("sum").field("支付金额")
			    ).execute().actionGet();
		Sum aggtotal = sumtotal.getAggregations().get("sum");
		bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_sum_total)
		        .setSource(jsonBuilder()
			           	 .startObject()
	                        .field("date", esUtilTest.oneDayAgoFrom().split(" ")[0])
	                        .field("gameId", game)
	                        .field("@timestamp", new Date())
	                        .field("key","all")
	                        .field("ct","sumtotal")
	                        .field("cv",aggtotal.getValue())
	                    .endObject()
		                  )
		        );
		
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}	
	}
	
	public void test1serverZone() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        FilterBuilders.termFilter("支付货币", paytype)
		        ));
		SearchResponse sum = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(builder)
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
			                        .field("date", esUtilTest.oneDayAgoFrom().split(" ")[0])
			                        .field("gameId", game)
			                        .field("@timestamp", new Date())
			                        .field("key","serverZone")
			                        .field("value",e.getKey())
			                        .field("ct","sum")
			                        .field("cv",agg.getValue())
			                    .endObject()
				                  )
				        );
		}		//累计收入金额
		FilteredQueryBuilder buildertotal = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from("2014-01-11 00:00:00.000").to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        FilterBuilders.termFilter("支付货币", paytype)
		        ));
		SearchResponse sumtotal = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(buildertotal)
		        .addAggregation(
						AggregationBuilders.terms("serverZone").field("运营大区ID").size(szsize).subAggregation(
								AggregationBuilders.sum("sum").field("支付金额")
								)
			    ).execute().actionGet();
		Terms genderstotal = sumtotal.getAggregations().get("serverZone");
		for (Terms.Bucket e : genderstotal.getBuckets()) {
			 Sum agg = e.getAggregations().get("sum");
				bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_sum_total)
				        .setSource(jsonBuilder()
					           	 .startObject()
			                        .field("date", esUtilTest.oneDayAgoFrom().split(" ")[0])
			                        .field("gameId", game)
			                        .field("@timestamp", new Date())
			                        .field("key","serverZone")
			                        .field("value",e.getKey())
			                        .field("ct","sumtotal")
			                        .field("cv",agg.getValue())
			                    .endObject()
				                  )
				        );
		}
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}	
	}	
	
	public void test1platForm() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        FilterBuilders.termFilter("支付货币", paytype)
		        ));
		SearchResponse sum = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(builder)
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
			                        .field("date", esUtilTest.oneDayAgoFrom().split(" ")[0])
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
		//累计收入金额
		FilteredQueryBuilder buildertotal = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from("2014-01-11 00:00:00.000").to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        FilterBuilders.termFilter("支付货币", paytype)
		        ));
		SearchResponse sumtotal = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(buildertotal)
		        .addAggregation(
						AggregationBuilders.terms("platForm").field("渠道ID").size(pfsize).subAggregation(
								AggregationBuilders.sum("sum").field("支付金额")
								)
			    ).execute().actionGet();
		Terms genderstotal = sumtotal.getAggregations().get("platForm");
		for (Terms.Bucket e : genderstotal.getBuckets()) {
			 Sum agg = e.getAggregations().get("sum");
				bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_sum_total)
				        .setSource(jsonBuilder()
					           	 .startObject()
			                        .field("date", esUtilTest.oneDayAgoFrom().split(" ")[0])
			                        .field("gameId", game)
			                        .field("@timestamp", new Date())
			                        .field("key","platForm")
			                        .field("value",e.getKey())
			                        .field("ct","sumtotal")
			                        .field("cv",agg.getValue())
			                    .endObject()
				                  )
				        );
		}
		
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}	
	}
	
	public void test1server() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        FilterBuilders.termFilter("支付货币", paytype)
		        ));
		SearchResponse sum = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(builder)
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
			                        .field("date", esUtilTest.oneDayAgoFrom().split(" ")[0])
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
		//累计收入金额
		FilteredQueryBuilder buildertotal = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from("2014-01-11 00:00:00.000").to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        FilterBuilders.termFilter("支付货币", paytype)
		        ));
		SearchResponse sumtotal = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(buildertotal)
		        .addAggregation(
						AggregationBuilders.terms("server").field("服务器ID").size(srsize).subAggregation(
								AggregationBuilders.sum("sum").field("支付金额")
								)
			    ).execute().actionGet();
		Terms genderstotal = sumtotal.getAggregations().get("server");
		for (Terms.Bucket e : genderstotal.getBuckets()) {
			 Sum agg = e.getAggregations().get("sum");
				bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_sum_total)
				        .setSource(jsonBuilder()
					           	 .startObject()
			                        .field("date", esUtilTest.oneDayAgoFrom().split(" ")[0])
			                        .field("gameId", game)
			                        .field("@timestamp", new Date())
			                        .field("key","server")
			                        .field("value",e.getKey())
			                        .field("ct","sumtotal")
			                        .field("cv",agg.getValue())
			                    .endObject()
				                  )
				        );
		}
		
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}	
	}
	
	
	//充值次数
	public void test2() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        FilterBuilders.termFilter("支付货币", paytype)
		        ));
		SearchResponse count = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(builder)
		        .execute().actionGet();
		bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_count)
		        .setSource(jsonBuilder()
			           	 .startObject()
	                        .field("date", esUtilTest.oneDayAgoFrom().split(" ")[0])
	                        .field("gameId", game)
	                        .field("key", "all")
	                        .field("@timestamp", new Date())
	                        .field("ct","count")
	                        .field("cv", count.getHits().getTotalHits())
	                    .endObject()
		                  )
		        );
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}	
	}
	
	public void test2serverZone() throws IOException, ParseException {	
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        FilterBuilders.termFilter("支付货币", paytype)
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
		                        .field("date", esUtilTest.oneDayAgoFrom().split(" ")[0])
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
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}	
	}
	
	public void test2platForm() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        FilterBuilders.termFilter("支付货币", paytype)
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
		                        .field("date", esUtilTest.oneDayAgoFrom().split(" ")[0])
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
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}	
	}
	
	public void test2server() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        FilterBuilders.termFilter("支付货币", paytype)
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
		                        .field("date", esUtilTest.oneDayAgoFrom().split(" ")[0])
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
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}	
	}
	
	//充值人数
	public void test3() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        FilterBuilders.termFilter("支付货币", paytype)
		        ));
		SearchResponse peoplenum = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(builder)
		        .addAggregation(
		        		AggregationBuilders.cardinality("agg").field("玩家GUID")
			    ).execute().actionGet();
		Cardinality agg = peoplenum.getAggregations().get("agg");
		
		bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_peoplenum)
		        .setSource(jsonBuilder()
			           	 .startObject()
	                        .field("date", esUtilTest.oneDayAgoFrom().split(" ")[0])
	                        .field("gameId", game)
	                        .field("key", "all")
	                        .field("@timestamp", new Date())
	                        .field("ct","peoplenum")
	                        .field("cv", agg.getValue())
	                    .endObject()
		                  )
		        );
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}	
	}
	
	public void test3serverZone() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        FilterBuilders.termFilter("支付货币", paytype)
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
		                        .field("date", esUtilTest.oneDayAgoFrom().split(" ")[0])
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
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}	
	}
	
	public void test3platForm() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        FilterBuilders.termFilter("支付货币", paytype)
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
		                        .field("date", esUtilTest.oneDayAgoFrom().split(" ")[0])
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
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}	
	}
	
	public void test3server() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        FilterBuilders.termFilter("支付货币", paytype)
		        ));
		SearchResponse peoplenum = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(builder)
		        .addAggregation(
						AggregationBuilders.terms("server").field("服务器ID").size(srsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		Terms genders = peoplenum.getAggregations().get("server");	
		for (Terms.Bucket e : genders.getBuckets()) {
			Cardinality agg = e.getAggregations().get("agg");
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_peoplenum)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split(" ")[0])
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
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}	
	}
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void schedual() throws Exception{
		logger.info("----------------kun income begin--------------");
		try {test1();} catch (Exception e) {logger.error("kun all sumtotal error " + e);}
		try {test1serverZone();} catch (Exception e) {logger.error("kun serverZone sumtotal error " + e);}
		try {test1platForm();} catch (Exception e) {logger.error("kun platForm sumtotal error " + e);}
		try {test1server();} catch (Exception e) {logger.error("kun server sumtotal error " + e);}
		
		try {test2();} catch (Exception e) {logger.error("kun all count error " + e);}
		try {test2serverZone();} catch (Exception e) {logger.error("kun serverZone count error " + e);}
		try {test2platForm();} catch (Exception e) {logger.error("kun platForm count error " + e);}
		try {test2server();} catch (Exception e) {logger.error("kun server count error " + e);}
		
		try {test3();} catch (Exception e) {logger.error("kun all peoplenum error " + e);}
		try {test3serverZone();} catch (Exception e) {logger.error("kun serverZone peoplenum error " + e);}
		try {test3platForm();} catch (Exception e) {logger.error("kun platForm peoplenum error " + e);}
		try {test3server();} catch (Exception e) {logger.error("kun server peoplenum error " + e);}

		logger.info("----------------kun income end--------------");
	}
}
