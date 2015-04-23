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

import com.enlight.game.entity.PlatForm;
import com.enlight.game.entity.ServerZone;
import com.enlight.game.entity.User;
import com.enlight.game.service.account.AccountService;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;
import com.enlight.game.service.platForm.PlatFormService;
import com.enlight.game.service.serverZone.ServerZoneService;
import com.google.common.collect.Maps;

@Controller("platFormController")
@RequestMapping("/manage/platForm")
public class PlatFormController extends BaseController{
	
	private static final String PAGE_SIZE = "30";
	
	private static final Logger logger = LoggerFactory.getLogger(PlatFormController.class);
	
	private static Map<String,String> sortTypes = Maps.newLinkedHashMap();
	
	static{
		sortTypes.put("auto","自动");
		sortTypes.put("id", "Id");
		sortTypes.put("pfId", "渠道ID");
		sortTypes.put("pfName", "渠道名称");
	}
	
	public static Map<String, String> getSortTypes() {
		return sortTypes;
	}
	
	public static void setSortTypes(Map<String, String> sortTypes) {
		PlatFormController.sortTypes = sortTypes;
	}

	public Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}

	@Autowired
	private PlatFormService platFormService;
	
	@Autowired
	private ServerZoneService serverZoneService;
	
	@Autowired
	private AccountService accountService;
	
	@RequestMapping(value = "index",method=RequestMethod.GET)
	public String index(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto")String sortType, Model model,
			ServletRequest request){
		Long userId = getCurrentUserId();
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		logger.info("userId"+userId+"渠道信息设置");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<PlatForm> platForms = platFormService.findPlatFormByCondition(userId,searchParams, pageNumber, pageSize, sortType);
		for (PlatForm platForm : platForms) {
			ServerZone serverZone = serverZoneService.findById(Long.valueOf(platForm.getServerZoneId()));
			platForm.setServerZone(serverZone);
		}
		model.addAttribute("platForms", platForms);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		if (!u.getRoles().equals(User.USER_ROLE_ADMIN)) {	
			List<ServerZone> serverZones = new ArrayList<ServerZone>();
			List<String> s = u.getServerZoneList();
			for (String str : s) {
				ServerZone server = serverZoneService.findById(Long.valueOf(str));
				serverZones.add(server);
			}
			model.addAttribute("serverZones", serverZones);
		}else{
			List<ServerZone> serverZones = serverZoneService.findAll();
			model.addAttribute("serverZones", serverZones);
		}
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/platForm/index";
	}
	
	/**
	 * 新增页面
	 */
	@RequestMapping(value = "/add" ,method=RequestMethod.GET)
	public String add(Model model){
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		if (!u.getRoles().equals(User.USER_ROLE_ADMIN)) {	
			List<ServerZone> serverZones = new ArrayList<ServerZone>();
			List<String> s = u.getServerZoneList();
			for (String str : s) {
				ServerZone server = serverZoneService.findById(Long.valueOf(str));
				serverZones.add(server);
			}
			model.addAttribute("serverZones", serverZones);
		}else{
			List<ServerZone> serverZones = serverZoneService.findAll();
			model.addAttribute("serverZones", serverZones);
		}
		return "/platForm/add";
	}
	
	/**
	 * 新增
	 */
	@RequestMapping(value = "/save",method=RequestMethod.POST)
	public String save(PlatForm platForm,ServletRequest request,RedirectAttributes redirectAttributes,Model model){
		platFormService.save(platForm);
		redirectAttributes.addFlashAttribute("message", "新增成功");
		return "redirect:/manage/platForm/index";
	}
	
	@RequestMapping(value="edit",method=RequestMethod.GET)
	public String edit(@RequestParam(value="id")long id,Model model){
		PlatForm platForm = platFormService.findById(id);
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		if (!u.getRoles().equals(User.USER_ROLE_ADMIN)) {	
			List<ServerZone> serverZones = new ArrayList<ServerZone>();
			List<String> s = u.getServerZoneList();
			for (String str : s) {
				ServerZone server = serverZoneService.findById(Long.valueOf(str));
				serverZones.add(server);
			}
			model.addAttribute("serverZones", serverZones);
		}else{
			List<ServerZone> serverZones = serverZoneService.findAll();
			model.addAttribute("serverZones", serverZones);
		}
		model.addAttribute("platForm", platForm);
		return "/platForm/edit";
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(value = "/update",method=RequestMethod.POST)
	public String update(PlatForm platForm,ServletRequest request,RedirectAttributes redirectAttributes){
		platFormService.update(platForm);
		redirectAttributes.addFlashAttribute("message", "更新成功");
		return "redirect:/manage/platForm/index";
	}
	
	/**
	 * 删除操作	 
	 * @param oid 用户id
	 * @throws Exception 
	 */
	@RequestMapping(value = "del", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> delServerZone(@RequestParam(value = "id")Long id) throws Exception{
		 Map<String,Object> map = new HashMap<String, Object>();
		 platFormService.delById(id);
		 map.put("success", "true");
		 return map;
	}

	/**
	 * 权限详细
	 * @param id 用户id
	 */
	@RequestMapping(value = "detail", method = RequestMethod.GET)
	public String show(@RequestParam(value = "id")long id,Model model){
		PlatForm platForm = platFormService.findById(id);
		ServerZone serverZone = serverZoneService.findById(Long.valueOf(platForm.getServerZoneId()));
		platForm.setServerZone(serverZone);
		model.addAttribute("platForm", platForm);
		return "/platForm/info";
	}
	
	
	/**
	 * Ajax请求校验ID是否唯一。
	 */
	@RequestMapping(value = "/checkId")
	@ResponseBody
	public String checkId(@RequestParam("pfId") String pfId) {
		if (platFormService.findByPfId(pfId)== null) {
			return "true";
		} else {
			return "false";
		}
	}
	
	public ShiroUser getCurrentUser() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user;
	}
}
