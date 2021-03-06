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
	@Query("from RoleFunction roleFunction where roleFunction.gameId=?1 and roleFunction.role=?2 and roleFunction.status='1'")
	List<RoleFunction> findByGameIdAndRole(Long gameId,String role);
	
	
	/**
	 * 去重
	 * @param gameId
	 * @return
	 */
	@Modifying
	@Query("select distinct roleFunction.role from RoleFunction roleFunction where roleFunction.gameId=?1 and roleFunction.status='1'")
	List<RoleFunction> findByGameId(Long gameId);
	
	/**
	 * 去重
	 * @param gameId
	 * @return
	 */
	@Modifying
	@Query("select distinct roleFunction.role from RoleFunction roleFunction where roleFunction.gameId=?1 and roleFunction.status='1'")
	List<String> findByGameIdStr(Long gameId);
	
	@Modifying
	@Query("delete from RoleFunction roleFunction where roleFunction.gameId=?1")
	void delByStoreId(Long storeId);
	
	
	@Modifying
	@Query("from RoleFunction roleFunction where roleFunction.gameId=?1 and roleFunction.status='1'")
	List<RoleFunction> findByGmId(Long gameId);
	
}
