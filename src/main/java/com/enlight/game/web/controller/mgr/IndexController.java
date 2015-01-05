package com.enlight.game.web.controller.mgr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 
 * @author hyl
 * 登入后首页跳转控制
 *
 */
@Controller("indexController")
@RequestMapping("/manage/")
public class IndexController {
	
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
	
     /**
      * 首页跳转
      * @return
      */
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index()
	{
		return "index";
	}

}
