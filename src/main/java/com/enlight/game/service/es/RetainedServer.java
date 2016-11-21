package com.enlight.game.service.es;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
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

import com.enlight.game.entity.gm.Retained1;
import com.enlight.game.entity.gm.Retained2;
import com.enlight.game.entity.gm.Retained3;

@Component
@Transactional
public class RetainedServer {
	

	@Autowired
	private Client client;

	public Map<String, Object> searchAllRetained(String index ,String type,String dateFrom,String dateTo) throws IOException, ElasticsearchException, ParseException{
		BoolQueryBuilder builder = QueryBuilders.boolQuery()
        		        .must(QueryBuilders.rangeQuery("date").from(dateFrom).to(dateTo))
                		.must(QueryBuilders.termQuery("key", "all"));
		return esSearch(builder, index, type, dateFrom, dateTo);
	}
	
	public Map<String, Object> searchServerZoneRetained(String index ,String type,String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		BoolQueryBuilder builder = QueryBuilders.boolQuery()
        		        .must(QueryBuilders.rangeQuery("date").from(dateFrom).to(dateTo))
                		.must(QueryBuilders.termQuery("key", "serverZone"))
                		.must(QueryBuilders.termQuery("value", value));
		return esSearch(builder, index, type, dateFrom, dateTo);
	}
	public Map<String, Object> searchPlatFormRetained(String index ,String type,String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		BoolQueryBuilder builder = QueryBuilders.boolQuery()
        		        .must(QueryBuilders.rangeQuery("date").from(dateFrom).to(dateTo))
                		.must(QueryBuilders.termQuery("key", "platForm"))
                		.must(QueryBuilders.termQuery("value", value));
		return esSearch(builder, index, type, dateFrom, dateTo);
	}
	
	public Map<String, Object> searchServerRetained(String index ,String type,String dateFrom,String dateTo,String value) throws IOException, ElasticsearchException, ParseException{
		BoolQueryBuilder builder = QueryBuilders.boolQuery()
        		        .must(QueryBuilders.rangeQuery("date").from(dateFrom).to(dateTo))
                		.must(QueryBuilders.termQuery("key", "server"))
                		.must(QueryBuilders.termQuery("value", value)
                		);
		return esSearch(builder, index, type, dateFrom, dateTo);
	}
	
