package com.enlight.game.schedual.fb;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enlight.game.util.EsUtil;

@Transactional(readOnly = true)
public class FbUserScheduled {
	
	@Autowired
	public Client client;
	
	//项目名称
	private static final String game = "fb";
	
	private static final String index = "logstash-fb-*";
	
	private static final String type = "fb_user.log";
	
	private static final String bulk_index = "log_fb_user";
	
	private static final String bulk_type_add = "fb_user_add";
	
	private static final String bulk_type_total = "fb_user_total";
	
	EsUtil esUtilTest = new EsUtil();
	

	public void esAll() throws IOException, ParseException {	
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
		        		FilterBuilders.termFilter("日志分类关键字", "create"))
		        );
		SearchResponse sr = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(builder).execute().actionGet();
		
		FilteredQueryBuilder builderTotal = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),FilterBuilders.andFilter(FilterBuilders.termFilter("日志分类关键字", "create")));
		SearchResponse srTotal = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(builderTotal).execute().actionGet();
		
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		Long ts = sdf.parse(esUtilTest.oneDayAgoFrom()).getTime();
		bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_add)
		        .setSource(jsonBuilder()
			           	 .startObject()
	                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
	                        .field("gameId", game)
	                        .field("userAdd", sr.getHits().totalHits())
	                        .field("key", "all")
	                        .field("ts_add","["+ts.toString()+","+sr.getHits().totalHits()+"]")
	                        .field("@timestamp", new Date())
	                    .endObject()
		                  )
		        );
		bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_total)
		        .setSource(jsonBuilder()
			           	 .startObject()
	                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
	                        .field("gameId", game)
	                        .field("userTotal", srTotal.getHits().totalHits())
	                        .field("key", "all")
	                        .field("ts_total","["+ts.toString()+","+srTotal.getHits().totalHits()+"]")
	                        .field("@timestamp", new Date())
	                    .endObject()
		                  )
		        );
		bulkRequest.execute().actionGet();

	}	
	
	public void esServerZone() throws IOException, ParseException {	
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
		        		FilterBuilders.termFilter("日志分类关键字", "create"))
		        );
		SearchResponse sr = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(builder)
				.addAggregation(
			    		AggregationBuilders.terms("serverZone").field("运营大区ID").size(10)
			    )
				.execute().actionGet();
		
		FilteredQueryBuilder builderTotal = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),FilterBuilders.andFilter(FilterBuilders.termFilter("日志分类关键字", "create")));
		SearchResponse srTotal = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(builderTotal)
				.addAggregation(
			    		AggregationBuilders.terms("serverZone").field("运营大区ID").size(10)
			    )
				.execute().actionGet();
		
		Terms genders = sr.getAggregations().get("serverZone");	
		Terms gendersTotal = srTotal.getAggregations().get("serverZone");

		BulkRequestBuilder bulkRequest = client.prepareBulk();
		Long ts = sdf.parse(esUtilTest.oneDayAgoFrom()).getTime();
		for (Terms.Bucket e : genders.getBuckets()) {
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_add)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("userAdd", e.getDocCount())
		                        .field("key", "serverZone")
		                        .field("value",e.getKey())
		                        .field("ts_add","["+ts.toString()+","+e.getDocCount()+"]")
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
		}
		
		for (Terms.Bucket entry : gendersTotal.getBuckets()) {
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_total)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("userTotal", entry.getDocCount())
		                        .field("key", "serverZone")
		                        .field("value",entry.getKey())
		                        .field("ts_total","["+ts.toString()+","+entry.getDocCount()+"]")
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
		}
		bulkRequest.execute().actionGet();		
	}
	
	
	public void esPlatForm() throws IOException, ParseException {	
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
		        		FilterBuilders.termFilter("日志分类关键字", "create"))
		        );
		SearchResponse sr = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(builder)
				.addAggregation(
			    		AggregationBuilders.terms("platForm").field("渠道ID").size(10)
			    )
				.execute().actionGet();
		
		FilteredQueryBuilder builderTotal = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),FilterBuilders.andFilter(FilterBuilders.termFilter("日志分类关键字", "create")));
		SearchResponse srTotal = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(builderTotal)
				.addAggregation(
			    		AggregationBuilders.terms("platForm").field("渠道ID").size(10)
			    )
				.execute().actionGet();
		
		Terms genders = sr.getAggregations().get("platForm");	
		Terms gendersTotal = srTotal.getAggregations().get("platForm");

		BulkRequestBuilder bulkRequest = client.prepareBulk();
		Long ts = sdf.parse(esUtilTest.oneDayAgoFrom()).getTime();
		for (Terms.Bucket e : genders.getBuckets()) {
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_add)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("userAdd", e.getDocCount())
		                        .field("key", "platForm")
		                        .field("value",e.getKey())
		                        .field("ts_add","["+ts.toString()+","+e.getDocCount()+"]")
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
		}
		
		for (Terms.Bucket entry : gendersTotal.getBuckets()) {
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_total)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("userTotal", entry.getDocCount())
		                        .field("key", "platForm")
		                        .field("value",entry.getKey())
		                        .field("ts_total","["+ts.toString()+","+entry.getDocCount()+"]")
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
		}
		bulkRequest.execute().actionGet();	
	}
	
	public void esServer() throws IOException, ParseException {	
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
		        		FilterBuilders.termFilter("日志分类关键字", "create"))
		        );
		SearchResponse sr = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(builder)
				.addAggregation(
			    		AggregationBuilders.terms("server").field("服务器ID").size(10)
			    )
				.execute().actionGet();
		
		FilteredQueryBuilder builderTotal = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),FilterBuilders.andFilter(FilterBuilders.termFilter("日志分类关键字", "create")));
		SearchResponse srTotal = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(builderTotal)
				.addAggregation(
			    		AggregationBuilders.terms("server").field("服务器ID").size(10)
			    )
				.execute().actionGet();
		
		Terms genders = sr.getAggregations().get("server");	
		Terms gendersTotal = srTotal.getAggregations().get("server");

		BulkRequestBuilder bulkRequest = client.prepareBulk();
		Long ts = sdf.parse(esUtilTest.oneDayAgoFrom()).getTime();
		for (Terms.Bucket e : genders.getBuckets()) {
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_add)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("userAdd", e.getDocCount())
		                        .field("key", "server")
		                        .field("value",e.getKey())
		                        .field("ts_add","["+ts.toString()+","+e.getDocCount()+"]")
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
		}
		
		for (Terms.Bucket entry : gendersTotal.getBuckets()) {
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_total)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("userTotal", entry.getDocCount())
		                        .field("key", "server")
		                        .field("value",entry.getKey())
		                        .field("ts_total","["+ts.toString()+","+entry.getDocCount()+"]")
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
		}
		bulkRequest.execute().actionGet();		
		
	}
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void schedual() throws Exception{
		System.out.println("----------------用户 useradd  usertotal 调度开始");
		esAll();
		esServerZone();
		esPlatForm();
		esServer();
		System.out.println("----------------用户 useradd  usertotal 调度结束");
	}
	

}
