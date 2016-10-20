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
import com.enlight.game.entity.gm.xyj.EventDataPrototypeInstruction;
import com.enlight.game.repository.gm.xyj.EventDataPrototypeInstructionDao;
import com.enlight.game.service.account.AccountService;

@Component
@Transactional
public class XyjEventDataPrototypeInstructionService {
	
	@Autowired
	private EventDataPrototypeInstructionDao eventDataPrototypeInstructionDao;
	
	@Autowired
	private AccountService accountService;
	
	public List<EventDataPrototypeInstruction> findAll(){
		return eventDataPrototypeInstructionDao.findAllCached();
	}
	
	public void save(EventDataPrototypeInstruction eventDataPrototypeInstruction){
		eventDataPrototypeInstructionDao.save(eventDataPrototypeInstruction);
	}
	
	public EventDataPrototypeInstruction saveReturnEventDataInstr(EventDataPrototypeInstruction eventDataPrototypeInstruction){
		return  eventDataPrototypeInstructionDao.save(eventDataPrototypeInstruction);
	}
	
	public EventDataPrototypeInstruction findById(Long id){
		return eventDataPrototypeInstructionDao.findOne(id);
	}
	
	public Page<EventDataPrototypeInstruction> findEventPrototypesByCondition(Long userId,
			Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,sortType);
		Specification<EventDataPrototypeInstruction> spec = buildSpecification(userId, searchParams);
		return eventDataPrototypeInstructionDao.findAll(spec, pageRequest);
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
	private Specification<EventDataPrototypeInstruction> buildSpecification(Long userId,
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		User user = accountService.getUser(userId);
		Specification<EventDataPrototypeInstruction> spec = DynamicSpecifications.bySearchFilter(filters.values(), EventDataPrototypeInstruction.class);
		return spec;
	}
}
