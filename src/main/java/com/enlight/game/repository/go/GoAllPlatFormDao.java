package com.enlight.game.repository.go;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.enlight.game.entity.go.GoAllPlatForm;


public interface GoAllPlatFormDao extends PagingAndSortingRepository<GoAllPlatForm, Long>,JpaSpecificationExecutor<GoAllPlatForm>{
	
	@Modifying
	@Query("select distinct goAllPlatForm.platFormId from GoAllPlatForm goAllPlatForm where goAllPlatForm.storeId=?1 and goAllPlatForm.serverZoneId=?2")
	List<String> findPlatFormId(Integer storeId,Integer serverZoneId);
	
	@Modifying
	@Query("select distinct goAllPlatForm.serverId from GoAllPlatForm goAllPlatForm where goAllPlatForm.storeId=?1 and goAllPlatForm.serverZoneId=?2 and goAllPlatForm.platFormId=?3")
	List<String> findAllByPlatFormIdAndStoreIdAndServerZoneId(Integer storeId,Integer serverZoneId,String platFormId);
	
}
