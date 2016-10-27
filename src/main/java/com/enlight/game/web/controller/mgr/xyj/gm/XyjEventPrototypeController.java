package com.enlight.game.web.controller.mgr.xyj.gm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.enlight.game.entity.Log;
import com.enlight.game.entity.ServerZone;
import com.enlight.game.entity.Stores;
import com.enlight.game.entity.User;
import com.enlight.game.entity.gm.xyj.EventDataPrototype;
import com.enlight.game.entity.gm.xyj.EventPrototype;
import com.enlight.game.entity.go.GoAllServer;
import com.enlight.game.service.account.AccountService;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;
import com.enlight.game.service.gm.xyj.XyjEventDataPrototypeInstructionService;
import com.enlight.game.service.gm.xyj.XyjEventDataPrototypeService;
import com.enlight.game.service.gm.xyj.XyjEventPrototypeService;
import com.enlight.game.service.go.GoAllServerService;
import com.enlight.game.service.log.LogService;
import com.enlight.game.service.serverZone.ServerZoneService;
import com.enlight.game.service.store.StoreService;
import com.enlight.game.service.user.UserService;
import com.enlight.game.util.HttpClientUts;
import com.enlight.game.web.controller.mgr.BaseController;
import com.google.common.collect.Maps;

@Controller("xyjEventPrototypeController")
@RequestMapping(value = "/manage/gm/xyj/eventPrototype")
public class XyjEventPrototypeController extends BaseController{

	private static final String PAGE_SIZE = "15";
	
	private static final Integer XYJ = 4; //数据库、excel表 ，xyj项目storeId为4

