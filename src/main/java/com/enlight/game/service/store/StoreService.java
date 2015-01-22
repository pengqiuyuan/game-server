package com.enlight.game.service.store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
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

import com.enlight.game.entity.Stores;
import com.enlight.game.entity.User;
import com.enlight.game.repository.StoreDao;
import com.enlight.game.repository.UserDao;
import com.enlight.game.service.account.AccountService;
import com.google.common.collect.ImmutableList;


@Component
@Transactional
/**
 * @Description 门店Service
 * @author Administrator
 *
 */
public class StoreService {

	@Autowired
	private StoreDao storeDao;

	@Autowired
	private AccountService accountService;

	@Autowired
	private UserDao userDao;

	/**
	 * 通过ID查询
	 * 
	 * @param id
	 * @return
	 */
	public Stores findById(long id) {
		return storeDao.findOne(id);
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
	public Page<Stores> findStoresByCondition(Long userId,
			Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType);
		Specification<Stores> spec = buildSpecification(userId, searchParams);
		return storeDao.findAll(spec, pageRequest);
	}

	/**
	 * api分页
	 * 
	 * @param searchParams
	 * @param pageNumber
	 * @param pageSize
	 * @param sortType
	 * @return
	 */
	public Page<Stores> findByApiCondition(Map<String, Object> searchParams,
			int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType);
		Specification<Stores> spec = buildApiSpecification(null, searchParams);

		return storeDao.findAll(spec, pageRequest);
	}

	/**
	 * 新增
	 * 
	 * @param store
	 */
	public void save(Stores store) {
		store.setStatus(Stores.STATUS_VALIDE);

		Stores stores = storeDao.findByMax();
		store.setSort(stores.getSort() + 1);
		storeDao.save(store);
	}

	/**
	 * 删除
	 * 
	 * @param store
	 */
	public void del(Stores store) {
		Stores store1 = this.findById(Long.valueOf(store.getId()));
		store1.setStatus(Stores.STATUS_INVALIDE);
		Set<User> users = store1.getUsers();
		for (User user : users) {
			user.setStatus(User.STATUS_INVALIDE);
			userDao.save(user);
		}
		/**
		 * 删除门店下的所有商品（伪）
		 */
		//productService.delByStoreId(store1.getId());

		storeDao.save(store1);
	}

	/**
	 * 修改
	 * 
	 * @param store
	 */
	public void update(Stores store) {
		Stores store1 = storeDao.findOne(store.getId());
		store1.setAddr(store.getAddr());
		store1.setName(store.getName());
		store1.setTel(store.getTel());
		store1.setThumb(store.getThumb());
		storeDao.save(store1);
	}

	/**
	 * 查询全部（有效）
	 * 
	 * @return
	 */
	public List<Stores> findList() {
		return storeDao.findList();
	}

	/**
	 * 通过用户的ID进行查询，用户角色相关，门店状态过虑
	 * 
	 * @param id
	 * @return
	 */
	public List<Stores> findListByUid(Long id) {
		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();

		User user = accountService.getUser(id);
		if (!user.getRoles().equals(User.USER_ROLE_ADMIN)
				&& !user.getRoles().equals(User.USER_ROLE_BUSINESS)) {
			filters.put("id",
					new SearchFilter("id", Operator.EQ, user.getStoreId()));
		}
		filters.put("status", new SearchFilter("status", Operator.EQ,
				Stores.STATUS_VALIDE));
		Specification<Stores> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), Stores.class);
		return storeDao.findAll(spec);
	}


	/**
	 * 更改排序字段
	 * 
	 * @param ids
	 * @param orders
	 */
	public void sort(int[] ids, int[] orders) {
		for (int i = 0; i < ids.length; i++) {
			Stores stores = storeDao.findOne(Long.valueOf(ids[i]));
			stores.setSort(orders[i]);
			storeDao.save(stores);
		}
	}

	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize,
			String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "sort");
		} else if ("createDate".equals(sortType)) {
			sort = new Sort(Direction.DESC, "createDate");
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<Stores> buildApiSpecification(Long userId,
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);

		filters.put("status", new SearchFilter("status", Operator.EQ,
				Stores.STATUS_VALIDE));

		Specification<Stores> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), Stores.class);
		return spec;
	}

	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<Stores> buildSpecification(Long userId,
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);

		User user = accountService.getUser(userId);
		if (!user.getRoles().equals(User.USER_ROLE_ADMIN) && !user.getRoles().equals(User.USER_ROLE_BUSINESS)) {
			List<String> storeIds  = ImmutableList.copyOf(StringUtils.split(user.getStoreId(), ","));
			for (String id : storeIds) {
				filters.put("id",new SearchFilter("id", Operator.EQ, Long.parseLong(id)));
			}
		}
		filters.put("status", new SearchFilter("status", Operator.EQ,Stores.STATUS_VALIDE));

		Specification<Stores> spec = DynamicSpecifications.bySearchFilter(filters.values(), Stores.class);
		return spec;
	}

}
