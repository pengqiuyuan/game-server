package com.enlight.game.schedual.kun;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enlight.game.entity.analysis.UserRetained;
import com.enlight.game.util.EsUtil;

@Transactional(readOnly = true)
public class KunRetainedScheduled {
	
	@Autowired
	public Client client;
	
	public static final Logger logger = LoggerFactory.getLogger(KunRetainedScheduled.class);
	
	//项目名称
	private static final String game = "KUN";
	
	private static final String index = "logstash-kun-user-*";
	
	private static final String type = "kun_userlog";
	
	private static final String bulk_index = "log_kun_user";
	
	private static final String bulk_type_retained = "kun_user_retained";
	
	private static final Integer szsize = 10; //运营大区
	
	private static final Integer pfsize = 300; //渠道
	
	private static final Integer srsize = 300; //服务器
	
	private static final Integer num = 35; //35天以内的留存
	
	EsUtil esUtilTest = new EsUtil();
	
	public Long createCount(String from , String to){
		SearchResponse sr = client.prepareSearch(index).setTypes(type).setSearchType("count")
				.setQuery(
		        		QueryBuilders.boolQuery()
		        		.must( QueryBuilders.rangeQuery("@timestamp").from(from).to(to))
		        		.must( QueryBuilders.termsQuery("日志分类关键字", "create"))
		        )
				.execute().actionGet();
		Long count = sr.getHits().getTotalHits();
		return count;
	}
	
	public Long createServerZoneCount(Object object,String from , String to){
		SearchResponse sr = client.prepareSearch(index).setTypes(type).setSearchType("count")
				.setQuery(
		        		QueryBuilders.boolQuery()
		        		.must( QueryBuilders.rangeQuery("@timestamp").from(from).to(to))
		        		.must( QueryBuilders.termsQuery("日志分类关键字", "create"))
		        		.must( QueryBuilders.termsQuery("运营大区ID", object))
		        )
				.execute().actionGet();
		Long count = sr.getHits().getTotalHits();
		return count;
	}
	
	public Long createPlatFormCount(String key,String from , String to){
		SearchResponse sr = client.prepareSearch(index).setTypes(type).setSearchType("count")
				.setQuery(
		        		QueryBuilders.boolQuery()
		        		.must( QueryBuilders.rangeQuery("@timestamp").from(from).to(to))
		        		.must( QueryBuilders.termsQuery("日志分类关键字", "create"))
		        		.must( QueryBuilders.termsQuery("渠道ID", key))
		        )
				.execute().actionGet();
		Long count = sr.getHits().getTotalHits();
		return count;
	}
	
	public Long createServerCount(String key,String from , String to){
		SearchResponse sr = client.prepareSearch(index).setTypes(type).setSearchType("count")
				.setQuery(
		        		QueryBuilders.boolQuery()
		        		.must( QueryBuilders.rangeQuery("@timestamp").from(from).to(to))
		        		.must( QueryBuilders.termsQuery("日志分类关键字", "create"))
		        		.must( QueryBuilders.termsQuery("服务器ID", key))
		        )
				.execute().actionGet();
		Long count = sr.getHits().getTotalHits();
		return count;
	}


	public void esAll() throws IOException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数  
		
