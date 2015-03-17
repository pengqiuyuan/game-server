package com.enlight.game.web.controller.mgr;

import java.util.HashMap;
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

import com.enlight.game.entity.Server;
import com.enlight.game.entity.ServerZone;
import com.enlight.game.entity.Stores;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;
import com.enlight.game.service.server.ServerService;
import com.enlight.game.service.serverZone.ServerZoneService;
import com.enlight.game.service.store.StoreService;
import com.google.common.collect.Maps;

@Controller("serverController")
@RequestMapping("/manage/server")
public class ServerController extends BaseController{

	private static final String PAGE_SIZE = "50";
	
	private static final Logger logger = LoggerFactory.getLogger(ServerController.class);
	
	private static Map<String,String> sortTypes = Maps.newLinkedHashMap();
	
	static{
		sortTypes.put("auto","自动");
		sortTypes.put("id", "Id");
		sortTypes.put("storeId", "游戏项目");
		sortTypes.put("serverZoneId", "运营大区");
	}
	
	public static Map<String, String> getSortTypes() {
		return sortTypes;
	}
	
	public static void setSortTypes(Map<String, String> sortTypes) {
		ServerController.sortTypes = sortTypes;
	}
	
	@Autowired
	private ServerService serverService;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private ServerZoneService serverZoneService;
	
	@RequestMapping(value = "index",method=RequestMethod.GET)
	public String index(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto")String sortType, Model model,
			ServletRequest request){
		Long userId = getCurrentUserId();
		logger.info("userId"+userId+"服务器信息设置");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<Server> servers = serverService.findServerByCondition(userId,searchParams, pageNumber, pageSize, sortType);
		for (Server server : servers) {
			Stores stores = storeService.findById(Long.valueOf(server.getStoreId()));
			ServerZone serverZone = serverZoneService.findById(Long.valueOf(server.getServerZoneId()));
			server.setStores(stores);
			server.setServerZone(serverZone);
		}
		model.addAttribute("servers", servers);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		List<Stores> stores = storeService.findList();
		List<ServerZone> serverZones = serverZoneService.findAll();
		model.addAttribute("stores", stores);
		model.addAttribute("serverZones", serverZones);
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/server/index";
	}
	
	/**
	 * 新增页面
	 */
	@RequestMapping(value = "/add" ,method=RequestMethod.GET)
	public String add(Model model){
		List<Stores> stores = storeService.findList();
		//List<EnumFunction> enumFunctions = enumFunctionService.findAll();
		List<ServerZone> serverZones = serverZoneService.findAll();
		model.addAttribute("stores", stores);
		model.addAttribute("serverZones", serverZones);
		return "/server/add";
	}
	
	/**
	 * 新增
	 */
	@RequestMapping(value = "/save",method=RequestMethod.POST)
	public String save(Server server,ServletRequest request,RedirectAttributes redirectAttributes,Model model){
		serverService.save(server);
		redirectAttributes.addFlashAttribute("message", "新增成功");
		return "redirect:/manage/server/index";
	}
	
	
	@RequestMapping(value="edit",method=RequestMethod.GET)
	public String edit(@RequestParam(value="id")long id,Model model){
		Server st = serverService.findById(id);
		List<Stores> stores = storeService.findList();
		List<ServerZone> serverZones = serverZoneService.findAll();
		model.addAttribute("stores", stores);
		model.addAttribute("serverZones", serverZones);
		model.addAttribute("st", st);
		return "/server/edit";
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(value = "/update",method=RequestMethod.POST)
	public String update(Server server,ServletRequest request,RedirectAttributes redirectAttributes){
		serverService.update(server);
		redirectAttributes.addFlashAttribute("message", "更新成功");
		return "redirect:/manage/server/index";
	}
	
	/**
	 * 删除操作	 
	 * @param oid 用户id
	 * @throws Exception 
	 */
	@RequestMapping(value = "del", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> delServerZone(@RequestParam(value = "id")Long id) throws Exception{
		 Map<String,Object> map = new HashMap<String, Object>();
		 serverService.delById(id);
		 map.put("success", "true");
		 return map;
	}
	
	/**
	 * 权限详细
	 * @param id 用户id
	 */
	@RequestMapping(value = "detail", method = RequestMethod.GET)
	public String show(@RequestParam(value = "id")long id,Model model){
		Server server = serverService.findById(id);
		Stores stores = storeService.findById(Long.valueOf(server.getStoreId()));
		ServerZone serverZone = serverZoneService.findById(Long.valueOf(server.getServerZoneId()));
		server.setStores(stores);
		server.setServerZone(serverZone);
		model.addAttribute("server", server);
		return "/server/info";
	}
	
	@RequestMapping(value = "/checkServerId")
	@ResponseBody
	public String checkServerId(@RequestParam("serverId") String serverId) {
		if (serverService.findByServerId(serverId) == null) {
			return "true";
		} else {
			return "false";
		}
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	public Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
}
