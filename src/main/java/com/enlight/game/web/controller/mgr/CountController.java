package com.enlight.game.web.controller.mgr;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.enlight.game.entity.EnumFunction;
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
	@RequiresRoles(value = { "admin", "22" }, logical = Logical.OR)
	@RequestMapping(value = "/item", method = RequestMethod.GET)
	public String item(@RequestParam(value = "id")String id,Model model){
		logger.debug("item coming..."+id);
		model.addAttribute("user", EnumFunction.ENUM_ITEM);
		return "/kibana/index";
	}	
	
	/**
	 * 体力日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "21" }, logical = Logical.OR)
	@RequestMapping(value = "/ap", method = RequestMethod.GET)
	public String ap(@RequestParam(value = "id")long id,Model model){
		logger.debug("ap coming...");
		model.addAttribute("user", EnumFunction.ENUM_AP);
		return "/kibana/index";
	}	
	
	/**
	 * 真实充值币日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "23" }, logical = Logical.OR)
	@RequestMapping(value = "/money", method = RequestMethod.GET)
	public String money(@RequestParam(value = "id")long id,Model model){
		logger.debug("money coming...");
		model.addAttribute("user", EnumFunction.ENUM_MONEY);
		return "/kibana/index";
	}	
	
	/**
	 * 虚拟充值币日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "24" }, logical = Logical.OR)
	@RequestMapping(value = "/dummy", method = RequestMethod.GET)
	public String dummy(@RequestParam(value="id")long id,Model model){
		logger.debug("dummy coming...");
		model.addAttribute("user", EnumFunction.ENUM_DUMMY);
		return "/kibana/index";
	}	
	
	/**
	 * 游戏币日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "25" }, logical = Logical.OR)
	@RequestMapping(value = "/coin", method = RequestMethod.GET)
	public String coin(@RequestParam(value="id")long id,Model model){
		logger.debug("coin coming...");
		model.addAttribute("user", EnumFunction.ENUM_COIN);
		return "/kibana/index";
	}	
	
	/**
	 * 竞技场徽章(货币)日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "26" }, logical = Logical.OR)
	@RequestMapping(value = "/arenacoin", method = RequestMethod.GET)
	public String arenacoin(@RequestParam(value="id")long id,Model model){
		logger.debug("arenacoin coming...");
		model.addAttribute("user", EnumFunction.ENUM_ARENACOIN);
		return "/kibana/index";
	}	
	
	/**
	 * 燃烧远征龙鳞币(货币)日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "27" }, logical = Logical.OR)
	@RequestMapping(value = "/expeditioncoin", method = RequestMethod.GET)
	public String expeditioncoin(@RequestParam(value="id")long id,Model model){
		logger.debug("expeditioncoin coming...");
		model.addAttribute("user", EnumFunction.ENUM_EXPEDITIONCOIN);
		return "/kibana/index";
	}	
	
	/**
	 * 用户日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "28" }, logical = Logical.OR)
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public String user(@RequestParam(value="id")long id,Model model){
		logger.debug("user coming...");
		model.addAttribute("user", EnumFunction.ENUM_USER);
		return "/kibana/index";
	}	
	
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	public ShiroUser getCurrentUserName() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user;
	}

}
