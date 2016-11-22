package com.enlight.game.es;


import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

import com.enlight.game.entity.analysis.UserRetained;
import com.enlight.game.schedual.xyj.XyjRetainedScheduled;
import com.enlight.game.service.es.RetainedServer;
import com.enlight.game.util.EsUtil;
import com.google.common.collect.Maps;



@ContextConfiguration(locations = {"/applicationContext.xml"})
public class EsTest2 extends SpringTransactionalTestCase{
	@Autowired
	public Client client;
	
	private static final String index = "log_xyj_user";
	
	private static final String type = "xyj_user_retained";
	
	@Autowired
	private RetainedServer retainedServer;
	
	@Test
	public void esAll() throws IOException, ParseException {	
		retainedServer.searchAllRetained(index, type, "2016-11-07", "2016-11-22","1");
	}
	
}
