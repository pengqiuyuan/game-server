package com.enlight.game.service.es.fb;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.naming.LinkLoopException;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class EsFbRetainedServer {
	

	@Autowired
	private Client client;
	
	public Map<String, Object> searchAllRetained(String dateFrom,String dateTo) throws IOException, ElasticsearchException, ParseException{

			SearchResponse response = client.prepareSearch("log_fb_retained")
			        .setTypes("fb_retained")
			        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
			        .addSort(SortBuilders.fieldSort("date").order(SortOrder.DESC))
			        .setPostFilter(
			                FilterBuilders.andFilter(
			        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
			                		FilterBuilders.termFilter("key", "all"))
			        		)
			        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
			        .execute()
			        .actionGet();		
			return retained(response);
	}
	
	public Map<String, Object> searchServerZoneRetained(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{

		SearchResponse response = client.prepareSearch("log_fb_retained")
		        .setTypes("fb_retained")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .addSort(SortBuilders.fieldSort("date").order(SortOrder.DESC))
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "serverZone"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();		
		return retained(response);
	}
	public Map<String, Object> searchPlatFormRetained(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{

		SearchResponse response = client.prepareSearch("log_fb_retained")
		        .setTypes("fb_retained")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .addSort(SortBuilders.fieldSort("date").order(SortOrder.DESC))
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "platForm"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		
		return retained(response);
	}
	
	public Map<String, Object> searchServerRetained(String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{

		SearchResponse response = client.prepareSearch("log_fb_retained")
		        .setTypes("fb_retained")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .addSort(SortBuilders.fieldSort("date").order(SortOrder.DESC))
		        .setPostFilter(
		                FilterBuilders.andFilter(
		        		        FilterBuilders.rangeFilter("date").from(dateFrom).to(dateTo),
		                		FilterBuilders.termFilter("key", "server"),
		                		FilterBuilders.termFilter("value", value))
		        		)
		        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
		        .execute()
		        .actionGet();
		
		return retained(response);
	}
	
	public Map<String, Object> retained(SearchResponse response){
			
			Map<String, Object> m = new HashMap<String, Object>();
			LinkedList<String> datenext = new LinkedList<String>();
			LinkedList<String> dateSeven = new LinkedList<String>();
			LinkedList<String> datethirty = new LinkedList<String>();
			HashMap<String, Retained> map = new HashMap<String, EsFbRetainedServer.Retained>();
			
			for (SearchHit hit : response.getHits()) {
				Map<String, Object> source = hit.getSource();
				if(source.get("ctRetained").equals("nextDay")){
					datenext.add(source.get("ts").toString());

					if(map.get(source.get("date").toString())==null){
						Retained retained = new Retained();
						retained.setDate(source.get("date").toString());
						retained.setNextDayRetain(source.get("retained").toString()+"%");
						map.put(source.get("date").toString(), retained);
					}else{
						Retained retained = map.get(source.get("date").toString());
						retained.setNextDayRetain(source.get("retained").toString()+"%");
						map.put(source.get("date").toString(), retained);
					}

				}else if(source.get("ctRetained").equals("sevenDay")){
					dateSeven.add(source.get("ts").toString());
					
					if(map.get(source.get("date").toString())==null){
						Retained retained = new Retained();
						retained.setDate(source.get("date").toString());
						retained.setSevenDayRetain(source.get("retained").toString()+"%");
						map.put(source.get("date").toString(), retained);
					}else{
						Retained retained = map.get(source.get("date").toString());
						retained.setSevenDayRetain(source.get("retained").toString()+"%");
						map.put(source.get("date").toString(), retained);
					}
				}else if(source.get("ctRetained").equals("thirtyDay")){
					datethirty.add(source.get("ts").toString());
					
					if(map.get(source.get("date").toString())==null){
						Retained retained = new Retained();
						retained.setDate(source.get("date").toString());
						retained.setThirtyDayRetain(source.get("retained").toString()+"%");
						map.put(source.get("date").toString(), retained);
					}else{
						Retained retained = map.get(source.get("date").toString());
						retained.setThirtyDayRetain(source.get("retained").toString()+"%");
						map.put(source.get("date").toString(), retained);
					}
				}
			}
			
			m.put("datenext", datenext);
			m.put("dateSeven", dateSeven);
			m.put("datethirty", datethirty);
			m.put("table", map);
			
			return m;
	}
	
		public static int daysBetween(String smdate,String bdate) throws ParseException{  
	        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
	        Calendar cal = Calendar.getInstance();    
	        cal.setTime(sdf.parse(smdate));    
	        long time1 = cal.getTimeInMillis();                 
	        cal.setTime(sdf.parse(bdate));    
	        long time2 = cal.getTimeInMillis();         
	        long between_days=(time2-time1)/(1000*3600*24);  
	            
	        return Integer.parseInt(String.valueOf(between_days));     
	    }  
		
		public class Retained{
			
			private String date ="---";
			
			private String nextDayRetain="---";
			
			private String sevenDayRetain="---";
			
			private String thirtyDayRetain="---";

			public String getDate() {
				return date;
			}

			public void setDate(String date) {
				this.date = date;
			}

			public String getNextDayRetain() {
				return nextDayRetain;
			}

			public void setNextDayRetain(String nextDayRetain) {
				this.nextDayRetain = nextDayRetain;
			}

			public String getSevenDayRetain() {
				return sevenDayRetain;
			}

			public void setSevenDayRetain(String sevenDayRetain) {
				this.sevenDayRetain = sevenDayRetain;
			}

			public String getThirtyDayRetain() {
				return thirtyDayRetain;
			}

			public void setThirtyDayRetain(String thirtyDayRetain) {
				this.thirtyDayRetain = thirtyDayRetain;
			}

		}
		

		
}
