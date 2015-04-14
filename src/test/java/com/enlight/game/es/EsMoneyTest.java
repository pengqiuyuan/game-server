package com.enlight.game.es;


import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

import com.enlight.game.service.store.StoreService;
import com.enlight.game.util.EsUtil;



@ContextConfiguration(locations = {"/applicationContext.xml"})
public class EsMoneyTest extends SpringTransactionalTestCase{
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
	//@Test
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
	
	//@Test
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
	
	//@Test
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
	
	//@Test
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
	//@Test
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
	
	//@Test
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
	
	//@Test
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
	
	//@Test
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
	//@Test
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
	
	//@Test
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
	
	//@Test
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
	
	//@Test
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//--------------------------
	
	//@Test
	public void te() throws IOException, ParseException {	
		String[] arr = new String[] {"logstash-fb-2015.04", "logstash-fb-2015.04"};
		SearchResponse num = client.prepareSearch("logstash-fb-*").setSearchType("count").setTypes(type).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from("2015-04-12").to("2015-04-13"),
				        FilterBuilders.termFilter("日志分类关键字", "money_get")
						))
		        )		        
		        .addAggregation(
						AggregationBuilders.terms("server").field("服务器ID").size(srsize).subAggregation(
								AggregationBuilders.cardinality("agg").field("玩家GUID")
								)
			    ).execute().actionGet();
		System.out.println(num);
	}
	
	
	//@Test
	public void t() throws IOException, ParseException {	
		String from = "2014-06-14";
		String to = "2015-04-12";
		String[]  arr = getMonthBetween(index,from,to);
		System.out.println(arr);
		for (int i = 0; i < arr.length; i++) {
			System.out.println(arr[i]);
		}
	}
	
	
	private static String[] getMonthBetween(String index,String minDate, String maxDate) throws ParseException {
		minDate = minDate.replace("-", ".");
		maxDate = maxDate.replace("-", ".");
	    ArrayList<String> result = new ArrayList<String>();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM");//格式化为年月

	    Calendar min = Calendar.getInstance();
	    Calendar max = Calendar.getInstance();

	    min.setTime(sdf.parse(minDate));
	    min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

	    max.setTime(sdf.parse(maxDate));
	    max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

	    Calendar curr = min;
	    while (curr.before(max)) {
	     result.add(index+sdf.format(curr.getTime()));
	     curr.add(Calendar.MONTH, 1);
	    }

	    final int size = result.size();
	    String[] arr = (String[])result.toArray(new String[size]);
	    return arr;
	  }
	
	
	@Test
	public void ts() throws IOException, ParseException {	
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
                FilterBuilders.andFilter(
        		        FilterBuilders.rangeFilter("date").from("2015-03-14").to("2015-04-13"),
                		FilterBuilders.termFilter("key", "all"))
                		);
			SearchResponse response = client.prepareSearch("log_fb_money")
			        .setTypes("fb_money_income_sum")
			        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
			        .setQuery(builder)
			        .addSort("date", SortOrder.ASC)
			        .setFrom(0).setSize(daysBetween("2015-03-14","2015-04-13")).setExplain(true)
			        .execute()
			        .actionGet();	
			System.out.println(response);
	}
	
	public static int daysBetween(String smdate, String bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	} 
}
