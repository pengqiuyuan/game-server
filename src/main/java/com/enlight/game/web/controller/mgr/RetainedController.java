package com.enlight.game.web.controller.mgr;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.elasticsearch.client.Client;
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
import com.enlight.game.service.platForm.PlatFormService;
import com.enlight.game.service.serverZone.ServerZoneService;

@Controller("RetainedController")
@RequestMapping("/manage/retained")
public class RetainedController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(RetainedController.class);
	
	@Autowired
	private ServerZoneService serverZoneService;
	
	@Autowired
	private PlatFormService platFormService;
	
	@Autowired
	public Client client;
	
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
}
