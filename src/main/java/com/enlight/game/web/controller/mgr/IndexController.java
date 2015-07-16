package com.enlight.game.web.controller.mgr;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.enlight.game.base.AppBizException;
import com.enlight.game.entity.EnumCategory;
import com.enlight.game.entity.EnumFunction;
import com.enlight.game.entity.User;
import com.enlight.game.entity.UserRole;
import com.enlight.game.service.account.AccountService;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;
import com.enlight.game.service.enumCategory.EnumCategoryService;
import com.enlight.game.service.enumFunction.EnumFunctionService;
import com.enlight.game.service.store.StoreService;
import com.enlight.game.service.userRole.UserRoleService;


/**
 * 
 * @author hyl
 * 登入后首页跳转控制
 *
 */
@Controller("indexController")
@RequestMapping("/manage/")
public class IndexController {
	
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private UserRoleService userRoleService;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private EnumCategoryService enumCategoryService;
	
	@Autowired
	private EnumFunctionService enumFunctionService;
	
     /**
      * 首页跳转
      * @return
      */
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index()
	{
		return "index";
	}

	@RequestMapping(value="/findStores",method=RequestMethod.GET)	
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<UserRole> findStores(@RequestParam(value="storeId") String storeId,@RequestParam(value="categoryId") String categoryId,@RequestParam(value="sta") String sta,ServletRequest request) throws AppBizException, UnknownHostException{
		ShiroUser u = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(sta.equals("0")){
			logger.debug("当前用户切换项目改变时执行....");
			SecurityUtils.getSubject().logout();
			request.setAttribute("categoryId",categoryId);
			request.setAttribute("storeId",storeId);
			request.setAttribute("password",u.passw);
			UsernamePasswordToken token = new UsernamePasswordToken(u.getName(), u.passw);  
			token.setRememberMe(false);
			token.setHost(InetAddress.getLocalHost().getHostAddress());
			SecurityUtils.getSubject().login(token); // 登录  
		}
		try {
			List<UserRole> userRoles = userRoleService.findByUserId(u.id);
			if(!userRoles.isEmpty() && userRoles.size()!=0){
				for (UserRole userRole : userRoles) {
					userRole.setStoreName(storeService.findById(userRole.getStoreId()).getName());
				}
			}
			return userRoles;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	
	@RequestMapping(value="/findCategorys",method=RequestMethod.GET)	
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Set<EnumCategory> findCategorys(@RequestParam(value="storeId") String storeId) throws AppBizException{
		Set<EnumCategory> enumCategories = new HashSet<EnumCategory>();
		try {
			ShiroUser u = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			List<UserRole> userRoles = userRoleService.findByUserIdAndStoreId(u.id, Long.valueOf(storeId));
			if(!userRoles.isEmpty() && userRoles.size()!=0){
				List<String> list = userRoles.get(0).getRoleList();
				for (String f : list) {
					EnumFunction enumFunction  = enumFunctionService.findByEnumRole(f);
					EnumCategory enumCategory = enumCategoryService.find(Long.valueOf(enumFunction.getCategoryId()));
					enumCategories.add(enumCategory);
				}
			}
		} catch (Exception e) {
			return null;
		}
		return enumCategories;
	}
}
