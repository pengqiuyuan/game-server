package com.enlight.game.web.controller.mgr.xyj;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import net.sf.json.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.codehaus.jackson.type.TypeReference;
import org.elasticsearch.ElasticsearchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.enlight.game.entity.PlatForm;
import com.enlight.game.entity.Server;
import com.enlight.game.entity.ServerZone;
import com.enlight.game.entity.Stores;
import com.enlight.game.entity.gm.Retained1;
import com.enlight.game.entity.gm.Retained2;
import com.enlight.game.entity.gm.Retained3;
import com.enlight.game.service.account.AccountService;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;
import com.enlight.game.service.es.RetainedServer;
import com.enlight.game.service.es.RetainedServer;
import com.enlight.game.service.platForm.PlatFormService;
import com.enlight.game.service.server.ServerService;
import com.enlight.game.service.serverZone.ServerZoneService;
import com.enlight.game.service.store.StoreService;
import com.enlight.game.util.JsonBinder;
import com.enlight.game.web.controller.mgr.BaseController;

@Controller("XyjRetainedController")
@RequestMapping("/manage/xyjRetained")
public class XyjRetainedController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(XyjRetainedController.class);
	
	/**
	 * 这个controller默认为xyj项目的控制层。项目id文档已定
	 */
	private static final String storeId = "4";
	
	private static final String index = "log_xyj_user";
	
	private static final String type = "xyj_user_retained";
	
	SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd" ); 
	Calendar calendar = new GregorianCalendar(); 
	
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	
	@Autowired
	private ServerZoneService serverZoneService;
	
	@Autowired
	private ServerService serverService;
	
	@Autowired
	private PlatFormService platFormService;
	
	@Autowired
	private RetainedServer retainedServer;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private StoreService storeService;
	
	/**
	 * 用户留存
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws ElasticsearchException 
	 * switchTable 留存页面的三张表切换 1、2、3   1所有 2登录 3留存
	 */
	@RequiresRoles(value = { "admin", "XYJ_OFF_USER_RETAINED" }, logical = Logical.OR)
	@RequestMapping(value = "/xyj/userRetained", method = RequestMethod.GET)
	public String userRetained(Model model,ServletRequest request,
			@RequestParam(value = "serverZone", defaultValue = "") String[] sZone,
			@RequestParam(value = "platForm", defaultValue = "") String[] pForm,
			@RequestParam(value = "server", defaultValue = "") String[] sv,
			@RequestParam(value = "switchTable", defaultValue = "1") String switchTable) throws ElasticsearchException, IOException, ParseException{
		logger.debug("user add total...");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Stores stores = storeService.findById(Long.valueOf(storeId));
		List<ServerZone> serverZones = serverZoneService.findAll();
		
		List<String> sZones = new ArrayList<String>();
		List<String> pForms = new ArrayList<String>();
		List<String> svs = new ArrayList<String>();

		Map<String, Object> n = new HashMap<String, Object>();
		if(searchParams.isEmpty()){
			//条件为空时
			String dateFrom = xDayAgoFrom();
			String dateTo = nowDate();
			n =retainedServer.searchAllRetained(index, type, dateFrom, dateTo,switchTable);
			
			model.addAttribute("dateFrom", dateFrom);
			model.addAttribute("dateTo", dateTo);
			model.addAttribute("platForm", platFormService.findAll());
			model.addAttribute("server", serverService.findByStoreId(storeId));
			sZones.add("所有运营大区");
		}else{
		    if(sZone != null && sZone.length>0){
				for (int i = 0; i < sZone.length; i++) {
					if(sZone[i].equals("all")){
						sZones.add("所有运营大区");
						n =retainedServer.searchAllRetained(index, type, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(),switchTable);
					}else{
						String szName = serverZoneService.findById(Long.valueOf(sZone[i])).getServerName();
						sZones.add(szName);
						n =retainedServer.searchServerZoneRetained(index, type, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sZone[i],switchTable);
					}

				}
			}
			if(pForm != null && pForm.length>0){
				for (int i = 0; i < pForm.length; i++) {
					String pfName = platFormService.findByPfId(pForm[i]).getPfName();
					pForms.add(pfName);
					n =retainedServer.searchPlatFormRetained(index, type, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), pForm[i],switchTable);
				}
			}
			if(sv != null && sv.length>0){
				for (int i = 0; i < sv.length; i++) {
					svs.add(sv[i]);
					n =retainedServer.searchServerRetained(index, type, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sv[i],switchTable);
				}
			}
		   
		}

		//logger.debug(binder.toJson(next));
		//logger.debug(binder.toJson(seven));
		//logger.debug(binder.toJson(thirty));
		
		System.out.println("Retained1   "  + binder.toJson(n.get("retained1s")));
		List<Retained1> beanList1 = binder.getMapper().readValue(binder.toJson(n.get("retained1s")), new TypeReference<List<Retained1>>() {}); 
		System.out.println("Retained2   "  + binder.toJson(n.get("retained2s")));
		List<Retained2> beanList2 = binder.getMapper().readValue(binder.toJson(n.get("retained2s")), new TypeReference<List<Retained2>>() {}); 
		System.out.println("Retained3   "  + binder.toJson(n.get("retained3s")));
		List<Retained3> beanList3 = binder.getMapper().readValue(binder.toJson(n.get("retained3s")), new TypeReference<List<Retained3>>() {}); 
		
		model.addAttribute("switchTable", switchTable);
		model.addAttribute("retained1s", beanList1);
		model.addAttribute("retained2s", beanList2);
		model.addAttribute("retained3s", beanList3);
		
		model.addAttribute("store", stores);
		model.addAttribute("serverZone", serverZones);
		model.addAttribute("platForm", platFormService.findAll());
		model.addAttribute("server", serverService.findByStoreId(storeId));
		
		model.addAttribute("sZones", sZones);
		model.addAttribute("pForms", pForms);
		model.addAttribute("svs", svs);
		
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/kibana/user/userRetain";
	}
	
	
	public String nowDate(){
		String nowDate = sdf.format(new Date());
		return nowDate;
	}
	
	public String thirtyDayAgoFrom(){
	    calendar.setTime(new Date()); 
	    calendar.add(calendar.DATE,-30);
	    Date date=calendar.getTime();
	    String da = sdf.format(date); 
		return da;
	}
	
	public String xDayAgoFrom(){
	    calendar.setTime(new Date()); 
	    calendar.add(calendar.DATE,-15);
	    Date date=calendar.getTime();
	    String da = sdf.format(date); 
		return da;
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
	
	@RequestMapping(value = "/findServerByStoreId")
	@ResponseBody
	public Set<Server> findServerByStoreId(@RequestParam(value="storeId")String storeId){
		Set<Server> servers = serverService.findByStoreId(storeId);
		return servers;
		
	}
	
	@RequestMapping(value = "/findServerByStoreIdAndServerZoneId")
	@ResponseBody
	public Set<Server> findServerByStoreIdAndServerZoneId(@RequestParam(value="storeId")String storeId,@RequestParam(value="serverZoneId")String serverZoneId){
		Set<Server> servers = serverService.findByServerZoneIdAndStoreId(serverZoneId, storeId);
		return servers;
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
	public ShiroUser getCurrentUser() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user;
	}
	
}
