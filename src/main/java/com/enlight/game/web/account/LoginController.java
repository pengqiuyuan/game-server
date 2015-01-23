/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.enlight.game.web.account;

import java.util.List;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.enlight.game.base.AppBizException;
import com.enlight.game.entity.User;
import com.enlight.game.entity.UserRole;
import com.enlight.game.service.account.AccountService;
import com.enlight.game.service.store.StoreService;
import com.enlight.game.service.userRole.UserRoleService;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)，
 * 
 * 真正登录的POST请求由Filter完成,
 * 
 * @author calvin
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController {

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private UserRoleService userRoleService;
	
	@Autowired
	private StoreService storeService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String login() {
		return "account/login";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String fail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName, Model model) {
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, userName);
		return "account/login";
	}
	
	@RequestMapping(value="/findStores",method=RequestMethod.GET)	
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<UserRole> findStores(@RequestParam(value="loginName") String loginName) throws AppBizException{
		User user = accountService.findUserByLoginName(loginName);
		List<UserRole> userRoles = userRoleService.findByUserId(user.getId());
		if(!userRoles.isEmpty()){
			for (UserRole userRole : userRoles) {
				userRole.setStoreName(storeService.findById(userRole.getStoreId()).getName());
			}
		}
		return userRoles;
	}

}
