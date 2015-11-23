package com.enlight.game.schedual;

import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enlight.game.util.email.MimeMailService;
import com.enlight.game.util.email.SimpleMailService;


@Transactional(readOnly = true)
public class GameValueScheduled {
	
	@Autowired
	public Client client;
	
	@Autowired
	private SimpleMailService simpleMailService;
	
	@Autowired
	private MimeMailService mimeMailService;
	
	private static final Logger logger = LoggerFactory.getLogger(GameValueScheduled.class);
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void schedual() throws Exception{
		//simpleMailService.sendNotificationMail("彭秋源");
		
		//mimeMailService.sendNotificationMail("彭秋源");
		
		logger.debug("----------------调度开始");
		try {
			logger.debug("111111111");
			//esAll();
		} catch (Exception e) {
			// TODO: handle exception
			logger.debug("计算失败");
		}
		logger.debug("----------------调度结束");
	}
	

}
