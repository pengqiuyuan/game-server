package com.enlight.game.schedual.xyj;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

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
import com.google.common.collect.Maps;

@Transactional(readOnly = true)
public class XyjRetainedScheduled {

	@Autowired
	public Client client;
	
	public static final Logger logger = LoggerFactory.getLogger(XyjRetainedScheduled.class);
	
	//项目名称
	private static final String game = "XYJ";
	
	private static final String index = "logstash-xyj-user-*";
	
	private static final String type = "xyj_userlog";
	
	private static final String bulk_index = "log_xyj_user";
	
	private static final String bulk_type_retained = "xyj_user_retained";
	
	private static final Integer szsize = 10; //运营大区
	
	private static final Integer pfsize = 300; //渠道
	
	private static final Integer srsize = 300; //服务器
	
	private static final Integer num = 35; //35天以内的留存
	
	static EsUtil esUtilTest = new EsUtil();
	
	public Long createCount(String from , String to){
		SearchResponse sr = client.prepareSearch(index).setTypes(type).setSearchType("count")
				.setQuery(
		        		QueryBuilders.boolQuery()
		        		.must( QueryBuilders.rangeQuery("@timestamp").from(esUtilTest.utcMinus8(from)).to(esUtilTest.utcMinus8(to)))
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
		        		.must( QueryBuilders.rangeQuery("@timestamp").from(esUtilTest.utcMinus8(from)).to(esUtilTest.utcMinus8(to)))
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
		        		.must( QueryBuilders.rangeQuery("@timestamp").from(esUtilTest.utcMinus8(from)).to(esUtilTest.utcMinus8(to)))
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
		        		.must( QueryBuilders.rangeQuery("@timestamp").from(esUtilTest.utcMinus8(from)).to(esUtilTest.utcMinus8(to)))
		        		.must( QueryBuilders.termsQuery("日志分类关键字", "create"))
		        		.must( QueryBuilders.termsQuery("服务器ID", key))
		        )
				.execute().actionGet();
		Long count = sr.getHits().getTotalHits();
		return count;
	}


