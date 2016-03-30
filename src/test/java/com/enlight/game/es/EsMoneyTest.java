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
	@Test
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
	

	
}
