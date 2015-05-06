package com.enlight.game.service.roleFunction;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.hibernate.id.IdentityGenerator.GetGeneratedKeysDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.persistence.SearchFilter.Operator;

import com.enlight.game.entity.RoleFunction;
import com.enlight.game.entity.User;
import com.enlight.game.repository.RoleFunctionDao;
import com.enlight.game.service.account.AccountService;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;
import com.enlight.game.service.enumFunction.EnumFunctionService;
import com.enlight.game.service.roleAndEnum.RoleAndEnumService;
import com.enlight.game.service.store.StoreService;

/**
 * 游戏权限分配Service
 * @author dell
 *
 */
@Component
@Transactional
public class RoleFunctionService {
	
	@Autowired
	private RoleFunctionDao roleFunctionDao;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private EnumFunctionService enumFunctionService;
	
	@Autowired
	private RoleAndEnumService roleAndEnumService;
	
	public RoleFunction findById(long id){
		return roleFunctionDao.findOne(id);
	}
	
	public List<RoleFunction> findByGameIdAndRole(Long gameId,String role){
		return roleFunctionDao.findByGameIdAndRole(gameId, role);
	}
	
	public List<String> findByGameIdAndRoleFunctions(Long gameId,String role){
		List<RoleFunction> roleFunctions =  roleFunctionDao.findByGameIdAndRole(gameId, role);
		List<String> i = roleAndEnumService.findByRole(roleFunctions.get(0).getId());
		return i;
	}
	
	public void delById(long id){
		roleFunctionDao.delete(id);
	}
	
	public List<RoleFunction> findByGameId(Long gameId){
		return roleFunctionDao.findByGameId(gameId);
	}
	
	public List<String> findByGameIdStr(Long gameId){
		return roleFunctionDao.findByGameIdStr(gameId);
	}
	
	public void delByStoreId(Long storeId){
		roleFunctionDao.delByStoreId(storeId);
	}
	
	public List<RoleFunction> findByGmId(Long gameId){
		return roleFunctionDao.findByGmId(gameId);
	}
	
	/**
	 * 根据项目索引+权限索引  判断权限组是否
	 * @param roleFunction
	 */
	public boolean isOnly(Long gameId,String role){
		List<RoleFunction> roleFunctions = roleFunctionDao.findByGameIdAndRole(gameId, role);
		return roleFunctions.isEmpty();
	}
	
	public void update(RoleFunction roleFunction){
		RoleFunction function = roleFunctionDao.findOne(roleFunction.getId());
		function.setStatus(roleFunction.getStatus());
		function.setUpdDate(new Date());
		function.setRole(roleFunction.getRole());
		function.setGameId(roleFunction.getGameId());
		//function.setFunction(roleFunction.getFunction());
		function.setGameName(storeService.findById(roleFunction.getGameId()).getName());
		//function.setFunctionName(enumFunctionService.findByEnumRole(roleFunction.getFunction()).getEnumName());
		roleFunctionDao.save(function);
	}
	
	public RoleFunction save(RoleFunction roleFunction){
		RoleFunction function = new RoleFunction();
		function.setStatus(roleFunction.getStatus());
		function.setCrDate(new Date());
		function.setUpdDate(new Date());
		function.setRole(roleFunction.getRole());
		function.setGameId(roleFunction.getGameId());
		//function.setFunction(roleFunction.getFunction());
		function.setGameName(storeService.findById(roleFunction.getGameId()).getName());
		//function.setFunctionName(enumFunctionService.findByEnumRole(roleFunction.getFunction()).getEnumName());
		return roleFunctionDao.save(function);
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
	public Page<RoleFunction> findRoleFunctionByCondition(Long userId,
			Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType);
		Specification<RoleFunction> spec = buildSpecification(userId, searchParams);
		return roleFunctionDao.findAll(spec, pageRequest);
	}
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize,
			String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "gameName");
		} else if ("id".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} else if ("role".equals(sortType)){
			sort = new Sort(Direction.DESC, "role");
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	
	
	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<RoleFunction> buildSpecification(Long userId,
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		User user = accountService.getUser(userId);
		ShiroUser u = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (!user.getRoles().equals(User.USER_ROLE_ADMIN)) {
			filters.put("gameId",new SearchFilter("gameId", Operator.EQ, u.storeId));
			filters.put("status", new SearchFilter("status", Operator.EQ,RoleFunction.STATUS_VALIDE));
		}
		filters.put("status", new SearchFilter("status", Operator.EQ,RoleFunction.STATUS_VALIDE));
		Specification<RoleFunction> spec = DynamicSpecifications.bySearchFilter(filters.values(), RoleFunction.class);
		return spec;
	}
}
