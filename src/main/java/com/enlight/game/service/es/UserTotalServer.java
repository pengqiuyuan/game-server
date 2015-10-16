package com.enlight.game.service.es;

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
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class UserTotalServer {
	

	@Autowired
	private Client client;
	/**
	private static final String index = "log_fb_user";
	
	private static final String type_total = "fb_user_total";
	**/
	public Map<String, String> searchAllUserTotal(String index,String type_total,String dateFrom,String dateTo) throws IOException, ElasticsearchException, ParseException{
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
                FilterBuilders.andFilter(
        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
                		FilterBuilders.termFilter("key", "all"))
                		);
		return esSearch(builder, index, type_total, dateFrom, dateTo);

	}
	
	public Map<String, String> searchServerZoneUserTotal(String index,String type_total,String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
                FilterBuilders.andFilter(
        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
                		FilterBuilders.termFilter("key", "serverZone"),
                		FilterBuilders.termFilter("value", value))
                		);
		return esSearch(builder, index, type_total, dateFrom, dateTo);

	}
	public Map<String, String> searchPlatFormUserTotal(String index,String type_total,String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
                FilterBuilders.andFilter(
        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
                		FilterBuilders.termFilter("key", "platForm"),
                		FilterBuilders.termFilter("value", value))
                		);
		return esSearch(builder, index, type_total, dateFrom, dateTo);
	}
	
	public Map<String, String> searchServerUserTotal(String index,String type_total,String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
                FilterBuilders.andFilter(
        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
                		FilterBuilders.termFilter("key", "server"),
                		FilterBuilders.termFilter("value", value))
                		);
		return esSearch(builder, index, type_total, dateFrom, dateTo);
	}
	
	
	
	public Map<String,String> esSearch(FilteredQueryBuilder builder,String index,String type,String dateFrom,String dateTo) throws IOException, ElasticsearchException, ParseException{
		try {
			SearchResponse response = client.prepareSearch(index)
			        .setTypes(type)
			        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
			        .setQuery(builder)
			        .addSort("date", SortOrder.ASC)
			        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
			        .execute()
			        .actionGet();		
			return retained(response,dateFrom,dateTo);
		} catch (Exception e) {
			// TODO: handle exception
			Map<String, String> map = new LinkedHashMap<String, String>();
			return map;
		}
	}
	
	public Map<String, String> retained(SearchResponse response,String dateFrom,String dateTo) throws ParseException{
			Map<String, String> map = new LinkedHashMap<String, String>();
			for (SearchHit hit : response.getHits()) {
				Map<String, Object> source = hit.getSource();
				map.put(source.get("date").toString(), source.get("userTotal").toString());
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
