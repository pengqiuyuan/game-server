package com.enlight.game.es;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

import com.enlight.game.entity.analysis.EsUserTotal;
import com.enlight.game.service.analysis.EsUserTotalService;
import com.enlight.game.util.EsUtil;



@ContextConfiguration(locations = {"/applicationContext.xml"})
public class EsMoneyTest extends SpringTransactionalTestCase{
	@Autowired
	private Client client;
	
	//项目名称
	private static final String game = "KDS";
	
	private static final String index = "logstash-kds-money-*";
	
	private static final String type = "kds_moneylog";
	
	private static final String bulk_index = "log_kds_money";
	
	private static final String bulk_type_money_sum = "kds_money_income_sum";
	
	private static final String bulk_type_money_sum_total = "kds_money_income_sum_total";
	
	private static final String bulk_type_money_count = "kds_money_income_count";
	
	private static final String bulk_type_money_peoplenum = "kds_money_income_peoplenum";
	
	private static final Integer szsize = 10; //运营大区
	
	private static final Integer pfsize = 300; //渠道
	
	private static final Integer srsize = 300; //服务器
	
	private static final String paytype = "1"; //货币类型，人民币1 ，美元2 ，离线部分只统计了1 
	
	EsUtil esUtilTest = new EsUtil();
	
