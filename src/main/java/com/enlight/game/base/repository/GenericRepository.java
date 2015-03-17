package com.enlight.game.base.repository;


import java.io.Serializable;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericRepository<T, ID extends Serializable> extends
		JpaRepository<T, ID> {

	/**
	 * 
	 * 设置query的参数
	 * 
	 * @param query
	 *            查询对象
	 * 
	 * @param queryParams
	 *            参数
	 */

	public void setQueryParams(Query query, Object[] queryParams);


	/**
	 * 
	 * 获取实体名
	 * 
	 * @param entityClass
	 * 
	 * @return
	 */

	public String getEntityName(Class<T> entityClass);

	/**
	 * 
	 * jpql语句查询
	 * 
	 * @param entityClass
	 * 
	 * @param whereSql
	 * 
	 * @param queryParams
	 * 
	 * @param orderby
	 * 
	 * @param pageable
	 * 
	 * @return
	 */

	public Page<T> getScrollDataByJpql(String hql, String countHql,String whereJpql,
			String  orderby,
			Pageable pageable);

}
