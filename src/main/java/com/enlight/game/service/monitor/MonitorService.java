package com.enlight.game.service.monitor;

import java.util.List;
import java.util.Map;

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

import com.enlight.game.entity.Monitor;
import com.enlight.game.entity.PlatForm;
import com.enlight.game.entity.User;
import com.enlight.game.repository.MonitorDao;
import com.enlight.game.service.account.AccountService;

@Component
@Transactional
public class MonitorService {

	@Autowired
	private MonitorDao monitorDao;
	
	@Autowired
	private AccountService accountService;
	
	public List<Monitor> findAll(String storeId){
		return monitorDao.findByStoreId(storeId);
	}
	
	public Monitor findByMoId(Long moId){
		return monitorDao.findOne(moId);
	}
	
	public void delById(Long id){
		monitorDao.delete(id);
	}
	
	public void save(Monitor monitor){
		monitorDao.save(monitor);
	}
	
	public void update(Monitor monitor){
		Monitor mo = monitorDao.findOne(monitor.getId());
		mo.setMonitorKey(monitor.getMonitorKey());
		mo.setEql(monitor.getEql());
		mo.setMonitorValue(monitor.getMonitorValue());
		monitorDao.save(mo);
	}
	
	public Monitor findByStoreIdAndMonitorKeyAndEql(String storeId ,String monitorKey, String eql){
		return monitorDao.findByStoreIdAndMonitorKeyAndEql(storeId, monitorKey, eql);
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
	public Page<Monitor> findMonitorByCondition(Long userId,
			Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType);
		Specification<Monitor> spec = buildSpecification(userId, searchParams);
		return monitorDao.findAll(spec, pageRequest);
	}
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize,
			String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		}else if ("monitorKey".equals(sortType)) {
			sort = new Sort(Direction.DESC, "monitorKey");
		}else if ("storeId".equals(sortType)) {
			sort = new Sort(Direction.DESC, "storeId");
		}
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	
	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<Monitor> buildSpecification(Long userId,
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		User user = accountService.getUser(userId);
		if (!user.getRoles().equals(User.USER_ROLE_ADMIN)) {
			filters.put("storeId", new SearchFilter("storeId", Operator.EQ,user.getStoreId()));
		}
		Specification<Monitor> spec = DynamicSpecifications.bySearchFilter(filters.values(), Monitor.class);
		return spec;
	}
	
	
}
