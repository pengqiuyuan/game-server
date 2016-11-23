package com.enlight.game.service.es;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
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
import com.enlight.game.entity.gm.RetainedAll1;

@Component
@Transactional
public class RetainedServer {
	

	@Autowired
	private Client client;

	public Map<String, Object> searchAllRetained(String index ,String type,String dateFrom,String dateTo,String switchTable) throws IOException, ElasticsearchException, ParseException{
		BoolQueryBuilder builder = QueryBuilders.boolQuery()
        		        .must(QueryBuilders.rangeQuery("date").from(dateFrom).to(dateTo))
                		.must(QueryBuilders.termQuery("key", "all"));
		return esSearch(builder, index, type, dateFrom, dateTo,switchTable);
	}
	
	public Map<String, Object> searchServerZoneRetained(String index ,String type,String dateFrom,String dateTo,String value,String switchTable) throws IOException, ElasticsearchException, ParseException{
		BoolQueryBuilder builder = QueryBuilders.boolQuery()
        		        .must(QueryBuilders.rangeQuery("date").from(dateFrom).to(dateTo))
                		.must(QueryBuilders.termQuery("key", "serverZone"))
                		.must(QueryBuilders.termQuery("value", value));
		return esSearch(builder, index, type, dateFrom, dateTo,switchTable);
	}
	public Map<String, Object> searchPlatFormRetained(String index ,String type,String dateFrom,String dateTo,String value,String switchTable) throws IOException, ElasticsearchException, ParseException{
		BoolQueryBuilder builder = QueryBuilders.boolQuery()
        		        .must(QueryBuilders.rangeQuery("date").from(dateFrom).to(dateTo))
                		.must(QueryBuilders.termQuery("key", "platForm"))
                		.must(QueryBuilders.termQuery("value", value));
		return esSearch(builder, index, type, dateFrom, dateTo,switchTable);
	}
	
	public Map<String, Object> searchServerRetained(String index ,String type,String dateFrom,String dateTo,String value,String switchTable) throws IOException, ElasticsearchException, ParseException{
		BoolQueryBuilder builder = QueryBuilders.boolQuery()
        		        .must(QueryBuilders.rangeQuery("date").from(dateFrom).to(dateTo))
                		.must(QueryBuilders.termQuery("key", "server"))
                		.must(QueryBuilders.termQuery("value", value)
                		);
		return esSearch(builder, index, type, dateFrom, dateTo,switchTable);
	}
	
