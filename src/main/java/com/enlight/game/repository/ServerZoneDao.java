package com.enlight.game.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.enlight.game.entity.ServerZone;

public interface ServerZoneDao extends PagingAndSortingRepository<ServerZone, Long>,JpaSpecificationExecutor<ServerZone> {
	
	@Modifying
	@Query("from ServerZone serverZone where serverZone.status='1'")
	List<ServerZone> findAll();
	
	ServerZone findByServerName(String serverName);
}
