package com.enlight.game.service.giftProps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.persistence.SearchFilter.Operator;

import com.enlight.game.entity.GiftProps;
import com.enlight.game.entity.Tag;
import com.enlight.game.entity.User;
import com.enlight.game.repository.GiftPropsDao;
import com.enlight.game.service.account.AccountService;

@Component
@Transactional
public class GiftPropsService {
	
	@Autowired
	private GiftPropsDao giftPropsDao;
	
	@Autowired
	private AccountService accountService;
	
	public void save(GiftProps giftProps){
		giftPropsDao.save(giftProps);
	}
	
	public void del(Long giftPropsId){
		giftPropsDao.delete(giftPropsId);
	}
	
	public List<GiftProps> findByGameId(String gameId){
		return giftPropsDao.findByGameId(gameId);
	}
	
	public void update(Tag tag){
		GiftProps p = giftPropsDao.findByItemId(tag.getTagId());
		p.setUpdDate(new Date());
		p.setItemName(tag.getTagName());
		giftPropsDao.save(p);
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
	public Page<GiftProps> findGiftPropsCondition(Long userId,
			Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType);
		Specification<GiftProps> spec = buildSpecification(userId, searchParams);
		User user = accountService.getUser(userId);
		return user.getRoles().equals(User.USER_ROLE_ADMIN)?giftPropsDao.findAll(spec, pageRequest):new PageImpl<GiftProps>(new ArrayList<GiftProps>());
	}
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize,
			String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} else if ("crDate".equals(sortType)) {
			sort = new Sort(Direction.DESC, "crDate");
		}
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	
	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<GiftProps> buildSpecification(Long userId,
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("status", new SearchFilter("status", Operator.EQ,GiftProps.STATUS_VALIDE));
		Specification<GiftProps> spec = DynamicSpecifications.bySearchFilter(filters.values(), GiftProps.class);
		return spec;
	}
	
	
}
