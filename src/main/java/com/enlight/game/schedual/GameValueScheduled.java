package com.enlight.game.schedual;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enlight.game.entity.Monitor;
import com.enlight.game.service.monitor.MonitorService;
import com.enlight.game.util.EsUtil;
import com.enlight.game.util.email.MimeMailService;
import com.enlight.game.util.email.SimpleMailService;


@Transactional(readOnly = true)
public class GameValueScheduled {
	
	@Autowired
	private SimpleMailService simpleMailService;
	
	@Autowired
	private MimeMailService mimeMailService;
	
	@Autowired
	private MonitorService monitorService;
	
	@Autowired
	private Client client;
	
	/**
	 * 这个controller默认为fb项目的控制层。项目id文档已定
	 */
	private static final String fbStoreId = "1";
	
	private static final String fbStoreName = "FB";
	
	private static final String fb_index = "logstash-fb-all-*";
	
	private static final String fb_item_type = "fb_itemlog";
	
	private static final String fb_money_type = "fb_moneylog";
	
	private static final String fb_coin_type = "fb_coinlog";
	
	/**
	 * 这个controller默认为kun项目的控制层。项目id文档已定
	 */
	private static final String kunStoreId = "2";
	
	private static final String kunStoreName = "KUN";
	
	private static final String kun_index = "logstash-kun-all-*";
	
	private static final String kun_item_type = "kun_itemlog";
	
	private static final String kun_money_type = "kun_moneylog";
	
	private static final String kun_coin_type = "kun_coinlog";
	
	/**
	 * 这个controller默认为kds项目的控制层。项目id文档已定
	 */
	private static final String kdsStoreId = "3";
	
	private static final String kdsStoreName = "KDS";
	
	private static final String kds_index = "logstash-kds-all-*";
	
	private static final String kds_item_type = "kds_itemlog";
	
	private static final String kds_money_type = "kds_moneylog";
	
	private static final String kds_coin_type = "kds_coinlog";
	
	EsUtil esUtilTest = new EsUtil();
	
	private static final Logger logger = LoggerFactory.getLogger(GameValueScheduled.class);
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void schedual() throws Exception{
		//simpleMailService.sendNotificationMail("彭秋源");
		
		//mimeMailService.sendNotificationMail("彭秋源");
		try {
			logger.debug("----------------Monitor调度开始");
			xMonitor(fbStoreId, fbStoreName, fb_index, fb_item_type, fb_money_type, fb_coin_type);
			xMonitor(kunStoreId, kunStoreName, kun_index, kun_item_type, kun_money_type, kun_coin_type);
			xMonitor(kdsStoreId, kdsStoreName, kds_index, kds_item_type, kds_money_type, kds_coin_type);
			logger.debug("----------------Monitor调度结束");
		} catch (Exception e) {
			// TODO: handle exception
			logger.debug("计算失败");
		}
	}
	
