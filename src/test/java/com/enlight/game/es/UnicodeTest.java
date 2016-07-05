package com.enlight.game.es;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;

import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

import com.enlight.game.util.JsonBinder;



@ContextConfiguration(locations = {"/applicationContext.xml"})
public class UnicodeTest extends SpringTransactionalTestCase{
	
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	
	@Test
	public void test0() throws IOException, ParseException {	
		System.out.println("11111");
		String sJava="\u0048\u0065\u006C";
		System.out.println("StringEscapeUtils.unescapeJava(sJava):\n" + StringEscapeUtils.unescapeJava(sJava));
		System.out.println("--------------------------");
		String sJava2="\u00E5\u008F\u0091\u00E8\u00B4\u00A7\u00E7\u009A\u0084\u00E8\u00B4\u00B9\u00E5\u0093\u0088\u00E5\u00B8\u0082\u00E7\u009A\u0084\u00E9\u00A3\u008E\u00E6\u00A0\u00BC";
		System.out.println("StringEscapeUtils.unescapeJava(sJava):\n" + StringEscapeUtils.unescapeJava(sJava2));
		
		System.out.println("44444   "  + URLEncoder.encode(sJava2, "gbk"));
	}


	
}
