package com.enlight.game.web.controller.mgr.xyj.gm;

import java.net.URLEncoder;
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
import org.springframework.data.domain.Page;
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
import org.apache.commons.lang3.StringUtils;

import com.enlight.game.base.AppBizException;
import com.enlight.game.entity.PlatForm;
import com.enlight.game.entity.User;
import com.enlight.game.service.account.AccountService;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;
import com.enlight.game.service.enumCategory.EnumCategoryService;
import com.enlight.game.service.enumFunction.EnumFunctionService;
import com.enlight.game.service.go.GoAllPlatFormService;
import com.enlight.game.service.go.GoAllServerService;
import com.enlight.game.service.go.GoServerZoneService;
import com.enlight.game.service.go.GoStoreService;
import com.enlight.game.service.platForm.PlatFormService;
import com.enlight.game.service.server.ServerService;
import com.enlight.game.util.HttpClientUts;
import com.enlight.game.util.JsonBinder;
import com.enlight.game.web.controller.mgr.BaseController;
import com.enlight.game.entity.gm.xyj.Category;
import com.enlight.game.entity.gm.xyj.ServerStatusAccount;
import com.enlight.game.entity.gm.xyj.ServerStatusList;
import com.enlight.game.entity.go.GoAllServer;
import com.enlight.game.entity.go.GoServerZone;
import com.enlight.game.entity.go.GoStore;
import com.google.common.collect.Maps;

@Controller("xyjServerStatusController")
@RequestMapping("/manage/gm/xyj/serverStatus")
public class XyjServerStatusController extends BaseController{

	private static final String PAGE_SIZE = "50";
	
	private static final Integer XYJ = 4; //数据库、excel表 ，xyj项目storeId为4

	private static final Logger logger = LoggerFactory.getLogger(XyjServerStatusController.class);
	
	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	
	static {
		sortTypes.put("auto", "自动");
	}

	public static Map<String, String> getSortTypes() {
		return sortTypes;
	}

	public static void setSortTypes(Map<String, String> sortTypes) {
		XyjServerStatusController.sortTypes = sortTypes;
	}
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private EnumFunctionService enumFunctionService;
	
	@Autowired
	private EnumCategoryService enumCategoryService;
	
	@Autowired
	private PlatFormService platFormService;
	
	@Autowired
	private ServerService serverService;
	
	@Autowired
	private GoStoreService goStoreService;
	
	@Autowired
	private GoServerZoneService  goServerZoneService;
	
	@Autowired
	private GoAllServerService goAllServerService;
	
	@Autowired
	private GoAllPlatFormService goAllPlatFormService;
	
	
	@Value("#{envProps.gm_url}")
	private String gm_url;
	