	public Map<String,Object> esSearch(BoolQueryBuilder builder,String index,String type,String dateFrom,String dateTo) throws IOException, ElasticsearchException, ParseException{
		try {
			TypesExistsResponse typeEx = client.admin().indices() .prepareTypesExists(index).setTypes(type).execute().actionGet(); 
			if( typeEx.isExists() == true){
				SearchResponse response = client.prepareSearch(index)
				        .setTypes(type)
				        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				        .setQuery(builder)
				        .addSort("date", SortOrder.DESC)		
				        .setFrom(0).setSize(daysBetween(dateFrom,dateTo)*32).setExplain(true)
				        .execute()
				        .actionGet();
				return retained(response,dateFrom,dateTo);
			}else{
				Map<String, Object> map = new HashMap<String, Object>();
				return map;
			}
		} catch (Exception e) {
			// TODO: handle exception
			Map<String, Object> map = new HashMap<String, Object>();
			return map;
		}
	}
	
	
	public Map<String, Object> retained(SearchResponse response,String dateFrom,String dateTo) throws ParseException{
		Map<String, Object> m = new HashMap<String, Object>();
		List<Retained1> retained1s = new LinkedList<Retained1>();
		List<Retained2> retained2s = new LinkedList<Retained2>();
		List<Retained3> retained3s = new LinkedList<Retained3>();

		Map<String, Object> date2= new HashMap<String, Object>();
		Map<String, Object> date3 = new HashMap<String, Object>();
		Map<String, Object> date4 = new HashMap<String, Object>();
		Map<String, Object> date5 = new HashMap<String, Object>();
		Map<String, Object> date6 = new HashMap<String, Object>();
		Map<String, Object> date7 = new HashMap<String, Object>();
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
		Map<String, Object> date30 = new HashMap<String, Object>();
		Map<String, Object> date31 = new HashMap<String, Object>();
		
		for (SearchHit hit : response.getHits()) {
			Map<String, Object> source = hit.getSource();
			if(source.get("ctRetained").equals("2Day")){
				date2.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("3Day")){
				date3.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("4Day")){
				date4.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("5Day")){
				date5.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("6Day")){
				date6.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("7Day")){
				date7.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("8Day")){
				date8.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("9Day")){
				date9.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("10Day")){
				date10.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("11Day")){
				date11.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("12Day")){
				date12.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("13Day")){
				date13.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("14Day")){
				date14.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("15Day")){
				date15.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("16Day")){
				date16.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("17Day")){
				date17.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("18Day")){
				date18.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("19Day")){
				date19.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("20Day")){
				date20.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("21Day")){
				date21.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("22Day")){
				date22.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("23Day")){
				date23.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("24Day")){
				date24.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("25Day")){
				date25.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("26Day")){
				date26.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("27Day")){
				date27.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("28Day")){
				date28.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("29Day")){
				date29.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("30Day")){
				date30.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("31Day")){
				date31.put(source.get("date").toString(), source.get("retained").toString()+"（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
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
			Retained1 retained1 = new Retained1();
			Retained2 retained2 = new Retained2();
			Retained3 retained3 = new Retained3();
			Date d = new Date(time);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String key = df.format(d);
			retained1.setxDate(key);
			retained2.setxDate(key);
			retained3.setxDate(key);
			if(date2.containsKey(df.format(d))){
				retained1.setDay2(date2.get(key).toString());
			}else{
				retained1.setDay2("");
			}
			if(date3.containsKey(df.format(d))){
				retained1.setDay3(date3.get(key).toString());
			}else{
				retained1.setDay3("");
			}
			if(date4.containsKey(df.format(d))){
				retained1.setDay4(date4.get(key).toString());
			}else{
				retained1.setDay4("");
			}
			if(date5.containsKey(df.format(d))){
				retained1.setDay5(date5.get(key).toString());
			}else{
				retained1.setDay5("");
			}
			if(date6.containsKey(df.format(d))){
				retained1.setDay6(date6.get(key).toString());
			}else{
				retained1.setDay6("");
			}
			if(date7.containsKey(df.format(d))){
				retained1.setDay7(date7.get(key).toString());
			}else{
				retained1.setDay7("");
			}
			if(date8.containsKey(df.format(d))){
				retained1.setDay8(date8.get(key).toString());
			}else{
				retained1.setDay8("");
			}
			if(date9.containsKey(df.format(d))){
				retained1.setDay9(date9.get(key).toString());
			}else{
				retained1.setDay9("");
			}
			if(date10.containsKey(df.format(d))){
				retained1.setDay10(date10.get(key).toString());
			}else{
				retained1.setDay10("");
			}
			if(date11.containsKey(df.format(d))){
				retained1.setDay11(date11.get(key).toString());
			}else{
				retained1.setDay11("");
			}			
			if(date12.containsKey(df.format(d))){
				retained2.setDay12(date12.get(key).toString());
			}else{
				retained2.setDay12("");
			}
			if(date13.containsKey(df.format(d))){
				retained2.setDay13(date13.get(key).toString());
			}else{
				retained2.setDay13("");
			}
			if(date14.containsKey(df.format(d))){
				retained2.setDay14(date14.get(key).toString());
			}else{
				retained2.setDay14("");
			}
			if(date15.containsKey(df.format(d))){
				retained2.setDay15(date15.get(key).toString());
			}else{
				retained2.setDay15("");
			}
			if(date16.containsKey(df.format(d))){
				retained2.setDay16(date16.get(key).toString());
			}else{
				retained2.setDay16("");
			}
			if(date17.containsKey(df.format(d))){
				retained2.setDay17(date17.get(key).toString());
			}else{
				retained2.setDay17("");
			}
			if(date18.containsKey(df.format(d))){
				retained2.setDay18(date18.get(key).toString());
			}else{
				retained2.setDay18("");
			}
			if(date19.containsKey(df.format(d))){
				retained2.setDay19(date19.get(key).toString());
			}else{
				retained2.setDay19("");
			}
			if(date20.containsKey(df.format(d))){
				retained2.setDay20(date20.get(key).toString());
			}else{
				retained2.setDay20("");
			}
			if(date21.containsKey(df.format(d))){
				retained2.setDay21(date21.get(key).toString());
			}else{
				retained2.setDay21("");
			}
			if(date22.containsKey(df.format(d))){
				retained3.setDay22(date22.get(key).toString());
			}else{
				retained3.setDay22("");
			}
			if(date23.containsKey(df.format(d))){
				retained3.setDay23(date23.get(key).toString());
			}else{
				retained3.setDay23("");
			}
			if(date24.containsKey(df.format(d))){
				retained3.setDay24(date24.get(key).toString());
			}else{
				retained3.setDay24("");
			}
			if(date25.containsKey(df.format(d))){
				retained3.setDay25(date25.get(key).toString());
			}else{
				retained3.setDay25("");
			}
			if(date26.containsKey(df.format(d))){
				retained3.setDay26(date26.get(key).toString());
			}else{
				retained3.setDay26("");
			}
			if(date27.containsKey(df.format(d))){
				retained3.setDay27(date27.get(key).toString());
			}else{
				retained3.setDay27("");
			}
			if(date28.containsKey(df.format(d))){
				retained3.setDay28(date28.get(key).toString());
			}else{
				retained3.setDay28("");
			}
			if(date29.containsKey(df.format(d))){
				retained3.setDay29(date29.get(key).toString());
			}else{
				retained3.setDay29("");
			}
			if(date30.containsKey(df.format(d))){
				retained3.setDay30(date30.get(key).toString());
			}else{
				retained3.setDay30("");
			}
			if(date31.containsKey(df.format(d))){
				retained3.setDay31(date31.get(key).toString());
			}else{
				retained3.setDay31("");
			}
			retained1s.add(retained1);
			retained2s.add(retained2);
			retained3s.add(retained3);
			time += oneDay;
		}
		m.put("retained1s", retained1s);
		m.put("retained2s", retained2s);
		m.put("retained3s", retained3s);
		
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

