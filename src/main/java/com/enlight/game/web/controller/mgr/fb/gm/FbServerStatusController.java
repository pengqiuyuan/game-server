package com.enlight.game.web.controller.mgr.fb.gm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import net.sf.json.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.enlight.game.entity.ServerZone;
import com.enlight.game.entity.Stores;
import com.enlight.game.entity.User;
import com.enlight.game.service.account.AccountService;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;
import com.enlight.game.service.enumCategory.EnumCategoryService;
import com.enlight.game.service.enumFunction.EnumFunctionService;
import com.enlight.game.service.platForm.PlatFormService;
import com.enlight.game.service.server.ServerService;
import com.enlight.game.service.serverZone.ServerZoneService;
import com.enlight.game.service.store.StoreService;
import com.enlight.game.util.HttpClientUts;
import com.enlight.game.util.JsonBinder;
import com.enlight.game.web.controller.mgr.BaseController;
import com.enlight.game.entity.fb.gm.Category;
import com.enlight.game.entity.fb.gm.ServerStatus;
import com.enlight.game.entity.fb.gm.ServerStatusAccount;
import com.enlight.game.entity.fb.gm.ServerStatusList;
import com.google.common.collect.Maps;

@Controller("fbServerStatusController")
@RequestMapping("/manage/gm/fb/serverStatus")
public class FbServerStatusController extends BaseController{

	private static final String PAGE_SIZE = "50";

	private static final Logger logger = LoggerFactory.getLogger(FbServerStatusController.class);
	
	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	
	static {
		sortTypes.put("auto", "自动");
	}

	public static Map<String, String> getSortTypes() {
		return sortTypes;
	}

	public static void setSortTypes(Map<String, String> sortTypes) {
		FbServerStatusController.sortTypes = sortTypes;
	}
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private EnumFunctionService enumFunctionService;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private ServerZoneService serverZoneService;
	
	@Autowired
	private EnumCategoryService enumCategoryService;
	
	@Autowired
	private PlatFormService platFormService;
	
	@Autowired
	private ServerService serverService;
	
	@Value("#{envProps.gm_url}")
	private String gm_url;
	
