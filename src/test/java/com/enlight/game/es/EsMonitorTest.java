package com.enlight.game.es;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.codehaus.jackson.type.TypeReference;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

import com.enlight.game.entity.Monitor;
import com.enlight.game.entity.gm.fb.ServerStatus;
import com.enlight.game.service.monitor.MonitorService;
import com.enlight.game.util.EsUtil;
import com.enlight.game.util.HttpClientUts;
import com.enlight.game.util.JsonBinder;
import com.enlight.game.util.email.MimeMailService;
import com.enlight.game.util.email.SimpleMailService;



@ContextConfiguration(locations = {"/applicationContext.xml"})
public class EsMonitorTest extends SpringTransactionalTestCase{
	
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	
	@Autowired
	private SimpleMailService simpleMailService;
	
	@Autowired
	private MimeMailService mimeMailService;
	
	@Autowired
	private MonitorService monitorService;
	
	@Autowired
	private Client client;
	
	private static final String kds_index = "logstash-kds-all-*";
	
	private static final String kds_item_type = "kds_item.log";
	
	private static final String kds_money_type = "kds_money.log";
	
	private static final String kds_coin_type = "kds_coin.log";
	
	EsUtil esUtilTest = new EsUtil();
	
	//@Test
	public void test13() throws IOException, ParseException {	
		System.out.println("00000000   "   + esUtilTest.fiveMinuteAgoFrom()  +  "   "  +  esUtilTest.nowDate1() );
		System.out.println("22222222   "   + esUtilTest.oneDayAgoFrom()  +  "   "  +  esUtilTest.nowDate() );
		
		List<String> sr_hits = new ArrayList<String>();
		List<String> items = new ArrayList<String>();
		List<String> moneys = new ArrayList<String>();
		List<String> coins = new ArrayList<String>();
		List<String> itemIds = new ArrayList<String>();
		
		//游戏币
		SearchResponse sr_coin = client.prepareSearch(kds_index).setTypes(kds_coin_type).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.fiveMinuteAgoFrom()).to(esUtilTest.nowDate1()),
				        FilterBuilders.rangeFilter("增加的游戏币数量").gte("999"))
		        )).execute().actionGet();
		System.out.println("11111   "  + sr_coin);
		sr_hits.add("近5分钟，增加的游戏币数量超过设定阙值999，共出现："+sr_coin.getHits().totalHits()+"条日志");
		for (SearchHit hit : sr_coin.getHits()) {
			coins.add(hit.getSource().toString());
		}
		
		SearchResponse sr_money = client.prepareSearch(kds_index).setTypes(kds_money_type).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.fiveMinuteAgoFrom()).to(esUtilTest.nowDate1()),
				        FilterBuilders.rangeFilter("增加的充值币数量").gte("999"))
		        )).execute().actionGet();
		System.out.println("22222   "  + sr_money);
		sr_hits.add("近5分钟，增加的充值币数量超过设定阙值999，共出现："+sr_money.getHits().totalHits()+"条日志");
		for (SearchHit hit : sr_money.getHits()) {
			moneys.add(hit.getSource().toString());
		}
		
		SearchResponse sr_item = client.prepareSearch(kds_index).setTypes(kds_item_type).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.fiveMinuteAgoFrom()).to(esUtilTest.nowDate1()),
				        FilterBuilders.rangeFilter("增加的道具数量").from("1").includeLower(true))
		        )).execute().actionGet();
		System.out.println("33333   "  + sr_item);
		sr_hits.add("近5分钟，增加的道具数量超过设定阙值1，共出现："+sr_item.getHits().totalHits()+"条日志");
		for (SearchHit hit : sr_item.getHits()) {
			items.add(hit.getSource().toString());
		}
		
		
		List<String> s = new ArrayList<String>();
		s.add("10080");
		s.add("10183");
		s.add("136");
		
		SearchResponse sr_item_id = client.prepareSearch(kds_index).setTypes(kds_item_type).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.fiveMinuteAgoFrom()).to(esUtilTest.nowDate1()),
						FilterBuilders.termsFilter("日志道具id", s))
		        )).execute().actionGet();
		System.out.println("44444   "  + sr_item_id);
		sr_hits.add("近5分钟，出现设定的日志道具id，共："+sr_item_id.getHits().totalHits()+"条日志");
		for (SearchHit hit : sr_item_id.getHits()) {
			itemIds.add(hit.getSource().toString());
		}
		
	}
	
	

	
	@Test
	public void test14() throws IOException, ParseException {	
		
		List<Monitor> monitors = monitorService.findAll();
		for (Monitor monitor : monitors) {
			System.out.println("1111122  " + monitor.getMonitorKey());
		}
		
		
		List<String> sr_hits = new ArrayList<String>();
		List<String> items = new ArrayList<String>();
		List<String> moneys = new ArrayList<String>();
		List<String> coins = new ArrayList<String>();
		List<String> itemIds = new ArrayList<String>();
		
		//游戏币
		SearchResponse sr_coin = client.prepareSearch(kds_index).setTypes(kds_coin_type).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.fiveMinuteAgoFrom()).to(esUtilTest.nowDate1()),
				        FilterBuilders.rangeFilter("增加的游戏币数量").gte("999"))
		        )).execute().actionGet();
		System.out.println("11111   "  + sr_coin);
		sr_hits.add("近5分钟，增加的游戏币数量：超过设定阙值999，共出现："+sr_coin.getHits().totalHits()+"条日志");
		for (SearchHit hit : sr_coin.getHits()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("玩家GUID", hit.getSource().get("玩家GUID"));
			map.put("日期", hit.getSource().get("日期"));
			map.put("增加的游戏币数量", hit.getSource().get("增加的游戏币数量"));
			coins.add(map.toString());
		}
		
		SearchResponse sr_money = client.prepareSearch(kds_index).setTypes(kds_money_type).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.fiveMinuteAgoFrom()).to(esUtilTest.nowDate1()),
				        FilterBuilders.rangeFilter("增加的充值币数量").gte("999"))
		        )).execute().actionGet();
		System.out.println("22222   "  + sr_money);
		sr_hits.add("近5分钟，增加的充值币数量：超过设定阙值999，共出现："+sr_money.getHits().totalHits()+"条日志");
		for (SearchHit hit : sr_money.getHits()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("玩家GUID", hit.getSource().get("玩家GUID"));
			map.put("日期", hit.getSource().get("日期"));
			map.put("增加的充值币数量", hit.getSource().get("增加的充值币数量"));
			moneys.add(map.toString());
		}
		
		SearchResponse sr_item = client.prepareSearch(kds_index).setTypes(kds_item_type).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.fiveMinuteAgoFrom()).to(esUtilTest.nowDate1()),
				        FilterBuilders.rangeFilter("增加的道具数量").from("1").includeLower(true))
		        )).execute().actionGet();
		System.out.println("33333   "  + sr_item);
		sr_hits.add("近5分钟，增加的道具数量：超过设定阙值1，共出现："+sr_item.getHits().totalHits()+"条日志");
		for (SearchHit hit : sr_item.getHits()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("玩家GUID", hit.getSource().get("玩家GUID"));
			map.put("日期", hit.getSource().get("日期"));
			map.put("增加的道具数量", hit.getSource().get("增加的道具数量"));
			items.add(map.toString());
		}
		
		
		List<String> s = new ArrayList<String>();
		s.add("10080");
		s.add("10183");
		s.add("136");
		
		SearchResponse sr_item_id = client.prepareSearch(kds_index).setTypes(kds_item_type).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("日期").from(esUtilTest.fiveMinuteAgoFrom()).to(esUtilTest.nowDate1()),
						FilterBuilders.termsFilter("日志道具id", s))
		        )).execute().actionGet();
		System.out.println("44444   "  + sr_item_id);
		sr_hits.add("近5分钟，日志道具id：出现设定的值，共："+sr_item_id.getHits().totalHits()+"条日志");
		for (SearchHit hit : sr_item_id.getHits()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("玩家GUID", hit.getSource().get("玩家GUID"));
			map.put("日期", hit.getSource().get("日期"));
			map.put("日志道具id", hit.getSource().get("日志道具id"));
			itemIds.add(map.toString());
		}
		
		
		if(sr_coin.getHits().totalHits()!=0 
				||sr_money.getHits().totalHits()!=0
				||sr_item.getHits().totalHits()!=0
				||sr_item_id.getHits().totalHits()!=0){
			mimeMailService.sendNotificationMail(sr_hits,items,moneys,coins,itemIds);
		}else{
			System.out.println("没有匹配值，不发送邮件");
		}
		
		
	}
	
	//@Test
	public void test15() throws IOException, ParseException {	
		List<Monitor> monitors = monitorService.findAll();
		for (Monitor monitor : monitors) {
			System.out.println("1111122  " + monitor.getMonitorKey());
		}
	}
	
}
