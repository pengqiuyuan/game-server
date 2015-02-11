package com.enlight.game.service.platForm;

import java.util.Date;
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

import com.enlight.game.entity.PlatForm;
import com.enlight.game.entity.User;
import com.enlight.game.repository.PlatFormDao;
import com.enlight.game.service.account.AccountService;

@Component
@Transactional
public class PlatFormService {

	@Autowired
	private PlatFormDao platFormDao;
	
	@Autowired
	private AccountService accountService;
	
	public PlatForm findById(Long id){
		return platFormDao.findOne(id);
	}
	
	public void save(PlatForm platForm){
		platForm.setCrDate(new Date());
		platForm.setUpdDate(new Date());
		platForm.setStatus(PlatForm.STATUS_VALIDE);
		platFormDao.save(platForm);
	}
	
	public void update(PlatForm platForm){
		PlatForm pf = platFormDao.findOne(platForm.getId());
		pf.setPfId(platForm.getPfId());
		pf.setPfName(platForm.getPfName());
		pf.setServerZoneId(platForm.getServerZoneId());
		pf.setUpdDate(new Date());
		platFormDao.save(pf);
	}
	
	public void delById(Long id){
		platFormDao.delete(id);
	}
	
	public void delByServerZoneId(String serverZoneId){
		platFormDao.deleteByServerZoneId(serverZoneId);
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
	public Page<PlatForm> findPlatFormByCondition(Long userId,
			Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType);
		Specification<PlatForm> spec = buildSpecification(userId, searchParams);
		return platFormDao.findAll(spec, pageRequest);
	}
	
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize,
			String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "updDate");
		} else if ("id".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		}else if ("platform".equals(sortType)) {
			sort = new Sort(Direction.DESC, "platform");
		}else if ("platformName".equals(sortType)) {
			sort = new Sort(Direction.DESC, "platformName");
		}
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	
	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<PlatForm> buildSpecification(Long userId,
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		User user = accountService.getUser(userId);
		//ShiroUser u = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (!user.getRoles().equals(User.USER_ROLE_ADMIN)) {
			filters.put("status", new SearchFilter("status", Operator.EQ,PlatForm.STATUS_VALIDE));
		}
		filters.put("status", new SearchFilter("status", Operator.EQ,PlatForm.STATUS_VALIDE));
		Specification<PlatForm> spec = DynamicSpecifications.bySearchFilter(filters.values(), PlatForm.class);
		return spec;
	}
}
