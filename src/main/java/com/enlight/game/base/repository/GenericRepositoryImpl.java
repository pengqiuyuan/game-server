package com.enlight.game.base.repository;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class GenericRepositoryImpl<T, ID extends Serializable> extends
		SimpleJpaRepository<T, ID> implements GenericRepository<T, ID> {

	private final EntityManager em;

	public GenericRepositoryImpl(Class<T> domainClass, EntityManager em) {
		this(JpaEntityInformationSupport.getMetadata(domainClass, em), em);
	}

	public GenericRepositoryImpl(
			final JpaEntityInformation<T, ?> entityInformation,
			EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.em = entityManager;
	}


	

	@Override
	public void setQueryParams(Query query, Object[] queryParams) {
		if(null != queryParams && queryParams.length != 0){
			for(int i=0;i<queryParams.length;i++){
			query.setParameter(i+1, queryParams[i]);
			}
			}

	}


	@Override
	public String getEntityName(Class<T> entityClass) {
		String entityname = entityClass.getSimpleName();
		Entity entity = entityClass.getAnnotation(Entity.class);
		if(entity.name()!=null && !"".equals(entity.name())){
		entityname = entity.name();
		}
		return entityname;
	}

	@Override
	public Page<T> getScrollDataByJpql(String hql, String countHql,String whereJpql,
			String  orderby,
			Pageable pageable) {
		

        String sqlWhere = whereJpql==null? "": "where "+ whereJpql;
        Query query = em.createQuery(hql+sqlWhere+ orderby);
     
        if(pageable.getPageNumber()!=-1 && pageable.getPageSize()!=-1)
        query.setFirstResult(pageable.getPageNumber()*pageable.getPageSize()).setMaxResults(pageable.getPageSize());  
        List<T> content = query.getResultList();
        
  
        Query query1 = em.createQuery(countHql+sqlWhere);  
        Long total = (Long)query1.getSingleResult();
        Page<T> qr = new PageImpl<T>(content, pageable, total);
        
        return qr;
	}



}
