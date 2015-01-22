package com.enlight.game.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.enlight.game.entity.Role;
import com.enlight.game.entity.RoleFunction;

public interface RoleFunctionDao extends PagingAndSortingRepository<RoleFunction, Long>,JpaSpecificationExecutor<RoleFunction>{
	
	@Modifying
	@Query("from RoleFunction roleFunction where roleFunction.gameId=?1 and roleFunction.role=?2 and roleFunction.status='1' order by roleFunction.role asc")
	List<RoleFunction> findByGameIdAndRole(Long gameId,String role);
	
	@Modifying
	@Query("select roleFunction.function from RoleFunction roleFunction where roleFunction.gameId=?1 and roleFunction.role=?2 and roleFunction.status='1' order by roleFunction.role asc")
	List<String> findByGameIdAndRoleOnlyFunc(Long gameId,String role);
	
	/**
	 * 去重
	 * @param gameId
	 * @return
	 */
	@Modifying
	@Query("select distinct roleFunction.role from RoleFunction roleFunction where roleFunction.gameId=?1 and roleFunction.status='1' order by roleFunction.role asc")
	List<RoleFunction> findByGameId(Long gameId);
}