	/**
	 * @param pageNumber 当前	 
	 * @param pageSize   显示条数
	 * @param sortType  排序
	 * @param model   返回对象
	 * @param request  封装的请求	
	 */
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto")String sortType, Model model,
			ServletRequest request){
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		
		if (!u.getRoles().equals(User.USER_ROLE_ADMIN)) {
			List<GoStore> goStores = new ArrayList<GoStore>();
			GoStore goStore = goStoreService.findByStoreId(Integer.valueOf(user.getStoreId()));
			if(goStore!=null){
				goStores.add(goStore);
			}
			List<GoServerZone> goServerZones = new ArrayList<GoServerZone>();
			List<String> s = u.getServerZoneList();
			for (String str : s) {	
				GoServerZone goServerZone = goServerZoneService.findByServerZoneIdAndStoreId(Integer.valueOf(str), Integer.valueOf(user.getStoreId()));
				if(goServerZone!=null){
					goServerZones.add(goServerZone);
				}
			}
			model.addAttribute("stores", goStores);
			model.addAttribute("serverZones", goServerZones);
		}else{
			List<GoStore> goStores = new ArrayList<GoStore>();
			GoStore goStore = goStoreService.findByStoreId(XYJ);
			if(goStore!=null){
				goStores.add(goStore);
			}
			List<GoServerZone> goServerZones = goServerZoneService.findByStoreId(XYJ);
			model.addAttribute("stores", goStores);
			model.addAttribute("serverZones", goServerZones);
		}
		
		try {
	        if(!searchParams.isEmpty()){
	        	Page<GoAllServer> serverStatus = goAllServerService.findGoAllServerByCondition(user.id,searchParams, pageNumber, pageSize, sortType);
				model.addAttribute("serverStatus", serverStatus);
	        }else{
	        	List<GoAllServer> beanList = new ArrayList<GoAllServer>();
				PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		        PageImpl<GoAllServer> serverStatus = new PageImpl<GoAllServer>(beanList, pageRequest, 0);
				model.addAttribute("serverStatus", serverStatus);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/gm/xyj/serverStatus/index";
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(value = "/update",method=RequestMethod.POST)
	public String update(ServletRequest request,RedirectAttributes redirectAttributes){
		String[] checkIds = request.getParameterValues("checkId");
		String checkStatus = request.getParameter("checkStatus");
		String gameId = request.getParameter("search_EQ_storeId");
		String serverZoneId = request.getParameter("search_EQ_serverZoneId");
		String s = StringUtils.join(checkIds,",");
		ServerStatusList list = new ServerStatusList();
		list.setServerId(s);
		list.setStatus(checkStatus);
		JSONObject res = HttpClientUts.doPost(gm_url+"/xyjserver/server/updateServers" , JSONObject.fromObject(list));
		redirectAttributes.addFlashAttribute("message", "选择"+res.getString("choose")+"个，成功"+res.getString("success")+"个，失败"+res.getString("fail")+"个，失败的服务器有："+res.getString("objFail"));
		return "redirect:/manage/gm/xyj/serverStatus/index?search_EQ_storeId="+gameId+"&search_EQ_serverZoneId="+serverZoneId;
	}

	
	@RequestMapping(value = "accountIndex", method = RequestMethod.GET)
	public String accountindex(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto")String sortType, Model model,
			ServletRequest request){
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		String storeId = request.getParameter("search_EQ_storeId");
		String serverZoneId =  request.getParameter("search_EQ_serverZoneId");
		String serverId = request.getParameter("search_EQ_serverId");
		
		if (!u.getRoles().equals(User.USER_ROLE_ADMIN)) {
			List<GoStore> goStores = new ArrayList<GoStore>();
			GoStore goStore = goStoreService.findByStoreId(Integer.valueOf(user.getStoreId()));
			if(goStore!=null){
				goStores.add(goStore);
			}
			List<GoServerZone> goServerZones = new ArrayList<GoServerZone>();
			List<String> s = u.getServerZoneList();
			for (String str : s) {	
				GoServerZone goServerZone = goServerZoneService.findByServerZoneIdAndStoreId(Integer.valueOf(str), Integer.valueOf(user.getStoreId()));
				if(goServerZone!=null){
					goServerZones.add(goServerZone);
				}
			}
			model.addAttribute("stores", goStores);
			model.addAttribute("serverZones", goServerZones);
		}else{
			List<GoStore> goStores = new ArrayList<GoStore>();
			GoStore goStore = goStoreService.findByStoreId(XYJ);
			if(goStore!=null){
				goStores.add(goStore);
			}
			List<GoServerZone> goServerZones = goServerZoneService.findByStoreId(XYJ);
			model.addAttribute("stores", goStores);
			model.addAttribute("serverZones", goServerZones);
		}
		
		try {
	        if(!searchParams.isEmpty() && null != request.getParameter("search_EQ_serverId")){
				if(!u.getRoles().equals(User.USER_ROLE_ADMIN)){
					storeId = user.getStoreId();
				}
				String gs = HttpClientUts.doGet(gm_url+"/xyjserver/server/getAllGrayAccount"+"?serverZoneId="+serverZoneId+"&gameId="+storeId+"&serverId="+URLEncoder.encode(serverId, "utf-8")+"&pageNumber="+pageNumber+"&pageSize="+pageSize, "utf-8");
				String total = HttpClientUts.doGet(gm_url+"/xyjserver/getTotalByServerZoneIdAndGameId"+"?serverZoneId="+serverZoneId+"&gameId="+storeId+"&category="+Category.account+"&serverId="+URLEncoder.encode(serverId, "utf-8"), "utf-8");
				JSONObject dataJson=JSONObject.fromObject(total);
				
				PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		        List<ServerStatusAccount> beanList = binder.getMapper().readValue(gs, new TypeReference<List<ServerStatusAccount>>() {}); 
		        PageImpl<ServerStatusAccount> serverStatusAccount = new PageImpl<ServerStatusAccount>(beanList, pageRequest, Long.valueOf(dataJson.get("num").toString()));
				model.addAttribute("serverStatusAccount", serverStatusAccount);
				
				List<GoAllServer> servers = goAllServerService.findAllByStoreIdAndServerZoneId(Integer.valueOf(request.getParameter("search_EQ_storeId")),Integer.valueOf(serverZoneId));
				model.addAttribute("servers", servers);
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
		return "/gm/xyj/serverStatus/accountIndex";
	}
	
	/**
	 * 新增页面
	 */
	@RequestMapping(value = "/accountAdd" ,method=RequestMethod.GET)
	public String accountAdd(Model model){
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		if (!u.getRoles().equals(User.USER_ROLE_ADMIN)) {
			List<GoStore> goStores = new ArrayList<GoStore>();
			GoStore goStore = goStoreService.findByStoreId(Integer.valueOf(user.getStoreId()));
			if(goStore!=null){
				goStores.add(goStore);
			}
			List<GoServerZone> goServerZones = new ArrayList<GoServerZone>();
			//List<GoAllServer> goAllServers = new ArrayList<GoAllServer>();
			List<String> s = u.getServerZoneList();
			for (String str : s) {	
				GoServerZone goServerZone = goServerZoneService.findByServerZoneIdAndStoreId(Integer.valueOf(str), Integer.valueOf(user.getStoreId()));
				//List<GoAllServer> gs= goAllServerService.findAllByStoreIdAndServerZoneId(Integer.valueOf(user.getStoreId()), Integer.valueOf(str));
				if(goServerZone!=null){
					goServerZones.add(goServerZone);
				}
			}
			model.addAttribute("stores", goStores);
			model.addAttribute("serverZones", goServerZones);
			//model.addAttribute("servers", gs);
		}else{
			List<GoStore> goStores = new ArrayList<GoStore>();
			GoStore goStore = goStoreService.findByStoreId(XYJ);
			if(goStore!=null){
				goStores.add(goStore);
			}
			List<GoServerZone> goServerZones = goServerZoneService.findByStoreId(XYJ);
			model.addAttribute("stores", goStores);
			model.addAttribute("serverZones", goServerZones);
		}
		
		return "/gm/xyj/serverStatus/accountAdd";
	}
	
	/**
	 * 新增
	 */
	@RequestMapping(value = "/accountSave",method=RequestMethod.POST)
	public String accountSave(ServerStatusAccount serverStatusAccount,ServletRequest request,RedirectAttributes redirectAttributes,Model model){
		System.out.println(serverStatusAccount.getGameId() + "  "  + serverStatusAccount.getServerZoneId()+ "  "  + serverStatusAccount.getServerId()+ "  "  +serverStatusAccount.getPlatForm() + "  "  + serverStatusAccount.getAccount() );
		if(serverStatusAccount.getServerId() != null){
			JSONObject res = HttpClientUts.doPost(gm_url+"/xyjserver/server/addGrayAccount" , JSONObject.fromObject(serverStatusAccount));
			redirectAttributes.addFlashAttribute("message", "选择"+res.getString("choose")+"个，成功"+res.getString("success")+"个，失败"+res.getString("fail")+"个，失败的服务器有："+res.getString("objFail"));
			return "redirect:/manage/gm/xyj/serverStatus/accountAdd";
	    }else{
	        redirectAttributes.addFlashAttribute("message", "服务器列表为空,保存失败");
	        return "redirect:/manage/gm/xyj/serverStatus/accountAdd";
	    }
	}
	
	
	/**
	 * 更新
	 */
	@RequestMapping(value = "/accountUpdate",method=RequestMethod.POST)
	public String accountUpdate(ServerStatusAccount serverStatusAccount,ServletRequest request,RedirectAttributes redirectAttributes,Model model){
		String gameId = request.getParameter("search_EQ_storeId");
		String serverZoneId = request.getParameter("search_EQ_serverZoneId");
		serverStatusAccount.setGameId(gameId);
		serverStatusAccount.setServerZoneId(serverZoneId);		
		JSONObject res = HttpClientUts.doPost(gm_url+"/xyjserver/server/updateGrayAccount" , JSONObject.fromObject(serverStatusAccount));
		redirectAttributes.addFlashAttribute("message", "选择"+res.getString("choose")+"个，成功"+res.getString("success")+"个，失败"+res.getString("fail")+"个，失败的服务器有："+res.getString("objFail"));
		return "redirect:/manage/gm/xyj/serverStatus/accountIndex";
	}
	
	/**
	 * 删除操作	 
	 * @param oid 用户id
	 * @throws Exception 
	 */
	@RequestMapping(value = "accountDel", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> del(@RequestParam(value = "id")Long id
			,@RequestParam(value = "gameId")String gameId
			,@RequestParam(value = "serverZoneId")String serverZoneId
			,@RequestParam(value = "serverId")String serverId
			) throws Exception{
		 Map<String,Object> map = new HashMap<String, Object>();
		 String	account = HttpClientUts.doGet(gm_url+"/xyjserver/server/delGrayAccountById?id="+id+"&gameId="+gameId+"&serverZoneId="+serverZoneId+"&serverId="+serverId, "utf-8");
		 JSONObject dataJson=JSONObject.fromObject(account);
		 map.put("message", dataJson.get("message"));
		 return map;
	}
	
	
	@RequestMapping(value="/findServers",method=RequestMethod.GET)	
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<GoAllServer> findServers(@RequestParam(value="serverZoneId") String serverZoneId
			,@RequestParam(value="gameId") String gameId) throws AppBizException{
		List<GoAllServer> servers = goAllServerService.findAllByStoreIdAndServerZoneId(Integer.valueOf(gameId), Integer.valueOf(serverZoneId));
		return servers;
	}
	
	@RequestMapping(value="/findPlatForms",method=RequestMethod.GET)	
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String, String> findPlatForms(@RequestParam(value="serverZoneId") String serverZoneId
			,@RequestParam(value="gameId") String gameId) throws AppBizException{
		List<String> servers = goAllPlatFormService.findPlatFormIds(Integer.valueOf(gameId), Integer.valueOf(serverZoneId));
		Map<String, String> map = new HashMap<String, String>();
		for (String s : servers) {
			PlatForm platForm = platFormService.findByPfId(s);
			if(platForm!=null){
				map.put(s, platForm.getPfName());
			}else{
				map.put(s,s);
			}
		}
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
