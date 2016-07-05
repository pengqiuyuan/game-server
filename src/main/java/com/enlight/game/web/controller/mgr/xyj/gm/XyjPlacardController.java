package com.enlight.game.web.controller.mgr.xyj.gm;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.enlight.game.entity.gm.xyj.Category;
import com.enlight.game.entity.gm.xyj.Email;
import com.enlight.game.entity.gm.xyj.Placard;
import com.enlight.game.entity.gm.xyj.PlacardList;
import com.enlight.game.entity.go.GoAllServer;
import com.enlight.game.entity.go.GoServerZone;
import com.enlight.game.entity.go.GoStore;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.zxing.FormatException;

@Controller("xyjPlacardController")
@RequestMapping("/manage/gm/xyj/placard")
public class XyjPlacardController extends BaseController{

	private static final String PAGE_SIZE = "10";
	
	private static final Integer XYJ = 4; //数据库、excel表 ，xyj项目storeId为4

	private static final Logger logger = LoggerFactory.getLogger(XyjPlacardController.class);
	
	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	
	static {
		sortTypes.put("auto", "自动");
	}

	public static Map<String, String> getSortTypes() {
		return sortTypes;
	}

	public static void setSortTypes(Map<String, String> sortTypes) {
		XyjPlacardController.sortTypes = sortTypes;
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
				
				String gs = HttpClientUts.doGet(gm_url+"/xyjserver/placard/getAllPlacards"+"?serverZoneId="+serverZoneId+"&gameId="+storeId+"&serverId="+URLEncoder.encode(serverId, "utf-8")+"&pageNumber="+pageNumber+"&pageSize="+pageSize, "utf-8");
				String total = HttpClientUts.doGet(gm_url+"/xyjserver/getTotalByServerZoneIdAndGameId"+"?serverZoneId="+serverZoneId+"&gameId="+storeId+"&category="+Category.placard+"&serverId="+URLEncoder.encode(serverId, "utf-8"), "utf-8");
				
				JSONObject dataJson=JSONObject.fromObject(total);
				
				PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		        List<Placard> beanList = binder.getMapper().readValue(binder.toJson(binder.fromJson(gs, PlacardList.class).getPlacardList()), new TypeReference<List<Placard>>() {}); 
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
		return "/gm/xyj/placard/index";
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
		return "/gm/xyj/placard/add";
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

		//placard.setContents(placard.getContents().replaceAll("(\r\n|\r|\n|\n\r)", ""));
        if(placard.getServerId() != null){
        	System.out.println("111111 "   +JSONObject.fromObject(placard));
        	int choose = 0,success = 0,fail = 0;
            List<String> objFail = new ArrayList<String>();
        	List<String> serverId = ImmutableList.copyOf(StringUtils.split(placard.getServerId(), ","));
        	for (String sId : serverId) {
        		placard.setServerId(sId);
				JSONObject res = HttpClientUts.doPost(gm_url+"/xyjserver/placard/updatePlacards" , JSONObject.fromObject(placard));
				System.out.println("多个 xyj placard 保存返回值" + res);
				choose += Integer.valueOf(res.getString("choose"));
				success += Integer.valueOf(res.getString("success"));
				fail += Integer.valueOf(res.getString("fail"));
				objFail.add(res.getString("objFail"));
			}
        	redirectAttributes.addFlashAttribute("message", "选择修改公告的服务器 "+choose+" 个，成功 "+ success+" 个，失败 "+fail+" 个，修改失败的服务器有："+StringUtils.join(objFail.toArray(), " "));
    		return "redirect:/manage/gm/xyj/placard/index";
        }else{
        	redirectAttributes.addFlashAttribute("message", "服务器列表为空,保存失败");
        	return "redirect:/manage/gm/xyj/placard/index";
        }
	}

	
	/**
	 * 保存公告
	 * @return
	 */
	@RequestMapping(value="/save" , method=RequestMethod.POST)
	public String save(Placard placard,ServletRequest request,RedirectAttributes redirectAttributes,Model model){
		System.out.println(placard.getGameId() + "  "  + placard.getServerZoneId()+ "  "  + placard.getServerId()+ "  "  +placard.getVersion() + "  "  + placard.getContents() );
		//placard.setContents(placard.getContents().replaceAll("(\r\n|\r|\n|\n\r)", ""));
        if(placard.getServerId() != null){
        	System.out.println("111111 "   +JSONObject.fromObject(placard));
        	int choose = 0,success = 0,fail = 0;
            List<String> objFail = new ArrayList<String>();
        	List<String> serverId = ImmutableList.copyOf(StringUtils.split(placard.getServerId(), ","));
        	for (String sId : serverId) {
        		placard.setServerId(sId);
				JSONObject res = HttpClientUts.doPost(gm_url+"/xyjserver/placard/addPlacards" , JSONObject.fromObject(placard));
				System.out.println("多个 xyj placard 保存返回值" + res);
				choose += Integer.valueOf(res.getString("choose"));
				success += Integer.valueOf(res.getString("success"));
				fail += Integer.valueOf(res.getString("fail"));
				objFail.add(res.getString("objFail"));
			}
        	redirectAttributes.addFlashAttribute("message", "选择新增公告的服务器 "+choose+" 个，成功 "+ success+" 个，失败 "+fail+" 个，新增失败的服务器有："+StringUtils.join(objFail.toArray(), " "));
    		return "redirect:/manage/gm/xyj/placard/index";
        }else{
        	redirectAttributes.addFlashAttribute("message", "服务器列表为空,保存失败");
        	return "redirect:/manage/gm/xyj/placard/index";
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
		 String	account = HttpClientUts.doGet(gm_url+"/xyjserver/placard/delPlacardById?id="+id+"&gameId="+gameId+"&serverZoneId="+serverZoneId+"&serverId="+serverId, "utf-8");
		 JSONObject dataJson=JSONObject.fromObject(account);
		 map.put("message", dataJson.get("message"));
		 return map;
	}

	/**
	 * 获取操作	 
	 * @param oid 用户id
	 * @throws Exception 
	 */
	@RequestMapping(value = "findByPlacardId", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Placard findByPlacardId(@RequestParam(value = "id")Long id
			,@RequestParam(value = "gameId")String gameId
			,@RequestParam(value = "serverZoneId")String serverZoneId
			,@RequestParam(value = "serverId")String serverId
			) throws Exception{
		 String	account = HttpClientUts.doGet(gm_url+"/xyjserver/placard/getPlacardById?id="+id+"&gameId="+gameId+"&serverZoneId="+serverZoneId+"&serverId="+serverId, "utf-8");
		 System.out.println("111111   "  + account);
		 Placard placard = binder.fromJson(account, Placard.class);
		 return placard;
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
