package com.enlight.game.repository.go;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.enlight.game.entity.go.GoAllServer;

public interface GoAllServerDao extends PagingAndSortingRepository<GoAllServer, Long>,JpaSpecificationExecutor<GoAllServer>{

	@Modifying
	@Query("from GoAllServer goAllServer where goAllServer.storeId=?1 and goAllServer.serverZoneId=?2")
	List<GoAllServer> findAllByPlatFormIdAndStoreIdAndServerZoneId(Integer storeId,Integer serverZoneId);
	
}
