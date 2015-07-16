package com.enlight.game.service.enumFunction;

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

import com.enlight.game.entity.EnumFunction;
import com.enlight.game.entity.Server;
import com.enlight.game.entity.User;
import com.enlight.game.repository.EnumFunctionDao;
import com.enlight.game.service.account.AccountService;

@Component
@Transactional 
public class EnumFunctionService {
	
	@Autowired
	private EnumFunctionDao enumFunctionDao;
	
	@Autowired
	private AccountService accountService;
	
	public EnumFunction findById(long id){
		return enumFunctionDao.findOne(id);
	}
	
	public List<EnumFunction> findAll(){
		return enumFunctionDao.findList();
	}
	
	public EnumFunction findByEnumRole(String enumRole){
		return enumFunctionDao.findByEnumRole(enumRole);
	}
	
	public Set<EnumFunction> findByCategoryId(String categoryId){
		return enumFunctionDao.findByCategoryId(categoryId);
	}
	
	public Set<EnumFunction> findByGameId(String gameId){
		return enumFunctionDao.findByGameId(gameId);
	}
	
	public void save(EnumFunction enumFunction){
		enumFunction.setCrDate(new Date());
		enumFunction.setUpdDate(new Date());
		enumFunction.setStatus(EnumFunction.STATUS_VALIDE);
		enumFunctionDao.save(enumFunction);
	}
	
	public void update(EnumFunction enumFunction){
		EnumFunction s =  enumFunctionDao.findOne(enumFunction.getId());
		s.setCategoryId(enumFunction.getCategoryId());
		s.setEnumName(enumFunction.getEnumName());
		s.setEnumRole(enumFunction.getEnumRole());
		s.setGameId(enumFunction.getGameId());
		s.setUpdDate(new Date());
		enumFunctionDao.save(s);
	}
	
	public void delById(Long id){
		enumFunctionDao.delete(id);
	}
	
	public Page<EnumFunction> findEnumFunctionsByCondition(Long userId,
			Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,sortType);
		Specification<EnumFunction> spec = buildSpecification(userId, searchParams);
		return enumFunctionDao.findAll(spec, pageRequest);
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
	private Specification<EnumFunction> buildSpecification(Long userId,
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		User user = accountService.getUser(userId);
		if (!user.getRoles().equals(User.USER_ROLE_ADMIN)) {
			filters.put("status", new SearchFilter("status", Operator.EQ,EnumFunction.STATUS_VALIDE));
		}
		filters.put("status", new SearchFilter("status", Operator.EQ,EnumFunction.STATUS_VALIDE));
		Specification<EnumFunction> spec = DynamicSpecifications.bySearchFilter(filters.values(), EnumFunction.class);
		return spec;
	}
}

