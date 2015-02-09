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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.enlight.game.entity.Log;
import com.enlight.game.entity.Stores;
import com.enlight.game.entity.User;
import com.enlight.game.entity.UserRole;
import com.enlight.game.service.account.AccountService;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;
import com.enlight.game.service.log.LogService;
import com.enlight.game.service.roleFunction.RoleFunctionService;
import com.enlight.game.service.store.StoreService;
import com.enlight.game.service.user.UserService;
import com.enlight.game.service.userRole.UserRoleService;
import com.enlight.game.util.ImageUploadService;
import com.google.common.collect.Maps;


/**
 * 门户管理的controller
 *
 */
@Controller("storesController")
@RequestMapping(value="/manage/store")
public class StoresController extends BaseController{

	private static final Logger logger = LoggerFactory.getLogger(StoresController.class);
	
	private static final String PAGE_SIZE = "15";
	

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();

	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("createDate", "时间");
	}
	
	public static Map<String, String> getSortTypes() {
		return sortTypes;
	}

	public static void setSortTypes(Map<String, String> sortTypes) {
		StoresController.sortTypes = sortTypes;
	}

	
	@Override
	@InitBinder
	protected void initBinder(ServletRequestDataBinder binder){
		binder.registerCustomEditor(Date.class,"createDate",new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
		binder.registerCustomEditor(Date.class,"upDate",new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
	}
	
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private ImageUploadService imageService;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRoleService userRoleService;
	
	@Autowired
	private RoleFunctionService roleFunctionService;
	
	/**
	 *  门店管理首页

	 */
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto")String sortType, Model model,
			ServletRequest request){
		Long storeId = getCurrentStoresId();
		logger.info("storeId"+storeId+"用户管理首页");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<Stores> stores = storeService.findStoresByCondition(storeId,searchParams, pageNumber, pageSize, sortType);
		model.addAttribute("stores", stores);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/store/index";
	}
	
	
	/**
	 * 操作员编辑页	 * @param oid 用户ID
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id")long id,Model model){
		Stores store = storeService.findById(id);
		model.addAttribute("store", store);
		model.addAttribute("id", id);
		return "/store/edit";
	}
	
	/**
	 * 操作员更新页
	 * 	 * @param store 用户
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String updateStores(Stores store,RedirectAttributes redirectAttributes){
	    storeService.update(store);
		redirectAttributes.addFlashAttribute("message", "修改项目成功");
		String message = "修改:" +store.toString();
		logService.log(getCurrentUserName(), message, Log.TYPE_STORE);
	    return "redirect:/manage/store/index";
	}
	
	/**
	 * 新增操作员页	 * @param uid 租户ID
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addStores(){
		
		return "/store/add";
	}
	
	/**
	 * 新增操作	 * @param Storestest 用户
	 * @return
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String saveStores(Stores store,RedirectAttributes redirectAttributes){
		storeService.save(store);
		redirectAttributes.addFlashAttribute("message", "新增项目成功");
		String message = "新增:" +store.toString();
		logService.log(getCurrentUserName(), message, Log.TYPE_STORE);
		return "redirect:/manage/store/index";
	}
	
	/**
	 * 删除操作	 
	 * @param oid 用户id
	 * @throws Exception 
	 */
	@RequestMapping(value = "del", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> delStores(@RequestParam(value = "id")long id) throws Exception{
		 Map<String,Object> map = new HashMap<String, Object>();
		if(id == 0)
		{
			throw new Exception("不能删除主项目！");
		}
		Stores store = storeService.findById(id);
		storeService.del(store);
		//功能表
		roleFunctionService.delByStoreId(id);
		
		//用户表
		List<UserRole> userRoles = userRoleService.findByStoreId(id);
		for (UserRole userRole : userRoles) {
			User user =userService.findById(userRole.getUserId());
			List<String> stIds = user.getStoreIds();
			List<String> storeIds = new ArrayList<String>(stIds);
			for (String s : stIds) {
				if(Long.parseLong(s) == id){
					storeIds.remove(s);
				}
			}
			user.setStoreId(StringUtils.join(storeIds,","));
			userService.update(user);
		}
		//权限
		userRoleService.delByStoreId(id);
		
		
		
		String message = "删除:" +store.getName();
		logService.log(getCurrentUserName(), message, Log.TYPE_STORE);
		map.put("success", "true");
		return map;
	}
	
	/**
	 * 门店详细
	 * @param id 用户id
	 */
	@RequestMapping(value = "detail", method = RequestMethod.GET)
	public String show(@RequestParam(value = "id")long id,Model model){
		
		Stores store = storeService.findById(id);
		model.addAttribute("store", store);
		return "/store/info";
	}
	
	
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	public Long getCurrentStoresId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
	
	
	
	public String getCurrentUserName() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.name;
	}
	
	
}
