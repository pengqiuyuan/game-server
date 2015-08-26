package com.enlight.game.repository.go;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.enlight.game.entity.go.GoServerZone;


public interface GoServerZoneDao extends PagingAndSortingRepository<GoServerZone, Long>,JpaSpecificationExecutor<GoServerZone>{
	
	@Modifying
	@Query("from GoServerZone")
	List<GoServerZone> findAll();
	
	GoServerZone findByServerZoneId(int serverZoneId);
	
}