	//收入金额
	//@Test
	public void test1() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		SearchResponse sum = client.prepareSearch(index).setSearchType("count").setTypes(type)
				.setQuery(
						QueryBuilders.boolQuery()
						.must(QueryBuilders.rangeQuery("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()) )
						.must(QueryBuilders.termQuery("日志分类关键字", "money_get"))
						.must(QueryBuilders.termQuery("支付货币", paytype))
		        )
		        .addAggregation(
		        		AggregationBuilders.sum("sum").field("支付金额")
			    ).execute().actionGet();
		System.out.println("1111  " + sum  );
		Sum agg = sum.getAggregations().get("sum");
		System.out.println("2222  " + agg  );
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
		
		//累计收入金额
		SearchResponse sumtotal = client.prepareSearch(index).setSearchType("count").setTypes(type)
				.setQuery(
						QueryBuilders.boolQuery()
						.must(QueryBuilders.rangeQuery("@timestamp").from("2014-01-11").to(esUtilTest.nowDate()) )
						.must(QueryBuilders.termQuery("日志分类关键字", "money_get"))
						.must(QueryBuilders.termQuery("支付货币", paytype))
		        )
		        .addAggregation(
		        		AggregationBuilders.sum("sum").field("支付金额")
			    ).execute().actionGet();
		Sum aggtotal = sumtotal.getAggregations().get("sum");
		
		bulkRequest.add(client.prepareIndex(bulk_index, bulk_type_money_sum_total)
		        .setSource(jsonBuilder()
			           	 .startObject()
	                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
	                        .field("gameId", game)
	                        .field("@timestamp", new Date())
	                        .field("key","all")
	                        .field("ct","sumtotal")
	                        .field("cv",aggtotal.getValue())
	                    .endObject()
		                  )
		        );
		
/*		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}	*/
	}
	

	@Test
	public void test2() throws IOException, ParseException {	
		System.out.println(esUtilTest.oneDayAgoFrom()  +"  "+esUtilTest.nowDate());
		System.out.println(esUtilTest.twoDayAgoFrom()  +"  "+esUtilTest.twoDayAgoTo());
		System.out.println(esUtilTest.eightDayAgoFrom()+"  "+esUtilTest.eightDayAgoTo());
		System.out.println(esUtilTest.thirtyOneDayAgoFrom()+"  "+esUtilTest.thirtyOneDayAgoTo());
		
		System.out.println("------------------------------");
		System.out.println(esUtilTest.twoDayAgoFrom()  +" 次日留存 "+esUtilTest.twoDayAgoTo());
		System.out.println(esUtilTest.xOneDay(-2)  +" 次日.. "+esUtilTest.xOneDay(-1));
		System.out.println(esUtilTest.xOneDay(-3)  +" 2日  "+esUtilTest.xOneDay(-2));
		System.out.println(esUtilTest.xOneDay(-4)  +" 3日  "+esUtilTest.xOneDay(-3));
		System.out.println(esUtilTest.xOneDay(-5)  +" 4日  "+esUtilTest.xOneDay(-4));
		System.out.println(esUtilTest.xOneDay(-6)  +" 5日  "+esUtilTest.xOneDay(-5));
		System.out.println(esUtilTest.xOneDay(-7)  +" 6日  "+esUtilTest.xOneDay(-6));
		System.out.println(esUtilTest.eightDayAgoFrom()+" 七日.. "+esUtilTest.eightDayAgoTo());
		System.out.println(esUtilTest.xOneDay(-8)  +" 7日  "+esUtilTest.xOneDay(-7));
		System.out.println(esUtilTest.xOneDay(-9)  +" 8日  "+esUtilTest.xOneDay(-8));
		System.out.println(esUtilTest.xOneDay(-10)  +" 9日  "+esUtilTest.xOneDay(-9));
		System.out.println(esUtilTest.xOneDay(-11)  +" 10日  "+esUtilTest.xOneDay(-10));
		System.out.println(esUtilTest.xOneDay(-12)  +" 11日  "+esUtilTest.xOneDay(-11));
		System.out.println(esUtilTest.xOneDay(-13)  +" 12日  "+esUtilTest.xOneDay(-12));
		System.out.println(esUtilTest.xOneDay(-14)  +" 13日  "+esUtilTest.xOneDay(-13));
		System.out.println(esUtilTest.xOneDay(-15)  +" 14日  "+esUtilTest.xOneDay(-14));
		System.out.println(esUtilTest.xOneDay(-16)  +" 15日  "+esUtilTest.xOneDay(-15));
		System.out.println(esUtilTest.xOneDay(-17)  +" 16日  "+esUtilTest.xOneDay(-16));
		System.out.println(esUtilTest.xOneDay(-18)  +" 17日  "+esUtilTest.xOneDay(-17));
		System.out.println(esUtilTest.xOneDay(-19)  +" 18日  "+esUtilTest.xOneDay(-18));
		System.out.println(esUtilTest.xOneDay(-20)  +" 19日  "+esUtilTest.xOneDay(-19));
		System.out.println(esUtilTest.xOneDay(-21)  +" 20日  "+esUtilTest.xOneDay(-20));
		System.out.println(esUtilTest.xOneDay(-22)  +" 21日  "+esUtilTest.xOneDay(-21));
		System.out.println(esUtilTest.xOneDay(-23)  +" 22日  "+esUtilTest.xOneDay(-22));
		System.out.println(esUtilTest.xOneDay(-24)  +" 23日  "+esUtilTest.xOneDay(-23));
		System.out.println(esUtilTest.xOneDay(-25)  +" 24日  "+esUtilTest.xOneDay(-24));
		System.out.println(esUtilTest.xOneDay(-26)  +" 25日  "+esUtilTest.xOneDay(-25));
		System.out.println(esUtilTest.xOneDay(-27)  +" 26日  "+esUtilTest.xOneDay(-26));
		System.out.println(esUtilTest.xOneDay(-28)  +" 27日  "+esUtilTest.xOneDay(-27));
		System.out.println(esUtilTest.xOneDay(-29)  +" 28日  "+esUtilTest.xOneDay(-28));
		System.out.println(esUtilTest.xOneDay(-30)  +" 29日  "+esUtilTest.xOneDay(-29));
		System.out.println(esUtilTest.thirtyOneDayAgoFrom()+" 30日.. "+esUtilTest.thirtyOneDayAgoTo());
		System.out.println(esUtilTest.xOneDay(-31)  +" 30日  "+esUtilTest.xOneDay(-30));
	}
	
}
