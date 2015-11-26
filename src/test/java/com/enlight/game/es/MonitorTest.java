package com.enlight.game.es;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.codehaus.jackson.type.TypeReference;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

import com.enlight.game.entity.gm.fb.ServerStatus;
import com.enlight.game.util.HttpClientUts;
import com.enlight.game.util.JsonBinder;



@ContextConfiguration(locations = {"/applicationContext.xml"})
public class MonitorTest extends SpringTransactionalTestCase{
	
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	
	@Autowired
	private Client client;
	
	//@Test
	public void test13() throws IOException, ParseException {	
		
	}
	
	
}