	private static final Logger logger = LoggerFactory.getLogger(XyjEventPrototypeController.class);
	
	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("registerDate", "时间");
	}

	public static Map<String, String> getSortTypes() {
		return sortTypes;
	}

	public static void setSortTypes(Map<String, String> sortTypes) {
		XyjEventPrototypeController.sortTypes = sortTypes;
	}
	
	@Override
	@InitBinder
	protected void initBinder(ServletRequestDataBinder binder){
		binder.registerCustomEditor(Date.class,"registerDate",new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
	}
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private ServerZoneService serverZoneService;
	
	@Autowired
	private XyjEventPrototypeService xyjEventPrototypeService;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private XyjEventDataPrototypeInstructionService xyjEventDataPrototypeInstructionService;
	
	@Autowired
	private XyjEventDataPrototypeService xyjEventDataPrototypeService;
	
	@Autowired
	private GoAllServerService goAllServerService;
	
	@Value("#{envProps.gm_url}")
	private String gm_url;
	
	/**
	 *  活动管理首页
	 * @param pageNumber 当前	 
	 * @param pageSize   显示条数
	 * @param sortType  排序
	 * @param model   返回对象
	 * @param request  封装的请	
	 * @throws Exception 
	 */
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto")String sortType, Model model,
			ServletRequest request) throws Exception{
		Long userId = getCurrentUserId();
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		logger.info("userId"+userId+"活动管理首页");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<EventPrototype> eventPrototypes = xyjEventPrototypeService.findEventPrototypesByCondition(userId, searchParams, pageNumber, pageSize, sortType);
		
		if (!u.getRoles().equals(User.USER_ROLE_ADMIN)) {	
			List<Stores> stores = new ArrayList<Stores>();
			Stores st = storeService.findById(Integer.valueOf(user.getStoreId()));
			if(st != null){
				stores.add(st);
			}
			List<ServerZone> serverZones = new ArrayList<ServerZone>();
			List<String> s = u.getServerZoneList();
			for (String str : s) {
				ServerZone server = serverZoneService.findById(Long.valueOf(str));
				serverZones.add(server);
			}
			model.addAttribute("stores", stores);
			model.addAttribute("serverZones", serverZones);
		}else{
			List<Stores> stores = new ArrayList<Stores>();
			Stores st = storeService.findById(XYJ);
			if(st != null){
				stores.add(st);
			}
			List<ServerZone> serverZones = serverZoneService.findAll();
			model.addAttribute("stores", stores);
			model.addAttribute("serverZones", serverZones);
		}
		
		/* times 持续时长，活动设置时间到期，自动关闭活动。*/
		List<EventPrototype> e2 = xyjEventPrototypeService.findByTimes();
		String nowDate = xyjEventPrototypeService.nowDate();
		for (EventPrototype eventPrototype : e2) {
			if(eventPrototype.getTimes().equals("-1")){
				logger.debug(eventPrototype.getId()+" |"+"0、活动为无限激活状态");
			}else{
				if(eventPrototype.getActiveType().equals("0")){/*active_type==0 激活时间基点，需要填写激活时间*/
					String time3 = "";
					if(eventPrototype.getActiveDay().equals("0")){/*0无时间点要求*/
						String time1 = xyjEventPrototypeService.getByHour(eventPrototype.getActiveData(), eventPrototype.getActiveDelay()); // （加上延时激活时间）活动激活时间起点
						String time2 = eventPrototype.getTimes(); //活动持续时间（单位小时）
						time3 = xyjEventPrototypeService.getByHour(time1, time2); //活动结束时间
						logger.debug(eventPrototype.getId()+" |"+"1：星期几激活选择为 0，通过激活时间基点："+eventPrototype.getActiveData()+"、延时时间（小时）："+eventPrototype.getActiveDelay()+"、延时后激活时间基点："+time1+"、持续时间（小时）："+eventPrototype.getTimes()+"、获取活动结束时间："+time3);
					}else { /*指定星期N激活 ，需要与活动激活时间起点 做比较*/
						String time1 = xyjEventPrototypeService.getByHour(eventPrototype.getActiveData(), eventPrototype.getActiveDelay()); // （加上延时激活时间）活动激活时间起点
						String timeWeek = xyjEventPrototypeService.getDateByWeek(eventPrototype.getActiveDay(), nowDate).compareTo(nowDate) <= 0?
								xyjEventPrototypeService.getDateByWeek(eventPrototype.getActiveDay(), xyjEventPrototypeService.getDateByWeek("7",xyjEventPrototypeService.getByDay(nowDate, "7")))
								:xyjEventPrototypeService.getDateByWeek(eventPrototype.getActiveDay(), nowDate); /*nowDate.compareTo() >= 0 代表nowDate 晚于 活动时间，活动时间已过，选下个星期 （活动激活时间起点 按星期几激活活动算）*/
						String timeStart = time1.compareTo(timeWeek) <= 0? time1 : timeWeek; /*time1 比 timeWeek 早，使用time1*/
						String time2 = eventPrototype.getTimes(); //活动持续时间（单位小时）
						time3 = xyjEventPrototypeService.getByHour(timeStart, time2); //活动结束时间 按活动时间基点计算
						logger.debug(eventPrototype.getId()+" |"+"1：星期几激活选择为 "+timeWeek+"，（按活动激活时间几点+延时激活时间）"+time1+" 与 （按按星期几激活活动）"+timeWeek+" 做比较，得到较近的激活时间："+timeStart);
					}
					if(time3.compareTo(nowDate)<=0){
						if(eventPrototype.getEventRepeatInterval().equals("0")){ /*活动不重复激活 单位天*/
							/*time3 早于 nowDate ，活动已结束*/
							logger.debug(eventPrototype.getId()+" |"+"2：活动重复激活："+eventPrototype.getEventRepeatInterval()+"天");
							logger.debug(eventPrototype.getId()+" |"+"3：活动自动关闭，活动结束时间："+time3+" 早于现在时间："+nowDate);
							closeEventPrototype(eventPrototype.getId());/*关闭活动及活动条目*/
						}else { /*活动重复激活 */
							String time4 = xyjEventPrototypeService.getByDay(time3, eventPrototype.getEventRepeatInterval());
							if(time4.compareTo(nowDate)<=0){
								/*time4（重复N天后） 早于 nowDate ，活动已结束*/
								logger.debug(eventPrototype.getId()+" |"+"2：活动重复激活："+eventPrototype.getEventRepeatInterval()+"天");
								logger.debug(eventPrototype.getId()+" |"+"3：活动自动关闭，活动结束时间："+time4+" 早于现在时间："+nowDate);
								closeEventPrototype(eventPrototype.getId());/*关闭活动及活动条目*/
							}else {
								logger.debug(eventPrototype.getId()+" |"+"2：活动重复激活："+eventPrototype.getEventRepeatInterval()+"天");
								logger.debug(eventPrototype.getId()+" |"+"3：活动不自动关闭，活动结束时间："+time4+" 晚于现在时间："+nowDate+"，活动未结束");
							}
						}
					}else{
						logger.debug(eventPrototype.getId()+" |"+"2：活动不自动关闭，活动结束时间："+time3+" 晚于现在时间："+nowDate);
					}
				}else{
					logger.debug(eventPrototype.getId()+" |"+"激活方式为（开服激活、玩家首次登录激活）：" + eventPrototype.getActiveType()+"，不为 0，手动关闭活动");
				}
			}
		}
		/* times 持续时长，活动设置时间到期，自动关闭活动。*/
		
		
		
		model.addAttribute("eventPrototypes", eventPrototypes);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/gm/xyj/eventPrototype/index";
	}
	
	/**
	 * 新增活动员页	
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addEventPrototype(Model model){
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		if (!u.getRoles().equals(User.USER_ROLE_ADMIN)) {	
			List<Stores> stores = new ArrayList<Stores>();
			Stores st = storeService.findById(Integer.valueOf(user.getStoreId()));
			if(st != null){
				stores.add(st);
			}
			List<ServerZone> serverZones = new ArrayList<ServerZone>();
			List<String> s = u.getServerZoneList();
			for (String str : s) {
				ServerZone server = serverZoneService.findById(Long.valueOf(str));
				serverZones.add(server);
			}
			model.addAttribute("stores", stores);
			model.addAttribute("serverZones", serverZones);
		}else{
			List<Stores> stores = new ArrayList<Stores>();
			Stores st = storeService.findById(XYJ);
			if(st != null){
				stores.add(st);
			}
			List<ServerZone> serverZones = serverZoneService.findAll();
			model.addAttribute("stores", stores);
			model.addAttribute("serverZones", serverZones);
		}
		return "/gm/xyj/eventPrototype/add";
	}
	
	/**
	 * 保存并编辑活动条目（活动保存为无效）
	 * @return
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String saveEventPrototype(EventPrototype eventPrototype,ServletRequest request,RedirectAttributes redirectAttributes,Model model){
		ShiroUser user = getCurrentUser();
		if(!eventPrototype.getEventPic().equals("") && eventPrototype.getEventPic() !=null){
			eventPrototype.setEventPic(eventPrototype.getEventPic()+".png");
		}else{
			eventPrototype.setEventPic("");
		}
		if(!eventPrototype.getEventShow().equals("") && eventPrototype.getEventShow() !=null){
			eventPrototype.setEventShow(eventPrototype.getEventShow()+".png");
		}else{
			eventPrototype.setEventShow("");
		}
		
		//logService.log(user.name, user.name+"：xyj 新增一条活动", Log.TYPE_GM_EVENT);
		//redirectAttributes.addFlashAttribute("message", "新增活动成功");
		/** 保存一条status为0（无效）的活动，活动在保存至少一条活动条目时保存 */
		eventPrototype.setStatus(EventPrototype.STATUS_INVALIDE);
		EventPrototype e =  xyjEventPrototypeService.saveRetureEventPrototype(eventPrototype);
		return "redirect:/manage/gm/xyj/eventDataPrototype/add?eventId="+e.getId();
	}
	
	/**
	 * 修改活动页面
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id")long id,Model model){
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		if (!u.getRoles().equals(User.USER_ROLE_ADMIN)) {	
			List<Stores> stores = new ArrayList<Stores>();
			Stores st = storeService.findById(Integer.valueOf(user.getStoreId()));
			if(st != null){
				stores.add(st);
			}
			List<ServerZone> serverZones = new ArrayList<ServerZone>();
			List<String> s = u.getServerZoneList();
			for (String str : s) {
				ServerZone server = serverZoneService.findById(Long.valueOf(str));
				serverZones.add(server);
			}
			model.addAttribute("stores", stores);
			model.addAttribute("serverZones", serverZones);
		}else{
			List<Stores> stores = new ArrayList<Stores>();
			Stores st = storeService.findById(XYJ);
			if(st != null){
				stores.add(st);
			}
			List<ServerZone> serverZones = serverZoneService.findAll();
			model.addAttribute("stores", stores);
			model.addAttribute("serverZones", serverZones);
		}
		
		EventPrototype eventPrototype =  xyjEventPrototypeService.findById(id);
		model.addAttribute("eventPrototype", eventPrototype);
		return "/gm/xyj/eventPrototype/edit";
	}
	
	/**
	 * 修改活动
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String updateEventPrototype(EventPrototype eventPrototype,RedirectAttributes redirectAttributes){
		ShiroUser user = getCurrentUser();
		EventPrototype eventP1 =  xyjEventPrototypeService.findById(eventPrototype.getId());
		/*要求知道活动修改了哪一个字段，并记录日志 logService*/
		if(!eventPrototype.getGameId().equals(eventP1.getGameId())){
			logService.log(user.name, user.name+"：xyj 活动 gameId 字段 "+eventP1.getGameId()+" 修改为 "+eventPrototype.getGameId() , Log.TYPE_GM_EVENT);
			eventP1.setGameId(eventPrototype.getGameId());
		}
		if(!eventPrototype.getServerZoneId().equals(eventP1.getServerZoneId())){
			logService.log(user.name, user.name+"：xyj 活动 serverZoneId 字段 "+eventP1.getServerZoneId()+" 修改为 "+eventPrototype.getServerZoneId() , Log.TYPE_GM_EVENT);
			eventP1.setServerZoneId(eventPrototype.getServerZoneId());
		}
		if(!eventPrototype.getEventType().equals(eventP1.getEventType())){
			logService.log(user.name, user.name+"：xyj 活动 eventType 字段 "+eventP1.getEventType()+" 修改为 "+eventPrototype.getEventType() , Log.TYPE_GM_EVENT);
			eventP1.setEventType(eventPrototype.getEventType());
		}
		if(!eventPrototype.getMainUiPosition().equals(eventP1.getMainUiPosition())){
			logService.log(user.name, user.name+"：xyj 活动 mainUiPosition 字段 "+eventP1.getMainUiPosition()+" 修改为 "+eventPrototype.getMainUiPosition() , Log.TYPE_GM_EVENT);
			eventP1.setMainUiPosition(eventPrototype.getMainUiPosition());
		}
		if(!eventPrototype.getEventPic().equals(eventP1.getEventPic())){
			logService.log(user.name, user.name+"：xyj 活动 eventPic 字段 "+eventP1.getEventPic()+" 修改为 "+eventPrototype.getEventPic() , Log.TYPE_GM_EVENT);
			if(!eventPrototype.getEventPic().equals("") && eventPrototype.getEventPic() !=null){
				eventP1.setEventPic(eventPrototype.getEventPic()+".png");
			}else{
				eventP1.setEventPic("");
			}
		}
		if(!eventPrototype.getEventShow().equals(eventP1.getEventShow())){
			logService.log(user.name, user.name+"：xyj 活动 eventShow 字段 "+eventP1.getEventShow()+" 修改为 "+eventPrototype.getEventShow() , Log.TYPE_GM_EVENT);
			if(!eventPrototype.getEventShow().equals("") && eventPrototype.getEventShow() !=null){
				eventP1.setEventShow(eventPrototype.getEventShow()+".png");
			}else{
				eventP1.setEventShow("");
			}
		}
		if(!eventPrototype.getActiveType().equals(eventP1.getActiveType())){
			logService.log(user.name, user.name+"：xyj 活动 activeType 字段 "+eventP1.getActiveType()+" 修改为 "+eventPrototype.getActiveType() , Log.TYPE_GM_EVENT);
			eventP1.setActiveType(eventPrototype.getActiveType());
		}
		if(!eventPrototype.getActiveData().equals(eventP1.getActiveData())){
			logService.log(user.name, user.name+"：xyj 活动 activeData 字段 "+eventP1.getActiveData()+" 修改为 "+eventPrototype.getActiveData() , Log.TYPE_GM_EVENT);
			eventP1.setActiveData(eventPrototype.getActiveData());
		}
		if(!eventPrototype.getRoleLevelMin().equals(eventP1.getRoleLevelMin())){
			logService.log(user.name, user.name+"：xyj 活动 roleLevelMin 字段 "+eventP1.getRoleLevelMin()+" 修改为 "+eventPrototype.getRoleLevelMin() , Log.TYPE_GM_EVENT);
			eventP1.setRoleLevelMin(eventPrototype.getRoleLevelMin());
		}
		if(!eventPrototype.getRoleLevelMax().equals(eventP1.getRoleLevelMax())){
			logService.log(user.name, user.name+"：xyj 活动 roleLevelMax 字段 "+eventP1.getRoleLevelMax()+" 修改为 "+eventPrototype.getRoleLevelMax() , Log.TYPE_GM_EVENT);
			eventP1.setRoleLevelMax(eventPrototype.getRoleLevelMax());
		}
		if(!eventPrototype.getTimes().equals(eventP1.getTimes())){
			logService.log(user.name, user.name+"：xyj 活动 times 字段 "+eventP1.getTimes()+" 修改为 "+eventPrototype.getTimes() , Log.TYPE_GM_EVENT);
			eventP1.setTimes(eventPrototype.getTimes());
		}
		if(!eventPrototype.getActiveDelay().equals(eventP1.getActiveDelay())){
			logService.log(user.name, user.name+"：xyj 活动 activeDelay 字段 "+eventP1.getActiveDelay()+" 修改为 "+eventPrototype.getActiveDelay() , Log.TYPE_GM_EVENT);
			eventP1.setActiveDelay(eventPrototype.getActiveDelay());
		}
		if(!eventPrototype.getActiveDay().equals(eventP1.getActiveDay())){
			logService.log(user.name, user.name+"：xyj 活动 activeDay 字段 "+eventP1.getActiveDay()+" 修改为 "+eventPrototype.getActiveDay() , Log.TYPE_GM_EVENT);
			eventP1.setActiveDay(eventPrototype.getActiveDay());
		}
		if(!eventPrototype.getEventRepeatInterval().equals(eventP1.getEventRepeatInterval())){
			logService.log(user.name, user.name+"：xyj 活动 eventRepeatInterval 字段 "+eventP1.getEventRepeatInterval()+" 修改为 "+eventPrototype.getEventRepeatInterval() , Log.TYPE_GM_EVENT);
			eventP1.setEventRepeatInterval(eventPrototype.getEventRepeatInterval());
		}
		if(!eventPrototype.getEventName().equals(eventP1.getEventName())){
			logService.log(user.name, user.name+"：xyj 活动 eventName 字段 "+eventP1.getEventName()+" 修改为 "+eventPrototype.getEventName() , Log.TYPE_GM_EVENT);
			eventP1.setEventName(eventPrototype.getEventName());
		}
		if(!eventPrototype.getEventTitle().equals(eventP1.getEventTitle())){
			logService.log(user.name, user.name+"：xyj 活动 eventTitle 字段 "+eventP1.getEventTitle()+" 修改为 "+eventPrototype.getEventTitle() , Log.TYPE_GM_EVENT);
			eventP1.setEventTitle(eventPrototype.getEventTitle());
		}
		if(!eventPrototype.getEventDes().equals(eventP1.getEventDes())){
			logService.log(user.name, user.name+"：xyj 活动 eventDes 字段 "+eventP1.getEventDes()+" 修改为 "+eventPrototype.getEventDes() , Log.TYPE_GM_EVENT);
			eventP1.setEventDes(eventPrototype.getEventDes());
		}
		if(!eventPrototype.getEventIcon().equals(eventP1.getEventIcon())){
			logService.log(user.name, user.name+"：xyj 活动 eventIcon 字段 "+eventP1.getEventIcon()+" 修改为 "+eventPrototype.getEventIcon() , Log.TYPE_GM_EVENT);
			eventP1.setEventIcon(eventPrototype.getEventIcon());
		}
		if(!eventPrototype.getListPriority().equals(eventP1.getListPriority())){
			logService.log(user.name, user.name+"：xyj 活动 listPriority 字段 "+eventP1.getListPriority()+" 修改为 "+eventPrototype.getListPriority() , Log.TYPE_GM_EVENT);
			eventP1.setListPriority(eventPrototype.getListPriority());
		}
		if(!eventPrototype.getDoneHiding().equals(eventP1.getDoneHiding())){
			logService.log(user.name, user.name+"：xyj 活动 doneHiding 字段 "+eventP1.getDoneHiding()+" 修改为 "+eventPrototype.getDoneHiding() , Log.TYPE_GM_EVENT);
			eventP1.setDoneHiding(eventPrototype.getDoneHiding());
		}
		if(!eventPrototype.getFollowingEvent().equals(eventP1.getFollowingEvent())){
			logService.log(user.name, user.name+"：xyj 活动 followingEvent 字段 "+eventP1.getFollowingEvent()+" 修改为 "+eventPrototype.getFollowingEvent() , Log.TYPE_GM_EVENT);
			eventP1.setFollowingEvent(eventPrototype.getFollowingEvent());
		}
		/*要求知道活动修改了哪一个字段，并记录日志 logService*/
		xyjEventPrototypeService.save(eventP1);
		
		List<GoAllServer> servers =  goAllServerService.findAllByStoreIdAndServerZoneId(Integer.valueOf(eventP1.getGameId()), Integer.valueOf(eventP1.getServerZoneId()));
		int choose = 0,success = 0,fail = 0;
        List<String> objFail = new ArrayList<String>();
		for (GoAllServer goAllServer : servers) {
			eventP1.setServerId(goAllServer.getServerId());
			JSONObject jsonObject = JSONObject.fromObject(eventP1);
			jsonObject.put("id", eventP1.getId().toString());
			jsonObject.remove("status");
    		JSONObject res = HttpClientUts.doPost(gm_url+"/xyjserver/eventPrototype/updateEventPrototype" , jsonObject);
			System.out.println("多个 xyj EventPrototype 修改活动，返回值" + res);
			choose += Integer.valueOf(res.getString("choose"));
			success += Integer.valueOf(res.getString("success"));
			fail += Integer.valueOf(res.getString("fail"));
			objFail.add(res.getString("objFail"));
		}
		redirectAttributes.addFlashAttribute("message","xyj 运营大区："+eventP1.getServerZoneId()+ " 下的在线服务器 "+choose+" 个，修改活动成功 "+ success+" 个，失败 "+fail+" 个，新增失败的服务器有："+StringUtils.join(objFail.toArray(), " "));
		//redirectAttributes.addFlashAttribute("message", "修改活动成功");
	    return "redirect:/manage/gm/xyj/eventPrototype/index";
	}
	
	/**
	 * 关闭活动	 
	 * @param oid 用户id
	 * @throws Exception 
	 */
	@RequestMapping(value = "close", method = RequestMethod.GET)
	public String closeEventPrototype(@RequestParam(value = "id")Long id,RedirectAttributes redirectAttributes) throws Exception{
		ShiroUser user = getCurrentUser();
		 EventPrototype eventPrototype =  xyjEventPrototypeService.findById(id);
		 logService.log(user.name, user.name+"：xyj 关闭活动 times 字段 "+eventPrototype.getTimes()+" 修改为 0", Log.TYPE_GM_EVENT);
		 eventPrototype.setTimes("0"); /**times 0 活动关闭 如果填写-1 则无限时长 **/
		 xyjEventPrototypeService.save(eventPrototype);
		 
		 /*条目关闭时间服从所从属的活动关闭时间，如果活动关闭则其下条目全部强制关闭*/
		 List<EventDataPrototype> eventDataPrototypes =  xyjEventDataPrototypeService.findAllByEventId(id);
		 for (EventDataPrototype eventDataPrototype : eventDataPrototypes) {
			 eventDataPrototype.setEventDataTimes("0");
			 xyjEventDataPrototypeService.save(eventDataPrototype);
		 }
		 
		List<GoAllServer> servers =  goAllServerService.findAllByStoreIdAndServerZoneId(Integer.valueOf(eventPrototype.getGameId()), Integer.valueOf(eventPrototype.getServerZoneId()));
		int choose = 0,success = 0,fail = 0;
	    List<String> objFail = new ArrayList<String>();
		for (GoAllServer goAllServer : servers) {
			String	account = HttpClientUts.doGet(gm_url+"/xyjserver/eventPrototype/closeEventPrototype?id="+eventPrototype.getId()+"&gameId="+eventPrototype.getGameId()+"&serverZoneId="+eventPrototype.getServerZoneId()+"&serverId="+goAllServer.getServerId(), "utf-8");
			JSONObject dataJson=JSONObject.fromObject(account);
			System.out.println("多个 xyj EventPrototype 关闭，返回值" + dataJson);
			choose ++;
			if(dataJson.get("message").equals("success")){
				success++;
			}else{
				fail++;
				objFail.add(goAllServer.getServerId());
			}
		}
		 redirectAttributes.addFlashAttribute("message","xyj 运营大区："+eventPrototype.getServerZoneId()+ " 下的在线服务器 "+choose+" 个，关闭活动成功 "+ success+" 个，失败 "+fail+" 个，关闭失败的服务器有："+StringUtils.join(objFail.toArray(), " "));
		 return "redirect:/manage/gm/xyj/eventPrototype/index";
	}	 
	
	public void closeEventPrototype(@RequestParam(value = "id")Long id) throws Exception{
		 ShiroUser user = getCurrentUser();
		 EventPrototype eventPrototype =  xyjEventPrototypeService.findById(id);
		 logService.log(user.name, "自动关闭：xyj 关闭活动 times 字段 "+eventPrototype.getTimes()+" 修改为 0", Log.TYPE_GM_EVENT);
		 eventPrototype.setTimes("0"); /**times 0 活动关闭 如果填写-1 则无限时长 **/
		 xyjEventPrototypeService.save(eventPrototype);
		 
		 /*条目关闭时间服从所从属的活动关闭时间，如果活动关闭则其下条目全部强制关闭*/
		 List<EventDataPrototype> eventDataPrototypes =  xyjEventDataPrototypeService.findAllByEventId(id);
		 for (EventDataPrototype eventDataPrototype : eventDataPrototypes) {
			 eventDataPrototype.setEventDataTimes("0");
			 xyjEventDataPrototypeService.save(eventDataPrototype);
		 }
	}	
	
	/**
	 * 删除活动	 
	 * @param oid 用户id
	 * @throws Exception 
	 */
	@RequestMapping(value = "del", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> delServerZone(@RequestParam(value = "id")Long id) throws Exception{
		 Map<String,Object> map = new HashMap<String, Object>();

		 map.put("success", "true");
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
}
