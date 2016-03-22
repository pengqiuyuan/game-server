package com.enlight.game.es;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

import com.enlight.game.entity.analysis.UserRetained;
import com.enlight.game.service.serverZone.ServerZoneService;

@ContextConfiguration(locations = {"/applicationContext.xml"})
public class EsAllTest extends SpringTransactionalTestCase{
	@Autowired
	private Client client;
	
	@Autowired
	private ServerZoneService serverZoneService;
	
	EsUtilTest esUtilTest = new EsUtilTest();
	
	
	public Long createCount(String from , String to){
		FilteredQueryBuilder builder2 = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
		        FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("@timestamp").from(from).to(to),
		        		FilterBuilders.termFilter("日志分类关键字", "create"))
		        );
		SearchResponse sr = client.prepareSearch().setSearchType("count").setTypes("fb_userlog").setQuery(builder2).execute().actionGet();
		System.out.println(sr);
		Long count = sr.getHits().getTotalHits();
		return count;
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
	                        .field("ts","["+userRetained.getTs()+","+userRetained.getRetained()+"]")
	                    .endObject()
		                  )
		        ).execute().actionGet();
	}
	
	
		@Test
		public void test13() throws IOException {	
			String typeName = "fb_userlog";
			SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'" ); 
			DecimalFormat df = new DecimalFormat("0.00");//格式化小数  
			
			FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
			        FilterBuilders.andFilter(
					        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
			        		FilterBuilders.termFilter("日志分类关键字", "login"))
			        );
			SearchResponse sr = client.prepareSearch().setSearchType("count").setTypes(typeName).setQuery(builder)
				    .addAggregation(
				    		AggregationBuilders.terms("create").field("注册时间").order(Terms.Order.term(false))
				    		.subAggregation(AggregationBuilders.cardinality("agg").field("玩家GUID")).size(35)
				    ).execute().actionGet();
			System.out.println(sr);
			
			Terms genders = sr.getAggregations().get("create");	
			for (Terms.Bucket entry : genders.getBuckets()) {
				String dateBucket = sdf.format(new Date((Long) entry.getKeyAsNumber()));
				if(dateBucket.equals(esUtilTest.twoDayAgoFrom())){
				    Cardinality agg = entry.getAggregations().get("agg");
				    Long aggcount = agg.getValue();
				    Double RetentionTwo ;

				    RetentionTwo = (double)aggcount*100/createCount(esUtilTest.twoDayAgoFrom(), esUtilTest.twoDayAgoTo());
				    UserRetained userRetained = new UserRetained();
				    userRetained.setDate(esUtilTest.twoDayAgoFrom());
				    userRetained.setGameId("fb");
				    userRetained.setKey(UserRetained.KEY_ALL);
				    userRetained.setCtRetained(UserRetained.CT_NEXTDAY);
				    userRetained.setRetained(df.format(RetentionTwo));
				    userRetained.setTs(entry.getKey());
				    bulk(userRetained);

				}else if(dateBucket.equals(esUtilTest.eightDayAgoFrom())){
				    Cardinality agg = entry.getAggregations().get("agg");
				    Long aggcount = agg.getValue();
				    Double RetentionEight ;

					RetentionEight = (double)aggcount*100/createCount(esUtilTest.eightDayAgoFrom(), esUtilTest.eightDayAgoTo());
					UserRetained userRetained = new UserRetained();
				    userRetained.setDate(esUtilTest.eightDayAgoFrom());
				    userRetained.setGameId("fb");
				    userRetained.setKey(UserRetained.KEY_ALL);
				    userRetained.setCtRetained(UserRetained.CT_SEVENDAY);
				    userRetained.setRetained(df.format(RetentionEight));
				    userRetained.setTs(entry.getKey());
				    bulk(userRetained);
				    	
				}else if(dateBucket.equals(esUtilTest.thirtyOneDayAgoFrom())){
				    Cardinality agg = entry.getAggregations().get("agg");
				    Long aggcount = agg.getValue();
				    Double RetentionThirty ;

				    RetentionThirty = (double)aggcount*100/createCount(esUtilTest.thirtyOneDayAgoFrom(), esUtilTest.thirtyOneDayAgoTo());
					UserRetained userRetained = new UserRetained();
				    userRetained.setDate(esUtilTest.thirtyOneDayAgoFrom());
				    userRetained.setGameId("fb");
				    userRetained.setKey(UserRetained.KEY_ALL);
				    userRetained.setCtRetained(UserRetained.CT_THIRYTDAY);
				    userRetained.setRetained(df.format(RetentionThirty));
				    userRetained.setTs(entry.getKey());
				    bulk(userRetained);

				}
			}

		}
	
}
