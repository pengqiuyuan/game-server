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
	List<GoAllServer> findAllByStoreIdAndServerZoneId(Integer storeId,Integer serverZoneId);
	
	@Modifying
	@Query("from GoAllServer")
	List<GoAllServer> findAll();
	
	GoAllServer findById(Integer Id);
	
	GoAllServer findByServerId(String serverId);
	
	@Modifying
	@Query("update GoAllServer goAllServer set goAllServer.status=?4 where goAllServer.storeId=?1 and goAllServer.serverZoneId=?2 and goAllServer.serverId=?3")
	void updateByStatus(Integer gameId,Integer serverZoneId,String serverId,String status);
}
