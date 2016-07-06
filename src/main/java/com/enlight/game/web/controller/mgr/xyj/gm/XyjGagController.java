package com.enlight.game.web.controller.mgr.xyj.gm;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.enlight.game.entity.Server;
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
import com.enlight.game.entity.gm.xyj.Gag;
import com.enlight.game.entity.go.GoAllServer;
import com.enlight.game.entity.go.GoServerZone;
import com.enlight.game.entity.go.GoStore;
import com.google.common.collect.Maps;

/**
 * 
 * @author apple
 * 禁言
 */
@Controller("xyjGagController")
@RequestMapping("/manage/gm/xyj/gag")
public class XyjGagController extends BaseController{

	private static final String PAGE_SIZE = "10";
	
	private static final Integer XYJ = 4; //数据库、excel表 ，xyj项目storeId为4

	private static final Logger logger = LoggerFactory.getLogger(XyjGagController.class);
	
	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	static {
		sortTypes.put("auto", "自动");
	}

	public static Map<String, String> getSortTypes() {
		return sortTypes;
	}

	public static void setSortTypes(Map<String, String> sortTypes) {
		XyjGagController.sortTypes = sortTypes;
	}
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private EnumFunctionService enumFunctionService;
	
	@Autowired
	private GoStoreService goStoreService;
	
	@Autowired
	private GoServerZoneService  goServerZoneService;
	
	@Autowired
	private GoAllServerService goAllServerService;
	
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
				String gs = HttpClientUts.doGet(gm_url+"/xyjserver/gag/getAllGagAccount"+"?serverZoneId="+serverZoneId+"&gameId="+storeId+"&serverId="+URLEncoder.encode(serverId, "utf-8")+"&pageNumber="+pageNumber+"&pageSize="+pageSize, "utf-8");
				String total = HttpClientUts.doGet(gm_url+"/xyjserver/getTotalByServerZoneIdAndGameId"+"?serverZoneId="+serverZoneId+"&gameId="+storeId+"&category="+Category.gag+"&serverId="+URLEncoder.encode(serverId, "utf-8"), "utf-8");
				JSONObject dataJson=JSONObject.fromObject(total);
				
				PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		        List<Gag> beanList = binder.getMapper().readValue(gs, new TypeReference<List<Gag>>() {}); 
		        PageImpl<Gag> gag = new PageImpl<Gag>(beanList, pageRequest, Long.valueOf(dataJson.get("num").toString()));
				model.addAttribute("gag", gag);
				
