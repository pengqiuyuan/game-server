package com.enlight.game.schedual.fb;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enlight.game.util.EsUtil;

@Transactional(readOnly = true)
public class FbUserScheduled {
	
	@Autowired
	public Client client;
	
	private static final Logger logger = LoggerFactory.getLogger(FbUserScheduled.class);
	
	//项目名称
	private static final String game = "FB";
	
	private static final String index = "logstash-fb-user-*";
	
	private static final String type = "fb_userlog";
	
	private static final String bulk_index = "log_fb_user";
	
	private static final String bulk_type_add = "fb_user_add";
	
	private static final String bulk_type_total = "fb_user_total";
	
	private static final Integer szsize = 10; //运营大区
	
	private static final Integer pfsize = 300; //渠道
	
	private static final Integer srsize = 300; //服务器
	
	EsUtil esUtilTest = new EsUtil();
	
	public void esAll() throws IOException, ParseException {	
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd 00:00:00.000" ); 
		//新增用户
		SearchResponse sr = client.prepareSearch(index).setTypes(type).setSearchType("count").setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
		        		FilterBuilders.termFilter("日志分类关键字", "create"))
		        )).execute().actionGet();
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		Long ts = sdf.parse(esUtilTest.oneDayAgoFrom()).getTime();
		bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_add)
		        .setSource(jsonBuilder()
			           	 .startObject() 
	                        .field("date", esUtilTest.oneDayAgoFrom().split(" ")[0])
	                        .field("gameId", game)
	                        .field("userAdd", sr.getHits().totalHits())
	                        .field("key", "all")
	                        .field("ts_add",ts.toString())
	                        .field("@timestamp", new Date())
	                    .endObject()
		                  )
		        );
		logger.debug("昨天新增用户all："+sr.getHits().totalHits());
		//累计用户
		
		SearchResponse srTotal = client.prepareSearch(index).setTypes(type).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
						FilterBuilders.andFilter(
								FilterBuilders.rangeFilter("日期").from("2014-01-11").to(esUtilTest.nowDate()),
								FilterBuilders.termFilter("日志分类关键字", "create")
								))
						).execute().actionGet();
		bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_total)
		        .setSource(jsonBuilder()
			           	 .startObject()
	                        .field("date", esUtilTest.oneDayAgoFrom().split(" ")[0])
	                        .field("gameId", game)
	                        .field("userTotal", srTotal.getHits().totalHits())
	                        .field("key", "all")
	                        .field("ts_total",ts.toString())
	                        .field("@timestamp", new Date())
	                    .endObject()
		                  )
		        );
		logger.debug("历史累计用户all："+srTotal.getHits().totalHits());
		
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}

	}	
	
	public void esServerZone() throws IOException, ParseException {	
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd 00:00:00.000" ); 
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		Long ts = sdf.parse(esUtilTest.oneDayAgoFrom()).getTime();
		//运营大区用户增加   
		SearchResponse sr = client.prepareSearch(index).setTypes(type).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
		        		FilterBuilders.termFilter("日志分类关键字", "create"))
		        ))
				.addAggregation(
			    		AggregationBuilders.terms("serverZone").field("运营大区ID").size(szsize)
			    )
				.setSize(szsize).execute().actionGet();
		Terms genders = sr.getAggregations().get("serverZone");	
		
		for (Terms.Bucket e : genders.getBuckets()) {
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_add)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split(" ")[0])
		                        .field("gameId", game)
		                        .field("userAdd", e.getDocCount())
		                        .field("key", "serverZone")
		                        .field("value",e.getKey())
		                        .field("ts_add",ts.toString())
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
			logger.debug("昨天新增用户serverZone："+e.getDocCount()  +" " + e.getKey());
		}
		
		//运营大区用户累计
		
		Terms gendersTotal = null;
		Map<String, Long> map = new HashMap<String, Long>();
		SearchResponse srTotal = client.prepareSearch(index).setTypes(type).setSearchType("count")
				.setQuery(
						QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
								FilterBuilders.andFilter(FilterBuilders.rangeFilter("日期").from("2014-01-11").to(esUtilTest.nowDate()),FilterBuilders.termFilter("日志分类关键字", "create"))
				        ))
				.addAggregation(
			    		AggregationBuilders.terms("serverZone").field("运营大区ID").size(szsize)
			    )
				.setSize(szsize).execute().actionGet();
		gendersTotal = srTotal.getAggregations().get("serverZone");
		for (Terms.Bucket entry : gendersTotal.getBuckets()) {
			map.put(entry.getKey(), entry.getDocCount());
		}
		
		for(Entry<String,Long> entry : map.entrySet()){
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_total)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split(" ")[0])
		                        .field("gameId", game)
		                        .field("userTotal", entry.getValue().toString())
		                        .field("key", "serverZone")
		                        .field("value",entry.getKey())
		                        .field("ts_total",ts.toString())
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
			logger.debug("历史累计用户serverZone："+entry.getValue().toString()  +"  " +entry.getKey());
		}
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}
	}
	
	
	public void esPlatForm() throws IOException, ParseException {	
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd 00:00:00.000" ); 
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		Long ts = sdf.parse(esUtilTest.oneDayAgoFrom()).getTime();
		//渠道用户增加
		SearchResponse sr = client.prepareSearch(index).setTypes(type).setSearchType("count").setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
		        		FilterBuilders.termFilter("日志分类关键字", "create"))
		        ))
				.addAggregation(
			    		AggregationBuilders.terms("platForm").field("渠道ID").size(pfsize)
			    )
				.setSize(pfsize).execute().actionGet();
		Terms genders = sr.getAggregations().get("platForm");	
		for (Terms.Bucket e : genders.getBuckets()) {
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_add)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split(" ")[0])
		                        .field("gameId", game)
		                        .field("userAdd", e.getDocCount())
		                        .field("key", "platForm")
		                        .field("value",e.getKey())
		                        .field("ts_add",ts.toString())
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
			logger.debug("昨天新增用户platForm："+e.getDocCount()  +" " + e.getKey());
		}

		//渠道用户累计
		
		Terms gendersTotal = null;
		Map<String, Long> map = new HashMap<String, Long>();
		FilteredQueryBuilder builderTotal = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),FilterBuilders.andFilter(FilterBuilders.rangeFilter("日期").from("2014-01-11").to(esUtilTest.nowDate()),FilterBuilders.termFilter("日志分类关键字", "create")));
		SearchResponse srTotal = client.prepareSearch(index).setTypes(type).setSearchType("count").setQuery(builderTotal)
				.addAggregation(
			    		AggregationBuilders.terms("platForm").field("渠道ID").size(pfsize)
			    )
				.setSize(pfsize).execute().actionGet();
		gendersTotal = srTotal.getAggregations().get("platForm");
		for (Terms.Bucket entry : gendersTotal.getBuckets()) {
			map.put(entry.getKey(), entry.getDocCount());
		}
		
		for(Entry<String,Long> entry : map.entrySet()){
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_total)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split(" ")[0])
		                        .field("gameId", game)
		                        .field("userTotal", entry.getValue().toString())
		                        .field("key", "platForm")
		                        .field("value",entry.getKey())
		                        .field("ts_total",ts.toString())
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
			logger.debug("历史累计用户platForm："+entry.getValue().toString()  +"  " +entry.getKey());
		}
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}
	}
	
	public void esServer() throws IOException, ParseException {	
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd 00:00:00.000" ); 
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		Long ts = sdf.parse(esUtilTest.oneDayAgoFrom()).getTime();
		//服务器用户增加
		SearchResponse sr = client.prepareSearch(index).setTypes(type).setSearchType("count").setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
		        		FilterBuilders.termFilter("日志分类关键字", "create"))
		        ))
				.addAggregation(
			    		AggregationBuilders.terms("server").field("服务器ID").size(srsize)
			    )
				.setSize(srsize).execute().actionGet();
		Terms genders = sr.getAggregations().get("server");	
		for (Terms.Bucket e : genders.getBuckets()) {
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_add)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split(" ")[0])
		                        .field("gameId", game)
		                        .field("userAdd", e.getDocCount())
		                        .field("key", "server")
		                        .field("value",e.getKey())
		                        .field("ts_add",ts.toString())
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
			logger.debug("昨天新增用户server："+e.getDocCount()  +" " + e.getKey());
		}
		
		//服务器用户累计
		
		Terms gendersTotal = null;
		Map<String, Long> map = new HashMap<String, Long>();
		SearchResponse srTotal = client.prepareSearch(index).setTypes(type).setSearchType("count").setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(FilterBuilders.rangeFilter("日期").from("2014-01-11").to(esUtilTest.nowDate()),FilterBuilders.termFilter("日志分类关键字", "create"))))
				.addAggregation(
			    		AggregationBuilders.terms("server").field("服务器ID").size(srsize)
			    )
				.setSize(srsize).execute().actionGet();
		gendersTotal = srTotal.getAggregations().get("server");
		for (Terms.Bucket entry : gendersTotal.getBuckets()) {
			map.put(entry.getKey(), entry.getDocCount());
		}
		
		for(Entry<String,Long> entry : map.entrySet()){
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_total)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split(" ")[0])
		                        .field("gameId", game)
		                        .field("userTotal", entry.getValue().toString())
		                        .field("key", "server")
		                        .field("value",entry.getKey())
		                        .field("ts_total",ts.toString())
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
			logger.debug("历史累计用户server："+entry.getValue().toString()  +"  " +entry.getKey());
		}
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}
	}
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void schedual() throws Exception{
		logger.info("----------------fb useradd usertotal begin----------------");
		try {
			esAll();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("fb all useradd usertotal error "+e);
		}
		
		try {
			esServerZone();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("fb serverZone useradd usertotal error "+e);
		}
		
		try {
			esPlatForm();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("fb platForm useradd usertotal error "+e);
		}
		
		try {
			esServer();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("fb server useradd usertotal error "+e);
		}
		
		logger.info("----------------fb useradd usertotal end-----------------");
	}
	

}
