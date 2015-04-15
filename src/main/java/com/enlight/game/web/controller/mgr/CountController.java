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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enlight.game.entity.EnumFunction;
import com.enlight.game.entity.PlatForm;
import com.enlight.game.entity.ServerZone;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;
import com.enlight.game.service.platForm.PlatFormService;
import com.enlight.game.service.serverZone.ServerZoneService;


/**
 * 数据统计fb
 * @author dell
 *
 */
@Controller("CountController")
@RequestMapping("/manage/count")
public class CountController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(CountController.class);
	
	@Autowired
	private ServerZoneService serverZoneService;
	
	@Autowired
	private PlatFormService platFormService;
	
	/**
	 * 道具日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "22" }, logical = Logical.OR)
	@RequestMapping(value = "/fb/item", method = RequestMethod.GET)
	public String item(@RequestParam(value = "id")String id,Model model){
		logger.debug("item coming..."+id);
		model.addAttribute("user", EnumFunction.ENUM_ITEM);
		return "/kibana/fb/item/index";
	}	
	
	/**
	 * 体力日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "21" }, logical = Logical.OR)
	@RequestMapping(value = "/fb/ap", method = RequestMethod.GET)
	public String ap(@RequestParam(value = "id")long id,Model model){
		logger.debug("ap coming...");
		model.addAttribute("user", EnumFunction.ENUM_AP);
		return "/kibana/fb/ap/index";
	}	
	
	/**
	 * 真实充值币日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "23" }, logical = Logical.OR)
	@RequestMapping(value = "/fb/money", method = RequestMethod.GET)
	public String money(@RequestParam(value = "id")long id,Model model){
		logger.debug("money coming...");
		model.addAttribute("user", EnumFunction.ENUM_MONEY);
		return "/kibana/fb/money/index";
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
		return "/kibana/fb/dummy/index";
	}	
	
	/**
	 * 游戏币日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "25" }, logical = Logical.OR)
	@RequestMapping(value = "/fb/coin", method = RequestMethod.GET)
	public String coin(@RequestParam(value="id")long id,Model model){
		logger.debug("coin coming...");
		model.addAttribute("user", EnumFunction.ENUM_COIN);
		return "/kibana/fb/coin/index";
	}	
	
	/**
	 * 竞技场徽章(货币)日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "26" }, logical = Logical.OR)
	@RequestMapping(value = "/fb/arenacoin", method = RequestMethod.GET)
	public String arenacoin(@RequestParam(value="id")long id,Model model){
		logger.debug("arenacoin coming...");
		model.addAttribute("user", EnumFunction.ENUM_ARENACOIN);
		return "/kibana/fb/arenacoin/index";
	}	
	
	/**
	 * 燃烧远征龙鳞币(货币)日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "27" }, logical = Logical.OR)
	@RequestMapping(value = "/fb/expeditioncoin", method = RequestMethod.GET)
	public String expeditioncoin(@RequestParam(value="id")long id,Model model){
		logger.debug("expeditioncoin coming...");
		model.addAttribute("user", EnumFunction.ENUM_EXPEDITIONCOIN);
		return "/kibana/fb/expeditioncoin/index";
	}	
	  
	/**
	 * 用户日志
	 * @return
	 */
	@RequiresRoles(value = { "admin", "28" }, logical = Logical.OR)
	@RequestMapping(value = "/fb/user", method = RequestMethod.GET)
	public String user(@RequestParam(value="id")long id,Model model){
		logger.debug("user coming...");
		model.addAttribute("user", EnumFunction.ENUM_USER);
		return "/kibana/fb/user/index";
	}	
	
	
	/**
	 * 用户留存
	 */
	@RequiresRoles(value = { "admin", "28" }, logical = Logical.OR)
	@RequestMapping(value = "/fb/userRetained", method = RequestMethod.GET)
	public String userRetained(Model model){
		logger.debug("user Retained...");
		model.addAttribute("user", EnumFunction.ENUM_USER);
		return "/kibana/fb/user/retain";
	}
	
	@RequestMapping(value = "/findServerZone")
	@ResponseBody
	public List<ServerZone> findServerZone() {
		List<ServerZone> serverZones = serverZoneService.findAll();
		return serverZones;
	}
	
	@RequestMapping(value = "/findPlatForm")
	@ResponseBody
	public List<PlatForm> findPlatForm() {
		List<PlatForm> platForms = platFormService.findAll();
		return platForms;
	}
	
	@RequestMapping(value = "/findPlatFormByServerId")
	@ResponseBody
	public List<PlatForm> findPlatFormByServerId(@RequestParam(value="serverId")String serverId) {
		List<PlatForm> platForms = platFormService.findByServerZoneId(serverId);
		return platForms;
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
	    calendar.add(calendar.DATE,-30);
	    String thirtyDayAgo = sdf.format(calendar.getTime()); 
		
	    dateMap.put("nowDate",nowDate);
	    dateMap.put("yesterday",yesterday);
	    dateMap.put("sevenDayAgo",sevenDayAgo);
	    dateMap.put("thirtyDayAgo",thirtyDayAgo);
		return dateMap;
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	public ShiroUser getCurrentUserName() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user;
	}

}
