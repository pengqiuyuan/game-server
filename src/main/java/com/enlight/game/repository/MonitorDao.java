package com.enlight.game.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.enlight.game.entity.Monitor;


public interface MonitorDao extends PagingAndSortingRepository<Monitor, Long>,JpaSpecificationExecutor<Monitor>{
	
	Monitor findByStoreIdAndMonitorKeyAndEql(String storeId, String monitorKey , String eql);	

	@Modifying
	@Query("from Monitor monitor")
	List<Monitor> findAll();
}
