package com.enlight.game.service.es.fb;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class FbUserPayServer {
	

	@Autowired
	private Client client;
	
	private static final String index_money = "log_fb_money";
	
	private static final String type_add = "fb_money_add";
	
	private static final String type_all = "fb_money_all";
	
	private static final String type_day = "fb_money_day"; //首日付费率
	
	private static final String type_week = "fb_money_week"; //首周付费率
	
	private static final String type_mouth = "fb_money_mouth"; //首月付费率
	
	private static final String key_add = "add";
	
	private static final String key_all = "all";
	
	private static final String key_day = "day";
	
	private static final String key_week = "week";
	
	private static final String key_mouth = "mouth";
	
	private static final String key_day_num = "day_num";
	
	private static final String key_week_num = "week_num";
	
	private static final String key_mouth_num = "mouth_num";
	
	
	//add新增付费用户
	public Map<String, String> searchAllUserAdd(String dateFrom,String dateTo) throws IOException, ElasticsearchException, ParseException{
			SearchResponse response = client.prepareSearch(index_money)
			        .setTypes(type_add)
			        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
			        .setPostFilter(
			                FilterBuilders.andFilter(
			        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
			                		FilterBuilders.termFilter("key", "all"))
			        		)
			        .addSort("date", SortOrder.ASC)
			        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
			        .execute()
			        .actionGet();		
			return retained(response,dateFrom,dateTo,key_add);
	}
	//all累计付费用户
	public Map<String, String> searchAllUserAll(String dateFrom,String dateTo) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_all)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "all"))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();		
		return retained(response,dateFrom,dateTo,key_all);
	}
	
	public Map<String, String> searchAllUserDay(String dateFrom,String dateTo) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_day)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "all"))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();		
		return retained(response,dateFrom,dateTo,key_day);
	}
	
	public Map<String, String> searchAllUserDaynum(String dateFrom,String dateTo) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_day)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "all"))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();		
		return retained(response,dateFrom,dateTo,key_day_num);
	}
	
	public Map<String, String> searchAllUserWeek(String dateFrom,String dateTo) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_week)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "all"))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();		
		return retained(response,dateFrom,dateTo,key_week);
	}
	
	public Map<String, String> searchAllUserWeeknum(String dateFrom,String dateTo) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_week)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "all"))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();		
		return retained(response,dateFrom,dateTo,key_week_num);
	}
	
	public Map<String, String> searchAllUserMouth(String dateFrom,String dateTo) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_mouth)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "all"))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();		
		return retained(response,dateFrom,dateTo,key_mouth);
	}
	
	public Map<String, String> searchAllUserMouthnum(String dateFrom,String dateTo) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_mouth)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "all"))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();		
		return retained(response,dateFrom,dateTo,key_mouth_num);
	}
	
	//-------------------------------------------------
	//serverzone 新增付费用户
	public Map<String, String> searchServerZoneUserAdd(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_add)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "serverZone"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();		
		return retained(response,dateFrom,dateTo,key_add);
	}
	//serverzone 累计付费用户
	public Map<String, String> searchServerZoneUserAll(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_all)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "serverZone"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();		
		return retained(response,dateFrom,dateTo,key_all);
	}
	
	public Map<String, String> searchServerZoneUserDay(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_day)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "serverZone"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();		
		return retained(response,dateFrom,dateTo,key_day);
	}
	
	public Map<String, String> searchServerZoneUserDaynum(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_day)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "serverZone"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();		
		return retained(response,dateFrom,dateTo,key_day_num);
	}
	
	
	public Map<String, String> searchServerZoneUserWeek(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_week)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "serverZone"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();		
		return retained(response,dateFrom,dateTo,key_week);
	}
	
	public Map<String, String> searchServerZoneUserWeeknum(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_week)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "serverZone"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();		
		return retained(response,dateFrom,dateTo,key_week_num);
	}
	
	public Map<String, String> searchServerZoneUserMouth(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_mouth)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "serverZone"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();		
		return retained(response,dateFrom,dateTo,key_mouth);
	}
	
	public Map<String, String> searchServerZoneUserMouthnum(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_mouth)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "serverZone"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();		
		return retained(response,dateFrom,dateTo,key_mouth_num);
	}
	//---------------------------------------
	//platform 新增付费用户
	public Map<String, String> searchPlatFormUserAdd(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_add)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "platForm"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		
		return retained(response,dateFrom,dateTo,key_add);
	}
	//platform 累计付费用户
	public Map<String, String> searchPlatFormUserAll(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_all)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "platForm"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		
		return retained(response,dateFrom,dateTo,key_all);
	}
	
	public Map<String, String> searchPlatFormUserDay(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_day)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "platForm"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		
		return retained(response,dateFrom,dateTo,key_day);
	}
	
	public Map<String, String> searchPlatFormUserDaynum(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_day)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "platForm"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		
		return retained(response,dateFrom,dateTo,key_day_num);
	}
	
	public Map<String, String> searchPlatFormUserWeek(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_week)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "platForm"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		
		return retained(response,dateFrom,dateTo,key_week);
	}
	
	public Map<String, String> searchPlatFormUserWeeknum(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_week)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "platForm"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		
		return retained(response,dateFrom,dateTo,key_week_num);
	}
	
	public Map<String, String> searchPlatFormUserMouth(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_mouth)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "platForm"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		
		return retained(response,dateFrom,dateTo,key_mouth);
	}
	
	public Map<String, String> searchPlatFormUserMouthnum(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_mouth)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "platForm"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		
		return retained(response,dateFrom,dateTo,key_mouth_num);
	}
	
	//------------------------
	//server 新增付费用户
	public Map<String, String> searchServerUserAdd(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_add)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "server"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		
		return retained(response,dateFrom,dateTo,key_add);
	}
	//server 累计付费用户
	public Map<String, String> searchServerUserAll(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_all)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "server"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		
		return retained(response,dateFrom,dateTo,key_all);
	}
	
	public Map<String, String> searchServerUserDay(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_day)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "server"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		
		return retained(response,dateFrom,dateTo,key_day);
	}
	
	public Map<String, String> searchServerUserDaynum(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_day)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "server"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		
		return retained(response,dateFrom,dateTo,key_day_num);
		
	}
	
	public Map<String, String> searchServerUserWeek(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_week)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "server"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		return retained(response,dateFrom,dateTo,key_week);
	}
	
	public Map<String, String> searchServerUserWeeknum(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_week)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "server"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		return retained(response,dateFrom,dateTo,key_week_num);
	}
	
	public Map<String, String> searchServerUserMouth(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_mouth)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "server"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		return retained(response,dateFrom,dateTo,key_mouth_num);
	}
	
	public Map<String, String> searchServerUserMouthnum(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		SearchResponse response = client.prepareSearch(index_money)
		        .setTypes(type_mouth)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "server"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		
		return retained(response,dateFrom,dateTo,key_mouth);
	}
	
	public Map<String, String> retained(SearchResponse response,String dateFrom,String dateTo,String k) throws ParseException{
			Map<String, String> map = new LinkedHashMap<String, String>();
			for (SearchHit hit : response.getHits()) {
				Map<String, Object> source = hit.getSource();
				if(k.equals(key_add)){
					map.put(source.get("date").toString(), source.get("newpayuser").toString());
				}else if(k.equals(key_all)){
					map.put(source.get("date").toString(), source.get("allpayuser").toString());
				}else if(k.equals(key_day) || k.equals(key_week) || k.equals(key_mouth) ){
					map.put(source.get("date").toString(), source.get("payofrate").toString());
				}
			}
			Map<String, String> m = new LinkedHashMap<String, String>();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(dateFrom));
			long startTIme = cal.getTimeInMillis();
			cal.setTime(sdf.parse(dateTo));
			long endTime = cal.getTimeInMillis();

			cal.setTime(new Date());
			cal.add(cal.DATE,-1);
			long t = cal.getTimeInMillis();
			if(endTime>t){
				endTime = t;
			}
			
			Long oneDay = 1000 * 60 * 60 * 24l;
			Long time = startTIme;
			while (time <= endTime) {
				Date d = new Date(time);
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String key = df.format(d);
				if(map.containsKey(key)){
					m.put(key, map.get(key));
				}else{
					m.put(key, "0");
				}
				time += oneDay;
			}
			return m;
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
