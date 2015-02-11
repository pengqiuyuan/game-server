package com.enlight.game.service.serverZone;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.print.attribute.standard.Severity;
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

import com.enlight.game.entity.ServerZone;
import com.enlight.game.entity.User;
import com.enlight.game.repository.ServerZoneDao;
import com.enlight.game.service.account.AccountService;

@Component
@Transactional
public class ServerZoneService {
	
	@Autowired
	private ServerZoneDao serverZoneDao;
	
	@Autowired
	private AccountService accountService;
	
	public List<ServerZone> findAll(){
		return serverZoneDao.findAll();
	}
	
	public ServerZone findById(Long id){
		return serverZoneDao.findOne(id);
	}
	
	public void delById(Long id){
		serverZoneDao.delete(id);
	}
	
	public ServerZone findByServerName(String serverName){
		return serverZoneDao.findByServerName(serverName);
	}

	
	public void save(ServerZone serverZone){
		serverZone.setCrDate(new Date());
		serverZone.setStatus(ServerZone.STATUS_VALIDE);
		serverZoneDao.save(serverZone);
	}
	
	public void update(ServerZone serverZone){
		ServerZone zone = serverZoneDao.findOne(serverZone.getId());
		zone.setUpdDate(new Date());
		zone.setServerName(serverZone.getServerName());
		serverZoneDao.save(zone);
	}
	
	public Page<ServerZone> findServerZonesByCondition(Long userId,
			Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,sortType);
		Specification<ServerZone> spec = buildSpecification(userId, searchParams);
		return serverZoneDao.findAll(spec, pageRequest);
	}
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize,
			String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.ASC, "id");
		} else if ("createDate".equals(sortType)) {
			sort = new Sort(Direction.DESC, "createDate");
		}
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	
	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<ServerZone> buildSpecification(Long userId,
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		User user = accountService.getUser(userId);
		if (!user.getRoles().equals(User.USER_ROLE_ADMIN)) {
			filters.put("status", new SearchFilter("status", Operator.EQ,ServerZone.STATUS_VALIDE));
		}
		filters.put("status", new SearchFilter("status", Operator.EQ,ServerZone.STATUS_VALIDE));
		Specification<ServerZone> spec = DynamicSpecifications.bySearchFilter(filters.values(), ServerZone.class);
		return spec;
	}
}
