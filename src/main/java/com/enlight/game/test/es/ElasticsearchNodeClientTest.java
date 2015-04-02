package com.enlight.game.test.es;

import static org.junit.Assert.assertNotNull;

import org.elasticsearch.client.Client;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

/**
 * 测试Elasticsearch 连接
 * @author pengqiuyuan
 * @ContextConfiguration(locations = {"/applicationContext.xml", "/application_es.xml" })
 *
 */
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class ElasticsearchNodeClientTest extends SpringTransactionalTestCase{
	@Autowired
	private Client client;
	

	@Test
	public void testStart() {
		assertNotNull(client);
		System.out.println(client);
		System.out.println("11111111");
	}

}
