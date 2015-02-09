/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.enlight.game.web.account;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.enlight.game.entity.EnumCategory;
import com.enlight.game.entity.EnumFunction;
import com.enlight.game.entity.User;
import com.enlight.game.entity.UserRole;
import com.enlight.game.service.account.AccountService;
import com.enlight.game.service.enumCategory.EnumCategoryService;
import com.enlight.game.service.enumFunction.EnumFunctionService;
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
	
	@Autowired
	private EnumCategoryService enumCategoryService;
	
	@Autowired
	private EnumFunctionService enumFunctionService;
	
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
	public Map<String, Object> findStores(@RequestParam(value="loginName") String loginName) throws AppBizException{
		User user = accountService.findUserByLoginName(loginName);
		//默认选择第一个gameid的权限组
		Map<String, Object> map = new HashMap<String, Object>();
		if(user == null){
			//用户不存在
			map.put("enumCategories", null); 
			map.put("storeId", null);
		}else{
			try {
				List<UserRole> userRoles = userRoleService.findByUserId(user.getId());
				Set<EnumCategory> enumCategories = new HashSet<EnumCategory>();
				if(!userRoles.isEmpty() && userRoles.size()!=0){
					List<String> list = userRoles.get(0).getRoleList();
					for (String f : list) {
						EnumFunction enumFunction  = enumFunctionService.findByEnumRole(Integer.valueOf(f));
						EnumCategory enumCategory = enumCategoryService.find((long)enumFunction.getCategoryId());
						enumCategories.add(enumCategory);
					}
					map.put("enumCategories", enumCategories);
					map.put("storeId", userRoles.get(0).getStoreId());//登陆默认取第一个项目的权限组
				}else{
					map.put("enumCategories", null); //admin用户
					map.put("storeId", null);
				}
				return map;
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}
		}
		return map;


	}

}
