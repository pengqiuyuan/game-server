package com.enlight.game.service.user;

import java.util.Date;
import java.util.Map;

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

import com.enlight.game.entity.User;
import com.enlight.game.repository.UserDao;
import com.enlight.game.service.account.AccountService;


@Component
@Transactional
/**
 * @Description 后台用户Service
 * @author Administrator
 *
 */
public class UserService {

	@Autowired
	private UserDao userDao ;

	@Autowired
	private AccountService accountService;

	/**
	 * 分页查询
	 * @param userId
	 * @param searchParams
	 * @param pageNumber
	 * @param pageSize
	 * @param sortType
	 * @return
	 */
	public Page<User> findTenanciesByCondition(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Specification<User> spec = buildSpecification(userId, searchParams);
		return userDao.findAll(spec, pageRequest);
	}


	/**
	 * 通过ID查询
	 * @param id
	 * @return
	 */
	public User findById(long id){
		return userDao.findOne(id);
	}


	/**
	 * 添加
	 * @param user
	 */
	public void add(User user){
		user.setRegisterDate(new Date());
		user.setUpdDate(new Date());
		userDao.save(user);
	}
	/**
	 * 修改
	 * @param user
	 */
	public void update(User user){
		User user1 = this.findById(user.getId());
		user1.setStoreId(user.getStoreId());
		user1.setEmail(user.getEmail());
		user1.setName(user.getName());
		user1.setRoles(user.getRoles());
		user1.setStatus(user.getStatus());
		user1.setUpdDate(user.getUpdDate());
		user1.setServerZone(user.getServerZone());
		userDao.save(user1);
	}

	/**
	 * 删除（伪）
	 * @param user
	 */
	public void del(User user){
		User user1 = this.findById(user.getId());
		user1.setRoles("");
		user1.setStatus(User.STATUS_INVALIDE);
		userDao.save(user1);
	}
	/**
	 * 删除（真）
	 * @param user
	 */
	public void realDel(User user){
		User user1 = this.findById(user.getId());
		userDao.delete(user1);
	}

	/**
	 * 用户激活
	 * @param user
	 */
	public void active(User user){
		User user1 = this.findById(user.getId());
		user1.setStatus(User.STATUS_VALIDE);
		userDao.save(user1);
	}

	/**
	 * 用户是否唯一
	 * @param loginName
	 * @return
	 */
	public boolean isOnly(String loginName){
	  User user = userDao.findByLoginName(loginName);
	  return user == null ? true:false;
	}

	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} else if ("registerDate".equals(sortType)) {
			sort = new Sort(Direction.DESC, "registerDate");
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<User> buildSpecification(Long userId, Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		User user = accountService.getUser(userId);
		if(!user.getRoles().equals(User.USER_ROLE_ADMIN))
		{
			filters.put("id",new SearchFilter("id", Operator.EQ, userId));
		}
		Specification<User> spec = DynamicSpecifications.bySearchFilter(filters.values(), User.class);
		return spec;
	}


}
