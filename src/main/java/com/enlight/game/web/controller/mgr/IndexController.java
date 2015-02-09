package com.enlight.game.web.controller.mgr;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.subject.WebSubject;
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
import com.enlight.game.entity.User;
import com.enlight.game.entity.UserRole;
import com.enlight.game.service.account.AccountService;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;
import com.enlight.game.service.store.StoreService;
import com.enlight.game.service.userRole.UserRoleService;
import com.enlight.game.util.EncryptUtils;


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
	public List<UserRole> findStores(@RequestParam(value="storeId") String storeId,@RequestParam(value="categoryId") String categoryId,ServletRequest request) throws AppBizException, UnknownHostException{
		ShiroUser u = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		User user = accountService.findUserByLoginName(u.name);
		if(!u.storeId.equals(storeId)){
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
			List<UserRole> userRoles = userRoleService.findByUserId(user.getId());
			List<UserRole> userRs= userRoleService.findByUserId(user.getId());
			if(!userRoles.isEmpty() && userRoles.size()!=0){
				for (UserRole userRole : userRoles) {
					if(userRole.getStoreId() == Long.parseLong(storeId)){
						userRs.remove(userRole);
					}else{
						userRole.setStoreName(storeService.findById(userRole.getStoreId()).getName());
					}
				}

			}
			return userRs;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}
}
