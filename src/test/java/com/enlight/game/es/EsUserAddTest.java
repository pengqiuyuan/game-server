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
import java.util.Map.Entry;

import org.codehaus.jackson.map.DeserializationConfig;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.FilterBuilder;
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

import com.enlight.game.entity.analysis.UserAdd;
import com.enlight.game.entity.analysis.UserRetained;
import com.enlight.game.service.platForm.PlatFormService;
import com.enlight.game.service.server.ServerService;
import com.enlight.game.service.serverZone.ServerZoneService;
import com.enlight.game.service.store.StoreService;
import com.enlight.game.util.EsUtil;
import com.enlight.game.util.JsonBinder;

/**
 * 测试Elasticsearch 连接
 * @author pengqiuyuan
 * @ContextConfiguration(locations = {"/applicationContext.xml", "/application_es.xml" })
 *
 */
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class EsUserAddTest extends SpringTransactionalTestCase{
	@Autowired
	private Client client;
	
	//项目名称
	private static final String game = "FB";
	
	private static final String index = "logstash-fb-*";
	
	private static final String type = "fb_user.log";
	
	private static final String bulk_index = "log_fb_user";
	
	private static final String bulk_type_add = "fb_user_add";
	
	private static final String bulk_type_total = "fb_user_total";
	
	@Autowired
	private ServerZoneService serverZoneService;
	
	@Autowired
	private ServerService serverService;
	
	@Autowired
	private PlatFormService platFormService;
	
	@Autowired
	private StoreService storeService;
	
	EsUtilTest esUtilTest = new EsUtilTest();
	
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	
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
				        FilterBuilders.rangeFilter("@timestamp").from(from).to(to),
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
				        FilterBuilders.rangeFilter("@timestamp").from(from).to(to),
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
				        FilterBuilders.rangeFilter("@timestamp").from(from).to(to),
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
	
	@Test
	public void testuseradd() throws ElasticsearchException, IOException, ParseException{
		
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
		//新增用户
		SearchResponse sr = client.prepareSearch(index).setTypes(type).setSearchType("count").setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
		        		FilterBuilders.termFilter("日志分类关键字", "create"))
		        )).execute().actionGet();
		
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
			System.out.println(esUtilTest.twoDayAgoFrom()+"    "+esUtilTest.twoDayAgoF() + "  11111111111  " + srTotal);
			
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
									FilterBuilders.rangeFilter("@timestamp").from("2014-01-11").to(esUtilTest.nowDate()),
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
		//bulkRequest.execute().actionGet();
	}
	
	//@Test
	public void testuseraddServerZone() throws ElasticsearchException, IOException, ParseException{
		//UserAdd userAdd = new UserAdd();
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		Long ts = sdf.parse(esUtilTest.oneDayAgoFrom()).getTime();
		//运营大区用户增加   
		SearchResponse sr = client.prepareSearch(index).setTypes(type).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
		        		FilterBuilders.termFilter("日志分类关键字", "create"))
		        ))
				.addAggregation(
			    		AggregationBuilders.terms("serverZone").field("运营大区ID").size(10)
			    )
				.setSize(10).execute().actionGet();
		Terms genders = sr.getAggregations().get("serverZone");	
		List<Long> serverZones = serverZoneService.findServerId();
		
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
			serverZones.remove(Long.valueOf(e.getKey()));
		}
		for (Long lo : serverZones) {
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_add)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("userAdd", 0)
		                        .field("key", "serverZone")
		                        .field("value",lo.toString())
		                        .field("ts_add",ts.toString())
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
			System.out.println("昨天新增用户serverZone,全部是0："+" " + lo.toString());
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
		List<Long> szs = serverZoneService.findServerId();
		Map<String, Long> map = new HashMap<String, Long>();
		if(responseindex.isExists() && ty){//判断index是否存在 判断type是否存在
			SearchResponse srTotal = client.prepareSearch(bulk_index).setTypes(bulk_type_total).
					setQuery(
							QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
									FilterBuilders.andFilter(
									        FilterBuilders.termFilter("date", esUtilTest.twoDayAgoFrom()),
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
									FilterBuilders.andFilter(FilterBuilders.rangeFilter("@timestamp").from("2014-01-11").to(esUtilTest.nowDate()),FilterBuilders.termFilter("日志分类关键字", "create"))
					        ))
					.addAggregation(
				    		AggregationBuilders.terms("serverZone").field("运营大区ID").size(10)
				    )
					.setSize(10).execute().actionGet();
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
			szs.remove(Long.valueOf(entry.getKey()));
		}
		
		for (Long lo : szs) {
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_total)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("userTotal", 0)
		                        .field("key", "serverZone")
		                        .field("value",lo.toString())
		                        .field("ts_total",ts.toString())
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
			System.out.println("历史累计用户serverZone,全部是0："+"  " +lo.toString());
		}
		
		bulkRequest.execute().actionGet();			
	}
	
	
	//@Test
	public void testuseraddPlatForm() throws ElasticsearchException, IOException, ParseException{
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		Long ts = sdf.parse(esUtilTest.oneDayAgoFrom()).getTime();
		//渠道用户增加
		SearchResponse sr = client.prepareSearch(index).setTypes(type).setSearchType("count").setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
		        		FilterBuilders.termFilter("日志分类关键字", "create"))
		        ))
				.addAggregation(
			    		AggregationBuilders.terms("platForm").field("渠道ID").size(300)
			    )
				.setSize(300).execute().actionGet();
		Terms genders = sr.getAggregations().get("platForm");	
		List<String> platForms = platFormService.findPlatFormId();
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
			platForms.remove(e.getKey());
		}
		for (String lo : platForms) {
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_add)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("userAdd", 0)
		                        .field("key", "platForm")
		                        .field("value",lo)
		                        .field("ts_add",ts.toString())
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
			System.out.println("昨天新增用户platForm,全部是0："+" " + lo.toString());
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
		List<String> pfs = platFormService.findPlatFormId();
		Map<String, Long> map = new HashMap<String, Long>();
		if(responseindex.isExists() && ty){//判断index是否存在 判断type是否存在
			SearchResponse srTotal = client.prepareSearch(bulk_index).setTypes(bulk_type_total).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
					FilterBuilders.andFilter(
					        FilterBuilders.termFilter("date", esUtilTest.twoDayAgoFrom()),
			        		FilterBuilders.termFilter("key", "platForm"))
			        ))
					.addAggregation(
				    		AggregationBuilders.terms("platForm").field("value").size(300)
				    )
					.setSize(300).execute().actionGet();
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
			FilteredQueryBuilder builderTotal = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),FilterBuilders.andFilter(FilterBuilders.rangeFilter("@timestamp").from("2014-01-11").to(esUtilTest.nowDate()),FilterBuilders.termFilter("日志分类关键字", "create")));
			SearchResponse srTotal = client.prepareSearch(index).setTypes(type).setSearchType("count").setQuery(builderTotal)
					.addAggregation(
				    		AggregationBuilders.terms("platForm").field("渠道ID").size(300)
				    )
					.setSize(300).execute().actionGet();
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
			pfs.remove(entry.getKey());
		}
		
		for (String lo : pfs) {
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_total)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("userTotal", 0)
		                        .field("key", "platForm")
		                        .field("value",lo.toString())
		                        .field("ts_total",ts.toString())
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
			System.out.println("历史累计用户platForm,全部是0："+"  " +lo.toString());
		}
		bulkRequest.execute().actionGet();		
	}	
	
	//@Test
	public void testuseraddServer() throws ElasticsearchException, IOException, ParseException{
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		Long ts = sdf.parse(esUtilTest.oneDayAgoFrom()).getTime();
		//服务器用户增加
		SearchResponse sr = client.prepareSearch(index).setTypes(type).setSearchType("count").setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
		        		FilterBuilders.termFilter("日志分类关键字", "create"))
		        ))
				.addAggregation(
			    		AggregationBuilders.terms("server").field("服务器ID").size(300)
			    )
				.setSize(300).execute().actionGet();
		Terms genders = sr.getAggregations().get("server");	
		List<String> serverIds = serverService.findServerId(storeService.findByName(game).getId().toString());
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
			serverIds.remove(e.getKey());
		}
		
		for (String lo : serverIds) {
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_add)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("userAdd", 0)
		                        .field("key", "server")
		                        .field("value",lo)
		                        .field("ts_add",ts.toString())
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
			System.out.println("昨天新增用户server,全部是0："+" " + lo.toString());
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
		List<String> sis = serverService.findServerId(storeService.findByName(game).getId().toString());
		Map<String, Long> map = new HashMap<String, Long>();
		if(responseindex.isExists() && ty){//判断index是否存在 判断type是否存在
			SearchResponse srTotal = client.prepareSearch(bulk_index).setTypes(bulk_type_total).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
					FilterBuilders.andFilter(
					        FilterBuilders.termFilter("date", esUtilTest.twoDayAgoFrom()),
			        		FilterBuilders.termFilter("key", "server"))
			        ))
					.addAggregation(
				    		AggregationBuilders.terms("server").field("value").size(300)
				    )
					.setSize(300).execute().actionGet();
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
					FilterBuilders.andFilter(FilterBuilders.rangeFilter("@timestamp").from("2014-01-11").to(esUtilTest.nowDate()),FilterBuilders.termFilter("日志分类关键字", "create"))))
					.addAggregation(
				    		AggregationBuilders.terms("server").field("服务器ID").size(300)
				    )
					.setSize(300).execute().actionGet();
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
			sis.remove(entry.getKey());
		}
		
		for (String lo : sis) {
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_total)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", game)
		                        .field("userTotal", 0)
		                        .field("key", "server")
		                        .field("value",lo.toString())
		                        .field("ts_total",ts.toString())
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
			System.out.println("历史累计用户server,全部是0："+"  " +lo.toString());
		}
		
		bulkRequest.execute().actionGet();		
	}	
	//@Test
	public void testuseradr() throws ElasticsearchException, IOException, ParseException{
		testuseradd();
		testuseraddServerZone();
		testuseraddPlatForm();
		testuseraddServer();
	}
	
}

