package com.enlight.game.web.controller.mgr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

@Controller("giftController")
@RequestMapping("/manage/gift")
public class GiftController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(GiftController.class);
	
	private static final String PAGE_SIZE = "2";
	
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
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto")String sortType, Model model,
			ServletRequest request){
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		if (!u.getRoles().equals(User.USER_ROLE_ADMIN)) {
			String category = Gift.CATEGORY_ADMIN;
			String gameId = null;
			try {
				//String gs = HttpClientUts.doGet(list_url+"?category="+category+"&gameId="+gameId+"&userId="+user.id, "utf-8");
				String gs = 
				
				"[\n" +
                "    {\n" +
                "        \"giftId\": \"1\",\n" +
                "        \"userId\": \"2\",\n" +
                "        \"gameId\": \"3\",\n" +
                "        \"number\": \"4\",\n" +
                "        \"coin\": \"5\",\n" +
                "        \"diamond\": \"6\",\n" +
                "        \"arenacoin\": \"7\",\n" +
                "        \"expeditioncoin\": \"8\",\n" +
                "        \"tradecoin\": \"9\",\n" +
                "        \"begindate\": \"2015-01-28 17:38:58\",\n" +
                "        \"enddate\": \"2015-01-28 17:38:59\",\n" +
                "        \"status\": \"0\",\n" +
                "        \"giftItems\": [\n" +
                "            {\n" +
                "                \"id\": \"11\",\n" +
                "                \"number\": \"1111\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": \"12\",\n" +
                "                \"number\": \"1212\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "]";

				
				PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		        List<Gift> beanList = binder.getMapper().readValue(gs, new TypeReference<List<Gift>>() {});  
		        PageImpl<Gift> gifts = new PageImpl<Gift>(beanList, pageRequest, beanList.size());
				
		        model.addAttribute("gifts", gifts);
				model.addAttribute("sortType", sortType);
				model.addAttribute("sortTypes", sortTypes);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			String category = Gift.CATEGORY_ORDINART;
			String gameId = user.getStoreId();
			try {
				String gs = HttpClientUts.doGet(list_url+"?category="+category+"&gameId=3"+"&userId="+user.id, "utf-8");
/*				String gs = 				"[\n" +
		                "    {\n" +
		                "        \"giftId\": \"1\",\n" +
		                "        \"userId\": \"1\",\n" +
		                "        \"gameId\": \"3\",\n" +
		                "        \"number\": \"4\",\n" +
		                "        \"beginDate\": \"1422837771000\",\n" +
		                "        \"endDate\": \"1422837771000\",\n" +
		                "        \"status\": \"0\",\n" +
		                "        \"giftItems\": [\n" +
		                "            {\n" +
		                "                \"id\": \"1\",\n" +
		                "                \"number\": \"1111\"\n" +
		                "            },\n" +
		                "            {\n" +
		                "                \"id\": \"2\",\n" +
		                "                \"number\": \"1212\"\n" +
		                "            },\n" +
		                "            {\n" +
		                "                \"id\": \"3\",\n" +
		                "                \"number\": \"1212\"\n" +
		                "            },\n" +
		                "            {\n" +
		                "                \"id\": \"4\",\n" +
		                "                \"number\": \"1212\"\n" +
		                "            },\n" +
		                "            {\n" +
		                "                \"id\": \"5\",\n" +
		                "                \"number\": \"1212\"\n" +
		                "            }\n" +
		                "        ]\n" +
		                "    },\n" +
		                "    {\n" +
		                "        \"giftId\": \"1\",\n" +
		                "        \"userId\": \"1\",\n" +
		                "        \"gameId\": \"2\",\n" +
		                "        \"number\": \"4\",\n" +
		                "        \"beginDate\": \"1422837771000\",\n" +
		                "        \"endDate\": \"1422837771000\",\n" +
		                "        \"status\": \"0\",\n" +
		                "        \"giftItems\": [\n" +
		                "            {\n" +
		                "                \"id\": \"3\",\n" +
		                "                \"number\": \"1212\"\n" +
		                "            },\n" +
		                "            {\n" +
		                "                \"id\": \"4\",\n" +
		                "                \"number\": \"1212\"\n" +
		                "            },\n" +
		                "            {\n" +
		                "                \"id\": \"5\",\n" +
		                "                \"number\": \"1212\"\n" +
		                "            }\n" +
		                "        ]\n" +
		                "    },\n" +
		                "    {\n" +
		                "        \"giftId\": \"1\",\n" +
		                "        \"userId\": \"1\",\n" +
		                "        \"gameId\": \"1\",\n" +
		                "        \"number\": \"4\",\n" +
		                "        \"beginDate\": \"1422837771000\",\n" +
		                "        \"endDate\": \"1422837771000\",\n" +
		                "        \"status\": \"0\",\n" +
		                "        \"giftItems\": [\n" +
		                "            {\n" +
		                "                \"id\": \"1\",\n" +
		                "                \"number\": \"1111\"\n" +
		                "            },\n" +
		                "            {\n" +
		                "                \"id\": \"2\",\n" +
		                "                \"number\": \"1212\"\n" +
		                "            },\n" +
		                "            {\n" +
		                "                \"id\": \"5\",\n" +
		                "                \"number\": \"1212\"\n" +
		                "            }\n" +
		                "        ]\n" +
		                "    }\n" +
		                "]";*/

				PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		        List<Gift> beanList = binder.getMapper().readValue(gs, new TypeReference<List<Gift>>() {}); 
		        //分页显示部分 pageNumber第几页 pageSize每页条数
		        List<Gift> List = beanList.subList((pageNumber-1)*pageSize, pageNumber*pageSize>beanList.size()? beanList.size():pageNumber*pageSize);
		        PageImpl<Gift> gifts = new PageImpl<Gift>(List, pageRequest, beanList.size());
		        
		        model.addAttribute("gifts", gifts);
				model.addAttribute("sortType", sortType);
				model.addAttribute("sortTypes", sortTypes);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return "/gift/index";
	}
	
	/**
	 * 新增礼品卡
	 * @return
	 */
	@RequestMapping(value = "/add" ,method=RequestMethod.GET)
	public String add(Model model){
		List<ServerZone> serverZones = serverZoneService.findAll();
		List<Stores> stores =  storeService.findList();
		logger.debug("新增礼品卡..");
		model.addAttribute("serverZones", serverZones);
		model.addAttribute("stores", stores);
		return "/gift/add";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST , produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String,Object> save(Gift gift,ServletRequest request,RedirectAttributes redirectAttributes,Model model) throws ParseException{
		//platFormService.save(platForm);
		//redirectAttributes.addFlashAttribute("message", "新增成功");
		//return "redirect:/manage/platForm/index";
		Map<String,Object> map = new HashMap<String, Object>();
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
		
		System.out.println("1111111  "  + JSONObject.fromObject(gift));
		HttpClientUts.doPost("http://10.0.10.105:40000/api/gameserver/v1/gift/add", JSONObject.fromObject(gift));
		
        map.put("gift", gift);
		return map;
	}
	
	/**
	 * 删除礼品卡
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/del", method = RequestMethod.GET , produces = "application/json;charset=UTF-8")
	public String del(ServletRequest request,RedirectAttributes redirectAttributes){
		String[] giftIds = request.getParameterValues("id");
		JSONObject params = new JSONObject();
		params.put("giftIds", giftIds);
		HttpClientUts.doPost(del_url, params);
/*		if(){
			redirectAttributes.addFlashAttribute("message", "新增权限成功");
		}else{
			redirectAttributes.addFlashAttribute("message", "删除礼品卡失败");
		}*/
		return "redirect:/manage/gift/index";
	}
	
	/**
	 * 礼品码导出
	 * @param giftId
	 * @param redirectAttributes
	 */
	@RequestMapping(value = "/export", method = RequestMethod.GET , produces = "application/json;charset=UTF-8")
	public void export(@RequestParam(value="giftId") String giftId,RedirectAttributes redirectAttributes){
		try {
			String gfcodes = HttpClientUts.doGet(export_url+"?giftId="+giftId, "utf-8");
			JsonFlattener parser = new JsonFlattener();
	        CSVWriter writer = new CSVWriter();
	        List<Map<String, String>> flatJson = parser.parseJson(gfcodes);
	        writer.writeAsCSV(flatJson, "D://sample.csv");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 审核礼品卡
	 * @param giftId
	 * @param status
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/review", method = RequestMethod.GET , produces = "application/json;charset=UTF-8")
	public String review(@RequestParam(value="giftId") String giftId,@RequestParam(value="status") String status,RedirectAttributes redirectAttributes){
		if(status=="1"){
			status = Gift.STATUS_PASS;
		}else if(status=="2"){
			status = Gift.STATUS_REFUSAL;
		}
		try {
			HttpClientUts.doGet(review_url+"?giftId="+giftId+"&status="+status, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*if(){
			redirectAttributes.addFlashAttribute("message", "服务器响应成功);
		}else{
			redirectAttributes.addFlashAttribute("message", "服务器响应失败");
		}*/
		return "redirect:/manage/gift/index";
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

	
}
