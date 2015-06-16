package com.enlight.game.web.controller.mgr;

import java.util.ArrayList;
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
import com.enlight.game.entity.Tag;
import com.enlight.game.entity.User;
import com.enlight.game.service.account.AccountService;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;
import com.enlight.game.service.giftProps.GiftPropsService;
import com.enlight.game.service.store.StoreService;
import com.enlight.game.service.tag.TagService;
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
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private AccountService accountService;
	
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

		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		if (!u.getRoles().equals(User.USER_ROLE_ADMIN)) {
			List<Stores> stores = new ArrayList<Stores>();
			Stores sto=  storeService.findById(Long.valueOf(user.getStoreId()));
			stores.add(sto);
			model.addAttribute("stores", stores);
		}else{
			List<Stores> stores =  storeService.findList();
			model.addAttribute("stores", stores);
		}
		
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/giftProps/index";
	}
	
	/**
	 * 新增页面
	 */
	@RequestMapping(value = "/add" ,method=RequestMethod.GET)
	public String add(Model model){
//		List<Tag> tags = tagService.findByCategory(Tag.CATEGORY_ITEM);
//		if(tags.size() == 0||tags == null){
//			model.addAttribute("message","道具表为空,请先导入道具Excel");
//		}

//		model.addAttribute("tagsSize", tags.size());
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		if (!u.getRoles().equals(User.USER_ROLE_ADMIN)) {
			List<Stores> stores = new ArrayList<Stores>();
			Stores sto=  storeService.findById(Long.valueOf(user.getStoreId()));
			stores.add(sto);
			model.addAttribute("stores", stores);
		}else{
			List<Stores> stores =  storeService.findList();
			model.addAttribute("stores", stores);
		}
		return "/giftProps/add";
	}
	
	/**
	 * 新增
	 */
	@RequestMapping(value = "/save",method=RequestMethod.POST)
	public String save(ServletRequest request,RedirectAttributes redirectAttributes,Model model){
		String gameId = request.getParameter("gameId");
		String[] fieldValues = request.getParameterValues("fieldValue");
		if(fieldValues != null && fieldValues.length>0){
			for (int i = 0; i < fieldValues.length; i++) {
				GiftProps giftProp = new GiftProps();
				giftProp.setItemId(Long.valueOf(fieldValues[i].split(":")[0]));
				giftProp.setItemName(fieldValues[i].split(":")[1].trim());
				giftProp.setGameId(gameId);
				giftProp.setStatus(GiftProps.STATUS_VALIDE);
				giftProp.setCrDate(new Date());
				GiftProps g =  giftPropsService.findByItemIdAndItemNameAndGameId(giftProp.getItemId(), giftProp.getItemName(), giftProp.getGameId());
				if(g==null){
					giftPropsService.save(giftProp);
				}
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
	public Map<String, String> del(@RequestParam(value = "id")Long id) throws Exception{
		 giftPropsService.del(id);
		 return SUCCESS_RESULT;
	}
	
	@RequestMapping(value = "/checkTagId")
	@ResponseBody
	public String checkLoginName(@RequestParam("fieldValue") String fieldValue
			,@RequestParam(value="gameId")String gameId) {
		List<Tag> tagsName = new ArrayList<Tag>();
		List<Tag> tags = tagService.findByTagIdAndCategoryAndStoreId(Long.valueOf(fieldValue.split(":")[0]),Tag.CATEGORY_ITEM,gameId);
		if(fieldValue.split(":").length>1){
			tagsName = tagService.findByTagNameAndCategoryAndStoreId(fieldValue.split(":")[1].trim(),Tag.CATEGORY_ITEM,gameId);
		}
		if (tags.size()!=0 && tagsName.size()!=0) {
			return "true";
		} else {
			return "false";
		}
	}
	
	/**
	 * Ajax请求校验项目是否有道具
	 */
	@RequestMapping(value = "/checkTagByGameId")
	@ResponseBody
	public String checkTagByGameId(@RequestParam(value="gameId")String gameId) {
		List<Tag> tags = tagService.findByCategoryAndStoreId(Tag.CATEGORY_ITEM, gameId);
		return tags.size()+"";

	}
	
	public ShiroUser getCurrentUser() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user;
	}

}
