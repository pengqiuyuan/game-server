package com.enlight.game.web.controller.mgr;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.enlight.game.entity.EnumFunction;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;
import com.enlight.game.service.platForm.PlatFormService;
import com.enlight.game.service.serverZone.ServerZoneService;
import com.enlight.game.web.controller.mgr.BaseController;


/**
 * 数据统计
 * @author dell
 *
 */
@Controller("CountController")
@RequestMapping("/manage/count")
public class CountController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(CountController.class);
	
	private static final String GAME_Fb = "fb";
	
	private static final String GAME_KUN = "kun";
	
	private static final String GAME_KDS = "kds";
	
	@Autowired
	private ServerZoneService serverZoneService;
	
	@Autowired
	private PlatFormService platFormService;
	
	/**
	 * 道具日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "FB_ITEM" }, logical = Logical.OR)
	@RequestMapping(value = "/fbItem", method = RequestMethod.GET)
	public String fbItem(@RequestParam(value = "id")String id,Model model){
		logger.debug("item coming..."+id);
		model.addAttribute("user", EnumFunction.ENUM_ITEM);
		model.addAttribute("game", GAME_Fb);
		return "/kibana/fb/index";
	}	
	
	/**
	 * 体力日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "FB_AP" }, logical = Logical.OR)
	@RequestMapping(value = "/fbAp", method = RequestMethod.GET)
	public String fbAp(@RequestParam(value = "id")long id,Model model){
		logger.debug("ap coming...");
		model.addAttribute("user", EnumFunction.ENUM_AP);
		model.addAttribute("game", GAME_Fb);
		return "/kibana/fb/index";
	}	
	
	/**
	 * 真实充值币日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "FB_MONEY" }, logical = Logical.OR)
	@RequestMapping(value = "/fbMoney", method = RequestMethod.GET)
	public String fbMoney(@RequestParam(value = "id")long id,Model model){
		logger.debug("money coming...");
		model.addAttribute("user", EnumFunction.ENUM_MONEY);
		model.addAttribute("game", GAME_Fb);
		return "/kibana/fb/index";
	}	
	
	/**
	 * 虚拟充值币日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "FB_DUMMY" }, logical = Logical.OR)
	@RequestMapping(value = "/fbDummy", method = RequestMethod.GET)
	public String fbDummy(@RequestParam(value="id")long id,Model model){
		logger.debug("dummy coming...");
		model.addAttribute("user", EnumFunction.ENUM_DUMMY);
		model.addAttribute("game", GAME_Fb);
		return "/kibana/fb/index";
	}	
	
	/**
	 * 游戏币日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "FB_COIN" }, logical = Logical.OR)
	@RequestMapping(value = "/fbCoin", method = RequestMethod.GET)
	public String fbCoin(@RequestParam(value="id")long id,Model model){
		logger.debug("coin coming...");
		model.addAttribute("user", EnumFunction.ENUM_COIN);
		model.addAttribute("game", GAME_Fb);
		return "/kibana/fb/index";
	}	
	
	/**
	 * 竞技场徽章(货币)日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "FB_ARENACOIN" }, logical = Logical.OR)
	@RequestMapping(value = "/fbArenacoin", method = RequestMethod.GET)
	public String fbArenacoin(@RequestParam(value="id")long id,Model model){
		logger.debug("arenacoin coming...");
		model.addAttribute("user", EnumFunction.ENUM_ARENACOIN);
		model.addAttribute("game", GAME_Fb);
		return "/kibana/fb/index";
	}	
	
	/**
	 * 燃烧远征龙鳞币(货币)日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "FB_EXPEDITIONC" }, logical = Logical.OR)
	@RequestMapping(value = "/fbExpeditioncoin", method = RequestMethod.GET)
	public String fbExpeditioncoin(@RequestParam(value="id")long id,Model model){
		logger.debug("expeditioncoin coming...");
		model.addAttribute("user", EnumFunction.ENUM_EXPEDITIONCOIN);
		model.addAttribute("game", GAME_Fb);
		return "/kibana/fb/index";
	}	
	  
	/**
	 * 用户日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "FB_USER" }, logical = Logical.OR)
	@RequestMapping(value = "/fbUser", method = RequestMethod.GET)
	public String fbUser(@RequestParam(value="id")long id,Model model){
		logger.debug("user coming...");
		model.addAttribute("user", EnumFunction.ENUM_USER);
		model.addAttribute("game", GAME_Fb);
		return "/kibana/fb/index";
	}	
	
	/**
	 * 用户留存
	 */
	@RequiresRoles(value = { "admin", "FB_USER" }, logical = Logical.OR)
	@RequestMapping(value = "/fbUserRetained", method = RequestMethod.GET)
	public String fbUserRetained(Model model){
		logger.debug("user Retained...");
		model.addAttribute("user", EnumFunction.ENUM_USER);
		model.addAttribute("game", GAME_Fb);
		return "/kibana/fb/user/retain";
	}
	
	//kun
	/**
	 * 道具日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "KUN_ITEM" }, logical = Logical.OR)
	@RequestMapping(value = "/kunItem", method = RequestMethod.GET)
	public String kunItem(@RequestParam(value = "id")String id,Model model){
		logger.debug("item coming..."+id);
		model.addAttribute("user", EnumFunction.ENUM_ITEM);
		model.addAttribute("game", GAME_KUN);
		return "/kibana/kun/index";
	}	
	
	/**
	 * 体力日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "KUN_AP" }, logical = Logical.OR)
	@RequestMapping(value = "/kunAp", method = RequestMethod.GET)
	public String kunAp(@RequestParam(value = "id")long id,Model model){
		logger.debug("ap coming...");
		model.addAttribute("user", EnumFunction.ENUM_AP);
		model.addAttribute("game", GAME_KUN);
		return "/kibana/kun/index";
	}	
	
	/**
	 * 真实充值币日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "KUN_MONEY" }, logical = Logical.OR)
	@RequestMapping(value = "/kunMoney", method = RequestMethod.GET)
	public String kunMoney(@RequestParam(value = "id")long id,Model model){
		logger.debug("money coming...");
		model.addAttribute("user", EnumFunction.ENUM_MONEY);
		model.addAttribute("game", GAME_KUN);
		return "/kibana/kun/index";
	}	
	
	/**
	 * 虚拟充值币日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "KUN_DUMMY" }, logical = Logical.OR)
	@RequestMapping(value = "/kunDummy", method = RequestMethod.GET)
	public String kunDummy(@RequestParam(value="id")long id,Model model){
		logger.debug("dummy coming...");
		model.addAttribute("user", EnumFunction.ENUM_DUMMY);
		model.addAttribute("game", GAME_KUN);
		return "/kibana/kun/index";
	}	
	
	/**
	 * 游戏币日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "KUN_COIN" }, logical = Logical.OR)
	@RequestMapping(value = "/kunCoin", method = RequestMethod.GET)
	public String kunCoin(@RequestParam(value="id")long id,Model model){
		logger.debug("coin coming...");
		model.addAttribute("user", EnumFunction.ENUM_COIN);
		model.addAttribute("game", GAME_KUN);
		return "/kibana/kun/index";
	}		
	  
	/**
	 * 用户日志
	 * @return
	 */
	@RequestMapping(value = "/kunUser", method = RequestMethod.GET)
	public String kunUser(@RequestParam(value="id")long id,Model model){
		logger.debug("user coming...");
		model.addAttribute("user", EnumFunction.ENUM_USER);
		model.addAttribute("game", GAME_KUN);
		return "/kibana/kun/index";
	}	
	
	
	//kds
	/**
	 * 道具日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "KDS_ITEM" }, logical = Logical.OR)
	@RequestMapping(value = "/kdsItem", method = RequestMethod.GET)
	public String kdsItem(@RequestParam(value = "id")String id,Model model){
		logger.debug("item coming..."+id);
		model.addAttribute("user", EnumFunction.ENUM_ITEM);
		model.addAttribute("game", GAME_KDS);
		return "/kibana/kds/index";
	}	
	
	/**
	 * 体力日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "KDS_AP" }, logical = Logical.OR)
	@RequestMapping(value = "/kdsAp", method = RequestMethod.GET)
	public String kdsAp(@RequestParam(value = "id")long id,Model model){
		logger.debug("ap coming...");
		model.addAttribute("user", EnumFunction.ENUM_AP);
		model.addAttribute("game", GAME_KDS);
		return "/kibana/kds/index";
	}	
	
	/**
	 * 真实充值币日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "KDS_MONEY" }, logical = Logical.OR)
	@RequestMapping(value = "/kdsMoney", method = RequestMethod.GET)
	public String kdsMoney(@RequestParam(value = "id")long id,Model model){
		logger.debug("money coming...");
		model.addAttribute("user", EnumFunction.ENUM_MONEY);
		model.addAttribute("game", GAME_KDS);
		return "/kibana/kds/index";
	}	
	
	/**
	 * 虚拟充值币日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "KDS_DUMMY" }, logical = Logical.OR)
	@RequestMapping(value = "/kdsDummy", method = RequestMethod.GET)
	public String kdsDummy(@RequestParam(value="id")long id,Model model){
		logger.debug("dummy coming...");
		model.addAttribute("user", EnumFunction.ENUM_DUMMY);
		model.addAttribute("game", GAME_KDS);
		return "/kibana/kds/index";
	}	
	
	/**
	 * 游戏币日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "KDS_COIN" }, logical = Logical.OR)
	@RequestMapping(value = "/kdsCoin", method = RequestMethod.GET)
	public String kdsCoin(@RequestParam(value="id")long id,Model model){
		logger.debug("coin coming...");
		model.addAttribute("user", EnumFunction.ENUM_COIN);
		model.addAttribute("game", GAME_KDS);
		return "/kibana/kds/index";
	}	
	  
	/**
	 * 用户日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "KDS_USER" }, logical = Logical.OR)
	@RequestMapping(value = "/kdsUser", method = RequestMethod.GET)
	public String kdsUser(@RequestParam(value="id")long id,Model model){
		logger.debug("user coming...");
		model.addAttribute("user", EnumFunction.ENUM_USER);
		model.addAttribute("game", GAME_KDS);
		return "/kibana/kds/index";
	}	
	
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	public ShiroUser getCurrentUserName() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user;
	}

}
