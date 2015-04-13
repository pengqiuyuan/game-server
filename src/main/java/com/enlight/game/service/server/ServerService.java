package com.enlight.game.service.server;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.persistence.SearchFilter.Operator;

import com.enlight.game.entity.Server;
import com.enlight.game.entity.User;
import com.enlight.game.repository.ServerDao;
import com.enlight.game.service.account.AccountService;

@Component
@Transactional
public class ServerService {
	
	@Autowired
	private ServerDao serverDao;
	
	@Autowired
	private AccountService accountService;
	
	public Set<Server> findByStoreId(String storeId){
		return serverDao.findByStoreId(storeId);
	}
	
	public List<Server> findAll(){
		return serverDao.findAll();
	}
	
	public Server findById(Long id){
		return serverDao.findOne(id);
	}
	
	public Server findByServerId(String serverId){
		return serverDao.findByServerId(serverId);
	}
	
	public Set<Server> findByServerZoneIdAndStoreId(String serverZoneId,String gameId){
		return serverDao.findByServerZoneIdAndStoreId(serverZoneId,gameId);
	}
	
	public void save(Server server){
		server.setCrDate(new Date());
		server.setUpdDate(new Date());
		server.setStatus(Server.STATUS_VALIDE);
		serverDao.save(server);
	}
	
	public void update(Server server){
		Server s =  serverDao.findOne(server.getId());
		s.setStoreId(server.getStoreId());
		s.setServerZoneId(server.getServerZoneId());
		s.setServerId(server.getServerId());
		s.setIp(server.getIp());
		s.setPort(server.getPort());
		s.setUpdDate(new Date());
		serverDao.save(s);
	}
	
	public void delById(Long id){
		serverDao.delete(id);
	}
	
	public void deleteByStoreId(String storeId){
		serverDao.deleteByStoreId(storeId);
	}
	public void deleteByServerZoneId(String serverZoneId){
		serverDao.deleteByServerZoneId(serverZoneId);
	}
	
	public List<String> findServerId(String storeId){
		return serverDao.findServerId(storeId);
	}
	
	/**
	 * 分页查询
	 * 
	 * @param userId
	 * @param searchParams
	 * @param pageNumber
	 * @param pageSize
	 * @param sortType
	 * @return
	 */
	public Page<Server> findServerByCondition(Long userId,
			Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType);
		Specification<Server> spec = buildSpecification(userId, searchParams);
		return serverDao.findAll(spec, pageRequest);
	}
	
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize,
			String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "updDate");
		} else if ("id".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		}else if ("storeId".equals(sortType)) {
			sort = new Sort(Direction.DESC, "storeId");
		}else if ("serverZoneId".equals(sortType)) {
			sort = new Sort(Direction.DESC, "serverZoneId");
		}
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	
	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<Server> buildSpecification(Long userId,
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		User user = accountService.getUser(userId);
		//ShiroUser u = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (!user.getRoles().equals(User.USER_ROLE_ADMIN)) {
			filters.put("status", new SearchFilter("status", Operator.EQ,Server.STATUS_VALIDE));
		}
		filters.put("status", new SearchFilter("status", Operator.EQ,Server.STATUS_VALIDE));
		Specification<Server> spec = DynamicSpecifications.bySearchFilter(filters.values(), Server.class);
		return spec;
	}
	
}
