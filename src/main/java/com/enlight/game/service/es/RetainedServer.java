package com.enlight.game.service.es;

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
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class RetainedServer {
	

	@Autowired
	private Client client;
	/**
	private static final String index = "log_fb_user";
	
	private static final String type = "fb_user_retained";
	**/
	public Map<String, Map<String, Object>> searchAllRetained(String index ,String type,String dateFrom,String dateTo) throws IOException, ElasticsearchException, ParseException{
		BoolQueryBuilder builder = QueryBuilders.boolQuery()
        		        .must(QueryBuilders.rangeQuery("date").from(dateFrom).to(dateTo))
                		.must(QueryBuilders.termQuery("key", "all"));
		return esSearch(builder, index, type, dateFrom, dateTo);
	}
	
	public Map<String, Map<String, Object>> searchServerZoneRetained(String index ,String type,String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		BoolQueryBuilder builder = QueryBuilders.boolQuery()
        		        .must(QueryBuilders.rangeQuery("date").from(dateFrom).to(dateTo))
                		.must(QueryBuilders.termQuery("key", "serverZone"))
                		.must(QueryBuilders.termQuery("value", value));
		return esSearch(builder, index, type, dateFrom, dateTo);
	}
	public Map<String, Map<String, Object>> searchPlatFormRetained(String index ,String type,String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		BoolQueryBuilder builder = QueryBuilders.boolQuery()
        		        .must(QueryBuilders.rangeQuery("date").from(dateFrom).to(dateTo))
                		.must(QueryBuilders.termQuery("key", "platForm"))
                		.must(QueryBuilders.termQuery("value", value));
		return esSearch(builder, index, type, dateFrom, dateTo);
	}
	
	public Map<String, Map<String, Object>> searchServerRetained(String index ,String type,String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		BoolQueryBuilder builder = QueryBuilders.boolQuery()
        		        .must(QueryBuilders.rangeQuery("date").from(dateFrom).to(dateTo))
                		.must(QueryBuilders.termQuery("key", "server"))
                		.must(QueryBuilders.termQuery("value", value)
                		);
		return esSearch(builder, index, type, dateFrom, dateTo);
	}
	
	public Map<String, Map<String, Object>> esSearch(BoolQueryBuilder builder,String index,String type,String dateFrom,String dateTo) throws IOException, ElasticsearchException, ParseException{
		try {
			TypesExistsResponse typeEx = client.admin().indices() .prepareTypesExists(index).setTypes(type).execute().actionGet(); 
			if( typeEx.isExists() == true){
				SearchResponse response = client.prepareSearch(index)
				        .setTypes(type)
				        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				        .setQuery(builder)
				        .addSort("date", SortOrder.DESC)		
				        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)).setExplain(true)
				        .execute()
				        .actionGet();
				return retained(response,dateFrom,dateTo);
			}else{
				Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
				return map;
			}
		} catch (Exception e) {
			// TODO: handle exception
			Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
			return map;
		}
	}
	
	
	public Map<String, Map<String, Object>> retained(SearchResponse response,String dateFrom,String dateTo) throws ParseException{
		Map<String, Map<String, Object>> m = new HashMap<String, Map<String, Object>>();

		Map<String, Object> datenext = new HashMap<String, Object>();
		Map<String, Object> dateSeven = new HashMap<String, Object>();
		Map<String, Object> datethirty = new HashMap<String, Object>();
		Map<String, Object> date2 = new HashMap<String, Object>();
		Map<String, Object> date3 = new HashMap<String, Object>();
		Map<String, Object> date4 = new HashMap<String, Object>();
		Map<String, Object> date5 = new HashMap<String, Object>();
		Map<String, Object> date6 = new HashMap<String, Object>();
		Map<String, Object> date8 = new HashMap<String, Object>();
		Map<String, Object> date9 = new HashMap<String, Object>();
		Map<String, Object> date10 = new HashMap<String, Object>();
		Map<String, Object> date11 = new HashMap<String, Object>();
		Map<String, Object> date12 = new HashMap<String, Object>();
		Map<String, Object> date13 = new HashMap<String, Object>();
		Map<String, Object> date14 = new HashMap<String, Object>();
		Map<String, Object> date15 = new HashMap<String, Object>();
		Map<String, Object> date16 = new HashMap<String, Object>();
		Map<String, Object> date17 = new HashMap<String, Object>();
		Map<String, Object> date18 = new HashMap<String, Object>();
		Map<String, Object> date19 = new HashMap<String, Object>();
		Map<String, Object> date20 = new HashMap<String, Object>();
		Map<String, Object> date21 = new HashMap<String, Object>();
		Map<String, Object> date22 = new HashMap<String, Object>();
		Map<String, Object> date23 = new HashMap<String, Object>();
		Map<String, Object> date24 = new HashMap<String, Object>();
		Map<String, Object> date25 = new HashMap<String, Object>();
		Map<String, Object> date26 = new HashMap<String, Object>();
		Map<String, Object> date27 = new HashMap<String, Object>();
		Map<String, Object> date28 = new HashMap<String, Object>();
		Map<String, Object> date29 = new HashMap<String, Object>();

		Map<String, Object> next = new LinkedHashMap<String, Object>();
		Map<String, Object> seven = new LinkedHashMap<String, Object>();
		Map<String, Object> thirty = new LinkedHashMap<String, Object>();
		Map<String, Object> d2 = new LinkedHashMap<String, Object>();
		Map<String, Object> d3 = new LinkedHashMap<String, Object>();
		Map<String, Object> d4 = new LinkedHashMap<String, Object>();
		Map<String, Object> d5 = new LinkedHashMap<String, Object>();
		Map<String, Object> d6 = new LinkedHashMap<String, Object>();
		Map<String, Object> d8 = new LinkedHashMap<String, Object>();
		Map<String, Object> d9 = new LinkedHashMap<String, Object>();
		Map<String, Object> d10 = new LinkedHashMap<String, Object>();
		Map<String, Object> d11 = new LinkedHashMap<String, Object>();
		Map<String, Object> d12 = new LinkedHashMap<String, Object>();
		Map<String, Object> d13 = new LinkedHashMap<String, Object>();
		Map<String, Object> d14 = new LinkedHashMap<String, Object>();
		Map<String, Object> d15 = new LinkedHashMap<String, Object>();
		Map<String, Object> d16 = new LinkedHashMap<String, Object>();
		Map<String, Object> d17 = new LinkedHashMap<String, Object>();
		Map<String, Object> d18 = new LinkedHashMap<String, Object>();
		Map<String, Object> d19 = new LinkedHashMap<String, Object>();
		Map<String, Object> d20 = new LinkedHashMap<String, Object>();
		Map<String, Object> d21 = new LinkedHashMap<String, Object>();
		Map<String, Object> d22 = new LinkedHashMap<String, Object>();
		Map<String, Object> d23 = new LinkedHashMap<String, Object>();
		Map<String, Object> d24 = new LinkedHashMap<String, Object>();
		Map<String, Object> d25 = new LinkedHashMap<String, Object>();
		Map<String, Object> d26 = new LinkedHashMap<String, Object>();
		Map<String, Object> d27 = new LinkedHashMap<String, Object>();
		Map<String, Object> d28 = new LinkedHashMap<String, Object>();
		Map<String, Object> d29 = new LinkedHashMap<String, Object>();
		
		for (SearchHit hit : response.getHits()) {
			Map<String, Object> source = hit.getSource();
			if(source.get("ctRetained").equals("nextDay")){
				datenext.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("sevenDay")){
				dateSeven.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("thirtyDay")){
				datethirty.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("2Day")){
				date2.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("3Day")){
				date3.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("4Day")){
				date4.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("5Day")){
				date5.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("6Day")){
				date6.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("8Day")){
				date8.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("9Day")){
				date9.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("10Day")){
				date10.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("11Day")){
				date11.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("12Day")){
				date12.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("13Day")){
				date13.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("14Day")){
				date14.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("15Day")){
				date15.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("16Day")){
				date16.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("17Day")){
				date17.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("18Day")){
				date18.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("19Day")){
				date19.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("20Day")){
				date20.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("21Day")){
				date21.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("22Day")){
				date22.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("23Day")){
				date23.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("24Day")){
				date24.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("25Day")){
				date25.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("26Day")){
				date26.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("27Day")){
				date27.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("28Day")){
				date28.put(source.get("date").toString(), source.get("retained").toString());
			}else if(source.get("ctRetained").equals("29Day")){
				date29.put(source.get("date").toString(), source.get("retained").toString());
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
			if(date2.containsKey(df.format(d))){
				d2.put(key, date2.get(key));
			}else{
				d2.put(key, "0.00");
			}
			if(date3.containsKey(df.format(d))){
				d3.put(key, date3.get(key));
			}else{
				d3.put(key, "0.00");
			}
			if(date4.containsKey(df.format(d))){
				d4.put(key, date4.get(key));
			}else{
				d4.put(key, "0.00");
			}
			if(date5.containsKey(df.format(d))){
				d5.put(key, date5.get(key));
			}else{
				d5.put(key, "0.00");
			}
			if(date6.containsKey(df.format(d))){
				d6.put(key, date6.get(key));
			}else{
				d6.put(key, "0.00");
			}
			if(date8.containsKey(df.format(d))){
				d8.put(key, date8.get(key));
			}else{
				d8.put(key, "0.00");
			}
			if(date9.containsKey(df.format(d))){
				d9.put(key, date9.get(key));
			}else{
				d9.put(key, "0.00");
			}
			if(date10.containsKey(df.format(d))){
				d10.put(key, date10.get(key));
			}else{
				d10.put(key, "0.00");
			}
			if(date11.containsKey(df.format(d))){
				d11.put(key, date11.get(key));
			}else{
				d11.put(key, "0.00");
			}			
			if(date12.containsKey(df.format(d))){
				d12.put(key, date12.get(key));
			}else{
				d12.put(key, "0.00");
			}
			if(date13.containsKey(df.format(d))){
				d13.put(key, date13.get(key));
			}else{
				d13.put(key, "0.00");
			}
			if(date14.containsKey(df.format(d))){
				d14.put(key, date14.get(key));
			}else{
				d14.put(key, "0.00");
			}
			if(date15.containsKey(df.format(d))){
				d15.put(key, date15.get(key));
			}else{
				d15.put(key, "0.00");
			}
			if(date16.containsKey(df.format(d))){
				d16.put(key, date16.get(key));
			}else{
				d16.put(key, "0.00");
			}
			if(date17.containsKey(df.format(d))){
				d17.put(key, date17.get(key));
			}else{
				d17.put(key, "0.00");
			}
			if(date18.containsKey(df.format(d))){
				d18.put(key, date18.get(key));
			}else{
				d18.put(key, "0.00");
			}
			if(date19.containsKey(df.format(d))){
				d19.put(key, date19.get(key));
			}else{
				d19.put(key, "0.00");
			}
			if(date20.containsKey(df.format(d))){
				d20.put(key, date20.get(key));
			}else{
				d20.put(key, "0.00");
			}
			if(date21.containsKey(df.format(d))){
				d21.put(key, date21.get(key));
			}else{
				d21.put(key, "0.00");
			}
			if(date22.containsKey(df.format(d))){
				d22.put(key, date22.get(key));
			}else{
				d22.put(key, "0.00");
			}
			if(date23.containsKey(df.format(d))){
				d23.put(key, date23.get(key));
			}else{
				d23.put(key, "0.00");
			}
			if(date24.containsKey(df.format(d))){
				d24.put(key, date24.get(key));
			}else{
				d24.put(key, "0.00");
			}
			if(date25.containsKey(df.format(d))){
				d25.put(key, date25.get(key));
			}else{
				d25.put(key, "0.00");
			}
			if(date26.containsKey(df.format(d))){
				d26.put(key, date26.get(key));
			}else{
				d26.put(key, "0.00");
			}
			if(date27.containsKey(df.format(d))){
				d27.put(key, date27.get(key));
			}else{
				d27.put(key, "0.00");
			}
			if(date28.containsKey(df.format(d))){
				d28.put(key, date28.get(key));
			}else{
				d28.put(key, "0.00");
			}
			if(date29.containsKey(df.format(d))){
				d29.put(key, date29.get(key));
			}else{
				d29.put(key, "0.00");
			}
			time += oneDay;
		}

		m.put("next", next);
		m.put("seven", seven);
		m.put("thirty", thirty);
		m.put("d2", d2);
		m.put("d3", d3);
		m.put("d4", d4);
		m.put("d5", d5);
		m.put("d6", d6);
		m.put("d8", d8);
		m.put("d9", d9);
		m.put("d10", d10);
		m.put("d11", d11);
		m.put("d12", d12);
		m.put("d13", d13);
		m.put("d14", d14);
		m.put("d15", d15);
		m.put("d16", d16);
		m.put("d17", d17);
		m.put("d18", d18);
		m.put("d19", d19);
		m.put("d20", d20);
		m.put("d21", d21);
		m.put("d22", d22);
		m.put("d23", d23);
		m.put("d24", d24);
		m.put("d25", d25);
		m.put("d26", d26);
		m.put("d27", d27);
		m.put("d28", d28);
		m.put("d29", d29);
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
