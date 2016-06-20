package com.enlight.game.web.controller.mgr.kds.gm;

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

import com.enlight.game.entity.User;
import com.enlight.game.service.account.AccountService;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;
import com.enlight.game.service.enumCategory.EnumCategoryService;
import com.enlight.game.service.enumFunction.EnumFunctionService;
import com.enlight.game.service.go.GoAllServerService;
import com.enlight.game.service.go.GoServerZoneService;
import com.enlight.game.service.go.GoStoreService;
import com.enlight.game.service.platForm.PlatFormService;
import com.enlight.game.service.server.ServerService;
import com.enlight.game.util.HttpClientUts;
import com.enlight.game.util.JsonBinder;
import com.enlight.game.web.controller.mgr.BaseController;
import com.enlight.game.entity.gm.kds.Category;
import com.enlight.game.entity.gm.kds.Placard;
import com.enlight.game.entity.go.GoAllServer;
import com.enlight.game.entity.go.GoServerZone;
import com.enlight.game.entity.go.GoStore;
import com.google.common.collect.Maps;

@Controller("kdsPlacardController")
@RequestMapping("/manage/gm/kds/placard")
public class KdsPlacardController extends BaseController{

	private static final String PAGE_SIZE = "10";
	
	private static final Integer KDS = 3; //数据库、excel表 ，kds项目storeId为1

	private static final Logger logger = LoggerFactory.getLogger(KdsPlacardController.class);
	
	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	
	static {
		sortTypes.put("auto", "自动");
	}

	public static Map<String, String> getSortTypes() {
		return sortTypes;
	}

