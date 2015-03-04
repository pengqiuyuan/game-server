package com.enlight.game.web.controller.mgr;

import java.util.Date;
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

import com.enlight.game.entity.GiftProps;
import com.enlight.game.entity.Stores;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;
import com.enlight.game.service.giftProps.GiftPropsService;
import com.enlight.game.service.store.StoreService;
import com.google.common.collect.Maps;

@Controller("giftPropsController")
@RequestMapping("/manage/giftProps")
public class GiftPropsController extends BaseController{
	
	private static final String PAGE_SIZE = "20";
	
	private static final Logger logger = LoggerFactory.getLogger(GiftPropsController.class);
	
	private static Map<String,String> sortTypes = Maps.newLinkedHashMap();
	
	static{
		sortTypes.put("auto","自动");
		sortTypes.put("创建时间", "创建时间");
	}
	
	public static Map<String, String> getSortTypes() {
		return sortTypes;
	}
	
	public static void setSortTypes(Map<String, String> sortTypes) {
		GiftPropsController.sortTypes = sortTypes;
	}

	public Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
	
	@Autowired
	private GiftPropsService giftPropsService;
	
	@Autowired
	private StoreService storeService;
	
	@RequestMapping(value = "index",method=RequestMethod.GET)
	public String index(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto")String sortType, Model model,
			ServletRequest request){
		Long userId = getCurrentUserId();
		logger.info("userId"+userId+"礼品卡道具设置");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<GiftProps> giftProps = giftPropsService.findGiftPropsCondition(userId,searchParams, pageNumber, pageSize, sortType);
		model.addAttribute("giftProps", giftProps);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		List<Stores> stores = storeService.findList();
		model.addAttribute("stores", stores);
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/giftProps/index";
	}
	
	/**
	 * 新增页面
	 */
	@RequestMapping(value = "/add" ,method=RequestMethod.GET)
	public String add(Model model){
		List<Stores> stores = storeService.findList();
		model.addAttribute("stores", stores);
		return "/giftProps/add";
	}
	
	/**
	 * 新增
	 */
	@RequestMapping(value = "/save",method=RequestMethod.POST)
	public String save(ServletRequest request,RedirectAttributes redirectAttributes,Model model){
		String gameId = request.getParameter("gameId");
		String[] fieldIds = request.getParameterValues("fieldId");
		String[] fieldValues = request.getParameterValues("fieldValue");
		if(fieldIds != null && fieldIds.length>0 && fieldValues != null && fieldValues.length>0){
			for (int i = 0; i < fieldIds.length; i++) {
				GiftProps giftProp = new GiftProps();
				giftProp.setItemId(Long.valueOf(fieldIds[i]));
				giftProp.setItemName(fieldValues[i]);
				giftProp.setGameId(gameId);
				giftProp.setStatus(GiftProps.STATUS_VALIDE);
				giftProp.setCrDate(new Date());
				giftPropsService.save(giftProp);
			}
		}
		redirectAttributes.addFlashAttribute("message", "新增成功");
		return "redirect:/manage/giftProps/index";
	}
	
	
	/**
	 * 删除操作	 
	 * @param oid 用户id
	 * @throws Exception 
	 */
	@RequestMapping(value = "del", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String del(@RequestParam(value = "id")Long id) throws Exception{
		 giftPropsService.del(id);
		 return "redirect:/manage/giftProps/index";
	}

}