	/**
	 * @param pageNumber 当前	 
	 * @param pageSize   显示条数
	 * @param sortType  排序
	 * @param model   返回对象
	 * @param request  封装的请	
	 */
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto")String sortType, Model model,
			ServletRequest request){
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		String storeId = request.getParameter("search_LIKE_storeId");
		String serverZoneId =  request.getParameter("search_LIKE_serverZoneId");

		
		if (!u.getRoles().equals(User.USER_ROLE_ADMIN)) {
			List<Stores> stores = new ArrayList<Stores>();
			Stores sto=  storeService.findById(Long.valueOf(user.getStoreId()));
			stores.add(sto);
			List<ServerZone> serverZones = new ArrayList<ServerZone>();
			List<String> s = u.getServerZoneList();
			for (String str : s) {
				ServerZone server = serverZoneService.findById(Long.valueOf(str));
				serverZones.add(server);
			}
			model.addAttribute("stores", stores);
			model.addAttribute("serverZones", serverZones);
		}else{
			List<Stores> stores =  storeService.findList();
			List<ServerZone> serverZones = serverZoneService.findAll();
			model.addAttribute("stores", stores);
			model.addAttribute("serverZones", serverZones);
		}
		
		try {
	        if(!searchParams.isEmpty()){
				if(!u.getRoles().equals(User.USER_ROLE_ADMIN)){
					storeId = user.getStoreId();
				}
				String gs = HttpClientUts.doGet(gm_url+"/fbserver/server/getAllServer"+"?serverZoneId="+serverZoneId+"&gameId="+storeId+"&pageNumber="+pageNumber+"&pageSize="+pageSize, "utf-8");
				String total = HttpClientUts.doGet(gm_url+"/fbserver/getTotalByServerZoneIdAndGameId"+"?serverZoneId="+serverZoneId+"&gameId="+storeId+"&category="+Category.server, "utf-8");
				JSONObject dataJson=JSONObject.fromObject(total);			
				PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		        List<ServerStatus> beanList = binder.getMapper().readValue(gs, new TypeReference<List<ServerStatus>>() {}); 
		        PageImpl<ServerStatus> serverStatus = new PageImpl<ServerStatus>(beanList, pageRequest, Long.valueOf(dataJson.get("num").toString()));
				model.addAttribute("serverStatus", serverStatus);
	        }else{
	        	List<ServerStatus> beanList = new ArrayList<ServerStatus>();
				PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		        PageImpl<ServerStatus> serverStatus = new PageImpl<ServerStatus>(beanList, pageRequest, 0);
				model.addAttribute("serverStatus", serverStatus);
	        }

		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/gm/fb/serverStatus/index";
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(value = "/update",method=RequestMethod.POST)
	public String update(ServletRequest request,RedirectAttributes redirectAttributes){
		String[] checkIds = request.getParameterValues("checkId");
		String checkStatus = request.getParameter("checkStatus");
		String gameId = request.getParameter("search_LIKE_storeId");
		String serverZoneId = request.getParameter("search_LIKE_serverZoneId");
		ServerStatusList list = new ServerStatusList();
		list.setId(checkIds);
		list.setStatus(checkStatus);
		JSONObject res = HttpClientUts.doPost(gm_url+"/fbserver/server/updateServers" , JSONObject.fromObject(list));
		redirectAttributes.addFlashAttribute("message", "修改"+res.getString("message"));
		return "redirect:/manage/gm/fb/serverStatus/index?search_LIKE_storeId="+gameId+"&search_LIKE_serverZoneId="+serverZoneId;
	}

	
	@RequestMapping(value = "accountIndex", method = RequestMethod.GET)
	public String accountindex(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto")String sortType, Model model,
			ServletRequest request){
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		String storeId = request.getParameter("search_LIKE_storeId");
		String serverZoneId =  request.getParameter("search_LIKE_serverZoneId");
		
		if (!u.getRoles().equals(User.USER_ROLE_ADMIN)) {
			List<Stores> stores = new ArrayList<Stores>();
			Stores sto=  storeService.findById(Long.valueOf(user.getStoreId()));
			stores.add(sto);
			List<ServerZone> serverZones = new ArrayList<ServerZone>();
			List<String> s = u.getServerZoneList();
			for (String str : s) {
				ServerZone server = serverZoneService.findById(Long.valueOf(str));
				serverZones.add(server);
			}
			model.addAttribute("stores", stores);
			model.addAttribute("serverZones", serverZones);
		}else{
			List<Stores> stores =  storeService.findList();
			List<ServerZone> serverZones = serverZoneService.findAll();
			model.addAttribute("stores", stores);
			model.addAttribute("serverZones", serverZones);
		}
		
		try {
			/**
			String gs = "[{\"id\":1,\"serverzoneId\":\"Sample text\",\"gameId\":\"Sample text\",\"serverId\":\"Sample text\",\"platFormId\":\"Sample text\",\"account\":\"Sample text\"}"
					+ ",{\"id\":2,\"serverzoneId\":\"Sample text\",\"gameId\":\"Sample text\",\"serverId\":\"Sample text\",\"platFormId\":\"Sample text\",\"account\":\"Sample text\"}"
					+ ",{\"id\":3,\"serverzoneId\":\"Sample text\",\"gameId\":\"Sample text\",\"serverId\":\"Sample text\",\"platFormId\":\"Sample text\",\"account\":\"Sample text\"}"
					+ ",{\"id\":4,\"serverzoneId\":\"Sample text\",\"gameId\":\"Sample text\",\"serverId\":\"Sample text\",\"platFormId\":\"Sample text\",\"account\":\"Sample text\"}]";
			**/
	        if(!searchParams.isEmpty()){
				if(!u.getRoles().equals(User.USER_ROLE_ADMIN)){
					storeId = user.getStoreId();
				}
				String gs = HttpClientUts.doGet(gm_url+"/fbserver/server/getAllGrayAccount"+"?serverZoneId="+serverZoneId+"&gameId="+storeId+"&pageNumber="+pageNumber+"&pageSize="+pageSize, "utf-8");
				String total = HttpClientUts.doGet(gm_url+"/fbserver/getTotalByServerZoneIdAndGameId"+"?serverZoneId="+serverZoneId+"&gameId="+storeId+"&category="+Category.account, "utf-8");
				JSONObject dataJson=JSONObject.fromObject(total);
				
				PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		        List<ServerStatusAccount> beanList = binder.getMapper().readValue(gs, new TypeReference<List<ServerStatusAccount>>() {}); 
		        PageImpl<ServerStatusAccount> serverStatusAccount = new PageImpl<ServerStatusAccount>(beanList, pageRequest, Long.valueOf(dataJson.get("num").toString()));
				model.addAttribute("serverStatusAccount", serverStatusAccount);
	        }else{
	        	List<ServerStatusAccount> beanList = new ArrayList<ServerStatusAccount>();
				PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		        PageImpl<ServerStatusAccount> serverStatusAccount = new PageImpl<ServerStatusAccount>(beanList, pageRequest, 0);
				model.addAttribute("serverStatusAccount", serverStatusAccount);
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/gm/fb/serverStatus/accountIndex";
	}
	
	/**
	 * 新增页面
	 */
	@RequestMapping(value = "/accountAdd" ,method=RequestMethod.GET)
	public String accountAdd(Model model){
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		if (!u.getRoles().equals(User.USER_ROLE_ADMIN)) {
			List<Stores> stores = new ArrayList<Stores>();
			Stores sto=  storeService.findById(Long.valueOf(user.getStoreId()));
			stores.add(sto);
			List<ServerZone> serverZones = new ArrayList<ServerZone>();
			List<String> s = u.getServerZoneList();
			for (String str : s) {
				ServerZone server = serverZoneService.findById(Long.valueOf(str));
				serverZones.add(server);
			}
			model.addAttribute("stores", stores);
			model.addAttribute("serverZones", serverZones);
		}else{
			List<Stores> stores =  storeService.findList();
			List<ServerZone> serverZones = serverZoneService.findAll();
			model.addAttribute("stores", stores);
			model.addAttribute("serverZones", serverZones);
		}
		return "/gm/fb/serverStatus/accountAdd";
	}
	
	/**
	 * 新增
	 */
	@RequestMapping(value = "/accountSave",method=RequestMethod.POST)
	public String accountSave(ServerStatusAccount ServerStatusAccount,ServletRequest request,RedirectAttributes redirectAttributes,Model model){
		System.out.println(ServerStatusAccount.getGameId() + "  "  + ServerStatusAccount.getServerZoneId()+ "  "  + ServerStatusAccount.getServerId()+ "  "  +ServerStatusAccount.getPlatFormId() + "  "  + ServerStatusAccount.getAccount() );
		JSONObject res = HttpClientUts.doPost(gm_url+"/fbserver/server/addGrayAccount" , JSONObject.fromObject(ServerStatusAccount));
		redirectAttributes.addFlashAttribute("message", "新增"+res.getString("message"));
		return "redirect:/manage/gm/fb/serverStatus/accountIndex?search_LIKE_storeId="+ServerStatusAccount.getGameId()+"&search_LIKE_serverZoneId="+ServerStatusAccount.getServerZoneId();
	}
	
	@RequestMapping(value="/accountEdit",method=RequestMethod.GET)
	public String accountEdit(@RequestParam(value="id")long id,Model model){
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		if (!u.getRoles().equals(User.USER_ROLE_ADMIN)) {
			List<Stores> stores = new ArrayList<Stores>();
			Stores sto=  storeService.findById(Long.valueOf(user.getStoreId()));
			stores.add(sto);
			List<ServerZone> serverZones = new ArrayList<ServerZone>();
			List<String> s = u.getServerZoneList();
			for (String str : s) {
				ServerZone server = serverZoneService.findById(Long.valueOf(str));
				serverZones.add(server);
			}
			model.addAttribute("stores", stores);
			model.addAttribute("serverZones", serverZones);
		}else{
			List<Stores> stores =  storeService.findList();
			List<ServerZone> serverZones = serverZoneService.findAll();
			model.addAttribute("stores", stores);
			model.addAttribute("serverZones", serverZones);
		}
		String account;
		try {
			account = HttpClientUts.doGet(gm_url+"/fbserver/server/getGrayAccountByAccountId"+"?id="+id, "utf-8");
			ServerStatusAccount beanList = binder.getMapper().readValue(account, new TypeReference<ServerStatusAccount>() {}); 
			model.addAttribute("account", beanList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "/gm/fb/serverStatus/accountEdit";
	}
	
	
	/**
	 * 更新
	 */
	@RequestMapping(value = "/accountUpdate",method=RequestMethod.POST)
	public String accountUpdate(ServerStatusAccount ServerStatusAccount,ServletRequest request,RedirectAttributes redirectAttributes,Model model){
		JSONObject res = HttpClientUts.doPost(gm_url+"/fbserver/server/updateGrayAccount" , JSONObject.fromObject(ServerStatusAccount));
		redirectAttributes.addFlashAttribute("message", "修改"+res.getString("message"));
		return "redirect:/manage/gm/fb/serverStatus/accountIndex?search_LIKE_storeId="+ServerStatusAccount.getGameId()+"&search_LIKE_serverZoneId="+ServerStatusAccount.getServerZoneId();
	}
	
	/**
	 * 删除操作	 
	 * @param oid 用户id
	 * @throws Exception 
	 */
	@RequestMapping(value = "accountDel", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> accountDel(@RequestParam(value = "id")Long id) throws Exception{
		 Map<String,Object> map = new HashMap<String, Object>();
		 String	account = HttpClientUts.doGet(gm_url+"/fbserver/server/delGrayAccountById"+"?id="+id, "utf-8");
		 JSONObject dataJson=JSONObject.fromObject(account);
		 map.put("success", dataJson.get("message"));
		 return map;
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	public Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
	
	public ShiroUser getCurrentUser() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user;
	}
	
	
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} else if ("registerDate".equals(sortType)) {
			sort = new Sort(Direction.DESC, "registerDate");
		}
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	
}
