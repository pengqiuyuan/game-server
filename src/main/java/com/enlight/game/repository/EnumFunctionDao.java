package com.enlight.game.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.enlight.game.entity.EnumFunction;

public interface EnumFunctionDao extends PagingAndSortingRepository<EnumFunction, Long>,JpaSpecificationExecutor<EnumFunction>{
	
	@Modifying
	@Query("from EnumFunction enumFunction where enumFunction.status=1")
	List<EnumFunction> findList();
	
	EnumFunction findByEnumRole(String enumRole);
	
	Set<EnumFunction> findByCategoryId(String categoryId);
	
	@Modifying
	@Query("from EnumFunction enumFunction where enumFunction.status=1 And enumFunction.gameId=?1")
	Set<EnumFunction> findByGameId(String gameId);
	
	@Modifying
	@Query("from EnumFunction enumFunction where enumFunction.status=1 And enumFunction.gameId=?1 And enumFunction.categoryId=?2")
	Set<EnumFunction> findByGameIdAndCategoryId(String gameId,String categoryId);
	
	
}