		SearchResponse sr = client.prepareSearch(index).setSearchType("count").setTypes(type)
				.setQuery(
		        		QueryBuilders.boolQuery()
		        		.must( QueryBuilders.rangeQuery("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()) )
		        		.must( QueryBuilders.termsQuery("日志分类关键字", "login"))
		        )
			    .addAggregation(
			    		AggregationBuilders.terms("create").field("注册时间").order(Terms.Order.term(false))
			    		.subAggregation(AggregationBuilders.cardinality("agg").field("玩家GUID")).size(num)
			    ).execute().actionGet();
		logger.debug("----------------esAll---------------");
		
		Terms genders = sr.getAggregations().get("create");	
		for (Terms.Bucket entry : genders.getBuckets()) {
			String dateBucket = sdf.format(new Date((Long) entry.getKeyAsNumber()));
			if(dateBucket.equals(esUtilTest.twoDayAgoFrom())){
			    Cardinality agg = entry.getAggregations().get("agg");
			    Long aggcount = agg.getValue();
			    Double RetentionTwo ;
			    
			    Long r = createCount(esUtilTest.twoDayAgoFrom(), esUtilTest.twoDayAgoTo());
			    if(r==0){
				    RetentionTwo = (double) 0.00;
			    }else{
				    RetentionTwo = (double)aggcount*100/createCount(esUtilTest.twoDayAgoFrom(), esUtilTest.twoDayAgoTo());
			    }
    
				bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_retained)
				        .setSource(jsonBuilder()
					           	 .startObject()
			                        .field("date", esUtilTest.twoDayAgoFrom().split("T")[0])
			                        .field("gameId", game)
			                        .field("ctRetained", UserRetained.CT_NEXTDAY)
			                        .field("retained", df.format(RetentionTwo))
			                        .field("key", UserRetained.KEY_ALL)
			                        .field("@timestamp", new Date())
			                    .endObject()
				                  )
				        );
			}else if(dateBucket.equals(esUtilTest.eightDayAgoFrom())){
			    Cardinality agg = entry.getAggregations().get("agg");
			    Long aggcount = agg.getValue();
			    Double RetentionEight ;
				RetentionEight = (double)aggcount*100/createCount(esUtilTest.eightDayAgoFrom(), esUtilTest.eightDayAgoTo());			 
				bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_retained)
				        .setSource(jsonBuilder()
					           	 .startObject()
			                        .field("date", esUtilTest.eightDayAgoFrom().split("T")[0])
			                        .field("gameId", game)
			                        .field("ctRetained", UserRetained.CT_SEVENDAY)
			                        .field("retained", df.format(RetentionEight))
			                        .field("key", UserRetained.KEY_ALL)
			                        .field("@timestamp", new Date())
			                    .endObject()
				                  )
				        );
			}else if(dateBucket.equals(esUtilTest.thirtyOneDayAgoFrom())){
			    Cardinality agg = entry.getAggregations().get("agg");
			    Long aggcount = agg.getValue();
			    Double RetentionThirty ;
			    RetentionThirty = (double)aggcount*100/createCount(esUtilTest.thirtyOneDayAgoFrom(), esUtilTest.thirtyOneDayAgoTo());
				bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_retained)
				        .setSource(jsonBuilder()
					           	 .startObject()
			                        .field("date", esUtilTest.thirtyOneDayAgoFrom().split("T")[0])
			                        .field("gameId", game)
			                        .field("ctRetained", UserRetained.CT_THIRYTDAY)
			                        .field("retained", df.format(RetentionThirty))
			                        .field("key", UserRetained.KEY_ALL)
			                        .field("@timestamp", new Date())
			                    .endObject()
				                  )
				        );
			}
		}
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}
	}	
	
	public void esServerZone() throws IOException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		//计算时间（当前）2015-04-15 ，统计出2015-04-14到2015-04-15的数据 ，得出2015-04-13的次日留存、得出2015-04-07的7日留存、得出2015-03-15的30日留存
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数  
		
		SearchResponse sr = client.prepareSearch(index).setTypes(type).setSearchType("count")
				.setQuery(
		        		QueryBuilders.boolQuery()
		        		.must( QueryBuilders.rangeQuery("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()) )
		        		.must( QueryBuilders.termsQuery("日志分类关键字", "login"))
		        )
			    .addAggregation(
			    		AggregationBuilders.terms("create").field("注册时间").order(Terms.Order.term(false)).size(num)
			    		.subAggregation(
			    				AggregationBuilders.cardinality("agg").field("玩家GUID").precisionThreshold(10000)
			    				)
			    		.subAggregation(
			    				AggregationBuilders.terms("serverZone").field("运营大区ID").size(szsize).subAggregation(
			    						AggregationBuilders.cardinality("agg").field("玩家GUID").precisionThreshold(10000)
			    						)
			    				)
			    ).execute().actionGet();
		logger.debug("-------------esServerZone-------------");

		Terms genders = sr.getAggregations().get("create");	
		for (Terms.Bucket entry : genders.getBuckets()) {
			String dateBucket = sdf.format(new Date((Long) entry.getKeyAsNumber()));
			if(dateBucket.equals(esUtilTest.twoDayAgoFrom())){
				Terms serverZone = entry.getAggregations().get("serverZone");
				
				for (Terms.Bucket e : serverZone.getBuckets()) {
				    Cardinality agg = e.getAggregations().get("agg");
				    Long aggcount = agg.getValue();
				    Double RetentionTwo ;

				    Long r = createServerZoneCount((String) e.getKey(),esUtilTest.twoDayAgoFrom(), esUtilTest.twoDayAgoTo());
				    if(r==0){
					    RetentionTwo = (double) 0.00;
				    }else{
				    	RetentionTwo = (double)aggcount*100/createServerZoneCount((String) e.getKey(),esUtilTest.twoDayAgoFrom(), esUtilTest.twoDayAgoTo());
				    }
				    
					bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_retained)
					        .setSource(jsonBuilder()
						           	 .startObject()
				                        .field("date", esUtilTest.twoDayAgoFrom().split("T")[0])
				                        .field("gameId",game)
				                        .field("ctRetained", UserRetained.CT_NEXTDAY)
				                        .field("retained", df.format(RetentionTwo))
				                        .field("key", UserRetained.KEY_SEVSERZONE)
				                        .field("value", e.getKey())
				                        .field("@timestamp", new Date())
				                    .endObject()
					                  )
					        );			    	
				}
			}else if(dateBucket.equals(esUtilTest.eightDayAgoFrom())){
				Terms serverZone = entry.getAggregations().get("serverZone");
				for (Terms.Bucket e : serverZone.getBuckets()) {
				    Cardinality agg = e.getAggregations().get("agg");
				    Long aggcount = agg.getValue();
				    Double RetentionEight ;
					RetentionEight = (double)aggcount*100/createServerZoneCount(e.getKey(),esUtilTest.eightDayAgoFrom(), esUtilTest.eightDayAgoTo());
					bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_retained)
					        .setSource(jsonBuilder()
						           	 .startObject()
				                        .field("date", esUtilTest.eightDayAgoFrom().split("T")[0])
				                        .field("gameId", game)
				                        .field("ctRetained", UserRetained.CT_SEVENDAY)
				                        .field("retained", df.format(RetentionEight))
				                        .field("key", UserRetained.KEY_SEVSERZONE)
				                        .field("value", e.getKey())
				                        .field("@timestamp", new Date())
				                    .endObject()
					                  )
					        );
				}
			}else if(dateBucket.equals(esUtilTest.thirtyOneDayAgoFrom())){
				Terms serverZone = entry.getAggregations().get("serverZone");
				for (Terms.Bucket e : serverZone.getBuckets()) {
				    Cardinality agg = e.getAggregations().get("agg");
				    Long aggcount = agg.getValue();
				    Double RetentionThirty ;
				    RetentionThirty = (double)aggcount*100/createServerZoneCount(e.getKey(),esUtilTest.thirtyOneDayAgoFrom(), esUtilTest.thirtyOneDayAgoTo());
				    
					bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_retained)
					        .setSource(jsonBuilder()
						           	 .startObject()
				                        .field("date", esUtilTest.thirtyOneDayAgoFrom().split("T")[0])
				                        .field("gameId", game)
				                        .field("ctRetained", UserRetained.CT_THIRYTDAY)
				                        .field("retained", df.format(RetentionThirty))
				                        .field("key", UserRetained.KEY_SEVSERZONE)
				                        .field("value", e.getKey())
				                        .field("@timestamp", new Date())
				                    .endObject()
					                  )
					        );
				}
			}
		}
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}
	}
	
	
	public void esPlatForm() throws IOException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		//计算时间（当前）2015-04-15 ，统计出2015-04-14到2015-04-15的数据 ，得出2015-04-13的次日留存、得出2015-04-07的7日留存、得出2015-03-15的30日留存
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数  
		
		SearchResponse sr = client.prepareSearch(index).setTypes(type).setSearchType("count")
				.setQuery(
		        		QueryBuilders.boolQuery()
		        		.must( QueryBuilders.rangeQuery("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()) )
		        		.must( QueryBuilders.termsQuery("日志分类关键字", "login"))
		        )
			    .addAggregation(
			    		AggregationBuilders.terms("create").field("注册时间").order(Terms.Order.term(false)).size(num)
			    		.subAggregation(AggregationBuilders.cardinality("agg").field("玩家GUID"))
			    		.subAggregation(AggregationBuilders.terms("platForm").field("渠道ID").size(pfsize).subAggregation(AggregationBuilders.cardinality("agg").field("玩家GUID")))
			    ).execute().actionGet();
		logger.debug("------------esPlatForm--------------");

		Terms genders = sr.getAggregations().get("create");	
		for (Terms.Bucket entry : genders.getBuckets()) {
			String dateBucket = sdf.format(new Date((Long) entry.getKeyAsNumber()));
			if(dateBucket.equals(esUtilTest.twoDayAgoFrom())){
				Terms serverZone = entry.getAggregations().get("platForm");
				for (Terms.Bucket e : serverZone.getBuckets()) {
				    Cardinality agg = e.getAggregations().get("agg");
				    Long aggcount = agg.getValue();
				    Double RetentionTwo ;
				    
				    Long r = createPlatFormCount((String) e.getKey(),esUtilTest.twoDayAgoFrom(), esUtilTest.twoDayAgoTo());
				    if(r==0){
					    RetentionTwo = (double) 0.00;
				    }else{
				    	RetentionTwo = (double)aggcount*100/createPlatFormCount((String) e.getKey(),esUtilTest.twoDayAgoFrom(), esUtilTest.twoDayAgoTo());
				    }
				    
					bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_retained)
					        .setSource(jsonBuilder()
						           	 .startObject()
				                        .field("date",esUtilTest.twoDayAgoFrom().split("T")[0])
				                        .field("gameId", game)
				                        .field("ctRetained", UserRetained.CT_NEXTDAY)
				                        .field("retained", df.format(RetentionTwo))
				                        .field("key", UserRetained.KEY_PLATFORM)
				                        .field("value", e.getKey())
				                        .field("@timestamp", new Date())
				                    .endObject()
					                  )
					        );
				}
			}else if(dateBucket.equals(esUtilTest.eightDayAgoFrom())){
				Terms serverZone = entry.getAggregations().get("platForm");
				for (Terms.Bucket e : serverZone.getBuckets()) {
				    Cardinality agg = e.getAggregations().get("agg");
				    Long aggcount = agg.getValue();
				    Double RetentionEight ;
				    RetentionEight = (double)aggcount*100/createPlatFormCount((String) e.getKey(),esUtilTest.eightDayAgoFrom(), esUtilTest.eightDayAgoTo());
					bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_retained)
					        .setSource(jsonBuilder()
						           	 .startObject()
				                        .field("date",esUtilTest.eightDayAgoFrom().split("T")[0])
				                        .field("gameId", game)
				                        .field("ctRetained", UserRetained.CT_SEVENDAY)
				                        .field("retained", df.format(RetentionEight))
				                        .field("key", UserRetained.KEY_PLATFORM)
				                        .field("value", e.getKey())
				                        .field("@timestamp", new Date())
				                    .endObject()
					                  )
					        );
				}
			}else if(dateBucket.equals(esUtilTest.thirtyOneDayAgoFrom())){
				Terms serverZone = entry.getAggregations().get("platForm");
				for (Terms.Bucket e : serverZone.getBuckets()) {
				    Cardinality agg = e.getAggregations().get("agg");
				    Long aggcount = agg.getValue();
				    Double RetentionThirty ;
			    	RetentionThirty = (double)aggcount*100/createPlatFormCount((String) e.getKey(),esUtilTest.thirtyOneDayAgoFrom(), esUtilTest.thirtyOneDayAgoTo());
					bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_retained)
					        .setSource(jsonBuilder()
						           	 .startObject()
				                        .field("date",esUtilTest.thirtyOneDayAgoFrom().split("T")[0])
				                        .field("gameId", game)
				                        .field("ctRetained", UserRetained.CT_THIRYTDAY)
				                        .field("retained", df.format(RetentionThirty))
				                        .field("key", UserRetained.KEY_PLATFORM)
				                        .field("value", e.getKey())
				                        .field("@timestamp", new Date())
				                    .endObject()
					                  )
					        );
				}
			}
		}
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}
	}
	
	public void esServer() throws IOException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		//计算时间（当前）2015-04-15 ，统计出2015-04-14到2015-04-15的数据 ，得出2015-04-13的次日留存、得出2015-04-07的7日留存、得出2015-03-15的30日留存
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数  
		
		SearchResponse sr = client.prepareSearch(index).setTypes(type).setSearchType("count")
				.setQuery(
		        		QueryBuilders.boolQuery()
		        		.must( QueryBuilders.rangeQuery("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()) )
		        		.must( QueryBuilders.termsQuery("日志分类关键字", "login"))
		        )
			    .addAggregation(
			    		AggregationBuilders.terms("create").field("注册时间").order(Terms.Order.term(false)).size(num)
			    		.subAggregation(AggregationBuilders.cardinality("agg").field("玩家GUID"))
			    		.subAggregation(AggregationBuilders.terms("server").field("服务器ID").size(srsize).subAggregation(AggregationBuilders.cardinality("agg").field("玩家GUID")))
			    ).execute().actionGet();
		logger.debug("-----------esServer---------------");

		Terms genders = sr.getAggregations().get("create");	
		for (Terms.Bucket entry : genders.getBuckets()) {
			String dateBucket = sdf.format(new Date((Long) entry.getKeyAsNumber()));
			if(dateBucket.equals(esUtilTest.twoDayAgoFrom())){
				Terms serverZone = entry.getAggregations().get("server");
				for (Terms.Bucket e : serverZone.getBuckets()) {
				    Cardinality agg = e.getAggregations().get("agg");
				    Long aggcount = agg.getValue();
				    Double RetentionTwo ;
				    
				    Long r = createServerCount((String) e.getKey(),esUtilTest.twoDayAgoFrom(), esUtilTest.twoDayAgoTo());
				    if(r==0){
					    RetentionTwo = (double) 0.00;
				    }else{
				    	RetentionTwo = (double)aggcount*100/createServerCount((String) e.getKey(),esUtilTest.twoDayAgoFrom(), esUtilTest.twoDayAgoTo());
				    }
					bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_retained)
					        .setSource(jsonBuilder()
						           	 .startObject()
				                        .field("date",esUtilTest.twoDayAgoFrom().split("T")[0])
				                        .field("gameId", game)
				                        .field("ctRetained", UserRetained.CT_NEXTDAY)
				                        .field("retained", df.format(RetentionTwo))
				                        .field("key", UserRetained.KEY_SEVSER)
				                        .field("value", e.getKey())
				                        .field("@timestamp", new Date())
				                    .endObject()
					                  )
					        );
				}
			}else if(dateBucket.equals(esUtilTest.eightDayAgoFrom())){
				Terms serverZone = entry.getAggregations().get("server");
				for (Terms.Bucket e : serverZone.getBuckets()) {
				    Cardinality agg = e.getAggregations().get("agg");
				    Long aggcount = agg.getValue();
				    Double RetentionEight ;
				    RetentionEight = (double)aggcount*100/createServerCount((String) e.getKey(),esUtilTest.eightDayAgoFrom(), esUtilTest.eightDayAgoTo());
					bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_retained)
					        .setSource(jsonBuilder()
						           	 .startObject()
				                        .field("date",esUtilTest.eightDayAgoFrom().split("T")[0])
				                        .field("gameId", game)
				                        .field("ctRetained", UserRetained.CT_SEVENDAY)
				                        .field("retained", df.format(RetentionEight))
				                        .field("key", UserRetained.KEY_SEVSER)
				                        .field("value", e.getKey())
				                        .field("@timestamp", new Date())
				                    .endObject()
					                  )
					        );
				}
			}else if(dateBucket.equals(esUtilTest.thirtyOneDayAgoFrom())){
				Terms serverZone = entry.getAggregations().get("server");
				for (Terms.Bucket e : serverZone.getBuckets()) {
				    Cardinality agg = e.getAggregations().get("agg");
				    Long aggcount = agg.getValue();
				    Double RetentionThirty ;
				    
			    	RetentionThirty = (double)aggcount*100/createServerCount((String) e.getKey(),esUtilTest.thirtyOneDayAgoFrom(), esUtilTest.thirtyOneDayAgoTo());
					bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_retained)
					        .setSource(jsonBuilder()
						           	 .startObject()
				                        .field("date",esUtilTest.thirtyOneDayAgoFrom().split("T")[0])
				                        .field("gameId", game)
				                        .field("ctRetained", UserRetained.CT_THIRYTDAY)
				                        .field("retained", df.format(RetentionThirty))
				                        .field("key", UserRetained.KEY_SEVSER)
				                        .field("value", e.getKey())
				                        .field("@timestamp", new Date())
				                    .endObject()
					                  )
					        );
				}
			}
		}
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}
	}
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void schedual() throws Exception{
		logger.info("----------------kun retained begin-----------");
		try {
			esAll();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("kun all retained error " + e);
		}
		
		try {
			esServerZone();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("kun serverZone retained error " +e);
		}
		
		try {
			esPlatForm();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("kun platForm retained error "+e);
		}
		
		try {
			esServer();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("kun server retained error "+e);
		}

		logger.info("----------------kun retained end-------------");
	}
	

}
