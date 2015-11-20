package com.enlight.game.es;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.elasticsearch.client.Client;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

import com.enlight.game.entity.analysis.EsUserTotal;
import com.enlight.game.service.analysis.EsUserTotalService;



@ContextConfiguration(locations = {"/applicationContext.xml"})
public class EsUserTotalTest extends SpringTransactionalTestCase{
	@Autowired
	private Client client;
	
	@Autowired
	private EsUserTotalService esUserTotalService;
	
	
	private static final Long gameId = 1L;

	@Test
	@Rollback(false)
	public void test13() throws IOException, ParseException {	
		
		
	    Date currentTime = new Date();  
	    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");  
	    System.out.println("0000000  "  + format1);
	    String dateString = format1.format(currentTime);  
	    System.out.println("ttt  "   + dateString);
	    Date date = format1.parse(dateString);
	    System.out.println("aaaa  "  + date);
	    
	 
	    
	    
		EsUserTotal esUserTotal = esUserTotalService.findByGameIdAndTotalDate(2l, date);
		
		if(esUserTotal == null){
			System.out.println("1111111   不存在");
			EsUserTotal esUserTotal2 = new EsUserTotal();
			esUserTotal2.setTotalDate(date);
			esUserTotal2.setGameId(2l);
			esUserTotalService.save(esUserTotal2);
		}else{
			System.out.println("222222222   存在" );
			esUserTotal.setTotalDate(date);
			esUserTotal.setGameId(266666l);
			esUserTotalService.update(esUserTotal);
		}
		
		
		

	}

	
}
