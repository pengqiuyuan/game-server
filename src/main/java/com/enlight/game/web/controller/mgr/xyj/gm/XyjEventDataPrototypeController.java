package com.enlight.game.web.controller.mgr.xyj.gm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
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
import com.enlight.game.entity.gm.xyj.EventDataPrototypeInstruction;
import com.enlight.game.entity.gm.xyj.EventPrototype;
import com.enlight.game.service.account.AccountService;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;
import com.enlight.game.service.gm.xyj.XyjEventDataPrototypeInstructionService;
import com.enlight.game.service.gm.xyj.XyjEventDataPrototypeService;
import com.enlight.game.service.gm.xyj.XyjEventPrototypeService;
import com.enlight.game.service.log.LogService;
import com.enlight.game.service.serverZone.ServerZoneService;
import com.enlight.game.service.store.StoreService;
import com.enlight.game.service.user.UserService;
import com.enlight.game.web.controller.mgr.BaseController;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

@Controller("xyjEventDataPrototypeController")
@RequestMapping(value = "/manage/gm/xyj/eventDataPrototype")
public class XyjEventDataPrototypeController extends BaseController{

	private static final String PAGE_SIZE = "15";
	
	private static final Integer XYJ = 4; //数据库、excel表 ，xyj项目storeId为4

