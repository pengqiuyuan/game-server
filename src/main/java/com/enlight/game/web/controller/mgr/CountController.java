package com.enlight.game.web.controller.mgr;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.enlight.game.base.AppBizException;
import com.enlight.game.entity.EnumFunction;
import com.enlight.game.entity.PlatForm;
import com.enlight.game.entity.ServerZone;
import com.enlight.game.entity.Tag;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;
import com.enlight.game.service.platForm.PlatFormService;
import com.enlight.game.service.server.ServerService;
import com.enlight.game.service.serverZone.ServerZoneService;
import com.enlight.game.service.tag.TagService;
import com.enlight.game.util.JsonBinder;
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
	
	private static final String GAME_KDS = "kds";
	
	private static final String GAME_XYJ = "xyj";
	
	@Autowired
	private ServerZoneService serverZoneService;
	
	@Autowired
	private PlatFormService platFormService;
	
	@Autowired
	private ServerService serverService;
	
	@Autowired
	private TagService tagService;
	
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	
	//kds
	/**
	 * 道具日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "KDS_ITEM" }, logical = Logical.OR)
	@RequestMapping(value = "/kdsItem", method = RequestMethod.GET)
	public String kdsItem(Model model){
		logger.debug("item coming...");
		model.addAttribute("user", EnumFunction.ENUM_ITEM);
		model.addAttribute("game", GAME_KDS);
		return "/kibana/index";
	}	
	
	/**
	 * 体力日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "KDS_AP" }, logical = Logical.OR)
	@RequestMapping(value = "/kdsAp", method = RequestMethod.GET)
	public String kdsAp(Model model){
		logger.debug("ap coming...");
		model.addAttribute("user", EnumFunction.ENUM_AP);
		model.addAttribute("game", GAME_KDS);
		return "/kibana/index";
	}	
	
	/**
	 * 真实充值币日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "KDS_MONEY" }, logical = Logical.OR)
	@RequestMapping(value = "/kdsMoney", method = RequestMethod.GET)
	public String kdsMoney(Model model){
		logger.debug("money coming...");
		model.addAttribute("user", EnumFunction.ENUM_MONEY);
		model.addAttribute("game", GAME_KDS);
		return "/kibana/index";
	}	
	
	/**
	 * 虚拟充值币日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "KDS_DUMMY" }, logical = Logical.OR)
	@RequestMapping(value = "/kdsDummy", method = RequestMethod.GET)
	public String kdsDummy(Model model){
		logger.debug("dummy coming...");
		model.addAttribute("user", EnumFunction.ENUM_DUMMY);
		model.addAttribute("game", GAME_KDS);
		return "/kibana/index";
	}	
	
	/**
	 * 游戏币日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "KDS_COIN" }, logical = Logical.OR)
	@RequestMapping(value = "/kdsCoin", method = RequestMethod.GET)
	public String kdsCoin(Model model){
		logger.debug("coin coming...");
		model.addAttribute("user", EnumFunction.ENUM_COIN);
		model.addAttribute("game", GAME_KDS);
		return "/kibana/index";
	}	
	  
	/**
	 * 用户日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "KDS_USER" }, logical = Logical.OR)
	@RequestMapping(value = "/kdsUser", method = RequestMethod.GET)
	public String kdsUser(Model model){
		logger.debug("user coming...");
		model.addAttribute("user", EnumFunction.ENUM_USER);
		model.addAttribute("game", GAME_KDS);
		return "/kibana/index";
	}	
	
	/**
	 * 用户画像
	 */
	@RequiresRoles(value = { "admin", "KDS_USER_PORTRAIT" }, logical = Logical.OR)
	@RequestMapping(value = "/kdsUserPortrait", method = RequestMethod.GET)
	public String kdsUserPortrait(Model model){
		logger.debug("user portrait...");
		model.addAttribute("user", EnumFunction.ENUM_USER_PORTRAIT);
		model.addAttribute("game", GAME_KDS);
		return "/kibana/index";
	}
	
	
	
	//xyj
	/**
	 * 道具日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "XYJ_ITEM" }, logical = Logical.OR)
	@RequestMapping(value = "/xyjItem", method = RequestMethod.GET)
	public String xyjItem(Model model){
		logger.debug("item coming...");
		model.addAttribute("user", EnumFunction.ENUM_ITEM);
		model.addAttribute("game", GAME_XYJ);
		return "/kibana/index";
	}	
	
	/**
	 * 体力日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "XYJ_AP" }, logical = Logical.OR)
	@RequestMapping(value = "/xyjAp", method = RequestMethod.GET)
	public String xyjAp(Model model){
		logger.debug("ap coming...");
		model.addAttribute("user", EnumFunction.ENUM_AP);
		model.addAttribute("game", GAME_XYJ);
		return "/kibana/index";
	}	
	
	/**
	 * 真实充值币日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "XYJ_MONEY" }, logical = Logical.OR)
	@RequestMapping(value = "/xyjMoney", method = RequestMethod.GET)
	public String xyjMoney(Model model){
		logger.debug("money coming...");
		model.addAttribute("user", EnumFunction.ENUM_MONEY);
		model.addAttribute("game", GAME_XYJ);
		return "/kibana/index";
	}	
	
	/**
	 * 虚拟充值币日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "XYJ_DUMMY" }, logical = Logical.OR)
	@RequestMapping(value = "/xyjDummy", method = RequestMethod.GET)
	public String xyjDummy(Model model){
		logger.debug("dummy coming...");
		model.addAttribute("user", EnumFunction.ENUM_DUMMY);
		model.addAttribute("game", GAME_XYJ);
		return "/kibana/index";
	}	
	
	/**
	 * 游戏币日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "XYJ_COIN" }, logical = Logical.OR)
	@RequestMapping(value = "/xyjCoin", method = RequestMethod.GET)
	public String xyjCoin(Model model){
		logger.debug("coin coming...");
		model.addAttribute("user", EnumFunction.ENUM_COIN);
		model.addAttribute("game", GAME_XYJ);
		return "/kibana/index";
	}	
	  
	/**
	 * 用户日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "XYJ_USER" }, logical = Logical.OR)
	@RequestMapping(value = "/xyjUser", method = RequestMethod.GET)
	public String xyjUser(Model model){
		logger.debug("user coming...");
		model.addAttribute("user", EnumFunction.ENUM_USER);
		model.addAttribute("game", GAME_XYJ);
		return "/kibana/index";
	}	
	
	/**
	 * 用户画像
	 */
	@RequiresRoles(value = { "admin", "XYJ_USER_PORTRAIT" }, logical = Logical.OR)
	@RequestMapping(value = "/xyjUserPortrait", method = RequestMethod.GET)
	public String xyjUserPortrait(Model model){
		logger.debug("user portrait...");
		model.addAttribute("user", EnumFunction.ENUM_USER_PORTRAIT);
		model.addAttribute("game", GAME_XYJ);
		return "/kibana/index";
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	public ShiroUser getCurrentUserName() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user;
	}
	
	/**
	 * 返回key对应value
	 * @return
	 * @throws AppBizException
	 */
	@RequestMapping(value="/findallvalue",method=RequestMethod.POST)	
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String,String> findallvalue(@RequestParam(value="name") String name
			,@RequestParam(value="game") String game
			,@RequestBody String[] arr
			) throws AppBizException{
		Map<String,String> value = new HashMap<String, String>();
		
		Map<String, String> map = new HashMap<String, String>();
		if(arr.length != 0){
			if(name.equals("运营大区ID")){
				List<ServerZone> serverZones =  serverZoneService.findAll();
				for (ServerZone serverZone : serverZones) {
					map.put(serverZone.getId().toString(),serverZone.getServerName());
				}
			}else if(name.equals("渠道ID") || name.equals("注册渠道")){
				List<PlatForm> platForms =  platFormService.findAll();
				for (PlatForm platForm : platForms) {
					map.put(platForm.getPfId(),platForm.getPfName());
				}
			}else if(name.equals("日志道具id")){
				List<Tag> tags = tagService.findByCategoryAndStoreName(Tag.CATEGORY_ITEM,game.toUpperCase());
				for (Tag tag : tags) {
					map.put(tag.getTagId().toString(), tag.getTagName());
				}
			}else if(name.equals("获得途径")||name.equals("消耗途径")){
				List<Tag> tags = tagService.findByCategoryAndStoreName(Tag.CATEGORY_GET_AND_COST,game.toUpperCase());
				for (Tag tag : tags) {
					map.put(tag.getTagId().toString(), tag.getTagName());
				}
			}else if(name.equals("功能编号")){ //新手引导
				List<Tag> tags = tagService.findByCategoryAndStoreName(Tag.CATEGORY_NEWPLAYER_GUIDE,game.toUpperCase());
				for (Tag tag : tags) {
					map.put(tag.getTagId().toString(), tag.getTagName());
				}
			}
		}
		
		for (String string : arr) {
			if(!map.isEmpty()){
				if(map.containsKey(string)){
					value.put(string,map.get(string));
				}else{
					value.put(string,string);
				}
			}else{
				value.put(string,string);
			}
		}
		return value;
	}
	
	/**
	 * 服务器获取时间
	 */
	@RequestMapping(value="/getDate")
	@ResponseBody
	public Map<String, String> getDate(){
		Map<String,String> dateMap = new HashMap<String, String>();
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd" ); 
		Calendar calendar = new GregorianCalendar(); 
		String nowDate = sdf.format(new Date());
		
	    calendar.setTime(new Date()); 
	    calendar.add(calendar.DATE,-1);
	    String yesterday = sdf.format(calendar.getTime());
	    
	    calendar.setTime(new Date()); 
	    calendar.add(calendar.DATE,-7);
	    String sevenDayAgo = sdf.format(calendar.getTime()); 
	    
	    calendar.setTime(new Date()); 
	    calendar.add(calendar.DATE,-15);
	    String fifteenDayAgo = sdf.format(calendar.getTime()); 
	    
	    calendar.setTime(new Date()); 
	    calendar.add(calendar.DATE,-30);
	    String thirtyDayAgo = sdf.format(calendar.getTime()); 
		
	    dateMap.put("nowDate",nowDate);
	    dateMap.put("yesterday",yesterday);
	    dateMap.put("sevenDayAgo",sevenDayAgo);
	    dateMap.put("fifteenDayAgo",fifteenDayAgo);
	    dateMap.put("thirtyDayAgo",thirtyDayAgo);
		return dateMap;
	}
}
