package com.enlight.game.service.go;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.shiro.SecurityUtils;
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

import com.enlight.game.entity.User;
import com.enlight.game.entity.go.GoAllServer;
import com.enlight.game.repository.go.GoAllServerDao;
import com.enlight.game.service.account.AccountService;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;

@Component
@Transactional
public class GoAllServerService {
	
	@Autowired
	private GoAllServerDao goAllServerDao;
	
	@Autowired
	private AccountService accountService;
	
	public GoAllServer findById(Integer id){
		return goAllServerDao.findById(id);
	}
	
	public GoAllServer findByServerId(String serverId){
		return goAllServerDao.findByServerId(serverId);
	}
	
	public List<GoAllServer> findAllByStoreIdAndServerZoneId(Integer storeId, Integer serverZoneId){
		return goAllServerDao.findAllByStoreIdAndServerZoneId(storeId, serverZoneId);
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
	public Page<GoAllServer> findGoAllServerByCondition(Long userId,
			Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType);
		Specification<GoAllServer> spec = buildSpecification(userId, searchParams);
		return goAllServerDao.findAll(spec, pageRequest);
	}
	
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize,
			String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		}
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	
	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<GoAllServer> buildSpecification(Long userId,
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		User user = accountService.getUser(userId);
		ShiroUser u = getCurrentUser();
		if (!user.getRoles().equals(User.USER_ROLE_ADMIN)) {
			filters.put("storeId", new SearchFilter("storeId", Operator.EQ,u.getStoreId()));
		}
		Specification<GoAllServer> spec = DynamicSpecifications.bySearchFilter(filters.values(), GoAllServer.class);
		return spec;
	}
	
	public ShiroUser getCurrentUser() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user;
	}
	
}
