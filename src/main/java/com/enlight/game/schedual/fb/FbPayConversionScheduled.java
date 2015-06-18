package com.enlight.game.schedual.fb;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enlight.game.entity.analysis.UserRetained;
import com.enlight.game.service.store.StoreService;
import com.enlight.game.util.EsUtil;

@Transactional(readOnly = true)
public class FbPayConversionScheduled {
	
	@Autowired
	public Client client;
	
	//项目名称
	private static final String fb_game = "FB";
	
	private static final String index = "logstash-fb-*";
	
	private static final String type_user = "fb_user.log";
	
	private static final String type_money = "fb_money.log";
	
	
	private static final String bulk_index_money = "log_fb_money";
	
	private static final String bulk_type_money_add = "fb_money_add";
	
	private static final String bulk_type_money_all = "fb_money_all";
	
	private static final String bulk_type_money_day = "fb_money_day";
	
	private static final String bulk_type_money_week = "fb_money_week";
	
	private static final String bulk_type_money_mouth = "fb_money_mouth";
	
	EsUtil esUtilTest = new EsUtil();
	
	@Autowired
	private StoreService storeService;

	public void esAll() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'"); 
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数  
		//新增付费用户
		SearchResponse sr = client.prepareSearch(index).setTypes(type_money).setSearchType("count").setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
				        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get"),
		        		FilterBuilders.termFilter("是否首次充值", "1")
						))
		        ).execute().actionGet();
		Long newpayuser = sr.getHits().totalHits();
		bulkRequest.add(client.prepareIndex(bulk_index_money, bulk_type_money_add)
		        .setSource(jsonBuilder()
			           	 .startObject()
	                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
	                        .field("gameId", fb_game)
	                        .field("key", "all")
	                        .field("@timestamp", new Date())
	                        .field("newpayuser", newpayuser)
	                    .endObject()
		                  )
		        );
		System.out.println("新增付费用户："+newpayuser);
		//累计付费用户
		IndicesExistsResponse responseindex = client.admin().indices().prepareExists(bulk_index_money).execute().actionGet(); 
		boolean ty = false;
		if(responseindex.isExists()){
			TypesExistsResponse responsetype = client.admin().indices() .prepareTypesExists(bulk_index_money).setTypes(bulk_type_money_all).execute().actionGet(); 
			if(responsetype.isExists()){
				ty = true;
			}
		}
		long allpayuser  = 0L;
		if(responseindex.isExists() && ty){//判断index是否存在 判断type是否存在
			System.out.println("存在");
			SearchResponse srTotal = client.prepareSearch(bulk_index_money).setTypes(bulk_type_money_all).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
					FilterBuilders.andFilter(
							FilterBuilders.termFilter("key", "all"),
					        FilterBuilders.termFilter("date", esUtilTest.twoDayAgoFrom()))
			        )).execute().actionGet();
			for (SearchHit searchHit : srTotal.getHits()) {
				Map<String, Object> source = searchHit.getSource();
				allpayuser = Long.valueOf(source.get("allpayuser").toString());
			}
			allpayuser = allpayuser+newpayuser;
			System.out.println("历史累计付费用户all："+allpayuser  +",昨天新增付费用户："+ newpayuser);
		}else{
			SearchResponse srTotal = client.prepareSearch(index).setTypes(type_money).setSearchType("count").setQuery(
					QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
							FilterBuilders.andFilter(
									FilterBuilders.rangeFilter("@timestamp").from("2014-01-11").to(esUtilTest.nowDate()),
							        FilterBuilders.termFilter("日志分类关键字", "money_get"),
					        		FilterBuilders.termFilter("是否首次充值", "1")
									))
							).execute().actionGet();
			allpayuser  = srTotal.getHits().totalHits();
			System.out.println("历史累计付费用户all："+allpayuser);
		}
		bulkRequest.add(client.prepareIndex(bulk_index_money, bulk_type_money_all)
		        .setSource(jsonBuilder()
			           	 .startObject()
	                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
	                        .field("gameId", fb_game)
	                        .field("key", "all")
	                        .field("@timestamp", new Date())
	                        .field("allpayuser", allpayuser)
	                    .endObject()
		                  )
		        );
		
		//首日付费率、首日付费数
		SearchResponse dayPayRate_user = client.prepareSearch(index).setTypes(type_user).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
						FilterBuilders.andFilter(
						        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.dayFrom()).to(esUtilTest.nowDate()),
				        		FilterBuilders.termFilter("日志分类关键字", "create")
								))
						).execute().actionGet();
		SearchResponse dayPayRate_money = client.prepareSearch(index).setTypes(type_money).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
						FilterBuilders.andFilter(
								FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.dayFrom()).to(esUtilTest.nowDate()),
								FilterBuilders.termFilter("注册时间", esUtilTest.dayFrom().split("T")[0]),
						        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        		FilterBuilders.termFilter("是否首次充值", "1")
								))
						).execute().actionGet();
		Double day;
		if(dayPayRate_user.getHits().totalHits() == 0){
			day = 0.00;
		}else{
			day = (double)dayPayRate_money.getHits().totalHits()*100/dayPayRate_user.getHits().totalHits();
		}
		bulkRequest.add(client.prepareIndex(bulk_index_money, bulk_type_money_day)
		        .setSource(jsonBuilder()
			           	 .startObject()
	                        .field("date", esUtilTest.dayFrom().split("T")[0])
	                        .field("gameId", fb_game)
	                        .field("key", "all")
	                        .field("@timestamp", new Date())
	                        .field("ct", "firstday") //首日
	                        .field("paynum", dayPayRate_money.getHits().totalHits()) //首日付费数 去重
	                        .field("payofrate", df.format(day)) // 首日付费率
	                    .endObject()
		                  )
		        );
		
		//首周付费率、首周付费数, 8天前
		SearchResponse weekPayRate_user = client.prepareSearch(index).setTypes(type_user).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
						FilterBuilders.andFilter(
						        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.weekFrom()).to(esUtilTest.weekTo()),
				        		FilterBuilders.termFilter("日志分类关键字", "create")
								))
						).execute().actionGet();
		
		SearchResponse weekPayRate_money = client.prepareSearch(index).setTypes(type_money).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
						FilterBuilders.andFilter(
								FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.weekFrom()).to(esUtilTest.nowDate()),
								FilterBuilders.termFilter("注册时间", esUtilTest.weekFrom().split("T")[0]),
						        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        		FilterBuilders.termFilter("是否首次充值", "1")
								))
						).execute().actionGet();
		
		Double week;
		if(weekPayRate_user.getHits().totalHits() == 0){
			week = 0.00;
		}else{
			week = (double)weekPayRate_money.getHits().totalHits()*100/weekPayRate_user.getHits().totalHits();
		}
		bulkRequest.add(client.prepareIndex(bulk_index_money, bulk_type_money_week)
		        .setSource(jsonBuilder()
			           	 .startObject()
	                        .field("date", esUtilTest.weekFrom().split("T")[0])
	                        .field("gameId", fb_game)
	                        .field("key", "all")
	                        .field("@timestamp", new Date())
	                        .field("ct", "weekday") //首周
	                        .field("paynum", weekPayRate_money.getHits().totalHits()) //首周付费数 去重
	                        .field("payofrate", df.format(week)) // 首周付费率
	                    .endObject()
		                  )
		        );
		
		//首月付费率、首月付费数,31天前
		SearchResponse mouthPayRate_user = client.prepareSearch(index).setTypes(type_user).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
						FilterBuilders.andFilter(
						        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.mouthFrom()).to(esUtilTest.mouthTo()),
				        		FilterBuilders.termFilter("日志分类关键字", "create")
								))
						).execute().actionGet();
		
		SearchResponse mouthPayRate_money = client.prepareSearch(index).setTypes(type_money).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
						FilterBuilders.andFilter(
								FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.mouthFrom()).to(esUtilTest.nowDate()),
								FilterBuilders.termFilter("注册时间", esUtilTest.mouthFrom().split("T")[0]),
						        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        		FilterBuilders.termFilter("是否首次充值", "1")
								))
						).execute().actionGet();
		Double mouth;
		if(mouthPayRate_user.getHits().totalHits() == 0){
			mouth = 0.00;
		}else{
			mouth = (double)mouthPayRate_money.getHits().totalHits()*100/mouthPayRate_user.getHits().totalHits();
		}
		bulkRequest.add(client.prepareIndex(bulk_index_money, bulk_type_money_mouth)
		        .setSource(jsonBuilder()
			           	 .startObject()
	                        .field("date", esUtilTest.mouthFrom().split("T")[0])
	                        .field("gameId", fb_game)
	                        .field("key", "all")
	                        .field("@timestamp", new Date())
	                        .field("ct", "mouthday") //首月
	                        .field("paynum", mouthPayRate_money.getHits().totalHits()) //首月付费数 去重
	                        .field("payofrate", df.format(mouth)) // 首月付费率
	                    .endObject()
		                  )
		        );
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}
	}	
	
	public void esServerZone() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'");
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数  
		//新增付费用户
		SearchResponse sr = client.prepareSearch(index).setTypes(type_money).setSearchType("count").setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get"),
		        		FilterBuilders.termFilter("是否首次充值", "1")
						))
		        ).addAggregation(
			    		AggregationBuilders.terms("serverZone").field("运营大区ID").size(10)
			    )
				.setSize(10).execute().actionGet();
		Terms genders = sr.getAggregations().get("serverZone");	
		for (Terms.Bucket e : genders.getBuckets()) {
			bulkRequest.add(client.prepareIndex(bulk_index_money, bulk_type_money_add)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", fb_game)
		                        .field("key", "serverZone")
		                        .field("value",e.getKey())
		                        .field("@timestamp", new Date())
		                        .field("newpayuser", e.getDocCount())
		                    .endObject()
			                  )
			        );
			System.out.println("昨天新增付费用户serverZone："+e.getDocCount()  +" " + e.getKey());
			//bulkRequest.execute().actionGet();
		}
		//累计付费用户
		IndicesExistsResponse responseindex = client.admin().indices().prepareExists(bulk_index_money).execute().actionGet(); 
		boolean ty = false;
		if(responseindex.isExists()){
			TypesExistsResponse responsetype = client.admin().indices() .prepareTypesExists(bulk_index_money).setTypes(bulk_type_money_all).execute().actionGet(); 
			if(responsetype.isExists()){
				ty = true;
			}
		}
		
		Map<String, Long> map = new HashMap<String, Long>();
		if(responseindex.isExists() && ty){//判断index是否存在 判断type是否存在
			System.out.println("存在");
			SearchResponse srTotal = client.prepareSearch(bulk_index_money).setTypes(bulk_type_money_all).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
					FilterBuilders.andFilter(
							FilterBuilders.termFilter("key", "serverZone"),
					        FilterBuilders.termFilter("date", esUtilTest.twoDayAgoFrom()))
			        )).execute().actionGet();
			for (SearchHit searchHit : srTotal.getHits()) {
				Map<String, Object> source = searchHit.getSource();
				long s = Long.valueOf(source.get("allpayuser").toString());
				map.put(source.get("value").toString(), s);
			}
			for (Terms.Bucket e : genders.getBuckets()) {
				if(map.containsKey(e.getKey())){
					map.put(e.getKey(), map.get(e.getKey())+e.getDocCount());
				}else{
					map.put(e.getKey(), e.getDocCount());
				}
			}
		}else{
			SearchResponse srTotal = client.prepareSearch(index).setTypes(type_money).setSearchType("count").setQuery(
					QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
							FilterBuilders.andFilter(
									FilterBuilders.rangeFilter("@timestamp").from("2014-01-11").to(esUtilTest.nowDate()),
							        FilterBuilders.termFilter("日志分类关键字", "money_get"),
					        		FilterBuilders.termFilter("是否首次充值", "1")
									))
							)
							.addAggregation(
						    		AggregationBuilders.terms("serverZone").field("运营大区ID").size(10)
						    )
							.setSize(10).execute().actionGet();
			Terms gendersTotal = srTotal.getAggregations().get("serverZone");
			for (Terms.Bucket entry : gendersTotal.getBuckets()) {
				map.put(entry.getKey(), entry.getDocCount());
			}
		}
		for(Entry<String,Long> entry : map.entrySet()){
			bulkRequest.add(client.prepareIndex(bulk_index_money, bulk_type_money_all)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", fb_game)
		                        .field("key", "serverZone")
		                        .field("value",entry.getKey())
		                        .field("@timestamp", new Date())
		                        .field("allpayuser", entry.getValue())
		                    .endObject()
			                  )
			        );
			System.out.println("历史累计用户serverZone："+entry.getValue().toString()  +"  " +entry.getKey());
		}
		
		//首日付费率、首日付费数
		SearchResponse dayPayRate_user = client.prepareSearch(index).setTypes(type_user).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
						FilterBuilders.andFilter(
						        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.dayFrom()).to(esUtilTest.nowDate()),
				        		FilterBuilders.termFilter("日志分类关键字", "create")
								))
						)
						.addAggregation(
					    		AggregationBuilders.terms("serverZone").field("运营大区ID").size(10)
							    )
						.setSize(10).execute().actionGet();
		Map<String, Long> dayusermap = new HashMap<String, Long>();
		Terms dayusergenders = dayPayRate_user.getAggregations().get("serverZone");	
		for (Terms.Bucket e : dayusergenders.getBuckets()) {
			dayusermap.put(e.getKey(), e.getDocCount());
		}
		
		SearchResponse dayPayRate_money = client.prepareSearch(index).setTypes(type_money).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
						FilterBuilders.andFilter(
								FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.dayFrom()).to(esUtilTest.nowDate()),
								FilterBuilders.termFilter("注册时间", esUtilTest.dayFrom().split("T")[0]),
						        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        		FilterBuilders.termFilter("是否首次充值", "1")
								))
						)
						.addAggregation(
					    		AggregationBuilders.terms("serverZone").field("运营大区ID").size(10)
							    )
						.setSize(10).execute().actionGet();
		Terms daymoneygenders = dayPayRate_money.getAggregations().get("serverZone");	
		for (Terms.Bucket e : daymoneygenders.getBuckets()) {
			Long all =  dayusermap.get(e.getKey());
			Long add = e.getDocCount();
			Double payofrate;
			if(all == null){
				payofrate = 0.00;
			}else{
				double al = all;
				double ad = add;
				payofrate = (ad*100/al);
			}
			bulkRequest.add(client.prepareIndex(bulk_index_money, bulk_type_money_day)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.dayFrom().split("T")[0])
		                        .field("gameId", fb_game)
		                        .field("key", "serverZone")
		                        .field("value", e.getKey())
		                        .field("@timestamp", new Date())
		                        .field("ct", "firstday") //首日
		                        .field("paynum", add) //首日付费数 去重
		                        .field("payofrate", df.format(payofrate)) // 首日付费率
		                    .endObject()
			                  )
			        );
		}
		
		//首周付费率、首周付费数, 8天前
		SearchResponse weekPayRate_user = client.prepareSearch(index).setTypes(type_user).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
						FilterBuilders.andFilter(
						        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.weekFrom()).to(esUtilTest.weekTo()),
				        		FilterBuilders.termFilter("日志分类关键字", "create")
								))
						)
						.addAggregation(
					    		AggregationBuilders.terms("serverZone").field("运营大区ID").size(10)
							    )
						.setSize(10).execute().actionGet();
		Map<String, Long> weekusermap = new HashMap<String, Long>();
		Terms weekusergenders = weekPayRate_user.getAggregations().get("serverZone");	
		for (Terms.Bucket e : weekusergenders.getBuckets()) {
			weekusermap.put(e.getKey(), e.getDocCount());
		}
		
		SearchResponse weekPayRate_money = client.prepareSearch(index).setTypes(type_money).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
						FilterBuilders.andFilter(
								FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.weekFrom()).to(esUtilTest.nowDate()),
								FilterBuilders.termFilter("注册时间", esUtilTest.weekFrom().split("T")[0]),
						        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        		FilterBuilders.termFilter("是否首次充值", "1")
								))
						)
						.addAggregation(
					    		AggregationBuilders.terms("serverZone").field("运营大区ID").size(10)
							    )
						.setSize(10).execute().actionGet();
		Terms weekmoneygenders = weekPayRate_money.getAggregations().get("serverZone");	
		for (Terms.Bucket e : weekmoneygenders.getBuckets()) {
			Long all =  weekusermap.get(e.getKey());
			Long add = e.getDocCount();
			Double payofrate;
			if(all == null){
				payofrate = 0.00;
			}else{
				double al = all;
				double ad = add;
				payofrate = (ad*100/al);
			}
			bulkRequest.add(client.prepareIndex(bulk_index_money, bulk_type_money_week)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.weekFrom().split("T")[0])
		                        .field("gameId", fb_game)
		                        .field("key", "serverZone")
		                        .field("value", e.getKey())
		                        .field("@timestamp", new Date())
		                        .field("ct", "weekday") //首周
		                        .field("paynum", add) //首周付费数 去重
		                        .field("payofrate", df.format(payofrate)) // 首周付费率
		                    .endObject()
			                  )
			        );
		}
		
		//首月付费率、首月付费数,31天前
		SearchResponse mouthPayRate_user = client.prepareSearch(index).setTypes(type_user).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
						FilterBuilders.andFilter(
						        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.mouthFrom()).to(esUtilTest.mouthTo()),
				        		FilterBuilders.termFilter("日志分类关键字", "create")
								))
						)
						.addAggregation(
					    		AggregationBuilders.terms("serverZone").field("运营大区ID").size(10)
							    )
						.setSize(10).execute().actionGet();
		Map<String, Long> mouthusermap = new HashMap<String, Long>();
		Terms mouthusergenders = mouthPayRate_user.getAggregations().get("serverZone");	
		for (Terms.Bucket e : mouthusergenders.getBuckets()) {
			mouthusermap.put(e.getKey(), e.getDocCount());
		}
		
		SearchResponse mouthPayRate_money = client.prepareSearch(index).setTypes(type_money).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
						FilterBuilders.andFilter(
								FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.mouthFrom()).to(esUtilTest.nowDate()),
								FilterBuilders.termFilter("注册时间", esUtilTest.mouthFrom().split("T")[0]),
						        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        		FilterBuilders.termFilter("是否首次充值", "1")
								))
						)
						.addAggregation(
					    		AggregationBuilders.terms("serverZone").field("运营大区ID").size(10)
							    )
						.setSize(10).execute().actionGet();
		Terms mouthmoneygenders = mouthPayRate_money.getAggregations().get("serverZone");	
		for (Terms.Bucket e : mouthmoneygenders.getBuckets()) {
			Long all =  mouthusermap.get(e.getKey());
			Long add = e.getDocCount();
			Double payofrate;
			if(all == null){
				payofrate = 0.00;
			}else{
				double al = all;
				double ad = add;
				payofrate = (ad*100/al);
			}
			bulkRequest.add(client.prepareIndex(bulk_index_money, bulk_type_money_mouth)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.mouthFrom().split("T")[0])
		                        .field("gameId", fb_game)
		                        .field("key", "serverZone")
		                        .field("value", e.getKey())
		                        .field("@timestamp", new Date())
		                        .field("ct", "mouthday") //首月
		                        .field("paynum", add) //首月付费数 去重
		                        .field("payofrate", df.format(payofrate)) // 首月付费率
		                    .endObject()
			                  )
			        );
		}
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}
	}
	
	public void esPlatForm() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'"); 
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数  
		//新增付费用户
		SearchResponse sr = client.prepareSearch(index).setTypes(type_money).setSearchType("count").setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get"),
		        		FilterBuilders.termFilter("是否首次充值", "1")
						))
		        ).addAggregation(
		        		AggregationBuilders.terms("platForm").field("渠道ID").size(300)
			    )
				.setSize(300).execute().actionGet();
		Terms genders = sr.getAggregations().get("platForm");	
		for (Terms.Bucket e : genders.getBuckets()) {
			bulkRequest.add(client.prepareIndex(bulk_index_money, bulk_type_money_add)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", fb_game)
		                        .field("key", "platForm")
		                        .field("value",e.getKey())
		                        .field("@timestamp", new Date())
		                        .field("newpayuser", e.getDocCount())
		                    .endObject()
			                  )
			        );
			System.out.println("昨天新增付费用户platForm："+e.getDocCount()  +" " + e.getKey());
		}
		//累计付费用户
		IndicesExistsResponse responseindex = client.admin().indices().prepareExists(bulk_index_money).execute().actionGet(); 
		boolean ty = false;
		if(responseindex.isExists()){
			TypesExistsResponse responsetype = client.admin().indices() .prepareTypesExists(bulk_index_money).setTypes(bulk_type_money_all).execute().actionGet(); 
			if(responsetype.isExists()){
				ty = true;
			}
		}
		Map<String, Long> map = new HashMap<String, Long>();
		if(responseindex.isExists() && ty){//判断index是否存在 判断type是否存在
			System.out.println("存在");
			SearchResponse srTotal = client.prepareSearch(bulk_index_money).setTypes(bulk_type_money_all).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
					FilterBuilders.andFilter(
							FilterBuilders.termFilter("key", "platForm"),
					        FilterBuilders.termFilter("date", esUtilTest.twoDayAgoFrom()))
			        )).execute().actionGet();
			for (SearchHit searchHit : srTotal.getHits()) {
				Map<String, Object> source = searchHit.getSource();
				long s = Long.valueOf(source.get("allpayuser").toString());
				map.put(source.get("value").toString(), s);
			}
			for (Terms.Bucket e : genders.getBuckets()) {
				if(map.containsKey(e.getKey())){
					map.put(e.getKey(), map.get(e.getKey())+e.getDocCount());
				}else{
					map.put(e.getKey(), e.getDocCount());
				}
			}
		}else{
			SearchResponse srTotal = client.prepareSearch(index).setTypes(type_money).setSearchType("count").setQuery(
					QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
							FilterBuilders.andFilter(
									FilterBuilders.rangeFilter("@timestamp").from("2014-01-11").to(esUtilTest.nowDate()),
							        FilterBuilders.termFilter("日志分类关键字", "money_get"),
					        		FilterBuilders.termFilter("是否首次充值", "1")
									))
							)
							.addAggregation(
									AggregationBuilders.terms("platForm").field("渠道ID").size(300)
						    )
							.setSize(300).execute().actionGet();
			Terms gendersTotal = srTotal.getAggregations().get("platForm");
			for (Terms.Bucket entry : gendersTotal.getBuckets()) {
				map.put(entry.getKey(), entry.getDocCount());
			}
		}
		for(Entry<String,Long> entry : map.entrySet()){
			bulkRequest.add(client.prepareIndex(bulk_index_money, bulk_type_money_all)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", fb_game)
		                        .field("key", "platForm")
		                        .field("value",entry.getKey())
		                        .field("@timestamp", new Date())
		                        .field("allpayuser", entry.getValue())
		                    .endObject()
			                  )
			        );
			System.out.println("历史累计用户platForm："+entry.getValue().toString()  +"  " +entry.getKey());
		}
		
		//首日付费率、首日付费数
		SearchResponse dayPayRate_user = client.prepareSearch(index).setTypes(type_user).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
						FilterBuilders.andFilter(
						        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.dayFrom()).to(esUtilTest.nowDate()),
				        		FilterBuilders.termFilter("日志分类关键字", "create")
								))
						)
						.addAggregation(
								AggregationBuilders.terms("platForm").field("渠道ID").size(300)
							    )
						.setSize(300).execute().actionGet();
		Map<String, Long> dayusermap = new HashMap<String, Long>();
		Terms dayusergenders = dayPayRate_user.getAggregations().get("platForm");	
		for (Terms.Bucket e : dayusergenders.getBuckets()) {
			dayusermap.put(e.getKey(), e.getDocCount());
		}
		
		SearchResponse dayPayRate_money = client.prepareSearch(index).setTypes(type_money).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
						FilterBuilders.andFilter(
								FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.dayFrom()).to(esUtilTest.nowDate()),
								FilterBuilders.termFilter("注册时间", esUtilTest.dayFrom().split("T")[0]),
						        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        		FilterBuilders.termFilter("是否首次充值", "1")
								))
						)
						.addAggregation(
								AggregationBuilders.terms("platForm").field("渠道ID").size(300)
							    )
						.setSize(300).execute().actionGet();
		Terms daymoneygenders = dayPayRate_money.getAggregations().get("platForm");	
		for (Terms.Bucket e : daymoneygenders.getBuckets()) {
			Long all =  dayusermap.get(e.getKey());
			Long add = e.getDocCount();
			Double payofrate;
			if(all == null){
				payofrate = 0.00;
			}else{
				double al = all;
				double ad = add;
				payofrate = (ad*100/al);
			}
			bulkRequest.add(client.prepareIndex(bulk_index_money, bulk_type_money_day)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.dayFrom().split("T")[0])
		                        .field("gameId", fb_game)
		                        .field("key", "platForm")
		                        .field("value", e.getKey())
		                        .field("@timestamp", new Date())
		                        .field("ct", "firstday") //首日
		                        .field("paynum", add) //首日付费数 去重
		                        .field("payofrate", df.format(payofrate)) // 首日付费率
		                    .endObject()
			                  )
			        );
		}
		
		//首周付费率、首周付费数, 8天前
		SearchResponse weekPayRate_user = client.prepareSearch(index).setTypes(type_user).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
						FilterBuilders.andFilter(
						        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.weekFrom()).to(esUtilTest.weekTo()),
				        		FilterBuilders.termFilter("日志分类关键字", "create")
								))
						)
						.addAggregation(
								AggregationBuilders.terms("platForm").field("渠道ID").size(300)
							    )
						.setSize(300).execute().actionGet();
		Map<String, Long> weekusermap = new HashMap<String, Long>();
		Terms weekusergenders = weekPayRate_user.getAggregations().get("platForm");	
		for (Terms.Bucket e : weekusergenders.getBuckets()) {
			weekusermap.put(e.getKey(), e.getDocCount());
		}
		
		SearchResponse weekPayRate_money = client.prepareSearch(index).setTypes(type_money).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
						FilterBuilders.andFilter(
								FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.weekFrom()).to(esUtilTest.nowDate()),
								FilterBuilders.termFilter("注册时间", esUtilTest.weekFrom().split("T")[0]),
						        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        		FilterBuilders.termFilter("是否首次充值", "1")
								))
						)
						.addAggregation(
								AggregationBuilders.terms("platForm").field("渠道ID").size(300)
							    )
						.setSize(300).execute().actionGet();
		Terms weekmoneygenders = weekPayRate_money.getAggregations().get("platForm");	
		for (Terms.Bucket e : weekmoneygenders.getBuckets()) {
			Long all =  weekusermap.get(e.getKey());
			Long add = e.getDocCount();
			Double payofrate;
			if(all == null){
				payofrate = 0.00;
			}else{
				double al = all;
				double ad = add;
				payofrate = (ad*100/al);
			}
			bulkRequest.add(client.prepareIndex(bulk_index_money, bulk_type_money_week)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.weekFrom().split("T")[0])
		                        .field("gameId", fb_game)
		                        .field("key", "platForm")
		                        .field("value", e.getKey())
		                        .field("@timestamp", new Date())
		                        .field("ct", "weekday") //首周
		                        .field("paynum", add) //首周付费数 去重
		                        .field("payofrate", df.format(payofrate)) // 首周付费率
		                    .endObject()
			                  )
			        );
		}
		
		//首月付费率、首月付费数,31天前
		SearchResponse mouthPayRate_user = client.prepareSearch(index).setTypes(type_user).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
						FilterBuilders.andFilter(
						        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.mouthFrom()).to(esUtilTest.mouthTo()),
				        		FilterBuilders.termFilter("日志分类关键字", "create")
								))
						)
						.addAggregation(
								AggregationBuilders.terms("platForm").field("渠道ID").size(300)
							    )
						.setSize(300).execute().actionGet();
		Map<String, Long> mouthusermap = new HashMap<String, Long>();
		Terms mouthusergenders = mouthPayRate_user.getAggregations().get("platForm");	
		for (Terms.Bucket e : mouthusergenders.getBuckets()) {
			mouthusermap.put(e.getKey(), e.getDocCount());
		}
		
		SearchResponse mouthPayRate_money = client.prepareSearch(index).setTypes(type_money).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
						FilterBuilders.andFilter(
								FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.mouthFrom()).to(esUtilTest.nowDate()),
								FilterBuilders.termFilter("注册时间", esUtilTest.mouthFrom().split("T")[0]),
						        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        		FilterBuilders.termFilter("是否首次充值", "1")
								))
						)
						.addAggregation(
								AggregationBuilders.terms("platForm").field("渠道ID").size(300)
							    )
						.setSize(300).execute().actionGet();
		Terms mouthmoneygenders = mouthPayRate_money.getAggregations().get("platForm");	
		for (Terms.Bucket e : mouthmoneygenders.getBuckets()) {
			Long all =  mouthusermap.get(e.getKey());
			Long add = e.getDocCount();
			Double payofrate;
			if(all == null){
				payofrate = 0.00;
			}else{
				double al = all;
				double ad = add;
				payofrate = (ad*100/al);
			}
			bulkRequest.add(client.prepareIndex(bulk_index_money, bulk_type_money_mouth)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.mouthFrom().split("T")[0])
		                        .field("gameId", fb_game)
		                        .field("key", "platForm")
		                        .field("value", e.getKey())
		                        .field("@timestamp", new Date())
		                        .field("ct", "mouthday") //首月
		                        .field("paynum", add) //首月付费数 去重
		                        .field("payofrate", df.format(payofrate)) // 首月付费率
		                    .endObject()
			                  )
			        );
		}
		
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}
	}
	
	public void esServer() throws IOException, ParseException {	
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'"); 
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数  
		//新增付费用户
		SearchResponse sr = client.prepareSearch(index).setTypes(type_money).setSearchType("count").setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
				FilterBuilders.andFilter(
						FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.oneDayAgoFrom()).to(esUtilTest.nowDate()),
				        FilterBuilders.termFilter("日志分类关键字", "money_get"),
		        		FilterBuilders.termFilter("是否首次充值", "1")
						))
		        ).addAggregation(
		        		AggregationBuilders.terms("server").field("服务器ID").size(300)
			    )
				.setSize(300).execute().actionGet();
		Terms genders = sr.getAggregations().get("server");	
		for (Terms.Bucket e : genders.getBuckets()) {
			bulkRequest.add(client.prepareIndex(bulk_index_money, bulk_type_money_add)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", fb_game)
		                        .field("key", "server")
		                        .field("value",e.getKey())
		                        .field("@timestamp", new Date())
		                        .field("newpayuser", e.getDocCount())
		                    .endObject()
			                  )
			        );
			System.out.println("昨天新增付费用户server："+e.getDocCount()  +" " + e.getKey());
		}
		//累计付费用户
		IndicesExistsResponse responseindex = client.admin().indices().prepareExists(bulk_index_money).execute().actionGet(); 
		boolean ty = false;
		if(responseindex.isExists()){
			TypesExistsResponse responsetype = client.admin().indices() .prepareTypesExists(bulk_index_money).setTypes(bulk_type_money_all).execute().actionGet(); 
			if(responsetype.isExists()){
				ty = true;
			}
		}
		Map<String, Long> map = new HashMap<String, Long>();
		if(responseindex.isExists() && ty){//判断index是否存在 判断type是否存在
			System.out.println("存在");
			SearchResponse srTotal = client.prepareSearch(bulk_index_money).setTypes(bulk_type_money_all).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
					FilterBuilders.andFilter(
							FilterBuilders.termFilter("key", "server"),
					        FilterBuilders.termFilter("date", esUtilTest.twoDayAgoFrom()))
			        )).execute().actionGet();
			for (SearchHit searchHit : srTotal.getHits()) {
				Map<String, Object> source = searchHit.getSource();
				long s = Long.valueOf(source.get("allpayuser").toString());
				map.put(source.get("value").toString(), s);
			}
			for (Terms.Bucket e : genders.getBuckets()) {
				if(map.containsKey(e.getKey())){
					map.put(e.getKey(), map.get(e.getKey())+e.getDocCount());
				}else{
					map.put(e.getKey(), e.getDocCount());
				}
			}
		}else{
			SearchResponse srTotal = client.prepareSearch(index).setTypes(type_money).setSearchType("count").setQuery(
					QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
							FilterBuilders.andFilter(
									FilterBuilders.rangeFilter("@timestamp").from("2014-01-11").to(esUtilTest.nowDate()),
							        FilterBuilders.termFilter("日志分类关键字", "money_get"),
					        		FilterBuilders.termFilter("是否首次充值", "1")
									))
							)
							.addAggregation(
									AggregationBuilders.terms("server").field("服务器ID").size(300)
						    )
							.setSize(300).execute().actionGet();
			Terms gendersTotal = srTotal.getAggregations().get("server");
			for (Terms.Bucket entry : gendersTotal.getBuckets()) {
				map.put(entry.getKey(), entry.getDocCount());
			}
		}
		for(Entry<String,Long> entry : map.entrySet()){
			bulkRequest.add(client.prepareIndex(bulk_index_money, bulk_type_money_all)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.oneDayAgoFrom().split("T")[0])
		                        .field("gameId", fb_game)
		                        .field("key", "server")
		                        .field("value",entry.getKey())
		                        .field("@timestamp", new Date())
		                        .field("allpayuser", entry.getValue())
		                    .endObject()
			                  )
			        );
			System.out.println("历史累计用户server："+entry.getValue().toString()  +"  " +entry.getKey());
		}
		
		//首日付费率、首日付费数
		SearchResponse dayPayRate_user = client.prepareSearch(index).setTypes(type_user).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
						FilterBuilders.andFilter(
						        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.dayFrom()).to(esUtilTest.nowDate()),
				        		FilterBuilders.termFilter("日志分类关键字", "create")
								))
						)
						.addAggregation(
								AggregationBuilders.terms("server").field("服务器ID").size(300)
							    )
						.setSize(300).execute().actionGet();
		Map<String, Long> dayusermap = new HashMap<String, Long>();
		Terms dayusergenders = dayPayRate_user.getAggregations().get("server");	
		for (Terms.Bucket e : dayusergenders.getBuckets()) {
			dayusermap.put(e.getKey(), e.getDocCount());
		}
		
		SearchResponse dayPayRate_money = client.prepareSearch(index).setTypes(type_money).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
						FilterBuilders.andFilter(
								FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.dayFrom()).to(esUtilTest.nowDate()),
								FilterBuilders.termFilter("注册时间", esUtilTest.dayFrom().split("T")[0]),
						        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        		FilterBuilders.termFilter("是否首次充值", "1")
								))
						)
						.addAggregation(
								AggregationBuilders.terms("server").field("服务器ID").size(300)
							    )
						.setSize(300).execute().actionGet();
		Terms daymoneygenders = dayPayRate_money.getAggregations().get("server");	
		for (Terms.Bucket e : daymoneygenders.getBuckets()) {
			Long all =  dayusermap.get(e.getKey());
			Long add = e.getDocCount();
			Double payofrate;
			if(all == null){
				payofrate = 0.00;
			}else{
				double al = all;
				double ad = add;
				payofrate = (ad*100/al);
			}
			bulkRequest.add(client.prepareIndex(bulk_index_money, bulk_type_money_day)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.dayFrom().split("T")[0])
		                        .field("gameId", fb_game)
		                        .field("key", "server")
		                        .field("value", e.getKey())
		                        .field("@timestamp", new Date())
		                        .field("ct", "firstday") //首日
		                        .field("paynum", add) //首日付费数 去重
		                        .field("payofrate", df.format(payofrate)) // 首日付费率
		                    .endObject()
			                  )
			        );
		}
		
		//首周付费率、首周付费数, 8天前
		SearchResponse weekPayRate_user = client.prepareSearch(index).setTypes(type_user).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
						FilterBuilders.andFilter(
						        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.weekFrom()).to(esUtilTest.weekTo()),
				        		FilterBuilders.termFilter("日志分类关键字", "create")
								))
						)
						.addAggregation(
								AggregationBuilders.terms("server").field("服务器ID").size(300)
							    )
						.setSize(300).execute().actionGet();
		Map<String, Long> weekusermap = new HashMap<String, Long>();
		Terms weekusergenders = weekPayRate_user.getAggregations().get("server");	
		for (Terms.Bucket e : weekusergenders.getBuckets()) {
			weekusermap.put(e.getKey(), e.getDocCount());
		}
		
		SearchResponse weekPayRate_money = client.prepareSearch(index).setTypes(type_money).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
						FilterBuilders.andFilter(
								FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.weekFrom()).to(esUtilTest.nowDate()),
								FilterBuilders.termFilter("注册时间", esUtilTest.weekFrom().split("T")[0]),
						        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        		FilterBuilders.termFilter("是否首次充值", "1")
								))
						)
						.addAggregation(
								AggregationBuilders.terms("server").field("服务器ID").size(300)
							    )
						.setSize(300).execute().actionGet();
		Terms weekmoneygenders = weekPayRate_money.getAggregations().get("server");	
		for (Terms.Bucket e : weekmoneygenders.getBuckets()) {
			Long all =  weekusermap.get(e.getKey());
			Long add = e.getDocCount();
			Double payofrate;
			if(all == null){
				payofrate = 0.00;
			}else{
				double al = all;
				double ad = add;
				payofrate = (ad*100/al);
			}
			bulkRequest.add(client.prepareIndex(bulk_index_money, bulk_type_money_week)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.weekFrom().split("T")[0])
		                        .field("gameId", fb_game)
		                        .field("key", "server")
		                        .field("value", e.getKey())
		                        .field("@timestamp", new Date())
		                        .field("ct", "weekday") //首周
		                        .field("paynum", add) //首周付费数 去重
		                        .field("payofrate", df.format(payofrate)) // 首周付费率
		                    .endObject()
			                  )
			        );
		}
		
		//首月付费率、首月付费数,31天前
		SearchResponse mouthPayRate_user = client.prepareSearch(index).setTypes(type_user).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
						FilterBuilders.andFilter(
						        FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.mouthFrom()).to(esUtilTest.mouthTo()),
				        		FilterBuilders.termFilter("日志分类关键字", "create")
								))
						)
						.addAggregation(
								AggregationBuilders.terms("server").field("服务器ID").size(300)
							    )
						.setSize(300).execute().actionGet();
		Map<String, Long> mouthusermap = new HashMap<String, Long>();
		Terms mouthusergenders = mouthPayRate_user.getAggregations().get("server");	
		for (Terms.Bucket e : mouthusergenders.getBuckets()) {
			mouthusermap.put(e.getKey(), e.getDocCount());
		}
		
		SearchResponse mouthPayRate_money = client.prepareSearch(index).setTypes(type_money).setSearchType("count").setQuery(
				QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(),
						FilterBuilders.andFilter(
								FilterBuilders.rangeFilter("@timestamp").from(esUtilTest.mouthFrom()).to(esUtilTest.nowDate()),
								FilterBuilders.termFilter("注册时间", esUtilTest.mouthFrom().split("T")[0]),
						        FilterBuilders.termFilter("日志分类关键字", "money_get"),
				        		FilterBuilders.termFilter("是否首次充值", "1")
								))
						)
						.addAggregation(
								AggregationBuilders.terms("server").field("服务器ID").size(300)
							    )
						.setSize(300).execute().actionGet();
		Terms mouthmoneygenders = mouthPayRate_money.getAggregations().get("server");	
		for (Terms.Bucket e : mouthmoneygenders.getBuckets()) {
			Long all =  mouthusermap.get(e.getKey());
			Long add = e.getDocCount();
			Double payofrate;
			if(all == null){
				payofrate = 0.00;
			}else{
				double al = all;
				double ad = add;
				payofrate = (ad*100/al);
			}
			bulkRequest.add(client.prepareIndex(bulk_index_money, bulk_type_money_mouth)
			        .setSource(jsonBuilder()
				           	 .startObject()
		                        .field("date", esUtilTest.mouthFrom().split("T")[0])
		                        .field("gameId", fb_game)
		                        .field("key", "server")
		                        .field("value", e.getKey())
		                        .field("@timestamp", new Date())
		                        .field("ct", "mouthday") //首月
		                        .field("paynum", add) //首月付费数 去重
		                        .field("payofrate", df.format(payofrate)) // 首月付费率
		                    .endObject()
			                  )
			        );
		}
		if(bulkRequest.numberOfActions()!=0){
			bulkRequest.execute().actionGet();	
		}
	}
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void schedual() throws Exception{
		System.out.println("----------------用户 付费率 开始");
		esAll();
		esServerZone();
		esPlatForm();
		esServer();
		System.out.println("----------------用户 付费率 调度结束");
	}
	

}
