package com.enlight.game.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.enlight.game.entity.EnumCategory;

public interface EnumCategoryDao extends PagingAndSortingRepository<EnumCategory, Long>,JpaSpecificationExecutor<EnumCategory>{

	@Modifying
	@Query("from EnumCategory enumCategory where enumCategory.status=1")
	List<EnumCategory> findAll();
	
	@Modifying
	@Query("from EnumCategory enumCategory where enumCategory.status=1 And enumCategory.id !=10002")
	List<EnumCategory> findAllNotCount();
	
}
