package com.enlight.game.web.controller.mgr.fb;

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
import com.enlight.game.service.es.UserPayServer;
import com.enlight.game.service.platForm.PlatFormService;
import com.enlight.game.service.server.ServerService;
import com.enlight.game.service.serverZone.ServerZoneService;
import com.enlight.game.service.store.StoreService;
import com.enlight.game.util.JsonBinder;
import com.enlight.game.web.controller.mgr.BaseController;

@Controller("FbUserPayController")
@RequestMapping("/manage/fbUserPay")
public class FbUserPayController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(FbUserPayController.class);
	
	/**
	 * 这个controller默认为fb项目的控制层。项目id文档已定
	 */
	private static final String storeId = "1";
	
	private static final String index_money = "log_fb_money";
	
	private static final String type_add = "fb_money_add";
	
	private static final String type_all = "fb_money_all";
	
	private static final String type_day = "fb_money_day"; //首日付费率
	
	private static final String type_week = "fb_money_week"; //首周付费率
	
	private static final String type_mouth = "fb_money_mouth"; //首月付费率
	
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
	private UserPayServer userPayServer;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private StoreService storeService;
	
	/**
	 * 付费用户新增、累计
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws ElasticsearchException 
	 */
	@RequiresRoles(value = { "admin", "FB_USER" }, logical = Logical.OR)
	@RequestMapping(value = "/fb/userPay", method = RequestMethod.GET)
	public String userPay(Model model,ServletRequest request,
			@RequestParam(value = "serverZone", defaultValue = "") String[] sZone,
			@RequestParam(value = "platForm", defaultValue = "") String[] pForm,
			@RequestParam(value = "server", defaultValue = "") String[] sv) throws ElasticsearchException, IOException, ParseException{
		logger.debug("user add total...");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Stores stores = storeService.findById(Long.valueOf(storeId));
		List<ServerZone> serverZones = serverZoneService.findAll();
		Map<String, Map<String,String>> mTotal = new HashMap<String, Map<String,String>>();
		Map<String, Map<String,String>> mAdd = new HashMap<String, Map<String,String>>();

		Map<String, String> mT = new HashMap<String, String>();
		Map<String, String> mA = new HashMap<String, String>();
		
		List<String> sZones = new ArrayList<String>();
		List<String> pForms = new ArrayList<String>();
		List<String> svs = new ArrayList<String>();

		if(searchParams.isEmpty()){
			//条件为空时
			String dateFrom = thirtyDayAgoFrom();
			String dateTo = nowDate();
			mT = userPayServer.searchAllUserAll(index_money, type_all, dateFrom, dateTo);
			mA = userPayServer.searchAllUserAdd(index_money, type_add, dateFrom, dateTo);
			mTotal.put("所有运营大区", mT);
			mAdd.put("所有运营大区", mA);
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
						mT = userPayServer.searchAllUserAll(index_money, type_all, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString());
						mA = userPayServer.searchAllUserAdd(index_money, type_add, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString());
						mTotal.put("所有运营大区", mT);
						mAdd.put("所有运营大区", mA);
					}else{
						String szName = serverZoneService.findById(Long.valueOf(sZone[i])).getServerName();
						sZones.add(szName);
						mT = userPayServer.searchServerZoneUserAll(index_money, type_all, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sZone[i]);
						mA = userPayServer.searchServerZoneUserAdd(index_money, type_add, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sZone[i]);
						mTotal.put(szName, mT);
						mAdd.put(szName, mA);
					}

				}
			}
			if(pForm != null && pForm.length>0){
				for (int i = 0; i < pForm.length; i++) {
					String pfName = platFormService.findByPfId(pForm[i]).getPfName();
					pForms.add(pfName);
					mT = userPayServer.searchPlatFormUserAll(index_money, type_all, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), pForm[i]);
					mA = userPayServer.searchPlatFormUserAdd(index_money, type_add, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), pForm[i]);
					mTotal.put(pfName, mT);
					mAdd.put(pfName, mA);
				}
			}
			if(sv != null && sv.length>0){
				for (int i = 0; i < sv.length; i++) {
					svs.add(sv[i]);
					mT = userPayServer.searchServerUserAll(index_money, type_all, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sv[i]);
					mA = userPayServer.searchServerUserAdd(index_money, type_add, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sv[i]);
					mTotal.put(sv[i], mT);
					mAdd.put(sv[i], mA);
				}
			}
		   
		}

		logger.debug(binder.toJson(mTotal));
		logger.debug(binder.toJson(mAdd));
		model.addAttribute("mTotal", binder.toJson(mTotal));
		model.addAttribute("mAdd", binder.toJson(mAdd));
		
		model.addAttribute("store", stores);
		model.addAttribute("serverZone", serverZones);
		model.addAttribute("platForm", platFormService.findAll());
		model.addAttribute("server", serverService.findByStoreId(storeId));
		
		model.addAttribute("sZones", sZones);
		model.addAttribute("pForms", pForms);
		model.addAttribute("svs", svs);
		
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/kibana/user/userPay";
	}
	
	/**
	 * 每日付费率
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws ElasticsearchException 
	 */
	@RequiresRoles(value = { "admin", "FB_USER" }, logical = Logical.OR)
	@RequestMapping(value = "/fb/userDay", method = RequestMethod.GET)
	public String userDay(Model model,ServletRequest request,
			@RequestParam(value = "serverZone", defaultValue = "") String[] sZone,
			@RequestParam(value = "platForm", defaultValue = "") String[] pForm,
			@RequestParam(value = "server", defaultValue = "") String[] sv) throws ElasticsearchException, IOException, ParseException{
		logger.debug("user userDay 每日付费率...");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Stores stores = storeService.findById(Long.valueOf(storeId));
		List<ServerZone> serverZones = serverZoneService.findAll();
		Map<String, Map<String,String>> mTotal = new HashMap<String, Map<String,String>>();
		Map<String, Map<String,String>> mAdd = new HashMap<String, Map<String,String>>();

		Map<String, String> mT = new HashMap<String, String>();
		Map<String, String> mA = new HashMap<String, String>();
		
		List<String> sZones = new ArrayList<String>();
		List<String> pForms = new ArrayList<String>();
		List<String> svs = new ArrayList<String>();

		if(searchParams.isEmpty()){
			//条件为空时
			String dateFrom = thirtyDayAgoFrom();
			String dateTo = nowDate();
			mT = userPayServer.searchAllUserDay(index_money, type_day, dateFrom, dateTo);
			mA = userPayServer.searchAllUserDaynum(index_money, type_day, dateFrom, dateTo);
			mTotal.put("所有运营大区", mT);
			mAdd.put("所有运营大区", mA);
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
						mT = userPayServer.searchAllUserDay(index_money, type_day, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString());
						mA = userPayServer.searchAllUserDaynum(index_money, type_day, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString());
						mTotal.put("所有运营大区", mT);
						mAdd.put("所有运营大区", mA);
					}else{
						String szName = serverZoneService.findById(Long.valueOf(sZone[i])).getServerName();
						sZones.add(szName);
						mT = userPayServer.searchServerZoneUserDay(index_money, type_day, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sZone[i]);
						mA = userPayServer.searchServerZoneUserDaynum(index_money, type_day, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sZone[i]);
						mTotal.put(szName, mT);
						mAdd.put(szName, mA);
					}

				}
			}
			if(pForm != null && pForm.length>0){
				for (int i = 0; i < pForm.length; i++) {
					String pfName = platFormService.findByPfId(pForm[i]).getPfName();
					pForms.add(pfName);
					mT = userPayServer.searchPlatFormUserDay(index_money, type_day, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), pForm[i]);
					mA = userPayServer.searchPlatFormUserDaynum(index_money, type_day, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), pForm[i]);
					mTotal.put(pfName, mT);
					mAdd.put(pfName, mA);
				}
			}
			if(sv != null && sv.length>0){
				for (int i = 0; i < sv.length; i++) {
					svs.add(sv[i]);
					mT = userPayServer.searchServerUserDay(index_money, type_day, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sv[i]);
					mA = userPayServer.searchServerUserDaynum(index_money, type_day, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sv[i]);
					mTotal.put(sv[i], mT);
					mAdd.put(sv[i], mA);
				}
			}
		   
		}

		logger.debug(binder.toJson(mTotal));
		logger.debug(binder.toJson(mAdd));
		model.addAttribute("mTotal", binder.toJson(mTotal));
		model.addAttribute("mAdd", binder.toJson(mAdd));
		
		model.addAttribute("store", stores);
		model.addAttribute("serverZone", serverZones);
		model.addAttribute("platForm", platFormService.findAll());
		model.addAttribute("server", serverService.findByStoreId(storeId));
		
		model.addAttribute("sZones", sZones);
		model.addAttribute("pForms", pForms);
		model.addAttribute("svs", svs);
		
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/kibana/user/userDay";
	}
	
	/**
	 * 每周付费率
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws ElasticsearchException 
	 */
	@RequiresRoles(value = { "admin", "FB_USER" }, logical = Logical.OR)
	@RequestMapping(value = "/fb/userWeek", method = RequestMethod.GET)
	public String userWeek(Model model,ServletRequest request,
			@RequestParam(value = "serverZone", defaultValue = "") String[] sZone,
			@RequestParam(value = "platForm", defaultValue = "") String[] pForm,
			@RequestParam(value = "server", defaultValue = "") String[] sv) throws ElasticsearchException, IOException, ParseException{
		logger.debug("user userDay 每周付费率...");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Stores stores = storeService.findById(Long.valueOf(storeId));
		List<ServerZone> serverZones = serverZoneService.findAll();
		Map<String, Map<String,String>> mTotal = new HashMap<String, Map<String,String>>();
		Map<String, Map<String,String>> mAdd = new HashMap<String, Map<String,String>>();

		Map<String, String> mT = new HashMap<String, String>();
		Map<String, String> mA = new HashMap<String, String>();
		
		List<String> sZones = new ArrayList<String>();
		List<String> pForms = new ArrayList<String>();
		List<String> svs = new ArrayList<String>();

		if(searchParams.isEmpty()){
			//条件为空时
			String dateFrom = thirtyDayAgoFrom();
			String dateTo = nowDate();
			mT = userPayServer.searchAllUserWeek(index_money, type_week, dateFrom, dateTo);
			mA = userPayServer.searchAllUserWeeknum(index_money, type_week, dateFrom, dateTo);
			mTotal.put("所有运营大区", mT);
			mAdd.put("所有运营大区", mA);
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
						mT = userPayServer.searchAllUserWeek(index_money, type_week, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString());
						mA = userPayServer.searchAllUserWeeknum(index_money, type_week, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString());
						mTotal.put("所有运营大区", mT);
						mAdd.put("所有运营大区", mA);
					}else{
						String szName = serverZoneService.findById(Long.valueOf(sZone[i])).getServerName();
						sZones.add(szName);
						mT = userPayServer.searchServerZoneUserWeek(index_money, type_week, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sZone[i]);
						mA = userPayServer.searchServerZoneUserWeeknum(index_money, type_week, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sZone[i]);
						mTotal.put(szName, mT);
						mAdd.put(szName, mA);
					}

				}
			}
			if(pForm != null && pForm.length>0){
				for (int i = 0; i < pForm.length; i++) {
					String pfName = platFormService.findByPfId(pForm[i]).getPfName();
					pForms.add(pfName);
					mT = userPayServer.searchPlatFormUserWeek(index_money, type_week, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), pForm[i]);
					mA = userPayServer.searchPlatFormUserWeeknum(index_money, type_week, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), pForm[i]);
					mTotal.put(pfName, mT);
					mAdd.put(pfName, mA);
				}
			}
			if(sv != null && sv.length>0){
				for (int i = 0; i < sv.length; i++) {
					svs.add(sv[i]);
					mT = userPayServer.searchServerUserWeek(index_money, type_week, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sv[i]);
					mA = userPayServer.searchServerUserWeeknum(index_money, type_week, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sv[i]);
					mTotal.put(sv[i], mT);
					mAdd.put(sv[i], mA);
				}
			}
		   
		}

		logger.debug(binder.toJson(mTotal));
		logger.debug(binder.toJson(mAdd));
		model.addAttribute("mTotal", binder.toJson(mTotal));
		model.addAttribute("mAdd", binder.toJson(mAdd));
		
		model.addAttribute("store", stores);
		model.addAttribute("serverZone", serverZones);
		model.addAttribute("platForm", platFormService.findAll());
		model.addAttribute("server", serverService.findByStoreId(storeId));
		
		model.addAttribute("sZones", sZones);
		model.addAttribute("pForms", pForms);
		model.addAttribute("svs", svs);
		
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/kibana/user/userWeek";
	}
	
	/**
	 * 每月付费率
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws ElasticsearchException 
	 */
	@RequiresRoles(value = { "admin", "FB_USER" }, logical = Logical.OR)
	@RequestMapping(value = "/fb/userMouth", method = RequestMethod.GET)
	public String userMouth(Model model,ServletRequest request,
			@RequestParam(value = "serverZone", defaultValue = "") String[] sZone,
			@RequestParam(value = "platForm", defaultValue = "") String[] pForm,
			@RequestParam(value = "server", defaultValue = "") String[] sv) throws ElasticsearchException, IOException, ParseException{
		logger.debug("user userDay 每月付费率...");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Stores stores = storeService.findById(Long.valueOf(storeId));
		List<ServerZone> serverZones = serverZoneService.findAll();
		Map<String, Map<String,String>> mTotal = new HashMap<String, Map<String,String>>();
		Map<String, Map<String,String>> mAdd = new HashMap<String, Map<String,String>>();

		Map<String, String> mT = new HashMap<String, String>();
		Map<String, String> mA = new HashMap<String, String>();
		
		List<String> sZones = new ArrayList<String>();
		List<String> pForms = new ArrayList<String>();
		List<String> svs = new ArrayList<String>();

		if(searchParams.isEmpty()){
			//条件为空时
			String dateFrom = thirtyDayAgoFrom();
			String dateTo = nowDate();
			mT = userPayServer.searchAllUserMouth(index_money, type_mouth, dateFrom, dateTo);
			mA = userPayServer.searchAllUserMouthnum(index_money, type_mouth, dateFrom, dateTo);
			mTotal.put("所有运营大区", mT);
			mAdd.put("所有运营大区", mA);
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
						mT = userPayServer.searchAllUserMouth(index_money, type_mouth, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString());
						mA = userPayServer.searchAllUserMouthnum(index_money, type_mouth, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString());
						mTotal.put("所有运营大区", mT);
						mAdd.put("所有运营大区", mA);
					}else{
						String szName = serverZoneService.findById(Long.valueOf(sZone[i])).getServerName();
						sZones.add(szName);
						mT = userPayServer.searchServerZoneUserMouth(index_money, type_mouth, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sZone[i]);
						mA = userPayServer.searchServerZoneUserMouthnum(index_money, type_mouth, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sZone[i]);
						mTotal.put(szName, mT);
						mAdd.put(szName, mA);
					}

				}
			}
			if(pForm != null && pForm.length>0){
				for (int i = 0; i < pForm.length; i++) {
					String pfName = platFormService.findByPfId(pForm[i]).getPfName();
					pForms.add(pfName);
					mT = userPayServer.searchPlatFormUserMouth(index_money, type_mouth, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), pForm[i]);
					mA = userPayServer.searchPlatFormUserMouthnum(index_money, type_mouth, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), pForm[i]);
					mTotal.put(pfName, mT);
					mAdd.put(pfName, mA);
				}
			}
			if(sv != null && sv.length>0){
				for (int i = 0; i < sv.length; i++) {
					svs.add(sv[i]);
					mT = userPayServer.searchServerUserMouth(index_money, type_mouth, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sv[i]);
					mA = userPayServer.searchServerUserMouthnum(index_money, type_mouth, searchParams.get("EQ_dateFrom").toString(), searchParams.get("EQ_dateTo").toString(), sv[i]);
					mTotal.put(sv[i], mT);
					mAdd.put(sv[i], mA);
				}
			}
		   
		}

		logger.debug(binder.toJson(mTotal));
		logger.debug(binder.toJson(mAdd));
		model.addAttribute("mTotal", binder.toJson(mTotal));
		model.addAttribute("mAdd", binder.toJson(mAdd));
		
		model.addAttribute("store", stores);
		model.addAttribute("serverZone", serverZones);
		model.addAttribute("platForm", platFormService.findAll());
		model.addAttribute("server", serverService.findByStoreId(storeId));
		
		model.addAttribute("sZones", sZones);
		model.addAttribute("pForms", pForms);
		model.addAttribute("svs", svs);
		
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/kibana/user/userMouth";
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
