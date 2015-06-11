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
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class FbMoneyPayPServer {
	

	@Autowired
	private Client client;
	
	private static final String index = "log_fb_money";
	
	private static final String type_money_arpu_day = "fb_money_arpu_day";
	
	private static final String type_money_arpu_mouth = "fb_money_arpu_mouth";
	
	private static final String type_money_arppu_day = "fb_money_arppu_day";
	
	private static final String type_money_arppu_mouth = "fb_money_arppu_mouth";
	
	
	public Map<String, String> searchAllArpuDay(String dateFrom,String dateTo) throws IOException, ElasticsearchException, ParseException{
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
                FilterBuilders.andFilter(
        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
                		FilterBuilders.termFilter("key", "all"))
                		);
			SearchResponse response = client.prepareSearch(index)
			        .setTypes(type_money_arpu_day)
			        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
			        .setQuery(builder)
			        .addSort("date", SortOrder.ASC)
			        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
			        .execute()
			        .actionGet();		
			return retained(response,dateFrom,dateTo);
	}
	
	public Map<String, String> searchAllArpuMouth(String dateFrom,String dateTo) throws IOException, ElasticsearchException, ParseException{
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
                FilterBuilders.andFilter(
        		        FilterBuilders.rangeFilter("date").from(dateFrom.substring(0,7)).to(dateTo.substring(0,7)),
                		FilterBuilders.termFilter("key", "all"))
                		);
		SearchResponse response = client.prepareSearch(index)
		        .setTypes(type_money_arpu_mouth)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(builder)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();		
		return mouthre(response,dateFrom,dateTo);
	}
	
	public Map<String, String> searchAllArppuDay(String dateFrom,String dateTo) throws IOException, ElasticsearchException, ParseException{
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
                FilterBuilders.andFilter(
        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
                		FilterBuilders.termFilter("key", "all"))
                		);
		SearchResponse response = client.prepareSearch(index)
		        .setTypes(type_money_arppu_day)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(builder)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();		
		return retained(response,dateFrom,dateTo);
	}
	
	public Map<String, String> searchAllArppuMouth(String dateFrom,String dateTo) throws IOException, ElasticsearchException, ParseException{
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
                FilterBuilders.andFilter(
        		        FilterBuilders.rangeFilter("date").from(dateFrom.substring(0,7)).to(dateTo.substring(0,7)),
                		FilterBuilders.termFilter("key", "all"))
                		);
		SearchResponse response = client.prepareSearch(index)
		        .setTypes(type_money_arppu_mouth)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(builder)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();		
		return mouthre(response,dateFrom,dateTo);
	}
	
	
	
	
	
	public Map<String, String> searchServerZoneArpuDay(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
                FilterBuilders.andFilter(
        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
                		FilterBuilders.termFilter("key", "serverZone"),
                		FilterBuilders.termFilter("value", value))
                		);
		SearchResponse response = client.prepareSearch(index)
		        .setTypes(type_money_arpu_day)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(builder)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();		
		return retained(response,dateFrom,dateTo);
	}
	
	public Map<String, String> searchServerZoneArpuMouth(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
                FilterBuilders.andFilter(
        		        FilterBuilders.rangeFilter("date").from(dateFrom.substring(0,7)).to(dateTo.substring(0,7)),
                		FilterBuilders.termFilter("key", "serverZone"),
                		FilterBuilders.termFilter("value", value))
                		);
		SearchResponse response = client.prepareSearch(index)
		        .setTypes(type_money_arpu_mouth)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(builder)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();		
		return mouthre(response,dateFrom,dateTo);
	}
	
	public Map<String, String> searchServerZoneArppuDay(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
                FilterBuilders.andFilter(
        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
                		FilterBuilders.termFilter("key", "serverZone"),
                		FilterBuilders.termFilter("value", value))
                		);
		SearchResponse response = client.prepareSearch(index)
		        .setTypes(type_money_arppu_day)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(builder)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();		
		return retained(response,dateFrom,dateTo);
	}
	
	public Map<String, String> searchServerZoneArppuMouth(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
                FilterBuilders.andFilter(
        		        FilterBuilders.rangeFilter("date").from(dateFrom.substring(0,7)).to(dateTo.substring(0,7)),
                		FilterBuilders.termFilter("key", "serverZone"),
                		FilterBuilders.termFilter("value", value))
                		);
		SearchResponse response = client.prepareSearch(index)
		        .setTypes(type_money_arppu_mouth)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(builder)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();		
		return mouthre(response,dateFrom,dateTo);
	}
	
	
	
	
	public Map<String, String> searchPlatFormArpuDay(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
                FilterBuilders.andFilter(
        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
                		FilterBuilders.termFilter("key", "platForm"),
                		FilterBuilders.termFilter("value", value))
                		);
		SearchResponse response = client.prepareSearch(index)
		        .setTypes(type_money_arpu_day)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(builder)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		
		return retained(response,dateFrom,dateTo);
	}
	
	public Map<String, String> searchPlatFormArpuMouth(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
                FilterBuilders.andFilter(
        		        FilterBuilders.rangeFilter("date").from(dateFrom.substring(0,7)).to(dateTo.substring(0,7)),
                		FilterBuilders.termFilter("key", "platForm"),
                		FilterBuilders.termFilter("value", value))
                		);
		SearchResponse response = client.prepareSearch(index)
		        .setTypes(type_money_arpu_mouth)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(builder)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		
		return mouthre(response,dateFrom,dateTo);
	}
	
	public Map<String, String> searchPlatFormArppuDay(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
                FilterBuilders.andFilter(
        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
                		FilterBuilders.termFilter("key", "platForm"),
                		FilterBuilders.termFilter("value", value))
                		);
		SearchResponse response = client.prepareSearch(index)
		        .setTypes(type_money_arppu_day)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(builder)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		
		return retained(response,dateFrom,dateTo);
	}
	
	public Map<String, String> searchPlatFormArppuMouth(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
                FilterBuilders.andFilter(
        		        FilterBuilders.rangeFilter("date").from(dateFrom.substring(0,7)).to(dateTo.substring(0,7)),
                		FilterBuilders.termFilter("key", "platForm"),
                		FilterBuilders.termFilter("value", value))
                		);
		SearchResponse response = client.prepareSearch(index)
		        .setTypes(type_money_arppu_mouth)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(builder)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		
		return mouthre(response,dateFrom,dateTo);
	}
	
	
	public Map<String, String> searchServerArpuDay(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
                FilterBuilders.andFilter(
        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
                		FilterBuilders.termFilter("key", "server"),
                		FilterBuilders.termFilter("value", value))
                		);
		SearchResponse response = client.prepareSearch(index)
		        .setTypes(type_money_arpu_day)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(builder)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		
		return retained(response,dateFrom,dateTo);
	}
	
	public Map<String, String> searchServerArpuMouth(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
                FilterBuilders.andFilter(
        		        FilterBuilders.rangeFilter("date").from(dateFrom.substring(0,7)).to(dateTo.substring(0,7)),
                		FilterBuilders.termFilter("key", "server"),
                		FilterBuilders.termFilter("value", value))
                		);
		SearchResponse response = client.prepareSearch(index)
		        .setTypes(type_money_arpu_mouth)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(builder)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		
		return mouthre(response,dateFrom,dateTo);
	}
	
	public Map<String, String> searchServerArppuDay(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
                FilterBuilders.andFilter(
        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
                		FilterBuilders.termFilter("key", "server"),
                		FilterBuilders.termFilter("value", value))
                		);
		SearchResponse response = client.prepareSearch(index)
		        .setTypes(type_money_arppu_day)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(builder)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		
		return retained(response,dateFrom,dateTo);
	}
	
	public Map<String, String> searchServerArppuMouth(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
                FilterBuilders.andFilter(
        		        FilterBuilders.rangeFilter("date").from(dateFrom.substring(0,7)).to(dateTo.substring(0,7)),
                		FilterBuilders.termFilter("key", "server"),
                		FilterBuilders.termFilter("value", value))
                		);
		SearchResponse response = client.prepareSearch(index)
		        .setTypes(type_money_arppu_mouth)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(builder)
		        .addSort("date", SortOrder.ASC)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		
		return mouthre(response,dateFrom,dateTo);
	}
	
	public Map<String, String> retained(SearchResponse response,String dateFrom,String dateTo) throws ParseException{
			Map<String, String> map = new LinkedHashMap<String, String>();
			for (SearchHit hit : response.getHits()) {
				Map<String, Object> source = hit.getSource();
				map.put(source.get("date").toString(), source.get("cv").toString());
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
	
	public Map<String, String> mouthre(SearchResponse response,String dateFrom,String dateTo) throws ParseException{
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (SearchHit hit : response.getHits()) {
			Map<String, Object> source = hit.getSource();
			map.put(source.get("date").toString(), source.get("cv").toString());
		}		
		return map;
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
