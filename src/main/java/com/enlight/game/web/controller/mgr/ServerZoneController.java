package com.enlight.game.web.controller.mgr;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.enlight.game.entity.ServerZone;
import com.enlight.game.entity.User;
import com.enlight.game.service.account.AccountService;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;
import com.enlight.game.service.platForm.PlatFormService;
import com.enlight.game.service.server.ServerService;
import com.enlight.game.service.serverZone.ServerZoneService;
import com.enlight.game.service.user.UserService;
import com.google.common.collect.Maps;

@Controller("serverZoneController")
@RequestMapping(value = "/manage/serverZone")
public class ServerZoneController extends BaseController{

	private static final String PAGE_SIZE = "15";

	private static final Logger logger = LoggerFactory.getLogger(ServerZoneController.class);
	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("registerDate", "时间");
	}

	public static Map<String, String> getSortTypes() {
		return sortTypes;
	}

	public static void setSortTypes(Map<String, String> sortTypes) {
		ServerZoneController.sortTypes = sortTypes;
	}
	
	@Override
	@InitBinder
	protected void initBinder(ServletRequestDataBinder binder){
		binder.registerCustomEditor(Date.class,"registerDate",new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
	}
	
	@Autowired
	private ServerZoneService serverZoneService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PlatFormService platFormService;
	
	@Autowired
	private ServerService serverService;
	
	/**
	 *  用户管理首页
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
		Long userId = getCurrentUserId();
		logger.info("userId"+userId+"运营大区管理首页");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<ServerZone> serverZones = serverZoneService.findServerZonesByCondition(userId,searchParams, pageNumber, pageSize, sortType);
		model.addAttribute("serverZones", serverZones);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/serverZone/index";
	}
	
	/**
	 * 新增操作员页	
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addServerZone(){
		return "/serverZone/add";
	}
	
	/**
	 * @return
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String saveServerZone(ServerZone serverZone,RedirectAttributes redirectAttributes){
		serverZoneService.save(serverZone);
		redirectAttributes.addFlashAttribute("message", "新增运营大区成功");
		return "redirect:/manage/serverZone/index";
	}
	
	/**
	 * 操作员编辑页
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id")long id,Model model){
		ServerZone serverZone = serverZoneService.findById(id);
		model.addAttribute("serverZone", serverZone);
		model.addAttribute("id", id);
		return "/serverZone/edit";
	}
	
	/**
	 * 操作员更新页
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String updateServerZone(ServerZone serverZone,RedirectAttributes redirectAttributes){
		serverZoneService.update(serverZone);
		redirectAttributes.addFlashAttribute("message", "修改运营大区成功");
	    return "redirect:/manage/serverZone/index";
	}
	
	/**
	 * @param id 用户id
	 */
	@RequestMapping(value = "detail", method = RequestMethod.GET)
	public String show(@RequestParam(value = "id")long id,Model model){
		ServerZone serverZone = serverZoneService.findById(id);
		model.addAttribute("serverZone", serverZone);
		return "/serverZone/info";
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
		 serverZoneService.delById(id);
		 //删除大区下的渠道
		 platFormService.delByServerZoneId(id.toString());
		 //删除大区下的服务器
		 serverService.deleteByServerZoneId(id.toString());
		 
		 List<User> users = accountService.getAllUser();
		 for (User user : users) {
			 List<String> list = user.getServerZoneList();
			 List<String> lis = new ArrayList<String>();
			 for (String string : list) {
				 if(!string.equals(id.toString())){
					 System.out.println(string);
					 lis.add(string); 
				 }
			 }
			 user.setServerZone(StringUtils.join(lis,","));
			 userService.update(user);
		}
		 map.put("success", "true");
		 return map;
	}	 
	
	/**
	 * Ajax请求校验name是否唯一。
	 */
	@RequestMapping(value = "/checkName")
	@ResponseBody
	public String checkName(@RequestParam("name") String name) {
		if (serverZoneService.findByServerName(name) == null) {
			return "true";
		} else {
			return "false";
		}
	}
	
	/**
	 * Ajax请求校验ID是否唯一。
	 */
	@RequestMapping(value = "/checkId")
	@ResponseBody
	public String checkId(@RequestParam("id") String id) {
		if (serverZoneService.findById(Long.valueOf(id))== null) {
			return "true";
		} else {
			return "false";
		}
	}
	
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	public Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
}
