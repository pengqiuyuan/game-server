package com.enlight.game.web.controller.mgr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.enlight.game.base.AppBizException;
import com.enlight.game.entity.Gift;
import com.enlight.game.entity.GiftItem;
import com.enlight.game.entity.GiftServer;
import com.enlight.game.entity.Server;
import com.enlight.game.entity.ServerZone;
import com.enlight.game.entity.Stores;
import com.enlight.game.entity.User;
import com.enlight.game.service.account.AccountService;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;
import com.enlight.game.service.server.ServerService;
import com.enlight.game.service.serverZone.ServerZoneService;
import com.enlight.game.service.store.StoreService;

@Controller("giftController")
@RequestMapping("/manage/gift")
public class GiftController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(GiftController.class);
	
	@Autowired
	private ServerZoneService serverZoneService;
	
	@Autowired
	private ServerService serverService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private StoreService storeService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET , produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String,Object> index(
			@RequestParam(value = "pic", required = false) String booleanPic,
			@RequestParam(value = "first",defaultValue="1", required = false) Integer pageNum,
			@RequestParam(value = "max",defaultValue="10", required = false) Integer pageSize){
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		if (!u.getRoles().equals(User.USER_ROLE_ADMIN)) {
			String category = Gift.CATEGORY_ADMIN;
			String gameId = null;
		}else{
			String category = Gift.CATEGORY_ORDINART;
			String gameId = user.getStoreId();
		}
		
		return map;
	}
	
	/**
	 * 新增礼品卡
	 * @return
	 */
	@RequestMapping(value = "/add" ,method=RequestMethod.GET)
	public String add(Model model){
		List<ServerZone> serverZones = serverZoneService.findAll();
		List<Stores> stores =  storeService.findList();
		logger.debug("新增礼品卡..");
		model.addAttribute("serverZones", serverZones);
		model.addAttribute("stores", stores);
		return "/gift/add";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST , produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String,Object> save(Gift gift,ServletRequest request,RedirectAttributes redirectAttributes,Model model){
		//platFormService.save(platForm);
		//redirectAttributes.addFlashAttribute("message", "新增成功");
		//return "redirect:/manage/platForm/index";
		Map<String,Object> map = new HashMap<String, Object>();
		ShiroUser user = getCurrentUser();
		User u = accountService.getUser(user.id);
		
		List<GiftServer> giftServers = new ArrayList<GiftServer>();
		String[] servers = request.getParameterValues("server");
		if(servers != null && servers.length>0){
			for (String string : servers) {
				GiftServer giftServer = new GiftServer();
				giftServer.setId(string);
				giftServers.add(giftServer);
			}
		}

		String[] playerIds = request.getParameterValues("playerId");
		
		String[] fieldIds = request.getParameterValues("fieldId");
		String[] fieldValues = request.getParameterValues("fieldValue");
		Set<GiftItem> giftItems = new HashSet<GiftItem>();
		if(fieldIds != null && fieldIds.length>0 && fieldValues != null && fieldValues.length>0){
			for (int i = 0; i < fieldIds.length; i++) {
				GiftItem giftItem = new GiftItem();
				giftItem.setId(Long.valueOf(fieldIds[i]));
				giftItem.setNumber(Integer.valueOf(fieldValues[i]));
				giftItems.add(giftItem);
			}
		}
		
		gift.setUserId(user.id);
		if (!u.getRoles().equals(User.USER_ROLE_ADMIN)) {
			gift.setGameId(Long.valueOf(user.getStoreId()));
		}
		gift.setGiftServers(giftServers);
		gift.setPlayerId(Arrays.asList(playerIds));
		gift.setStatus(Gift.STATUS_CHECKING);
		gift.setGiftItems(giftItems);
		
        map.put("gift", gift);
		return map;
	}
	
	@RequestMapping(value="/findServers",method=RequestMethod.GET)	
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Set<Server> findServers(@RequestParam(value="serverZoneId") String serverZoneId) throws AppBizException{
		Set<Server> servers = serverService.findByServerZoneId(serverZoneId);
		return servers;
	}
	
	public ShiroUser getCurrentUser() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user;
	}
	
}