				List<GoAllServer> servers = goAllServerService.findAllByStoreIdAndServerZoneId(Integer.valueOf(request.getParameter("search_EQ_storeId")),Integer.valueOf(serverZoneId));
				model.addAttribute("servers", servers);
	        }else{
	        	List<Gag> beanList = new ArrayList<Gag>();
				PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		        PageImpl<Gag> gag = new PageImpl<Gag>(beanList, pageRequest, 0);
				model.addAttribute("gag", gag);
				
				Set<Server> servers = new HashSet<Server>();
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
		return "/gm/xyj/gag/index";
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
		return "/gm/xyj/gag/add";
	}
	
	/**
	 * 修改
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/update",method=RequestMethod.POST)
	public String update(ServletRequest request,RedirectAttributes redirectAttributes) throws UnsupportedEncodingException{
		String id = request.getParameter("id");
		String gameId = request.getParameter("search_EQ_storeId");
		String serverZoneId = request.getParameter("search_EQ_serverZoneId");
		String serverId = request.getParameter("search_EQ_serverId");
		String guid = request.getParameter("guid");
		Gag gag = new Gag();
		gag.setId(Integer.parseInt(id));
		gag.setGameId(gameId);
		gag.setServerZoneId(serverZoneId);
		gag.setServerId(serverId);
		gag.setGuid(guid);
		gag.setName(request.getParameter("name"));
		gag.setPlatForm(request.getParameter("platForm"));
		gag.setAccount(request.getParameter("account"));
		if(null != request.getParameter("gagTime")){
			/**
			 * 
			if(!request.getParameter("gagTime").equals("-1")){
				gag.setGagTime(request.getParameter("gagTime"));
				
				Calendar now = Calendar.getInstance();
				String datestart = sdf.format(now.getTimeInMillis());
				now.add(Calendar.SECOND,Integer.parseInt(request.getParameter("gagTime")));
				String dateend = sdf.format(now.getTimeInMillis());
				
				gag.setGagStart(datestart);
				gag.setGagEnd(dateend);
			}else{
				gag.setGagTime(request.getParameter("gagTime"));
			}
			*/
			gag.setGagTime(request.getParameter("gagTime"));
		}else if(null != request.getParameter("gagStart") && null != request.getParameter("gagEnd")){
			gag.setGagStart(request.getParameter("gagStart"));
			gag.setGagEnd(request.getParameter("gagEnd"));
			/**
			try {
				Date begin = sdf.parse(request.getParameter("gagStart"));
				Date end = sdf.parse(request.getParameter("gagEnd"));   
				long between=(end.getTime()-begin.getTime())/1000;
				gag.setGagTime(String.valueOf(between));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}   
			 */
		}
		
		JSONObject res = HttpClientUts.doPost(gm_url+"/xyjserver/gag/updateGagAccount" , JSONObject.fromObject(gag));
		redirectAttributes.addFlashAttribute("message", "修改禁言Guid:"+guid+":" +res.getString("message"));
		
		return "redirect:/manage/gm/xyj/gag/index?search_EQ_storeId="+gameId+"&search_EQ_serverZoneId="+serverZoneId+"&search_EQ_serverId="+URLEncoder.encode(serverId, "utf-8");
	}
	
	/**
	 * 保存
	 * @return
	 */
	@RequestMapping(value="/save" , method=RequestMethod.POST)
	public String save(Gag gag,ServletRequest request,RedirectAttributes redirectAttributes,Model model){
        if(gag.getServerId() != null){
    		if(null != request.getParameter("gagTime")){
    			/**
    			 * 
    			if(!request.getParameter("gagTime").equals("-1")){
    				gag.setGagTime(request.getParameter("gagTime"));
    				
    				Calendar now = Calendar.getInstance();
    				String datestart = sdf.format(now.getTimeInMillis());
    				now.add(Calendar.SECOND,Integer.parseInt(request.getParameter("gagTime")));
    				String dateend = sdf.format(now.getTimeInMillis());
    				
    				gag.setGagStart(datestart);
    				gag.setGagEnd(dateend);
    			}else{
    				gag.setGagTime(request.getParameter("gagTime"));
    			}
    			*/
    			gag.setGagTime(request.getParameter("gagTime"));
    		}else if(null != request.getParameter("gagStart") && null != request.getParameter("gagEnd")){
    			gag.setGagStart(request.getParameter("gagStart"));
    			gag.setGagEnd(request.getParameter("gagEnd"));
    			/**
    			try {
    				Date begin = sdf.parse(request.getParameter("gagStart"));
    				Date end = sdf.parse(request.getParameter("gagEnd"));   
    				long between=(end.getTime()-begin.getTime())/1000;
    				gag.setGagTime(String.valueOf(between));
    			} catch (ParseException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}   
    			 */
    		}
    		
    		System.out.println("111111 "   +JSONObject.fromObject(gag));
    		JSONObject res = HttpClientUts.doPost(gm_url+"/xyjserver/gag/addGagAccount" , JSONObject.fromObject(gag));
    		redirectAttributes.addFlashAttribute("message", "新增禁言 Guid："+gag.getGuid()+" "+res.getString("message"));
    		return "redirect:/manage/gm/xyj/gag/add";
        }else{
        	redirectAttributes.addFlashAttribute("message", "服务器列表为空,保存失败");
        	return "redirect:/manage/gm/xyj/gag/add";
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
	public Map<String,Object> del(@RequestParam(value = "guid")Long guid
			,@RequestParam(value = "gameId")String gameId
			,@RequestParam(value = "serverZoneId")String serverZoneId
			,@RequestParam(value = "serverId")String serverId
			) throws Exception{
		 Map<String,Object> map = new HashMap<String, Object>();
		 String	account = HttpClientUts.doGet(gm_url+"/xyjserver/gag/delGagAccountById?guid="+guid+"&gameId="+gameId+"&serverZoneId="+serverZoneId+"&serverId="+serverId, "utf-8");
		 JSONObject dataJson=JSONObject.fromObject(account);
		 map.put("message", dataJson.get("message"));
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
