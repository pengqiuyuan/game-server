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
import com.enlight.game.service.es.UserActiveServer;
import com.enlight.game.service.es.UserIncomeServer;
import com.enlight.game.service.platForm.PlatFormService;
import com.enlight.game.service.server.ServerService;
import com.enlight.game.service.serverZone.ServerZoneService;
import com.enlight.game.service.store.StoreService;
import com.enlight.game.util.JsonBinder;
import com.enlight.game.web.controller.mgr.BaseController;

@Controller("KdsIncomeController")
@RequestMapping("/manage/kdsIncome")
public class KdsIncomeController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(KdsIncomeController.class);
	
	/**
	 * 这个controller默认为kds项目的控制层。项目id文档已定
	 */
	private static final String storeId = "3";
	
	private static final String index = "log_kds_money";
	
	private static final String type_income_sum = "kds_money_income_sum";
	
	private static final String type_income_sum_total = "kds_money_income_sum_total";
	
	private static final String type_income_count = "kds_money_income_count";
	
	private static final String type_income_peoplenum = "kds_money_income_peoplenum";
	
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
	private UserIncomeServer userIncomeServer;
	
	/**
	 * 收入分析 收入金额 充值次数 充值人数
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws ElasticsearchException 
	 */
	@RequiresRoles(value = { "admin", "KDS_MONEY" }, logical = Logical.OR)
	@RequestMapping(value = "/kds/userIncome", method = RequestMethod.GET)
	public String userActive(Model model,ServletRequest request,
			@RequestParam(value = "serverZone", defaultValue = "") String[] sZone,
			@RequestParam(value = "platForm", defaultValue = "") String[] pForm,
			@RequestParam(value = "server", defaultValue = "") String[] sv) throws ElasticsearchException, IOException, ParseException{
		logger.debug("user income ...");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Stores stores = storeService.findById(Long.valueOf(storeId));
		List<ServerZone> serverZones = serverZoneService.findAll();
		
		Map<String, Map<String,String>> sum = new HashMap<String, Map<String,String>>();
		Map<String, Map<String,String>> sumtotal = new HashMap<String, Map<String,String>>();
		Map<String, Map<String,String>> count = new HashMap<String, Map<String,String>>();
		Map<String, Map<String,String>> peoplenum = new HashMap<String, Map<String,String>>();

		List<String> sZones = new ArrayList<String>();
		List<String> pForms = new ArrayList<String>();
		List<String> svs = new ArrayList<String>();

		if(searchParams.isEmpty()){
			//条件为空时
			String dateFrom = thirtyDayAgoFrom();
			String dateTo = nowDate();
			sum.put("所有运营大区", userIncomeServer.searchAllIncomesum(index, type_income_sum, dateFrom, dateTo));
			sumtotal.put("所有运营大区", userIncomeServer.searchAllIncomesumtotal(index, type_income_sum_total, dateFrom, dateTo));
			count.put("所有运营大区", userIncomeServer.searchAllIncomecount(index, type_income_count, dateFrom, dateTo));
			peoplenum.put("所有运营大区", userIncomeServer.searchAllIncomepeoplenum(index, type_income_peoplenum, dateFrom, dateTo));
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
						sum.put("所有运营大区", userIncomeServer.searchAllIncomesum(index, type_income_sum, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString()));
						sumtotal.put("所有运营大区", userIncomeServer.searchAllIncomesumtotal(index, type_income_sum_total, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString()));
						count.put("所有运营大区", userIncomeServer.searchAllIncomecount(index, type_income_count, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString()));
						peoplenum.put("所有运营大区", userIncomeServer.searchAllIncomepeoplenum(index, type_income_peoplenum, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString()));
					}else{
						String szName = serverZoneService.findById(Long.valueOf(sZone[i])).getServerName();
						sZones.add(szName);
						sum.put(szName, userIncomeServer.searchServerZoneIncomesum(index, type_income_sum, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sZone[i]));
						sumtotal.put(szName, userIncomeServer.searchServerZoneIncomesumtotal(index, type_income_sum_total, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sZone[i]));
						count.put(szName, userIncomeServer.searchServerZoneIncomecount(index, type_income_count, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sZone[i]));
						peoplenum.put(szName, userIncomeServer.searchServerZoneIncomepeoplenum(index, type_income_peoplenum, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sZone[i]));
					}

				}
			}
			if(pForm != null && pForm.length>0){
				for (int i = 0; i < pForm.length; i++) {
					String pfName = platFormService.findByPfId(pForm[i]).getPfName();
					pForms.add(pfName);
					sum.put(pfName, userIncomeServer.searchPlatFormIncomesum(index, type_income_sum, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(),  pForm[i]));
					sumtotal.put(pfName, userIncomeServer.searchPlatFormIncomesumtotal(index, type_income_sum_total, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(),  pForm[i]));
					count.put(pfName, userIncomeServer.searchPlatFormIncomecount(index, type_income_count, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(),  pForm[i]));
					peoplenum.put(pfName, userIncomeServer.searchPlatFormIncomepeople(index, type_income_peoplenum, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(),  pForm[i]));
				}
			}
			if(sv != null && sv.length>0){
				for (int i = 0; i < sv.length; i++) {
					svs.add(sv[i]);
					sum.put(sv[i], userIncomeServer.searchServerIncomesum(index, type_income_sum, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sv[i]));
					sumtotal.put(sv[i], userIncomeServer.searchServerIncomesumtotal(index, type_income_sum_total, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sv[i]));
					count.put(sv[i], userIncomeServer.searchServerIncomecount(index, type_income_count, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sv[i]));
					peoplenum.put(sv[i], userIncomeServer.searchServerIncomepeoplenum(index, type_income_peoplenum, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sv[i]));
				}
			}
		   
		}

		logger.debug(binder.toJson(sum));
		logger.debug(binder.toJson(count));
		logger.debug(binder.toJson(peoplenum));
		logger.debug(binder.toJson(sumtotal));
		model.addAttribute("next", binder.toJson(sum));
		model.addAttribute("seven", binder.toJson(count));
		model.addAttribute("thirty", binder.toJson(peoplenum));
		model.addAttribute("nextTotal", binder.toJson(sumtotal));
		
		model.addAttribute("store", stores);
		model.addAttribute("serverZone", serverZones);
		model.addAttribute("platForm", platFormService.findAll());
		model.addAttribute("server", serverService.findByStoreId(storeId));
		
		model.addAttribute("sZones", sZones);
		model.addAttribute("pForms", pForms);
		model.addAttribute("svs", svs);
		
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/kibana/user/userIncome";
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
