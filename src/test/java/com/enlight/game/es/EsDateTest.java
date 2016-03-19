package com.enlight.game.es;

import java.io.IOException;

import org.elasticsearch.client.Client;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

import com.enlight.game.service.serverZone.ServerZoneService;
import com.enlight.game.util.EsUtil;

@ContextConfiguration(locations = {"/applicationContext.xml"})
public class EsDateTest extends SpringTransactionalTestCase{
	@Autowired
	private Client client;
	
	@Autowired
	private ServerZoneService serverZoneService;
	
	EsUtil esUtilTest = new EsUtil();
	
	@Test
	public void test13() throws IOException {	
		System.out.println("111111 oneDayAgoFrom  "+esUtilTest.oneDayAgoFrom());
		System.out.println("111111 twoDayAgoFrom  "+esUtilTest.twoDayAgoFrom());
		System.out.println("111111  twoDayAgoTo "+esUtilTest.twoDayAgoTo());
		System.out.println("111111  eightDayAgoFrom "+esUtilTest.eightDayAgoFrom());
		System.out.println("111111  eightDayAgoTo "+esUtilTest.eightDayAgoTo());
		System.out.println("111111  thirtyOneDayAgoFrom "+esUtilTest.thirtyOneDayAgoFrom());
		System.out.println("111111  thirtyOneDayAgoTo "+esUtilTest.thirtyOneDayAgoTo());
		System.out.println("222222  dayFrom "+esUtilTest.dayFrom());
		System.out.println("222222  weekFrom "+esUtilTest.weekFrom());
		System.out.println("222222 weekTo  "+esUtilTest.weekTo());
		System.out.println("222222  mouthFrom "+esUtilTest.mouthFrom());
		System.out.println("222222 mouthTo  "+esUtilTest.mouthTo());
		System.out.println("333333  presentfirstday "+esUtilTest.presentfirstday());
		System.out.println("333333  lastmouthfirstday "+esUtilTest.lastmouthfirstday());
		System.out.println("333333  lastmouth "+esUtilTest.lastmouth());
		System.out.println("333333  nowDate1 "+esUtilTest.nowDate1());
		System.out.println("333333  fiveMinuteAgoFrom "+esUtilTest.fiveMinuteAgoFrom());
	}
	
}
