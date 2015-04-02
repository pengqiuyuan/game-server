package com.enlight.game.web.controller.mgr;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.modules.web.Servlets;

import com.enlight.game.entity.Log;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;
import com.enlight.game.service.log.LogService;
import com.google.common.collect.Maps;

/**
 * 日志管理
 *
 */
@Controller("LogController")
@RequestMapping("/manage/log")
public class LogController extends BaseController{
private static final Logger logger = LoggerFactory.getLogger(LogController.class);
	
	private static final String PAGE_SIZE = "15";

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();

	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("crDate", "时间");
	}
	
	public static Map<String, String> getSortTypes() {
		return sortTypes;
	}

	public static void setSortTypes(Map<String, String> sortTypes) {
		LogController.sortTypes = sortTypes;
	}

	
	@Override
	@InitBinder
	protected void initBinder(ServletRequestDataBinder binder){
		binder.registerCustomEditor(Date.class,"crDate",new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
		binder.registerCustomEditor(Date.class,"upDate",new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
	}
	
	
	
	@Autowired
	private LogService logService;


	/**
	 * 日志管理首页
	 * @param pageNumber
	 * @param pageSize
	 * @param sortType
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto")String sortType, Model model,
			ServletRequest request){
		Long uid = getCurrentId();
		logger.info("uid"+uid+"日志管理首页");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<Log> logs = logService.findByCondition(uid,searchParams, pageNumber, pageSize, sortType);
		model.addAttribute("logs", logs);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/log/index";
	}
	
	
	/**
	 * 活动详细
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "detail", method = RequestMethod.GET)
	public String show(@RequestParam(value = "id")long id,Model model){
		
		Log log = logService.findById(id);
		model.addAttribute("log", log);
		return "/log/info";
	}	
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	public Long getCurrentId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
}
