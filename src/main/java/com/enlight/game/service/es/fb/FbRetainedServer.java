package com.enlight.game.service.es.fb;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
public class FbRetainedServer {
	

	@Autowired
	private Client client;
	
	public Map<String, Map<String, Object>> searchAllRetained(String dateFrom,String dateTo) throws IOException, ElasticsearchException, ParseException{

			SearchResponse response = client.prepareSearch("log_fb_user")
			        .setTypes("fb_user_retained")
			        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
			        .setPostFilter(
			                FilterBuilders.andFilter(
			        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
			                		FilterBuilders.termFilter("key", "all"))
			        		)
			        .addSort("date", SortOrder.DESC)
			        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
			        .execute()
			        .actionGet();		
			return retained(response,dateFrom,dateTo);
	}
	
	public Map<String, Map<String, Object>> searchServerZoneRetained(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{

		SearchResponse response = client.prepareSearch("log_fb_user")
		        .setTypes("fb_user_retained")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "serverZone"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.DESC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();		
		return retained(response,dateFrom,dateTo);
	}
	public Map<String, Map<String, Object>> searchPlatFormRetained(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{

		SearchResponse response = client.prepareSearch("log_fb_user")
		        .setTypes("fb_user_retained")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "platForm"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.DESC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		
		return retained(response,dateFrom,dateTo);
	}
	
	public Map<String, Map<String, Object>> searchServerRetained(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{

		SearchResponse response = client.prepareSearch("log_fb_user")
		        .setTypes("fb_user_retained")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "server"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .addSort("date", SortOrder.DESC)		
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		
		return retained(response,dateFrom,dateTo);
	}
	
	public Map<String, Map<String, Object>> retained(SearchResponse response,String dateFrom,String dateTo) throws ParseException{
		Map<String, Map<String, Object>> m = new HashMap<String, Map<String, Object>>();

		Map<String, Object> datenext = new HashMap<String, Object>();
		Map<String, Object> dateSeven = new HashMap<String, Object>();
		Map<String, Object> datethirty = new HashMap<String, Object>();

		Map<String, Object> next = new LinkedHashMap<String, Object>();
		Map<String, Object> seven = new LinkedHashMap<String, Object>();
		Map<String, Object> thirty = new LinkedHashMap<String, Object>();
		
		for (SearchHit hit : response.getHits()) {
			Map<String, Object> source = hit.getSource();
			if(source.get("ctRetained").equals("nextDay")){
				datenext.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("sevenDay")){
				dateSeven.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("thirtyDay")){
				datethirty.put(source.get("date").toString(), source.get("retained").toString());
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(dateFrom));
		long startTIme = cal.getTimeInMillis();
		cal.setTime(sdf.parse(dateTo));
		long endTime = cal.getTimeInMillis();

		cal.setTime(new Date());
		cal.add(cal.DATE,-2);
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
			if(datenext.containsKey(key)){
				next.put(key, datenext.get(key));
			}else{
				next.put(key, "0.00");
			}
			if(dateSeven.containsKey(df.format(d))){
				seven.put(key, dateSeven.get(key));
			}else{
				seven.put(key, "0.00");
			}
			if(datethirty.containsKey(df.format(d))){
				thirty.put(key, datethirty.get(key));
			}else{
				thirty.put(key, "0.00");
			}
			time += oneDay;
		}

		m.put("next", next);
		m.put("seven", seven);
		m.put("thirty", thirty);

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
