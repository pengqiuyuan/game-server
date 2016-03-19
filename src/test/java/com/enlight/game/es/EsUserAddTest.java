package com.enlight.game.es;


import java.io.IOException;
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
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.test.spring.SpringTransactionalTestCase;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import com.enlight.game.service.store.StoreService;
import com.enlight.game.util.EsUtil;

/**
 * 测试Elasticsearch 连接
 * @author pengqiuyuan
 * @ContextConfiguration(locations = {"/applicationContext.xml", "/application_es.xml" })
 *
 */
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class EsUserAddTest extends SpringTransactionalTestCase{
	@Autowired
	public Client client;
	
	//项目名称
	private static final String game = "FB";
	
	private static final String index = "logstash-fb-user*";
	
	private static final String type = "fb_userlog";
	
	private static final String bulk_index = "log_fb_user";
	
	private static final String bulk_type_add = "fb_user_add";
	
	private static final String bulk_type_total = "fb_user_total";
	
	private static final Integer szsize = 10; //运营大区
	
	private static final Integer pfsize = 300; //渠道
	
	private static final Integer srsize = 300; //服务器
	
	EsUtil esUtilTest = new EsUtil();
	
	@Autowired
	private StoreService storeService;

	@Test
	public void esAll() throws IOException, ParseException {	
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
		//新增用户
		SearchResponse sr = client.prepareSearch(index).setTypes(type).setSearchType("count").setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
		        		FilterBuilders.termFilter("日志分类关键字", "create"))
		        )).execute().actionGet();
		SearchResponse srs = client.prepareSearch(index).setTypes(type).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("日期").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
		        		FilterBuilders.termFilter("日志分类关键字", "create"))
		        )).execute().actionGet();
		System.out.println("111111111  "  + sr);
		System.out.println("222222222  "  + srs);
		
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		Long ts = sdf.parse(esUtilTest.oneDayAgoFrom()).getTime();
		bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_add)
		        .setSource(jsonBuilder()
			           	 .startObject() 
	                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
	                        .field("gameId", game)
	                        .field("userAdd", sr.getHits().totalHits())
	                        .field("key", "all")
	                        .field("ts_add",ts.toString())
	                        .field("@timestamp", new Date())
	                    .endObject()
		                  )
		        );
		System.out.println("昨天新增用户all："+sr.getHits().totalHits());
		//累计用户
		IndicesExistsResponse responseindex = client.admin().indices().prepareExists(bulk_index).execute().actionGet(); 
		boolean ty = false;
		if(responseindex.isExists()){
			TypesExistsResponse responsetype = client.admin().indices() .prepareTypesExists(bulk_index).setTypes(bulk_type_total).execute().actionGet(); 
			if(responsetype.isExists()){
				ty = true;
			}
		}
		if(responseindex.isExists() && ty){//判断index是否存在 判断type是否存在
			System.out.println("存在");
			SearchResponse srTotal = client.prepareSearch(bulk_index).setTypes(bulk_type_total).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
					FilterBuilders.andFilter(
							FilterBuilders.termFilter("key", "all"),
					        FilterBuilders.termFilter("date", esUtilTest.twoDayAgoF()))
			        )).execute().actionGet();
			long s  = 0L;
			for (SearchHit searchHit : srTotal.getHits()) {
				Map<String, Object> source = searchHit.getSource();
				s = Long.valueOf(source.get("userTotal").toString());
			}
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_total)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("userTotal", s+sr.getHits().totalHits())
		                        .field("key", "all")
		                        .field("ts_total",ts.toString())
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
			System.out.println(s+sr.getHits().totalHits());
			System.out.println("历史累计用户all："+s+sr.getHits().totalHits()  +",前天累计用户："+s+",昨天新增用户："+ sr.getHits().totalHits());
		}else{
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
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("userTotal", srTotal.getHits().totalHits())
		                        .field("key", "all")
		                        .field("ts_total",ts.toString())
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
			System.out.println("历史累计用户all："+srTotal.getHits().totalHits());
		}
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}

	}	
	
	public void esServerZone() throws IOException, ParseException {	
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
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
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("userAdd", e.getDocCount())
		                        .field("key", "serverZone")
		                        .field("value",e.getKey())
		                        .field("ts_add",ts.toString())
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
			System.out.println("昨天新增用户serverZone："+e.getDocCount()  +" " + e.getKey());
		}
		
		//运营大区用户累计
		IndicesExistsResponse responseindex = client.admin().indices().prepareExists(bulk_index).execute().actionGet(); 
		boolean ty = false;
		if(responseindex.isExists()){
			TypesExistsResponse responsetype = client.admin().indices() .prepareTypesExists(bulk_index).setTypes(bulk_type_total).execute().actionGet(); 
			if(responsetype.isExists()){
				ty = true;
			}
		}
		Terms gendersTotal = null;
		Map<String, Long> map = new HashMap<String, Long>();
		if(responseindex.isExists() && ty){//判断index是否存在 判断type是否存在
			SearchResponse srTotal = client.prepareSearch(bulk_index).setTypes(bulk_type_total).
					setQuery(
							QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
									FilterBuilders.andFilter(
									        FilterBuilders.termFilter("date", esUtilTest.twoDayAgoF()),
							        		FilterBuilders.termFilter("key", "serverZone"))
					        ))
					.execute().actionGet();
			
			for (SearchHit searchHit : srTotal.getHits()) {
				Map<String, Object> source = searchHit.getSource();
				long s = Long.valueOf(source.get("userTotal").toString());
				map.put(source.get("value").toString(), s);
			}
			
			for (Terms.Bucket e : genders.getBuckets()) {
				if(map.containsKey(e.getKey())){
					map.put(e.getKey(), map.get(e.getKey())+e.getDocCount());
				}else{
					map.put(e.getKey(), e.getDocCount());
				}
			}
		}else{
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
		}

		for(Entry<String,Long> entry : map.entrySet()){
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_total)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("userTotal", entry.getValue().toString())
		                        .field("key", "serverZone")
		                        .field("value",entry.getKey())
		                        .field("ts_total",ts.toString())
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
			System.out.println("历史累计用户serverZone："+entry.getValue().toString()  +"  " +entry.getKey());
		}
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}
	}
	
	
	public void esPlatForm() throws IOException, ParseException {	
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
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
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("userAdd", e.getDocCount())
		                        .field("key", "platForm")
		                        .field("value",e.getKey())
		                        .field("ts_add",ts.toString())
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
			System.out.println("昨天新增用户platForm："+e.getDocCount()  +" " + e.getKey());
		}

		//渠道用户累计
		IndicesExistsResponse responseindex = client.admin().indices().prepareExists(bulk_index).execute().actionGet(); 
		boolean ty = false;
		if(responseindex.isExists()){
			TypesExistsResponse responsetype = client.admin().indices() .prepareTypesExists(bulk_index).setTypes(bulk_type_total).execute().actionGet(); 
			if(responsetype.isExists()){
				ty = true;
			}
		}
		Terms gendersTotal = null;
		Map<String, Long> map = new HashMap<String, Long>();
		if(responseindex.isExists() && ty){//判断index是否存在 判断type是否存在
			SearchResponse srTotal = client.prepareSearch(bulk_index).setTypes(bulk_type_total).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
					FilterBuilders.andFilter(
					        FilterBuilders.termFilter("date", esUtilTest.twoDayAgoF()),
			        		FilterBuilders.termFilter("key", "platForm"))
			        ))
					.addAggregation(
				    		AggregationBuilders.terms("platForm").field("value").size(pfsize)
				    )
					.setSize(pfsize).execute().actionGet();
			for (SearchHit searchHit : srTotal.getHits()) {
				Map<String, Object> source = searchHit.getSource();
				long s = Long.valueOf(source.get("userTotal").toString());
				map.put(source.get("value").toString(), s);
			}
			for (Terms.Bucket e : genders.getBuckets()) {
				if(map.containsKey(e.getKey())){
					map.put(e.getKey(), map.get(e.getKey())+e.getDocCount());
				}else{
					map.put(e.getKey(), e.getDocCount());
				}
			}
		}else{
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
		}

		for(Entry<String,Long> entry : map.entrySet()){
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_total)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("userTotal", entry.getValue().toString())
		                        .field("key", "platForm")
		                        .field("value",entry.getKey())
		                        .field("ts_total",ts.toString())
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
			System.out.println("历史累计用户platForm："+entry.getValue().toString()  +"  " +entry.getKey());
		}
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}
	}
	
	public void esServer() throws IOException, ParseException {	
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
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
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("userAdd", e.getDocCount())
		                        .field("key", "server")
		                        .field("value",e.getKey())
		                        .field("ts_add",ts.toString())
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
			System.out.println("昨天新增用户server："+e.getDocCount()  +" " + e.getKey());
		}
		
		//服务器用户累计
		IndicesExistsResponse responseindex = client.admin().indices().prepareExists(bulk_index).execute().actionGet(); 
		boolean ty = false;
		if(responseindex.isExists()){
			TypesExistsResponse responsetype = client.admin().indices() .prepareTypesExists(bulk_index).setTypes(bulk_type_total).execute().actionGet(); 
			if(responsetype.isExists()){
				ty = true;
			}
		}
		Terms gendersTotal = null;
		Map<String, Long> map = new HashMap<String, Long>();
		if(responseindex.isExists() && ty){//判断index是否存在 判断type是否存在
			SearchResponse srTotal = client.prepareSearch(bulk_index).setTypes(bulk_type_total).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
					FilterBuilders.andFilter(
					        FilterBuilders.termFilter("date", esUtilTest.twoDayAgoF()),
			        		FilterBuilders.termFilter("key", "server"))
			        ))
					.addAggregation(
				    		AggregationBuilders.terms("server").field("value").size(srsize)
				    )
					.setSize(srsize).execute().actionGet();
			for (SearchHit searchHit : srTotal.getHits()) {
				Map<String, Object> source = searchHit.getSource();
				long s = Long.valueOf(source.get("userTotal").toString());
				map.put(source.get("value").toString(), s);
			}
			for (Terms.Bucket e : genders.getBuckets()) {
				if(map.containsKey(e.getKey())){
					map.put(e.getKey(), map.get(e.getKey())+e.getDocCount());
				}else{
					map.put(e.getKey(), e.getDocCount());
				}
			}
		}else{
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
		}

		for(Entry<String,Long> entry : map.entrySet()){
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_total)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("userTotal", entry.getValue().toString())
		                        .field("key", "server")
		                        .field("value",entry.getKey())
		                        .field("ts_total",ts.toString())
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
			System.out.println("历史累计用户server："+entry.getValue().toString()  +"  " +entry.getKey());
		}
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}
	}
	
	@Test
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

