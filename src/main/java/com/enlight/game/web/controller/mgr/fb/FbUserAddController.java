package com.enlight.game.web.controller.mgr.fb;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.elasticsearch.ElasticsearchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.enlight.game.entity.EnumFunction;
import com.enlight.game.entity.GiftItem;
import com.enlight.game.entity.PlatForm;
import com.enlight.game.entity.Server;
import com.enlight.game.entity.ServerZone;
import com.enlight.game.entity.Stores;
import com.enlight.game.service.account.AccountService;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;
import com.enlight.game.service.es.fb.FbUserAddServer;
import com.enlight.game.service.es.fb.FbUserTotalServer;
import com.enlight.game.service.platForm.PlatFormService;
import com.enlight.game.service.server.ServerService;
import com.enlight.game.service.serverZone.ServerZoneService;
import com.enlight.game.service.store.StoreService;
import com.enlight.game.web.controller.mgr.BaseController;

@Controller("FbUserAddController")
@RequestMapping("/manage/fbUserAdd")
public class FbUserAddController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(FbRetainedController.class);
	
	/**
	 * 这个controller默认为fb项目的控制层。项目id文档已定
	 */
	private static final String storeId = "1";
	
	SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd" ); 
	Calendar calendar = new GregorianCalendar(); 
	
	@Autowired
	private ServerZoneService serverZoneService;
	
	@Autowired
	private ServerService serverService;
	
	@Autowired
	private PlatFormService platFormService;
	
	@Autowired
	private FbUserTotalServer fbUserTotalServer;
	
	@Autowired
	private FbUserAddServer fbUserAddServer;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private StoreService storeService;
	
	/**
	 * 用户新增
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws ElasticsearchException 
	 */
	@RequiresRoles(value = { "admin", "FB_USER" }, logical = Logical.OR)
	@RequestMapping(value = "/fb/userAdd", method = RequestMethod.GET)
	public String userRetained(Model model,ServletRequest request,
			@RequestParam(value = "serverZone", defaultValue = "") String[] sZone,
			@RequestParam(value = "platForm", defaultValue = "") String[] pForm,
			@RequestParam(value = "server", defaultValue = "") String[] sv) throws ElasticsearchException, IOException, ParseException{
		logger.debug("user add total...");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Stores stores = storeService.findById(Long.valueOf(storeId));
		List<ServerZone> serverZones = serverZoneService.findAll();
		Map<String, Object> mTotal = new HashMap<String, Object>();
		Map<String, Object> mAdd = new HashMap<String, Object>();

		Map<String,LinkedList<String>> m = new HashMap<String, LinkedList<String>>();
		

		List<String> sZones = new ArrayList<String>();
		List<String> pForms = new ArrayList<String>();
		List<String> svs = new ArrayList<String>();

		if(searchParams.isEmpty()){
			//条件为空时
			String dateFrom = thirtyDayAgoFrom();
			String dateTo = nowDate();
			mTotal = fbUserTotalServer.searchAllUserTotal(dateFrom, dateTo);
			mAdd = fbUserAddServer.searchAllUserAdd(dateFrom, dateTo);
			model.addAttribute("dateFrom", dateFrom);
			model.addAttribute("dateTo", dateTo);
			model.addAttribute("platForm", platFormService.findAll());
			model.addAttribute("server", serverService.findByStoreId(storeId));
		}else{
		    Long startTIme = sdf.parse(searchParams.get("EQ_dateFrom").toString()).getTime(); 
		    Long endTime = sdf.parse(searchParams.get("EQ_dateTo").toString()).getTime();
		    Long oneDay = 1000 * 60 * 60 * 24l;  
		    Long time = startTIme;  
		    while (time <= endTime) {  
		        Date d = new Date(time);  
		        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
		        System.out.println(df.format(d).toString());  
		        time += oneDay;  
		    } 
		    if(sZone != null && sZone.length>0){
				for (int i = 0; i < sZone.length; i++) {
					sZones.add(sZone[i]);
					System.out.println(sZone[i]);
					mTotal = fbUserTotalServer.searchServerZoneUserTotal(searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sZone[i]);
				}
			}
			if(pForm != null && pForm.length>0){
				for (int i = 0; i < pForm.length; i++) {
					pForms.add(pForm[i]);
					System.out.println(pForm[i]);
					mTotal = fbUserTotalServer.searchPlatFormUserTotal(searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), pForm[i]);
				}
			}
			if(sv != null && sv.length>0){
				for (int i = 0; i < sv.length; i++) {
					svs.add(sv[i]);
					System.out.println(sv[i]);
					mTotal = fbUserTotalServer.searchServerUserTotal(searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sv[i]);
				}
			}
		    
			/**
			if(searchParams.get("EQ_platForm_value")!="" && searchParams.get("EQ_platForm_value")!=null){
				//查询渠道
				model.addAttribute("dateFrom", searchParams.get("EQ_dateFrom").toString());
				model.addAttribute("dateTo", searchParams.get("EQ_dateTo").toString());
				model.addAttribute("platForm",  searchParams.get("EQ_serverZone").toString().equals("all")?platFormService.findAll():platFormService.findByServerZoneId(searchParams.get("EQ_serverZone").toString()));
				model.addAttribute("server", searchParams.get("EQ_serverZone").toString().equals("all")?serverService.findByStoreId(storeId):serverService.findByServerZoneIdAndStoreId(searchParams.get("EQ_serverZone").toString(),storeId));
				PlatForm platform =  platFormService.findByPfName(searchParams.get("EQ_platForm_value").toString());
				mTotal = fbUserTotalServer.searchPlatFormUserTotal(searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), platform.getPfId());
				mAdd = fbUserAddServer.searchPlatFormUserAdd(searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), platform.getPfId());
			}else if(searchParams.get("EQ_server_value")!="" && searchParams.get("EQ_server_value")!=null){
				//查询服务器
				model.addAttribute("dateFrom", searchParams.get("EQ_dateFrom").toString());
				model.addAttribute("dateTo", searchParams.get("EQ_dateTo").toString());
				model.addAttribute("platForm",  searchParams.get("EQ_serverZone").toString().equals("all")?platFormService.findAll():platFormService.findByServerZoneId(searchParams.get("EQ_serverZone").toString()));
				model.addAttribute("server", searchParams.get("EQ_serverZone").toString().equals("all")?serverService.findByStoreId(storeId):serverService.findByServerZoneIdAndStoreId(searchParams.get("EQ_serverZone").toString(),storeId));
				Server server = serverService.findByServerId(searchParams.get("EQ_server_value").toString());
				mTotal = fbUserTotalServer.searchServerUserTotal(searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), server.getServerId());
				mAdd = fbUserAddServer.searchServerUserAdd(searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), server.getServerId());
			}else{
				//查询运营大区
				if(searchParams.get("EQ_serverZone").toString().equals("all")){
					model.addAttribute("dateFrom", searchParams.get("EQ_dateFrom").toString());
					model.addAttribute("dateTo", searchParams.get("EQ_dateTo").toString());
					mTotal = fbUserTotalServer.searchAllUserTotal(searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString());
					mAdd = fbUserAddServer.searchAllUserAdd(searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString());
					model.addAttribute("platForm", platFormService.findAll());
					model.addAttribute("server", serverService.findByStoreId(storeId));
				}else{
					model.addAttribute("dateFrom", searchParams.get("EQ_dateFrom").toString());
					model.addAttribute("dateTo", searchParams.get("EQ_dateTo").toString());
					model.addAttribute("platForm",  platFormService.findByServerZoneId(searchParams.get("EQ_serverZone").toString()));
					model.addAttribute("server", searchParams.get("EQ_serverZone").toString().equals("all")?serverService.findByStoreId(storeId):serverService.findByServerZoneIdAndStoreId(searchParams.get("EQ_serverZone").toString(),storeId));
					mTotal = fbUserTotalServer.searchServerZoneUserTotal(searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), searchParams.get("EQ_serverZone").toString());
					mAdd = fbUserAddServer.searchServerZoneUserAdd(searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), searchParams.get("EQ_serverZone").toString());
				}
			}
			**/
		}
	
		model.addAttribute("userTotal", mTotal.get("userTotal"));
		model.addAttribute("tableTotal", mTotal.get("table"));
		
		model.addAttribute("userAdd", mAdd.get("userAdd"));
		model.addAttribute("tableAdd", mAdd.get("table"));
		
		model.addAttribute("store", stores);
		model.addAttribute("serverZone", serverZones);
		model.addAttribute("platForm", platFormService.findAll());
		model.addAttribute("server", serverService.findByStoreId(storeId));
		
		model.addAttribute("sZones", sZones);
		model.addAttribute("pForms", pForms);
		model.addAttribute("svs", svs);
		

		
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/kibana/fb/user/userAdd";
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
