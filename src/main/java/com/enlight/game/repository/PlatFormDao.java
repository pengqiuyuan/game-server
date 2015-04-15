package com.enlight.game.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.enlight.game.entity.PlatForm;

public interface PlatFormDao extends PagingAndSortingRepository<PlatForm, Long>,JpaSpecificationExecutor<PlatForm>{
	
	PlatForm findByPfId(String PfId);
	
	PlatForm findByPfName(String PfName);
	
	@Modifying
	@Query("delete from PlatForm platForm where platForm.serverZoneId=?1")
	void deleteByServerZoneId(String serverZoneId);
	
	@Modifying
	@Query("from PlatForm platForm where platForm.status='1'")
	List<PlatForm> findAll();
	
	List<PlatForm> findByServerZoneId(String serverZoneId);
}