	public void esAll() throws IOException, ParseException {	
		
		Map<String, String> sortTypes = Maps.newLinkedHashMap();
		
		Map<String, String> sortTypes2 = Maps.newLinkedHashMap();

		{
			/*次日留存 这一条忽略*/
			sortTypes.put(esUtilTest.xOneDay(-3), esUtilTest.xOneDay(-2));
			sortTypes.put(esUtilTest.xOneDay(-4), esUtilTest.xOneDay(-3));
			sortTypes.put(esUtilTest.xOneDay(-5), esUtilTest.xOneDay(-4));
			sortTypes.put(esUtilTest.xOneDay(-6), esUtilTest.xOneDay(-5));
			sortTypes.put(esUtilTest.xOneDay(-7), esUtilTest.xOneDay(-6));
			/*7日留存 这一条忽略*/
			sortTypes.put(esUtilTest.xOneDay(-9), esUtilTest.xOneDay(-8));
			sortTypes.put(esUtilTest.xOneDay(-10), esUtilTest.xOneDay(-9));
			sortTypes.put(esUtilTest.xOneDay(-11), esUtilTest.xOneDay(-10));
			sortTypes.put(esUtilTest.xOneDay(-12), esUtilTest.xOneDay(-11));
			sortTypes.put(esUtilTest.xOneDay(-13), esUtilTest.xOneDay(-12));
			sortTypes.put(esUtilTest.xOneDay(-14), esUtilTest.xOneDay(-13));
			sortTypes.put(esUtilTest.xOneDay(-15), esUtilTest.xOneDay(-14));
			sortTypes.put(esUtilTest.xOneDay(-16), esUtilTest.xOneDay(-15));
			sortTypes.put(esUtilTest.xOneDay(-17), esUtilTest.xOneDay(-16));
			sortTypes.put(esUtilTest.xOneDay(-18), esUtilTest.xOneDay(-17));
			sortTypes.put(esUtilTest.xOneDay(-19), esUtilTest.xOneDay(-18));
			sortTypes.put(esUtilTest.xOneDay(-20), esUtilTest.xOneDay(-19));
			sortTypes.put(esUtilTest.xOneDay(-21), esUtilTest.xOneDay(-20));
			sortTypes.put(esUtilTest.xOneDay(-22), esUtilTest.xOneDay(-21));
			sortTypes.put(esUtilTest.xOneDay(-23), esUtilTest.xOneDay(-22));
			sortTypes.put(esUtilTest.xOneDay(-24), esUtilTest.xOneDay(-23));
			sortTypes.put(esUtilTest.xOneDay(-25), esUtilTest.xOneDay(-24));
			sortTypes.put(esUtilTest.xOneDay(-26), esUtilTest.xOneDay(-25));
			sortTypes.put(esUtilTest.xOneDay(-27), esUtilTest.xOneDay(-26));
			sortTypes.put(esUtilTest.xOneDay(-28), esUtilTest.xOneDay(-27));
			sortTypes.put(esUtilTest.xOneDay(-29), esUtilTest.xOneDay(-28));
			sortTypes.put(esUtilTest.xOneDay(-30), esUtilTest.xOneDay(-29));
			/*30日留存 这一条忽略*/
		}
		
		{
			/*次日留存 这一条忽略*/
			sortTypes2.put(esUtilTest.xOneDay(-3),"2Day");
			sortTypes2.put(esUtilTest.xOneDay(-4), "3Day");
			sortTypes2.put(esUtilTest.xOneDay(-5), "4Day");
			sortTypes2.put(esUtilTest.xOneDay(-6), "5Day");
			sortTypes2.put(esUtilTest.xOneDay(-7), "6Day");
			/*7日留存 这一条忽略*/
			sortTypes2.put(esUtilTest.xOneDay(-9), "8Day");
			sortTypes2.put(esUtilTest.xOneDay(-10), "9Day");
			sortTypes2.put(esUtilTest.xOneDay(-11), "10Day");
			sortTypes2.put(esUtilTest.xOneDay(-12), "11Day");
			sortTypes2.put(esUtilTest.xOneDay(-13), "12Day");
			sortTypes2.put(esUtilTest.xOneDay(-14), "13Day");
			sortTypes2.put(esUtilTest.xOneDay(-15), "14Day");
			sortTypes2.put(esUtilTest.xOneDay(-16), "15Day");
			sortTypes2.put(esUtilTest.xOneDay(-17), "16Day");
			sortTypes2.put(esUtilTest.xOneDay(-18), "17Day");
			sortTypes2.put(esUtilTest.xOneDay(-19), "18Day");
			sortTypes2.put(esUtilTest.xOneDay(-20), "19Day");
			sortTypes2.put(esUtilTest.xOneDay(-21), "20Day");
			sortTypes2.put(esUtilTest.xOneDay(-22), "21Day");
			sortTypes2.put(esUtilTest.xOneDay(-23), "22Day");
			sortTypes2.put(esUtilTest.xOneDay(-24), "23Day");
			sortTypes2.put(esUtilTest.xOneDay(-25), "24Day");
			sortTypes2.put(esUtilTest.xOneDay(-26), "25Day");
			sortTypes2.put(esUtilTest.xOneDay(-27), "26Day");
			sortTypes2.put(esUtilTest.xOneDay(-28), "27Day");
			sortTypes2.put(esUtilTest.xOneDay(-29), "28Day");
			sortTypes2.put(esUtilTest.xOneDay(-30), "29Day");
			/*30日留存 这一条忽略*/
		}
		
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数  
		/* game-server 注册时间这个字段传入的是 2016-11-4 为 java.text.ParseException: Unparseable date: "2016-11-4" */
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		/* game-server 注册时间这个字段传入的是 2016-11-4 为 java.text.ParseException: Unparseable date: "2016-11-4" */
		
		SearchResponse sr = client.prepareSearch(index).setSearchType("count").setTypes(type)
				.setQuery(
		        		QueryBuilders.boolQuery()
		        		.must( QueryBuilders.rangeQuery("@timestamp").from(esUtilTest.utcMinus8(esUtilTest.oneDayAgoFrom())).to(esUtilTest.utcMinus8(esUtilTest.nowDate())) )
		        		.must( QueryBuilders.termsQuery("日志分类关键字", "login"))
		        )
			    .addAggregation(
			    		AggregationBuilders.terms("create").field("注册时间").order(Terms.Order.term(false)).size(num)
			    		.subAggregation(AggregationBuilders.cardinality("agg").field("玩家GUID").precisionThreshold(40000))
			    ).execute().actionGet();
		logger.debug("----------------esAll---------------");
		
		Terms genders = sr.getAggregations().get("create");	
		for (Terms.Bucket entry : genders.getBuckets()) {
			/* game-server 注册时间这个字段传入的是 2016-11-4 为 java.text.ParseException: Unparseable date: "2016-11-4" */
			Date date = format.parse(entry.getKeyAsString());
			String dateBucket = sdf.format(date);
			/* game-server 注册时间这个字段传入的是 2016-11-4 为 java.text.ParseException: Unparseable date: "2016-11-4" */
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
			logger.debug("xyj all xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx   " +sortTypes.containsKey(dateBucket)   + dateBucket);
			if(sortTypes.containsKey(dateBucket)){
			    Cardinality agg = entry.getAggregations().get("agg");
			    Long aggcount = agg.getValue();
			    Double x ;
				x = (double)aggcount*100/createCount(dateBucket, sortTypes.get(dateBucket));	
				logger.debug("-----------xyj all 留存---------------"+dateBucket.split("T")[0]+"  "+game+"  "+sortTypes2.get(dateBucket)+"  "+df.format(x)+"  "+UserRetained.KEY_ALL);
				bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_retained)
				        .setSource(jsonBuilder()
					           	 .startObject()
			                        .field("date", dateBucket.split("T")[0])
			                        .field("gameId", game)
			                        .field("ctRetained", sortTypes2.get(dateBucket))
			                        .field("retained", df.format(x))
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
	
	public void esServerZone() throws IOException, ParseException {	
		
		
		Map<String, String> sortTypes = Maps.newLinkedHashMap();
		
		Map<String, String> sortTypes2 = Maps.newLinkedHashMap();

		{
			/*次日留存 这一条忽略*/
			sortTypes.put(esUtilTest.xOneDay(-3), esUtilTest.xOneDay(-2));
			sortTypes.put(esUtilTest.xOneDay(-4), esUtilTest.xOneDay(-3));
			sortTypes.put(esUtilTest.xOneDay(-5), esUtilTest.xOneDay(-4));
			sortTypes.put(esUtilTest.xOneDay(-6), esUtilTest.xOneDay(-5));
			sortTypes.put(esUtilTest.xOneDay(-7), esUtilTest.xOneDay(-6));
			/*7日留存 这一条忽略*/
			sortTypes.put(esUtilTest.xOneDay(-9), esUtilTest.xOneDay(-8));
			sortTypes.put(esUtilTest.xOneDay(-10), esUtilTest.xOneDay(-9));
			sortTypes.put(esUtilTest.xOneDay(-11), esUtilTest.xOneDay(-10));
			sortTypes.put(esUtilTest.xOneDay(-12), esUtilTest.xOneDay(-11));
			sortTypes.put(esUtilTest.xOneDay(-13), esUtilTest.xOneDay(-12));
			sortTypes.put(esUtilTest.xOneDay(-14), esUtilTest.xOneDay(-13));
			sortTypes.put(esUtilTest.xOneDay(-15), esUtilTest.xOneDay(-14));
			sortTypes.put(esUtilTest.xOneDay(-16), esUtilTest.xOneDay(-15));
			sortTypes.put(esUtilTest.xOneDay(-17), esUtilTest.xOneDay(-16));
			sortTypes.put(esUtilTest.xOneDay(-18), esUtilTest.xOneDay(-17));
			sortTypes.put(esUtilTest.xOneDay(-19), esUtilTest.xOneDay(-18));
			sortTypes.put(esUtilTest.xOneDay(-20), esUtilTest.xOneDay(-19));
			sortTypes.put(esUtilTest.xOneDay(-21), esUtilTest.xOneDay(-20));
			sortTypes.put(esUtilTest.xOneDay(-22), esUtilTest.xOneDay(-21));
			sortTypes.put(esUtilTest.xOneDay(-23), esUtilTest.xOneDay(-22));
			sortTypes.put(esUtilTest.xOneDay(-24), esUtilTest.xOneDay(-23));
			sortTypes.put(esUtilTest.xOneDay(-25), esUtilTest.xOneDay(-24));
			sortTypes.put(esUtilTest.xOneDay(-26), esUtilTest.xOneDay(-25));
			sortTypes.put(esUtilTest.xOneDay(-27), esUtilTest.xOneDay(-26));
			sortTypes.put(esUtilTest.xOneDay(-28), esUtilTest.xOneDay(-27));
			sortTypes.put(esUtilTest.xOneDay(-29), esUtilTest.xOneDay(-28));
			sortTypes.put(esUtilTest.xOneDay(-30), esUtilTest.xOneDay(-29));
			/*30日留存 这一条忽略*/
		}
		
		{
			/*次日留存 这一条忽略*/
			sortTypes2.put(esUtilTest.xOneDay(-3),"2Day");
			sortTypes2.put(esUtilTest.xOneDay(-4), "3Day");
			sortTypes2.put(esUtilTest.xOneDay(-5), "4Day");
			sortTypes2.put(esUtilTest.xOneDay(-6), "5Day");
			sortTypes2.put(esUtilTest.xOneDay(-7), "6Day");
			/*7日留存 这一条忽略*/
			sortTypes2.put(esUtilTest.xOneDay(-9), "8Day");
			sortTypes2.put(esUtilTest.xOneDay(-10), "9Day");
			sortTypes2.put(esUtilTest.xOneDay(-11), "10Day");
			sortTypes2.put(esUtilTest.xOneDay(-12), "11Day");
			sortTypes2.put(esUtilTest.xOneDay(-13), "12Day");
			sortTypes2.put(esUtilTest.xOneDay(-14), "13Day");
			sortTypes2.put(esUtilTest.xOneDay(-15), "14Day");
			sortTypes2.put(esUtilTest.xOneDay(-16), "15Day");
			sortTypes2.put(esUtilTest.xOneDay(-17), "16Day");
			sortTypes2.put(esUtilTest.xOneDay(-18), "17Day");
			sortTypes2.put(esUtilTest.xOneDay(-19), "18Day");
			sortTypes2.put(esUtilTest.xOneDay(-20), "19Day");
			sortTypes2.put(esUtilTest.xOneDay(-21), "20Day");
			sortTypes2.put(esUtilTest.xOneDay(-22), "21Day");
			sortTypes2.put(esUtilTest.xOneDay(-23), "22Day");
			sortTypes2.put(esUtilTest.xOneDay(-24), "23Day");
			sortTypes2.put(esUtilTest.xOneDay(-25), "24Day");
			sortTypes2.put(esUtilTest.xOneDay(-26), "25Day");
			sortTypes2.put(esUtilTest.xOneDay(-27), "26Day");
			sortTypes2.put(esUtilTest.xOneDay(-28), "27Day");
			sortTypes2.put(esUtilTest.xOneDay(-29), "28Day");
			sortTypes2.put(esUtilTest.xOneDay(-30), "29Day");
			/*30日留存 这一条忽略*/
		}
		
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		//计算时间（当前）2015-04-15 ，统计出2015-04-14到2015-04-15的数据 ，得出2015-04-13的次日留存、得出2015-04-07的7日留存、得出2015-03-15的30日留存
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数 
		/* game-server 注册时间这个字段传入的是 2016-11-4 为 java.text.ParseException: Unparseable date: "2016-11-4" */
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		/* game-server 注册时间这个字段传入的是 2016-11-4 为 java.text.ParseException: Unparseable date: "2016-11-4" */
		
		SearchResponse sr = client.prepareSearch(index).setTypes(type).setSearchType("count")
				.setQuery(
		        		QueryBuilders.boolQuery()
		        		.must( QueryBuilders.rangeQuery("@timestamp").from(esUtilTest.utcMinus8(esUtilTest.oneDayAgoFrom())).to(esUtilTest.utcMinus8(esUtilTest.nowDate())) )
		        		.must( QueryBuilders.termsQuery("日志分类关键字", "login"))
		        )
			    .addAggregation(
			    		AggregationBuilders.terms("create").field("注册时间").order(Terms.Order.term(false)).size(num)
			    		.subAggregation(
			    				AggregationBuilders.cardinality("agg").field("玩家GUID").precisionThreshold(40000)
			    				)
			    		.subAggregation(
			    				AggregationBuilders.terms("serverZone").field("运营大区ID").size(szsize).subAggregation(
			    						AggregationBuilders.cardinality("agg").field("玩家GUID").precisionThreshold(40000)
			    						)
			    				)
			    ).execute().actionGet();
		logger.debug("-------------esServerZone-------------");

		Terms genders = sr.getAggregations().get("create");	
		for (Terms.Bucket entry : genders.getBuckets()) {
			/* game-server 注册时间这个字段传入的是 2016-11-4 为 java.text.ParseException: Unparseable date: "2016-11-4" */
			Date date = format.parse(entry.getKeyAsString());
			String dateBucket = sdf.format(date);
			/* game-server 注册时间这个字段传入的是 2016-11-4 为 java.text.ParseException: Unparseable date: "2016-11-4" */
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
			logger.debug("xyj serverzone xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx   " +sortTypes.containsKey(dateBucket)   + dateBucket);	
			if(sortTypes.containsKey(dateBucket)){
				Terms serverZone = entry.getAggregations().get("serverZone");
				for (Terms.Bucket e : serverZone.getBuckets()) {
				    Cardinality agg = entry.getAggregations().get("agg");
				    Long aggcount = agg.getValue();
				    Double x ;
					x = (double)aggcount*100/createCount(dateBucket, sortTypes.get(dateBucket));
					logger.debug("-----------xyj serverzone 留存---------------"+dateBucket.split("T")[0]+"  "+game+"  "+sortTypes2.get(dateBucket)+"  "+df.format(x)+"  "+UserRetained.KEY_SEVSERZONE+"  "+e.getKey());
					bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_retained)
					        .setSource(jsonBuilder()
						           	 .startObject()
				                        .field("date", dateBucket.split("T")[0])
				                        .field("gameId", game)
				                        .field("ctRetained", sortTypes2.get(dateBucket))
				                        .field("retained", df.format(x))
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
	
	
	public void esPlatForm() throws IOException, ParseException {	
		
		
		Map<String, String> sortTypes = Maps.newLinkedHashMap();
		
		Map<String, String> sortTypes2 = Maps.newLinkedHashMap();

		{
			/*次日留存 这一条忽略*/
			sortTypes.put(esUtilTest.xOneDay(-3), esUtilTest.xOneDay(-2));
			sortTypes.put(esUtilTest.xOneDay(-4), esUtilTest.xOneDay(-3));
			sortTypes.put(esUtilTest.xOneDay(-5), esUtilTest.xOneDay(-4));
			sortTypes.put(esUtilTest.xOneDay(-6), esUtilTest.xOneDay(-5));
			sortTypes.put(esUtilTest.xOneDay(-7), esUtilTest.xOneDay(-6));
			/*7日留存 这一条忽略*/
			sortTypes.put(esUtilTest.xOneDay(-9), esUtilTest.xOneDay(-8));
			sortTypes.put(esUtilTest.xOneDay(-10), esUtilTest.xOneDay(-9));
			sortTypes.put(esUtilTest.xOneDay(-11), esUtilTest.xOneDay(-10));
			sortTypes.put(esUtilTest.xOneDay(-12), esUtilTest.xOneDay(-11));
			sortTypes.put(esUtilTest.xOneDay(-13), esUtilTest.xOneDay(-12));
			sortTypes.put(esUtilTest.xOneDay(-14), esUtilTest.xOneDay(-13));
			sortTypes.put(esUtilTest.xOneDay(-15), esUtilTest.xOneDay(-14));
			sortTypes.put(esUtilTest.xOneDay(-16), esUtilTest.xOneDay(-15));
			sortTypes.put(esUtilTest.xOneDay(-17), esUtilTest.xOneDay(-16));
			sortTypes.put(esUtilTest.xOneDay(-18), esUtilTest.xOneDay(-17));
			sortTypes.put(esUtilTest.xOneDay(-19), esUtilTest.xOneDay(-18));
			sortTypes.put(esUtilTest.xOneDay(-20), esUtilTest.xOneDay(-19));
			sortTypes.put(esUtilTest.xOneDay(-21), esUtilTest.xOneDay(-20));
			sortTypes.put(esUtilTest.xOneDay(-22), esUtilTest.xOneDay(-21));
			sortTypes.put(esUtilTest.xOneDay(-23), esUtilTest.xOneDay(-22));
			sortTypes.put(esUtilTest.xOneDay(-24), esUtilTest.xOneDay(-23));
			sortTypes.put(esUtilTest.xOneDay(-25), esUtilTest.xOneDay(-24));
			sortTypes.put(esUtilTest.xOneDay(-26), esUtilTest.xOneDay(-25));
			sortTypes.put(esUtilTest.xOneDay(-27), esUtilTest.xOneDay(-26));
			sortTypes.put(esUtilTest.xOneDay(-28), esUtilTest.xOneDay(-27));
			sortTypes.put(esUtilTest.xOneDay(-29), esUtilTest.xOneDay(-28));
			sortTypes.put(esUtilTest.xOneDay(-30), esUtilTest.xOneDay(-29));
			/*30日留存 这一条忽略*/
		}
		
		{
			/*次日留存 这一条忽略*/
			sortTypes2.put(esUtilTest.xOneDay(-3),"2Day");
			sortTypes2.put(esUtilTest.xOneDay(-4), "3Day");
			sortTypes2.put(esUtilTest.xOneDay(-5), "4Day");
			sortTypes2.put(esUtilTest.xOneDay(-6), "5Day");
			sortTypes2.put(esUtilTest.xOneDay(-7), "6Day");
			/*7日留存 这一条忽略*/
			sortTypes2.put(esUtilTest.xOneDay(-9), "8Day");
			sortTypes2.put(esUtilTest.xOneDay(-10), "9Day");
			sortTypes2.put(esUtilTest.xOneDay(-11), "10Day");
			sortTypes2.put(esUtilTest.xOneDay(-12), "11Day");
			sortTypes2.put(esUtilTest.xOneDay(-13), "12Day");
			sortTypes2.put(esUtilTest.xOneDay(-14), "13Day");
			sortTypes2.put(esUtilTest.xOneDay(-15), "14Day");
			sortTypes2.put(esUtilTest.xOneDay(-16), "15Day");
			sortTypes2.put(esUtilTest.xOneDay(-17), "16Day");
			sortTypes2.put(esUtilTest.xOneDay(-18), "17Day");
			sortTypes2.put(esUtilTest.xOneDay(-19), "18Day");
			sortTypes2.put(esUtilTest.xOneDay(-20), "19Day");
			sortTypes2.put(esUtilTest.xOneDay(-21), "20Day");
			sortTypes2.put(esUtilTest.xOneDay(-22), "21Day");
			sortTypes2.put(esUtilTest.xOneDay(-23), "22Day");
			sortTypes2.put(esUtilTest.xOneDay(-24), "23Day");
			sortTypes2.put(esUtilTest.xOneDay(-25), "24Day");
			sortTypes2.put(esUtilTest.xOneDay(-26), "25Day");
			sortTypes2.put(esUtilTest.xOneDay(-27), "26Day");
			sortTypes2.put(esUtilTest.xOneDay(-28), "27Day");
			sortTypes2.put(esUtilTest.xOneDay(-29), "28Day");
			sortTypes2.put(esUtilTest.xOneDay(-30), "29Day");
			/*30日留存 这一条忽略*/
		}
		
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		//计算时间（当前）2015-04-15 ，统计出2015-04-14到2015-04-15的数据 ，得出2015-04-13的次日留存、得出2015-04-07的7日留存、得出2015-03-15的30日留存
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数  
		/* game-server 注册时间这个字段传入的是 2016-11-4 为 java.text.ParseException: Unparseable date: "2016-11-4" */
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		/* game-server 注册时间这个字段传入的是 2016-11-4 为 java.text.ParseException: Unparseable date: "2016-11-4" */
		
		SearchResponse sr = client.prepareSearch(index).setTypes(type).setSearchType("count")
				.setQuery(
		        		QueryBuilders.boolQuery()
		        		.must( QueryBuilders.rangeQuery("@timestamp").from(esUtilTest.utcMinus8(esUtilTest.oneDayAgoFrom())).to(esUtilTest.utcMinus8(esUtilTest.nowDate())) )
		        		.must( QueryBuilders.termsQuery("日志分类关键字", "login"))
		        )
			    .addAggregation(
			    		AggregationBuilders.terms("create").field("注册时间").order(Terms.Order.term(false)).size(num)
			    		.subAggregation(AggregationBuilders.cardinality("agg").field("玩家GUID").precisionThreshold(40000))
			    		.subAggregation(AggregationBuilders.terms("platForm").field("渠道ID").size(pfsize).subAggregation(AggregationBuilders.cardinality("agg").field("玩家GUID").precisionThreshold(40000)))
			    ).execute().actionGet();
		logger.debug("------------esPlatForm--------------");

		Terms genders = sr.getAggregations().get("create");	
		for (Terms.Bucket entry : genders.getBuckets()) {
			/* game-server 注册时间这个字段传入的是 2016-11-4 为 java.text.ParseException: Unparseable date: "2016-11-4" */
			Date date = format.parse(entry.getKeyAsString());
			String dateBucket = sdf.format(date);
			/* game-server 注册时间这个字段传入的是 2016-11-4 为 java.text.ParseException: Unparseable date: "2016-11-4" */
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
			logger.debug("xyj platform xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx   " +sortTypes.containsKey(dateBucket)   + dateBucket);
			if(sortTypes.containsKey(dateBucket)){
				Terms serverZone = entry.getAggregations().get("platForm");
				for (Terms.Bucket e : serverZone.getBuckets()) {
				    Cardinality agg = entry.getAggregations().get("agg");
				    Long aggcount = agg.getValue();
				    Double x ;
					x = (double)aggcount*100/createCount(dateBucket, sortTypes.get(dateBucket));	
					logger.debug("-----------xyj platform 留存---------------"+dateBucket.split("T")[0]+"  "+game+"  "+sortTypes2.get(dateBucket)+"  "+df.format(x)+"  "+UserRetained.KEY_PLATFORM+"  "+e.getKey());
					bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_retained)
					        .setSource(jsonBuilder()
						           	 .startObject()
				                        .field("date",dateBucket.split("T")[0])
				                        .field("gameId", game)
				                        .field("ctRetained", sortTypes2.get(dateBucket))
				                        .field("retained", df.format(x))
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
	
	public void esServer() throws IOException, ParseException {	
		
		
		Map<String, String> sortTypes = Maps.newLinkedHashMap();
		
		Map<String, String> sortTypes2 = Maps.newLinkedHashMap();

		{
			/*次日留存 这一条忽略*/
			sortTypes.put(esUtilTest.xOneDay(-3), esUtilTest.xOneDay(-2));
			sortTypes.put(esUtilTest.xOneDay(-4), esUtilTest.xOneDay(-3));
			sortTypes.put(esUtilTest.xOneDay(-5), esUtilTest.xOneDay(-4));
			sortTypes.put(esUtilTest.xOneDay(-6), esUtilTest.xOneDay(-5));
			sortTypes.put(esUtilTest.xOneDay(-7), esUtilTest.xOneDay(-6));
			/*7日留存 这一条忽略*/
			sortTypes.put(esUtilTest.xOneDay(-9), esUtilTest.xOneDay(-8));
			sortTypes.put(esUtilTest.xOneDay(-10), esUtilTest.xOneDay(-9));
			sortTypes.put(esUtilTest.xOneDay(-11), esUtilTest.xOneDay(-10));
			sortTypes.put(esUtilTest.xOneDay(-12), esUtilTest.xOneDay(-11));
			sortTypes.put(esUtilTest.xOneDay(-13), esUtilTest.xOneDay(-12));
			sortTypes.put(esUtilTest.xOneDay(-14), esUtilTest.xOneDay(-13));
			sortTypes.put(esUtilTest.xOneDay(-15), esUtilTest.xOneDay(-14));
			sortTypes.put(esUtilTest.xOneDay(-16), esUtilTest.xOneDay(-15));
			sortTypes.put(esUtilTest.xOneDay(-17), esUtilTest.xOneDay(-16));
			sortTypes.put(esUtilTest.xOneDay(-18), esUtilTest.xOneDay(-17));
			sortTypes.put(esUtilTest.xOneDay(-19), esUtilTest.xOneDay(-18));
			sortTypes.put(esUtilTest.xOneDay(-20), esUtilTest.xOneDay(-19));
			sortTypes.put(esUtilTest.xOneDay(-21), esUtilTest.xOneDay(-20));
			sortTypes.put(esUtilTest.xOneDay(-22), esUtilTest.xOneDay(-21));
			sortTypes.put(esUtilTest.xOneDay(-23), esUtilTest.xOneDay(-22));
			sortTypes.put(esUtilTest.xOneDay(-24), esUtilTest.xOneDay(-23));
			sortTypes.put(esUtilTest.xOneDay(-25), esUtilTest.xOneDay(-24));
			sortTypes.put(esUtilTest.xOneDay(-26), esUtilTest.xOneDay(-25));
			sortTypes.put(esUtilTest.xOneDay(-27), esUtilTest.xOneDay(-26));
			sortTypes.put(esUtilTest.xOneDay(-28), esUtilTest.xOneDay(-27));
			sortTypes.put(esUtilTest.xOneDay(-29), esUtilTest.xOneDay(-28));
			sortTypes.put(esUtilTest.xOneDay(-30), esUtilTest.xOneDay(-29));
			/*30日留存 这一条忽略*/
		}
		
		{
			/*次日留存 这一条忽略*/
			sortTypes2.put(esUtilTest.xOneDay(-3),"2Day");
			sortTypes2.put(esUtilTest.xOneDay(-4), "3Day");
			sortTypes2.put(esUtilTest.xOneDay(-5), "4Day");
			sortTypes2.put(esUtilTest.xOneDay(-6), "5Day");
			sortTypes2.put(esUtilTest.xOneDay(-7), "6Day");
			/*7日留存 这一条忽略*/
			sortTypes2.put(esUtilTest.xOneDay(-9), "8Day");
			sortTypes2.put(esUtilTest.xOneDay(-10), "9Day");
			sortTypes2.put(esUtilTest.xOneDay(-11), "10Day");
			sortTypes2.put(esUtilTest.xOneDay(-12), "11Day");
			sortTypes2.put(esUtilTest.xOneDay(-13), "12Day");
			sortTypes2.put(esUtilTest.xOneDay(-14), "13Day");
			sortTypes2.put(esUtilTest.xOneDay(-15), "14Day");
			sortTypes2.put(esUtilTest.xOneDay(-16), "15Day");
			sortTypes2.put(esUtilTest.xOneDay(-17), "16Day");
			sortTypes2.put(esUtilTest.xOneDay(-18), "17Day");
			sortTypes2.put(esUtilTest.xOneDay(-19), "18Day");
			sortTypes2.put(esUtilTest.xOneDay(-20), "19Day");
			sortTypes2.put(esUtilTest.xOneDay(-21), "20Day");
			sortTypes2.put(esUtilTest.xOneDay(-22), "21Day");
			sortTypes2.put(esUtilTest.xOneDay(-23), "22Day");
			sortTypes2.put(esUtilTest.xOneDay(-24), "23Day");
			sortTypes2.put(esUtilTest.xOneDay(-25), "24Day");
			sortTypes2.put(esUtilTest.xOneDay(-26), "25Day");
			sortTypes2.put(esUtilTest.xOneDay(-27), "26Day");
			sortTypes2.put(esUtilTest.xOneDay(-28), "27Day");
			sortTypes2.put(esUtilTest.xOneDay(-29), "28Day");
			sortTypes2.put(esUtilTest.xOneDay(-30), "29Day");
			/*30日留存 这一条忽略*/
		}
		
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		//计算时间（当前）2015-04-15 ，统计出2015-04-14到2015-04-15的数据 ，得出2015-04-13的次日留存、得出2015-04-07的7日留存、得出2015-03-15的30日留存
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数  
		/* game-server 注册时间这个字段传入的是 2016-11-4 为 java.text.ParseException: Unparseable date: "2016-11-4" */
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		/* game-server 注册时间这个字段传入的是 2016-11-4 为 java.text.ParseException: Unparseable date: "2016-11-4" */
		
		SearchResponse sr = client.prepareSearch(index).setTypes(type).setSearchType("count")
				.setQuery(
		        		QueryBuilders.boolQuery()
		        		.must( QueryBuilders.rangeQuery("@timestamp").from(esUtilTest.utcMinus8(esUtilTest.oneDayAgoFrom())).to(esUtilTest.utcMinus8(esUtilTest.nowDate())) )
		        		.must( QueryBuilders.termsQuery("日志分类关键字", "login"))
		        )
			    .addAggregation(
			    		AggregationBuilders.terms("create").field("注册时间").order(Terms.Order.term(false)).size(num)
			    		.subAggregation(AggregationBuilders.cardinality("agg").field("玩家GUID").precisionThreshold(40000))
			    		.subAggregation(AggregationBuilders.terms("server").field("服务器ID").size(srsize).subAggregation(AggregationBuilders.cardinality("agg").field("玩家GUID").precisionThreshold(40000)))
			    ).execute().actionGet();
		logger.debug("-----------esServer---------------");

		Terms genders = sr.getAggregations().get("create");	
		for (Terms.Bucket entry : genders.getBuckets()) {
			/* game-server 注册时间这个字段传入的是 2016-11-4 为 java.text.ParseException: Unparseable date: "2016-11-4" */
			Date date = format.parse(entry.getKeyAsString());
			String dateBucket = sdf.format(date);
			/* game-server 注册时间这个字段传入的是 2016-11-4 为 java.text.ParseException: Unparseable date: "2016-11-4" */
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
			logger.debug("xyj server xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx   " +sortTypes.containsKey(dateBucket)   + dateBucket);	
			if(sortTypes.containsKey(dateBucket)){
				Terms serverZone = entry.getAggregations().get("server");
				for (Terms.Bucket e : serverZone.getBuckets()) {
				    Cardinality agg = entry.getAggregations().get("agg");
				    Long aggcount = agg.getValue();
				    Double x ;
					x = (double)aggcount*100/createCount(dateBucket, sortTypes.get(dateBucket));	
					logger.debug("-----------xyj server 留存---------------"+dateBucket.split("T")[0]+"  "+game+"  "+sortTypes2.get(dateBucket)+"  "+df.format(x)+"  "+UserRetained.KEY_SEVSER+"  "+e.getKey());
					bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_retained)
					        .setSource(jsonBuilder()
						           	 .startObject()
				                        .field("date",dateBucket.split("T")[0])
				                        .field("gameId", game)
				                        .field("ctRetained", sortTypes2.get(dateBucket))
				                        .field("retained", df.format(x))
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
		logger.info("----------------xyj retained begin-----------");
		try {
			esAll();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("xyj all retained error " + e);
		}
		
		try {
			esServerZone();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("xyj serverZone retained error " +e);
		}
		
		try {
			esPlatForm();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("xyj platForm retained error "+e);
		}
		
		try {
			esServer();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("xyj server retained error "+e);
		}

		logger.info("----------------xyj retained end-------------");
	}
}
