package com.enlight.game.web.controller.mgr.xyj.gm;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import net.sf.json.JSONObject;

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

import com.enlight.game.entity.Server;
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
import com.enlight.game.entity.gm.xyj.Annex;
import com.enlight.game.entity.gm.xyj.Category;
import com.enlight.game.entity.gm.xyj.Email;
import com.enlight.game.entity.go.GoAllServer;
import com.enlight.game.entity.go.GoServerZone;
import com.enlight.game.entity.go.GoStore;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

@Controller("XyjEmailController")
@RequestMapping("/manage/gm/xyj/email")
public class XyjEmailController extends BaseController{

	private static final String PAGE_SIZE = "10";
	
	private static final String CATEGORY = "email";
	
	private static final Integer XYJ = 4; //数据库、excel表 ，xyj项目storeId为4

	private static final Logger logger = LoggerFactory.getLogger(XyjEmailController.class);
	
	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	
	static {
		sortTypes.put("auto", "自动");
	}

	public static Map<String, String> getSortTypes() {
		return sortTypes;
	}

	public static void setSortTypes(Map<String, String> sortTypes) {
		XyjEmailController.sortTypes = sortTypes;
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
				String gs = HttpClientUts.doGet(gm_url+"/xyjserver/email/getAllEmails"+"?serverZoneId="+serverZoneId+"&gameId="+storeId+"&serverId="+URLEncoder.encode(serverId, "utf-8")+"&pageNumber="+pageNumber+"&pageSize="+pageSize, "utf-8");
				String total = HttpClientUts.doGet(gm_url+"/xyjserver/getTotalByServerZoneIdAndGameId"+"?serverZoneId="+serverZoneId+"&gameId="+storeId+"&category="+Category.email+"&serverId="+URLEncoder.encode(serverId, "utf-8"), "utf-8");
				JSONObject dataJson=JSONObject.fromObject(total);
				PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		        List<Email> beanList = binder.getMapper().readValue(gs, new TypeReference<List<Email>>() {}); 
		        PageImpl<Email> email = new PageImpl<Email>(beanList, pageRequest, Long.valueOf(dataJson.get("num").toString()));
				model.addAttribute("email", email);
				List<GoAllServer> servers = goAllServerService.findAllByStoreIdAndServerZoneId(Integer.valueOf(request.getParameter("search_EQ_storeId")),Integer.valueOf(serverZoneId));
				model.addAttribute("servers", servers);
	        }else{
	        	List<Email> beanList = new ArrayList<Email>();
				PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		        PageImpl<Email> email = new PageImpl<Email>(beanList, pageRequest, 0);
				model.addAttribute("email", email);
				
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
		return "/gm/xyj/email/index";
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
		
		return "/gm/xyj/email/add";
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(value = "/update",method=RequestMethod.POST)
	public String update(Email email,ServletRequest request,RedirectAttributes redirectAttributes){
		String gameId = request.getParameter("search_EQ_storeId");
		String serverZoneId = request.getParameter("search_EQ_serverZoneId");

		String[] itemId = request.getParameterValues("itemId");
		String[] itemNum = request.getParameterValues("itemNum");
		List<Annex> annexs  = new ArrayList<Annex>();
		for (int i = 0; i < itemId.length; i++) {
			Annex annex = new Annex();
			annex.setItemId(itemId[i]);
			annex.setItemNum(itemNum[i]);
			annexs.add(annex);
		}
		email.setServerZoneId(serverZoneId);
		email.setGameId(gameId);
		email.setAnnex(annexs);
		
        if(email.getServerId() != null){
        	System.out.println("111111 "   +JSONObject.fromObject(email));
        	int choose = 0,success = 0,fail = 0;
            List<String> objFail = new ArrayList<String>();
        	List<String> serverId = ImmutableList.copyOf(StringUtils.split(email.getServerId(), ","));
        	for (String sId : serverId) {
        		email.setServerId(sId);
        		JSONObject res = HttpClientUts.doPost(gm_url+"/xyjserver/email/updateEmail" , JSONObject.fromObject(email));
				System.out.println("多个 xyj email 保存返回值" + res);
				choose += Integer.valueOf(res.getString("choose"));
				success += Integer.valueOf(res.getString("success"));
				fail += Integer.valueOf(res.getString("fail"));
				objFail.add(res.getString("objFail"));
			}
        	redirectAttributes.addFlashAttribute("message", "选择修改邮件的服务器："+choose+" 个，成功："+success+" 个，失败："+fail+" 个，失败的服务器有："+StringUtils.join(objFail.toArray(), " "));
    		return "redirect:/manage/gm/xyj/email/index";
        }else{
        	redirectAttributes.addFlashAttribute("message", "服务器列表为空,保存失败");
        	return "redirect:/manage/gm/xyj/email/index";
        }
		
	}

	
	/**
	 * 保存
	 * @return
	 */
	@RequestMapping(value="/save" , method=RequestMethod.POST)
	public String save(Email email,ServletRequest request,RedirectAttributes redirectAttributes,Model model){
		
		String[] itemId = request.getParameterValues("itemId");
		String[] itemNum = request.getParameterValues("itemNum");
		String rev = request.getParameter("receiver");
		if(itemId!=null && itemNum!=null){
			List<Annex> annexs  = new ArrayList<Annex>();
			for (int i = 0; i < itemId.length; i++) {
				Annex annex = new Annex();
				annex.setItemId(itemId[i]);
				annex.setItemNum(itemNum[i]);
				annexs.add(annex);
			}
			email.setAnnex(annexs);
		}else{
			List<Annex> annexs  = new ArrayList<Annex>();
			email.setAnnex(annexs);
		}
		List<String> receiver =  ImmutableList.copyOf(StringUtils.split(rev, ","));
		email.setReceiver(receiver);
		//按渠道发送，最终也要找出渠道对应服务器
		if(email.getPlatForm()!=null){
			Set<String>  set=new HashSet<String>();
			List<String> pfIds =  ImmutableList.copyOf(StringUtils.split(email.getPlatForm(), ","));
			for (String platFormId : pfIds) {
				List<String> serverIds =  goAllPlatFormService.findAllByPlatFormIdAndStoreIdAndServerZoneId(Integer.valueOf(email.getGameId()), Integer.valueOf(email.getServerZoneId()), platFormId);
				set.addAll(serverIds);
			}
			String serverId = "";
			serverId = StringUtils.join(set.toArray(), ","); // 根据不同切割符返回字符串
			email.setServerId(serverId);
		}
        if(email.getServerId() != null){
        	System.out.println("111111 "   +JSONObject.fromObject(email));
        	int choose = 0,success = 0,fail = 0;
            List<String> objFail = new ArrayList<String>();
        	List<String> serverId = ImmutableList.copyOf(StringUtils.split(email.getServerId(), ","));
        	for (String sId : serverId) {
				email.setServerId(sId);
				JSONObject res = HttpClientUts.doPost(gm_url+"/xyjserver/email/addEmail" , JSONObject.fromObject(email));
				System.out.println("多个 xyj email 保存返回值" + res);
				choose += Integer.valueOf(res.getString("choose"));
				success += Integer.valueOf(res.getString("success"));
				fail += Integer.valueOf(res.getString("fail"));
				objFail.add(res.getString("objFail"));
			}
        	if(email.getPlatForm()!=null){
        		redirectAttributes.addFlashAttribute("message", "选择新增邮件的渠道："+ email.getPlatForm() +"，选择新增邮件的服务器："+choose+" 个，成功："+ success+" 个，失败："+fail+" 个，失败的服务器有："+StringUtils.join(objFail.toArray(), " "));
        	}else{
        		redirectAttributes.addFlashAttribute("message", "选择新增邮件的服务器："+choose+" 个，成功："+ success+" 个，失败："+fail+" 个，新增失败的服务器有："+StringUtils.join(objFail.toArray(), " "));
        	}
    		return "redirect:/manage/gm/xyj/email/add";
        }else{
        	redirectAttributes.addFlashAttribute("message", "服务器列表为空,保存失败");
    		return "redirect:/manage/gm/xyj/email/add";
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
		 String	account = HttpClientUts.doGet(gm_url+"/xyjserver/email/delEmailById?id="+id+"&gameId="+gameId+"&serverZoneId="+serverZoneId+"&serverId="+serverId, "utf-8");
		 JSONObject dataJson=JSONObject.fromObject(account);
		 map.put("message", dataJson.get("message"));
		 return map;
	}

	/**
	 * 获取操作	 
	 * @param oid 用户id
	 * @throws Exception 
	 */
	@RequestMapping(value = "findByEmailId", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Email findByEmailId(@RequestParam(value = "id")Long id
			,@RequestParam(value = "gameId")String gameId
			,@RequestParam(value = "serverZoneId")String serverZoneId
			,@RequestParam(value = "serverId")String serverId
			) throws Exception{
		 String	account = HttpClientUts.doGet(gm_url+"/xyjserver/email/getEmailById?id="+id+"&gameId="+gameId+"&serverZoneId="+serverZoneId+"&serverId="+serverId, "utf-8");
		 Email email = binder.fromJson(account, Email.class);
		 return email;
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
