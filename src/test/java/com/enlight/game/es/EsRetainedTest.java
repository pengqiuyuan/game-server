package com.enlight.game.es;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

import com.enlight.game.entity.analysis.UserRetained;
import com.enlight.game.util.EsUtil;



@ContextConfiguration(locations = {"/applicationContext.xml"})
public class EsRetainedTest extends SpringTransactionalTestCase{
	@Autowired
	private Client client;
	
	//项目名称
	private static final String fb_game = "FB";
	
	private static final String index = "logstash-fb-*";
	
	private static final String type = "fb_user.log";
	
	private static final String bulk_index = "log_fb_user";
	
	private static final String bulk_type_retained = "fb_user_retained";
	
	private static final Integer szsize = 10; //运营大区
	
	private static final Integer pfsize = 300; //渠道
	
	private static final Integer srsize = 300; //服务器
	
	private static final Integer num = 35; //35天以内的留存
	
	EsUtil esUtilTest = new EsUtil();
	
	public Long createCount(String from , String to){
		FilteredQueryBuilder builder2 = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("@timestamp").from(from).to(to),
		        		FilterBuilders.termFilter("日志分类关键字", "create"))
		        );
		SearchResponse sr = client.prepareSearch(index).setTypes(type).setSearchType("count").setQuery(builder2).execute().actionGet();
		Long count = sr.getHits().getTotalHits();
		return count;
	}
	
	@Test
	public void esAll() throws IOException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数  
		
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
		        		FilterBuilders.termFilter("日志分类关键字", "login"))
		        );
		SearchResponse sr = client.prepareSearch(index).setSearchType("count").setTypes(type).setQuery(builder)
			    .addAggregation(
			    		AggregationBuilders.terms("create").field("注册时间").order(Terms.Order.term(false))
			    		.subAggregation(AggregationBuilders.cardinality("agg").field("玩家GUID")).size(num)
			    ).execute().actionGet();
		System.out.println("----------------esAll---------------"+sr);
	    UserRetained userRetained = new UserRetained();
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


			    userRetained.setDate(esUtilTest.twoDayAgoFrom());
			    userRetained.setGameId(fb_game);
			    userRetained.setKey(UserRetained.KEY_ALL);
			    userRetained.setCtRetained(UserRetained.CT_NEXTDAY);
			    userRetained.setRetained(df.format(RetentionTwo));
			    userRetained.setTs(entry.getKey());

			}else if(dateBucket.equals(esUtilTest.eightDayAgoFrom())){
			    Cardinality agg = entry.getAggregations().get("agg");
			    Long aggcount = agg.getValue();
			    Double RetentionEight ;

				RetentionEight = (double)aggcount*100/createCount(esUtilTest.eightDayAgoFrom(), esUtilTest.eightDayAgoTo());
			    userRetained.setDate(esUtilTest.eightDayAgoFrom());
			    userRetained.setGameId(fb_game);
			    userRetained.setKey(UserRetained.KEY_ALL);
			    userRetained.setCtRetained(UserRetained.CT_SEVENDAY);
			    userRetained.setRetained(df.format(RetentionEight));
			    userRetained.setTs(entry.getKey());
			    	
			}else if(dateBucket.equals(esUtilTest.thirtyOneDayAgoFrom())){
			    Cardinality agg = entry.getAggregations().get("agg");
			    Long aggcount = agg.getValue();
			    Double RetentionThirty ;

			    RetentionThirty = (double)aggcount*100/createCount(esUtilTest.thirtyOneDayAgoFrom(), esUtilTest.thirtyOneDayAgoTo());
			    userRetained.setDate(esUtilTest.thirtyOneDayAgoFrom());
			    userRetained.setGameId(fb_game);
			    userRetained.setKey(UserRetained.KEY_ALL);
			    userRetained.setCtRetained(UserRetained.CT_THIRYTDAY);
			    userRetained.setRetained(df.format(RetentionThirty));
			    userRetained.setTs(entry.getKey());


			}
		}

		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_retained)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", userRetained.getDate().split("T")[0])
		                        .field("gameId", userRetained.getGameId())
		                        .field("ctRetained", userRetained.getCtRetained())
		                        .field("retained", userRetained.getRetained())
		                        .field("key", userRetained.getKey())
		                        .field("@timestamp", new Date())
		                    .endObject()
			                  )
			        );
			bulkRequest.execute().actionGet();	
		}else{
			bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_retained)
			        .setSource(
			                  )
			        );
			bulkRequest.execute().actionGet();	
		}
	}	

	
}
