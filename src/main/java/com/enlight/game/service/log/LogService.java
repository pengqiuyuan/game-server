package com.enlight.game.service.log;

import java.util.Date;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
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

import com.enlight.game.entity.Log;
import com.enlight.game.repository.LogDao;
import com.enlight.game.service.account.ShiroDbRealm.ShiroUser;



//Spring Bean的标�?
@Component
//类中�?��public函数都纳入事务管理的标识.
@Transactional
/**
 * @Description 日志Service
 * @author Administrator
 *
 */
public class LogService {
	
	@Autowired
	private LogDao logDao;
	
	public void log(String crUser,String message,String type){
		
		Log log = new Log();
		log.setCrUser(crUser);
		log.setCrDate(new Date());
		log.setContent(message);
		log.setStatus(Log.STATUS_VALIDE);
		log.setType(type);
		logDao.save(log);
		
	}
	
	public Page<Log> findByCondition(Long memberId, Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Specification<Log> spec = buildSpecification(memberId, searchParams);
		return logDao.findAll(spec, pageRequest);
	}
	
	public Log findById(Long id){
		return logDao.findOne(id);
	}
	
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
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
	private Specification<Log> buildSpecification(Long memberId, Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);	
		Specification<Log> spec = DynamicSpecifications.bySearchFilter(filters.values(), Log.class);
		return spec;
	}
	/**
	 * 取出Shiro中的当前用户登入名
	 */
	public String getName() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.name;
	}
}
