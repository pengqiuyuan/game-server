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
	 * 新增活动条目页面
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addEventDataPrototype(Model model,@RequestParam(value =  "eventId") long eventId){
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
		eventPrototype.setStatus(EventPrototype.STATUS_INVALIDE);
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
		redirectAttributes.addFlashAttribute("message", "xyj 活动 "+eventDataPrototype.getEventId()+" 新增一条条目成功");
		
		if(request.getParameter("submitAndEventData") !=null){ /**保存并新增条目*/
			List<EventDataPrototypeInstruction> eventDataPrototypeInstructions = xyjEventDataPrototypeInstructionService.findAll();
			model.addAttribute("eventPrototype", eventPrototype2);
			model.addAttribute("eventDataPrototypeInstructions", eventDataPrototypeInstructions);
			model.addAttribute("group", e.getGroup());
			return "/gm/xyj/eventDataPrototype/add";
		}else{
			logService.log(user.name, user.name+"：xyj 活动 "+eventDataPrototype.getEventId()+" 新增一条活动条目", Log.TYPE_GM_EVENT);
			redirectAttributes.addFlashAttribute("message", "xyj 活动 "+eventDataPrototype.getEventId()+" 新增一条条目成功");
			return "redirect:/manage/gm/xyj/eventPrototype/index";
		}
	}
	
	/**
	 * 修改活动条目页面
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "eventId")long eventId,Model model){
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
		
		return "/gm/xyj/eventPrototype/edit";
	}
	
	/**
	 * 修改活动条目
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String updateEventPrototype(EventDataPrototype eventDataPrototype,RedirectAttributes redirectAttributes){
		ShiroUser user = getCurrentUser();
		/*要求知道活动修改了哪一个字段，并记录日志 logService*/
		redirectAttributes.addFlashAttribute("message", "修改活动条目成功");
	    return "redirect:/manage/gm/xyj/eventPrototype/edit";
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
