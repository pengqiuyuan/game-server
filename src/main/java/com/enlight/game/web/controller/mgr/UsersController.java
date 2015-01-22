package com.enlight.game.web.controller.mgr;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

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

import com.enlight.game.base.AppBizException;
import com.enlight.game.entity.Log;
import com.enlight.game.entity.RoleFunction;
import com.enlight.game.entity.User;
import com.enlight.game.entity.UserRole;
import com.enlight.game.service.account.AccountService;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;
import com.enlight.game.service.log.LogService;
import com.enlight.game.service.roleFunction.RoleFunctionService;
import com.enlight.game.service.serverZone.ServerZoneService;
import com.enlight.game.service.store.StoreService;
import com.enlight.game.service.user.UserService;
import com.enlight.game.service.userRole.UserRoleService;
import com.google.common.collect.Maps;


/**
 * 
 * 用户管理的controller
 *
 */
@Controller("usersController")
@RequestMapping(value="/manage/user")
public class UsersController extends BaseController{
	
	private static final String PAGE_SIZE = "15";

	private static final Logger logger = LoggerFactory.getLogger(UsersController.class);
	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();

	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("registerDate", "时间");
	}
	
	public static Map<String, String> getSortTypes() {
		return sortTypes;
	}

	public static void setSortTypes(Map<String, String> sortTypes) {
		UsersController.sortTypes = sortTypes;
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
	private LogService logService;
	
	@Autowired
	private ServerZoneService serverZoneService;
	
	@Autowired
	private RoleFunctionService roleFunctionService;
	
	@Autowired
	private UserRoleService userRoleService;
	
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
		logger.info("userId"+userId+"用户管理首页");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<User> users = userService.findTenanciesByCondition(userId,searchParams, pageNumber, pageSize, sortType);
		model.addAttribute("users", users);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/user/index";
	}
	
	
	/**
	 * 操作员编辑页	 
	 * @param oid 用户ID
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id")long id,Model model){
		User user = userService.findById(id);
		Long userId = getCurrentUserId();
		//model.addAttribute("stores",storeService.findListByUid(userId));
		List<UserRole> userRoles = userRoleService.findByUserId(id);
		model.addAttribute("userRoles", userRoles);
		if(userRoles.isEmpty()){
			model.addAttribute("serverZones",null);
		}else{
			model.addAttribute("serverZones",userRoles.get(0).getServerZoneList());
		}
		model.addAttribute("user", user);
		model.addAttribute("id", id);
		return "/user/edit";
	}
	
	/**
	 * 操作员更新页	 
	 * @param user 用户
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String updateUser(User user,RedirectAttributes redirectAttributes){
		userService.update(user);
		redirectAttributes.addFlashAttribute("message", "修改用户成功");
		String message = "修改:" +user.toString();
		logService.log(getCurrentUserName(), message, Log.TYPE_USER);
	    return "redirect:/manage/user/index";
	}
	
	/**
	 * 新增操作员页	
	 * @param uid 租户ID
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addUser(Model model){
		Long userId = getCurrentUserId();
		model.addAttribute("stores",storeService.findListByUid(userId));
		model.addAttribute("serverZones",serverZoneService.findAll());
		return "/user/add";
	}
	
	/**
	 * 新增操作	 
	 * @param Usertest 用户
	 * @return
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String saveUser(User user,ServletRequest request,Model model,RedirectAttributes redirectAttributes){
		String serverZones = "";
		for(String str0 : request.getParameterValues("serverName")){
			serverZones=serverZones+","+str0;
		}
		String password = request.getParameter("confirmPwdCipher");
		user.setPlainPassword(password);
		boolean flag = userService.isOnly(user.getLoginName());
		if(flag){
			accountService.register(user);
			if (request.getParameterValues("storeId") != null && request.getParameterValues("role") != null) {
				if (request.getParameterValues("storeId") != null && request.getParameterValues("role").length > 0) {
					for (int i = 0; i < request.getParameterValues("storeId").length; i++) {
						List<String> funcs = roleFunctionService.findByGameIdAndRoleFunctions(Long.parseLong(request.getParameterValues("storeId")[i]), request.getParameterValues("role")[i]);
						String functions = "";
						for (String string : funcs) {
							functions = functions + ","+string;
						}
						UserRole userRole = new UserRole();
						userRole.setCrDate(new Date());
						userRole.setUpDate(new Date());
						userRole.setStatus(UserRole.STATUS_VALIDE);
						userRole.setUserId(accountService.register(user).getId());
						userRole.setServerZone(serverZones);
						userRole.setFunctions(functions);
						userRole.setStoreId(Long.parseLong(request.getParameterValues("storeId")[i]));
						userRole.setRole(request.getParameterValues("role")[i]);
						userRoleService.save(userRole);
					}
				}
			}
			
			redirectAttributes.addFlashAttribute("message", "新增用户成功");
			String message = "添加:" +user.toString();
			logService.log(getCurrentUserName(), message, Log.TYPE_USER);
			return "redirect:/manage/user/index";
		}
		model.addAttribute("storeId", user.getStoreId());
		model.addAttribute("message", "用户名重复！");
	    return "/user/add"; 
	}

	/**
	 * 重置密码 
	 * @param oid 用户ID
	 * @return
	 */
	@RequestMapping(value = "resetPwd", method = RequestMethod.GET)
	public String resetPwd(@RequestParam(value = "id")long id,Model model){
		User user = userService.findById(id);
        model.addAttribute("user", user);
       
		return "/user/resetPwd";
	}
	
	/**
	 * 更新密码
	 * @param oid 用户id 
	 * @return
	 */
	@RequestMapping(value = "savePwd", method = RequestMethod.POST)
	public String savePwd(@RequestParam(value = "id")long id,ServletRequest request,RedirectAttributes redirectAttributes){
		User user = userService.findById(id);
		String password = request.getParameter("confirmPwdCipher");
		user.setPlainPassword(password);
		accountService.updateUser(user);
		String message = "重置密码:" +user.getName();
	    logService.log(getCurrentUserName(), message, Log.TYPE_USER);
		redirectAttributes.addFlashAttribute("message", "更新密码成功");
		return "redirect:/manage/user/index";
	}

	
	/**
	 * 冻结	 
	 * @param oid 用户id
	 */
	@RequestMapping(value = "del", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String,String> del(@RequestParam(value = "id")long id){
		User user = userService.findById(id);
		userService.del(user);
	    Map<String,String> map = new HashMap<String, String>();
		map.put("success", "true");
		String message = "冻结:" +user.getName();
	    logService.log(getCurrentUserName(), message, Log.TYPE_USER);
		return map;
	}
	
	/**
	 * 删除	 
	 * @param oid 用户id
	 */
	@RequestMapping(value = "delUser", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String,String> delUser(@RequestParam(value = "id")long id){
		User user = userService.findById(id);
		userService.realDel(user);
	    Map<String,String> map = new HashMap<String, String>();
		map.put("success", "true");
		String message = "删除:" +user.getName();
	    logService.log(getCurrentUserName(), message, Log.TYPE_USER);
		return map;
	}
	
	/**
	 * 操作	 
	 * @param oid 用户id
	 */
	@RequestMapping(value = "active", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String,String> lockUser(@RequestParam(value = "id")long id){
		
		User user = userService.findById(id);
		userService.active(user);
	    Map<String,String> map = new HashMap<String, String>();
		map.put("success", "true");
		String message = "激活:" +user.getName();
	    logService.log(getCurrentUserName(), message, Log.TYPE_USER);
		return map;
	}
	/**
	 * 用户详细
	 * @param id 用户id
	 */
	@RequestMapping(value = "detail", method = RequestMethod.GET)
	public String show(@RequestParam(value = "id")long id,Model model){
		
		User user = userService.findById(id);
		model.addAttribute("user", user);
		return "/user/info";
	}
	
	
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	public Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	public String getCurrentUserName() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.name;
	}
	
	/**
	 * 根据项目查找对应权限组
	 * @param 
	 * @return
	 * @throws AppBizException
	 */
	@RequestMapping(value="/findRoles",method=RequestMethod.GET)	
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<RoleFunction> findRoles(@RequestParam(value="gameId",required=true) Long gameId) throws AppBizException{
		System.out.println(roleFunctionService.findByGameId(gameId));
		return roleFunctionService.findByGameId(gameId);
	}
	
	/**
	 * 根据项目、权限组 查找对应功能
	 * @param
	 * @return
	 * @throws AppBizException
	 */
	@RequestMapping(value="/findFunctions",method=RequestMethod.GET)	
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<RoleFunction> findFunctions(
			@RequestParam(value="gameId") Long gameId,
			@RequestParam(value="role") String role) throws AppBizException{
		roleFunctionService.findByGameIdAndRole(gameId, role);
		return roleFunctionService.findByGameIdAndRole(gameId, role);
	}
	
	/**
	 * Ajax请求校验loginName是否唯一。
	 */
	@RequestMapping(value = "/checkLoginName")
	@ResponseBody
	public String checkLoginName(@RequestParam("loginName") String loginName) {
		if (accountService.findUserByLoginName(loginName) == null) {
			return "true";
		} else {
			return "false";
		}
	}
}
