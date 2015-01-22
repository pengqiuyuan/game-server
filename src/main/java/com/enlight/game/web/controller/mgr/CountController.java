package com.enlight.game.web.controller.mgr;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;


/**
 * 数据统计
 * @author dell
 *
 */
@Controller("CountController")
@RequestMapping("/manage/count")
public class CountController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(CountController.class);
	
	/**
	 * 道具日志
	 * @return
	 */
	@RequestMapping(value = "/item", method = RequestMethod.GET)
	public String item(@RequestParam(value = "id")long id){
		logger.debug("item coming...");
		ShiroUser shiroUser = getCurrentUserName();
		String url = null;
		if(id == 1){
			url = "redirect:/kibana/index.html#/dashboard/file/item.json"; 
		}else if(id == 2){
			url = "redirect:/kibana/index.html#/dashboard/file/itemGet.json"; 
		}else if(id == 3){
			url = "redirect:/kibana/index.html#/dashboard/file/itemCost.json"; 
		}
		return url;
	}	
	
	/**
	 * 体力日志
	 * @return
	 */
	@RequestMapping(value = "/ap", method = RequestMethod.GET)
	public String ap(@RequestParam(value = "id")long id){
		logger.debug("ap coming...");
		String url = null;
		if(id == 1){
			url = "redirect:/kibana/index.html#/dashboard/file/ap.json"; 
		}else if(id == 2){
			url = "redirect:/kibana/index.html#/dashboard/file/apGet.json"; 
		}else if(id == 3){
			url = "redirect:/kibana/index.html#/dashboard/file/apCost.json"; 
		}
		return url;
	}	
	
	/**
	 * 真实充值币日志
	 * @return
	 */
	@RequestMapping(value = "/money", method = RequestMethod.GET)
	public String money(@RequestParam(value = "id")long id){
		logger.debug("money coming...");
		String url = null;
		if(id == 1){
			url = "redirect:/kibana/index.html#/dashboard/file/money.json"; 
		}else if(id == 2){
			url = "redirect:/kibana/index.html#/dashboard/file/moneyGet.json"; 
		}else if(id == 3){
			url = "redirect:/kibana/index.html#/dashboard/file/moneyCost.json"; 
		}
		return url;
	}	
	
	/**
	 * 虚拟充值币日志
	 * @return
	 */
	@RequestMapping(value = "/dummy", method = RequestMethod.GET)
	public String dummy(@RequestParam(value="id")long id){
		logger.debug("dummy coming...");
		String url = null;
		if(id == 1){
			url = "redirect:/kibana/index.html#/dashboard/file/dummy.json"; 
		}else if(id == 2){
			url = "redirect:/kibana/index.html#/dashboard/file/dummyGet.json"; 
		}else if(id == 3){
			url = "redirect:/kibana/index.html#/dashboard/file/dummyCost.json"; 
		}
		return url;
	}	
	
	/**
	 * 游戏币日志
	 * @return
	 */
	@RequestMapping(value = "/coin", method = RequestMethod.GET)
	public String coin(@RequestParam(value="id")long id){
		logger.debug("coin coming...");
		String url = null;
		if(id == 1){
			url = "redirect:/kibana/index.html#/dashboard/file/coin.json"; 
		}else if(id == 2){
			url = "redirect:/kibana/index.html#/dashboard/file/coinGet.json"; 
		}else if(id == 3){
			url = "redirect:/kibana/index.html#/dashboard/file/coinCost.json"; 
		}
		return url;
	}	
	
	/**
	 * 竞技场徽章(货币)日志
	 * @return
	 */
	@RequestMapping(value = "/arenacoin", method = RequestMethod.GET)
	public String arenacoin(@RequestParam(value="id")long id){
		logger.debug("arenacoin coming...");
		String url = null;
		if(id == 1){
			url = "redirect:/kibana/index.html#/dashboard/file/arenacoin.json"; 
		}else if(id == 2){
			url = "redirect:/kibana/index.html#/dashboard/file/arenacoinGet.json"; 
		}else if(id == 3){
			url = "redirect:/kibana/index.html#/dashboard/file/arenacoinCost.json"; 
		}
		return url;
	}	
	
	/**
	 * 燃烧远征龙鳞币(货币)日志
	 * @return
	 */
	@RequestMapping(value = "/expeditioncoin", method = RequestMethod.GET)
	public String expeditioncoin(@RequestParam(value="id")long id){
		logger.debug("expeditioncoin coming...");
		String url = null;
		if(id == 1){
			url = "redirect:/kibana/index.html#/dashboard/file/expeditioncoin.json"; 
		}else if(id == 2){
			url = "redirect:/kibana/index.html#/dashboard/file/expeditioncoinGet.json"; 
		}else if(id == 3){
			url = "redirect:/kibana/index.html#/dashboard/file/expeditioncoinCost.json"; 
		}
		return url;
	}	
	
	/**
	 * 用户日志
	 * @return
	 */
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public String user(@RequestParam(value="id")long id){
		logger.debug("user coming...");
		String url = null;
		if(id == 1){
			url = "redirect:/kibana/index.html#/dashboard/file/user.json"; 
		}else if(id == 2){
			url = "redirect:/kibana/index.html#/dashboard/file/userLogin.json"; 
		}else if(id == 3){
			url = "redirect:/kibana/index.html#/dashboard/file/userCreate.json"; 
		}else if(id == 4){
			url = "redirect:/kibana/index.html#/dashboard/file/userOnline.json"; 
		}
		return url;
	}	
	
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	public ShiroUser getCurrentUserName() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user;
	}

}
