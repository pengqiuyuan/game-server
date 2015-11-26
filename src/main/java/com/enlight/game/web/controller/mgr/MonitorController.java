package com.enlight.game.web.controller.mgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.enlight.game.entity.Monitor;
import com.enlight.game.entity.Stores;
import com.enlight.game.entity.User;
import com.enlight.game.service.account.AccountService;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;
import com.enlight.game.service.monitor.MonitorService;
import com.enlight.game.service.store.StoreService;
import com.google.common.collect.Maps;

@Controller("monitorController")
@RequestMapping("/manage/monitor")
public class MonitorController extends BaseController{

	private static final String PAGE_SIZE = "50";
	
	private static final Logger logger = LoggerFactory.getLogger(MonitorController.class);
	
	private static Map<String,String> sortTypes = Maps.newLinkedHashMap();
	
	static{
		sortTypes.put("auto","自动");
		sortTypes.put("key", "key");
	}
	
	public static Map<String, String> getSortTypes() {
		return sortTypes;
	}
	
	public static void setSortTypes(Map<String, String> sortTypes) {
		MonitorController.sortTypes = sortTypes;
	}
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private MonitorService monitorService;
	
	@RequestMapping(value = "index",method=RequestMethod.GET)
	public String index(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto")String sortType, Model model,
			ServletRequest request){
		Long userId = getCurrentUserId();
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		logger.info("userId"+userId+"监控");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<Monitor> monitors = monitorService.findMonitorByCondition(userId,searchParams, pageNumber, pageSize, sortType);
		model.addAttribute("monitors", monitors);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		
		if (!u.getRoles().equals(User.USER_ROLE_ADMIN)) {
			List<Stores> stores = new ArrayList<Stores>();
			Stores sto=  storeService.findById(Long.valueOf(user.getStoreId()));
			stores.add(sto);
			model.addAttribute("stores", stores);
		}else{
			List<Stores> stores =  storeService.findList();
			model.addAttribute("stores", stores);
		}

		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/monitor/index";
	}
	
	/**
	 * 新增页面
	 */
	@RequestMapping(value = "/add" ,method=RequestMethod.GET)
	public String add(Model model){
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
		return "/monitor/add";
	}
	
	/**
	 * 新增
	 */
	@RequestMapping(value = "/save",method=RequestMethod.POST)
	public String save(Monitor monitor,ServletRequest request,RedirectAttributes redirectAttributes,Model model){
		monitorService.save(monitor);
		redirectAttributes.addFlashAttribute("message", "新增成功");
		return "redirect:/manage/monitor/index?search_LIKE_storeId="+monitor.getStoreId();
	}
	
	@RequestMapping(value="edit",method=RequestMethod.GET)
	public String edit(@RequestParam(value="id")long id,Model model){
		Monitor monitor = monitorService.findByMoId(id);
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
		model.addAttribute("monitor", monitor);
		return "/monitor/edit";
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(value = "/update",method=RequestMethod.POST)
	public String update(Monitor monitor,ServletRequest request,RedirectAttributes redirectAttributes){
		monitorService.update(monitor);
		redirectAttributes.addFlashAttribute("message", "更新成功");
		return "redirect:/manage/monitor/index?search_LIKE_storeId="+monitor.getStoreId();
	}
	
	/**
	 * 删除操作	 
	 * @param oid 用户id
	 * @throws Exception 
	 */
	@RequestMapping(value = "del", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> delMonitorZone(@RequestParam(value = "id")Long id) throws Exception{
		 Map<String,Object> map = new HashMap<String, Object>();
		 monitorService.delById(id);
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