	public static void setSortTypes(Map<String, String> sortTypes) {
		KdsPlacardController.sortTypes = sortTypes;
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
				GoServerZone goServerZone = goServerZoneService.findByServerZoneId(Integer.valueOf(str));
				if(goServerZone!=null){
					goServerZones.add(goServerZone);
				}
			}
			model.addAttribute("stores", goStores);
			model.addAttribute("serverZones", goServerZones);
		}else{
			List<GoStore> goStores = new ArrayList<GoStore>();
			GoStore goStore = goStoreService.findByStoreId(KDS);
			if(goStore!=null){
				goStores.add(goStore);
			}
			List<GoServerZone> goServerZones = goServerZoneService.findAll();
			model.addAttribute("stores", goStores);
			model.addAttribute("serverZones", goServerZones);
		}
		
		try {
	        if(!searchParams.isEmpty() && null != request.getParameter("search_EQ_serverId")){
				if(!u.getRoles().equals(User.USER_ROLE_ADMIN)){
					storeId = user.getStoreId();
				}
				
				String gs = HttpClientUts.doGet(gm_url+"/kdsserver/placard/getAllPlacards"+"?serverZoneId="+serverZoneId+"&gameId="+storeId+"&serverId="+URLEncoder.encode(serverId, "utf-8")+"&pageNumber="+pageNumber+"&pageSize="+pageSize, "utf-8");
				String total = HttpClientUts.doGet(gm_url+"/kdsserver/getTotalByServerZoneIdAndGameId"+"?serverZoneId="+serverZoneId+"&gameId="+storeId+"&category="+Category.placard+"&serverId="+URLEncoder.encode(serverId, "utf-8"), "utf-8");
				
				JSONObject dataJson=JSONObject.fromObject(total);
				
				PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		        List<Placard> beanList = binder.getMapper().readValue(gs, new TypeReference<List<Placard>>() {}); 
		        PageImpl<Placard> placard = new PageImpl<Placard>(beanList, pageRequest, Long.valueOf(dataJson.get("num").toString()));
				model.addAttribute("placard", placard);
				
				List<GoAllServer> servers = goAllServerService.findAllByStoreIdAndServerZoneId(Integer.valueOf(request.getParameter("search_EQ_storeId")),Integer.valueOf(serverZoneId));
				model.addAttribute("servers", servers);
	        }else{
	        	List<Placard> beanList = new ArrayList<Placard>();
				PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		        PageImpl<Placard> placard = new PageImpl<Placard>(beanList, pageRequest, 0);
				model.addAttribute("placard", placard);
				
				List<GoAllServer> servers = new ArrayList<GoAllServer>();
				model.addAttribute("servers", servers);
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/gm/kds/placard/index";
	}
	
	/**
	 * 新增页面
	 */
	@RequestMapping(value = "/add" ,method=RequestMethod.GET)
	public String add(Model model){
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		if (!u.getRoles().equals(User.USER_ROLE_ADMIN)) {
			List<GoStore> goStores = new ArrayList<GoStore>();
			GoStore goStore = goStoreService.findByStoreId(Integer.valueOf(user.getStoreId()));
			if(goStore!=null){
				goStores.add(goStore);
			}
			List<GoServerZone> goServerZones = new ArrayList<GoServerZone>();
			List<String> s = u.getServerZoneList();
			for (String str : s) {	
				GoServerZone goServerZone = goServerZoneService.findByServerZoneId(Integer.valueOf(str));
				if(goServerZone!=null){
					goServerZones.add(goServerZone);
				}
			}
			model.addAttribute("stores", goStores);
			model.addAttribute("serverZones", goServerZones);
		}else{
			List<GoStore> goStores = new ArrayList<GoStore>();
			GoStore goStore = goStoreService.findByStoreId(KDS);
			if(goStore!=null){
				goStores.add(goStore);
			}
			List<GoServerZone> goServerZones = goServerZoneService.findAll();
			model.addAttribute("stores", goStores);
			model.addAttribute("serverZones", goServerZones);
		}
		return "/gm/kds/placard/add";
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(value = "/update",method=RequestMethod.POST)
	public String update(Placard placard,ServletRequest request,RedirectAttributes redirectAttributes){
		String gameId = request.getParameter("search_EQ_storeId");
		String serverZoneId = request.getParameter("search_EQ_serverZoneId");

		placard.setGameId(gameId);
		placard.setServerZoneId(serverZoneId);
		System.out.println(placard.getId() +"  " + placard.getGameId() + " "+ placard.getServerZoneId() + "  "  + placard.getServerId() + " " + placard.getVersion() + "  " + placard.getContents());

		JSONObject res = HttpClientUts.doPost(gm_url+"/kdsserver/placard/updatePlacards" , JSONObject.fromObject(placard));
		redirectAttributes.addFlashAttribute("message", "选择"+res.getString("choose")+"个，成功"+res.getString("success")+"个，失败"+res.getString("fail")+"个，失败的服务器有："+res.getString("objFail"));
		return "redirect:/manage/gm/kds/placard/index";
	}

	
	/**
	 * 保存公告
	 * @return
	 */
	@RequestMapping(value="/save" , method=RequestMethod.POST)
	public String save(Placard placard,ServletRequest request,RedirectAttributes redirectAttributes,Model model){
		System.out.println(placard.getGameId() + "  "  + placard.getServerZoneId()+ "  "  + placard.getServerId()+ "  "  +placard.getVersion() + "  "  + placard.getContents() );

        if(placard.getServerId() != null){
    		System.out.println(JSONObject.fromObject(placard));
    		JSONObject res = HttpClientUts.doPost(gm_url+"/kdsserver/placard/addPlacards"  , JSONObject.fromObject(placard));
    		redirectAttributes.addFlashAttribute("message", "选择"+res.getString("choose")+"个，成功"+res.getString("success")+"个，失败"+res.getString("fail")+"个，失败的服务器有："+res.getString("objFail"));
    		return "redirect:/manage/gm/kds/placard/add";
        }else{
        	redirectAttributes.addFlashAttribute("message", "服务器列表为空,保存失败");
        	return "redirect:/manage/gm/kds/placard/add";
        }
	}
	
	/**
	 * 删除操作	 
	 * @param oid 用户id
	 * @throws Exception 
	 */
	@RequestMapping(value = "del", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> del(@RequestParam(value = "id")Long id
			,@RequestParam(value = "gameId")String gameId
			,@RequestParam(value = "serverZoneId")String serverZoneId
			,@RequestParam(value = "serverId")String serverId
			) throws Exception{
		 Map<String,Object> map = new HashMap<String, Object>();
		 String	account = HttpClientUts.doGet(gm_url+"/kdsserver/placard/delPlacardById?id="+id+"&gameId="+gameId+"&serverZoneId="+serverZoneId+"&serverId="+serverId, "utf-8");
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
