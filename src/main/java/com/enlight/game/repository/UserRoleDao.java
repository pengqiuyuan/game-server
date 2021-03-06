package com.enlight.game.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.enlight.game.entity.UserRole;

public interface UserRoleDao extends PagingAndSortingRepository<UserRole, Long>,JpaSpecificationExecutor<UserRole>{
	
	@Modifying
	@Query("from UserRole userRole where userRole.userId =?1 and userRole.status='1' order by userRole.storeId ASC")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })  
	List<UserRole> findByUserId(Long userId);
	
	@Modifying
	@Query("from UserRole userRole where userRole.storeId =?1 and userRole.status='1'")
	List<UserRole> findByStoreId(Long storeId);
	
	@Modifying
	@Query("from UserRole userRole where userRole.userId =?1 and userRole.storeId =?2 and userRole.status='1'")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })  
	List<UserRole> findByUserIdAndStoreId(Long userId,Long storeId);
	
	@Modifying
	@Query("delete from UserRole userRole where userRole.storeId =?1 and userRole.userId =?2 and userRole.status='1'")
	void delByStoreId(Long storeId,Long userId);
	
	@Modifying
	@Query("delete from UserRole userRole where userRole.userId =?1 and userRole.status='1'")
	void delByUserId(Long userId);
	
	@Modifying
	@Query("from UserRole userRole where userRole.storeId =?1 and userRole.role =?2 and userRole.status='1'")
	List<UserRole> findByStoreIdAndRole(Long storeId,String role);
	
	@Modifying
	@Query("delete from UserRole userRole where userRole.storeId =?1 and userRole.role =?2 and userRole.status='1'")
	void delByStoreIdAndRole(Long storeId,String role);
	
	@Modifying
	@Query("delete from UserRole userRole where userRole.storeId =?1")
	void delByStoreId(Long storeId);
	
}
