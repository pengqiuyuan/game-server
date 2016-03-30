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
public class UserActiveServer {
	

	@Autowired
	private Client client;
	/**
	private static final String index = "log_fb_user";
	
	private static final String type_active_day = "fb_user_active_day";
	
	private static final String type_active_week = "fb_user_active_week";
	
	private static final String type_active_mouth = "fb_user_active_mouth";
	**/
	//all 日活
	public Map<String, String> searchAllUserDay(String index ,String type_active_day,String dateFrom,String dateTo) throws IOException, ElasticsearchException, ParseException{

                BoolQueryBuilder builder = QueryBuilders.boolQuery()
        		        .must(QueryBuilders.rangeQuery("date").from(dateFrom).to(dateTo))
                		.must(QueryBuilders.termQuery("key", "all")
                		);
		return esSearch(builder, index, type_active_day, dateFrom, dateTo);
	}
	
	//all 周活
	public Map<String, String> searchAllUserWeek(String index ,String type_active_week,String dateFrom,String dateTo) throws IOException, ElasticsearchException, ParseException{


                BoolQueryBuilder builder = QueryBuilders.boolQuery()
        		        .must(QueryBuilders.rangeQuery("date").from(dateFrom).to(dateTo))
                		.must(QueryBuilders.termQuery("key", "all")
                		);
		return esSearch(builder, index, type_active_week, dateFrom, dateTo);
	}
	
	//all 月活
	public Map<String, String> searchAllUserMouth(String index ,String type_active_mouth,String dateFrom,String dateTo) throws IOException, ElasticsearchException, ParseException{

                BoolQueryBuilder builder = QueryBuilders.boolQuery()
        		        .must(QueryBuilders.rangeQuery("date").from(dateFrom).to(dateTo))
                		.must(QueryBuilders.termQuery("key", "all")
                		);
		return esSearch(builder, index, type_active_mouth, dateFrom, dateTo);
	}
	
	public Map<String, String> searchServerZoneUserDay(String index ,String type_active_day,String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{

                BoolQueryBuilder builder = QueryBuilders.boolQuery()
        		        .must(QueryBuilders.rangeQuery("date").from(dateFrom).to(dateTo))
                		.must(QueryBuilders.termQuery("key", "serverZone"))
                		.must(QueryBuilders.termQuery("value", value)
                		);
		return esSearch(builder, index, type_active_day, dateFrom, dateTo);
	}
	
	public Map<String, String> searchServerZoneUserWeek(String index ,String type_active_week,String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{

                BoolQueryBuilder builder = QueryBuilders.boolQuery()
        		        .must(QueryBuilders.rangeQuery("date").from(dateFrom).to(dateTo))
                		.must(QueryBuilders.termQuery("key", "serverZone"))
                		.must(QueryBuilders.termQuery("value", value)
                		);
		return esSearch(builder, index, type_active_week, dateFrom, dateTo);
	}
	
	public Map<String, String> searchServerZoneUserMouth(String index ,String type_active_mouth,String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{

                BoolQueryBuilder builder = QueryBuilders.boolQuery()
        		        .must(QueryBuilders.rangeQuery("date").from(dateFrom).to(dateTo))
                		.must(QueryBuilders.termQuery("key", "serverZone"))
                		.must(QueryBuilders.termQuery("value", value)
                		);
		return esSearch(builder, index, type_active_mouth, dateFrom, dateTo);
	}
	
	public Map<String, String> searchPlatFormUserDay(String index ,String type_active_day,String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{

                BoolQueryBuilder builder = QueryBuilders.boolQuery()
        		        .must(QueryBuilders.rangeQuery("date").from(dateFrom).to(dateTo))
                		.must(QueryBuilders.termQuery("key", "platForm"))
                		.must(QueryBuilders.termQuery("value", value)
                		);
		return esSearch(builder, index, type_active_day, dateFrom, dateTo);
	}
	
	public Map<String, String> searchPlatFormUserWeek(String index ,String type_active_week,String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{

                BoolQueryBuilder builder = QueryBuilders.boolQuery()
        		        .must(QueryBuilders.rangeQuery("date").from(dateFrom).to(dateTo))
                		.must(QueryBuilders.termQuery("key", "platForm"))
                		.must(QueryBuilders.termQuery("value", value)
                		);
		return esSearch(builder, index, type_active_week, dateFrom, dateTo);
	}
	
	public Map<String, String> searchPlatFormUserMouth(String index ,String type_active_mouth,String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{

                BoolQueryBuilder builder = QueryBuilders.boolQuery()
        		        .must(QueryBuilders.rangeQuery("date").from(dateFrom).to(dateTo))
                		.must(QueryBuilders.termQuery("key", "platForm"))
                		.must(QueryBuilders.termQuery("value", value)
                		);
		return esSearch(builder, index, type_active_mouth, dateFrom, dateTo);
	}
	
	public Map<String, String> searchServerUserDay(String index ,String type_active_day,String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{

                BoolQueryBuilder builder = QueryBuilders.boolQuery()
        		        .must(QueryBuilders.rangeQuery("date").from(dateFrom).to(dateTo))
                		.must(QueryBuilders.termQuery("key", "server"))
                		.must(QueryBuilders.termQuery("value", value)
                		);
		return esSearch(builder, index, type_active_day, dateFrom, dateTo);
	}
	
	public Map<String, String> searchServerUserWeek(String index ,String type_active_week,String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{

                BoolQueryBuilder builder = QueryBuilders.boolQuery()
        		        .must(QueryBuilders.rangeQuery("date").from(dateFrom).to(dateTo))
                		.must(QueryBuilders.termQuery("key", "server"))
                		.must(QueryBuilders.termQuery("value", value)
                		);
		return esSearch(builder, index, type_active_week, dateFrom, dateTo);
	}
	
	public Map<String, String> searchServerUserMouth(String index ,String type_active_mouth,String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{

                BoolQueryBuilder builder = QueryBuilders.boolQuery()
        		        .must(QueryBuilders.rangeQuery("date").from(dateFrom).to(dateTo))
                		.must(QueryBuilders.termQuery("key", "server"))
                		.must(QueryBuilders.termQuery("value", value)
                		);
		return esSearch(builder, index, type_active_mouth, dateFrom, dateTo);
	}
	
	public Map<String,String> esSearch(BoolQueryBuilder builder,String index,String type,String dateFrom,String dateTo) throws IOException, ElasticsearchException, ParseException{
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
				Map<String, String> map = new LinkedHashMap<String, String>();
				return map;
			}
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
