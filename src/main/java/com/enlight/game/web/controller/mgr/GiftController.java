package com.enlight.game.web.controller.mgr;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.ServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.enlight.game.base.AppBizException;
import com.enlight.game.entity.Gift;
import com.enlight.game.entity.GiftItem;
import com.enlight.game.entity.GiftProps;
import com.enlight.game.entity.GiftSearch;
import com.enlight.game.entity.Server;
import com.enlight.game.entity.ServerZone;
import com.enlight.game.entity.Stores;
import com.enlight.game.entity.User;
import com.enlight.game.service.account.AccountService;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;
import com.enlight.game.service.giftProps.GiftPropsService;
import com.enlight.game.service.server.ServerService;
import com.enlight.game.service.serverZone.ServerZoneService;
import com.enlight.game.service.store.StoreService;
import com.enlight.game.util.CSVWriter;
import com.enlight.game.util.HttpClientUts;
import com.enlight.game.util.JsonBinder;
import com.enlight.game.util.JsonFlattener;
import com.enlight.game.util.TimeZoneUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springside.modules.web.Servlets;

@Controller("giftController")
@RequestMapping("/manage/gift")
public class GiftController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(GiftController.class);
	
	private static final String PAGE_SIZE = "20";
	
	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();

	static {
		sortTypes.put("auto", "自动");
	}
	
	public static Map<String, String> getSortTypes() {
		return sortTypes;
	}

	public static void setSortTypes(Map<String, String> sortTypes) {
		GiftController.sortTypes = sortTypes;
	}
	
	@Value("#{envProps.list_url}")
	private String list_url;
	
	@Value("#{envProps.save_url}")
	private String save_url;
	
	@Value("#{envProps.del_url}")
	private String del_url;
	
	@Value("#{envProps.export_url}")
	private String export_url;
	
	@Value("#{envProps.review_url}")
	private String review_url;
	
	@Value("#{envProps.search_url}")
	private String search_url;
	
	@Value("#{envProps.searchgift_url}")
	private String searchgift_url;

	
	@Autowired
	private ServerZoneService serverZoneService;
	
	@Autowired
	private ServerService serverService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private GiftPropsService giftPropsService;
	
	
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	
	@RequiresRoles(value = { "admin", "18" }, logical = Logical.OR)
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto")String sortType, Model model,
			ServletRequest request){
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		String store = (String) searchParams.get("LIKE_store");
		
		if (!u.getRoles().equals(User.USER_ROLE_ADMIN)) {
			List<Stores> stores = new ArrayList<Stores>();
			Stores sto=  storeService.findById(Long.valueOf(user.getStoreId()));
			stores.add(sto);
			model.addAttribute("stores", stores);
		}else{
			List<Stores> stores =  storeService.findList();
			model.addAttribute("stores", stores);
		}
		
		if (store!=null&&store!="") {
			try {
				String category = u.getRoles().equals(User.USER_ROLE_ADMIN)?Gift.CATEGORY_ADMIN:Gift.CATEGORY_ORDINART;
				String gs = HttpClientUts.doGet(list_url+"?category="+category+"&gameId="+store+"&userId="+user.id, "utf-8");
				PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		        List<Gift> beanList = binder.getMapper().readValue(gs, new TypeReference<List<Gift>>() {}); 
		        //分页显示部分 pageNumber第几页 pageSize每页条数
		        List<Gift> List = beanList.subList((pageNumber-1)*pageSize, pageNumber*pageSize>beanList.size()? beanList.size():pageNumber*pageSize);
		        PageImpl<Gift> gifts = new PageImpl<Gift>(List, pageRequest, beanList.size());
		        model.addAttribute("gifts", gifts);
				model.addAttribute("sortType", sortType);
				model.addAttribute("sortTypes", sortTypes);
				model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
			} catch (Exception e) {
				e.printStackTrace();
				PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		        PageImpl<Gift> gifts = new PageImpl<Gift>(new ArrayList<Gift>(), pageRequest, 0);
		        model.addAttribute("gifts", gifts);
				model.addAttribute("sortType", sortType);
				model.addAttribute("sortTypes", sortTypes);
				model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
				model.addAttribute("message","发生网络异常!与游戏服务器断开连接");
			}
		}else{
			PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
	        PageImpl<Gift> gifts = new PageImpl<Gift>(new ArrayList<Gift>(), pageRequest, 0);
	        model.addAttribute("gifts", gifts);
			model.addAttribute("sortType", sortType);
			model.addAttribute("sortTypes", sortTypes);
			model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		}
		return "/gift/index";
	}
	
	/**
	 * 查询礼品卡
	 * @param pageNumber
	 * @param pageSize
	 * @param sortType
	 * @param model
	 * @param request
	 * @return
	 */
	@RequiresRoles(value = { "admin", "20" }, logical = Logical.OR)
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto")String sortType, Model model,
			ServletRequest request){
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		String status = (String) searchParams.get("LIKE_status");
		String query = (String) searchParams.get("LIKE_query");
		String store = (String) searchParams.get("LIKE_store");
		if (!u.getRoles().equals(User.USER_ROLE_ADMIN)) {
			List<Stores> stores = new ArrayList<Stores>();
			Stores sto=  storeService.findById(Long.valueOf(user.getStoreId()));
			stores.add(sto);
			model.addAttribute("stores", stores);
		}else{
			List<Stores> stores =  storeService.findList();
			model.addAttribute("stores", stores);
		}
		try {
			if(store!=null&&store!=""
					&&status!=null&&status!=""
					&&query!=null&&query!=""
					&&status.equals("0")){//GUID
				String gs = HttpClientUts.doGet(search_url+"?status="+status+"&query="+query+"&gameId="+store, "utf-8");
				PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
				List<GiftSearch> beanList = binder.getMapper().readValue(gs, new TypeReference<List<GiftSearch>>() {}); 
				List<GiftSearch> List = beanList.subList((pageNumber-1)*pageSize, pageNumber*pageSize>beanList.size()? beanList.size():pageNumber*pageSize);
		        PageImpl<GiftSearch> giftSearchs = new PageImpl<GiftSearch>(List, pageRequest, beanList.size());
		        model.addAttribute("giftSearchs", giftSearchs);
				model.addAttribute("sortType", sortType);
				model.addAttribute("sortTypes", sortTypes);
				model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
			}else if(store!=null&&store!=""
					&&status!=null&&status!=""
					&&query!=null&&query!=""
					&&status.equals("1")){//礼品码
				String gs = HttpClientUts.doGet(search_url+"?status="+status+"&query="+query+"&gameId="+store, "utf-8");
				//String gs="{\"status\":\"1\"}";
				//gs ="[" +gs +"]";
				PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
				List<GiftSearch> beanList = binder.getMapper().readValue(gs, new TypeReference<List<GiftSearch>>() {}); 
				if(beanList.size()!=0){
					for (GiftSearch giftSearch : beanList) {
						if(giftSearch.getStatus().equals(GiftSearch.STATUS_1)){
							model.addAttribute("message", "此礼品码未使用");
					        PageImpl<GiftSearch> giftSearchs = new PageImpl<GiftSearch>(new ArrayList<GiftSearch>(), pageRequest, 0);
					        model.addAttribute("giftSearchs", giftSearchs);
							model.addAttribute("sortType", sortType);
							model.addAttribute("sortTypes", sortTypes);
							model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
						}else if(giftSearch.getStatus().equals(GiftSearch.STATUS_2)){
							model.addAttribute("message", "此礼品码已使用");
							List<GiftSearch> List = beanList.subList((pageNumber-1)*pageSize, pageNumber*pageSize>beanList.size()? beanList.size():pageNumber*pageSize);
					        PageImpl<GiftSearch> giftSearchs = new PageImpl<GiftSearch>(List, pageRequest, beanList.size());
					        model.addAttribute("giftSearchs", giftSearchs);
							model.addAttribute("sortType", sortType);
							model.addAttribute("sortTypes", sortTypes);
							model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
						}
					}
				}else if(beanList.size() == 0){
					model.addAttribute("message", "此礼品码不存在");
					List<GiftSearch> List = beanList.subList((pageNumber-1)*pageSize, pageNumber*pageSize>beanList.size()? beanList.size():pageNumber*pageSize);
			        PageImpl<GiftSearch> giftSearchs = new PageImpl<GiftSearch>(List, pageRequest, beanList.size());
			        model.addAttribute("giftSearchs", giftSearchs);
					model.addAttribute("sortType", sortType);
					model.addAttribute("sortTypes", sortTypes);
					model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
				}

			}else if(store!=null&&store!=""
					&&status!=null&&status!=""
					&&query!=null&&query!=""
					&&status.equals("2")){//礼品卡Id
				String gs = HttpClientUts.doGet(search_url+"?status="+status+"&query="+query+"&gameId="+store, "utf-8");
				JSONObject jsonObject = JSONObject.fromString(gs);
				String num = jsonObject.getString("number");
				
				PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		        PageImpl<GiftSearch> giftSearchs = new PageImpl<GiftSearch>(new ArrayList<GiftSearch>(), pageRequest, 0);
		        model.addAttribute("giftSearchs", giftSearchs);
				model.addAttribute("sortType", sortType);
				model.addAttribute("sortTypes", sortTypes);
				model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
				if(num.equals("-1")){
					model.addAttribute("message", "此礼品卡不存在!");
				}else{
					model.addAttribute("message", "此礼品卡已使用礼品码数量为："+num);
				}

			}else{
				PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		        PageImpl<GiftSearch> giftSearchs = new PageImpl<GiftSearch>(new ArrayList<GiftSearch>(), pageRequest, 0);
		        model.addAttribute("giftSearchs", giftSearchs);
				model.addAttribute("sortType", sortType);
				model.addAttribute("sortTypes", sortTypes);
				model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
	        PageImpl<GiftSearch> giftSearchs = new PageImpl<GiftSearch>(new ArrayList<GiftSearch>(), pageRequest, 0);
	        model.addAttribute("giftSearchs", giftSearchs);
			model.addAttribute("sortType", sortType);
			model.addAttribute("sortTypes", sortTypes);
			model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
			model.addAttribute("message","发生异常!");
		}
		return "/gift/search";
	}
	
	/**
	 * 新增礼品卡
	 * @return
	 */
	@RequiresRoles(value = { "admin", "17" }, logical = Logical.OR)
	@RequestMapping(value = "/add" ,method=RequestMethod.GET)
	public String add(Model model){
		List<ServerZone> serverZones = serverZoneService.findAll();
		logger.debug("新增礼品卡..");
		model.addAttribute("serverZones", serverZones);
		
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		if (!u.getRoles().equals(User.USER_ROLE_ADMIN)) {
			List<Stores> stores = new ArrayList<Stores>();
			Stores sto=  storeService.findById(Long.valueOf(user.getStoreId()));
			stores.add(sto);
			model.addAttribute("stores", stores);
		}else{
			List<Stores> stores =  storeService.findList();
			model.addAttribute("stores", stores);
		}
		
		return "/gift/add";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Gift gift,ServletRequest request,RedirectAttributes redirectAttributes,Model model) throws ParseException{
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		
		String[] servers = request.getParameterValues("server");
		String playerIds = request.getParameter("playerId");
		
		String[] fieldIds = request.getParameterValues("fieldId");
		String[] fieldValues = request.getParameterValues("fieldValue");
		Set<GiftItem> giftItems = new HashSet<GiftItem>();
		if(fieldIds != null && fieldIds.length>0 && fieldValues != null && fieldValues.length>0){
			for (int i = 0; i < fieldIds.length; i++) {
				GiftItem giftItem = new GiftItem();
				giftItem.setId(fieldIds[i].split(":")[0]);
				giftItem.setNumber(fieldValues[i]);
				giftItems.add(giftItem);
			}
		}
		
		gift.setUserId(user.id.toString());
		if (!u.getRoles().equals(User.USER_ROLE_ADMIN)) {
			gift.setGameId(user.getStoreId());
		}
		gift.setServers(servers);
		gift.setPlayerId(ImmutableList.copyOf(StringUtils.split(playerIds, ",")));
		gift.setStatus(Gift.STATUS_CHECKING);
		gift.setGiftItems(giftItems);
		
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date daBegin = f.parse(request.getParameter("beginD"));
		Date daEnd = f.parse(request.getParameter("endD"));
		
		daBegin = TimeZoneUtil.changeTimeZone(daBegin, TimeZone.getDefault(), TimeZone.getTimeZone("UTC"));
		daEnd   = TimeZoneUtil.changeTimeZone(daEnd, TimeZone.getDefault(), TimeZone.getTimeZone("UTC"));
		
		gift.setBeginDate(daBegin.getTime());
		gift.setEndDate(daEnd.getTime());
		
		HttpClientUts.doPost(save_url, JSONObject.fromObject(gift));
		return "redirect:/manage/gift/index?search_LIKE_store="+gift.getGameId();
	}
	
	/**
	 * 删除礼品卡
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresRoles(value = { "admin", "30" }, logical = Logical.OR)
	@RequestMapping(value = "del", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> del(ServletRequest request,RedirectAttributes redirectAttributes){
		 Map<String,Object> map = new HashMap<String, Object>();
	    String[] giftIds = StringUtils.split(request.getParameter("id"), ",");
	    GIds g = new GIds();
		g.setGiftIds(giftIds);
		System.out.println(JSONObject.fromObject(g));
		JSONObject res = HttpClientUts.doPost(del_url, JSONObject.fromObject(g));
		System.out.println(res);
		map.put("status", res.get("status"));
		return map;
	}
	
	/**
	 * 礼品码导出
	 * @param giftId
	 * @param redirectAttributes
	 */
	@RequestMapping(value = "/exportCode", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String,String> exportCode(@RequestParam(value="giftId") String giftId,RedirectAttributes redirectAttributes){
		try {
			String gfcodes = HttpClientUts.doGet(export_url+"?giftId="+giftId, "utf-8");
			String gf = HttpClientUts.doGet(searchgift_url+"?giftId="+giftId, "utf-8");
			System.out.println("3333  " +gf);
			Gift gift = binder.fromJson(gf, Gift.class);
			JsonFlattener parser = new JsonFlattener();
	        CSVWriter writer = new CSVWriter();
	        List<Map<String, String>> flatJson = parser.parseJson(gfcodes);
	        
	        List<Map<String, String>> Json = new ArrayList<Map<String,String>>();
	        Map<String, String> map = new HashMap<String, String>();
	        map.put("礼品卡Id", "礼品卡Id="+gift.getGiftId());
	        map.put("生成礼品卡GM", accountService.getUser(Long.valueOf(gift.getUserId())).getName());
			SimpleDateFormat tt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date begindate =new Date(Long.valueOf(gift.getBeginDate()));
			Date enddate =new Date(Long.valueOf(gift.getEndDate()));
	        map.put("开始日期", tt.format(TimeZoneUtil.changeTimeZone(begindate,TimeZone.getTimeZone("UTC"),TimeZone.getDefault())));
	        map.put("结束日期", tt.format(TimeZoneUtil.changeTimeZone(enddate,TimeZone.getTimeZone("UTC"),TimeZone.getDefault())));
	        map.put("数量", gift.getNumber());
	        if(gift.getStatus().equals(Gift.STATUS_CHECKING)){
	        	map.put("状态","审核中");
	        }else if(gift.getStatus().equals(Gift.STATUS_PASS)){
	        	map.put("状态", "审核通过");
	        }else if(gift.getStatus().equals(Gift.STATUS_REFUSAL)){
	        	map.put("状态", "审核不通过");
	        }
	        Json.add(map);
	        Json.addAll(flatJson);
	        javax.swing.filechooser.FileSystemView fsv = javax.swing.filechooser.FileSystemView.getFileSystemView(); 
	        fsv.getHomeDirectory(); 
	        writer.writeAsCSV(Json, fsv.getHomeDirectory()+"\\"+giftId+".csv");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS_RESULT;
	}
	
	/**
	 * 审核礼品卡
	 * @param giftId
	 * @param status
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresRoles(value = { "admin", "19" }, logical = Logical.OR)
	@RequestMapping(value = "/review", method = RequestMethod.GET , produces = "application/json;charset=UTF-8")
	public String review(@RequestParam(value="giftId") String giftId,
			@RequestParam(value="status") String status,
			@RequestParam(value="stores") String stores,
			RedirectAttributes redirectAttributes){
		if(status=="1"){
			status = Gift.STATUS_PASS;
		}else if(status=="2"){
			status = Gift.STATUS_REFUSAL;
		}else if(status=="0"){
			status = Gift.STATUS_CHECKING;
		}
		try {
			HttpClientUts.doGet(review_url+"?giftId="+giftId+"&status="+status, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/manage/gift/index?search_LIKE_store="+stores;
	}
	
	
	@RequestMapping(value="/findServers",method=RequestMethod.GET)	
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Set<Server> findServers(@RequestParam(value="serverZoneId") String serverZoneId) throws AppBizException{
		Set<Server> servers = serverService.findByServerZoneId(serverZoneId);
		return servers;
	}
	
	@RequestMapping(value="/findGiftProps",method=RequestMethod.GET)	
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<GiftProps> findGiftProps(@RequestParam(value="gameId") String gameId) throws AppBizException{
		return giftPropsService.findByGameId(gameId);
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

	//删除礼品码
	public class GIds {
		
		private String[] giftIds;

		public String[] getGiftIds() {
			return giftIds;
		}

		public void setGiftIds(String[] giftIds) {
			this.giftIds = giftIds;
		}
	}
	
}
