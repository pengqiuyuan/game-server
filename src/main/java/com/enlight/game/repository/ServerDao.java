package com.enlight.game.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.enlight.game.entity.Server;

public interface ServerDao extends PagingAndSortingRepository<Server, Long>,JpaSpecificationExecutor<Server>{

	Server findByServerId(String serverId);
	
	@Modifying
	@Query("delete from Server server where server.storeId=?1")
	void deleteByStoreId(String storeId);
	
	@Modifying
	@Query("delete from Server server where server.serverZoneId=?1")
	void deleteByServerZoneId(String serverZoneId);
	
	Set<Server> findByServerZoneIdAndStoreId(String serverZoneId,String storeId);
	
	@Modifying
	@Query("from Server server where server.status='1'")
	List<Server> findAll();
	
	Set<Server> findByStoreId(String storeId);
}