	public void xMonitor(String xStoreId,String xStoreName,String x_index,String x_item_type,String x_money_type,String x_coin_type) throws IOException, ParseException {	
		List<String> sr_hits = new ArrayList<String>();
		List<String> items = new ArrayList<String>();
		List<String> moneys = new ArrayList<String>();
		List<String> coins = new ArrayList<String>();
		List<String> itemIds = new ArrayList<String>();
		
		List<Monitor> monitors = monitorService.findAll(xStoreId);
		for (Monitor monitor : monitors) {
			if(monitor.getMonitorKey().equals("增加的游戏币数量")){
				if(monitor.getEql().equals("gte")){
					//游戏币
					SearchResponse sr_coin = client.prepareSearch(x_index).setTypes(x_coin_type)
					        .setQuery(QueryBuilders.matchAllQuery())                 // Query
					        .setPostFilter(
					        		QueryBuilders.boolQuery()
					        		.must( QueryBuilders.rangeQuery("日期").from(esUtilTest.fiveMinuteAgoFrom()).to(esUtilTest.nowDate1()) )
					        		.must( QueryBuilders.rangeQuery("增加的游戏币数量").from(monitor.getMonitorValue()).includeLower(true) )
					        )     // Filter
					        .execute().actionGet();
					sr_hits.add("近5分钟，增加的游戏币数量：超过设定阙值："+monitor.getMonitorValue()+"，共出现："+sr_coin.getHits().totalHits()+"条日志");
					for (SearchHit hit : sr_coin.getHits()) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("玩家GUID", hit.getSource().get("玩家GUID"));
						map.put("日期", hit.getSource().get("日期"));
						map.put("增加的游戏币数量", hit.getSource().get("增加的游戏币数量"));
						map.put("服务器ID", xStoreName+"_s"+hit.getSource().get("服务器ID"));
						coins.add(map.toString());
					}
				}
			}else if(monitor.getMonitorKey().equals("增加的充值币数量")){
				if(monitor.getEql().equals("gte")){
					//充值币
					SearchResponse sr_money = client.prepareSearch(x_index).setTypes(x_money_type)
					        .setQuery(QueryBuilders.matchAllQuery())                 // Query
					        .setPostFilter(
					        		QueryBuilders.boolQuery()
					        		.must( QueryBuilders.rangeQuery("日期").from(esUtilTest.fiveMinuteAgoFrom()).to(esUtilTest.nowDate1()) )
					        		.must( QueryBuilders.rangeQuery("增加的充值币数量").from(monitor.getMonitorValue()).includeLower(true))
					        )     // Filter
					        .execute().actionGet();
					sr_hits.add("近5分钟，增加的充值币数量：超过设定阙值："+monitor.getMonitorValue()+"，共出现："+sr_money.getHits().totalHits()+"条日志");
					for (SearchHit hit : sr_money.getHits()) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("玩家GUID", hit.getSource().get("玩家GUID"));
						map.put("日期", hit.getSource().get("日期"));
						map.put("增加的充值币数量", hit.getSource().get("增加的充值币数量"));
						map.put("服务器ID", xStoreName+"_s"+hit.getSource().get("服务器ID"));
						moneys.add(map.toString());
					}
				}
			}else if(monitor.getMonitorKey().equals("增加的道具数量")){
				if(monitor.getEql().equals("gte")){
					//道具
					SearchResponse sr_item = client.prepareSearch(x_index).setTypes(x_item_type)
					        .setQuery(QueryBuilders.matchAllQuery())                 // Query
					        .setPostFilter(
					        		QueryBuilders.boolQuery()
					        		.must( QueryBuilders.rangeQuery("日期").from(esUtilTest.fiveMinuteAgoFrom()).to(esUtilTest.nowDate1()) )
					        		.must( QueryBuilders.rangeQuery("增加的道具数量").from(monitor.getMonitorValue()).includeLower(true))
					        )     // Filter
					        .execute().actionGet();
					sr_hits.add("近5分钟，增加的道具数量：超过设定阙值："+monitor.getMonitorValue()+"，共出现："+sr_item.getHits().totalHits()+"条日志");
					for (SearchHit hit : sr_item.getHits()) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("玩家GUID", hit.getSource().get("玩家GUID"));
						map.put("日期", hit.getSource().get("日期"));
						map.put("增加的道具数量", hit.getSource().get("增加的道具数量"));
						map.put("服务器ID", xStoreName+"_s"+hit.getSource().get("服务器ID"));
						items.add(map.toString());
					}
				}
			}else if(monitor.getMonitorKey().equals("日志道具id")){
				//道具id
				SearchResponse sr_item_id = client.prepareSearch(x_index).setTypes(x_item_type)
					        .setQuery(QueryBuilders.matchAllQuery())                 // Query
					        .setPostFilter(
					        		QueryBuilders.boolQuery()
					        		.must( QueryBuilders.rangeQuery("日期").from(esUtilTest.fiveMinuteAgoFrom()).to(esUtilTest.nowDate1()) )
					        		.must( QueryBuilders.termsQuery("日志道具id", monitor.getValueList()))
					        )     // Filter				        
				        .execute().actionGet();
				sr_hits.add("近5分钟，日志道具id："+monitor.getMonitorValue()+"，共出现："+sr_item_id.getHits().totalHits()+"条日志");
				for (SearchHit hit : sr_item_id.getHits()) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("玩家GUID", hit.getSource().get("玩家GUID"));
					map.put("日期", hit.getSource().get("日期"));
					map.put("日志道具id", hit.getSource().get("日志道具id"));
					map.put("服务器ID", xStoreName+"_s"+hit.getSource().get("服务器ID"));
					itemIds.add(map.toString());
				}
			}
		}
		
		if(coins.size()!=0 
				||moneys.size()!=0
				||items.size()!=0
				||itemIds.size()!=0){
			mimeMailService.sendNotificationMail(xStoreName,sr_hits,items,moneys,coins,itemIds);
		}else{
			System.out.println(xStoreName+"：没有匹配值，不发送邮件");
		}
	}
}