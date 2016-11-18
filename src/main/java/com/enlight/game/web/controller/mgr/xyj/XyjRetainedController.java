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
import com.enlight.game.service.es.RetainedServer2;
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
	private RetainedServer2 retainedServer2;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private StoreService storeService;
	
	/**
	 * 用户留存
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws ElasticsearchException 
	 */
	@RequiresRoles(value = { "admin", "XYJ_OFF_USER_RETAINED" }, logical = Logical.OR)
	@RequestMapping(value = "/xyj/userRetained", method = RequestMethod.GET)
	public String userRetained(Model model,ServletRequest request,
			@RequestParam(value = "serverZone", defaultValue = "") String[] sZone,
			@RequestParam(value = "platForm", defaultValue = "") String[] pForm,
			@RequestParam(value = "server", defaultValue = "") String[] sv) throws ElasticsearchException, IOException, ParseException{
		logger.debug("user add total...");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Stores stores = storeService.findById(Long.valueOf(storeId));
		List<ServerZone> serverZones = serverZoneService.findAll();
		
		Map<String, Map<String,Object>> next = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> seven = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> thirty = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d2 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d3 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d4 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d5 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d6 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d8 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d9 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d10 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d11 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d12 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d13 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d14 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d15 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d16 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d17 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d18 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d19 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d20 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d21 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d22 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d23 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d24 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d25 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d26 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d27 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d28 = new HashMap<String, Map<String,Object>>();
		Map<String, Map<String,Object>> d29 = new HashMap<String, Map<String,Object>>();
		
		Map<String, Map<String, Object>> n = new HashMap<String, Map<String, Object>>();
		
		List<String> sZones = new ArrayList<String>();
		List<String> pForms = new ArrayList<String>();
		List<String> svs = new ArrayList<String>();

		if(searchParams.isEmpty()){
			//条件为空时
			String dateFrom = thirtyDayAgoFrom();
			String dateTo = nowDate();
			n =retainedServer.searchAllRetained(index, type, dateFrom, dateTo);
			next.put("所有运营大区", n.get("next"));
			seven.put("所有运营大区", n.get("seven"));
			thirty.put("所有运营大区", n.get("thirty"));
			d2.put("所有运营大区", n.get("d2"));
			d3.put("所有运营大区", n.get("d3"));
			d4.put("所有运营大区", n.get("d4"));
			d5.put("所有运营大区", n.get("d5"));
			d6.put("所有运营大区", n.get("d6"));
			d8.put("所有运营大区", n.get("d8"));
			d9.put("所有运营大区", n.get("d9"));
			d10.put("所有运营大区", n.get("d10"));
			d11.put("所有运营大区", n.get("d11"));
			d12.put("所有运营大区", n.get("d12"));
			d13.put("所有运营大区", n.get("d13"));
			d14.put("所有运营大区", n.get("d14"));
			d15.put("所有运营大区", n.get("d15"));
			d16.put("所有运营大区", n.get("d16"));
			d17.put("所有运营大区", n.get("d17"));
			d18.put("所有运营大区", n.get("d18"));
			d19.put("所有运营大区", n.get("d19"));
			d20.put("所有运营大区", n.get("d20"));
			d21.put("所有运营大区", n.get("d21"));
			d22.put("所有运营大区", n.get("d22"));
			d23.put("所有运营大区", n.get("d23"));
			d24.put("所有运营大区", n.get("d24"));
			d25.put("所有运营大区", n.get("d25"));
			d26.put("所有运营大区", n.get("d26"));
			d27.put("所有运营大区", n.get("d27"));
			d28.put("所有运营大区", n.get("d28"));
			d29.put("所有运营大区", n.get("d29"));
			
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
						n =retainedServer.searchAllRetained(index, type, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString());
						next.put("所有运营大区", n.get("next"));
						seven.put("所有运营大区", n.get("seven"));
						thirty.put("所有运营大区", n.get("thirty"));
						d2.put("所有运营大区", n.get("d2"));
						d3.put("所有运营大区", n.get("d3"));
						d4.put("所有运营大区", n.get("d4"));
						d5.put("所有运营大区", n.get("d5"));
						d6.put("所有运营大区", n.get("d6"));
						d8.put("所有运营大区", n.get("d8"));
						d9.put("所有运营大区", n.get("d9"));
						d10.put("所有运营大区", n.get("d10"));
						d11.put("所有运营大区", n.get("d11"));
						d12.put("所有运营大区", n.get("d12"));
						d13.put("所有运营大区", n.get("d13"));
						d14.put("所有运营大区", n.get("d14"));
						d15.put("所有运营大区", n.get("d15"));
						d16.put("所有运营大区", n.get("d16"));
						d17.put("所有运营大区", n.get("d17"));
						d18.put("所有运营大区", n.get("d18"));
						d19.put("所有运营大区", n.get("d19"));
						d20.put("所有运营大区", n.get("d20"));
						d21.put("所有运营大区", n.get("d21"));
						d22.put("所有运营大区", n.get("d22"));
						d23.put("所有运营大区", n.get("d23"));
						d24.put("所有运营大区", n.get("d24"));
						d25.put("所有运营大区", n.get("d25"));
						d26.put("所有运营大区", n.get("d26"));
						d27.put("所有运营大区", n.get("d27"));
						d28.put("所有运营大区", n.get("d28"));
						d29.put("所有运营大区", n.get("d29"));
					}else{
						String szName = serverZoneService.findById(Long.valueOf(sZone[i])).getServerName();
						sZones.add(szName);
						n =retainedServer.searchServerZoneRetained(index, type, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sZone[i]);
						next.put(szName, n.get("next"));
						seven.put(szName, n.get("seven"));
						thirty.put(szName, n.get("thirty"));
						d2.put(szName, n.get("d2"));
						d3.put(szName, n.get("d3"));
						d4.put(szName, n.get("d4"));
						d5.put(szName, n.get("d5"));
						d6.put(szName, n.get("d6"));
						d8.put(szName, n.get("d8"));
						d9.put(szName, n.get("d9"));
						d10.put(szName, n.get("d10"));
						d11.put(szName, n.get("d11"));
						d12.put(szName, n.get("d12"));
						d13.put(szName, n.get("d13"));
						d14.put(szName, n.get("d14"));
						d15.put(szName, n.get("d15"));
						d16.put(szName, n.get("d16"));
						d17.put(szName, n.get("d17"));
						d18.put(szName, n.get("d18"));
						d19.put(szName, n.get("d19"));
						d20.put(szName, n.get("d20"));
						d21.put(szName, n.get("d21"));
						d22.put(szName, n.get("d22"));
						d23.put(szName, n.get("d23"));
						d24.put(szName, n.get("d24"));
						d25.put(szName, n.get("d25"));
						d26.put(szName, n.get("d26"));
						d27.put(szName, n.get("d27"));
						d28.put(szName, n.get("d28"));
						d29.put(szName, n.get("d29"));
					}

				}
			}
			if(pForm != null && pForm.length>0){
				for (int i = 0; i < pForm.length; i++) {
					String pfName = platFormService.findByPfId(pForm[i]).getPfName();
					pForms.add(pfName);
					n =retainedServer.searchPlatFormRetained(index, type, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), pForm[i]);
					next.put(pfName, n.get("next"));
					seven.put(pfName, n.get("seven"));
					thirty.put(pfName, n.get("thirty"));
					d2.put(pfName, n.get("d2"));
					d3.put(pfName, n.get("d3"));
					d4.put(pfName, n.get("d4"));
					d5.put(pfName, n.get("d5"));
					d6.put(pfName, n.get("d6"));
					d8.put(pfName, n.get("d8"));
					d9.put(pfName, n.get("d9"));
					d10.put(pfName, n.get("d10"));
					d11.put(pfName, n.get("d11"));
					d12.put(pfName, n.get("d12"));
					d13.put(pfName, n.get("d13"));
					d14.put(pfName, n.get("d14"));
					d15.put(pfName, n.get("d15"));
					d16.put(pfName, n.get("d16"));
					d17.put(pfName, n.get("d17"));
					d18.put(pfName, n.get("d18"));
					d19.put(pfName, n.get("d19"));
					d20.put(pfName, n.get("d20"));
					d21.put(pfName, n.get("d21"));
					d22.put(pfName, n.get("d22"));
					d23.put(pfName, n.get("d23"));
					d24.put(pfName, n.get("d24"));
					d25.put(pfName, n.get("d25"));
					d26.put(pfName, n.get("d26"));
					d27.put(pfName, n.get("d27"));
					d28.put(pfName, n.get("d28"));
					d29.put(pfName, n.get("d29"));
				}
			}
			if(sv != null && sv.length>0){
				for (int i = 0; i < sv.length; i++) {
					svs.add(sv[i]);
					n =retainedServer.searchServerRetained(index, type, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sv[i]);
					next.put(sv[i], n.get("next"));
					seven.put(sv[i], n.get("seven"));
					thirty.put(sv[i], n.get("thirty"));
					d2.put(sv[i], n.get("d2"));
					d3.put(sv[i], n.get("d3"));
					d4.put(sv[i], n.get("d4"));
					d5.put(sv[i], n.get("d5"));
					d6.put(sv[i], n.get("d6"));
					d8.put(sv[i], n.get("d8"));
					d9.put(sv[i], n.get("d9"));
					d10.put(sv[i], n.get("d10"));
					d11.put(sv[i], n.get("d11"));
					d12.put(sv[i], n.get("d12"));
					d13.put(sv[i], n.get("d13"));
					d14.put(sv[i], n.get("d14"));
					d15.put(sv[i], n.get("d15"));
					d16.put(sv[i], n.get("d16"));
					d17.put(sv[i], n.get("d17"));
					d18.put(sv[i], n.get("d18"));
					d19.put(sv[i], n.get("d19"));
					d20.put(sv[i], n.get("d20"));
					d21.put(sv[i], n.get("d21"));
					d22.put(sv[i], n.get("d22"));
					d23.put(sv[i], n.get("d23"));
					d24.put(sv[i], n.get("d24"));
					d25.put(sv[i], n.get("d25"));
					d26.put(sv[i], n.get("d26"));
					d27.put(sv[i], n.get("d27"));
					d28.put(sv[i], n.get("d28"));
					d29.put(sv[i], n.get("d29"));
				}
			}
		   
		}

		logger.debug(binder.toJson(next));
		logger.debug(binder.toJson(seven));
		logger.debug(binder.toJson(thirty));
		logger.debug(binder.toJson(d2));
		model.addAttribute("next", binder.toJson(next));
		model.addAttribute("seven", binder.toJson(seven));
		model.addAttribute("thirty", binder.toJson(thirty));
		model.addAttribute("d2", binder.toJson(d2));
		model.addAttribute("d3", binder.toJson(d3));
		model.addAttribute("d4", binder.toJson(d4));
		model.addAttribute("d5", binder.toJson(d5));
		model.addAttribute("d6", binder.toJson(d6));
		model.addAttribute("d8", binder.toJson(d8));
		model.addAttribute("d9", binder.toJson(d9));
		model.addAttribute("d10", binder.toJson(d10));
		model.addAttribute("d11", binder.toJson(d11));
		model.addAttribute("d12", binder.toJson(d12));
		model.addAttribute("d13", binder.toJson(d13));
		model.addAttribute("d14", binder.toJson(d14));
		model.addAttribute("d15", binder.toJson(d15));
		model.addAttribute("d16", binder.toJson(d16));
		model.addAttribute("d17", binder.toJson(d17));
		model.addAttribute("d18", binder.toJson(d18));
		model.addAttribute("d19", binder.toJson(d19));
		model.addAttribute("d20", binder.toJson(d20));
		model.addAttribute("d21", binder.toJson(d21));
		model.addAttribute("d22", binder.toJson(d22));
		model.addAttribute("d23", binder.toJson(d23));
		model.addAttribute("d24", binder.toJson(d24));
		model.addAttribute("d25", binder.toJson(d25));
		model.addAttribute("d26", binder.toJson(d26));
		model.addAttribute("d27", binder.toJson(d27));
		model.addAttribute("d28", binder.toJson(d28));
		model.addAttribute("d29", binder.toJson(d29));
		
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
	
	/**
	 * 用户留存
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws ElasticsearchException 
	 */
	@RequiresRoles(value = { "admin", "XYJ_OFF_USER_RETAINED" }, logical = Logical.OR)
	@RequestMapping(value = "/xyj/userRetained2", method = RequestMethod.GET)
	public String userRetained2(Model model,ServletRequest request,
			@RequestParam(value = "serverZone", defaultValue = "") String[] sZone,
			@RequestParam(value = "platForm", defaultValue = "") String[] pForm,
			@RequestParam(value = "server", defaultValue = "") String[] sv) throws ElasticsearchException, IOException, ParseException{
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
			n =retainedServer2.searchAllRetained(index, type, dateFrom, dateTo);
			
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
						n =retainedServer2.searchAllRetained(index, type, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString());
					}else{
						String szName = serverZoneService.findById(Long.valueOf(sZone[i])).getServerName();
						sZones.add(szName);
						n =retainedServer2.searchServerZoneRetained(index, type, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sZone[i]);
					}

				}
			}
			if(pForm != null && pForm.length>0){
				for (int i = 0; i < pForm.length; i++) {
					String pfName = platFormService.findByPfId(pForm[i]).getPfName();
					pForms.add(pfName);
					n =retainedServer2.searchPlatFormRetained(index, type, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), pForm[i]);
				}
			}
			if(sv != null && sv.length>0){
				for (int i = 0; i < sv.length; i++) {
					svs.add(sv[i]);
					n =retainedServer2.searchServerRetained(index, type, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sv[i]);
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
		return "/kibana/user2/userRetain";
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