	private static final Logger logger = LoggerFactory.getLogger(XyjEventDataPrototypeController.class);
	
	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("registerDate", "时间");
	}

	public static Map<String, String> getSortTypes() {
		return sortTypes;
	}

	public static void setSortTypes(Map<String, String> sortTypes) {
		XyjEventDataPrototypeController.sortTypes = sortTypes;
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
	private XyjEventDataPrototypeService xyjEventDataPrototypeService;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private XyjEventDataPrototypeInstructionService xyjEventDataPrototypeInstructionService;
	
	/**
	 * 活动下的条目列表
	 * @param pageNumber 当前	 
	 * @param pageSize   显示条数
	 * @param sortType  排序
	 * @param model   返回对象
	 * @param request  封装的请	
	 * @throws ParseException 
	 */
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto")String sortType, Model model,
			ServletRequest request) throws ParseException{
		Long userId = getCurrentUserId();
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<EventDataPrototype> eventDataPrototypes = xyjEventDataPrototypeService.findEventDataPrototypesByCondition(userId, searchParams, pageNumber, pageSize, sortType);
		
		model.addAttribute("eventDataPrototypes", eventDataPrototypes);
		model.addAttribute("eventId", request.getParameter("search_EQ_eventId"));
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/gm/xyj/eventDataPrototype/index";
	}
	
	/**
	 * 新增活动条目页面
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addEventDataPrototype(Model model,@RequestParam(value =  "eventId") long eventId,@RequestParam(value =  "group" , required = false) String group){
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		List<EventDataPrototypeInstruction> eventDataPrototypeInstructions = xyjEventDataPrototypeInstructionService.findAll();
		EventPrototype eventPrototype = xyjEventPrototypeService.findById(eventId);
		model.addAttribute("eventPrototype", eventPrototype);
		model.addAttribute("eventDataPrototypeInstructions", eventDataPrototypeInstructions);
		/* 页面两个按钮，
		 * 保存了至少一条条目（活动是有效的status为1），按钮为返回，
		 * 一条条目也没有保存（活动是无效的status为0）按钮为放弃编辑*/
		model.addAttribute("giveUpOrReturn",eventPrototype.getStatus());
		model.addAttribute("group",group);/*点击保存并新增则保存当前条目，并在其下方新增出新的条目编辑，ID自增1，从属ID不变，group等于上一条条目*/
		return "/gm/xyj/eventDataPrototype/add";
	}
	
	/**
	 * 新增活动条目放弃编辑
	 * @return
	 */
	@RequestMapping(value = "giveup", method = RequestMethod.GET)
	public String giveup(Model model,@RequestParam(value =  "eventId") long eventId){
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		/* 点击放弃编辑，删除这条 status为0的活动*/
		xyjEventPrototypeService.deleteByStatusInvalide(eventId);
		return "redirect:/manage/gm/xyj/eventPrototype/index";
	}
	
	/**
	 * @return
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String saveEventDataPrototype(EventDataPrototype eventDataPrototype,RedirectAttributes redirectAttributes,ServletRequest request,Model model){
		ShiroUser user = getCurrentUser();
		/*先修改活动status 无效为有效*/
		EventPrototype eventPrototype = xyjEventPrototypeService.findById(eventDataPrototype.getEventId());
		eventPrototype.setStatus(EventPrototype.STATUS_VALIDE);
		EventPrototype eventPrototype2 =  xyjEventPrototypeService.saveRetureEventPrototype(eventPrototype);
		logService.log(user.name, user.name+"：xyj 新增一条活动", Log.TYPE_GM_EVENT);
		
		String[] eventRewards = request.getParameterValues("eventRewards");
		String[] eventRewardsNum = request.getParameterValues("eventRewardsNum");
		String eRewards=StringUtils.join(eventRewards, "_");
		String eRewardsNum = StringUtils.join(eventRewardsNum, "_");
		
		eventDataPrototype.setEventRewards(eRewards);
		eventDataPrototype.setEventRewardsNum(eRewardsNum);
		EventDataPrototype e =  xyjEventDataPrototypeService.saveReturnEventData(eventDataPrototype);
		logService.log(user.name, user.name+"：xyj 活动 "+eventDataPrototype.getEventId()+" 新增一条活动条目", Log.TYPE_GM_EVENT);
		
		if(request.getParameter("submitAndEventData") !=null){ /**保存并新增条目*/
			redirectAttributes.addFlashAttribute("message", "xyj 活动 "+eventDataPrototype.getEventId()+" 新增一条条目成功！请继续新增条目或者返回活动列表！");
			return "redirect:/manage/gm/xyj/eventDataPrototype/add?eventId="+eventPrototype2.getId() + "&group="+e.getGroup();
		}else{
			redirectAttributes.addFlashAttribute("message", "xyj 活动 "+eventDataPrototype.getEventId()+" 新增一条条目成功！");
			return "redirect:/manage/gm/xyj/eventPrototype/index";
		}
	}
	
	/**
	 * 修改活动条目页面
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "eventId")long eventId,@RequestParam(value = "eventDataId")long eventDataId,Model model){
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		List<EventDataPrototypeInstruction> eventDataPrototypeInstructions = xyjEventDataPrototypeInstructionService.findAll();
		EventDataPrototype eventDataPrototype = xyjEventDataPrototypeService.findAllByEventIdAndEventDataId(eventId, eventDataId);
		Map<String, String> map = new HashMap<String, String>();
		System.out.println("11111  "+eventDataPrototype.getEventRewards()  +  "     "  +  eventDataPrototype.getEventRewardsNum());
		List<String> eventRewards = ImmutableList.copyOf(StringUtils.split(eventDataPrototype.getEventRewards(), "_"));
		List<String> eventRewardsNum = ImmutableList.copyOf(StringUtils.split(eventDataPrototype.getEventRewardsNum(), "_"));
		for (int i = 0; i < eventRewards.size(); i++) {
			map.put(eventRewards.get(i), eventRewardsNum.get(i));
		}
		EventPrototype eventPrototype = xyjEventPrototypeService.findById(eventId);
		model.addAttribute("eventPrototype", eventPrototype);
		model.addAttribute("eventDataPrototype", eventDataPrototype);
		model.addAttribute("eventDataPrototypeInstructions", eventDataPrototypeInstructions);
		
		model.addAttribute("eventRewardsMap", map);
		EventDataPrototypeInstruction eventInstr = xyjEventDataPrototypeInstructionService.findById(Long.valueOf(eventDataPrototype.getEventCondition()));
		model.addAttribute("eventInstr", eventInstr);
		return "/gm/xyj/eventDataPrototype/edit";
	}
	
	/**
	 * 修改活动条目
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String updateEventPrototype(EventDataPrototype eventDataPrototype,RedirectAttributes redirectAttributes,ServletRequest request,Model model){
		
		String[] eventRewards = request.getParameterValues("eventRewards");
		String[] eventRewardsNum = request.getParameterValues("eventRewardsNum");
		String eRewards=StringUtils.join(eventRewards, "_");
		String eRewardsNum = StringUtils.join(eventRewardsNum, "_");
		System.out.println("22222  "+eRewards + "   "  + eRewardsNum);

		ShiroUser user = getCurrentUser();
		EventDataPrototype eventP1 = xyjEventDataPrototypeService.findByEventDataId(eventDataPrototype.getEventDataId());
		System.out.println(eventP1.getEventRewardsNum() + "   3333  "  + eRewardsNum);
		/*要求知道活动条目修改了哪一个字段，并记录日志 logService*/
		if(!eventDataPrototype.getEventId().equals(eventP1.getEventId())){
			logService.log(user.name, user.name+"：xyj "+eventP1.getEventId()+" 活动下修改条目 "+ eventP1.getEventDataId() +" "
					+ "的eventId 字段 "+eventP1.getEventId()+" 修改为 "+eventDataPrototype.getEventId(), Log.TYPE_GM_EVENT);
			eventP1.setEventId(eventDataPrototype.getEventId());
		} if(!eventDataPrototype.getGroup().equals(eventP1.getGroup())){
			logService.log(user.name, user.name+"：xyj "+eventP1.getEventId()+" 活动下修改条目 "+ eventP1.getEventDataId() +" "
					+ "的group 字段 "+eventP1.getGroup()+" 修改为 "+eventDataPrototype.getGroup(), Log.TYPE_GM_EVENT);
			eventP1.setGroup(eventDataPrototype.getGroup());
		} if(!eventDataPrototype.getEventDataName().equals(eventP1.getEventDataName())){
			logService.log(user.name, user.name+"：xyj "+eventP1.getEventId()+" 活动下修改条目 "+ eventP1.getEventDataId() +" "
					+ "的eventDataName 字段 "+eventP1.getEventDataName()+" 修改为 "+eventDataPrototype.getEventDataName(), Log.TYPE_GM_EVENT);
			eventP1.setEventDataName(eventDataPrototype.getEventDataName());
		} if(!eventDataPrototype.getVipMin().equals(eventP1.getVipMin())){
			logService.log(user.name, user.name+"：xyj "+eventP1.getEventId()+" 活动下修改条目 "+ eventP1.getEventDataId() +" "
					+ "的vipMin 字段 "+eventP1.getVipMin()+" 修改为 "+eventDataPrototype.getVipMin(), Log.TYPE_GM_EVENT);
			eventP1.setVipMin(eventDataPrototype.getVipMin());
		} if(!eventDataPrototype.getVipMax().equals(eventP1.getVipMax())){
			logService.log(user.name, user.name+"：xyj "+eventP1.getEventId()+" 活动下修改条目 "+ eventP1.getEventDataId() +" "
					+ "的vipMax 字段 "+eventP1.getVipMax()+" 修改为 "+eventDataPrototype.getVipMax(), Log.TYPE_GM_EVENT);
			eventP1.setVipMax(eventDataPrototype.getVipMax());
		} if(!eventDataPrototype.getEventDataTimes().equals(eventP1.getEventDataTimes())){
			logService.log(user.name, user.name+"：xyj "+eventP1.getEventId()+" 活动下修改条目 "+ eventP1.getEventDataId() +" "
					+ "的eventDataTimes 字段 "+eventP1.getEventDataTimes()+" 修改为 "+eventDataPrototype.getEventDataTimes(), Log.TYPE_GM_EVENT);
			eventP1.setEventDataTimes(eventDataPrototype.getEventDataTimes());
		} if(!eventDataPrototype.getEventDataDelay().equals(eventP1.getEventDataDelay())){
			logService.log(user.name, user.name+"：xyj "+eventP1.getEventId()+" 活动下修改条目 "+ eventP1.getEventDataId() +" "
					+ "的eventDataDelay 字段 "+eventP1.getEventDataDelay()+" 修改为 "+eventDataPrototype.getEventDataDelay(), Log.TYPE_GM_EVENT);
			eventP1.setEventDataDelay(eventDataPrototype.getEventDataDelay());
		} if(!eventDataPrototype.getEventDataDes().equals(eventP1.getEventDataDes())){
			logService.log(user.name, user.name+"：xyj "+eventP1.getEventId()+" 活动下修改条目 "+ eventP1.getEventDataId() +" "
					+ "的eventDataDes 字段 "+eventP1.getEventDataDes()+" 修改为 "+eventDataPrototype.getEventDataDes(), Log.TYPE_GM_EVENT);
			eventP1.setEventDataDes(eventDataPrototype.getEventDataDes());
		} if(!eventDataPrototype.getEventCondition().equals(eventP1.getEventCondition())){
			logService.log(user.name, user.name+"：xyj "+eventP1.getEventId()+" 活动下修改条目 "+ eventP1.getEventDataId() +" "
					+ "的eventCondition 字段 "+eventP1.getEventCondition()+" 修改为 "+eventDataPrototype.getEventCondition(), Log.TYPE_GM_EVENT);
			eventP1.setEventCondition(eventDataPrototype.getEventCondition());
		} if(!eventDataPrototype.getEventConditionType().equals(eventP1.getEventConditionType())){
			logService.log(user.name, user.name+"：xyj "+eventP1.getEventId()+" 活动下修改条目 "+ eventP1.getEventDataId() +" "
					+ "的eventConditionType 字段 "+eventP1.getEventConditionType()+" 修改为 "+eventDataPrototype.getEventConditionType(), Log.TYPE_GM_EVENT);
			eventP1.setEventConditionType(eventDataPrototype.getEventConditionType());
		} if(!eventDataPrototype.getConditionValue1().equals(eventP1.getConditionValue1())){
			logService.log(user.name, user.name+"：xyj "+eventP1.getEventId()+" 活动下修改条目 "+ eventP1.getEventDataId() +" "
					+ "的conditionValue1 字段 "+eventP1.getConditionValue1()+" 修改为 "+eventDataPrototype.getConditionValue1(), Log.TYPE_GM_EVENT);
			eventP1.setConditionValue1(eventDataPrototype.getConditionValue1());
		} if(!eventDataPrototype.getConditionValue2().equals(eventP1.getConditionValue2())){
			logService.log(user.name, user.name+"：xyj "+eventP1.getEventId()+" 活动下修改条目 "+ eventP1.getEventDataId() +" "
					+ "的conditionValue2 字段 "+eventP1.getConditionValue2()+" 修改为 "+eventDataPrototype.getConditionValue2(), Log.TYPE_GM_EVENT);
			eventP1.setConditionValue2(eventDataPrototype.getConditionValue2());
		} if(!eRewards.equals(eventP1.getEventRewards())){
			logService.log(user.name, user.name+"：xyj "+eventP1.getEventId()+" 活动下修改条目 "+ eventP1.getEventDataId() +" "
					+ "的eventRewards 字段 "+eventP1.getEventRewards()+" 修改为 "+eRewards, Log.TYPE_GM_EVENT);
			eventP1.setEventRewards(eRewards);
		} if(!eRewardsNum.equals(eventP1.getEventRewardsNum())){
			System.out.println("tttttttttttttt");
			logService.log(user.name, user.name+"：xyj "+eventP1.getEventId()+" 活动下修改条目 "+ eventP1.getEventDataId() +" "
					+ "的eventRewardsNum 字段 "+eventP1.getEventRewardsNum()+" 修改为 "+eRewardsNum, Log.TYPE_GM_EVENT);
			eventP1.setEventRewardsNum(eRewardsNum);
		}
		System.out.println(eventP1.getEventRewardsNum() + "   4444444  "  + eRewardsNum);
		xyjEventDataPrototypeService.save(eventP1);
		redirectAttributes.addFlashAttribute("message", "修改活动条目 "+eventP1.getEventDataId()+" 成功");
	    return "redirect:/manage/gm/xyj/eventDataPrototype/index?search_EQ_eventId="+eventDataPrototype.getEventId();
	}
	 
	
	/**
	 * 删除活动	条目 
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
	 * 活动条目配置文件
	 * @param oid 用户id
	 * @throws Exception 
	 */
	@RequestMapping(value = "findEventDpi", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<EventDataPrototypeInstruction> findEventDpi() throws Exception{
		List<EventDataPrototypeInstruction> eventDataPrototypeInstructions = xyjEventDataPrototypeInstructionService.findAll();
		return eventDataPrototypeInstructions;
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
