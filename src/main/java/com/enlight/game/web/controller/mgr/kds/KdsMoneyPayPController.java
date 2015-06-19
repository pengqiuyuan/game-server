package com.enlight.game.web.controller.mgr.kds;

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

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
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
import com.enlight.game.service.account.AccountService;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;
import com.enlight.game.service.es.MoneyPayPServer;
import com.enlight.game.service.es.UserActiveServer;
import com.enlight.game.service.es.UserIncomeServer;
import com.enlight.game.service.platForm.PlatFormService;
import com.enlight.game.service.server.ServerService;
import com.enlight.game.service.serverZone.ServerZoneService;
import com.enlight.game.service.store.StoreService;
import com.enlight.game.util.JsonBinder;
import com.enlight.game.web.controller.mgr.BaseController;

@Controller("KdsMoneyPayPController")
@RequestMapping("/manage/kdsMoneyPayP")
public class KdsMoneyPayPController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(KdsMoneyPayPController.class);
	
	/**
	 * 这个controller默认为kds项目的控制层。项目id文档已定
	 */
	private static final String storeId = "3";
	
	private static final String index = "log_kds_money";
	
	private static final String type_money_arpu_day = "kds_money_arpu_day";
	
	private static final String type_money_arpu_mouth = "kds_money_arpu_mouth";
	
	private static final String type_money_arppu_day = "kds_money_arppu_day";
	
	private static final String type_money_arppu_mouth = "kds_money_arppu_mouth";
	
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
	private AccountService accountService;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private MoneyPayPServer moneyPayPServer;
	
	/**
	 * ARPU(日) ARPU(月) ARPPU(日) ARPPU(月)
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws ElasticsearchException 
	 */
	@RequiresRoles(value = { "admin", "KDS_MONEY" }, logical = Logical.OR)
	@RequestMapping(value = "/kds/moneyPayP", method = RequestMethod.GET)
	public String userMoneyPayP(Model model,ServletRequest request,
			@RequestParam(value = "serverZone", defaultValue = "") String[] sZone,
			@RequestParam(value = "platForm", defaultValue = "") String[] pForm,
			@RequestParam(value = "server", defaultValue = "") String[] sv) throws ElasticsearchException, IOException, ParseException{
		logger.debug("user ARPU(日) ARPU(月) ARPPU(日) ARPPU(月) ...");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Stores stores = storeService.findById(Long.valueOf(storeId));
		List<ServerZone> serverZones = serverZoneService.findAll();
		
		Map<String, Map<String,String>> arpuday = new HashMap<String, Map<String,String>>();
		Map<String, Map<String,String>> arpumouth = new HashMap<String, Map<String,String>>();
		Map<String, Map<String,String>> arppuday = new HashMap<String, Map<String,String>>();
		Map<String, Map<String,String>> arppumouth = new HashMap<String, Map<String,String>>();

		Map<String, Map<String, Object>> n = new HashMap<String, Map<String, Object>>();
		
		List<String> sZones = new ArrayList<String>();
		List<String> pForms = new ArrayList<String>();
		List<String> svs = new ArrayList<String>();

		if(searchParams.isEmpty()){
			//条件为空时
			String dateFrom = thirtyDayAgoFrom();
			String dateTo = nowDate();
			arpuday.put("所有运营大区", moneyPayPServer.searchAllArpuDay(index, type_money_arpu_day, dateFrom, dateTo));
			arpumouth.put("所有运营大区", moneyPayPServer.searchAllArpuMouth(index, type_money_arpu_mouth, dateFrom, dateTo));
			arppuday.put("所有运营大区", moneyPayPServer.searchAllArppuDay(index, type_money_arppu_day, dateFrom, dateTo));
			arppumouth.put("所有运营大区", moneyPayPServer.searchAllArppuMouth(index, type_money_arppu_mouth, dateFrom, dateTo));
			model.addAttribute("dateFrom", dateFrom);
			model.addAttribute("dateTo", dateTo);
			model.addAttribute("platForm", platFormService.findAll());
			model.addAttribute("server", serverService.findByStoreId(storeId));
		}else{
		    if(sZone != null && sZone.length>0){
				for (int i = 0; i < sZone.length; i++) {
					if(sZone[i].equals("all")){
						sZones.add("所有运营大区");
						arpuday.put("所有运营大区", moneyPayPServer.searchAllArpuDay(index, type_money_arpu_day, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString()));
						arpumouth.put("所有运营大区", moneyPayPServer.searchAllArpuMouth(index, type_money_arpu_mouth, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString()));
						arppuday.put("所有运营大区", moneyPayPServer.searchAllArppuDay(index, type_money_arppu_day, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString()));
						arppumouth.put("所有运营大区", moneyPayPServer.searchAllArppuMouth(index, type_money_arppu_mouth, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString()));
					}else{
						String szName = serverZoneService.findById(Long.valueOf(sZone[i])).getServerName();
						sZones.add(szName);
						arpuday.put(szName, moneyPayPServer.searchServerZoneArpuDay(index, type_money_arpu_day, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sZone[i]));
						arpumouth.put(szName, moneyPayPServer.searchServerZoneArpuMouth(index, type_money_arpu_mouth, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sZone[i]));
						arppuday.put(szName, moneyPayPServer.searchServerZoneArppuDay(index, type_money_arppu_day, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sZone[i]));
						arppumouth.put(szName, moneyPayPServer.searchServerZoneArppuMouth(index, type_money_arppu_mouth, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sZone[i]));
					}

				}
			}
			if(pForm != null && pForm.length>0){
				for (int i = 0; i < pForm.length; i++) {
					String pfName = platFormService.findByPfId(pForm[i]).getPfName();
					pForms.add(pfName);
					arpuday.put(pfName, moneyPayPServer.searchPlatFormArpuDay(index, type_money_arpu_day, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(),  pForm[i]));
					arpumouth.put(pfName, moneyPayPServer.searchPlatFormArpuMouth(index, type_money_arpu_mouth, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(),  pForm[i]));
					arppuday.put(pfName, moneyPayPServer.searchPlatFormArppuDay(index, type_money_arppu_day, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(),  pForm[i]));
					arppumouth.put(pfName, moneyPayPServer.searchPlatFormArppuMouth(index, type_money_arppu_mouth, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(),  pForm[i]));
				}
			}
			if(sv != null && sv.length>0){
				for (int i = 0; i < sv.length; i++) {
					svs.add(sv[i]);
					arpuday.put(sv[i], moneyPayPServer.searchServerArpuDay(index, type_money_arpu_day, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sv[i]));
					arpumouth.put(sv[i], moneyPayPServer.searchServerArpuMouth(index, type_money_arpu_mouth, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sv[i]));
					arppuday.put(sv[i], moneyPayPServer.searchServerArppuDay(index, type_money_arppu_day, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sv[i]));
					arppumouth.put(sv[i], moneyPayPServer.searchServerArppuMouth(index, type_money_arppu_mouth, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sv[i]));
				}
			}
		   
		}

		logger.debug(binder.toJson(arpuday));
		logger.debug(binder.toJson(arpumouth));
		logger.debug(binder.toJson(arppuday));
		logger.debug(binder.toJson(arppumouth));
		model.addAttribute("arpuday", binder.toJson(arpuday));
		model.addAttribute("arpumouth", binder.toJson(arpumouth));
		model.addAttribute("arppuday", binder.toJson(arppuday));
		model.addAttribute("arppumouth", binder.toJson(arppumouth));
		
		model.addAttribute("store", stores);
		model.addAttribute("serverZone", serverZones);
		model.addAttribute("platForm", platFormService.findAll());
		model.addAttribute("server", serverService.findByStoreId(storeId));
		
		model.addAttribute("sZones", sZones);
		model.addAttribute("pForms", pForms);
		model.addAttribute("svs", svs);
		
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/kibana/money/moneyPayP";
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
