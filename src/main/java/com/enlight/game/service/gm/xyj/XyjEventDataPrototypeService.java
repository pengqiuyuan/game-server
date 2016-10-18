package com.enlight.game.service.gm.xyj;

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

import com.enlight.game.entity.User;
import com.enlight.game.entity.gm.xyj.EventDataPrototype;
import com.enlight.game.repository.gm.xyj.EventDataPrototypeDao;
import com.enlight.game.service.account.AccountService;

@Component
@Transactional
public class XyjEventDataPrototypeService {
	
	@Autowired
	private EventDataPrototypeDao eventDataPrototypeDao;
	
	@Autowired
	private AccountService accountService;
	
	public List<EventDataPrototype> findAllByEventId(Long eventId){
		return eventDataPrototypeDao.findAllByEventId(eventId);
	}
	
	public Page<EventDataPrototype> findEventPrototypesByCondition(Long userId,
			Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,sortType);
		Specification<EventDataPrototype> spec = buildSpecification(userId, searchParams);
		return eventDataPrototypeDao.findAll(spec, pageRequest);
	}
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize,
			String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.ASC, "id");
		}
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	
	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<EventDataPrototype> buildSpecification(Long userId,
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		User user = accountService.getUser(userId);
		filters.put("times",new SearchFilter("times", Operator.EQ, userId));
		Specification<EventDataPrototype> spec = DynamicSpecifications.bySearchFilter(filters.values(), EventDataPrototype.class);
		return spec;
	}
}