	public Map<String,Object> esSearch(BoolQueryBuilder builder,String index,String type,String dateFrom,String dateTo,String switchTable) throws IOException, ElasticsearchException, ParseException{
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
				return retained(response,dateFrom,dateTo,switchTable);
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
	
	
	public Map<String, Object> retained(SearchResponse response,String dateFrom,String dateTo,String switchTable) throws ParseException{
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
				date2.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("3Day")){
				date3.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("4Day")){
				date4.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("5Day")){
				date5.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("6Day")){
				date6.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("7Day")){
				date7.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("8Day")){
				date8.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("9Day")){
				date9.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("10Day")){
				date10.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("11Day")){
				date11.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("12Day")){
				date12.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("13Day")){
				date13.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("14Day")){
				date14.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("15Day")){
				date15.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("16Day")){
				date16.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("17Day")){
				date17.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("18Day")){
				date18.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("19Day")){
				date19.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("20Day")){
				date20.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("21Day")){
				date21.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("22Day")){
				date22.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("23Day")){
				date23.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("24Day")){
				date24.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("25Day")){
				date25.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("26Day")){
				date26.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("27Day")){
				date27.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("28Day")){
				date28.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("29Day")){
				date29.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("30Day")){
				date30.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
			}else if(source.get("ctRetained").equals("31Day")){
				date31.put(source.get("date").toString(), source.get("retained").toString()+"%（"+source.get("value2").toString()+"/"+source.get("value1").toString()+"）");
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
		/*计算N日留存*/
		int t2=0,t3=0,t4=0,t5=0,t6=0,t7=0,t8=0,t9=0,t10=0,t11=0,t12=0,t13=0,t14=0,t15=0,t16=0,t17=0,t18=0,t19=0,t20=0,t21=0,t22=0,t23=0,t24=0,t25=0,t26=0,t27=0,t28=0,t29=0,t30=0,t31=0;
		int a2=0,a3=0,a4=0,a5=0,a6=0,a7=0,a8=0,a9=0,a10=0,a11=0,a12=0,a13=0,a14=0,a15=0,a16=0,a17=0,a18=0,a19=0,a20=0,a21=0,a22=0,a23=0,a24=0,a25=0,a26=0,a27=0,a28=0,a29=0,a30=0,a31 = 0;
		
		String m2="",m3="",m4="",m5="",m6="",m7="",m8="",m9="",m10="",m11="",m12="",m13="",m14="",m15="",m16="",m17="",m18="",m19="",m20="",m21="",m22="",m23="",m24="",m25="",m26="",m27="",m28="",m29="",m30="",m31 ="";
		String n2="",n3="",n4="",n5="",n6="",n7="",n8="",n9="",n10="",n11="",n12="",n13="",n14="",n15="",n16="",n17="",n18="",n19="",n20="",n21="",n22="",n23="",n24="",n25="",n26="",n27="",n28="",n29="",n30="",n31 ="";
		/*计算N日留存*/
		
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
			/*switchTable 留存页面的三张表切换 1、2、3   1所有 2登录 3留存*/
			if(date2.containsKey(df.format(d))){
				retained1.setAddUser(date2.get(key).toString().split("（|/|）")[2]); //新增的人数
				if(switchTable.equals("1")){
					retained1.setDay2(date2.get(key).toString());
				}else if(switchTable.equals("2")){
					retained1.setDay2(date2.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained1.setDay2(date2.get(key).toString().split("（|/|）")[0]);
				}
				t2 = t2 + Integer.valueOf(date2.get(key).toString().split("（|/|）")[1]);
				a2 = a2 + Integer.valueOf(date2.get(key).toString().split("（|/|）")[2]);
				m2 = m2 + date2.get(key).toString().split("（|/|）")[1] +"+"; 
				n2 = n2 + date2.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained1.setDay2("");
			}
			if(date3.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained1.setDay3(date3.get(key).toString());
				}else if(switchTable.equals("2")){
					retained1.setDay3(date3.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained1.setDay3(date3.get(key).toString().split("（|/|）")[0]);
				}
				t3 = t3 + Integer.valueOf(date3.get(key).toString().split("（|/|）")[1]);
				a3 = a3 + Integer.valueOf(date3.get(key).toString().split("（|/|）")[2]);
				m3 = m3 + date3.get(key).toString().split("（|/|）")[1] +"+"; 
				n3 = n3 + date3.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained1.setDay3("");
			}
			if(date4.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained1.setDay4(date4.get(key).toString());
				}else if(switchTable.equals("2")){
					retained1.setDay4(date4.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained1.setDay4(date4.get(key).toString().split("（|/|）")[0]);
				}
				t4 = t4 + Integer.valueOf(date4.get(key).toString().split("（|/|）")[1]);
				a4 = a4 + Integer.valueOf(date4.get(key).toString().split("（|/|）")[2]);
				m4 = m4 + date4.get(key).toString().split("（|/|）")[1] +"+"; 
				n4 = n4 + date4.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained1.setDay4("");
			}
			if(date5.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained1.setDay5(date5.get(key).toString());
				}else if(switchTable.equals("2")){
					retained1.setDay5(date5.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained1.setDay5(date5.get(key).toString().split("（|/|）")[0]);
				}
				t5 = t5 + Integer.valueOf(date5.get(key).toString().split("（|/|）")[1]);
				a5 = a5 + Integer.valueOf(date5.get(key).toString().split("（|/|）")[2]);
				m5 = m5 + date5.get(key).toString().split("（|/|）")[1] +"+"; 
				n5 = n5 + date5.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained1.setDay5("");
			}
			if(date6.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained1.setDay6(date6.get(key).toString());
				}else if(switchTable.equals("2")){
					retained1.setDay6(date6.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained1.setDay6(date6.get(key).toString().split("（|/|）")[0]);
				}
				t6 = t6 + Integer.valueOf(date6.get(key).toString().split("（|/|）")[1]);
				a6 = a6 + Integer.valueOf(date6.get(key).toString().split("（|/|）")[2]);
				m6 = m6 + date6.get(key).toString().split("（|/|）")[1] +"+"; 
				n6 = n6 + date6.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained1.setDay6("");
			}
			if(date7.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained1.setDay7(date7.get(key).toString());
				}else if(switchTable.equals("2")){
					retained1.setDay7(date7.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained1.setDay7(date7.get(key).toString().split("（|/|）")[0]);
				}
				t7 = t7 + Integer.valueOf(date7.get(key).toString().split("（|/|）")[1]);
				a7 = a7 + Integer.valueOf(date7.get(key).toString().split("（|/|）")[2]);
				m7 = m7 + date7.get(key).toString().split("（|/|）")[1] +"+"; 
				n7 = n7 + date7.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained1.setDay7("");
			}
			if(date8.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained1.setDay8(date8.get(key).toString());
				}else if(switchTable.equals("2")){
					retained1.setDay8(date8.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained1.setDay8(date8.get(key).toString().split("（|/|）")[0]);
				}
				t8 = t8 + Integer.valueOf(date8.get(key).toString().split("（|/|）")[1]);
				a8 = a8 + Integer.valueOf(date8.get(key).toString().split("（|/|）")[2]);
				m8 = m8 + date8.get(key).toString().split("（|/|）")[1] +"+"; 
				n8 = n8 + date8.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained1.setDay8("");
			}
			if(date9.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained1.setDay9(date9.get(key).toString());
				}else if(switchTable.equals("2")){
					retained1.setDay9(date9.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained1.setDay9(date9.get(key).toString().split("（|/|）")[0]);
				}
				t9 = t9 + Integer.valueOf(date9.get(key).toString().split("（|/|）")[1]);
				a9 = a9 + Integer.valueOf(date9.get(key).toString().split("（|/|）")[2]);
				m9 = m9 + date9.get(key).toString().split("（|/|）")[1] +"+"; 
				n9 = n9 + date9.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained1.setDay9("");
			}
			if(date10.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained1.setDay10(date10.get(key).toString());
				}else if(switchTable.equals("2")){
					retained1.setDay10(date10.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained1.setDay10(date10.get(key).toString().split("（|/|）")[0]);
				}
				t10 = t10 + Integer.valueOf(date10.get(key).toString().split("（|/|）")[1]);
				a10 = a10 + Integer.valueOf(date10.get(key).toString().split("（|/|）")[2]);
				m10 = m10 + date10.get(key).toString().split("（|/|）")[1] +"+"; 
				n10 = n10 + date10.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained1.setDay10("");
			}
			if(date11.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained1.setDay11(date11.get(key).toString());
				}else if(switchTable.equals("2")){
					retained1.setDay11(date11.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained1.setDay11(date11.get(key).toString().split("（|/|）")[0]);
				}
				t11 = t11 + Integer.valueOf(date11.get(key).toString().split("（|/|）")[1]);
				a11 = a11 + Integer.valueOf(date11.get(key).toString().split("（|/|）")[2]);
				m11 = m11 + date11.get(key).toString().split("（|/|）")[1] +"+"; 
				n11 = n11 + date11.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained1.setDay11("");
			}			
			if(date12.containsKey(df.format(d))){
				retained2.setAddUser(date12.get(key).toString().split("（|/|）")[2]); //新增的人数
				if(switchTable.equals("1")){
					retained2.setDay12(date12.get(key).toString());
				}else if(switchTable.equals("2")){
					retained2.setDay12(date12.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained2.setDay12(date12.get(key).toString().split("（|/|）")[0]);
				}
				t12 = t12 + Integer.valueOf(date12.get(key).toString().split("（|/|）")[1]);
				a12 = a12 + Integer.valueOf(date12.get(key).toString().split("（|/|）")[2]);
				m12 = m12 + date12.get(key).toString().split("（|/|）")[1] +"+"; 
				n12 = n12 + date12.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained2.setDay12("");
			}
			if(date13.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained2.setDay13(date13.get(key).toString());
				}else if(switchTable.equals("2")){
					retained2.setDay13(date13.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained2.setDay13(date13.get(key).toString().split("（|/|）")[0]);
				}
				t13 = t13 + Integer.valueOf(date13.get(key).toString().split("（|/|）")[1]);
				a13 = a13 + Integer.valueOf(date13.get(key).toString().split("（|/|）")[2]);
				m13 = m13 + date13.get(key).toString().split("（|/|）")[1] +"+"; 
				n13 = n13 + date13.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained2.setDay13("");
			}
			if(date14.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained2.setDay14(date14.get(key).toString());
				}else if(switchTable.equals("2")){
					retained2.setDay14(date14.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained2.setDay14(date14.get(key).toString().split("（|/|）")[0]);
				}
				t14 = t14 + Integer.valueOf(date14.get(key).toString().split("（|/|）")[1]);
				a14 = a14 + Integer.valueOf(date14.get(key).toString().split("（|/|）")[2]);
				m14 = m14 + date14.get(key).toString().split("（|/|）")[1] +"+"; 
				n14 = n14 + date14.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained2.setDay14("");
			}
			if(date15.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained2.setDay15(date15.get(key).toString());
				}else if(switchTable.equals("2")){
					retained2.setDay15(date15.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained2.setDay15(date15.get(key).toString().split("（|/|）")[0]);
				}
				t15 = t15 + Integer.valueOf(date15.get(key).toString().split("（|/|）")[1]);
				a15 = a15 + Integer.valueOf(date15.get(key).toString().split("（|/|）")[2]);
				m15 = m15 + date15.get(key).toString().split("（|/|）")[1] +"+"; 
				n15 = n15 + date15.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained2.setDay15("");
			}
			if(date16.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained2.setDay16(date16.get(key).toString());
				}else if(switchTable.equals("2")){
					retained2.setDay16(date16.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained2.setDay16(date16.get(key).toString().split("（|/|）")[0]);
				}
				t16 = t16 + Integer.valueOf(date16.get(key).toString().split("（|/|）")[1]);
				a16 = a16 + Integer.valueOf(date16.get(key).toString().split("（|/|）")[2]);
				m16 = m16 + date16.get(key).toString().split("（|/|）")[1] +"+"; 
				n16 = n16 + date16.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained2.setDay16("");
			}
			if(date17.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained2.setDay17(date17.get(key).toString());
				}else if(switchTable.equals("2")){
					retained2.setDay17(date17.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained2.setDay17(date17.get(key).toString().split("（|/|）")[0]);
				}
				t17 = t17 + Integer.valueOf(date17.get(key).toString().split("（|/|）")[1]);
				a17 = a17 + Integer.valueOf(date17.get(key).toString().split("（|/|）")[2]);
				m17 = m17 + date17.get(key).toString().split("（|/|）")[1] +"+"; 
				n17 = n17 + date17.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained2.setDay17("");
			}
			if(date18.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained2.setDay18(date18.get(key).toString());
				}else if(switchTable.equals("2")){
					retained2.setDay18(date18.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained2.setDay18(date18.get(key).toString().split("（|/|）")[0]);
				}
				t18 = t18 + Integer.valueOf(date18.get(key).toString().split("（|/|）")[1]);
				a18 = a18 + Integer.valueOf(date18.get(key).toString().split("（|/|）")[2]);
				m18 = m18 + date18.get(key).toString().split("（|/|）")[1] +"+"; 
				n18 = n18 + date18.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained2.setDay18("");
			}
			if(date19.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained2.setDay19(date19.get(key).toString());
				}else if(switchTable.equals("2")){
					retained2.setDay19(date19.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained2.setDay19(date19.get(key).toString().split("（|/|）")[0]);
				}
				t19 = t19 + Integer.valueOf(date19.get(key).toString().split("（|/|）")[1]);
				a19 = a19 + Integer.valueOf(date19.get(key).toString().split("（|/|）")[2]);
				m19 = m19 + date19.get(key).toString().split("（|/|）")[1] +"+"; 
				n19 = n19 + date19.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained2.setDay19("");
			}
			if(date20.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained2.setDay20(date20.get(key).toString());
				}else if(switchTable.equals("2")){
					retained2.setDay20(date20.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained2.setDay20(date20.get(key).toString().split("（|/|）")[0]);
				}
				t20 = t20 + Integer.valueOf(date20.get(key).toString().split("（|/|）")[1]);
				a20 = a20 + Integer.valueOf(date20.get(key).toString().split("（|/|）")[2]);
				m20 = m20 + date20.get(key).toString().split("（|/|）")[1] +"+"; 
				n20 = n20 + date20.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained2.setDay20("");
			}
			if(date21.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained2.setDay21(date21.get(key).toString());
				}else if(switchTable.equals("2")){
					retained2.setDay21(date21.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained2.setDay21(date21.get(key).toString().split("（|/|）")[0]);
				}
				t21 = t21 + Integer.valueOf(date21.get(key).toString().split("（|/|）")[1]);
				a21 = a21 + Integer.valueOf(date21.get(key).toString().split("（|/|）")[2]);
				m21 = m21 + date21.get(key).toString().split("（|/|）")[1] +"+"; 
				n21 = n21 + date21.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained2.setDay21("");
			}
			if(date22.containsKey(df.format(d))){
				retained3.setAddUser(date22.get(key).toString().split("（|/|）")[2]); //新增的人数
				if(switchTable.equals("1")){
					retained3.setDay22(date22.get(key).toString());
				}else if(switchTable.equals("2")){
					retained3.setDay22(date22.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained3.setDay22(date22.get(key).toString().split("（|/|）")[0]);
				}
				t22 = t22 + Integer.valueOf(date22.get(key).toString().split("（|/|）")[1]);
				a22 = a22 + Integer.valueOf(date22.get(key).toString().split("（|/|）")[2]);
				m22 = m22 + date22.get(key).toString().split("（|/|）")[1] +"+"; 
				n22 = n22 + date22.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained3.setDay22("");
			}
			if(date23.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained3.setDay23(date23.get(key).toString());
				}else if(switchTable.equals("2")){
					retained3.setDay23(date23.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained3.setDay23(date23.get(key).toString().split("（|/|）")[0]);
				}
				t23 = t23 + Integer.valueOf(date23.get(key).toString().split("（|/|）")[1]);
				a23 = a23 + Integer.valueOf(date23.get(key).toString().split("（|/|）")[2]);
				m23 = m23 + date23.get(key).toString().split("（|/|）")[1] +"+"; 
				n23 = n23 + date23.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained3.setDay23("");
			}
			if(date24.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained3.setDay24(date24.get(key).toString());
				}else if(switchTable.equals("2")){
					retained3.setDay24(date24.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained3.setDay24(date24.get(key).toString().split("（|/|）")[0]);
				}
				t24 = t24 + Integer.valueOf(date24.get(key).toString().split("（|/|）")[1]);
				a24 = a24 + Integer.valueOf(date24.get(key).toString().split("（|/|）")[2]);
				m24 = m24 + date24.get(key).toString().split("（|/|）")[1] +"+"; 
				n24 = n24 + date24.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained3.setDay24("");
			}
			if(date25.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained3.setDay25(date25.get(key).toString());
				}else if(switchTable.equals("2")){
					retained3.setDay25(date25.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained3.setDay25(date25.get(key).toString().split("（|/|）")[0]);
				}
				t25 = t25 + Integer.valueOf(date25.get(key).toString().split("（|/|）")[1]);
				a25 = a25 + Integer.valueOf(date25.get(key).toString().split("（|/|）")[2]);
				m25 = m25 + date25.get(key).toString().split("（|/|）")[1] +"+"; 
				n25 = n25 + date25.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained3.setDay25("");
			}
			if(date26.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained3.setDay26(date26.get(key).toString());
				}else if(switchTable.equals("2")){
					retained3.setDay26(date26.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained3.setDay26(date26.get(key).toString().split("（|/|）")[0]);
				}
				t26 = t26 + Integer.valueOf(date26.get(key).toString().split("（|/|）")[1]);
				a26 = a26 + Integer.valueOf(date26.get(key).toString().split("（|/|）")[2]);
				m26 = m26 + date26.get(key).toString().split("（|/|）")[1] +"+"; 
				n26 = n26 + date26.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained3.setDay26("");
			}
			if(date27.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained3.setDay27(date27.get(key).toString());
				}else if(switchTable.equals("2")){
					retained3.setDay27(date27.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained3.setDay27(date27.get(key).toString().split("（|/|）")[0]);
				}
				t27 = t27 + Integer.valueOf(date27.get(key).toString().split("（|/|）")[1]);
				a27 = a27 + Integer.valueOf(date27.get(key).toString().split("（|/|）")[2]);
				m27 = m27 + date27.get(key).toString().split("（|/|）")[1] +"+"; 
				n27 = n27 + date27.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained3.setDay27("");
			}
			if(date28.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained3.setDay28(date28.get(key).toString());
				}else if(switchTable.equals("2")){
					retained3.setDay28(date28.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained3.setDay28(date28.get(key).toString().split("（|/|）")[0]);
				}
				t28 = t28 + Integer.valueOf(date28.get(key).toString().split("（|/|）")[1]);
				a28 = a28 + Integer.valueOf(date28.get(key).toString().split("（|/|）")[2]);
				m28 = m28 + date28.get(key).toString().split("（|/|）")[1] +"+"; 
				n28 = n28 + date28.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained3.setDay28("");
			}
			if(date29.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained3.setDay29(date29.get(key).toString());
				}else if(switchTable.equals("2")){
					retained3.setDay29(date29.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained3.setDay29(date29.get(key).toString().split("（|/|）")[0]);
				}
				t29 = t29 + Integer.valueOf(date29.get(key).toString().split("（|/|）")[1]);
				a29 = a29 + Integer.valueOf(date29.get(key).toString().split("（|/|）")[2]);
				m29 = m29 + date29.get(key).toString().split("（|/|）")[1] +"+"; 
				n29 = n29 + date29.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained3.setDay29("");
			}
			if(date30.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained3.setDay30(date30.get(key).toString());
				}else if(switchTable.equals("2")){
					retained3.setDay30(date30.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained3.setDay30(date30.get(key).toString().split("（|/|）")[0]);
				}
				t30 = t30 + Integer.valueOf(date30.get(key).toString().split("（|/|）")[1]);
				a30 = a30 + Integer.valueOf(date30.get(key).toString().split("（|/|）")[2]);
				m30 = m30 + date30.get(key).toString().split("（|/|）")[1] +"+"; 
				n30 = n30 + date30.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained3.setDay30("");
			}
			if(date31.containsKey(df.format(d))){
				if(switchTable.equals("1")){
					retained3.setDay31(date31.get(key).toString());
				}else if(switchTable.equals("2")){
					retained3.setDay31(date31.get(key).toString().split("（|/|）")[1]);
				}else if(switchTable.equals("3")){
					retained3.setDay31(date31.get(key).toString().split("（|/|）")[0]);
				}
				t31 = t31 + Integer.valueOf(date31.get(key).toString().split("（|/|）")[1]);
				a31 = a31 + Integer.valueOf(date31.get(key).toString().split("（|/|）")[2]);
				m31 = m31 + date31.get(key).toString().split("（|/|）")[1] +"+"; 
				n31 = n31 + date31.get(key).toString().split("（|/|）")[2] +"+"; 
			}else{
				retained3.setDay31("");
			}
			retained1s.add(retained1);
			retained2s.add(retained2);
			retained3s.add(retained3);
			time += oneDay;
		}
		/*计算N日留存*/
		RetainedAll1 retainedAll1 = new RetainedAll1();
		retainedAll1.setxDate(dateFrom+"~"+dateTo);
		retainedAll1.setAddUser(a2==0?"":String.valueOf(a2));
		retainedAll1.setDay2(a2==0 ?"":String.valueOf(formatDouble(t2*100/a2))+"%");
		retainedAll1.setDay2text(n2==""?"":"（"+m2+"）/（"+n2+"）");
		retainedAll1.setDay3(a3==0 ?"":String.valueOf(formatDouble(t3*100/a3))+"%");
		retainedAll1.setDay3text(n3==""?"":"（"+m3+"）/（"+n3+"）");
		retainedAll1.setDay4(a4==0 ?"":String.valueOf(formatDouble(t4*100/a4))+"%");
		retainedAll1.setDay4text(n4==""?"":"（"+m4+"）/（"+n4+"）");
		retainedAll1.setDay5(a5==0 ?"":String.valueOf(formatDouble(t5*100/a5))+"%");
		retainedAll1.setDay5text(n5==""?"":"（"+m5+"）/（"+n5+"）");
		retainedAll1.setDay6(a6==0 ?"":String.valueOf(formatDouble(t6*100/a6))+"%");
		retainedAll1.setDay6text(n6==""?"":"（"+m6+"）/（"+n6+"）");
		retainedAll1.setDay7(a7==0 ?"":String.valueOf(formatDouble(t7*100/a7))+"%");
		retainedAll1.setDay7text(n7==""?"":"（"+m7+"）/（"+n7+"）");
		retainedAll1.setDay8(a8==0 ?"":String.valueOf(formatDouble(t8*100/a8))+"%");
		retainedAll1.setDay8text(n8==""?"":"（"+m8+"）/（"+n8+"）");
		retainedAll1.setDay9(a9==0 ?"":String.valueOf(formatDouble(t9*100/a9))+"%");
		retainedAll1.setDay9text(n9==""?"":"（"+m9+"）/（"+n9+"）");
		retainedAll1.setDay10(a10==0 ?"":String.valueOf(formatDouble(t10*100/a10))+"%");
		retainedAll1.setDay10text(n10==""?"":"（"+m10+"）/（"+n10+"）");
		retainedAll1.setDay11(a11==0 ?"":String.valueOf(formatDouble(t11*100/a11))+"%");
		retainedAll1.setDay11text(n11==""?"":"（"+m11+"）/（"+n11+"）");
		retainedAll1.setDay12(a12==0 ?"":String.valueOf(formatDouble(t12*100/a12))+"%");
		retainedAll1.setDay12text(n12==""?"":"（"+m12+"）/（"+n12+"）");
		retainedAll1.setDay13(a13==0 ?"":String.valueOf(formatDouble(t13*100/a13))+"%");
		retainedAll1.setDay13text(n13==""?"":"（"+m13+"）/（"+n13+"）");
		retainedAll1.setDay14(a14==0 ?"":String.valueOf(formatDouble(t14*100/a14))+"%");
		retainedAll1.setDay14text(n14==""?"":"（"+m14+"）/（"+n14+"）");
		retainedAll1.setDay15(a15==0 ?"":String.valueOf(formatDouble(t15*100/a15))+"%");
		retainedAll1.setDay15text(n15==""?"":"（"+m15+"）/（"+n15+"）");
		retainedAll1.setDay16(a16==0 ?"":String.valueOf(formatDouble(t16*100/a16))+"%");
		retainedAll1.setDay16text(n16==""?"":"（"+m16+"）/（"+n16+"）");
		retainedAll1.setDay17(a17==0 ?"":String.valueOf(formatDouble(t17*100/a17))+"%");
		retainedAll1.setDay17text(n17==""?"":"（"+m17+"）/（"+n17+"）");
		retainedAll1.setDay18(a18==0 ?"":String.valueOf(formatDouble(t18*100/a18))+"%");
		retainedAll1.setDay18text(n18==""?"":"（"+m18+"）/（"+n18+"）");
		retainedAll1.setDay19(a19==0 ?"":String.valueOf(formatDouble(t19*100/a19))+"%");
		retainedAll1.setDay19text(n19==""?"":"（"+m19+"）/（"+n19+"）");
		retainedAll1.setDay20(a20==0 ?"":String.valueOf(formatDouble(t20*100/a20))+"%");
		retainedAll1.setDay20text(n20==""?"":"（"+m20+"）/（"+n20+"）");
		retainedAll1.setDay21(a21==0 ?"":String.valueOf(formatDouble(t21*100/a21))+"%");
		retainedAll1.setDay21text(n21==""?"":"（"+m21+"）/（"+n21+"）");
		retainedAll1.setDay22(a22==0 ?"":String.valueOf(formatDouble(t22*100/a22))+"%");
		retainedAll1.setDay22text(n22==""?"":"（"+m22+"）/（"+n22+"）");
		retainedAll1.setDay23(a23==0 ?"":String.valueOf(formatDouble(t23*100/a23))+"%");
		retainedAll1.setDay23text(n23==""?"":"（"+m23+"）/（"+n23+"）");
		retainedAll1.setDay24(a24==0 ?"":String.valueOf(formatDouble(t24*100/a24))+"%");
		retainedAll1.setDay24text(n24==""?"":"（"+m24+"）/（"+n24+"）");
		retainedAll1.setDay25(a25==0 ?"":String.valueOf(formatDouble(t25*100/a25))+"%");
		retainedAll1.setDay25text(n25==""?"":"（"+m25+"）/（"+n25+"）");
		retainedAll1.setDay26(a26==0 ?"":String.valueOf(formatDouble(t26*100/a26))+"%");
		retainedAll1.setDay26text(n26==""?"":"（"+m26+"）/（"+n26+"）");
		retainedAll1.setDay27(a27==0 ?"":String.valueOf(formatDouble(t27*100/a27))+"%");
		retainedAll1.setDay27text(n27==""?"":"（"+m27+"）/（"+n27+"）");
		retainedAll1.setDay28(a28==0 ?"":String.valueOf(formatDouble(t28*100/a28))+"%");
		retainedAll1.setDay28text(n28==""?"":"（"+m28+"）/（"+n28+"）");
		retainedAll1.setDay29(a29==0 ?"":String.valueOf(formatDouble(t29*100/a29))+"%");
		retainedAll1.setDay29text(n29==""?"":"（"+m29+"）/（"+n29+"）");
		retainedAll1.setDay30(a30==0 ?"":String.valueOf(formatDouble(t30*100/a30))+"%");
		retainedAll1.setDay30text(n30==""?"":"（"+m30+"）/（"+n30+"）");
		retainedAll1.setDay31(a31==0 ?"":String.valueOf(formatDouble(t31*100/a31))+"%");
		retainedAll1.setDay31text(n31==""?"":"（"+m31+"）/（"+n31+"）");
		/*计算N日留存*/		
		m.put("retained1s", retained1s);
		m.put("retained2s", retained2s);
		m.put("retained3s", retained3s);
		m.put("retainedAll1", retainedAll1);
		
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
	
    public static String formatDouble(double d) {
    	System.out.println("adsasdasd   "  + d);
        DecimalFormat df = new DecimalFormat("#.00");
        System.out.println("qw42342342   "  + df.format(d));
        return df.format(d);
    }

}

